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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.monash.merc.dto.AssignedPermsBean;
import edu.monash.merc.dto.MPermBeanType;
import edu.monash.merc.dto.ManagablePerm;
import edu.monash.merc.dto.PermissionBean;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import edu.monash.merc.domain.Experiment;
import edu.monash.merc.domain.PermType;
import edu.monash.merc.domain.Permission;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;

/**
 * PermissionAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("perm.permAction")
public class PermissionAction extends DMBaseAction {

    private Map<Long, String> allActiveUsers = new HashMap<Long, String>();

    private List<PermissionBean> permissionBeans = new ArrayList<PermissionBean>();

    private PermissionBean permForAllRegUser;

    private PermissionBean permForAnonyUser;

    private String viewExpActName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showChangeExpPerms() {
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isChangePermAllowed()) {
                    addActionError(getText("experiment.change.perms.permission.denied"));
                    setNavAndTitleForPermExc();
                    return ERROR;
                }

                // populate all active users
                populateFilteredUserNames(experiment.getOwner().getId());
                // get all permission from the database
                List<Permission> permissions = this.dmService.getExpPermsByExpId(experiment.getId());
                copyPermsToPermissionBean(permissions);
                // set the view experiment details action name
                if (fromMyExp) {
                    viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
                } else {
                    viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
                }
                setNavAndTitleForPerm();
            } else {
                addActionError(getText("experiment.change.perms.failed.experiment.not.found"));
                setNavAndTitleForPermExc();
                return ERROR;
            }

        } catch (Exception e) {
            logger.error(e);
            addActionError("experiment.change.perms.show.permissions.failed");
            setNavAndTitleForPermExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public String setExpPerms() {
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());

            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isChangePermAllowed()) {
                    addActionError(getText("experiment.change.perms.permission.denied"));
                    setNavAndTitleForPermExc();
                    return ERROR;
                }
                // populate all active users
                populateFilteredUserNames(experiment.getOwner().getId());
                AssignedPermsBean assignedPerms = setAssignedPermissions(experiment);
                // save the permission
                this.dmService.saveExpUserPerms(assignedPerms);
                List<Permission> updatedPerms = this.dmService.getExpPermsByExpId(experiment.getId());
                // copy the permissions
                copyPermsToPermissionBean(updatedPerms);
                // set the view experiment details action name
                if (fromMyExp) {
                    viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
                } else {
                    viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
                }
                // set action successful message
                setSuccessActMsg(getText("experiment.change.perms.success.message"));
                setNavAndTitleForPerm();
            } else {
                addActionError(getText("experiment.change.perms.failed.experiment.not.found"));
                setNavAndTitleForPermExc();
                return ERROR;
            }

        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.change.perms.failed"));
            setNavAndTitleForPermExc();
            return ERROR;
        }

        return SUCCESS;
    }

    // for experiemnt permission title and navigation bar
    private void setNavAndTitleForPerm() {
        setPageTitle(getText("experiment.change.perms.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.change.perms.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // for experiemnt permission title and navigation bar after exception
    private void setNavAndTitleForPermExc() {
        setPageTitle(getText("experiment.change.perms.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.change.perms.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // populate all active user names
    private void populateFilteredUserNames(long ownerId) {
        allActiveUsers.clear();
        List<User> users = this.userService.getAllActiveUsers();
        for (User u : users) {
            if ((u.getId() != ownerId) && (u.getId() != getCurrentUserId()) && (u.getUserType() != UserType.ADMIN.code())
                    && (u.getUserType() != UserType.SUPERADMIN.code())) {
                allActiveUsers.put(u.getId(), u.getDisplayName());
            }
        }
    }

    private void copyPermsToPermissionBean(List<Permission> permissions) {
        permForAllRegUser = new PermissionBean();
        permForAnonyUser = new PermissionBean();
        permissionBeans.clear();
        for (Permission perm : permissions) {
            // get default permissions for all-registered user
            if (perm.getPermType().equals(PermType.ALLREGUSER.code())) {
                permForAllRegUser.setId(perm.getId());
                permForAllRegUser.setName(perm.getPermForUser().getDisplayName());
                permForAllRegUser.setUserId(perm.getPermForUser().getId());
                permForAllRegUser.setViewAllowed(perm.isViewAllowed());
                permForAllRegUser.setUpdateAllowed(perm.isUpdateAllowed());
                permForAllRegUser.setImportAllowed(perm.isImportAllowed());
                permForAllRegUser.setExportAllowed(perm.isExportAllowed());
                permForAllRegUser.setDeleteAllowed(perm.isDeleteAllowed());
                permForAllRegUser.setChangePermAllowed(perm.isChangePermAllowed());
                // get default permission for anonymous user
            } else if (perm.getPermType().equals(PermType.ANONYMOUS.code())) {
                permForAnonyUser.setId(perm.getId());
                permForAnonyUser.setName(perm.getPermForUser().getDisplayName());
                permForAnonyUser.setUserId(perm.getPermForUser().getId());
                permForAnonyUser.setViewAllowed(perm.isViewAllowed());
                permForAnonyUser.setUpdateAllowed(perm.isUpdateAllowed());
                permForAnonyUser.setImportAllowed(perm.isImportAllowed());
                permForAnonyUser.setExportAllowed(perm.isExportAllowed());
                permForAnonyUser.setDeleteAllowed(perm.isDeleteAllowed());
                permForAnonyUser.setChangePermAllowed(perm.isChangePermAllowed());
            } else {// get all permission for individual users
                PermissionBean mp = new PermissionBean();
                mp.setId(perm.getId());
                mp.setUserId(perm.getPermForUser().getId());
                mp.setName(perm.getPermForUser().getDisplayName());
                mp.setViewAllowed(perm.isViewAllowed());
                mp.setUpdateAllowed(perm.isUpdateAllowed());
                mp.setImportAllowed(perm.isImportAllowed());
                mp.setExportAllowed(perm.isExportAllowed());
                mp.setDeleteAllowed(perm.isDeleteAllowed());
                mp.setChangePermAllowed(perm.isChangePermAllowed());
                permissionBeans.add(mp);
            }
        }
    }

    private AssignedPermsBean setAssignedPermissions(Experiment exp) {

        List<Permission> newPerms = new ArrayList<Permission>();
        List<Permission> updatedPerms = new ArrayList<Permission>();
        List<Long> deletedPermIds = new ArrayList<Long>();
        AssignedPermsBean assignPms = new AssignedPermsBean();

        // the permissions for all-registered-user will inherited the permissions from the anonymous
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

        // for individual user
        for (PermissionBean pm : permissionBeans) {

            // inherited the permissions from anonymous user
            if (permForAnonyUser.isViewAllowed()) {
                pm.setViewAllowed(true);
            }
            if (permForAnonyUser.isUpdateAllowed()) {
                pm.setUpdateAllowed(true);
            }
            if (permForAnonyUser.isImportAllowed()) {
                pm.setImportAllowed(true);
            }
            if (permForAnonyUser.isExportAllowed()) {
                pm.setExportAllowed(true);
            }
            if (permForAnonyUser.isDeleteAllowed()) {
                pm.setDeleteAllowed(true);
            }
            if (permForAnonyUser.isChangePermAllowed()) {
                pm.setChangePermAllowed(true);
            }
            ManagablePerm<PermissionBean> managablePerm = new ManagablePerm<PermissionBean>();
            managablePerm.setPerm(pm);

            ManagablePerm mPerm = sortAssignedPermBean(managablePerm, permForAllRegUser, permForAnonyUser);

            if (mPerm != null) {
                Permission perm = new Permission();
                PermissionBean pmb = (PermissionBean) mPerm.getPerm();
                perm.setId(pmb.getId());
                long uid = pmb.getUserId();
                User u = this.userService.getUserById(uid);
                perm.setPermForUser(u);
                perm.setViewAllowed(pmb.isViewAllowed());
                perm.setUpdateAllowed(pmb.isUpdateAllowed());
                perm.setImportAllowed(pmb.isImportAllowed());
                perm.setExportAllowed(pmb.isExportAllowed());
                perm.setDeleteAllowed(pmb.isDeleteAllowed());
                perm.setChangePermAllowed(pmb.isChangePermAllowed());
                perm.setPermType(PermType.REGISTERED.code());
                perm.setExperiment(experiment);

                if (mPerm.getManagablePermType().equals(MPermBeanType.DELETEREQ)) {
                    deletedPermIds.add(perm.getId());
                }
                if (mPerm.getManagablePermType().equals(MPermBeanType.NEWREQ)) {
                    perm.setId(0);
                    newPerms.add(perm);
                }
                if (mPerm.getManagablePermType().equals(MPermBeanType.UPDATEREQ)) {
                    updatedPerms.add(perm);
                }
            }

        }

        // update all registered user permissions
        Permission updatedPmForAllRegUser = new Permission();
        // set all-registered-user permission id
        updatedPmForAllRegUser.setId(permForAllRegUser.getId());
        // get all-registered-user id
        long allRegUserId = permForAllRegUser.getUserId();
        // get all-registered-user
        User allRegUser = this.userService.getUserById(allRegUserId);
        // set all-registered-user for this permission
        updatedPmForAllRegUser.setPermForUser(allRegUser);
        updatedPmForAllRegUser.setViewAllowed(permForAllRegUser.isViewAllowed());
        updatedPmForAllRegUser.setUpdateAllowed(permForAllRegUser.isUpdateAllowed());
        updatedPmForAllRegUser.setImportAllowed(permForAllRegUser.isImportAllowed());
        updatedPmForAllRegUser.setExportAllowed(permForAllRegUser.isExportAllowed());
        updatedPmForAllRegUser.setDeleteAllowed(permForAllRegUser.isDeleteAllowed());
        updatedPmForAllRegUser.setChangePermAllowed(permForAllRegUser.isChangePermAllowed());
        updatedPmForAllRegUser.setPermType(PermType.ALLREGUSER.code());
        updatedPmForAllRegUser.setExperiment(experiment);
        updatedPerms.add(updatedPmForAllRegUser);

        // update the anonymous permissions
        Permission updatedPmForAnonyUser = new Permission();
        // set the anonymous permission id
        updatedPmForAnonyUser.setId(permForAnonyUser.getId());
        // get anonymous user id
        long anonyUserId = permForAnonyUser.getUserId();
        // get anonymous user
        User anonymous = this.userService.getUserById(anonyUserId);
        // set anonmous user for this permission
        updatedPmForAnonyUser.setPermForUser(anonymous);
        updatedPmForAnonyUser.setViewAllowed(permForAnonyUser.isViewAllowed());
        updatedPmForAnonyUser.setUpdateAllowed(permForAnonyUser.isUpdateAllowed());
        updatedPmForAnonyUser.setImportAllowed(permForAnonyUser.isImportAllowed());
        updatedPmForAnonyUser.setExportAllowed(permForAnonyUser.isExportAllowed());
        updatedPmForAnonyUser.setDeleteAllowed(permForAnonyUser.isDeleteAllowed());
        updatedPmForAnonyUser.setChangePermAllowed(permForAnonyUser.isChangePermAllowed());
        updatedPmForAnonyUser.setPermType(PermType.ANONYMOUS.code());
        updatedPmForAnonyUser.setExperiment(experiment);
        updatedPerms.add(updatedPmForAnonyUser);

        assignPms.setNewPerms(newPerms);
        assignPms.setUpdatedPerms(updatedPerms);
        assignPms.setDeletedPermIds(deletedPermIds);

        return assignPms;
    }

    private ManagablePerm<PermissionBean> sortAssignedPermBean(ManagablePerm<PermissionBean> managablePerm, PermissionBean allUserPerm, PermissionBean anonyPerm) {
        //get the individual permission bean.
        PermissionBean perm = managablePerm.getPerm();
        // If none permission is allowed for the all-registered-user, the anonymous user and this individual user:
        // a). If this individual user permission is new, we just ignore it. as it's the same as the permissions
        // for the all-registered-user and the anonymous user.
        // b). If this individual user permission already existed, we have to remove it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && perm.isNonePerm()) {
            if (perm.getId() == 0) {
                managablePerm.setManagablePermType(MPermBeanType.IGNOREREQ);
            } else {
                managablePerm.setManagablePermType(MPermBeanType.DELETEREQ);
            }
            return managablePerm;
        }
        // if none permission is assigned for the anonymous user and the all-registered-user, but assigned for the
        // individual user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && allUserPerm.isNonePerm() && !perm.isNonePerm()) {
            if (perm.getId() == 0) {
                managablePerm.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                managablePerm.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return managablePerm;
        }

        // if none permission is assigned for the anonymous user and the individual user. but assigned to the
        // all-registered-user, which mean the owner would not give any permissions to this registered user:
        // a). If this individual user permission is new, just create it.
        // b). If this individual user permission already existed, we just update it.
        if (anonyPerm.isNonePerm() && !allUserPerm.isNonePerm() && perm.isNonePerm()) {
            if (perm.getId() == 0) {
                managablePerm.setManagablePermType(MPermBeanType.NEWREQ);
            } else {
                managablePerm.setManagablePermType(MPermBeanType.UPDATEREQ);
            }
            return managablePerm;
        }

        // if the permission is assigned for the all-registered-user and the individual user:
        // 1. If this individual user permission is new:
        // a): if the individual permissions are the same as the all-registered-user permissions, just ignore
        // b): otherwise we create a new permission for the individual user.
        //
        // 2). If this individual user permission already existed:
        // a): if the individual permissions are the same as the all-registered-user permissions, just remove it.
        // b): otherwise we update permission for the individual user.
        if (!allUserPerm.isNonePerm() && !perm.isNonePerm()) {
            if (perm.getId() == 0) {
                if ((isSamePerms(allUserPerm, perm))) {
                    managablePerm.setManagablePermType(MPermBeanType.IGNOREREQ);
                } else {
                    // create a new permission
                    managablePerm.setManagablePermType(MPermBeanType.NEWREQ);
                }
            } else {
                if ((isSamePerms(allUserPerm, perm))) {
                    managablePerm.setManagablePermType(MPermBeanType.DELETEREQ);
                } else {
                    managablePerm.setManagablePermType(MPermBeanType.UPDATEREQ);
                }
            }
            return managablePerm;
        }
        return null;
    }

    private boolean isSamePerms(PermissionBean aPerm, PermissionBean bPerm) {
        if ((aPerm.isViewAllowed() != bPerm.isViewAllowed()) || (aPerm.isUpdateAllowed() != bPerm.isUpdateAllowed())
                || (aPerm.isImportAllowed() != bPerm.isImportAllowed()) || (aPerm.isExportAllowed() != bPerm.isExportAllowed())
                || (aPerm.isDeleteAllowed() != bPerm.isDeleteAllowed()) || (aPerm.isChangePermAllowed() != bPerm.isChangePermAllowed())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return the allActiveUsers
     */
    public Map<Long, String> getAllActiveUsers() {
        return allActiveUsers;
    }

    /**
     * @param allActiveUsers the allActiveUsers to set
     */
    public void setAllActiveUsers(Map<Long, String> allActiveUsers) {
        this.allActiveUsers = allActiveUsers;
    }

    /**
     * @return the permissionBeans
     */
    public List<PermissionBean> getPermissionBeans() {
        return permissionBeans;
    }

    /**
     * @param permissionBeans the permissionBeans to set
     */
    public void setPermissionBeans(List<PermissionBean> permissionBeans) {
        this.permissionBeans = permissionBeans;
    }

    /**
     * @return the permForAllRegUser
     */
    public PermissionBean getPermForAllRegUser() {
        return permForAllRegUser;
    }

    /**
     * @param permForAllRegUser the permForAllRegUser to set
     */
    public void setPermForAllRegUser(PermissionBean permForAllRegUser) {
        this.permForAllRegUser = permForAllRegUser;
    }

    /**
     * @return the permForAnonyUser
     */
    public PermissionBean getPermForAnonyUser() {
        return permForAnonyUser;
    }

    /**
     * @param permForAnonyUser the permForAnonyUser to set
     */
    public void setPermForAnonyUser(PermissionBean permForAnonyUser) {
        this.permForAnonyUser = permForAnonyUser;
    }

    /**
     * @return the viewExpActName
     */
    public String getViewExpActName() {
        return viewExpActName;
    }

    /**
     * @param viewExpActName the viewExpActName to set
     */
    public void setViewExpActName(String viewExpActName) {
        this.viewExpActName = viewExpActName;
    }

}
