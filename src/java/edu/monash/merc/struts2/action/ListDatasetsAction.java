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

import edu.monash.merc.dto.DatasetFactorBean;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * ListDatasetsAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.listDatasetsAction")
public class ListDatasetsAction extends DMBaseAction {

    private List<DatasetFactorBean> datasetFactorBeans;

    private String viewExpActName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String listDatasets() {

        try {
            // set the view experiment details action name
            setViewExpAction();

            //get the experiment details
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isViewAllowed()) {
                    addActionError(getText("experiment.view.datasets.permission.denied"));
                    setNavAndTitleForViewDsExc();
                    return INPUT;
                }
            } else {
                addActionError(getText("experiment.view.datasets.failed.experiment.not.found"));
                setNavAndTitleForViewDsExc();
                return ERROR;
            }

            //list all datasets if any
            datasetFactorBeans = getDatasetByExperiment(experiment.getId());
            //set action title and nav bar
            setNavAndTitleForViewDs();

        } catch (Exception e) {
            logger.error(e);
            addActionError("experiment.view.datasets.failed");
            setNavAndTitleForViewDsExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public void validateListDatasets() {
        if (experiment == null || (experiment != null && experiment.getId() == 0)) {
            addFieldError("experiment.id", getText("experiment.view.datasets.experiment.id.must.be.provided"));
            setNavAndTitleForViewDsExc();
        }
    }

    private void setViewExpAction() {
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }
    }

    private void setNavAndTitleForViewDs() {
        setPageTitle(getText("experiment.view.all.datasets.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.view.all.datasets.action.title");
        String thirdNavLink = "data/listDatasets.jspx?experiment.id=" + experiment.getId() + "&fromMyExp=" + fromMyExp;
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    public void setNavAndTitleForViewDsExc() {
        setPageTitle(getText("experiment.view.all.datasets.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.view.all.datasets.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    public List<DatasetFactorBean> getDatasetFactorBeans() {
        return datasetFactorBeans;
    }

    public void setDatasetFactorBeans(List<DatasetFactorBean> datasetFactorBeans) {
        this.datasetFactorBeans = datasetFactorBeans;
    }

    public String getViewExpActName() {
        return viewExpActName;
    }

    public void setViewExpActName(String viewExpActName) {
        this.viewExpActName = viewExpActName;
    }
}
