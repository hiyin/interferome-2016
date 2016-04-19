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

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ContactUsAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("site.submitDataAction")
public class SubmitDataAction extends DMBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private String subject;

    private String message;

    private String securityCode;

    private File upload;

    public String showSubmitData() {
        try {
            user = getCurrentUser();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("interferome.site.show.submitdata.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String submitData() {
        try {
            setPageTitle(getText("interferome.submitdata.action.title"));
            user = getCurrentUser();
            if (checkInputs()) {
                return INPUT;
            }
            //Copy File to Server
            if(upload != null) {
                if(!uploadFile()){
                    addActionError(getText("interferome.site.submit.submitdata.uploadfailed.msg"));
                    return ERROR;
                }
            }
            //send the contact details to admin
            sendSubmitDataMailToAdmin();
            //set the action success message
            setSuccessActMsg(getText("interferome.site.submit.submitdata.success.msg", new String[]{contactName}));
            //reset the inputs
            resetInputs();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("interferome.site.submit.submitdata.success.msg"));
            return ERROR;
        }
        return SUCCESS;
    }

    private boolean uploadFile(){
        //start to upload the tf site file
        try{

            //TODO: change the file location (same as the ImageCropAction.java), store the metadata of the uploaded file into DB
            String filePath = getText("data.upload.directory");
            System.out.println(filePath);
            FileUtils.copyFileToDirectory(upload, new File(filePath));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    private void sendSubmitDataMailToAdmin() {
        // site name
        String serverQName = getServerQName();

        //mail template
        String activateEmailTemplateFile = "submitDataMailTemplate.ftl";
        //application name
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        // prepare to send email.
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_SERVICE_EMAIL);

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("Subject", subject);
        templateMap.put("UserName", contactName);
        templateMap.put("UserEmail", contactEmail);
        if (StringUtils.isBlank(contactPhone)) {
            contactPhone = " ";
        }
        templateMap.put("UserPhone", contactPhone);
        templateMap.put("Message", message);

        templateMap.put("SiteName", serverQName);
        templateMap.put("AppName", appName);

        this.dmService.sendMail(contactEmail, adminEmail, subject, templateMap, activateEmailTemplateFile, true);
    }

    private void resetInputs() {
        this.contactName = null;
        this.contactEmail = null;
        this.contactPhone = null;
        this.subject = null;
        this.message = null;
        this.securityCode = null;
    }

    private boolean checkInputs() {
        boolean hasError = false;
        if (StringUtils.isBlank(contactName)) {
            addFieldError("contactName", getText("interferome.submitdata.name.required"));
            hasError = true;
        }

        if (StringUtils.isBlank(contactEmail)) {
            addFieldError("contactEmail", getText("interferome.submitdata.email.required"));
            hasError = true;
        }

        if (!MercUtil.validateEmail(contactEmail)) {
            addFieldError("contactEmail", getText("interferome.submitdata.email.invalid"));
            hasError = true;
        }

        if (StringUtils.isBlank(subject)) {
            addFieldError("subject", getText("interferome.submitdata.subject.required"));
            hasError = true;
        }

        if (StringUtils.isBlank(message)) {
            addFieldError("message", getText("interferome.submitdata.message.required"));
            hasError = true;
        }

        if (isSecurityCodeError(securityCode)) {
            addFieldError("securityCode", getText("security.code.invalid"));
            hasError = true;
        }

        return hasError;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
