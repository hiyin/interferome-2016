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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;

/**
 * ManageUserAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("admin.manageUserAction")
public class ManageUserAction extends DMBaseAction {

	private User regUser;

	private Profile profile;

	private String manageType;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public String showUserDetails() {
		try {
			user = getCurrentUser();
			regUser = this.userService.getUserById(regUser.getId());
			profile = regUser.getProfile();
			if (profile != null) {
				// normalize the interests and contact details format
				convertNlToBr();
			}
		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("admin.manage.get.user.details.error"));
			return ERROR;
		}
		return SUCCESS;
	}

	public String manageUser() {
		try {
			user = getCurrentUser();
			if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
				addActionError(getText("admin.manage.user.account.permission.denied"));
				return ERROR;
			}

			if (StringUtils.isBlank(manageType)) {
				addActionError(getText("admin.manage.user.action.type.not.specified"));
				return ERROR;
			}

			regUser = this.userService.getUserById(regUser.getId());
			profile = regUser.getProfile();
			if (profile != null) {
				// normalize the interests and contact details format
				convertNlToBr();
			}

			if (manageType.equals(ActionConts.UserManageType.ACTIVATE.manageType())) {
				if (!regUser.isActivated()) {
					regUser.setActivated(true);
					regUser.setRejected(false);
					this.userService.updateUser(regUser);
					sendApprovalAccountEmail(regUser.getDisplayName(), regUser.getEmail(), regUser.getProfile().getOrganization());
					// set action successful message
					setSuccessActMsg(getText("admin.manage.user.success.message",
							new String[] { ("Activated " + regUser.getDisplayName() + " user account") }));
				}
			}
			if (manageType.equals(ActionConts.UserManageType.DEACTIVATE.manageType())) {
				if (regUser.isActivated()) {
					regUser.setActivated(false);
					regUser.setRejected(false);
					this.userService.updateUser(regUser);
					sendRejectEmailToUser(regUser.getDisplayName(), regUser.getEmail(), regUser.getProfile().getOrganization());
					// set action successful message
					setSuccessActMsg(getText("admin.manage.user.success.message",
							new String[] { ("Deactivated " + regUser.getDisplayName() + " user account") }));
				}
			}

			if (manageType.equals(ActionConts.UserManageType.SETASADMIN.manageType())) {
				if (regUser.getUserType() != UserType.ADMIN.code()) {
					regUser.setUserType(UserType.ADMIN.code());
					this.userService.updateUser(regUser);
					// set action successful message
					setSuccessActMsg(getText("admin.manage.user.success.message",
							new String[] { ("Set " + regUser.getDisplayName() + " user as an admin") }));
				}

			}
			if (manageType.equals(ActionConts.UserManageType.SETASUSER.manageType())) {
				if (regUser.getUserType() == UserType.ADMIN.code()) {
					regUser.setUserType(UserType.REGUSER.code());
					this.userService.updateUser(regUser);
					// set action successful message
					setSuccessActMsg(getText("admin.manage.user.success.message",
							new String[] { ("Set " + regUser.getDisplayName() + " user as a normal user") }));
				}
			}

		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("admin.manage.user.account.failed"));
		}
		return SUCCESS;
	}

	private void convertNlToBr() {
		if (!StringUtils.isBlank(profile.getContactDetails())) {
			String contactDetails = newLineToBr(profile.getContactDetails());
			profile.setContactDetails(contactDetails);
		}
		if (!StringUtils.isBlank(profile.getInterests())) {
			String interests = newLineToBr(profile.getInterests());
			profile.setInterests(interests);
		}
	}

	public User getRegUser() {
		return regUser;
	}

	public void setRegUser(User regUser) {
		this.regUser = regUser;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}
}
