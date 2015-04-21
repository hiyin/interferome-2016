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
import edu.monash.merc.domain.Tissue;
import edu.monash.merc.domain.TissueExpression;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.dto.TissueBean;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.util.MercUtil;
import edu.monash.merc.util.csv.CSVTissueGenerator;
import edu.monash.merc.util.csv.TissueColumn;
import edu.monash.merc.util.tissue.TissueManager;
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
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 13/02/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */

@Scope("prototype")
@Controller("data.importTissueAction")
public class ImportTissueAction extends DMBaseAction{
    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private boolean sendMailRequired;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showImportTissue() {
        try {
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("tissue.show.import.permission.denied"));
                return ERROR;
            }

            if (TissueManager.findImportProcess(TissueManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("tissue.import.working.in.progress"));
                return INPUT;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tissue.import.show.importing.page.failed"));
            return ERROR;
        }

        return SUCCESS;
    }

    public String importTissue() {
        try {
            //get the current user, only the admins who can import the report into system
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("tissue.import.permission.denied"));
                return ERROR;
            }
            if (TissueManager.findImportProcess(TissueManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("tissue.import.working.in.progress"));
                return INPUT;
            }
            //start to upload the tissue file
            FileInputStream fis = null;
            fis = new FileInputStream(upload);
            //get all tissues from the file
            List<TissueExpression> tissueExpressions = generateTissueExpression(fis);

            //create tissue bean
            TissueBean tissueBean = createTissueBean(user, tissueExpressions);
            //call the import tissues service
            this.dmService.importTissue(tissueBean);

            //set the success message
            String successMsg = getText("tissue.import.start.success.msg");
            if (sendMailRequired) {
                successMsg = getText("tissue.import.start.success.with.mail.msg");
            }
            setSuccessActMsg(successMsg);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tissue.import.failed.to.start.msg") + " " + ex.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    private List<TissueExpression> generateTissueExpression(FileInputStream fileInputStream) {
        CSVReader csvReader = null;
        List<TissueExpression> tissueExpressionList = new ArrayList<TissueExpression>();
        try {
            csvReader = new CSVReader(new InputStreamReader(fileInputStream));
            String[] columnsLine = csvReader.readNext();

            //normalize the column names
            for (int i = 0; i < columnsLine.length; i++) {
                columnsLine[i] = MercUtil.replaceSpace(columnsLine[i]);
            }

            String[] columnValuesLine;
            while ((columnValuesLine = csvReader.readNext()) != null) {
                TissueExpression tissueExpression = new TissueExpression();
                if (columnValuesLine.length == columnsLine.length) {
                    CSVTissueGenerator tissueGenerator = new CSVTissueGenerator();

                    for (int i = 0; i < columnsLine.length; i++) {
                        tissueGenerator.getColumns().add(new TissueColumn(columnsLine[i], columnValuesLine[i]));
                    }
                    tissueExpression = tissueGenerator.genTissues();
                    tissueExpressionList.add(tissueExpression);
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
        return tissueExpressionList;
    }


    private TissueBean createTissueBean(User user, List<TissueExpression> tissueExpressions) {
        TissueBean tissueBean = new TissueBean();
        String serverQName = getServerQName();
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL);
        tissueBean.setServerName(serverQName);
        tissueBean.setAppName(appName);
        tissueBean.setSendMailRequired(sendMailRequired);
        tissueBean.setFromMail(adminEmail);
        tissueBean.setUser(user);
        tissueBean.setToMail(user.getEmail());
        tissueBean.setTissueName(this.uploadFileName);
        tissueBean.setTissues(tissueExpressions);
        return tissueBean;
    }

    public void validateImportTissues() {
        if (StringUtils.isBlank(getUploadFileName())) {
            addFieldError("uploadFileName", getText("tissue.import.file.name.must.be.provided"));
            //call get user
            postProcess();
            return;
        }
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".csv"))) {
            addFieldError("fileFormatError", getText("tissue.import.file.format.invalid"));
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
            addFieldError("userInfo", getText("tissue.import.get.user.info.failed"));
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
