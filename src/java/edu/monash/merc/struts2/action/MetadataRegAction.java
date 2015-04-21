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

import edu.monash.merc.common.service.ldap.LDAPService;
import edu.monash.merc.common.service.rifcs.PartyActivityWSService;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.*;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * MetadataRegAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.metadataRegAction")
public class MetadataRegAction extends DMBaseAction {

    @Autowired
    private PartyActivityWSService paWsService;

    @Autowired
    private LDAPService ldapService;

    private String nlaId;

    private List<PartyBean> partyList;

    private List<ProjectBean> projectList;

    private Licence licence;

    private String accessRights;

    private Map<String, String> addPartyTypeMap = new LinkedHashMap<String, String>();

    private String addPartyType;

    private String anzSrcCode;

    private String physicalAddress;

    private PartyBean addedPartyBean;

    private String searchCnOrEmail;

    private String viewExpActName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void initPartyTypes() {
        addPartyTypeMap.put(ActionConts.ANDS_MD_REG_PARTY_RM_TYPE, ActionConts.ANDS_MD_REG_PARTY_RM_TYPE_LABEL);
        addPartyTypeMap.put(ActionConts.ANDS_MD_REG_PARTY_USER_DEFINED_TYPE, ActionConts.ANDS_MD_REG_PARTY_USER_DEFINED_TYPE_LABEL);
    }


    public String showMdReg() {
        try {
            user = getCurrentUser();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.get.user.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.get.experiment.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        if (experiment == null) {
            logger.error(getText("experiment.metadata.reg.experiment.not.found"));
            addActionError(getText("experiment.metadata.reg.experiment.not.found"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        String passwd = user.getPassword();
        if (!StringUtils.equals(passwd, "ldap")) {
            addActionError(getText("experiment.metadata.reg.none.ldap.user.not.supported"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        if (user.getId() != experiment.getOwner().getId() && ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code()))) {
            logger.error("The user is neither the owner of this experiment nor the administrator, unable to register metadata for this experiment.");
            addActionError(getText("experiment.metadata.reg.permission.denied"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }
        //check the existed parties if any
        List<Party> ps = new ArrayList<Party>();
        try {
            ps = this.dmService.getPartiesByExpId(experiment.getId());
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.check.existed.parties.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        // check the existed activities if any
        List<Activity> as = new ArrayList<Activity>();
        try {
            as = this.dmService.getActivitiesByExpId(experiment.getId());
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.check.existed.activities.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        //check the existed Licence
        try {
            Licence existedLicence = this.dmService.getLicenceByExpId(experiment.getId());
            if (existedLicence != null) {
                this.licence = existedLicence;
            } else {
                this.licence = new Licence();
            }
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.check.existed.licence.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        try {
            Permission perm = this.dmService.getExpPermForAnonymous(experiment.getId());
            if (perm.isViewAllowed()) {
                accessRights = getText("experiment.metadata.reg.access.rights.public");
            } else {
                User owner = experiment.getOwner();
                accessRights = getText("experiment.metadata.reg.access.rights.private", new String[]{owner.getFirstName(), owner.getLastName(), owner.getEmail()});
            }
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.check.access.rights.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        // get researcher nla id from rm ws
        try {
            nlaId = paWsService.getNlaId(user.getUniqueId());
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            logger.error(getText("experiment.metadata.reg.ws.failed") + ", " + e);
            boolean keepdoing = false;
            if (StringUtils.containsIgnoreCase(errorMsg, "NLA Id not found") || StringUtils.containsIgnoreCase(errorMsg, "Invalid authcate username")) {
                keepdoing = true;
            }
            if (!keepdoing) {
                setForRMWSException(errorMsg);
                return ERROR;
            }
        }
        // get the researcher party from rm ws
        PartyBean partyBean = null;
        if (nlaId != null) {
            try {
                partyBean = paWsService.getParty(nlaId);
                // set the paty as selected if party available
                if (partyBean != null) {
                    partyBean.setSelected(true);
                }
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                logger.error(getText("experiment.metadata.reg.ws.failed") + ", " + e);
                boolean keepdoing = false;

                if (StringUtils.containsIgnoreCase(errorMsg, "Invalid party id")) {
                    keepdoing = true;
                }
                if (!keepdoing) {
                    setForRMWSException(errorMsg);
                    return ERROR;
                }
            }
        }

        // get the project summary from rm ws
        List<ProjectBean> prolist = null;
        if (nlaId != null) {
            try {
                // get activity summary
                prolist = paWsService.getProjects(nlaId);
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                logger.error(getText("experiment.metadata.reg.ws.failed") + ", " + e);
                boolean keepdoing = false;

                if (StringUtils.containsIgnoreCase(errorMsg, "Projects not found") || StringUtils.containsIgnoreCase(errorMsg, "Invalid NLA Id")) {
                    keepdoing = true;
                }

                if (!keepdoing) {
                    setForRMWSException(errorMsg);
                    return ERROR;
                }
            }
        }

        // merge previous existed parties if any
        populateAllParties(ps, partyBean);
        // merge previous existed project summary
        popilateAllActivities(as, prolist);

        //set navigation bar and title
        setNavAndTitleForMdReg();

        // set the view experiment details action name
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }

        return SUCCESS;
    }

    public String addPartyOptions() {
        // set the add party option as the rm type
        if (addPartyType == null) {
            addPartyType = ActionConts.ANDS_MD_REG_PARTY_RM_TYPE;
        }
        return SUCCESS;
    }

    public String selectPartyType() {
        //just return success
        return SUCCESS;
    }

    public String addRMParty() {
        if (StringUtils.isBlank(searchCnOrEmail)) {
            addFieldError("cnoremail", getText("experiment.metadata.reg.search.ldap.user.cnorname.must.be.provided"));
            return INPUT;
        }
        LDAPUser ldapUser = null;
        try {
            ldapUser = this.ldapService.searchLdapUser(searchCnOrEmail);
        } catch (Exception e) {
            logger.error(e);
            addFieldError("ldapUser", getText("experiment.metadata.reg.search.ldap.user.failed"));
            return INPUT;
        }

        if (ldapUser == null) {
            addFieldError("ldapUser", getText("experiment.metadata.reg.ldap.user.not.existed"));
            return INPUT;
        }

        try {
            String rmNlaId = paWsService.getNlaId(ldapUser.getUid());
            // get party
            addedPartyBean = paWsService.getParty(rmNlaId);
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.ws.failed" + ", Failed to find a researcher"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String addUDParty() {
        try {
            if (addedPartyBean != null) {
                Party p = dmService.getPartyByUsrNameAndEmail(addedPartyBean.getPersonGivenName(), addedPartyBean.getPersonFamilyName(), addedPartyBean.getEmail());
                if (p == null) {
                    //create a new party in the database
                    p = new Party();
                    String localKey = pidService.genUUIDWithPrefix();
                    p.setPersonTitle(addedPartyBean.getPersonTitle());
                    p.setPersonGivenName(addedPartyBean.getPersonGivenName());
                    p.setPersonFamilyName(addedPartyBean.getPersonFamilyName());
                    p.setEmail(addedPartyBean.getEmail());
                    p.setAddress(addedPartyBean.getAddress());
                    p.setUrl(addedPartyBean.getUrl());
                    p.setPartyKey(localKey);
                    p.setIdentifierValue(localKey);
                    p.setIdentifierType("local");
                    p.setOriginateSourceType("authoritative");
                    p.setOriginateSourceValue(addedPartyBean.getOriginateSourceValue());
                    p.setFromRm(addedPartyBean.isFromRm());
                    p.setGroupName(addedPartyBean.getGroupName());
                    this.dmService.saveParty(p);
                } else {
                    //update the party information
                    p.setPersonTitle(addedPartyBean.getPersonTitle());
                    p.setPersonGivenName(addedPartyBean.getPersonGivenName());
                    p.setPersonFamilyName(addedPartyBean.getPersonFamilyName());
                    p.setEmail(addedPartyBean.getEmail());
                    p.setAddress(addedPartyBean.getAddress());
                    p.setUrl(addedPartyBean.getUrl());
                    p.setOriginateSourceValue(addedPartyBean.getOriginateSourceValue());
                    p.setFromRm(addedPartyBean.isFromRm());
                    p.setGroupName(addedPartyBean.getGroupName());
                    this.dmService.updateParty(p);
                }
                addedPartyBean = copyPartyToPartyBean(p);
            } else {
                addActionError(getText("ands.add.party.party.info.not.provided"));
                return ERROR;
            }
        } catch (Exception e) {
            logger.error("failed to save party, " + e.getMessage());
            addActionError(getText("ands.add.party.add.another.party.failed"));
            return INPUT;
        }
        // just return
        return SUCCESS;
    }


    public String previewMdReg() {
        //Set navigation bar and title
        setNavAndTitleForMdReg();
        // set the view experiment details action name
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }

        List<PartyBean> selectedPas = new ArrayList<PartyBean>();
        for (PartyBean partyb : partyList) {
            if (partyb.isSelected()) {
                selectedPas.add(partyb);
            }
        }
        partyList = selectedPas;

        List<ProjectBean> selectedActs = new ArrayList<ProjectBean>();
        if (projectList != null) {
            for (ProjectBean projb : projectList) {
                if (projb.isSelected()) {
                    selectedActs.add(projb);
                }
            }
            projectList = selectedActs;
        }

        anzSrcCode = appSetting.getPropValue(AppPropSettings.ANDS_RIFCS_REG_ANZSRC_CODE);
        physicalAddress = appSetting.getPropValue(AppPropSettings.EXPERIMENT_PHYSICAL_LOCATION);

        return SUCCESS;
    }

    public void validatePreviewMdReg() {
        boolean hasError = false;
        boolean atLeastOnePartySelected = false;
        if (partyList != null) {
            for (PartyBean ptb : partyList) {
                if (ptb.isSelected()) {
                    atLeastOnePartySelected = true;
                }
            }
        }
        if (!atLeastOnePartySelected) {
            addFieldError("partyId", getText("experiment.metadata.reg.party.required"));
            hasError = true;
        }
        if (StringUtils.isBlank(licence.getLicenceType())) {
            addFieldError("licence", getText("experiment.metadata.reg.license.required"));
            hasError = true;
        }
        // set navigations
        if (hasError) {
            setNavAndTitleForMdReg();
            // set the view experiment details action name
            if (fromMyExp) {
                viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
            } else {
                viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
            }
        }
    }


    public String mdReg() {
        String rifcsStoreLocation = appSetting.getPropValue(AppPropSettings.ANDS_RIFCS_STORE_LOCATION);
        anzSrcCode = appSetting.getPropValue(AppPropSettings.ANDS_RIFCS_REG_ANZSRC_CODE);
        physicalAddress = appSetting.getPropValue(AppPropSettings.EXPERIMENT_PHYSICAL_LOCATION);
        String groupName = appSetting.getPropValue(AppPropSettings.ANDS_RIFCS_REG_GROUP_NAME);
        // set the view experiment details action name
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }

        try {
            user = getCurrentUser();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.get.user.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.get.experiment.failed"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        if (experiment == null) {
            logger.error(getText("experiment.metadata.reg.experiment.not.found"));
            addActionError(getText("experiment.metadata.reg.experiment.not.found"));
            setNavAndTitleForMdRegExc();
            return ERROR;
        }

        if (user.getId() != experiment.getOwner().getId() && ((user.getUserType() != UserType.ADMIN.code()) && (user.getUserType() != UserType.SUPERADMIN.code()))) {
            logger.error("The user is neither the owner of this experiment nor the administrator, unable to register metadata for this experiment.");
            addActionError(getText("experiment.metadata.reg.permission.denied"));
            setNavAndTitleForMdReg();
            return INPUT;
        }


        //check the unique key for experiment
        String uniqueKey = experiment.getUniqueKey();
        if (StringUtils.isBlank(uniqueKey)) {
            try {
                uniqueKey = pidService.genUUIDWithPrefix();
                experiment.setUniqueKey(uniqueKey);
            } catch (Exception e) {
                logger.error(e);
                addActionError(getText("experiment.metadata.reg.create.unique.key.failed"));
                setNavAndTitleForMdReg();
                return INPUT;
            }
        }

        // create handle if handle service is enabled
        // populate the url of this collection
        String serverQName = getServerQName();
        String appContext = getAppContextPath();
        StringBuffer expUrl = new StringBuffer();

        //get the server name to construct a view experiment url:
        expUrl.append(serverQName).append(appContext).append(ActionConts.URL_PATH_DEIM);
        expUrl.append("pubdata/viewExperiment.jspx?experiment.id=").append(experiment.getId());

        String hdlEnabledStr = appSetting.getPropValue(AppPropSettings.HANDLE_SERVICE_ENABLED);
        String handle = experiment.getPersistIdentifier();
        //if handle doesn't exist
        if (StringUtils.isBlank(handle)) {
            if (Boolean.valueOf(hdlEnabledStr)) {
                try {
                    handle = pidService.genHandleIdentifier(expUrl.toString());
                    experiment.setPersistIdentifier(handle);
                } catch (Exception e) {
                    addActionError(getText("experiment.metadata.reg.create.handle.failed"));
                    setNavAndTitleForMdReg();
                    return INPUT;
                }
            } else {
                experiment.setPersistIdentifier(experiment.getUniqueKey());
            }
        }
        //set experiment is ready to register metadata
        experiment.setMdPublished(true);

        //create a metadata registration bean
        MDRegistrationBean mdRegBean = new MDRegistrationBean();
        mdRegBean.setPartyList(partyList);
        mdRegBean.setActivityList(projectList);
        mdRegBean.setNamePrefix(this.namePrefix);
        mdRegBean.setExperiment(experiment);
        mdRegBean.setLicence(licence);
        mdRegBean.setAccessRights(accessRights);


        mdRegBean.setRifcsStoreLocation(rifcsStoreLocation);
        mdRegBean.setAnzsrcCode(anzSrcCode);
        mdRegBean.setPhysicalAddress(physicalAddress);
        mdRegBean.setRifcsGroupName(groupName);
        mdRegBean.setAppName(getServerQName());
        mdRegBean.setElectronicURL(MercUtil.replaceURLAmpsands(expUrl.toString()));

        //set the RelatedInfo object if any
        RelatedInfo relatedInfo = createRelatedInfo(experiment);
        if (relatedInfo != null) {
            mdRegBean.setRelatedInfo(relatedInfo);
        }

        try {
            this.dmService.saveMetaRegInfo(mdRegBean);
            recordMDRegAuditEvent(experiment, user);
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.metadata.reg.action.failed"));
            setNavAndTitleForMdReg();
            return INPUT;

        }
        setNavAndTitleForMdReg();
        // set action successful message
        setSuccessActMsg(getText("experiment.metadata.reg.success.message", new String[]{this.namePrefix + experiment.getId()}));
        return SUCCESS;
    }

    private RelatedInfo createRelatedInfo(Experiment experiment) {
        String pubmedBaseUrl = appSetting.getPropValue(AppPropSettings.PUBMED_CITATION_BASE_URL);
        String pubmedId = experiment.getPubMedId();
        if (StringUtils.isNotBlank(pubmedId)) {
            RelatedInfo relatedInfo = new RelatedInfo();
            relatedInfo.setType(ActionConts.RELATEDINFO_DEFAULT_TYPE);
            relatedInfo.setIdentifierType(ActionConts.RELATEDINFO_IDENT_DEFAULT_TYPE);

//            if (StringUtils.contains(pubmedId, ActionConts.PUBMED_ID_DELIM)) {
//                String pmId = StringUtils.substringAfter(pubmedId, ActionConts.PUBMED_ID_DELIM);
//                relatedInfo.setIdentifier(pubmedBaseUrl + "?term=" + pmId);
//            } else {
//                relatedInfo.setIdentifier(pubmedId);
//            }

            if (StringUtils.contains(pubmedId, ActionConts.HTTP_SCHEME)) {
                relatedInfo.setIdentifier(pubmedId);
            } else {
                if (StringUtils.containsIgnoreCase(pubmedId, ActionConts.PUBMED_ID_DELIM)) {
                    String pmId = StringUtils.substringAfter(pubmedId, ActionConts.PUBMED_ID_DELIM);
                    relatedInfo.setIdentifier(pubmedBaseUrl + "?term=" + pmId);
                } else {
                    relatedInfo.setIdentifier(pubmedBaseUrl + "?term=" + pubmedId);
                }
            }
            relatedInfo.setTitle(experiment.getPubTitle());
            //relatedInfo.setNotes(experiment.getAbstraction() + " " + experiment.getAuthors() + ", " + experiment.getPublicationDate());
            return relatedInfo;
        } else {
            return null;
        }
    }

    private void recordMDRegAuditEvent(Experiment exp, User operator) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        ev.setEvent(getText("experiment.metadata.reg.audit.info", new String[]{this.namePrefix + exp.getId()}));
        ev.setEventOwner(exp.getOwner());
        ev.setOperator(operator);
        recordActionAuditEvent(ev);
    }

    private void populateAllParties(List<Party> existedParties, PartyBean rmpb) {
        if (partyList == null) {
            partyList = new ArrayList<PartyBean>();
            // add the rm party
            if (rmpb != null) {
                partyList.add(rmpb);
            }
        }
        if (existedParties != null && existedParties.size() > 0) {
            for (Party party : existedParties) {
                String partykey = party.getPartyKey();
                // create a previous PartyBean
                PartyBean existedParty = copyPartyToPartyBean(party);
                existedParty.setSelected(true);
                // if rmpb is not null, then we compare it with exsited parties which previous populated
                // if key is not the same, then we add it into the list,
                // if rmpb is null, then we add all existed parties
                if (rmpb != null) {
                    String rmPbKey = rmpb.getPartyKey();
                    if (!rmPbKey.equals(partykey)) {
                        partyList.add(existedParty);
                    }
                } else {
                    partyList.add(existedParty);
                }
            }
        }
    }

    private void popilateAllActivities(List<Activity> existedActivities, List<ProjectBean> rmProjList) {
        // sign the rm project summary list to project list
        projectList = rmProjList;
        if (projectList != null && projectList.size() > 0) {
            for (ProjectBean projb : projectList) {
                if (existedActivities != null && existedActivities.size() > 0) {
                    for (Activity a : existedActivities) {
                        if (projb.getActivityKey().equals(a.getActivityKey())) {
                            projb.setSelected(true);
                        }
                    }
                }
            }
        }
    }

    private PartyBean copyPartyToPartyBean(Party p) {
        PartyBean pb = new PartyBean();
        pb.setPartyKey(p.getPartyKey());
        pb.setPersonTitle(p.getPersonTitle());
        pb.setPersonGivenName(p.getPersonGivenName());
        pb.setPersonFamilyName(p.getPersonFamilyName());
        pb.setUrl(p.getUrl());
        pb.setEmail(p.getEmail());
        pb.setAddress(p.getAddress());
        pb.setIdentifierType(p.getIdentifierType());
        pb.setIdentifierValue(p.getIdentifierValue());
        pb.setOriginateSourceType(p.getOriginateSourceType());
        pb.setOriginateSourceValue(p.getOriginateSourceValue());
        pb.setGroupName(p.getGroupName());
        pb.setFromRm(p.isFromRm());
        return pb;
    }

    // for metadata registration action title and navigation bar
    private void setNavAndTitleForMdReg() {
        setPageTitle(getText("experiment.metadata.reg.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.metadata.reg.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // for metadata registration action title and navigation bar after exception
    private void setNavAndTitleForMdRegExc() {
        setPageTitle(getText("experiment.metadata.reg.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.metadata.reg.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    private void setForRMWSException(String errorMsg) {
        addActionError(getText("ands.md.registration.ws.failed") + ", " + errorMsg);
        setNavAndTitleForMdRegExc();
    }

    public void setPaWsService(PartyActivityWSService paWsService) {
        this.paWsService = paWsService;
    }

    public void setLdapService(LDAPService ldapService) {
        this.ldapService = ldapService;
    }

    public String getNlaId() {
        return nlaId;
    }

    public void setNlaId(String nlaId) {
        this.nlaId = nlaId;
    }

    public List<PartyBean> getPartyList() {
        return partyList;
    }

    public void setPartyList(List<PartyBean> partyList) {
        this.partyList = partyList;
    }

    public List<ProjectBean> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectBean> projectList) {
        this.projectList = projectList;
    }

    public Licence getLicence() {
        return licence;
    }

    public void setLicence(Licence licence) {
        this.licence = licence;
    }

    public String getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    public Map<String, String> getAddPartyTypeMap() {
        return addPartyTypeMap;
    }

    public void setAddPartyTypeMap(Map<String, String> addPartyTypeMap) {
        this.addPartyTypeMap = addPartyTypeMap;
    }

    public String getAddPartyType() {
        return addPartyType;
    }

    public void setAddPartyType(String addPartyType) {
        this.addPartyType = addPartyType;
    }

    public String getAnzSrcCode() {
        return anzSrcCode;
    }

    public void setAnzSrcCode(String anzSrcCode) {
        this.anzSrcCode = anzSrcCode;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public PartyBean getAddedPartyBean() {
        return addedPartyBean;
    }

    public void setAddedPartyBean(PartyBean addedPartyBean) {
        this.addedPartyBean = addedPartyBean;
    }

    public String getSearchCnOrEmail() {
        return searchCnOrEmail;
    }

    public void setSearchCnOrEmail(String searchCnOrEmail) {
        this.searchCnOrEmail = searchCnOrEmail;
    }

    public String getViewExpActName() {
        return viewExpActName;
    }

    public void setViewExpActName(String viewExpActName) {
        this.viewExpActName = viewExpActName;
    }
}
