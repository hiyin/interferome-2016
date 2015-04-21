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

import au.com.bytecode.opencsv.CSVReader;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.TFSite;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.dto.TFSiteBean;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.util.MercUtil;
import edu.monash.merc.util.csv.CSVTFSiteGenerator;
import edu.monash.merc.util.tfsite.TFSiteManager;
import edu.monash.merc.util.csv.TFSiteColumn;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ImportReporterAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.importTFSiteAction")
public class ImportTFSiteAction extends DMBaseAction {

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private boolean sendMailRequired;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showImportTFSite() {
        try {
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("tfsite.show.import.permission.denied"));
                return ERROR;
            }

            if (TFSiteManager.findImportProcess(TFSiteManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("tfsite.import.working.in.progress"));
                return INPUT;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tfsite.import.show.importing.page.failed"));
            return ERROR;
        }

        return SUCCESS;
    }

    public String importTFSite() {
        try {
            //get the current user, only the admins who can import the tfsite into system
            user = getCurrentUser();

            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("tfsite.import.permission.denied"));
                return ERROR;
            }
            if (TFSiteManager.findImportProcess(TFSiteManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("tfsite.import.working.in.progress"));
                return INPUT;
            }
            //start to upload the tf site file
            FileInputStream fis = null;
            fis = new FileInputStream(upload);
            //get all tf site from the file
            List<TFSite> tfSites = generateTFSites(fis);
            //create tf site bean
            TFSiteBean tfSiteBean = createTFSiteBean(user, tfSites);
            //call the import tf site service
            this.dmService.importTFSite(tfSiteBean);
            //set the success message
            String successMsg = getText("tfsite.import.start.success.msg");
            if (sendMailRequired) {
                successMsg = getText("tfsite.import.start.success.with.mail.msg");
            }
            setSuccessActMsg(successMsg);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tfsite.import.failed.to.start.msg") + " " + ex.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    private List<TFSite> generateTFSites(FileInputStream fileInputStream) {
        CSVReader csvReader = null;
        List<TFSite> tfSiteList = new ArrayList<TFSite>();
        try {
            csvReader = new CSVReader(new InputStreamReader(fileInputStream));
            String[] columnsLine = csvReader.readNext();

            //normalize the column names
            for (int i = 0; i < columnsLine.length; i++) {
                columnsLine[i] = MercUtil.replaceSpace(columnsLine[i]);
            }

            String[] columnValuesLine;
            while ((columnValuesLine = csvReader.readNext()) != null) {
                TFSite tfSite = new TFSite();
                if (columnValuesLine.length == columnsLine.length) {
                    CSVTFSiteGenerator tfSiteGenerator = new CSVTFSiteGenerator();

                    for (int i = 0; i < columnsLine.length; i++) {
                        tfSiteGenerator.getColumns().add(new TFSiteColumn(columnsLine[i], columnValuesLine[i]));
                    }
                    tfSite = tfSiteGenerator.genTFSite();
                    tfSiteList.add(tfSite);
                }
            }
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception fex) {
                //ignore whatever caught
            }
        }
        return tfSiteList;
    }

    private TFSiteBean createTFSiteBean(User user, List<TFSite> tfSites) {
        TFSiteBean tfSiteBean = new TFSiteBean();
        String serverQName = getServerQName();
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL);
        tfSiteBean.setServerName(serverQName);
        tfSiteBean.setAppName(appName);
        tfSiteBean.setSendMailRequired(sendMailRequired);
        tfSiteBean.setFromMail(adminEmail);
        tfSiteBean.setUser(user);
        tfSiteBean.setToMail(user.getEmail());
        tfSiteBean.setTfSiteName(this.uploadFileName);
        tfSiteBean.setTfSites(tfSites);
        return tfSiteBean;
    }

    public void validateImportTFSite() {
        if (StringUtils.isBlank(getUploadFileName())) {
            addFieldError("uploadFileName", getText("tfsite.import.file.name.must.be.provided"));
            //call get user
            postProcess();
            return;
        }
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".csv"))) {
            addFieldError("fileFormatError", getText("tfsite.import.file.format.invalid"));
            //call get user
            postProcess();
            return;
        }
    }

    private void postProcess() {
        try {
            user = getCurrentUser();
        } catch (Exception ex) {
            logger.error(ex);
            addFieldError("userInfo", getText("tfsite.import.get.user.info.failed"));
        }
    }


    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public boolean isSendMailRequired() {
        return sendMailRequired;
    }

    public void setSendMailRequired(boolean sendMailRequired) {
        this.sendMailRequired = sendMailRequired;
    }

}
