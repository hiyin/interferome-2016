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
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;

import edu.monash.merc.common.service.ldap.LDAPService;
import edu.monash.merc.dto.LDAPUser;
import edu.monash.merc.exception.DCConfigException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.Avatar;
import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.util.encrypt.MD5;

/**
 * HomeAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("interferome.homeAction")
public class HomeAction extends BaseAction {

    private String appName;

    protected static User allRegUser;

    protected static User anonymous;

    protected static boolean superAdminExisted;

    @Autowired
    private LDAPService ldapService;

    public void setLdapService(LDAPService ldapService) {
        this.ldapService = ldapService;
    }

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String home() {
        appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        return SUCCESS;
    }

    @PostConstruct
    public void appInit() {
        try {
            appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
            // System.out.println("initializing the system ...");
            if (allRegUser == null) {
                User regUser = this.userService.getVirtualUser(UserType.ALLREGUSER.code());
                if (regUser == null) {
                    regUser = new User();
                    regUser.setActivated(true);
                    regUser.setFirstName("registeredUser");
                    regUser.setLastName(appName);
                    regUser.setDisplayName(regUser.getFirstName() + " " + regUser.getLastName());
                    regUser.setRegistedDate(GregorianCalendar.getInstance().getTime());
                    String email = "registeredUser@" + appName;
                    regUser.setEmail(email);
                    regUser.setUniqueId(email);
                    // set user type as all-registered user
                    regUser.setUserType(UserType.ALLREGUSER.code());

                    // create a default profile for regUser (a virtual user).
                    Profile regp = new Profile();
                    regp.setGender("Male");
                    regp.setOrganization(appName);
                    regUser.setProfile(regp);
                    regp.setUser(regUser);

                    Avatar avatar = new Avatar();
                    avatar.setFileName("avatar" + File.separator + "male.png");
                    avatar.setFileType("png");
                    regUser.setAvatar(avatar);
                    avatar.setUser(regUser);

                    // save the virtual all-registered-user
                    this.userService.saveUser(regUser);
                    allRegUser = regUser;
                }
            }

            if (anonymous == null) {
                User anonymousUser = this.userService.getVirtualUser(UserType.ANONYMOUS.code());
                if (anonymousUser == null) {
                    anonymousUser = new User();
                    anonymousUser.setActivated(true);
                    anonymousUser.setFirstName("anonymousUser");
                    anonymousUser.setLastName(appName);
                    anonymousUser.setDisplayName(anonymousUser.getFirstName() + " " + anonymousUser.getLastName());
                    anonymousUser.setRegistedDate(GregorianCalendar.getInstance().getTime());
                    String email = "anonymousUser@" + appName;
                    anonymousUser.setEmail(email);
                    anonymousUser.setUniqueId(email);
                    // set user type as anonymous user
                    anonymousUser.setUserType(UserType.ANONYMOUS.code());

                    // create a default profile for anonymous user (a virtual user).
                    Profile anonyp = new Profile();
                    anonyp.setGender("Male");
                    anonyp.setOrganization(appName);
                    anonymousUser.setProfile(anonyp);
                    anonyp.setUser(anonymousUser);

                    Avatar avatar = new Avatar();
                    avatar.setFileName("avatar" + File.separator + "male.png");
                    avatar.setFileType("png");
                    anonymousUser.setAvatar(avatar);
                    avatar.setUser(anonymousUser);
                    this.userService.saveUser(anonymousUser);
                    anonymous = anonymousUser;
                }
            }

            if (!superAdminExisted) {
                // setup superadmin
                User superAdmin = this.userService.getVirtualUser(UserType.SUPERADMIN.code());
                if (superAdmin == null) {
                    String gender = "Male";
                    superAdmin = new User();
                    superAdmin.setActivated(true);
                    superAdmin.setUserType(UserType.SUPERADMIN.code());
                    superAdmin.setRegistedDate(GregorianCalendar.getInstance().getTime());

                    // if the super admin account is authenticated by LDAP, then we just put the password as ldap,
                    // otherwise, we have to hash the password with MD5
                    String password = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_PWD);
                    String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL);
                    if (StringUtils.equalsIgnoreCase(password, "ldap")) {
                        LDAPUser ldapUser = this.ldapService.searchLdapUser(adminEmail);
                        if (ldapUser == null) {
                            throw new DCConfigException("System administrator doesn't exist in the LDAP Server");
                        }
                        gender = ldapUser.getGender();
                        superAdmin.setPassword("ldap");
                        superAdmin.setDisplayName(ldapUser.getDisplayName());
                        superAdmin.setFirstName(ldapUser.getFirstName());
                        superAdmin.setLastName(ldapUser.getLastName());
                        superAdmin.setEmail(adminEmail);
                        superAdmin.setUniqueId(ldapUser.getUid());
                    } else {
                        superAdmin.setEmail(adminEmail);
                        superAdmin.setUniqueId(appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL));
                        superAdmin.setPassword(MD5.hash(password));
                        superAdmin.setDisplayName(appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_NAME));
                    }

                    // profile
                    Profile adminProf = new Profile();
                    adminProf.setGender(gender);
                    adminProf.setOrganization(appName);
                    superAdmin.setProfile(adminProf);
                    adminProf.setUser(superAdmin);

                    // avatar
                    Avatar adminAvatar = new Avatar();
                    String avatarFile = "avatar" + File.separator + "male.png";
                    if (StringUtils.equalsIgnoreCase(gender, "male")) {
                        avatarFile = "avatar" + File.separator + "male.png";
                    } else if (StringUtils.equalsIgnoreCase(gender, "female")) {
                        avatarFile = "avatar" + File.separator + "female.png";
                    } else {
                        avatarFile = "avatar" + File.separator + "male.png";
                    }

                    adminAvatar.setFileName(avatarFile);
                    adminAvatar.setFileType("png");
                    superAdmin.setAvatar(adminAvatar);
                    adminAvatar.setUser(superAdmin);
                    this.userService.saveUser(superAdmin);
                    superAdminExisted = true;
                }
            }
        } catch (Exception e) {
            logger.error("System initialization error: " + e);
        }
        // Sys
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
