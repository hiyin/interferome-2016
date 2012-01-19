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

import edu.monash.merc.domain.UserType;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * ApproveExpAction Action class provides the experiment approval function
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.approveExpAction")
public class ApproveExpAction extends DMBaseAction {

    private boolean mdRegSelected;

    private String viewExpActName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showApproveExp() {

        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                user = getCurrentUser();
                if (user != null && (user.getUserType() != UserType.ADMIN.code() && (user.getUserType() != UserType.SUPERADMIN.code()))) {
                    addActionError(getText("experiment.approve.exp.show.approval.permission.denied"));
                    setNavAndTitleForAppExpExc();
                    return ERROR;
                }
            } else {
                addActionError(getText("experiment.approve.exp.failed.experiment.not.found"));
                setNavAndTitleForAppExpExc();
                return ERROR;
            }

            // set the view experiment details action name
            if (fromMyExp) {
                viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
            } else {
                viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
            }
            //set the metadata registration as default
            mdRegSelected = true;
            setNavAndTitleForAppExp();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.approve.exp.show.approval.failed"));
            setNavAndTitleForAppExpExc();
            return ERROR;
        }
        return SUCCESS;
    }

    // for experiment  title and navigation bar
    private void setNavAndTitleForAppExp() {
        setPageTitle(getText("experiment.approve.exp.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.approve.exp.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // for experiment title and navigation bar after exception
    private void setNavAndTitleForAppExpExc() {
        setPageTitle(getText("experiment.approve.exp.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.approve.exp.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    public String approveExp() {
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                user = getCurrentUser();
                if (user != null && (user.getUserType() != UserType.ADMIN.code() && (user.getUserType() != UserType.SUPERADMIN.code()))) {
                    addActionError(getText("experiment.approve.exp.approval.permission.denied"));
                    setNavAndTitleForAppExpExc();
                    return ERROR;
                }
            } else {
                addActionError(getText("experiment.approve.exp.failed.experiment.not.found"));
                setNavAndTitleForAppExpExc();
                return ERROR;
            }
            //set the status as approved
            experiment.setApproved(true);

            //start to approve the experiment including update the permissions.
            this.dmService.approveExp(experiment);

            // check the permissions for this experiment
            checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());

            // set the view experiment details action name
            if (fromMyExp) {
                viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
            } else {
                viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
            }

            // set action successful message
            setSuccessActMsg(getText("experiment.approve.exp.success.message", new String[]{this.namePrefix + experiment.getId()}));
            //set the navigation and title
            setNavAndTitleForAppExp();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.approve.exp.approval.failed"));
            setNavAndTitleForAppExpExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public void validateApproveExp() {
        boolean error = false;
        if (experiment == null) {
            addFieldError("expId", getText("experiment.approve.exp.experiment.must.be.provide"));
            error = true;
        } else {
            long id = experiment.getId();
            if (id == 0) {
                addFieldError("expId", getText("experiment.approve.exp.experiment.must.be.provide"));
                error = true;
            }
        }
        if (error) {
            setNavAndTitleForAppExpExc();
        }
    }


    public String getViewExpActName() {
        return viewExpActName;
    }

    public void setViewExpActName(String viewExpActName) {
        this.viewExpActName = viewExpActName;
    }

    public boolean isMdRegSelected() {
        return mdRegSelected;
    }

    public void setMdRegSelected(boolean mdRegSelected) {
        this.mdRegSelected = mdRegSelected;
    }


}
