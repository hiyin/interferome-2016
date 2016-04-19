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

import edu.monash.merc.domain.User;
import edu.monash.merc.util.encrypt.MD5;

/**
 * ChangePasswdAction Action class provides changing user's password functionality
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("manage.changePasswdAction")
public class ChangePasswdAction extends DMBaseAction {

    /**
     * User password *
     */
    private String password;

    /**
     * re-entered new password *
     */
    private String rePassword;

    /**
     * Security code *
     */
    private String securityCode;

    /**
     * Logger object *
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Show change user password page.
     *
     * @return A String represents success or not.
     */
    public String showChangePwd() {
        try {
            user = getCurrentUser();
            if (StringUtils.equals(user.getPassword(), "ldap")) {
                addActionError(getText("change.password.ldap.user.password.not.supported"));
                setNavAndTitle();
                return ERROR;
            }
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("change.password.show.change.password.page.failed"));
            setNavAndTitle();
            return ERROR;
        }
        return SUCCESS;
    }

    private void setNavAndTitle() {
        setPageTitle(getText("change.password.action.title"));
        String secondNav = getText("change.password.action.title");
        String secondNavLink = "manage/showChangePwd.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
    }

    /**
     * Change user password
     *
     * @return A String represents success or not.
     */
    public String changePassword() {
        try {
            boolean hasError = false;
            User foundUsr = getCurrentUser();
            String md5pwd = foundUsr.getPassword();
            String md5newpwd = MD5.hash(user.getPassword());

            if (!StringUtils.equals(md5pwd, md5newpwd)) {
                addFieldError("currentPassword", getText("change.password.current.password.incorrect"));
                hasError = true;
            }

            if (!StringUtils.equals(password, rePassword)) {
                addFieldError("repassword", getText("change.password.two.entered.password.not.same"));
                hasError = true;
            }
            if (isSecurityCodeError(securityCode)) {
                addFieldError("securityCode", getText("security.code.invalid"));
                hasError = true;
            }
            if (hasError) {
                return INPUT;
            }
            foundUsr.setPassword(MD5.hash(password));
            this.userService.updateUser(foundUsr);
            user = foundUsr;
            setSuccessActMsg(getText("change.password.new.password.success.msg", new String[]{user.getDisplayName()}));
            // set navigation bar and title after the password is changed
            setNavAndTitle();
        } catch (Exception e) {
            logger.error(e.getMessage());
            addActionError(getText("change.password.action.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
