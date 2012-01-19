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

import edu.monash.merc.domain.PermissionRequest;
import edu.monash.merc.domain.User;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * PermissionRequestAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("perm.permReqAction")
public class PermissionRequestAction extends DMBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private PermissionRequest permReq;

    private List<PermissionRequest> permRequests;

    public String showApplyForPerms() {
        try {
            user = getCurrentUser();
            experiment = this.dmService.getExperimentById(experiment.getId());
            User owner = experiment.getOwner();
            // check if the logged in user is an owner of this experiment or not
            // owner of the experiment doesn't need to apply for the access permissions.
            if (user.getId() == owner.getId()) {
                addActionError(getText("apply.for.perm.owner.does.not.need.apply.perms"));
                setNavAfterExc();
                return INPUT;
            }
            permReq = this.dmService.getExpPermissionRequestByReqUser(experiment.getId(), user.getId());
            if (permReq == null) {
                permReq = new PermissionRequest();
            }
            setNavAfterSuccess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("apply.for.perm.failed.to.show.perms.applying.page"));
            setNavAfterExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public String applyForPerms() {
        try {
            if (checkPermsReqError()) {
                setNavAfterSuccess();
                return INPUT;
            }
            experiment = this.dmService.getExperimentById(experiment.getId());
            User owner = experiment.getOwner();
            user = getCurrentUser();
            // check if the logged in user is an owner of this experiment or not
            // owner of the experiment doesn't need to apply for the access permissions.
            if (user.getId() == owner.getId()) {
                addActionError(getText("apply.for.perm.owner.does.not.need.apply.perms"));
                setNavAfterExc();
                return INPUT;
            }
            permReq.setOwner(owner);
            permReq.setRequestUser(user);
            permReq.setExperiment(experiment);
            permReq.setRequestTime(GregorianCalendar.getInstance().getTime());

            //check the permission request exist or not
            PermissionRequest prequest = this.dmService.getExpPermissionRequestByReqUser(experiment.getId(), user.getId());
            if (prequest != null) {
                //we just do an update
                if (permReq.getId() == 0) {
                    permReq.setId(prequest.getId());
                }
                this.dmService.updatePermissionRequest(permReq);
            } else {
                //we just do create
                if (permReq.getId() > 0) {
                    permReq.setId(0);
                }
                this.dmService.savePermissionRequest(permReq);
            }

            setSuccessActMsg(getText("apply.for.perm.success.msg", new String[]{this.namePrefix + experiment.getId()}));
            setNavAfterSuccess();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("apply.for.perm.failed"));
            setNavAfterExc();
            return ERROR;
        }

        return SUCCESS;
    }

    private boolean checkPermsReqError() {
        if (!permReq.isViewAllowed() && !permReq.isUpdateAllowed() && !permReq.isImportAllowed() && !permReq.isExportAllowed()
                && !permReq.isDeleteAllowed() && !permReq.isChangePermAllowed()) {
            addFieldError("perms", getText("apply.for.perm.at.least.one.permission.required"));
            return true;
        }
        return false;
    }

    private void setNavAfterExc() {
        setPageTitle(getText("apply.for.permission.action.title"));
        String secondNav = getText("experiment.list.all.experiments.action.title");
        String secondNavLink = "data/listExperiments.jspx";
        String thirdNav = getText("apply.for.permission.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    private void setNavAfterSuccess() {
        setPageTitle(getText("apply.for.permission.action.title"));
        String secondNav = getText("experiment.list.all.experiments.action.title");
        String secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        String thirdNav = getText("apply.for.permission.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    public PermissionRequest getPermReq() {
        return permReq;
    }

    public void setPermReq(PermissionRequest permReq) {
        this.permReq = permReq;
    }

    public List<PermissionRequest> getPermRequests() {
        return permRequests;
    }

    public void setPermRequests(List<PermissionRequest> permRequests) {
        this.permRequests = permRequests;
    }
}
