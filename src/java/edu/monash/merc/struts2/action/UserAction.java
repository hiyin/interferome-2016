/*
 * Copyright (c) 2010-2011, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.monash.merc.struts2.action;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import edu.monash.merc.common.service.ldap.LDAPService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.Avatar;
import edu.monash.merc.domain.BlockIP;
import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.dto.LDAPUser;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.service.BlockIPService;
import edu.monash.merc.util.encrypt.MD5;

/**
 * UserAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("user.userAction")
public class UserAction extends DMBaseAction {

	private String organization;

	private String securityCode;

	@Autowired
	private LDAPService ldapService;

	@Autowired
	private BlockIPService blockIPService;

	private String loginTryMsg;

	private String appName;

	private String requestUrl;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public String register() {
		try {
			// if errors existed
			if (checkRegInfo()) {
				return INPUT;
			}
			// encrypt the user password
			user.setPassword(MD5.hash(user.getPassword()));
			// set the registed date
			user.setRegistedDate(GregorianCalendar.getInstance().getTime());
			// set the user email as a unique id
			user.setUniqueId(user.getEmail());
			// set the unique id hash code.
			user.setUidHashCode(generateSecurityHash(user.getEmail()));
			// set the activate hash code
			user.setActivationHashCode(generateSecurityHash(user.getEmail()));
			// set the user active into false
			user.setActivated(false);
			// set user type as a registered user.
			user.setUserType(UserType.REGUSER.code());

			// create a default user profile.
			Profile p = genProfile();
			p.setOrganization(organization);
			user.setProfile(p);
			p.setUser(user);

			// create an avatar
			Avatar avatar = genAvatar(p.getGender());
			user.setAvatar(avatar);
			avatar.setUser(user);

			// save user
			this.userService.saveUser(user);

			String serverQName = getServerQName();
			// start to send register email to admin for approval
			String activateURL = constructActivationURL(serverQName, user.getId(), user.getActivationHashCode());
			sendRegMailToAdmin(serverQName, user.getDisplayName(), user.getEmail(), organization, activateURL);

		} catch (Exception e) {
			logger.error(e.getMessage());
			// reponse the action error
			addActionError(getText("user.register.failed"));
			return ERROR;
		}
		// set action finished messsage
		addActionMessage(getText("user.register.finished.msg", new String[] { user.getDisplayName() }));
		// set action success info
		setRegSuccessInfo();
		return SUCCESS;
	}

	private boolean checkRegInfo() {
		boolean hasErrors = false;
		try {
			if (isSecurityCodeError(securityCode)) {
				addFieldError("securityCode", getText("security.code.invalid"));
				hasErrors = true;
			}
			// duplicated email is not allowed
			boolean emailExisted = userService.checkEmailExisted(user.getEmail());
			if (emailExisted) {
				addFieldError("email", getText("user.register.email.already.registered"));
				hasErrors = true;
			}
			// set the display name
			user.setDisplayName(user.getFirstName() + " " + user.getLastName());
			// duplicated user display name is not allowed
			boolean displayNameExisted = userService.checkUserDisplayNameExisted(user.getDisplayName());
			if (displayNameExisted) {
				addFieldError("displayName", getText("user.register.name.already.registed"));
				hasErrors = true;
			}
		} catch (Exception e) {
			logger.error("checking the user registration failed, " + e);
			addFieldError("checkAccount", getText("user.register.check.user.from.db.failed"));
			hasErrors = true;
		}
		return hasErrors;
	}

	public String registerLdapUser() {
		LDAPUser ldapUsr = null;
		try {
			// if validate ldap user failed, just return to the ldap user registration page.
			ldapUsr = checkLdapUserInfo();
			if (ldapUsr == null) {
				return INPUT;
			}
			// try to register ldap user in the database
			user.setDisplayName(ldapUsr.getDisplayName());
			user.setFirstName(ldapUsr.getFirstName());
			user.setLastName(ldapUsr.getLastName());
			// set ldap user password as ldap
			user.setPassword("ldap");
			user.setRegistedDate(GregorianCalendar.getInstance().getTime());
			user.setUidHashCode(generateSecurityHash(user.getUniqueId()));
			// set user email which get from ldap server
			user.setEmail(ldapUsr.getMail());
			user.setActivationHashCode(generateSecurityHash(user.getUniqueId()));

			user.setActivated(false);
			user.setUserType(UserType.REGUSER.code());

			// create a default user profile.
			Profile p = genProfile();
			p.setOrganization("Monash University");

			p.setGender(ldapUsr.getGender());
			user.setProfile(p);
			p.setUser(user);
			// create an avatar
			Avatar avatar = genAvatar(p.getGender());
			avatar.setUser(user);
			user.setAvatar(avatar);

			this.userService.saveUser(user);
			// site name
			String serverQName = getServerQName();
			// start to send register email to admin for approval
			String activateURL = constructActivationURL(serverQName, user.getId(), user.getActivationHashCode());
			sendRegMailToAdmin(serverQName, user.getDisplayName(), user.getEmail(), p.getOrganization(), activateURL);

		} catch (Exception e) {
			// log the database error
			logger.error(e.getMessage());
			// reponse the action error
			addActionError(getText("user.register.failed"));
			return ERROR;
		}
		// set action finished messsage
		addActionMessage(getText("user.register.finished.msg", new String[] { user.getDisplayName() }));
		// set action success info
		setLdapRegSuccessInfo();
		return SUCCESS;
	}

	public LDAPUser checkLdapUserInfo() {
		LDAPUser ldapUsr = null;

		if (isSecurityCodeError(securityCode)) {
			addFieldError("securityCode", getText("security.code.invalid"));
			return null;
		}

		try {
			ldapUsr = this.ldapService.validateLdapUser(user.getUniqueId(), user.getPassword());
			if (ldapUsr == null) {
				addFieldError("idorpasswd", getText("user.register.ldap.invalid.authcateId.or.password"));
				return null;
			}
		} catch (Exception e) {
			logger.error(e);
			addFieldError("ldaperror", getText("user.register.ldap.check.ldap.account.failed"));
			return null;
		}

		try {
			boolean existed = this.userService.checkUserUniqueIdExisted(user.getUniqueId());
			if (existed) {
				addFieldError("authcateid", getText("user.register.ldap.authcate.id.already.registed"));
				// set ldap user to null
				logger.error("user authcate id is already registered in the system.");
				return null;
			} else {
				boolean emailRegistered = this.userService.checkEmailExisted(ldapUsr.getMail());
				if (emailRegistered) {
					addFieldError("email", getText("user.register.ldap.email.already.registed"));
					logger.error("user email is already registered in the system.");
					return null;
				}
			}
		} catch (Exception e) {
			addFieldError("checkdb", getText("user.register.ldap.check.db.account.failed"));
			logger.error(e);
			ldapUsr = null;
		}

		return ldapUsr;
	}

	public String showLogin() {
		// try to remove any previous session values.
		removeFromSession(ActionConts.SESS_AUTHENTICATION_FLAG);
		removeFromSession(ActionConts.SESS_AUTHEN_USER_ID);
		removeFromSession(ActionConts.SESS_AUTHEN_USER_NAME);
		setNavBarForLogin();
		try {
			// checkBlockIPInfo();
			// first of all, get the request ip address
			String ipAddress = request.getRemoteAddr();
			long currentRequestedTime = System.currentTimeMillis();

			String defaultTryStr = appSetting.getPropValue(AppPropSettings.ALLOW_LOGIN_TRY_TIMES);
			String defaultBlockedTimeStr = appSetting.getPropValue(AppPropSettings.LOGIN_IP_BLOCK_WAITING_TIMES);
			int defaultTryTimes = Integer.valueOf(defaultTryStr).intValue();
			int defaultBlockedTimes = Integer.valueOf(defaultBlockedTimeStr).intValue();
			// find wheather there is a previous blocked ip info or not.
			BlockIP blockedIp = findBlockIp(ipAddress);
			// just do updating check for the blocked ip
			BlockIP updatedBlockedIp = checkPreviousBlockIp(blockedIp, currentRequestedTime, defaultBlockedTimes);

			// if no previous blocked ip, just display the how many times an user can try to login
			if (updatedBlockedIp == null) {
				loginTryMsg = getText("user.login.try.times", new String[] { String.valueOf(defaultTryTimes) });
				return SUCCESS;
			}
			// if previous blocked ip existed, then if the try times permits are more than the default value, we just
			// display the message to an user
			int triedTimes = updatedBlockedIp.getTryTimes();
			if (triedTimes == defaultTryTimes) {
				loginTryMsg = getText("user.login.try.too.manay.times", new String[] { String.valueOf(defaultBlockedTimes) });
			} else {
				loginTryMsg = getText("user.login.try.times", new String[] { String.valueOf(defaultTryTimes - triedTimes) });
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("user.login.check.blockip.failed"));
			return ERROR;
		}
	}

	private void setNavBarForLogin() {
		setPageTitle(getText("user.login.action.title"));
		String secondNav = getText("user.login.action.title");
		String secondNavLink = "user/showLogin.jspx";
		navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
	}

	// ====
	private BlockIP findBlockIp(String ipAddress) {
		try {
			return this.blockIPService.getBlockIPByIp(ipAddress);
		} catch (Exception e) {
			throw new DCException("Failed to check the blocked ip status, " + e);
		}
	}

	private BlockIP checkPreviousBlockIp(BlockIP blockIp, long requestedTime, long defaultBlockedTimes) {
		try {
			if (blockIp != null) {
				long previousBlockedTime = blockIp.getBlockTimes();
				if (requestedTime >= (previousBlockedTime + defaultBlockedTimes * 60 * 1000)) {
					removeBlockIp(blockIp.getIp());
					return null;
				}
			}
		} catch (Exception e) {
			throw new DCException("Failed to update previous blocked ip status, " + e);
		}
		return blockIp;
	}

	private BlockIP updateBlockIp(String ipAddress, long requestedTime, int defaultTryTimes, int defaultBlockTimes) {
		try {
			BlockIP blockIp = this.blockIPService.getBlockIPByIp(ipAddress);
			if (blockIp == null) {
				blockIp = new BlockIP();
				blockIp.setBlockTimes(System.currentTimeMillis());
				blockIp.setIp(ipAddress);
				blockIp.setTryTimes(1);
				this.blockIPService.saveBlockIP(blockIp);
				return blockIp;
			} else {
				// already has a ip block, just update it.
				int triedTimes = blockIp.getTryTimes();
				int currentTriedTimes = triedTimes + 1;

				if (currentTriedTimes >= defaultTryTimes) {
					blockIp.setTryTimes(defaultTryTimes);
				} else {
					blockIp.setTryTimes(currentTriedTimes);
				}
				this.blockIPService.updateBlockIP(blockIp);
				return blockIp;
			}
		} catch (Exception e) {
			throw new DCException("Failed to update blocked ip status, " + e);
		}
	}

	private void removeBlockIp(String blockIpAddress) {
		try {
			this.blockIPService.deleteBlockIPByIp(blockIpAddress);
		} catch (Exception e) {
			throw new DCException("Failed to remove the blocked ip, " + e);
		}
	}

	public String logout() {
        cleanupSession();
        return SUCCESS;

    }

	private void cleanupSession() {
		removeFromSession(ActionConts.SESS_AUTHENTICATION_FLAG);
		removeFromSession(ActionConts.SESS_AUTHEN_USER_ID);
		removeFromSession(ActionConts.SESS_AUTHEN_USER_NAME);
		removeFromSession(ActionConts.REQUEST_URL);
        removeFromSession(ActionConts.SEARCH_CON_KEY);
	}

	public String login() {
		// set action title
		setNavBarForLogin();
		try {
			// first of all, get the request ip address
			String ipAddress = request.getRemoteAddr();

			long requestedTime = System.currentTimeMillis();
			String defaultTryStr = appSetting.getPropValue(AppPropSettings.ALLOW_LOGIN_TRY_TIMES);
			String defaultBlockedTimeStr = appSetting.getPropValue(AppPropSettings.LOGIN_IP_BLOCK_WAITING_TIMES);

			int defaultTryTimes = Integer.valueOf(defaultTryStr).intValue();
			int defaultBlockedTimes = Integer.valueOf(defaultBlockedTimeStr).intValue();

			// find wheather there is a previous blocked ip info or not.
			BlockIP blockedIp = findBlockIp(ipAddress);
			// just do updating check for the blocked ip
			BlockIP updatedBlockedIp = checkPreviousBlockIp(blockedIp, requestedTime, defaultBlockedTimes);

			if (updatedBlockedIp != null && (updatedBlockedIp.getTryTimes() == defaultTryTimes)) {
				loginTryMsg = getText("user.login.try.too.manay.times", new String[] { String.valueOf(defaultBlockedTimes) });
				return INPUT;
			}

			// found some errors, then update the ip block
			if (validateLoginInputs()) {
				updatedBlockedIp = updateBlockIp(ipAddress, requestedTime, defaultTryTimes, defaultBlockedTimes);
				long alreadyTriedTimes = updatedBlockedIp.getTryTimes();
				if (alreadyTriedTimes == defaultTryTimes) {
					loginTryMsg = getText("user.login.try.too.manay.times", new String[] { String.valueOf(defaultBlockedTimes) });
				} else {
					loginTryMsg = getText("user.login.try.times", new String[] { String.valueOf(defaultTryTimes - updatedBlockedIp.getTryTimes()) });
				}
				return INPUT;
			}

			String ldapSupportedStr = appSetting.getPropValue(AppPropSettings.LDAP_AUTH_SUPPORTED);
			boolean ldapsupported = Boolean.valueOf(ldapSupportedStr);

			User loginUser = validateUserLogin(user.getUniqueId(), user.getPassword(), ldapsupported);
			// can't find user login info
			if (loginUser == null) {
				updatedBlockedIp = updateBlockIp(ipAddress, requestedTime, defaultTryTimes, defaultBlockedTimes);
				long alreadyTriedTimes = updatedBlockedIp.getTryTimes();
				if (alreadyTriedTimes == defaultTryTimes) {
					loginTryMsg = getText("user.login.try.too.manay.times", new String[] { String.valueOf(defaultBlockedTimes) });
				} else {
					loginTryMsg = getText("user.login.try.times", new String[] { String.valueOf(defaultTryTimes - updatedBlockedIp.getTryTimes()) });
				}
				addFieldError("userIdorPassword", getText("user.login.invalid.user.id.password"));
				return INPUT;
			}
			// try to remove any blocked ip address if login successfully.
			if (updatedBlockedIp != null) {
				removeBlockIp(ipAddress);
			}
			user = loginUser;
			storeInSession(ActionConts.SESS_AUTHENTICATION_FLAG, ActionConts.SESS_AUTHENCATED);
			storeInSession(ActionConts.SESS_AUTHEN_USER_ID, user.getId());
			storeInSession(ActionConts.SESS_AUTHEN_USER_NAME, user.getDisplayName());
			requestUrl = (String) findFromSession(ActionConts.REQUEST_URL);

			// populate the application name in success logged in page to display the application name
			appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);

			// remove the requested url.
			removeFromSession(ActionConts.REQUEST_URL);
			if (StringUtils.isBlank(requestUrl)) {
				requestUrl = ActionConts.DISPLAY_USER_HOME_ACTION;
			}

		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("user.login.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	private User validateUserLogin(String uniqueId, String password, boolean ldapSupported) {
		try {
			User existedUser = this.userService.checkUserLogin(uniqueId, MD5.hash(password));
			if (existedUser != null) {
				// user existed and account is activated already, just return user, otherwis return null.
				if (existedUser.isActivated()) {
					return existedUser;
				} else {
					return null;
				}
			}
			if (ldapSupported) {
				boolean success = this.ldapService.ldapUserLogin(uniqueId, password);
				if (success) {
					// if an user is a ldap user, then get the registered user from db.
					return this.userService.getUserByUnigueId(uniqueId);
				} else {
					return null;
				}
			} else {// not found from db, and ldap not supported, just return null.
				return null;
			}
		} catch (Exception e) {
			logger.error("failed to validate user login, " + e);
			return null;
		}
	}

	private boolean validateLoginInputs() {
		boolean errors = false;
		if (user.getUniqueId() == null || (user.getUniqueId().trim().equals(""))) {
			addFieldError("uniqueId", getText("user.login.unique.id.required"));
			errors = true;
		}
		if (user.getPassword() == null || (user.getPassword().trim().equals(""))) {
			addFieldError("password", getText("user.login.password.required"));
			errors = true;
		}
		if (securityCode == null || (securityCode.trim().equals(""))) {
			addFieldError("securityCode", getText("security.code.required"));
			errors = true;
		} else {
			if (isSecurityCodeError(securityCode)) {
				addFieldError("securityCode", getText("security.code.invalid"));
				errors = true;
			}
		}
		return errors;
	}

	private Profile genProfile() {
		// create a default user profile.
		return new Profile();
	}

	private Avatar genAvatar(String maleOrFemale) {
		Avatar avatar = new Avatar();
		String avatarFile = null;
		if (StringUtils.isBlank(maleOrFemale)) {
			avatarFile = "avatar" + File.separator + "male.png";
		} else {
			if (StringUtils.equalsIgnoreCase(maleOrFemale, "male")) {
				avatarFile = "avatar" + File.separator + "male.png";
			} else if (StringUtils.equalsIgnoreCase(maleOrFemale, "female")) {
				avatarFile = "avatar" + File.separator + "female.png";
			} else {
				avatarFile = "avatar" + File.separator + "male.png";
			}
		}
		avatar.setFileName(avatarFile);
		avatar.setFileType("png");
		return avatar;
	}

	private String constructActivationURL(String serverQName, long userId, String activationCode) {

		String appcontext = getAppContextPath();

		String pkNamespace = "admin";
		String actionName = "verifyAccount.jspx?";
		String actNamePair = "actionId=" + ActionConts.ACTIVATION_ACTION_NAME;
		String regUidPair = "&regUid=" + userId;

		String hashCodePair = "&activationHashCode=" + activationCode;

		StringBuffer activationURL = new StringBuffer();
		// application root url
		activationURL.append(serverQName).append(appcontext).append(ActionConts.URL_PATH_DEIM);
		// action name
		activationURL.append(pkNamespace).append(ActionConts.URL_PATH_DEIM).append(actionName);
		// actId, idcode, act name and hash code
		activationURL.append(actNamePair).append(regUidPair).append(hashCodePair);

		return new String(activationURL).trim();
	}

	private void sendRegMailToAdmin(String serverQName, String userName, String userEmail, String organization, String activationURL) {

		String activateEmailTemplateFile = "activateAccountTemplate.ftl";
		String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
		// prepare to send email.
		String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_SERVICE_EMAIL);
		String subject = getText("user.register.account.activation.request.mail.title");

		Map<String, String> templateMap = new HashMap<String, String>();
		templateMap.put("RegisteredUser", userName);
		templateMap.put("UserEmail", userEmail);
		templateMap.put("Organization", organization);
		templateMap.put("ActivationURL", activationURL);
		templateMap.put("SiteName", serverQName);
		templateMap.put("AppName", appName);

		this.dmService.sendMail(adminEmail, adminEmail, subject, templateMap, activateEmailTemplateFile, true);
	}

	private void setRegSuccessInfo() {
		String secondNav = getText("user.register.options.title");
		String secondLink = "user/register_options";
		String thirdNav = getText("user.self.register.action.title");
		String thirdLink = "user/user_register";
		setPageTitle(secondNav);
		navBar = createNavBar("User", null, secondNav, secondLink, thirdNav, thirdLink);
	}

	private void setLdapRegSuccessInfo() {
		String secondNav = getText("user.register.options.title");
		String secondLink = "user/register_options";
		String thirdNav = getText("user.ldap.register.action.title");
		String thirdLink = "user/ldap_user_register";
		setPageTitle(secondNav);
		navBar = createNavBar("User", null, secondNav, secondLink, thirdNav, thirdLink);
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getLoginTryMsg() {
		return loginTryMsg;
	}

	public void setLoginTryMsg(String loginTryMsg) {
		this.loginTryMsg = loginTryMsg;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public void setLdapService(LDAPService ldapService) {
		this.ldapService = ldapService;
	}

	public void setBlockIPService(BlockIPService blockIPService) {
		this.blockIPService = blockIPService;
	}

}
