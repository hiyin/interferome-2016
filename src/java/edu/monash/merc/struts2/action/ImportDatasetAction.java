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

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.domain.User;
import edu.monash.merc.util.DCFileUtils;
import edu.monash.merc.util.interferome.dataset.BaseDataset;
import edu.monash.merc.util.interferome.dataset.TxtDataset;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImportDatasetAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.importDatasetAction")
public class ImportDatasetAction extends DMBaseAction {

    private Map<String, String> responseData = new HashMap<String, String>();

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private Logger logger = Logger.getLogger(this.getClass().getName());


    public String importDataset() {
        try {
            user = getCurrentUser();
        } catch (Exception ex) {
            logger.error(ex);
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.user.not.found"));
            return SUCCESS;
        }

        //check the file name
        if (StringUtils.isBlank(getUploadFileName())) {
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.file.name.must.be.provided"));
            return SUCCESS;
        }

        //check the file format
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".txt"))) {
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.dataset.file.format.invalid"));
            return SUCCESS;
        }

        try {
            boolean existed = this.dmService.checkExperimentDatasetExisted(experiment.getId(), uploadFileName);
            if (existed) {
                logger.error(getText("dataset.import.dataset.already.exist.in.this.experiment"));
                responseData.put("success", "false");
                responseData.put("message", getText("dataset.import.dataset.already.exist.in.this.experiment"));
                return SUCCESS;
            }
        } catch (Exception ex) {
            logger.error(ex);
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.failed.to.check.dataset.name"));
            return SUCCESS;
        }

        //check the experiment id
        if (experiment == null || (experiment != null && experiment.getId() == 0)) {
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.experiment.id.must.be.provided"));
            return SUCCESS;
        }

        //get the experiment
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isImportAllowed()) {
                    responseData.put("success", "false");
                    responseData.put("message", getText("dataset.import.permission.denied"));
                    return SUCCESS;
                }
            } else {
                responseData.put("success", "false");
                responseData.put("message", getText("dataset.import.experiment.not.found"));
                return SUCCESS;
            }
        } catch (Exception ex) {
            logger.error(ex);
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.experiment.not.found"));
            return SUCCESS;
        }

        //extract the dataset and data
        try {
            List<String> dataLines = DCFileUtils.readLines(upload);
            BaseDataset txtDataset = new TxtDataset(uploadFileName, dataLines);
            //save the file
            String resultMsg = this.dmService.importExpDataset(experiment, txtDataset);

            if (resultMsg.contains("not found")){
                responseData.put("success", "false");
            } else {
                responseData.put("success", "true");
            }

            responseData.put("message", getText("dataset.import.success.message", new String[]{uploadFileName})+resultMsg);

            //record the audit info
            recordAuditEventForImport(experiment, uploadFileName, user);
            return SUCCESS;

        } catch (Exception e) {
            logger.error(e);
            responseData.put("success", "false");
            responseData.put("message", getText("dataset.import.new.dataset.failed") + ", " + e.getMessage());
            return SUCCESS;
        }
    }

    private void recordAuditEventForImport(Experiment exp, String fileName, User operator) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        ev.setEvent(getText("dataset.import.success.message", new String[]{fileName}));
        ev.setEventOwner(exp.getOwner());
        ev.setOperator(operator);
        recordActionAuditEvent(ev);
    }

    public Map<String, String> getResponseData() {
        return responseData;
    }

    public void setResponseData(Map<String, String> responseData) {
        this.responseData = responseData;
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
}
