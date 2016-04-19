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
import edu.monash.merc.dto.MPermBeanType;
import edu.monash.merc.dto.ManagablePerm;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * PermissionApproveAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("perm.permReqAppAction")
public class PermissionApproveAction extends DMBaseAction {

    private PermissionRequest permRequest;

    private List<PermissionRequest> permRequests;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void initAct() {
        setNavForAppPerms();
    }

    public String listPermRequests() {
        try {
            user = getCurrentUser();
            permRequests = this.dmService.getPermissionRequestsByOwner(user.getId());
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("approve.perm.failed.to.get.permission.requests"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String approvePermReq() {
        try {
            user = getCurrentUser();
            PermissionRequest pmReq = this.dmService.getPermissionReqById(permRequest.getId());
            Experiment exp = pmReq.getExperiment();
            User requestUser = pmReq.getRequestUser();
            //grant the permissions for this request user
            ManagablePerm<Permission> grantPerms = grantPermsForUser(pmReq, exp, requestUser);
            if (grantPerms != null) {
                //save the grant permissions
                this.dmService.saveRequestedPerms(grantPerms, pmReq.getId());
            }
            //get all request perms again
            permRequests = this.dmService.getPermissionRequestsByOwner(user.getId());
            //set the succeed message
            setSuccessActMsg(getText("approve.perm.grant.user.requested.perms.success.msg", new String[]{this.namePrefix + exp.getId()}));
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("approve.perm.failed.to.grant.user.requested.permissions"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String rejectPermReq() {
        try {
            user = getCurrentUser();
            //delete the permission request
            this.dmService.deletePermissionRequestById(permRequest.getId());
            //get all request perms again
            permRequests = this.dmService.getPermissionRequestsByOwner(user.getId());
            //set the success message
            setSuccessActMsg(getText("approve.perm.reject.user.requested.perms.success.msg", new String[]{this.namePrefix + permRequest.getExperiment().getId()}));
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("approve.perm.failed.to.reject.user.requested.permissions"));
            return ERROR;
        }
        return SUCCESS;
    }

    private ManagablePerm<Permission> grantPermsForUser(PermissionRequest pmRequest, Experiment experiment, User requestUser) {
        long pmReqId = pmRequest.getId();
        Permission requestPerm = new Permission();
        requestPerm.setViewAllowed(pmRequest.isViewAllowed());
        requestPerm.setUpdateAllowed(pmRequest.isUpdateAllowed());
        requestPerm.setImportAllowed(pmRequest.isImportAllowed());
        requestPerm.setExportAllowed(pmRequest.isExportAllowed());
        requestPerm.setDeleteAllowed(pmRequest.isDeleteAllowed());
        requestPerm.setChangePermAllowed(pmRequest.isChangePermAllowed());
        // get the user permission for this exp, return max three permissions or min two permissions
        List<Permission> userAllPerms = this.dmService.getExpPermsForUser(experiment.getId(), requestUser.getId());
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
        } else {
            //impossible case, no default permissions set for this user, just return null
            return null;
        }
        //if previous permissions are already existed, just get the id for this requested permissions
        if (userPerm != null) {
            requestPerm.setId(userPerm.getId());
        }

        // inherited the permissions from anonymous user
        if (anonyPerm.isViewAllowed()) {
            requestPerm.setViewAllowed(true);
        }
        if (anonyPerm.isUpdateAllowed()) {
            requestPerm.setUpdateAllowed(true);
        }
        if (anonyPerm.isImportAllowed()) {
            requestPerm.setImportAllowed(true);
        }
        if (anonyPerm.isExportAllowed()) {
            requestPerm.setExportAllowed(true);
        }
        if (anonyPerm.isDeleteAllowed()) {
            requestPerm.setDeleteAllowed(true);
        }
        if (anonyPerm.isChangePermAllowed()) {
            requestPerm.setChangePermAllowed(true);
        }

        //set the permissions for this experiment
        requestPerm.setExperiment(experiment);
        //set the permission for this request user
        requestPerm.setPermForUser(requestUser);
        //set the permission type is a registered user
        requestPerm.setPermType(PermType.REGISTERED.code());

        ManagablePerm<Permission> managablePerm = new ManagablePerm<Permission>();
        managablePerm.setPerm(requestPerm);
        return createUserPerms(managablePerm, allRegPerm, anonyPerm);
    }

    private ManagablePerm<Permission> createUserPerms(ManagablePerm<Permission> requestedIndividualPerms, Permission allUserPerm, Permission anonyPerm) {
        Permission perm = requestedIndividualPerms.getPerm();
        // If none permission is allowed for the all-registered-user, the anonymous user and this individual user:
        // a). If this individual user permission is new, we just ignore it. as it's the same as the permissions
        // for the all-registered-user and the anonymous user.
        // b). If this individual user permission already existed, we have to remove it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && perm.isNonePerm()) {
            if (perm.getId() == 0) {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.IGNOREREQ);
            } else {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.DELETEREQ);
            }
            return requestedIndividualPerms;
        }
        // if none permission is assigned for the anonymous user and the all-registered-user, but assigned for the
        // individual user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && !perm.isNonePerm()) {
            if (perm.getId() == 0) {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return requestedIndividualPerms;
        }

        // if none permission is assigned for the anonymous user and the individual user. but assigned to the
        // all-registered-user, which mean the owner would not give any permissions to this registered user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && !allUserPerm.isNonePerm() && perm.isNonePerm()) {
            if (perm.getId() == 0) {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                requestedIndividualPerms.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return requestedIndividualPerms;
        }

        // if the permission is assigned for the all-registered-user and the individual user:
        // 1. If this individual user permission is new:
        // a): if the individual permissions are the same as the all-registered-user permissions, just ignore
        // b): otherwise we create a new permission for the individual user.
        //
        // 2). If this individual user permission already existed:
        // a): if the individual permissions are the same as the all-registered-user permissions,just remove it.
        // b): otherwise we update permission for the individual user.
        if (!allUserPerm.isNonePerm() && !perm.isNonePerm()) {
            if (perm.getId() == 0) {
                if ((isSamePerms(allUserPerm, perm))) {
                    requestedIndividualPerms.setManagablePermType(MPermBeanType.IGNOREREQ);
                } else {
                    // create a new permission
                    requestedIndividualPerms.setManagablePermType(MPermBeanType.NEWREQ);
                }
            } else {
                if ((isSamePerms(allUserPerm, perm))) {
                    requestedIndividualPerms.setManagablePermType(MPermBeanType.DELETEREQ);
                } else {
                    requestedIndividualPerms.setManagablePermType(MPermBeanType.UPDATEREQ);
                }
            }
            return requestedIndividualPerms;
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

    private void setNavForAppPerms() {
        setPageTitle(getText("list.all.permission.requests.action.title"));
        String secondNav = getText("user.home.action.title");
        String secondNavLink = "manage/userHome.jspx";
        String thirdNav = getText("list.all.permission.requests.action.title");
        String thirdNavLink = "perm/listPermRequests.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    public PermissionRequest getPermRequest() {
        return permRequest;
    }

    public void setPermRequest(PermissionRequest permRequest) {
        this.permRequest = permRequest;
    }

    public List<PermissionRequest> getPermRequests() {
        return permRequests;
    }

    public void setPermRequests(List<PermissionRequest> permRequests) {
        this.permRequests = permRequests;
    }
}
