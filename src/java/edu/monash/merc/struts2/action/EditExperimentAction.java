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
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.domain.User;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.GregorianCalendar;

/**
 * EditExperimentAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.editExpAction")
public class EditExperimentAction extends DMBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String showEditExp() {
        try {
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isChangePermAllowed()) {
                    addActionError(getText("experiment.edit.exp.permission.denied"));
                    setNavAndTitleForExpExc();
                    return ERROR;
                }
            } else {
                addActionError(getText("experiment.edit.exp.failed.experiment.not.found"));
                setNavAndTitleForExpExc();
                return ERROR;
            }

            //set the experiment reference link
            setExperimentReferenceLink(experiment);
            setNavAndTitleForExp();
        } catch (Exception e) {
            logger.error(e);
            addActionError("experiment.edit.exp.show.experiment.failed");
            setNavAndTitleForExpExc();
            return ERROR;
        }
        return SUCCESS;
    }

    public String editExp() {
        try {
            Experiment exp = this.dmService.getExperimentById(experiment.getId());
            if (exp != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(exp.getId(), exp.getOwner().getId());
                if (!permissionBean.isChangePermAllowed()) {
                    addActionError(getText("experiment.edit.exp.permission.denied"));
                    setNavAndTitleForExpExc();
                    return ERROR;
                }

                exp.setDescription(experiment.getDescription());
                exp.setModifiedTime(GregorianCalendar.getInstance().getTime());
                exp.setModifiedByUser(user);
                exp.setPubMedId(experiment.getPubMedId());
                exp.setPubTitle(experiment.getPubTitle());
                exp.setPublicationDate(experiment.getPublicationDate());
                exp.setAbstraction(experiment.getAbstraction());
                exp.setExperimentDesign(experiment.getExperimentDesign());
                exp.setExperimentType(experiment.getExperimentType());
                exp.setAffiliations(experiment.getAffiliations());
                exp.setAuthors(experiment.getAuthors());
                exp.setPublication(experiment.getPublication());
                this.dmService.updateExperiment(exp);
                experiment = exp;

                //count all datasets if any
                totalDatasetNum = countTotalDatasetsNumber(experiment.getId());

                // format the description
                String desc = experiment.getDescription();
                if (StringUtils.isNotBlank(desc)) {
                    experiment.setDescription(newLineToBr(desc));
                }
                // format the abstraction
                String abstraction = experiment.getAbstraction();
                if (StringUtils.isNotBlank(abstraction)) {
                    experiment.setAbstraction(newLineToBr(abstraction));
                }

                // format the publication
                String publication = experiment.getPublication();
                if (StringUtils.isNotBlank(publication)) {
                    experiment.setPublication(newLineToBr(publication));
                }

            } else {
                addActionError(getText("experiment.edit.exp.failed.experiment.not.found"));
                setNavAndTitleForExpExc();
                return ERROR;
            }

            // set the delete experiment action name
            if (fromMyExp) {
                deleteExpActName = ActionConts.DELETE_MY_EXP_ACTION;
            } else {
                deleteExpActName = ActionConts.DELETE_MY_EXP_ACTION;
            }

            // record the updating auditing info
            recordAuditEventForUpdating(experiment, user);
            // set action successful message
            setSuccessActMsg(getText("experiment.edit.exp.update.success.message", new String[]{this.namePrefix + exp.getId()}));
            setNavTitleForExpDetails();
            //set the experiment reference link
            setExperimentReferenceLink(experiment);
        } catch (Exception e) {
            logger.error(e);
            addActionError("experiment.edit.exp.update.experiment.failed");
            setNavAndTitleForExpExc();
        }
        return SUCCESS;
    }

    public void validateEditExp() {
        boolean error = false;
        if (StringUtils.isBlank(experiment.getDescription())) {
            addFieldError("description", getText("experiment.edit.exp.desc.must.be.provided"));
            error = true;
        }
        if (StringUtils.isNotBlank(experiment.getPubMedId()) || StringUtils.isNotBlank(experiment.getPubTitle())
                || StringUtils.isNotBlank(experiment.getAbstraction()) || (experiment.getPublicationDate() != null)
                || StringUtils.isNotBlank(experiment.getAuthors()) || StringUtils.isNotBlank(experiment.getPublication())) {

            if (StringUtils.isBlank(experiment.getPubMedId())) {
                addFieldError("pubMedId", getText("experiment.edit.exp.pub.Id.must.be.provided"));
                error = true;
            }

            if (StringUtils.isBlank(experiment.getPubTitle())) {
                addFieldError("pubTitle", getText("experiment.edit.exp.pub.title.must.be.provided"));
                error = true;
            }

            if (experiment.getPublicationDate() == null) {
                addFieldError("publicationDate", getText("experiment.edit.exp.pub.date.must.be.provided"));
                error = true;
            }

            if (StringUtils.isBlank(experiment.getAbstraction())) {
                addFieldError("abstraction", getText("experiment.edit.exp.pub.abstraction.must.be.provided"));
                error = true;
            }

            if (StringUtils.isBlank(experiment.getAuthors())) {
                addFieldError("authors", getText("experiment.edit.exp.pub.authors.must.be.provided"));
                error = true;
            }

            if (StringUtils.isBlank(experiment.getPublication())) {
                addFieldError("publication", getText("experiment.edit.exp.pub.publication.must.be.provided"));
                error = true;
            }

        }
        if (error) {
            setNavAndTitleForExp();
            try {
                //get User
                user = getCurrentUser();
            } catch (Exception ex) {
                addFieldError("user", getText("experiment.edit.get.user.info.failed"));
            }
        }
    }

    // for experiment  title and navigation bar
    private void setNavAndTitleForExp() {
        setPageTitle(getText("experiment.edit.exp.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.edit.exp.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // for experiment title and navigation bar after exception
    private void setNavAndTitleForExpExc() {
        setPageTitle(getText("experiment.edit.exp.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.edit.exp.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    // for my experiment
    private void setNavTitleForExpDetails() {
        setPageTitle(getText("experiment.edit.exp.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        String thirdNav = this.namePrefix + experiment.getId();
        String thirdNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
            thirdNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
            thirdNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    // set the auditing event
    private void recordAuditEventForUpdating(Experiment exp, User operator) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        ev.setEvent(getText("experiment.edit.exp.update.success.message", new String[]{this.namePrefix + exp.getId()}));
        ev.setEventOwner(exp.getOwner());
        ev.setOperator(operator);
        recordActionAuditEvent(ev);
    }
}
