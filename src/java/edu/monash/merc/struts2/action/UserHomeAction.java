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

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.config.ApplicationPropertyConfigurater;
import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.Avatar;
import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UserHomeAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("manage.userHomeAction")
public class UserHomeAction extends DMBaseAction {

    private Pagination<AuditEvent> eventPagination;

    private long totalPermRequests;

    private Profile profile;

    protected static Map<String, String> countryMap = new LinkedHashMap<String, String>();

    protected static Map<String, String> genderMap = new LinkedHashMap<String, String>();

    @Autowired
    @Qualifier("countryConfigurer")
    private ApplicationPropertyConfigurater countryPropertyConfigurer;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void propInit() {
        setGenderMap();
        setCountries();
    }

    public String userHome() {
        try {
            user = getCurrentUser();
            // profile = this.dmService.getUserProfile(user.getId());
            profile = user.getProfile();
            //get total permissions request if any
            totalPermRequests = this.dmService.getTotalPermRequests(user.getId());
            //get events by page
            eventPagination = this.dmService.getEventByUserId(user.getId(), 1, 10, orderByDescTime("createdTime"));
            // post processing for populating the gender and countries
            setNavBarForUserHomeSuccess();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("failed.to.show.user.home"));
            setNavBarForUserHomeFailure();
            return ERROR;
        }
        return SUCCESS;
    }

    public String showProfile() {
        try {
            user = getCurrentUser();
            profile = user.getProfile();
            // convert the newline to break symble for user interests and contact details.
            convertNlToBr();
        } catch (Exception e) {
            addActionError(getText("failed.to.get.user.profile.details"));
            setNavBarForShowProfileFailure();
            return ERROR;
        }
        return SUCCESS;
    }

    public String showProfileUpdate() {
        try {
            user = getCurrentUser();
            profile = user.getProfile();
            // post processing for populating the gender and countries
            postProcess();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("failed.to.get.user.profile.details"));
            setNavBarForShowProfileFailure();
            return ERROR;
        }
        return SUCCESS;
    }

    public String updateProfile() {
        try {
            User currentUser = getCurrentUser();
            Profile oldProfile = currentUser.getProfile();
            Avatar avatar = currentUser.getAvatar();

            // try to update the avatar based on the gender which an user selected
            if (StringUtils.isNotBlank(profile.getGender()) && (!oldProfile.getGender().equalsIgnoreCase(profile.getGender()))) {
                if (!avatar.isCustomized()) {
                    String avatarFile = null;
                    if (profile.getGender().equalsIgnoreCase("male")) {
                        avatarFile = "avatar" + File.separator + "male.png";
                    } else {
                        avatarFile = "avatar" + File.separator + "female.png";
                    }
                    avatar.setFileName(avatarFile);
                }
            }

            // start to update the profile.
            oldProfile.setGender(profile.getGender());

            if (StringUtils.isNotBlank(profile.getAddress())) {
                oldProfile.setAddress(profile.getAddress());
            }
            if (StringUtils.isNotBlank(profile.getCity())) {
                oldProfile.setCity(profile.getCity());
            }
            if (StringUtils.isNotBlank(profile.getState())) {
                oldProfile.setState(profile.getState());
            }
            if (StringUtils.isNotBlank(profile.getPostcode())) {
                oldProfile.setPostcode(profile.getPostcode());
            }
            if (StringUtils.isNotBlank(profile.getContactDetails())) {
                oldProfile.setContactDetails(profile.getContactDetails());
            }
            if (StringUtils.isNotBlank(profile.getCountry())) {
                oldProfile.setCountry(profile.getCountry());
            }

            if (StringUtils.isNotBlank(profile.getOccupation())) {
                oldProfile.setOccupation(profile.getOccupation());
            }
            if (StringUtils.isNotBlank(profile.getIndustryField())) {
                oldProfile.setIndustryField(profile.getIndustryField());
            }
            if (StringUtils.isNotBlank(profile.getInterests())) {
                oldProfile.setInterests(profile.getInterests());
            }
            if (StringUtils.isNotBlank(profile.getOrganization())) {
                oldProfile.setOrganization(profile.getOrganization());
            }

            if (StringUtils.isNotBlank(user.getFirstName())) {
                currentUser.setFirstName(user.getFirstName());
            }

            if (StringUtils.isNotBlank(user.getLastName())) {
                currentUser.setLastName(user.getLastName());
            }

            if (StringUtils.isNotBlank(user.getFirstName()) && StringUtils.isNotBlank(user.getLastName())) {
                currentUser.setDisplayName(user.getFirstName() + " " + user.getLastName());
            }

            currentUser.setAvatar(avatar);
            currentUser.setProfile(oldProfile);
            // update the user and profile.
            this.userService.updateUser(currentUser);
            user = currentUser;
            profile = oldProfile;
            storeInSession(ActionConts.SESS_AUTHEN_USER_NAME, user.getDisplayName());
            // convert the newline to break symble for user interests and contact details.
            convertNlToBr();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("failed.to.update.user.profile"));
            return ERROR;
        }
        return SUCCESS;
    }

    public void validateUpdateProfile() {

        if (!MercUtil.notGTFixedLength(profile.getContactDetails(), 1000)) {
            addFieldError("contactDetails", getText("profile.contact.details.length.too.long"));
        }

        if (!MercUtil.notGTFixedLength(profile.getInterests(), 1000)) {
            addFieldError("interests", getText("profile.interests.length.too.long"));
        }
        if (StringUtils.isBlank(user.getFirstName())) {
            addFieldError("firstName", getText("user.register.first.name.required"));
        } else {
            if (!MercUtil.notGTFixedLength(user.getFirstName(), 10)) {
                addFieldError("fistNameSpec", getText("user.register.first.name.spec"));
            }
        }
        if (StringUtils.isBlank(user.getLastName())) {
            addFieldError("lastName", getText("user.register.last.name.required"));
        } else {
            if (!MercUtil.notGTFixedLength(user.getLastName(), 10)) {
                addFieldError("lastNameSpec", getText("user.register.last.name.spec"));
            }
        }
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

    private void postProcess() {
        if (StringUtils.isBlank(profile.getGender())) {
            profile.setGender("Male");
        }
        if (StringUtils.isBlank(profile.getCountry())) {
            profile.setCountry("AU");
        }
    }

    private void setGenderMap() {
        if (genderMap == null || genderMap.size() == 0) {
            genderMap.put("Male", "Male");
            genderMap.put("Female", "Female");
        }
    }

    private void setCountries() {
        if (countryMap == null || countryMap.size() == 0) {
            Map<String, String> cMp = this.countryPropertyConfigurer.getResolvedProps();
            countryMap = MercUtil.sortByValue(cMp);
        }
    }

    private void setNavBarForUserHomeSuccess() {
        String secondNav = getText("user.home.action.title");
        setPageTitle(secondNav);
        String secondNavLink = "manage/userHome.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
    }

    private void setNavBarForUserHomeFailure() {
        String secondNav = getText("user.home.action.title");
        setPageTitle(secondNav + " - Error");
        String secondNavLink = "user/userHome.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
    }

    private void setNavBarForShowProfileFailure() {
        String secondNav = getText("user.profile.action.title");
        setPageTitle(secondNav + " - Error");
        String secondNavLink = "manage/showProfile.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
    }

    public Map<String, String> getCountryMap() {
        return UserHomeAction.countryMap;
    }

    public void setCountryMap(Map<String, String> countryMap) {
        UserHomeAction.countryMap = countryMap;
    }

    public Map<String, String> getGenderMap() {
        return UserHomeAction.genderMap;
    }

    public void setGenderMap(Map<String, String> genderMap) {
        UserHomeAction.genderMap = genderMap;
    }

    public void setCountryPropertyConfigurer(ApplicationPropertyConfigurater countryPropertyConfigurer) {
        this.countryPropertyConfigurer = countryPropertyConfigurer;
    }

    protected OrderBy[] orderByDescTime(String orderName) {
        return new OrderBy[]{OrderBy.desc(orderName)};
    }

    public Pagination<AuditEvent> getEventPagination() {
        return eventPagination;
    }

    public void setEventPagination(Pagination<AuditEvent> eventPagination) {
        this.eventPagination = eventPagination;
    }

    public long getTotalPermRequests() {
        return totalPermRequests;
    }

    public void setTotalPermRequests(long totalPermRequests) {
        this.totalPermRequests = totalPermRequests;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
