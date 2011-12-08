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
package edu.monash.merc.service;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.*;
import edu.monash.merc.util.interferome.dataset.BaseDataset;

import java.util.List;
import java.util.Map;

/**
 * DMService Service Interface
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public interface DMService {

    public void updateProfile(Profile profile);

    public Avatar getAvatarByUserId(long userId);

    public void updateAvatar(Avatar avatar);

    public void saveAuditEvent(AuditEvent event);

    public void deleteEventByIdWithUserId(long eId, long userId);

    public AuditEvent getAuditEventById(long eid);

    public Pagination<AuditEvent> getEventByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys);

    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml);

    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile,
                         boolean isHtml);

    // experiment related functions
    public void createExperiment(Experiment experiment);

    public void deleteExperiment(Experiment experiment);

    public void deletePublishedExperiment(Experiment experiment, String rifcsRootPath);

    public void updateExperiment(Experiment experiment);

    public Pagination<Experiment> getExperimentsByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys);

    /**
     * getAllPublicExperiments method will list all Experiments which are approved by admin and allowed anonymous user
     * access
     *
     * @param startPageNo
     * @param recordsPerPage
     * @param orderBys
     * @return A Pagination object which contains a list of Experiments.
     */
    public Pagination<Experiment> getAllPublicExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys);

    /**
     * getAllExperiments method will get all Experiments which inlcudes all approved an unapproved experiments
     *
     * @param startPageNo
     * @param recordsPerPage
     * @param orderBys
     * @return A Pagination object which contains a list of Experiments.
     */
    public Pagination<Experiment> getAllExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys);

    public Experiment getExperimentById(long id);

    public Experiment getExperimentByExpIdUsrId(long expId, long uid);

    public boolean checkExperimentNameExisted(String expName);

    public List<Permission> getExpPermsForUser(long expId, long permForUsrId);

    /**
     * getExpDefaultPerms method will get all anonymous user and all registered users permissions in the experiment.
     *
     * @param expId
     * @return a List of Permission.
     */
    public List<Permission> getExpDefaultPerms(long expId);

    /**
     * getExpPermsByExpId method will get all permissions in the experiment.
     *
     * @param expId
     * @return a List of Permission.
     */
    public List<Permission> getExpPermsByExpId(long expId);

    public Permission getExpPermForAnonymous(long expId);

    public void createExpUserPerm(Permission permission);

    public void updateExpUserPerm(Permission permission);

    public void deleteExpUserPerm(Permission permission);

    public void deleteExpPermByPermId(long permId);

    public void deleteExpAllPermsByExpId(long expId);

    public void createExpUserPerms(List<Permission> permissions);

    public void updateExpUserPerms(List<Permission> permissions);

    public void deleteExpUserPerms(List<Permission> permissions);

    public void deleteExpUserPermsByPermIds(List<Long> permIds);

    public void saveExpUserPerms(AssignedPermsBean assignedPerms);

    public void saveRequestedPerms(ManagablePerm<Permission> mgPerms, long requestPermId);

    public void savePermissionRequest(PermissionRequest permRequest);

    public PermissionRequest getPermissionReqById(long id);

    public List<PermissionRequest> getPermissionRequestsByOwner(long ownerId);

    public void deletePermissionRequestsByExpId(long expId);

    public void deletePermissionRequestById(long pmReqId);

    public PermissionRequest getExpPermissionRequestByReqUser(long expId, long reqUserId);

    public void updatePermissionRequest(PermissionRequest permRequest);

    public long getTotalPermRequests(long ownerId);

    public void approveExp(Experiment experiment);

    public List<Party> getPartiesByExpId(long expId);

    public Party getPartyByPartyKey(String partyKey);

    public Party getPartyByUsrNameAndEmail(String firstName, String lastName, String email);

    public void saveParty(Party party);

    public void updateParty(Party party);

    public List<Activity> getActivitiesByExpId(long expId);

    public Activity getActivityByActKey(String activityKey);

    public void saveActivity(Activity activity);

    public void saveLicence(Licence licence);

    public void updateLicence(Licence licence);

    public Licence getLicenceByExpId(long expId);

    public Licence getLicenceById(long lId);

    public void saveMetaRegInfo(MDRegistrationBean mdRegistrationBean);

    public void saveReporter(Reporter reporter);

    public void mergeReporter(Reporter reporter);

    public void updateReporter(Reporter reporter);

    public Pagination<Reporter> getReporters(int startPageNo, int recordsPerPage, OrderBy[] orderBys);

    public Reporter getReporterByProbeId(String probeId);

    public RepCounter importAllReporters(List<Reporter> reporters);

    public void importReporters(ReporterBean reporterBean);

    public void saveDataset(Dataset dataset);

    public Dataset getDataset(long dsId);

    public void deleteDataset(Dataset dataset);

    public void deleteDatasetById(long datasetId);

    public boolean checkExperimentDatasetExisted(long expId, String datasetName);

    public List<Dataset> getDatasetsByExpId(long expId);

    public int getTotalDatasetsNumber(long expId);

    public Pagination<Data> getDataByDatasetId(long dsId, int startPageNo, int recordsPerPage, String orderBy, String sortBy);

    public Dataset importExpDataset(Experiment experiment, BaseDataset txtDataset);

    public void saveFactorValue(ExperimentFactorValue efv);

    public ExperimentFactorValue getFactorValueByNameValuePair(String fname, String fvalue);

    public void saveFactor(ExperimentFactor expFactors);

    public List<ExperimentFactorValue> getFactorValuesByFactorId(long factorId);

    public ExperimentFactor getFactorByName(String factorName);

    public List<NameValueBean> getFactorValuesBeanByDatasetId(long dsId);

    public List<String> getFactorValuesByFactorName(String factorName);

    public void saveIFNType(IFNType ifnType);

    public IFNType getIFNTypeByTypes(String type, String subType);

    public List<IFNType> getIfnTypes(String typeName);

    public IFNVariation getIFNVariation(boolean isAbnormal, String value);

    public void saveIFNVariation(IFNVariation ifnVariation);

    public List<String> getAbnormalFactors();
}
