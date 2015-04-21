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
import edu.monash.merc.domain.Reporter;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.dto.ReporterBean;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.util.MercUtil;
import edu.monash.merc.util.csv.CSVRepGenerator;
import edu.monash.merc.util.csv.ReporterColumn;
import edu.monash.merc.util.reporter.ReporterManager;
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
@Controller("data.importRepAction")
public class ImportReporterAction extends DMBaseAction {

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private boolean sendMailRequired;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showImportRep() {
        try {
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("experiment.annotation.show.import.permission.denied"));
                return ERROR;
            }

            if (ReporterManager.findImportProcess(ReporterManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("experiment.annotation.import.working.in.progress"));
                return INPUT;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("experiment.annotation.import.show.importing.page.failed"));
            return ERROR;
        }

        return SUCCESS;
    }

    public String importRep() {
        try {
            //get the current user, only the admins who can import the report into system
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("experiment.annotation.import.permission.denied"));
                return ERROR;
            }
            if (ReporterManager.findImportProcess(ReporterManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("experiment.annotation.import.working.in.progress"));
                return INPUT;
            }
            //start to upload the reporter file
            FileInputStream fis = null;
            fis = new FileInputStream(upload);
            //get all reporters from the file
            List<Reporter> reporters = generateReporters(fis);
            //create reporter bean
            ReporterBean reporterBean = createReporterBean(user, reporters);
            //call the import reporters service
            this.dmService.importReporters(reporterBean);
            //set the success message
            String successMsg = getText("experiment.annotation.import.start.success.msg");
            if (sendMailRequired) {
                successMsg = getText("experiment.annotation.import.start.success.with.mail.msg");
            }
            setSuccessActMsg(successMsg);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("experiment.annotation.import.failed.to.start.msg") + " " + ex.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    private List<Reporter> generateReporters(FileInputStream fileInputStream) {
        CSVReader csvReader = null;
        List<Reporter> reporterList = new ArrayList<Reporter>();
        try {
            csvReader = new CSVReader(new InputStreamReader(fileInputStream));
            String[] columnsLine = csvReader.readNext();

            //normalize the column names
            for (int i = 0; i < columnsLine.length; i++) {
                columnsLine[i] = MercUtil.replaceSpace(columnsLine[i]);
            }

            String[] columnValuesLine;
            while ((columnValuesLine = csvReader.readNext()) != null) {
                Reporter reporter = new Reporter();
                if (columnValuesLine.length == columnsLine.length) {
                    CSVRepGenerator repGenerator = new CSVRepGenerator();

                    for (int i = 0; i < columnsLine.length; i++) {
                        repGenerator.getColumns().add(new ReporterColumn(columnsLine[i], columnValuesLine[i]));
                    }
                    reporter = repGenerator.genReport();
                    reporterList.add(reporter);
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
        return reporterList;
    }

    private ReporterBean createReporterBean(User user, List<Reporter> reporters) {
        ReporterBean reporterBean = new ReporterBean();
        String serverQName = getServerQName();
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL);
        reporterBean.setServerName(serverQName);
        reporterBean.setAppName(appName);
        reporterBean.setSendMailRequired(sendMailRequired);
        reporterBean.setFromMail(adminEmail);
        reporterBean.setUser(user);
        reporterBean.setToMail(user.getEmail());
        reporterBean.setReporterName(this.uploadFileName);
        reporterBean.setReporters(reporters);
        return reporterBean;
    }

    public void validateImportRep() {
        if (StringUtils.isBlank(getUploadFileName())) {
            addFieldError("uploadFileName", getText("experiment.annotation.import.file.name.must.be.provided"));
            //call get user
            postProcess();
            return;
        }
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".csv"))) {
            addFieldError("fileFormatError", getText("experiment.annotation.import.file.format.invalid"));
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
            addFieldError("userInfo", getText("experiment.annotation.import.get.user.info.failed"));
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
