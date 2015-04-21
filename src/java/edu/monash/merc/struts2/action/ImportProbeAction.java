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
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.dto.ProbeBean;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.util.MercUtil;
import edu.monash.merc.util.csv.CSVProbGenerator;
import edu.monash.merc.util.csv.ProbeColumn;
import edu.monash.merc.util.probe.ProbeManager;
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
 * User: irinar
 * Date: 17/12/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Scope("prototype")
@Controller("data.importProbeAction")
public class ImportProbeAction extends DMBaseAction {

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private boolean sendMailRequired;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showImportProbes() {
        try {
            user = getCurrentUser();
            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("probe.show.import.permission.denied"));
                return ERROR;
            }

            if (ProbeManager.findImportProcess(ProbeManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("probe.import.working.in.progress"));
                return INPUT;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("probe.import.show.importing.page.failed"));
            return ERROR;
        }

        return SUCCESS;
    }

    public String importProbes() {
        try {
            //get the current user, only the admins who can import the report into system
            user = getCurrentUser();

            if ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code())) {
                addActionError(getText("probe.import.permission.denied"));
                return ERROR;
            }
            if (ProbeManager.findImportProcess(ProbeManager.PROCESS_ID)) {
                addFieldError("importnotfinished", getText("probe.import.working.in.progress"));
                return INPUT;
            }
            //start to upload the probe file
            FileInputStream fis = null;
            fis = new FileInputStream(upload);
            //get all probes from the file
            List<Probe> probes = generateProbes(fis);
            //create probes bean
            ProbeBean probeBean = createProbeBean(user, probes);
            //call the import probes service
            this.dmService.importProbe(probeBean);
            //set the success message
            String successMsg = getText("probe.import.start.success.msg");
            if (sendMailRequired) {
                successMsg = getText("probe.import.start.success.with.mail.msg");
            }
            setSuccessActMsg(successMsg);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("probe.import.failed.to.start.msg") + " " + ex.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    private List<Probe> generateProbes(FileInputStream fileInputStream) {
        CSVReader csvReader = null;
        List<Probe> probeList = new ArrayList<Probe>();
        try {
            csvReader = new CSVReader(new InputStreamReader(fileInputStream));
            String[] columnsLine = csvReader.readNext();

            //normalize the column names
            for (int i = 0; i < columnsLine.length; i++) {
                columnsLine[i] = MercUtil.replaceSpace(columnsLine[i]);
            }

            String[] columnValuesLine;
            while ((columnValuesLine = csvReader.readNext()) != null) {
                Probe probes = new Probe();
                if (columnValuesLine.length == columnsLine.length) {
                    CSVProbGenerator probGenerator = new CSVProbGenerator();

                    for (int i = 0; i < columnsLine.length; i++) {
                        probGenerator.getColumns().add(new ProbeColumn(columnsLine[i], columnValuesLine[i]));
                    }
                    probes = probGenerator.genProbe();
                    probeList.add(probes);
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
        return probeList;
    }

    private ProbeBean createProbeBean(User user, List<Probe> probes) {
        ProbeBean probeBean = new ProbeBean();
        String serverQName = getServerQName();
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_ADMIN_EMAIL);
        probeBean.setServerName(serverQName);
        probeBean.setAppName(appName);
        probeBean.setSendMailRequired(sendMailRequired);
        probeBean.setFromMail(adminEmail);
        probeBean.setUser(user);
        probeBean.setToMail(user.getEmail());
        probeBean.setProbeName(this.uploadFileName);
        probeBean.setProbes(probes);
        return probeBean;
    }

    public void validateImportProbes() {
        if (StringUtils.isBlank(getUploadFileName())) {
            addFieldError("uploadFileName", getText("probe.import.file.name.must.be.provided"));
            //call get user
            postProcess();
            return;
        }
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".csv"))) {
            addFieldError("fileFormatError", getText("probe.import.file.format.invalid"));
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
            addFieldError("userInfo", getText("probe.import.get.user.info.failed"));
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
