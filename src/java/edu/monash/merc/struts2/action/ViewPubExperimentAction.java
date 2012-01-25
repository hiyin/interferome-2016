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

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * ViewPubExperimentAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("pub.viewExpDetailsAction")
public class ViewPubExperimentAction extends DMBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String viewExp() {
        try {
            // check user is already logged in or not
            long userId = getCurrentUserId();
            if (userId != 0) {
                redNamespace = "/data";
                redActionName = "viewExperiment.jspx";
                return REDIRECTEXP;
            }
            // get experiment
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment == null) {
                logger.error(getText("experiment.view.experiment.not.found"));
                addActionError(getText("experiment.view.experiment.not.found"));
                setNavAndTitleForExc();
                return INPUT;
            }
            checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
            setNavAndTitleForPubExp();
            // check the view permissions
            if (!permissionBean.isViewAllowed()) {
                setSuccessActMsg(getText("experiment.view.experiment.details.permissions.denied"));
                return SUCCESS;
            }
            //count all datasets if any
            totalDatasetNum = countTotalDatasetsNumber(experiment.getId());

            //set the experiment reference link
            setExperimentReferenceLink(experiment);
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.view.experiment.details.failed"));
            setNavAndTitleForExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public void validateViewExp() {
        if (experiment == null || (experiment != null && experiment.getId() == 0)) {
            addFieldError("experiment.id", getText("experiment.view.experiment.id.must.be.provided"));
            setNavAndTitleForExc();
        }
    }

    private void setNavAndTitleForPubExp() {
        setPageTitle(getText("experiment.list.all.experiments.action.title"));
        //String secondNav = getText("experiment.list.all.experiments.action.title");
        //String secondNavLink = "pubdata/listExperiments.jspx";
        // String thirdNav = experiment.getName();
        //String thirdNavLink = "pubdata/viewExperiment.jspx?experiment.id=" + experiment.getId();

        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = "pubdata/viewExperiment.jspx?experiment.id=" + experiment.getId();
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, null, null);
    }

    private void setNavAndTitleForExc() {
        setPageTitle(getText("experiment.list.all.experiments.action.title"));
        // String secondNav = getText("experiment.list.all.experiments.action.title");
        // String secondNavLink = "pubdata/listExperiments.jspx";
        // String thirdNav = getText("experiment.view.experiment.details.action.title");
        String secondNav = getText("experiment.view.experiment.details.action.title");
        navBar = createNavBar("Experiment", null, secondNav, null, null, null);
    }
}
