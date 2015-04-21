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
import edu.monash.merc.common.service.pid.IdentifierService;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.DatasetFactorBean;
import edu.monash.merc.dto.NameValueBean;
import edu.monash.merc.dto.PermissionBean;
import edu.monash.merc.service.DMService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DMBaseAction Action class is a data management base action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class DMBaseAction extends BaseAction {

    @Autowired
    protected DMService dmService;

    @Autowired
    protected IdentifierService pidService;

    protected Pagination<Experiment> exPagination;

    protected PermissionBean permissionBean;

    protected String deleteExpActName;

    protected Experiment experiment;

    protected int totalDatasetNum;

    protected boolean fromMyExp;

    protected String redActionName;

    protected String redNamespace;

    protected String namePrefix;

    protected String referenceLink;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmService(DMService dmService) {
        this.dmService = dmService;
    }

    /**
     * @param pidService the pidService to set
     */
    public void setPidService(IdentifierService pidService) {
        this.pidService = pidService;
    }

    @PostConstruct
    public void initNamePrefix() {
        this.namePrefix = appSetting.getPropValue(AppPropSettings.EXPERIMENT_NAME_PREFIX);
    }

    protected void sendApprovalAccountEmail(String userFullName, String userEmail, String organization) {
        String approveAccountMailTemplateFile = "approveUserRegistrationEmailTemplate.ftl";
        // site name
        String serverQName = getServerQName();
        // prepare to send email.
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_SERVICE_EMAIL);
        String subject = getText("admin.activate.account.accepted.mail.title");

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userFullName);
        templateMap.put("UserEmail", userEmail);
        // Organization
        templateMap.put("Organization", organization);
        templateMap.put("SiteName", serverQName);
        templateMap.put("AppName", appName);

        // send an email to user
        this.dmService.sendMail(adminEmail, userEmail, subject, templateMap, approveAccountMailTemplateFile, true);
    }

    protected void sendRejectEmailToUser(String userFullName, String userEmail, String organization) {

        String approveAccountMailTemplateFile = "rejectUserRegistrationEmailTemplate.ftl";
        // site name
        String serverQName = getServerQName();
        // prepare to send email.
        String appName = appSetting.getPropValue(AppPropSettings.APPLICATION_NAME);
        String adminEmail = appSetting.getPropValue(AppPropSettings.SYSTEM_SERVICE_EMAIL);
        String subject = getText("admin.activate.account.rejected.mail.title");

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userFullName);
        templateMap.put("UserEmail", userEmail);
        // Organization
        templateMap.put("Organization", organization);
        templateMap.put("SiteName", serverQName);
        templateMap.put("AppName", appName);
        // send an email to user
        this.dmService.sendMail(adminEmail, userEmail, subject, templateMap, approveAccountMailTemplateFile, true);
    }

    /**
     * @return the exPagination
     */
    public Pagination<Experiment> getExPagination() {
        return exPagination;
    }

    /**
     * @param exPagination the exPagination to set
     */
    public void setExPagination(Pagination<Experiment> exPagination) {
        this.exPagination = exPagination;
    }

    protected void initExpPageParams() {
        // page size per page values
        pageSizeMap.put(5, 5);
        pageSizeMap.put(10, 10);
        pageSizeMap.put(15, 15);
        pageSizeMap.put(20, 20);
        pageSizeMap.put(25, 25);
        pageSizeMap.put(30, 30);
        pageSizeMap.put(40, 40);
        pageSizeMap.put(50, 50);
        // orderby values
        //map the id as a name, due to we use IFM plus id as name. so sorting the name is actually sorted by id
        orderByMap.put("id", "name");
        orderByMap.put("createdTime", "imported date");
        orderByMap.put("modifiedTime", "modified date");
        orderByMap.put("approved", "approval status");
        // orderby type values
        orderByTypeMap.put("ASC", "asc");
        orderByTypeMap.put("DESC", "desc");
    }

    protected void setExpDefaultPageParams() {
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "id";
        }
        if (StringUtils.isBlank(orderByType)) {
            orderByType = ActionConts.DEFAULT_ORDERBY_TYPE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = ActionConts.DEFAULT_PAGE_SIZE;
        }
    }

    /**
     * check the experiment permissions for an user
     *
     * @param expId      the experiment id to set
     * @param expOwnerId the experiment owner id to set
     */
    protected void checkExpPermsForUser(long expId, long expOwnerId) {
        // get the current logged in user id from session if any
        long loggedInUserId = getCurrentUserId();
        // get current logged in user from the database if any
        user = getCurrentUser();

        // non-login user, just create an empty permission
        if (loggedInUserId == 0) {
            permissionBean = new PermissionBean();
            // check the perimssion for anonymous from the database
            Permission anonyPerm = this.dmService.getExpPermForAnonymous(expId);
            permissionBean.setId(anonyPerm.getId());
            permissionBean.setName(anonyPerm.getPermForUser().getDisplayName());
            permissionBean.setUserId(anonyPerm.getPermForUser().getId());
            permissionBean.setViewAllowed(anonyPerm.isViewAllowed());
            permissionBean.setUpdateAllowed(anonyPerm.isUpdateAllowed());
            permissionBean.setImportAllowed(anonyPerm.isImportAllowed());
            permissionBean.setExportAllowed(anonyPerm.isExportAllowed());
            permissionBean.setDeleteAllowed(anonyPerm.isDeleteAllowed());
            permissionBean.setChangePermAllowed(anonyPerm.isChangePermAllowed());
            return;
        }
        // if user is the owner of experiment
        if (loggedInUserId == expOwnerId) {
            // create a new permissions.
            permissionBean = new PermissionBean();
            permissionBean.setFullPermissions();
            return;
        }

        // if logged in user is an admin or super admin
        if (user != null && (user.getUserType() == UserType.ADMIN.code() || (user.getUserType() == UserType.SUPERADMIN.code()))) {
            // create a new permissions.
            permissionBean = new PermissionBean();
            permissionBean.setFullPermissions();
            return;
        }

        // get the user permission for this experiment, return at least two permissions or max three permissions
        // case1: return only two permissions (one is for anonymous. the other is for all registered users
        // case2: return three permissions (first one is for anonymous, the second one is for all registered users, the
        // third one will be for the current user
        List<Permission> userAllPerms = this.dmService.getExpPermsForUser(expId, loggedInUserId);
        permissionBean = new PermissionBean();
        //set user permissions as null first.
        Permission userPerm = null;
        Permission anonyPerm = new Permission();
        Permission allRegPerm = new Permission();

        if (userAllPerms != null) {
            for (Permission perm : userAllPerms) {
                String permType = perm.getPermType();
                if (permType.equals(PermType.REGISTERED.code())) {
                    userPerm = perm;
                }
                if (permType.equals(PermType.ANONYMOUS.code())) {
                    anonyPerm = perm;
                }
                if (permType.equals(PermType.ALLREGUSER.code())) {
                    allRegPerm = perm;
                }
            }
        }

        if (userPerm != null) {// if user permissions found, just return user permissions
            permissionBean.setViewAllowed(userPerm.isViewAllowed());
            permissionBean.setUpdateAllowed(userPerm.isUpdateAllowed());
            permissionBean.setImportAllowed(userPerm.isImportAllowed());
            permissionBean.setExportAllowed(userPerm.isExportAllowed());
            permissionBean.setDeleteAllowed(userPerm.isDeleteAllowed());
            permissionBean.setChangePermAllowed(userPerm.isChangePermAllowed());
        } else {// if user permissions not found, just return all-registered-user permissions
            permissionBean.setViewAllowed(allRegPerm.isViewAllowed());
            permissionBean.setUpdateAllowed(allRegPerm.isUpdateAllowed());
            permissionBean.setImportAllowed(allRegPerm.isImportAllowed());
            permissionBean.setExportAllowed(allRegPerm.isExportAllowed());
            permissionBean.setDeleteAllowed(allRegPerm.isDeleteAllowed());
            permissionBean.setChangePermAllowed(allRegPerm.isChangePermAllowed());
        }
    }

    protected void recordActionAuditEvent(AuditEvent event) {
        try {
            this.dmService.saveAuditEvent(event);
        } catch (Exception e) {
            // if can't persist the audit event, just log the exception, and let the other action finish
            logger.error("Failed to persist the audit event, " + e.getMessage());
            logger.error(event.getEvent() + " , operated by " + event.getOperator().getDisplayName() + ", audit event owned by "
                    + event.getEventOwner().getDisplayName());
        }
    }

    /**
     * get all dataset with factor values by an experiment id.
     *
     * @param expId
     * @return a list of DatasetFactorBean which contains all dataset with factor values
     */
    protected List<DatasetFactorBean> getDatasetByExperiment(long expId) {
        List<DatasetFactorBean> datasetFactorBeans = new ArrayList<DatasetFactorBean>();
        List<Dataset> dsList = this.dmService.getDatasetsByExpId(expId);
        for (Dataset ds : dsList) {
            long dsId = ds.getId();
            List<NameValueBean> nameValueBeans = this.dmService.getFactorValuesBeanByDatasetId(dsId);
            String sampleChars = ds.getSampleChars();
            DatasetFactorBean dsFactorBean = new DatasetFactorBean(ds, nameValueBeans);
            datasetFactorBeans.add(dsFactorBean);
        }
        return datasetFactorBeans;
    }

    private String insertNewLineStr(String str){
        if(StringUtils.isNotBlank(str)){
            int length = str.length();
            if(length >= 300){

            }
        }
        return null;
    }

    /**
     * Count the total datasets in this experiment
     *
     * @param expId
     * @return a total number of datasets
     */
    protected int countTotalDatasetsNumber(long expId) {
        return this.dmService.getTotalDatasetsNumber(expId);
    }

    protected void setExperimentReferenceLink(Experiment experiment) {
        if (experiment != null) {
            String expName = experiment.getName();

            if (StringUtils.startsWithIgnoreCase(expName, "E")) {
                referenceLink = appSetting.getPropValue(AppPropSettings.REFERENCE_EBI_LINK);
            }
            if (StringUtils.startsWithIgnoreCase(expName, "G")) {
                referenceLink = appSetting.getPropValue(AppPropSettings.REFERENCE_NCBI_LINK);
            }
        }
    }

    /**
     * @return the permissionBean
     */
    public PermissionBean getPermissionBean() {
        return permissionBean;
    }

    /**
     * @param permissionBean the permissionBean to set
     */
    public void setPermissionBean(PermissionBean permissionBean) {
        this.permissionBean = permissionBean;
    }

    protected void setupFullPermissions() {
        permissionBean = new PermissionBean();
        permissionBean.setFullPermissions();
    }

    /**
     * @return the deleteExpActName
     */
    public String getDeleteExpActName() {
        return deleteExpActName;
    }

    /**
     * @param deleteExpActName the deleteExpActName to set
     */
    public void setDeleteExpActName(String deleteExpActName) {
        this.deleteExpActName = deleteExpActName;
    }

    /**
     * @return the experiment
     */
    public Experiment getExperiment() {
        return experiment;
    }

    /**
     * @param experiment the experiment to set
     */
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     * @return the fromMyExp
     */
    public boolean isFromMyExp() {
        return fromMyExp;
    }

    /**
     * @param fromMyExp the fromMyExp to set
     */
    public void setFromMyExp(boolean fromMyExp) {
        this.fromMyExp = fromMyExp;
    }

    public int getTotalDatasetNum() {
        return totalDatasetNum;
    }

    public void setTotalDatasetNum(int totalDatasetNum) {
        this.totalDatasetNum = totalDatasetNum;
    }

    /**
     * @return the redActionName
     */
    public String getRedActionName() {
        return redActionName;
    }

    /**
     * @param redActionName the redActionName to set
     */
    public void setRedActionName(String redActionName) {
        this.redActionName = redActionName;
    }

    /**
     * @return the redNamespace
     */
    public String getRedNamespace() {
        return redNamespace;
    }

    /**
     * @param redNamespace the redNamespace to set
     */
    public void setRedNamespace(String redNamespace) {
        this.redNamespace = redNamespace;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }
}
