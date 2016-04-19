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

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.Dataset;
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.domain.User;
import edu.monash.merc.dto.DatasetFactorBean;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * DeleteDatasetAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.deleteDatasetAction")
public class DeleteDatasetAction extends DMBaseAction {

    private List<DatasetFactorBean> datasetFactorBeans;

    private String viewExpActName;

    private Dataset dataset;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String deleteDataset() {

        //check if the experiment id is provided or not
        if (experiment == null || (experiment != null && experiment.getId() == 0)) {
            addActionError(getText("dataset.delete.experiment.id.must.be.provided"));
            setNavAndTitleForDsExc();
            return ERROR;
        }

        //get the experiment first
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment == null) {
                addFieldError("experiment.id", getText("dataset.delete.failed.experiment.not.found"));
                setNavAndTitleForDsExc();
                return ERROR;
            }
        } catch (Exception ex) {
            addFieldError("permission", getText("dataset.delete.check.experiment.failed"));
            setNavAndTitleForDsExc();
            return ERROR;
        }
        //check the permissions
        try {
            // check the permissions for this experiment
            checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
            if (!permissionBean.isDeleteAllowed()) {
                addFieldError("permission", getText("dataset.delete.permission.denied"));
                //set page title
                setNavAndTitleForDs();
                try {
                    retrieveAllDatasets();
                } catch (Exception dse) {
                    logger.error(dse);
                    addActionError(getText("dataset.delete.get.all.dataset.failed"));
                    setNavAndTitleForDsExc();
                    return ERROR;
                }
                return INPUT;
            }
        } catch (Exception ex) {
            addActionError(getText("dataset.delete.check.permission.failed"));
            //set page title
            setNavAndTitleForDs();
            try {
                retrieveAllDatasets();
            } catch (Exception dse) {
                logger.error(dse);
                addActionError(getText("dataset.delete.get.all.dataset.failed"));
                setNavAndTitleForDsExc();
                return ERROR;
            }
            return INPUT;
        }

        //retrieve the dataset
        try {
            dataset = this.dmService.getDataset(dataset.getId());
            if (dataset == null) {
                addActionError(getText("dataset.delete.dataset.not.found"));
                try {
                    retrieveAllDatasets();
                    //set page title
                    setNavAndTitleForDs();
                    //setup view exp action
                    setViewExpAct();
                } catch (Exception dse) {
                    logger.error(dse);
                    addActionError(getText("dataset.delete.get.all.dataset.failed"));
                    setNavAndTitleForDsExc();
                    return ERROR;
                }

                return INPUT;
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("dataset.delete.check.dataset.failed"));
            //set page title
            setNavAndTitleForDs();
            //setup view exp action
            setViewExpAct();
            try {
                retrieveAllDatasets();
            } catch (Exception dse) {
                logger.error(dse);
                addActionError(getText("dataset.delete.get.all.dataset.failed"));
                setNavAndTitleForDsExc();
                return ERROR;
            }
            return INPUT;
        }


        //delete the dataset
        try {
            this.dmService.deleteDataset(dataset);

            //retrieve all datasets
            retrieveAllDatasets();

            //setup view exp action
            setViewExpAct();

            //set page title
            setNavAndTitleForDs();
            setSuccessActMsg(getText("dataset.delete.success.message", new String[]{dataset.getName()}));
            // set the audit event
            recordAuditEventForDelete(experiment, dataset, user);
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("dataset.delete.failed"));
            try {
                retrieveAllDatasets();
            } catch (Exception dse) {
                logger.error(dse);
                addActionError(getText("dataset.delete.get.all.dataset.failed"));
                setNavAndTitleForDsExc();
                return ERROR;
            }
            //set page title
            setNavAndTitleForDs();
            //setup view exp action
            setViewExpAct();
            return INPUT;
        }
        return SUCCESS;
    }

    private void retrieveAllDatasets() {
        //list all datasets if any
        datasetFactorBeans = getDatasetByExperiment(experiment.getId());
    }

    private void setViewExpAct() {
        // set the view experiment details action name
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }
    }

    private void recordAuditEventForDelete(Experiment exp, Dataset ds, User operator) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        ev.setEvent(getText("dataset.delete.success.message", new String[]{ds.getName()}));
        ev.setEventOwner(exp.getOwner());
        ev.setOperator(operator);
        recordActionAuditEvent(ev);
    }

    private void setNavAndTitleForDs() {
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

    public void setNavAndTitleForDsExc() {
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

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
}
