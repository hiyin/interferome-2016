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

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;

/**
 * AdminAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("admin.adminAction")
public class AdminAction extends DMBaseAction {

	private User regUser;

	private Profile regUsrProfile;

	private long regUid;

	private String activationHashCode;

	private String actionId;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public String verifyActivationLink() {
		// set navbar and action title
		setNavBarActionTitle();
		try {
			// check the activation info
			if (checkActivationLinkInfo()) {
				addActionError(getText("admin.activate.account.link.invalid"));
				return ERROR;
			}
			// get current logged in user
			user = getCurrentUser();
			if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
				addActionError(getText("admin.activate.account.permission.denied"));
				return ERROR;
			}

			regUser = this.userService.getUserById(regUid);
			if (regUser == null) {
				addActionError(getText("admin.activate.account.link.invalid"));
				return ERROR;
			}

			if (regUser.isActivated()) {
				addActionError(getText("admin.activate.account.link.expired"));
				return ERROR;
			}
			if (regUser.isRejected()) {
				addActionError(getText("admin.activate.account.link.expired"));
				return ERROR;
			}

			if (regUser.getActivationHashCode() != null && (!regUser.getActivationHashCode().equals(activationHashCode))) {
				addActionError(getText("admin.activate.account.link.invalid"));
				return ERROR;
			}
			regUsrProfile = regUser.getProfile();

		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("admin.activate.account.link.check.error"));
			return ERROR;
		}

		return SUCCESS;
	}

	public boolean checkActivationLinkInfo() {
		if ((regUid <= 0) || (actionId == null) || (activationHashCode == null)) {
			return true;
		}
		if (actionId != null && (!actionId.equals(ActionConts.ACTIVATION_ACTION_NAME))) {
			return true;
		}
		return false;
	}

	private void setNavBarActionTitle() {
		setPageTitle(getText("admin.activate.user.account.action.title"));
		String secondNav = getText("admin.activate.user.account.action.title");
		navBar = createNavBar("User", null, secondNav, null, null, null);
	}

	public String activateUserAccount() {
		// set navigation bar and page title
		setNavBarActionTitle();
		try {
			if (checkRegUserInfo()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			// check the admin permission
			user = getCurrentUser();
			if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
				addActionError(getText("admin.activate.account.permission.denied"));
				return ERROR;
			}

			User checkedUser = this.userService.getUserById(regUser.getId());

			if (checkedUser == null) {
				// User doesn't existed
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			if (checkedUser.isActivated()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}

			if (checkedUser.isRejected()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}

			if (checkedUser.getActivationHashCode() != null && (!checkedUser.getActivationHashCode().equals(regUser.getActivationHashCode()))) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			if (checkedUser.getEmail() != null && (!checkedUser.getEmail().equals(regUser.getEmail()))) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}

			checkedUser.setActivated(true);
			this.userService.updateUser(checkedUser);
			// set action finished messsage
			sendApprovalAccountEmail(checkedUser.getDisplayName(), checkedUser.getEmail(), regUsrProfile.getOrganization());
			setSuccessActMsg(getText("admin.activate.account.success.msg", new String[] { checkedUser.getEmail() }));
		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("admin.activate.account.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	private boolean checkRegUserInfo() {
		if (regUser == null) {
			return true;
		}
		if (regUser.getId() <= 0 || regUser.getActivationHashCode() == null || regUser.getEmail() == null || regUsrProfile.getOrganization() == null) {
			return true;
		}
		return false;
	}

	public String rejectUserAccount() {
		// set navigation bar and page title
		setNavBarActionTitle();
		try {
			if (checkRegUserInfo()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			// check the admin permission
			user = getCurrentUser();
			if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
				addActionError(getText("admin.manage.user.account.permission.denied"));
				return ERROR;
			}

			User checkedUser = this.userService.getUserById(regUser.getId());

			if (checkedUser == null) {
				// User doesn't existed
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			if (checkedUser.isActivated()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}

			if (checkedUser.isRejected()) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}

			if (checkedUser.getActivationHashCode() != null && (!checkedUser.getActivationHashCode().equals(regUser.getActivationHashCode()))) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			if (checkedUser.getEmail() != null && (!checkedUser.getEmail().equals(regUser.getEmail()))) {
				addActionError(getText("admin.activate.account.info.invalid"));
				return ERROR;
			}
			checkedUser.setRejected(true);
			this.userService.updateUser(checkedUser);
			sendRejectEmailToUser(checkedUser.getDisplayName(), checkedUser.getEmail(), regUsrProfile.getOrganization());
			setSuccessActMsg(getText("admin.reject.account.success.msg", new String[] { checkedUser.getEmail() }));
		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("admin.activate.account.reject.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	public User getRegUser() {
		return regUser;
	}

	public void setRegUser(User regUser) {
		this.regUser = regUser;
	}

	public Profile getRegUsrProfile() {
		return regUsrProfile;
	}

	public void setRegUsrProfile(Profile regUsrProfile) {
		this.regUsrProfile = regUsrProfile;
	}

	public long getRegUid() {
		return regUid;
	}

	public void setRegUid(long regUid) {
		this.regUid = regUid;
	}

	public String getActivationHashCode() {
		return activationHashCode;
	}

	public void setActivationHashCode(String activationHashCode) {
		this.activationHashCode = activationHashCode;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

}
