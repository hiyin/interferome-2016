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
package edu.monash.merc.service.impl;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.results.SearchResultRow;
import edu.monash.merc.common.service.file.FileSystemSerivce;
import edu.monash.merc.common.service.mail.MailService;
import edu.monash.merc.common.service.rifcs.RIFCSService;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.*;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.service.*;
import edu.monash.merc.util.MercUtil;
import edu.monash.merc.util.interferome.dataset.BaseDataset;
import edu.monash.merc.util.interferome.dataset.ExpFactor;
import edu.monash.merc.util.interferome.dataset.IFNTypeFactor;
import edu.monash.merc.util.interferome.dataset.VarFactor;
import edu.monash.merc.util.probe.ImportProbeThread;
import edu.monash.merc.util.reporter.ImportReporterThread;
import edu.monash.merc.util.tfsite.ImportTFSiteThread;
import edu.monash.merc.util.tissue.ImportTissueThread;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * DMService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class DMServiceImpl implements DMService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private AuditEventService auditEventService;

    @Autowired
    @Qualifier("freemarkerMailService")
    private MailService mailService;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionRequestService permRequestService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LicenceService licenceService;

    @Autowired
    private FileSystemSerivce fileService;

    @Autowired
    private RIFCSService rifcsService;

    @Autowired
    private ReporterService reporterService;

    @Autowired
    private TFSiteService tfSiteService;

    @Autowired
    private TissueExpressionService tissueExpressionService;

    @Autowired
    private TissueService tissueService;

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private FactorService factorService;

    @Autowired
    private FactorValueService factorValueService;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private IFNVariationService ifnVariationService;

    @Autowired
    private IFNTypeService ifnTypeService;

    @Autowired
    private DataService dataService;

    @Autowired
    private GeneService geneService;

    @Autowired
    private EvidenceCodeService evidenceCodeService;

    @Autowired
    private GeneOntologyService geneOntologyService;

    @Autowired
    private GoDomainService goDomainService;

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private ProbeService probeService;
    private Logger logger = Logger.getLogger("importProbe");  //this.getClass().getName()

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void setAvatarService(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    public void setAuditEventService(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * @param fileService the fileService to set
     */
    public void setFileService(FileSystemSerivce fileService) {
        this.fileService = fileService;
    }

    /**
     * @param experimentService the experimentService to set
     */
    public void setExperimentService(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    /**
     * @param permissionService the permissionService to set
     */
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setPermRequestService(PermissionRequestService permRequestService) {
        this.permRequestService = permRequestService;
    }

    public void setPartyService(PartyService partyService) {
        this.partyService = partyService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public void setLicenceService(LicenceService licenceService) {
        this.licenceService = licenceService;
    }

    public void setRifcsService(RIFCSService rifcsService) {
        this.rifcsService = rifcsService;
    }

    public void setReporterService(ReporterService reporterService) {
        this.reporterService = reporterService;
    }

    public void setFactorService(FactorService factorService) {
        this.factorService = factorService;
    }

    public void setFactorValueService(FactorValueService factorValueService) {
        this.factorValueService = factorValueService;
    }

    public void setDatasetService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setIfnVariationService(IFNVariationService ifnVariationService) {
        this.ifnVariationService = ifnVariationService;
    }

    public void setIfnTypeService(IFNTypeService ifnTypeService) {
        this.ifnTypeService = ifnTypeService;
    }

    public void setGeneService(GeneService geneService) {
        this.geneService = geneService;
    }

    public void setEvidenceCodeService(EvidenceCodeService evidenceCodeService) {
        this.evidenceCodeService = evidenceCodeService;
    }

    public void setGeneOntologyService(GeneOntologyService geneOntologyService) {
        this.geneOntologyService = geneOntologyService;
    }

    public void setGoDomainService(GoDomainService goDomainService) {
        this.goDomainService = goDomainService;
    }

    public void setOntologyService(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    public void setProbeService(ProbeService probeService) {
        this.probeService = probeService;
    }

    @Override
    public void updateProfile(Profile profile) {
        this.profileService.updateProfile(profile);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getAvatarByUserId(long)
      */
    @Override
    public Avatar getAvatarByUserId(long userId) {
        return this.avatarService.getUserAvatar(userId);
    }

    @Override
    public void updateAvatar(Avatar avatar) {
        this.avatarService.updateAvatar(avatar);
    }

    @Override
    public void saveAuditEvent(AuditEvent event) {
        this.auditEventService.saveAuditEvent(event);
    }

    @Override
    public void deleteEventByIdWithUserId(long eId, long userId) {
        this.auditEventService.deleteEventByIdWithUserId(eId, userId);
    }

    @Override
    public AuditEvent getAuditEventById(long eid) {
        return this.auditEventService.getAuditEventById(eid);
    }

    @Override
    public Pagination<AuditEvent> getEventByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.auditEventService.getEventByUserId(uid, startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml) {
        this.mailService.sendMail(emailFrom, emailTo, emailSubject, emailBody, isHtml);
    }

    @Override
    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile,
                         boolean isHtml) {
        this.mailService.sendMail(emailFrom, emailTo, emailSubject, templateValues, templateFile, isHtml);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#createExperiment(edu.monash.merc.domain.Experiment)
      */
    @Override
    public void createExperiment(Experiment experiment) {
        this.experimentService.saveExperiment(experiment);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExperiment(edu.monash.merc.domain.Experiment)
      */
    @Override
    public void deleteExperiment(Experiment experiment) {
        //remove all permissions by this experiment id
        this.deleteExpAllPermsByExpId(experiment.getId());
        //remove all permission requests by this experiment id
        this.deletePermissionRequestsByExpId(experiment.getId());
        //final remove the experiment
        this.experimentService.deleteExperiment(experiment);

    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deletePublishedExperiment(edu.monash.merc.domain.Experiment,
      * java.lang.String)
      */
    @Override
    public void deletePublishedExperiment(Experiment experiment, String rifcsRootPath) {
        this.deleteExperiment(experiment);
        // then delete it from the rifcs storage
        String uuidkey = experiment.getUniqueKey();
        // delete the file
        try {
            this.fileService.deleteFile(rifcsRootPath + File.separator + MercUtil.pathEncode(uuidkey) + ".xml");
        } catch (Exception e) {
            throw new DCException(e);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#updateExperiment(edu.monash.merc.domain.Experiment)
      */
    @Override
    public void updateExperiment(Experiment experiment) {
        this.experimentService.updateExperiment(experiment);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExperimentsByUserId(long, int, int,
      * edu.monash.merc.common.sql.OrderBy[])
      */
    @Override
    public Pagination<Experiment> getExperimentsByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.experimentService.getExperimentsByUserId(uid, startPageNo, recordsPerPage, orderBys);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getAllPublicExperiments(int, int, edu.monash.merc.common.sql.OrderBy[])
      */
    @Override
    public Pagination<Experiment> getAllPublicExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.experimentService.getAllPublicExperiments(startPageNo, recordsPerPage, orderBys);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getAllExperiments(int, int, edu.monash.merc.common.sql.OrderBy[])
      */
    @Override
    public Pagination<Experiment> getAllExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.experimentService.getAllExperiments(startPageNo, recordsPerPage, orderBys);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExperimentById(long)
      */
    @Override
    public Experiment getExperimentById(long id) {
        return this.experimentService.getExperimentById(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExperimentByExpIdUsrId(long, long)
      */
    @Override
    public Experiment getExperimentByExpIdUsrId(long expId, long uid) {
        return this.experimentService.getExperimentByExpIdUsrId(expId, uid);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#checkExperimentNameExisted(java.lang.String)
      */
    @Override
    public boolean checkExperimentNameExisted(String expName) {
        return this.experimentService.checkExperimentNameExisted(expName);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExpPermsForUser(long, long)
      */
    @Override
    public List<Permission> getExpPermsForUser(long expId, long permForUsrId) {
        return this.permissionService.getExpPermsByUsrIdExpId(permForUsrId, expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExpDefaultPerms(long)
      */
    @Override
    public List<Permission> getExpDefaultPerms(long expId) {
        return this.permissionService.getExpDefaultPerms(expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExpPermsByExpId(long)
      */
    @Override
    public List<Permission> getExpPermsByExpId(long expId) {
        return this.permissionService.getExpPermsByExpId(expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#getExpPermForAnonymous(long)
      */
    @Override
    public Permission getExpPermForAnonymous(long expId) {
        return this.permissionService.getAnonymousPermsByExpId(expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#createExpUserPerm(edu.monash.merc.domain.Permission)
      */
    @Override
    public void createExpUserPerm(Permission permission) {
        this.permissionService.savePermission(permission);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#updateExpUserPerm(edu.monash.merc.domain.Permission)
      */
    @Override
    public void updateExpUserPerm(Permission permission) {
        this.permissionService.updatePermission(permission);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExpUserPerm(edu.monash.merc.domain.Permission)
      */
    @Override
    public void deleteExpUserPerm(Permission permission) {
        this.permissionService.deletePermission(permission);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExpPermByPermId(long)
      */
    @Override
    public void deleteExpPermByPermId(long permId) {
        this.permissionService.deletePermByPermId(permId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExpAllPermsByExpId(long)
      */
    @Override
    public void deleteExpAllPermsByExpId(long expId) {
        this.permissionService.deleteAllPermsByExpId(expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#createExpUserPerms(java.util.List)
      */
    @Override
    public void createExpUserPerms(List<Permission> permissions) {
        for (Permission pm : permissions) {
            this.createExpUserPerm(pm);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#updateExpUserPerms(java.util.List)
      */
    @Override
    public void updateExpUserPerms(List<Permission> permissions) {
        for (Permission pm : permissions) {
            this.updateExpUserPerm(pm);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExpUserPerms(java.util.List)
      */
    @Override
    public void deleteExpUserPerms(List<Permission> permissions) {
        for (Permission pm : permissions) {
            this.deleteExpUserPerm(pm);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#deleteExpUserPermsByPermIds(java.util.List)
      */
    @Override
    public void deleteExpUserPermsByPermIds(List<Long> permIds) {
        for (Long pid : permIds) {
            this.deleteExpPermByPermId(pid);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.DMService#saveExpUserPerms(edu.monash.merc.dto.AssignedPermsBean)
      */
    @Override
    public void saveExpUserPerms(AssignedPermsBean assignedPerms) {
        createExpUserPerms(assignedPerms.getNewPerms());
        updateExpUserPerms(assignedPerms.getUpdatedPerms());
        deleteExpUserPermsByPermIds(assignedPerms.getDeletedPermIds());
    }

    @Override
    public void saveRequestedPerms(ManagablePerm<Permission> mgPerms, long requestPermId) {
        Permission grantPerm = mgPerms.getPerm();
        if (mgPerms.getManagablePermType().equals(MPermBeanType.DELETEREQ)) {
            this.deleteExpPermByPermId(grantPerm.getId());
        } else if (mgPerms.getManagablePermType().equals(MPermBeanType.NEWREQ)) {
            this.createExpUserPerm(grantPerm);
        } else if (mgPerms.getManagablePermType().equals(MPermBeanType.UPDATEREQ)) {
            this.updateExpUserPerm(grantPerm);
        } else {
            //others do nothing
        }
        //finally have to delete the permission request'
        this.deletePermissionRequestById(requestPermId);
    }

    @Override
    public void savePermissionRequest(PermissionRequest permRequest) {
        this.permRequestService.savePermissionRequest(permRequest);
    }

    @Override
    public PermissionRequest getPermissionReqById(long id) {
        return this.permRequestService.getPermissionRequestById(id);
    }

    @Override
    public List<PermissionRequest> getPermissionRequestsByOwner(long ownerId) {
        return this.permRequestService.getPermissionRequestsByOwner(ownerId);
    }

    @Override
    public void deletePermissionRequestsByExpId(long expId) {
        this.permRequestService.deletePermissionRequestsByExpId(expId);
    }

    @Override
    public void deletePermissionRequestById(long pmReqId) {
        this.permRequestService.deletePermissionRequestById(pmReqId);
    }

    @Override
    public PermissionRequest getExpPermissionRequestByReqUser(long expId, long reqUserId) {
        return this.permRequestService.getExpPermissionRequestByReqUser(expId, reqUserId);
    }

    @Override
    public void updatePermissionRequest(PermissionRequest permRequest) {
        this.permRequestService.updatePermissionRequest(permRequest);
    }

    @Override
    public long getTotalPermRequests(long ownerId) {
        return this.permRequestService.getTotalPermRequests(ownerId);
    }

    @Override
    public void approveExp(Experiment experiment) {
        //update the experiment first to avoid the two objects identical issue in hibernate
        this.updateExperiment(experiment);
        //get the experiment permissions
        List<Permission> expPerms = this.getExpPermsByExpId(experiment.getId());
        //get updated new permissions for this experiment
        AssignedPermsBean newAssignedPerms = assignExpPublicPerms(expPerms);
        //update the new assigned permissions
        this.saveExpUserPerms(newAssignedPerms);
    }

    private AssignedPermsBean assignExpPublicPerms(List<Permission> expPerms) {
        //updated permissions
        List<Permission> newPermsFinal = new ArrayList<Permission>();
        List<Permission> updatedPermsFinal = new ArrayList<Permission>();
        List<Long> deletedPermIdsFinal = new ArrayList<Long>();
        AssignedPermsBean assignPms = new AssignedPermsBean();

        //previous existed permissions
        Permission permForAllRegUser = new Permission();
        Permission permForAnonyUser = new Permission();
        List<Permission> individualPerms = new ArrayList<Permission>();
        for (Permission perm : expPerms) {
            // get default permissions for all-registered user
            if (perm.getPermType().equals(PermType.ALLREGUSER.code())) {
                permForAllRegUser = perm;
            } else if (perm.getPermType().equals(PermType.ANONYMOUS.code())) {
                permForAnonyUser = perm;
            } else {
                individualPerms.add(perm);
            }
        }

        //set view-allowed to true as publicly access
        permForAnonyUser.setViewAllowed(true);

        //all-register-user will inherit the perms from the anonymous user's permissions
        if (permForAnonyUser.isViewAllowed()) {
            permForAllRegUser.setViewAllowed(true);
        }
        if (permForAnonyUser.isUpdateAllowed()) {
            permForAllRegUser.setUpdateAllowed(true);
        }
        if (permForAnonyUser.isImportAllowed()) {
            permForAllRegUser.setImportAllowed(true);
        }
        if (permForAnonyUser.isExportAllowed()) {
            permForAllRegUser.setExportAllowed(true);
        }
        if (permForAnonyUser.isDeleteAllowed()) {
            permForAllRegUser.setDeleteAllowed(true);
        }
        if (permForAnonyUser.isChangePermAllowed()) {
            permForAllRegUser.setChangePermAllowed(true);
        }

        //for each individual registered user
        for (Permission indPerm : individualPerms) {
            // inherited the permissions from anonymous user
            if (permForAnonyUser.isViewAllowed()) {
                indPerm.setViewAllowed(true);
            }
            if (permForAnonyUser.isUpdateAllowed()) {
                indPerm.setUpdateAllowed(true);
            }
            if (permForAnonyUser.isImportAllowed()) {
                indPerm.setImportAllowed(true);
            }
            if (permForAnonyUser.isExportAllowed()) {
                indPerm.setExportAllowed(true);
            }
            if (permForAnonyUser.isDeleteAllowed()) {
                indPerm.setDeleteAllowed(true);
            }
            if (permForAnonyUser.isChangePermAllowed()) {
                indPerm.setChangePermAllowed(true);
            }
            ManagablePerm<Permission> mgIndividualPerm = new ManagablePerm<Permission>();
            mgIndividualPerm.setPerm(indPerm);
            ManagablePerm<Permission> mgIndividualPermFinal = sortIndividualUserPerm(mgIndividualPerm, permForAllRegUser, permForAnonyUser);
            if (mgIndividualPermFinal != null) {
                Permission perm = mgIndividualPermFinal.getPerm();
                if (mgIndividualPermFinal.getManagablePermType().equals(MPermBeanType.DELETEREQ)) {
                    deletedPermIdsFinal.add(perm.getId());
                }
                if (mgIndividualPermFinal.getManagablePermType().equals(MPermBeanType.NEWREQ)) {
                    perm.setId(0);
                    newPermsFinal.add(perm);
                }
                if (mgIndividualPermFinal.getManagablePermType().equals(MPermBeanType.UPDATEREQ)) {
                    updatedPermsFinal.add(perm);
                }
            }
        }
        updatedPermsFinal.add(permForAllRegUser);
        updatedPermsFinal.add(permForAllRegUser);
        //get updated permissions
        assignPms.setNewPerms(newPermsFinal);
        assignPms.setUpdatedPerms(updatedPermsFinal);
        assignPms.setDeletedPermIds(deletedPermIdsFinal);
        return assignPms;
    }

    private ManagablePerm<Permission> sortIndividualUserPerm(ManagablePerm<Permission> permForIndividual, Permission allUserPerm, Permission anonyPerm) {
        Permission indivPerm = permForIndividual.getPerm();

        // If none permission is allowed for the all-registered-user, the anonymous user and this individual user:
        // a). If this individual user permission is new, we just ignore it. as it's the same as the permissions
        // for the all-registered-user and the anonymous user.
        // b). If this individual user permission already existed, we have to remove it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && indivPerm.isNonePerm()) {
            if (indivPerm.getId() == 0) {
                permForIndividual.setManagablePermType(MPermBeanType.IGNOREREQ);
            } else {
                permForIndividual.setManagablePermType(MPermBeanType.DELETEREQ);
            }
            return permForIndividual;
        }
        // if none permission is assigned for the anonymous user and the all-registered-user, but assigned for the
        // individual user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && !indivPerm.isNonePerm()) {
            if (indivPerm.getId() == 0) {
                permForIndividual.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                permForIndividual.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return permForIndividual;
        }

        // if none permission is assigned for the anonymous user and the individual user. but assigned to the
        // all-registered-user, which mean the owner would not give any permissions to this registered user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && !allUserPerm.isNonePerm() && indivPerm.isNonePerm()) {
            if (indivPerm.getId() == 0) {
                permForIndividual.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                permForIndividual.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return permForIndividual;
        }

        // if the permission is assigned for the all-registered-user and the individual user:
        // 1. If this individual user permission is new:
        // a): if the individual permissions are the same as the all-registered-user permissions, just ignore
        // b): otherwise we create a new permission for the individual user.
        //
        // 2). If this individual user permission already existed:
        // a): if the individual permissions are the same as the all-registered-user permissions, just remove it.
        // b): otherwise we update permission for the individual user.
        if (!allUserPerm.isNonePerm() && !indivPerm.isNonePerm()) {
            if (indivPerm.getId() == 0) {
                if ((isSamePerms(allUserPerm, indivPerm))) {
                    permForIndividual.setManagablePermType(MPermBeanType.IGNOREREQ);
                } else {
                    // create a new permission
                    permForIndividual.setManagablePermType(MPermBeanType.NEWREQ);
                }
            } else {
                if ((isSamePerms(allUserPerm, indivPerm))) {
                    permForIndividual.setManagablePermType(MPermBeanType.DELETEREQ);
                } else {
                    permForIndividual.setManagablePermType(MPermBeanType.UPDATEREQ);
                }
            }
            return permForIndividual;
        }
        return null;
    }


    private boolean isSamePerms(Permission aPerm, Permission bPerm) {
        if ((aPerm.isViewAllowed() != bPerm.isViewAllowed()) || (aPerm.isUpdateAllowed() != bPerm.isUpdateAllowed())
                || (aPerm.isImportAllowed() != bPerm.isImportAllowed()) || (aPerm.isExportAllowed() != bPerm.isExportAllowed())
                || (aPerm.isDeleteAllowed() != bPerm.isDeleteAllowed()) || (aPerm.isChangePermAllowed() != bPerm.isChangePermAllowed())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Party> getPartiesByExpId(long expId) {
        return this.partyService.getPartiesByExpId(expId);
    }

    @Override
    public Party getPartyByPartyKey(String partyKey) {
        return this.partyService.getPartyByPartyKey(partyKey);
    }

    @Override
    public Party getPartyByUsrNameAndEmail(String firstName, String lastName, String email) {
        return this.partyService.getPartyByUsrNameAndEmail(firstName, lastName, email);
    }

    @Override
    public void saveParty(Party party) {
        this.partyService.saveParty(party);
    }

    @Override
    public void updateParty(Party party) {
        this.partyService.updateParty(party);
    }

    @Override
    public List<Activity> getActivitiesByExpId(long expId) {
        return this.activityService.getActivitiesByExpId(expId);
    }

    @Override
    public Activity getActivityByActKey(String activityKey) {
        return this.activityService.getActivityByActKey(activityKey);
    }

    @Override
    public void saveActivity(Activity activity) {
        this.activityService.saveActivity(activity);
    }

    @Override
    public void saveLicence(Licence licence) {
        this.licenceService.saveLicence(licence);
    }

    @Override
    public void updateLicence(Licence licence) {
        this.licenceService.updateLicence(licence);
    }

    @Override
    public Licence getLicenceByExpId(long expId) {
        return this.licenceService.getLicenceByExpId(expId);
    }

    @Override
    public Licence getLicenceById(long lId) {
        return this.licenceService.getLicenceById(lId);
    }

    @Override
    public void saveMetaRegInfo(MDRegistrationBean mdRegistrationBean) {
        List<PartyBean> partyList = mdRegistrationBean.getPartyList();
        // parties
        List<Party> parties = new ArrayList<Party>();
        for (PartyBean partybean : partyList) {
            Party p = null;
            if (!partybean.isFromRm()) {
                // search the party detail by the person's first name and last name from the database;
                p = getPartyByUsrNameAndEmail(partybean.getPersonGivenName(), partybean.getPersonFamilyName(), partybean.getEmail());
            } else {
                // search the party detail by the party's key
                p = getPartyByPartyKey(partybean.getPartyKey());
            }
            // if party not found from the database, we just save it into database;
            if (p == null) {
                p = copyPartyBeanToParty(partybean);
                saveParty(p);
            }
            parties.add(p);
        }
        Experiment experiment = mdRegistrationBean.getExperiment();
        //set the parties
        experiment.setParties(parties);
        //activity:

        List<ProjectBean> actList = mdRegistrationBean.getActivityList();
        // activities
        List<Activity> activities = new ArrayList<Activity>();
        if (actList != null) {
            for (ProjectBean projbean : actList) {
                Activity act = this.getActivityByActKey(projbean.getActivityKey());
                if (act == null) {
                    act = new Activity();
                    act.setActivityKey(projbean.getActivityKey());
                    this.saveActivity(act);
                }
                activities.add(act);
            }
        }
        //set the activities
        experiment.setActivities(activities);
        //update experiment
        this.updateExperiment(experiment);

        //save the licence:
        Licence licence = mdRegistrationBean.getLicence();
        licence.setExperiment(experiment);
        if (licence.getId() == 0) {
            Licence existedLicence = this.getLicenceByExpId(experiment.getId());
            if (existedLicence != null) {
                licence.setId(existedLicence.getId());
                this.updateLicence(licence);
            } else {
                this.saveLicence(licence);
            }
        } else {
            this.updateLicence(licence);
        }

        //register the metada:
        this.rifcsService.publishExpRifcs(mdRegistrationBean);
    }

    private Party copyPartyBeanToParty(PartyBean pb) {
        Party pa = new Party();
        pa.setPartyKey(pb.getPartyKey());
        pa.setPersonTitle(pb.getPersonTitle());
        pa.setPersonGivenName(pb.getPersonGivenName());
        pa.setPersonFamilyName(pb.getPersonFamilyName());
        pa.setUrl(pb.getUrl());
        pa.setEmail(pb.getEmail());
        pa.setAddress(pb.getAddress());
        pa.setIdentifierType(pb.getIdentifierType());
        pa.setIdentifierValue(pb.getIdentifierValue());
        pa.setOriginateSourceType(pb.getOriginateSourceType());
        pa.setOriginateSourceValue(pb.getOriginateSourceValue());
        pa.setGroupName(pb.getGroupName());
        pa.setFromRm(pb.isFromRm());
        return pa;
    }

    @Override
    public void saveReporter(Reporter reporter) {
        this.reporterService.saveReporter(reporter);
    }

    @Override
    public void mergeReporter(Reporter reporter) {
        this.reporterService.mergeReporter(reporter);
    }

    @Override
    public void updateReporter(Reporter reporter) {
        this.reporterService.updateReporter(reporter);
    }


    @Override
    public Pagination<Reporter> getReporters(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.reporterService.getReporters(startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public Reporter getReporterByProbeId(String probeId) {
        return this.reporterService.getReporterByProbeId(probeId);
    }

    @Override
    public RepCounter importAllReporters(List<Reporter> reporters) {
        int countUpdated = 0;
        int countNew = 0;
        //reporters counter
        RepCounter counter = new RepCounter();

        for (Reporter reporter : reporters) {
            if (StringUtils.isNotBlank(reporter.getProbeId())) {
                Reporter existedReporter = this.getReporterByProbeId(reporter.getProbeId());
                if (existedReporter != null) {
                    reporter.setId(existedReporter.getId());
                    // this.mergeReporter(reporter);
                    this.updateReporter(reporter);
                    //count how many reporters have been updated
                    countUpdated++;
                } else {
                    this.saveReporter(reporter);
                    //count how many reporters are new
                    countNew++;
                }
            }
        }
        counter.setTotalUpdated(countUpdated);
        counter.setTotalNew(countNew);
        return counter;
    }


    @Override
    public void importReporters(ReporterBean reporterBean) {
        ImportReporterThread reporterThread = new ImportReporterThread(this, reporterBean);
        reporterThread.importReporter();
    }

    //Probes
    @Override
    public void saveProbe(Probe probe) {
        this.probeService.saveProbe(probe);
    }

    @Override
    public void mergeProbe(Probe probe) {
        this.probeService.mergeProbe(probe);
    }

    @Override
    public void updateProbe(Probe probe) {
        this.probeService.updateProbe(probe);
    }


    @Override
    public Pagination<Probe> getProbes(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.probeService.getProbes(startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public Probe getProbeByProbeId(String probeId) {
        return this.probeService.getProbeByProbeId(probeId);
    }

    @Override
    public ProbCounter importAllProbes(List<Probe> probes) {
        int countUpdated = 0;
        int countNew = 0;
        //probes counter
        ProbCounter counter = new ProbCounter();

        for (Probe probe : probes) {
            if (StringUtils.isNotBlank(probe.getProbeId())) {
                // fill species ID, ensembl ID then save
                // find out if the probe is already in the database
                Probe existedProbe = this.getProbeByProbeId(probe.getProbeId());
                logger.debug("PROBE ID: "+probe.getProbeId());

                // set the species ID from the name
                Species speciesN =this.getSpeciesByName(probe.getSpeciesName());
                probe.setSpecies(speciesN);
                logger.debug("PROBE SPECIES: "+probe.getSpecies().getSpeciesName());

                // get the gene ID (many-to-many) - set it after including existing gene list
                Gene gene = this.getGeneByEnsgAccession(probe.getEnsemblId());

                // if there is no matching gene, log the error and skip this probe
                if (gene != null) {
                    logger.debug("PROBE MATCHED GENE: "+gene.getGeneName());
                    logger.debug("geneID: "+gene.getId());
                } else {
                    logger.debug("GENE IS NULL");
                    importError(probe, "No matching gene in database");
                    continue;
                }

                if (existedProbe != null) {
                    probe.setId(existedProbe.getId());
                    probe.getGenes();
                    List<Gene> geneList = this.getGenesByProbeId(probe.getProbeId());

                    if (gene != null && !geneList.contains(gene)) {
                        geneList.add(gene);
                    }
                    // set the gene ID,
                    probe.setGenes(geneList);
                    // update.
                    this.mergeProbe(probe);
                    this.updateProbe(probe);
                    //count how many probes have been updated
                    countUpdated++;
                } else {
                    ArrayList<Gene> geneList = new ArrayList<Gene>();
                    if (gene != null) {
                        logger.debug("Adding gene to geneList");
                        geneList.add(gene);
                        probe.setGenes(geneList);
                    }
                    this.saveProbe(probe);
                    //coimportErrorunt how many probes are new
                    countNew++;
                }
            } else {
                importError(probe, "Blank ID column");
            }
        }
        counter.setTotalUpdated(countUpdated);
        counter.setTotalNew(countNew);
        return counter;
    }

    /**
        Message the user through email that this probe can not be created, as the gene it references isn't in our
        database.
     */
    private void importError(Probe probe, String msg) {
        logger.warn("The following probe failed to import, with message '"+msg+"': " + probe.getProbeId());
        importErrors.add(probe);
        importErrorMessages.add(msg);
    }

    @Override
    public List<Probe> getProbeImportErrors() {
        return importErrors;
    }

    @Override
    public List<String> getProbeImportErrorMessages(){
        return importErrorMessages;
    }

    private List<Probe> importErrors = new ArrayList<Probe>();
    private List<String> importErrorMessages = new ArrayList<String>() ;

    @Override
    public void importProbe(ProbeBean probeBean) {
        ImportProbeThread probeThread = new ImportProbeThread(this, probeBean);
        probeThread.importProbe();
    }

    @Override
    public Probe getProbeById(long id) {
        return this.probeService.getProbeById(id);
    }


    @Override
    public List<Probe> getProbesByGeneAccession(String geneAccession) {
        return this.probeService.getProbesByGeneAccession(geneAccession);
    }

    @Override
    public List<Probe> getProbeBySpecies(String speciesName){
        return this.probeService.getProbeBySpecies(speciesName);
    }
    @Override
    public List<Probe> getProbesByGeneId(long geneId) {
        return this.probeService.getProbesByGeneId(geneId);
    }

    @Override
    public void importProbes(List<ProbeGeneBean> probeGeneBeans) {

        if (probeGeneBeans != null) {
            for (ProbeGeneBean probeGeneBean : probeGeneBeans) {
                String ensgAc = probeGeneBean.getEnsgAccession();
                String probeId = probeGeneBean.getProbeId();
                String speciesName = probeGeneBean.getSpeciesName();
                Gene gene = this.getGeneByEnsgAccession(ensgAc);
                Species speciesN = this.getSpeciesByName(speciesName);
                //if not save the species, the following code will get db exception when probe is referencing the species id.
                Probe probe = this.getProbeByProbeId(probeId);
                if (gene != null) {
                    if (probe == null) {
                        probe = new Probe();
                        probe.setProbeId(probeId);
                        probe.setSpecies(speciesN);
                    }
                    List<Gene> geneList = this.getGenesByProbeId(probe.getProbeId());
                    if (geneList != null) {
                        if (!geneList.contains(gene)) {
                            geneList.add(gene);
                        }
                    } else {
                        geneList = new ArrayList<Gene>();
                        geneList.add(gene);
                    }
                    probe.setGenes(geneList);
                    //check if primary key id is zero which means new entity,
                    if (probe.getId() == 0) {
                        this.saveProbe(probe);
                    } else {
                        this.mergeProbe(probe);
                    }
                }
            }
        }
    }
    //TFSite

    @Override
    public void saveTFSite(TFSite tfSite) {
        this.tfSiteService.saveTFSite(tfSite);
    }

    @Override
    public void mergeTFSite(TFSite tfSite) {
        this.tfSiteService.mergeTFSite(tfSite);
    }

    @Override
    public void updateTFSite(TFSite tfSite) {
        this.tfSiteService.updateTFSite(tfSite);
    }

    @Override
    public TFSite getTFSite(TFSite tfSite) {
        return this.tfSiteService.getTFSite(tfSite);
    }

    @Override
    public TfSiteCounter importAllTFSites(List<TFSite> tfSites) {
        int countUpdated = 0;
        int countNew = 0;
        //reporters counter
        TfSiteCounter counter = new TfSiteCounter();

        for (TFSite tfSite : tfSites) {
            Gene gene = this.getGeneByEnsgAccession(tfSite.getEnsemblID());
            if (gene != null) {
                tfSite.setGene(gene);
                if (StringUtils.isNotBlank(tfSite.getFactor())) {
                    TFSite existedTFSite = this.getTFSite(tfSite);
                    if (existedTFSite != null) {
                        tfSite.setId(existedTFSite.getId());
                        // this.mergeReporter(reporter);
                        this.updateTFSite(tfSite);
                        //count how many reporters have been updated
                        countUpdated++;
                    } else {
                        this.saveTFSite(tfSite);
                        //count how many reporters are new
                        countNew++;
                    }
                }
            }
        }
        counter.setTotalUpdated(countUpdated);
        counter.setTotalNew(countNew);
        return counter;


    }

    @Override
    public void importTFSite(TFSiteBean tfSiteBean) {
        ImportTFSiteThread tfSiteThread = new ImportTFSiteThread(this, tfSiteBean);
        tfSiteThread.importTFSite();
    }

    // Tissues
    @Override
    public TissueExpression getTissueExpressionById(long id) {
        return this.tissueExpressionService.getTissueExpressionById(id);
    }

    @Override
    public Tissue getTissueByName(String tissueId){
        return this.tissueService.getTissueByName(tissueId);
    }

    @Override
    public void saveTissueExpression(TissueExpression tissueExpression){
        this.tissueExpressionService.saveTissueExpression(tissueExpression);
    }
    @Override
    public void updateTissueExpression(TissueExpression tissueExpression){
        this.tissueExpressionService.updateTissueExpression(tissueExpression);
    }
    @Override
    public void mergeTissueExpression(TissueExpression tissueExpression){
        this.tissueExpressionService.mergeTissueExpression(tissueExpression);
    }
    @Override
    public void deleteTissueExpression(TissueExpression tissueExpression){
        this.tissueExpressionService.deleteTissueExpression(tissueExpression);
    }

    @Override
    public List<TissueExpression> getTissueByProbeId(String probeId){
        return this.tissueExpressionService.getTissueByProbeId(probeId);
    }
    @Override
    public List<TissueExpression> getTissueByTissueId(String tissueId){
        return this.tissueExpressionService.getTissueByTissueId(tissueId);
    }

    @Override
    public TissueExpression getTissueExpressionByProbeAndTissue(Probe probe, Tissue tissue){
        return this.tissueExpressionService.getTissueExpressionByProbeAndTissue(probe, tissue);
    }


    @Override
    public TissueCounter importAllTissues(List<TissueExpression> tissues){
        int countUpdated = 0;
        int countNew = 0;
        //tissue counter
        TissueCounter counter = new TissueCounter();

        for (TissueExpression tissueExpressions : tissues) {
            Probe probeId  = this.getProbeByProbeId(tissueExpressions.getProbeId());
            Tissue tissueN =this.getTissueByName(tissueExpressions.getTissueId());
            tissueExpressions.setTissue(tissueN);
            tissueExpressions.setProbe(probeId);
            if (probeId != null) {
            TissueExpression existingTissue = this.getTissueExpressionByProbeAndTissue(tissueExpressions.getProbe(),tissueExpressions.getTissue());
                  if (existingTissue  != null)  {
                      tissueExpressions.setId(existingTissue.getId());
                      this.updateTissueExpression(tissueExpressions);
                      countUpdated++;

                   }  else {
                      this.saveTissueExpression(tissueExpressions);
                      countNew++;
                   }

            }
        }
        counter.setTotalUpdated(countUpdated);
        counter.setTotalNew(countNew);
        return counter;
    }

    @Override
    public void importTissue(TissueBean tissueBean){
        ImportTissueThread tissueThread = new ImportTissueThread(this, tissueBean);
        tissueThread.importTissue();
    }



    //Species

    @Override
    public Species getSpeciesById(long id) {
        return this.speciesService.getSpeciesById(id);
    }


    @Override
    public void saveSpecies(Species species) {
        this.speciesService.saveSpecies(species);
    }

    @Override
    public void mergeSpecies(Species species) {
        this.speciesService.mergeSpecies(species);
    }

    @Override
    public void updateSpecies(Species species) {
        this.speciesService.updateSpecies(species);
    }

    @Override
    public Pagination<Species> getSpecies(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.speciesService.getSpecies(startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public Species getSpeciesByName(String speciesName) {
        return this.speciesService.getSpeciesByName(speciesName);
    }

    //Dataset

    @Override
    public void saveDataset(Dataset dataset) {
        this.datasetService.saveDataset(dataset);
    }

    @Override
    public Dataset getDataset(long dsId) {
        return this.datasetService.getDatasetById(dsId);
    }

    @Override
    public void deleteDataset(Dataset dataset) {
        this.datasetService.deleteDataset(dataset);
    }

    @Override
    public void deleteDatasetById(long datasetId) {
        this.datasetService.deleteDatasetById(datasetId);
    }

    @Override
    public boolean checkExperimentDatasetExisted(long expId, String datasetName) {
        return this.datasetService.checkExperimentDatasetExisted(expId, datasetName);
    }

    @Override
    public List<Dataset> getDatasetsByExpId(long expId) {
        return this.datasetService.getDatasetsByExpId(expId);
    }

    @Override
    public int getTotalDatasetsNumber(long expId) {
        return this.datasetService.getTotalDatasetsNumber(expId);
    }

    @Override
    public Pagination<SearchResultRow> getDataByDatasetId(long dsId, int startPageNo, int recordsPerPage, String orderBy, String sortBy) {
        return this.dataService.getDataByDatasetId(dsId, startPageNo, recordsPerPage, orderBy, sortBy);
    }

    @Override
    public String importExpDataset(Experiment experiment, BaseDataset txtDataset) {

        long starttime = System.currentTimeMillis();

        // populate factorValues
        ExpFactor[] expFactors = txtDataset.getExpFactors();

        List<ExperimentFactorValue> factorValueList = new ArrayList<ExperimentFactorValue>();

        for (ExpFactor tef : expFactors) {
            String factor = tef.getName();
            String fValue = tef.getValue();

            ExperimentFactorValue efv = this.getFactorValueByNameValuePair(factor, fValue);
            if (null == efv) {
                ExperimentFactor ef = this.getFactorByName(factor);
                if (null == ef) {
                    ef = new ExperimentFactor();
                    ef.setName(factor);
                    this.saveFactor(ef);
                }
                efv = new ExperimentFactorValue();
                efv.setFactor(ef);
                efv.setFactorValue(fValue);
                efv.setFactor(ef);
            }
            this.saveFactorValue(efv);
            factorValueList.add(efv);
        }

        //Interferon type and subtype:
        IFNTypeFactor ifnTypeFactor = txtDataset.getIfnTypeFactor();
        String typeName = ifnTypeFactor.getTypeName();
        String subTypeName = ifnTypeFactor.getSubTypeName();

        IFNType ifnType = this.getIFNTypeByTypes(typeName, subTypeName);

        if (ifnType == null) {
            ifnType = new IFNType();
            ifnType.setTypeName(typeName);
            ifnType.setSubTypeName(subTypeName);
            this.saveIFNType(ifnType);
        }

        //Interferon normal or abnormal variations
        VarFactor varFactor = txtDataset.getVarFactor();
        boolean isAbnormal = varFactor.isAbnormal();
        String value = varFactor.getVarValue();

        IFNVariation ifnVariation = this.getIFNVariation(isAbnormal, value);

        if (ifnVariation == null) {
            ifnVariation = new IFNVariation();
            ifnVariation.setAbnormal(isAbnormal);
            ifnVariation.setValue(value);
            this.saveIFNVariation(ifnVariation);
        }

        // Create BaseDataset
        Dataset dataset = new Dataset();
        Date date = GregorianCalendar.getInstance().getTime();
        //set created date
        dataset.setCreatedDate(date);
        //set name
        dataset.setName(txtDataset.getName());
        //set genetic variation
        //dataset.setNormalWt(txtDataset.isNormalWt());
        //set invivo or invitro
        dataset.setInVivo(txtDataset.isInVivo());
        //set treatment time
        dataset.setTreatmentTime(txtDataset.getTreatmentTime());
        //set treatment concentration
        dataset.setTreatmentCon(txtDataset.getTreatmentCon());
        //set description
        dataset.setDescription(txtDataset.getDescription());
        //set comment
        dataset.setComment(txtDataset.getComment());
        //set the sample characteristics
        dataset.setSampleChars(txtDataset.getSampleChars());

        //set the interferon type and subtype
        dataset.setIfnType(ifnType);

        //set the interferon normal or abnormal variation
        dataset.setIfnVar(ifnVariation);

        //set factor values
        dataset.setFactorValues(factorValueList);

        //set experiemnt
        dataset.setExperiment(experiment);

        // Create Data
        String[] probes = txtDataset.getProbes();
        String[] data_arr = txtDataset.getData();

        List<Data> dataList = new ArrayList<Data>();
        String missingProbes = "";
        int successCount = 0;
        int failCount = 0;
        for (int i = 0; i < data_arr.length; i++) {
            String data_val = data_arr[i];
            String probeId = probes[i];

            Probe probe = this.getProbeByProbeId(probeId);
            if (probe == null){
                failCount++;
                missingProbes += probeId + ", ";
                continue;
            } else {
                successCount++;
            }
            Data data = new Data();
            data.setDataset(dataset);
            data.setProbe(probe);
            data.setValue(Double.valueOf(data_val));
            dataList.add(data);

        }
        dataset.setData(dataList);
        //save dataset, and save the all data as we
        logger.info("About to save dataset. Datalist size: " + dataList.size());
        logger.debug("dataset: "+dataList.get(0).getDataset());
        logger.debug("probe: "+dataList.get(0).getProbe());
        logger.debug("value: "+dataList.get(0).getValue());
        this.saveDataset(dataset);

        // If any probes were not found, throw error containing list of missing probes and success count.
        String msg = "" ;
        if (failCount > 0)  {
            msg = " - well, " +successCount + " out of "+data_arr.length+" data units were imported successfully, but the following "+failCount+" probes were not found in the database: " + missingProbes;
        }

        long endtime = System.currentTimeMillis();
        return msg;
    }

    @Override
    public void saveFactorValue(ExperimentFactorValue factorValue) {
        this.factorValueService.saveFactorValue(factorValue);
    }

    @Override
    public ExperimentFactorValue getFactorValueByNameValuePair(String fname, String fvalue) {
        return factorValueService.getFactorValueByNameValuePair(fname, fvalue);
    }

    @Override
    public void saveFactor(ExperimentFactor expFactors) {
        this.factorService.saveFactor(expFactors);
    }

    @Override
    public List<ExperimentFactorValue> getFactorValuesByFactorId(long factorId) {
        return this.factorValueService.getFactorValuesByFactorId(factorId);
    }

    @Override
    public ExperimentFactor getFactorByName(String factorName) {
        return this.factorService.getFactorByName(factorName);
    }

    @Override
    public List<NameValueBean> getFactorValuesBeanByDatasetId(long dsId) {
        return this.factorValueService.getFactorValuesBeanByDatasetId(dsId);
    }

    @Override
    public List<String> getFactorValuesByFactorName(String factorName) {
        return this.factorValueService.getFactorValuesByFactorName(factorName);
    }

    @Override
    public void saveIFNType(IFNType ifnType) {
        this.ifnTypeService.saveIFNType(ifnType);
    }

    @Override
    public IFNType getIFNTypeByTypes(String type, String subType) {
        return this.ifnTypeService.getIFNTypeByTypes(type, subType);
    }

    @Override
    public List<IFNType> getIfnTypes(String typeName) {
        return this.ifnTypeService.getIfnTypes(typeName);
    }

    @Override
    public IFNVariation getIFNVariation(boolean isAbnormal, String value) {
        return this.ifnVariationService.getIFNVariation(isAbnormal, value);
    }

    @Override
    public void saveIFNVariation(IFNVariation ifnVariation) {
        this.ifnVariationService.saveIFNVariation(ifnVariation);
    }

    @Override
    public List<String> getAbnormalFactors() {
        return this.ifnVariationService.getAbnormalFactors();
    }

    //Genes
    @Override
    public Gene getGeneById(long id) {
        return this.geneService.getGeneById(id);
    }

    @Override
    public void saveGene(Gene gene) {
        this.geneService.saveGene(gene);
    }

    @Override
    public void mergeGene(Gene gene) {
        this.geneService.mergeGene(gene);
    }

    @Override
    public void updateGene(Gene gene) {
        this.geneService.updateGene(gene);
    }

    @Override
    public void deleteGene(Gene gene) {
        this.geneService.deleteGene(gene);
    }

    @Override
    public Gene getGeneByEnsgAccession(String ensgAccession) {
        return this.geneService.getGeneByEnsgAccession(ensgAccession);
    }

    @Override
    public List<Gene> getGenesByProbeId(String probeId) {
        return this.geneService.getGenesByProbeId(probeId);
    }

    @Override
    public void importGenes(List<Gene> genes, Date importedTime) {
        if (genes != null) {
            for (Gene gene : genes) {
                gene.setImportedTime(importedTime);
                Gene foundGene = this.getGeneByEnsgAccession(gene.getEnsgAccession());
                if (foundGene != null) {
                    gene.setId(foundGene.getId());
                    this.mergeGene(gene);
                } else {
                    this.saveGene(gene);
                }
            }
        }
    }

    //EvidenceCode
    @Override
    public EvidenceCode getEvidenceCodeById(long id) {
        return this.evidenceCodeService.getEvidenceCodeById(id);
    }

    @Override
    public void saveEvidenceCode(EvidenceCode evidenceCode) {
        this.evidenceCodeService.saveEvidenceCode(evidenceCode);
    }

    @Override
    public void mergeEvidenceCode(EvidenceCode evidenceCode) {
        this.evidenceCodeService.mergeEvidenceCode(evidenceCode);
    }

    @Override
    public void updateEvidenceCode(EvidenceCode evidenceCode) {
        this.evidenceCodeService.updateEvidenceCode(evidenceCode);
    }

    @Override
    public void deleteEvidenceCode(EvidenceCode evidenceCode) {
        this.evidenceCodeService.deleteEvidenceCode(evidenceCode);
    }

    @Override
    public EvidenceCode getEvidenceCodeByCode(String code) {
        return this.evidenceCodeService.getEvidenceCodeByCode(code);
    }


    //GeneOntology
    @Override
    public GeneOntology getGeneOntologyById(long id) {
        return this.geneOntologyService.getGeneOntologyById(id);
    }

    @Override
    public void saveGeneOntology(GeneOntology geneOntology) {
        this.geneOntologyService.saveGeneOntology(geneOntology);
    }

    @Override
    public void mergeGeneOntology(GeneOntology geneOntology) {
        this.geneOntologyService.mergeGeneOntology(geneOntology);
    }

    @Override
    public void updateGeneOntology(GeneOntology geneOntology) {
        this.geneOntologyService.updateGeneOntology(geneOntology);
    }

    @Override
    public void deleteGeneOntology(GeneOntology geneOntology) {
        this.geneOntologyService.deleteGeneOntology(geneOntology);
    }

    @Override
    public GeneOntology getGeneOntologyByGeneAndOntology(String ensgAccession, String goTermAccession) {
        return this.geneOntologyService.getGeneOntologyByGeneAndOntology(ensgAccession, goTermAccession);
    }

    //GoDomain
    @Override
    public GoDomain getGoDomainById(long id) {
        return this.goDomainService.getGoDomainById(id);
    }

    @Override
    public void saveGoDomain(GoDomain goDomain) {
        this.goDomainService.saveGoDomain(goDomain);
    }

    @Override
    public void mergeGoDomain(GoDomain goDomain) {
        this.goDomainService.mergeGoDomain(goDomain);
    }

    @Override
    public void updateGoDomain(GoDomain goDomain) {
        this.goDomainService.updateGoDomain(goDomain);
    }

    @Override
    public void deleteGoDomain(GoDomain goDomain) {
        this.goDomainService.deleteGoDomain(goDomain);
    }

    @Override
    public GoDomain getGoDomainByNamespace(String namespace) {
        return this.goDomainService.getGoDomainByNamespace(namespace);
    }


    //Ontology
    @Override
    public Ontology getOntologyById(long id) {
        return this.ontologyService.getOntologyById(id);
    }

    @Override
    public void saveOntology(Ontology ontology) {
        this.ontologyService.saveOntology(ontology);
    }

    @Override
    public void mergeOntology(Ontology ontology) {
        this.ontologyService.mergeOntology(ontology);
    }

    @Override
    public void updateOntology(Ontology ontology) {
        this.ontologyService.updateOntology(ontology);
    }

    @Override
    public void deleteOntology(Ontology ontology) {
        this.ontologyService.deleteOntology(ontology);
    }

    @Override
    public Ontology getOntologyByGoTermAccession(String goTermAccession) {
        return this.ontologyService.getOntologyByGoTermAccession(goTermAccession);
    }

    @Override
    public void importGeneOntologies(List<GeneOntologyBean> geneOntologyBeans) {
        if (geneOntologyBeans != null) {
            for (GeneOntologyBean geneOntologyBean : geneOntologyBeans) {
                String ensgAc = geneOntologyBean.getEnsembleGeneId();
                String goTermAc = geneOntologyBean.getGoTermAccession();
                String goTermName = geneOntologyBean.getGoTermName();
                String goTermDefinition = geneOntologyBean.getGoTermDefinition();
                String goEvCode = geneOntologyBean.getGoTermEvidenceCode();
                String goDomainNamespace = geneOntologyBean.getGoDomain();

                GoDomain goDomain = this.getGoDomainByNamespace(goDomainNamespace);
                if (goDomain == null) {
                    goDomain = new GoDomain();
                    goDomain.setNamespace(goDomainNamespace);
                    this.saveGoDomain(goDomain);
                }

                Ontology ontology = this.getOntologyByGoTermAccession(goTermAc);
                if (ontology == null) {
                    ontology = new Ontology();
                    ontology.setGoTermAccession(goTermAc);
                    ontology.setGoTermDefinition(goTermDefinition);
                    ontology.setGoTermName(goTermName);
                    ontology.setGoDomain(goDomain);
                    this.saveOntology(ontology);
                }

                EvidenceCode evidenceCode = this.getEvidenceCodeByCode(goEvCode);
                if (evidenceCode == null) {
                    evidenceCode = genEvidneceCodeEntity(goEvCode);
                    this.saveEvidenceCode(evidenceCode);
                }

                Gene gene = this.getGeneByEnsgAccession(ensgAc);
                if (gene != null) {
                    GeneOntology geneOntology = this.getGeneOntologyByGeneAndOntology(ensgAc, goTermAc);
                    if (geneOntology == null) {
                        geneOntology = new GeneOntology();
                        geneOntology.setEvidenceCode(evidenceCode);
                        geneOntology.setGene(gene);
                        geneOntology.setOntology(ontology);
                        this.saveGeneOntology(geneOntology);
                    } else {
                        EvidenceCode fEvidenceCode1 = geneOntology.getEvidenceCode();
                        int fRank = fEvidenceCode1.getRank();
                        int currentRank = evidenceCode.getRank();

                        //the smaller rank number should best evidenc
                        if (currentRank < fRank) {
                            geneOntology.setEvidenceCode(evidenceCode);
                            geneOntology.setGene(gene);
                            geneOntology.setOntology(ontology);
                            this.mergeGeneOntology(geneOntology);
                        }
                    }
                }
            }
        }
    }

    private EvidenceCode genEvidneceCodeEntity(String code) {
        String expCode = "EXP";
        String expDesc = "Inferred from Experiment";
        int expRank = 1;

        String idaCode = "IDA";
        String idaDesc = "Inferred from Direct Assay";
        int idaRank = 2;

        String ipiCode = "IPI";
        String ipiDesc = "Inferred from Physical Interaction";
        int ipiRank = 3;

        String impCode = "IMP";
        String impDesc = "Inferred from Mutant Phenotype";
        int impRank = 4;

        String igiCode = "IGI";
        String igiDesc = "Inferred from Genetic Interaction";
        int igiRank = 5;


        String iepCode = "IEP";
        String iepDesc = "Inferred from Expression Pattern";
        int iepRank = 6;

        String issCode = "ISS";
        String issDesc = "Inferred from Sequence or Structural Similarity";
        int issRank = 7;

        String isoCode = "ISO";
        String isoDesc = "Inferred from Sequence Orthology";
        int isoRank = 8;

        String isaCode = "ISA";
        String isaDesc = "Inferred from Sequence Alignment";
        int isaRank = 9;

        String ismCode = "ISM";
        String ismDesc = "Inferred from Sequence Model";
        int ismRank = 10;

        String igcCode = "IGC";
        String igcDesc = "Inferred from Genomic Context";
        int igcRank = 11;

        String ibaCode = "IBA";
        String ibaDesc = "Inferred from Biological aspect of Ancestor";
        int ibaRank = 12;

        String ibdCode = "IBD";
        String ibdDesc = "Inferred from Biological aspect of Descendant";
        int ibdRank = 13;

        String ikrCode = "IKR";
        String ikrDesc = "Inferred from Key Residues";
        int ikrRank = 14;

        String irdCode = "IRD";
        String irdDesc = "Inferred from Rapid Divergence";
        int irdRank = 15;

        String rcaCode = "RCA";
        String rcaDesc = "inferred from Reviewed Computational Analysis";
        int rcaRank = 16;

        String tasCode = "TAS";
        String tasDesc = "Traceable Author Statement";
        int tasRank = 17;

        String nasCode = "NAS";
        String nasDesc = "Non-traceable Author Statement";
        int nasRank = 18;

        String icCode = "IC";
        String icDesc = "Inferred by Curator";
        int icRank = 19;

        String ndCode = "ND";
        String ndDesc = "No biological Data available";
        int ndRank = 20;

        String ieaCode = "IEA";
        String ieaDesc = "Inferred from Electronic Annotation";
        int ieaRank = 21;

        String nrCode = "NR";
        String nrDesc = "Not Recorded";
        int nrRank = 22;

        EvidenceCode evidenceCode = new EvidenceCode();
        if (StringUtils.equals(code, expCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(expDesc);
            evidenceCode.setRank(expRank);
        }
        if (StringUtils.equals(code, idaCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(idaDesc);
            evidenceCode.setRank(idaRank);
        }
        if (StringUtils.equals(code, ipiCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ipiDesc);
            evidenceCode.setRank(ipiRank);
        }
        if (StringUtils.equals(code, impCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(impDesc);
            evidenceCode.setRank(impRank);
        }
        if (StringUtils.equals(code, igiCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(igiDesc);
            evidenceCode.setRank(igiRank);
        }
        if (StringUtils.equals(code, iepCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(iepDesc);
            evidenceCode.setRank(iepRank);
        }
        if (StringUtils.equals(code, issCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(issDesc);
            evidenceCode.setRank(issRank);
        }
        if (StringUtils.equals(code, isoCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(isoDesc);
            evidenceCode.setRank(isoRank);
        }
        if (StringUtils.equals(code, isaCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(isaDesc);
            evidenceCode.setRank(isaRank);
        }
        if (StringUtils.equals(code, ismCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ismDesc);
            evidenceCode.setRank(ismRank);
        }
        if (StringUtils.equals(code, igcCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(igcDesc);
            evidenceCode.setRank(igcRank);
        }
        if (StringUtils.equals(code, ibaCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ibaDesc);
            evidenceCode.setRank(ibaRank);
        }
        if (StringUtils.equals(code, ibdCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ibdDesc);
            evidenceCode.setRank(ibdRank);
        }
        if (StringUtils.equals(code, ikrCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ikrDesc);
            evidenceCode.setRank(ikrRank);
        }
        if (StringUtils.equals(code, irdCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(irdDesc);
            evidenceCode.setRank(irdRank);
        }
        if (StringUtils.equals(code, rcaCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(rcaDesc);
            evidenceCode.setRank(rcaRank);
        }
        if (StringUtils.equals(code, tasCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(tasDesc);
            evidenceCode.setRank(tasRank);
        }
        if (StringUtils.equals(code, nasCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(nasDesc);
            evidenceCode.setRank(nasRank);
        }
        if (StringUtils.equals(code, icCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(icDesc);
            evidenceCode.setRank(icRank);
        }
        if (StringUtils.equals(code, ndCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ndDesc);
            evidenceCode.setRank(ndRank);
        }
        if (StringUtils.equals(code, ieaCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(ieaDesc);
            evidenceCode.setRank(ieaRank);
        }
        if (StringUtils.equals(code, nrCode)) {
            evidenceCode.setCode(code);
            evidenceCode.setDescription(nrDesc);
            evidenceCode.setRank(nrRank);
        }

        return evidenceCode;
    }
}
