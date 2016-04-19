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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.User;
import edu.monash.merc.service.BlockIPService;
import edu.monash.merc.util.encrypt.MD5;

/**
 * PasswordAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("user.passwordAction")
public class PasswordAction extends DMBaseAction {

	// dummy action id for security protection.
	private long actUId = -1;

	// user uid hash code
	private String usrIdCode;

	// dummy action name for security protection.
	private String act;

	// dummy reset password hashcode for security protection
	private String hashCd;

	// re-enter new password
	private String rePassword;

	private String securityCode;

	@Autowired
	private BlockIPService blockIPService;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void setBlockIPService(BlockIPService blockIPService) {
		this.blockIPService = blockIPService;
	}

	public String forgotPassword() {

		try {
			// security code error. just return immediately, not go further.
			if (isSecurityCodeError(securityCode)) {
				addFieldError("securityCode", getText("security.code.invalid"));
				return INPUT;
			}

			User foundUser = this.userService.getUserByNameEmail(user.getFirstName(), user.getLastName(), user.getEmail());
			// can find the user in the database
			if (foundUser == null) {
				addActionError(getText("forgot.password.user.name.or.email.invalid"));
				return INPUT;
			}

			// user account is inactive
			if (!foundUser.isActivated()) {
				addActionError(getText("forgot.password.user.account.inactive"));
				return INPUT;
			}
			if (foundUser.getPassword().equals("ldap")) {
				addActionError(getText("forgot.password.cannot.reset.ldap.account"));
				return INPUT;
			}

			String displayName = foundUser.getDisplayName();
			String userFullName = user.getFirstName() + " " + user.getLastName();

			// user first name and last name is not the same first name and last name as the registered
			if (!StringUtils.equals(userFullName, displayName)) {
				addActionError(getText("forgot.password.user.name.or.email.invalid"));
				return INPUT;
			}

			String resetPasswdCode = generateSecurityHash(foundUser.getEmail());
			foundUser.setResetPasswdHashCode(resetPasswdCode);

			this.userService.updateUser(foundUser);

			// construct a reset password url for email
			String resetPwdUrl = constructResetPwdUrl(foundUser.getId(), foundUser.getUidHashCode(), foundUser.getResetPasswdHashCode());
			// site name
			String serverQName = getServerQName();
			// start to send an email to user
			sendResetPasswdEmailToUser(serverQName, foundUser.getEmail(), resetPwdUrl);
			// set action finished messsage
			addActionMessage(getText("forgot.password.request.for.reset.password.finished.msg", new String[] { displayName }));
			// set the page title and nav label
			setNavBarAndTitleForForgotPwd();
		} catch (Exception e) {
			logger.error(e.getMessage());
			addActionError(getText("forgot.password.request.for.reset.password.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	private String constructResetPwdUrl(long actUsrId, String uidCode, String hashCode) {
		String serverQName = getServerQName();
		String appcontext = getAppContextPath();
		String pkName = "user";
		String actionName = "verifyPwdReset.jspx?";
		String actNamePair = "act=" + ActionConts.RESET_PWD_ACTION_NAME;
		String actIdPair = "&actUId=" + actUsrId;
		String idCodePair = "&usrIdCode=" + uidCode;
		String hashCodePair = "&hashCd=" + hashCode;
		StringBuffer resetPwdUrl = new StringBuffer();
		// application root url
		resetPwdUrl.append(serverQName).append(appcontext).append(ActionConts.URL_PATH_DEIM);
		// action name
		resetPwdUrl.append(pkName).append(ActionConts.URL_PATH_DEIM).append(actionName);
		// actId, idcode, act name and hash code
		resetPwdUrl.append(actNamePair).append(actIdPair).append(idCodePair).append(hashCodePair);
		return new String(resetPwdUrl).trim();
	}

	private void sendResetPasswdEmailToUser(String serverQName, String userEmail, String resetPasswdURL) {
		String resetPasswdMailTemplateFile = "resetPasswordEmailTemplate.ftl";
		// prepare to send email.
		String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
		String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_SERVICE_EMAIL);
		String subject = getText("forgot.password.request.for.reset.password.mail.title");

		Map<String, String> templateMap = new HashMap<String, String>();
		templateMap.put("userEmail", userEmail);
		templateMap.put("resetPasswdURL", resetPasswdURL);
		templateMap.put("SiteName", serverQName);
		templateMap.put("AppName", appName);
		// send an email to user
		this.dmService.sendMail(adminEmail, userEmail, subject, templateMap, resetPasswdMailTemplateFile, true);
	}

	private void setNavBarAndTitleForForgotPwd() {
		setPageTitle(getText("forgot.password.action.title"));
		String secondNav = getText("forgot.password.action.title");
		String secondNavLink = "user/user_request_resetpwd";
		navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
	}

	public String verifyPasswdReset() {
		// set title and navigation bar
		setNavBarAndTitleForResetPwd();
		try {
			if (verifyResetPwdLink()) {
				addActionError(getText("reset.password.url.link.invalid"));
				return INPUT;
			}
			user = this.userService.getUserById(actUId);
			if (user == null) {
				// User doesn't existed
				addActionError(getText("reset.password.url.link.invalid"));
				return INPUT;
			}

			if (user.getResetPasswdHashCode() == null) {
				// The reset password link has been expired
				addActionError(getText("reset.password.url.link.expired"));
				return INPUT;
			}

			if (user.getResetPasswdHashCode() != null && (!user.getResetPasswdHashCode().equals(hashCd))) {
				addActionError(getText("reset.password.url.link.invalid"));
				return INPUT;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			addActionError(getText("reset.password.url.link.verify.error"));
			return ERROR;
		}
		return SUCCESS;
	}

	private boolean verifyResetPwdLink() {

		if ((actUId <= 0) || (act == null) || (usrIdCode == null) || (hashCd == null)) {
			return true;
		}
		if (act != null && (!act.equals(ActionConts.RESET_PWD_ACTION_NAME))) {
			return true;
		}
		return false;
	}

	public String resetPassword() {
		if (checkResetPasswdErrors()) {
			return INPUT;
		}
		try {
			User usr = this.userService.getUserById(user.getId());
			// can't find user, means the reset password link has been expired
			if (usr == null) {
				addActionError("reset.password.user.account.information.invalid");
				return INPUT;
			}
			// reset password hash code is null, means the reset password link has been expired
			if (usr.getResetPasswdHashCode() == null) {
				// The reset password link has been expired
				addActionError(getText("reset.password.user.account.information.expired"));
				return INPUT;
			}

			if (usr.getResetPasswdHashCode() != null && (!usr.getResetPasswdHashCode().equals(user.getResetPasswdHashCode()))) {
				addActionError(getText("reset.password.user.account.information.expired"));
				return INPUT;
			}
			//
			usr.setPassword(MD5.hash(user.getPassword()));
			usr.setResetPasswdHashCode(null);
			this.userService.updateUser(usr);
			// sign a persistent User
			user = usr;
			// find any previous blocked ip, if find, just remove it.
			String ipAddress = request.getRemoteAddr();
			this.blockIPService.deleteBlockIPByIp(ipAddress);
			// set action finished messsage
			addActionMessage(getText("reset.password.new.password.reset.success.msg", new String[] { user.getDisplayName() }));
			// set page title and navigation label
			setNavBarAndTitleForResetPwd();
		} catch (Exception e) {
			logger.error(e.getMessage());
			addActionError(getText("reset.password.new.password.reset.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	private void setNavBarAndTitleForResetPwd() {
		setPageTitle(getText("reset.password.action.title"));
		String secondNav = getText("reset.password.action.title");
		navBar = createNavBar("User", null, secondNav, null, null, null);
	}

	private boolean checkResetPasswdErrors() {
		boolean hasError = false;
		if (!user.getPassword().equals(rePassword)) {
			addFieldError("password", getText("reset.password.two.entered.passwords.not.same"));
			hasError = true;
		}

		if (isSecurityCodeError(securityCode)) {
			addFieldError("securityCode", getText("security.code.invalid"));
			hasError = true;
		}
		return hasError;
	}

	public long getActUId() {
		return actUId;
	}

	public void setActUId(long actUId) {
		this.actUId = actUId;
	}

	public String getUsrIdCode() {
		return usrIdCode;
	}

	public void setUsrIdCode(String usrIdCode) {
		this.usrIdCode = usrIdCode;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getHashCd() {
		return hashCd;
	}

	public void setHashCd(String hashCd) {
		this.hashCd = hashCd;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
}
