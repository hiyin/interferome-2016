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

import edu.monash.merc.domain.*;
import edu.monash.merc.util.importer.BaseExp;
import edu.monash.merc.util.importer.ExpImporter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * ImportExperimentAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.importExpAction")
public class ImportExperimentAction extends DMBaseAction {

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showImportExp() {
        try {
            user = getCurrentUser();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("experiment.import.show.experiment.importing.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String importExp() {
        try {
            // set fromMyExp true
            fromMyExp = true;
            user = getCurrentUser();
            //upload the file
            FileInputStream fis = null;
            fis = new FileInputStream(upload);
            //calling the experiment importer
            experiment = invokeExpImporter(fis);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("experiment.import.new.experiment.failed") + ". " + ex.getMessage());
            return ERROR;
        }
        //check the experiment name existed or not
        try {
            boolean existed = this.dmService.checkExperimentNameExisted(experiment.getName());
            if (existed) {
                addActionError(getText("experiment.import.new.experiment.name.existed"));
                return ERROR;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("experiment.import.new.experiment.failed"));
            return ERROR;
        }

        try {
            Date date = GregorianCalendar.getInstance().getTime();
            experiment.setCreatedTime(date);
            experiment.setModifiedTime(date);
            experiment.setOwner(user);
            experiment.setModifiedByUser(user);

            List<Permission> perms = setupDefaultExpPerms(experiment);
            experiment.setPermissions(perms);
            this.dmService.createExperiment(experiment);

            // format the description
            String desc = experiment.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                experiment.setDescription(newLineToBr(desc));
            }
            // format the abstraction
            String abstraction = experiment.getAbstraction();
            if (StringUtils.isNotBlank(abstraction)) {
                experiment.setAbstraction(newLineToBr(abstraction));
            }

            // format the publication
            String publication = experiment.getPublication();
            if (StringUtils.isNotBlank(publication)) {
                experiment.setPublication(newLineToBr(publication));
            }

            // record the auditing event
            recordAuditEventForImport(experiment, user);
            // set the success message.
            setSuccessActMsg(getText("experiment.import.new.experiment.success.message", new String[]{this.namePrefix + experiment.getId()}));
            // set full permissions for owner
            setupFullPermissions();
            // set the delete experiment action name
            deleteExpActName = ActionConts.DELETE_MY_EXP_ACTION;

            //set the experiment reference link
            setExperimentReferenceLink(experiment);
            // set navigation bar and page title.
            setNavAndTitleForMyExp();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.import.new.experiment.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public void validateImportExp() {
        if (StringUtils.isBlank(getUploadFileName())) {
            addFieldError("uploadFileName", getText("experiment.import.file.name.must.be.provided"));
            //call post process
            postProcess();
            return;
        }
        if (!(StringUtils.endsWithIgnoreCase(getUploadFileName(), ".xml"))) {
            addFieldError("fileFormatError", getText("experiment.import.file.format.invalid"));
            //call post process
            postProcess();
            return;
        }
    }

    private void postProcess() {
        try {
            user = getCurrentUser();
        } catch (Exception ex) {
            logger.error(ex);
            addFieldError("userInfo", getText("experiment.import.get.user.info.failed"));
        }
    }

    // set the navigation bar and page title after the importing.
    private void setNavAndTitleForMyExp() {
        setPageTitle(getText("experiment.list.my.all.experiments.action.title"));
        String secondNav = getText("experiment.list.my.all.experiments.action.title");
        String secondNavLink = "data/listMyExperiments.jspx";
        String thirdNav = this.namePrefix + experiment.getId();
        String thirdNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    // set the auditing event
    private void recordAuditEventForImport(Experiment exp, User operator) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        ev.setEvent(getText("experiment.import.new.experiment.success.message", new String[]{this.namePrefix + exp.getId()}));
        ev.setEventOwner(exp.getOwner());
        ev.setOperator(operator);
        recordActionAuditEvent(ev);
    }

    // set the default experiment permissions
    private List<Permission> setupDefaultExpPerms(Experiment exp) {
        // all-registered user
        Permission regUserPerm = new Permission();
        // set the permission type;
        regUserPerm.setPermType(PermType.ALLREGUSER.code());

        User allRegUser = this.userService.getVirtualUser(UserType.ALLREGUSER.code());

        regUserPerm.setExperiment(exp);
        regUserPerm.setPermForUser(allRegUser);
        // anonymous user
        Permission anonyPerm = new Permission();
        // set the permission type.
        anonyPerm.setPermType(PermType.ANONYMOUS.code());

        User anonymous = this.userService.getVirtualUser(UserType.ANONYMOUS.code());

        anonyPerm.setExperiment(exp);
        anonyPerm.setPermForUser(anonymous);

        List<Permission> defaultPerms = new ArrayList<Permission>();
        defaultPerms.add(regUserPerm);
        defaultPerms.add(anonyPerm);
        return defaultPerms;
    }

    private Experiment invokeExpImporter(InputStream inputStream) {
        ExpImporter importer = new ExpImporter();
        BaseExp baseExp = importer.importExp(inputStream);
        return convertBaseExpToExp(baseExp);
    }

    private Experiment convertBaseExpToExp(BaseExp baseExp) {
        Experiment exp = new Experiment();
        exp.setName(baseExp.getName());
        exp.setUniqueKey(this.pidService.genUUIDWithPrefix());
        exp.setDescription(baseExp.getDescription());
        exp.setBaseOwner(baseExp.getOwnerName());
        exp.setRawDataType(baseExp.getRawDataType());
        exp.setEntryDate(baseExp.getEntryDate());
        exp.setDirectoy(baseExp.getDirectoryName());
        exp.setExpBytes(baseExp.getBytes());
        exp.setExperimentDesign(baseExp.getExperimentDesign());
        exp.setExperimentType(baseExp.getExperimentType());
        exp.setPubMedId(baseExp.getPubMedId());
        exp.setPublication(baseExp.getPublication());
        exp.setPublicationDate(baseExp.getPublicationDate());
        exp.setPubTitle(baseExp.getTitle());
        exp.setAbstraction(baseExp.getAbstraction());
        exp.setAffiliations(baseExp.getAffiliations());
        exp.setAuthors(baseExp.getAuthors());

        exp.setApproved(false);
        exp.setMdPublished(false);
        return exp;
    }

    /**
     * @return the upload
     */
    public File getUpload() {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(File upload) {
        this.upload = upload;
    }

    /**
     * @return the uploadContentType
     */
    public String getUploadContentType() {
        return uploadContentType;
    }

    /**
     * @param uploadContentType the uploadContentType to set
     */
    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    /**
     * @return the uploadFileName
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * @param uploadFileName the uploadFileName to set
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
}
