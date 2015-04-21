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

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.results.SearchResultRow;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.Data;
import edu.monash.merc.domain.Dataset;
import edu.monash.merc.dto.NameValueBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * DatasetAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.datasetAction")
public class DatasetAction extends DMBaseAction {

    private Dataset dataset;

    private List<NameValueBean> nameValueBeans;

    private String viewExpActName;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Pagination<SearchResultRow> dataPagination;

    /**
     * genbank link
     */
    private String genBankLink;

    /**
     * ensembl link
     */
    private String ensemblLink;

    @PostConstruct
    public void initDsAct() {
        initDataPagination();
        setDefaultPageParams();
        loadResourceLinks();
    }

    private void loadResourceLinks() {
        this.ensemblLink = this.appSetting.getPropValue(AppPropSettings.ENSEMBL_SUMMARY_LINK);
        this.genBankLink = this.appSetting.getPropValue(AppPropSettings.GENBANK_SUMMARY_LINK);
    }

    //initialize the pagination parameters
    protected void initDataPagination() {
        // page size per page values
        pageSizeMap.put(20, 20);
        pageSizeMap.put(30, 30);
        pageSizeMap.put(50, 50);
        pageSizeMap.put(100, 100);
        pageSizeMap.put(150, 150);
        pageSizeMap.put(200, 200);

        // orderby values
        orderByMap.clear();
        orderByMap.put("dataId", "Id");
        orderByMap.put("foldchange", "Fold Change");
        orderByMap.put("genename", "Gene Symbol");
        orderByMap.put("genbank", "GenBank");
        orderByMap.put("ensemblid", "Ensembl Id");
        orderByMap.put("probeid", "Probe Id");
        // orderby type values
        orderByTypeMap.put("ASC", "asc");
        orderByTypeMap.put("DESC", "desc");
    }

    //set the default pagination parameters
    protected void setDefaultPageParams() {
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "genename";
        }
        if (StringUtils.isBlank(orderByType)) {
            orderByType = ActionConts.ASC_SORT_TYPE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = 30;
        }
    }

    /**
     * view dataset for logged in user
     *
     * @return SUCCESS if succeed
     */
    public String viewDataset() {
        //set view experiment action name  and view dataset action name
        setViewActs();
        try {
            //get the experiment details
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isViewAllowed()) {
                    addActionError(getText("experiment.view.dataset.permission.denied"));
                    setNavAndTitleForViewDsExc();
                    return INPUT;
                }
            } else {
                addActionError(getText("experiment.view.dataset.failed.experiment.not.found"));
                setNavAndTitleForViewDsExc();
                return ERROR;
            }
            //get dataset
            dataset = this.dmService.getDataset(dataset.getId());
            //get factor values;
            nameValueBeans = this.dmService.getFactorValuesBeanByDatasetId(dataset.getId());
            //get pagination data
            dataPagination = this.dmService.getDataByDatasetId(dataset.getId(), pageNo, pageSize, orderBy, orderByType);
            //set action title and nav bar
            setNavAndTitleForViewDs();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("experiment.view.dataset.failed");
            setNavAndTitleForViewDsExc();
            return ERROR;
        }
        return SUCCESS;
    }

    private void setViewActs() {
        if (fromMyExp) {
            viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
        } else {
            viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
        }
        pageLink = "data/viewDataset.jspx";
        pageSuffix = ActionConts.PAGINATION_SUFFUX;
    }

    private void setNavAndTitleForViewDs() {
        setPageTitle(getText("experiment.view.dataset.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = null;
        if (fromMyExp) {
            secondNavLink = "data/viewMyExperiment.jspx?experiment.id=" + experiment.getId();
        } else {
            secondNavLink = "data/viewExperiment.jspx?experiment.id=" + experiment.getId();
        }
        String thirdNav = getText("experiment.view.dataset.action.title");
        String thirdNavLink = "data/viewDataset.jspx?experiment.id=" + experiment.getId() + "&dataset.id=" + dataset.getId() + "&fromMyExp=" + fromMyExp;
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    public void setNavAndTitleForViewDsExc() {
        setPageTitle(getText("experiment.view.dataset.action.title"));
        String secondNav = null;
        String secondNavLink = null;
        if (fromMyExp) {
            secondNav = getText("experiment.list.my.all.experiments.action.title");
            secondNavLink = "data/listMyExperiments.jspx";
        } else {
            secondNav = getText("experiment.list.all.experiments.action.title");
            secondNavLink = "data/listExperiments.jspx";
        }
        String thirdNav = getText("experiment.view.dataset.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    /**
     * view the public dataset
     *
     * @return SUCCESS if succeed
     */
    public String viewPubDataset() {
        try {
            // check user is already logged in or not
            long userId = getCurrentUserId();
            if (userId != 0) {
                redNamespace = "/data";
                redActionName = "viewDataset.jspx";
                return REDIRECTDS;
            }
            //set view public dataset action name
            setViewPubDsAct();
            //get the experiment details
            experiment = this.dmService.getExperimentById(experiment.getId());
            if (experiment != null) {
                // check the permissions for this experiment
                checkExpPermsForUser(experiment.getId(), experiment.getOwner().getId());
                if (!permissionBean.isViewAllowed()) {
                    addActionError(getText("experiment.view.dataset.permission.denied"));
                    setNavAndTitleForViewPubDsExc();
                    return INPUT;
                }
            } else {
                addActionError(getText("experiment.view.dataset.failed.experiment.not.found"));
                setNavAndTitleForViewPubDsExc();
                return ERROR;
            }
            //get dataset
            dataset = this.dmService.getDataset(dataset.getId());
            //get factor values;
            nameValueBeans = this.dmService.getFactorValuesBeanByDatasetId(dataset.getId());
            //get pagination data
            dataPagination = this.dmService.getDataByDatasetId(dataset.getId(), pageNo, pageSize, orderBy, orderByType);
            //set action title and nav bar
            setNavAndTitleForViewPubDs();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("experiment.view.dataset.failed");
            setNavAndTitleForViewPubDsExc();
            return ERROR;
        }
        return SUCCESS;
    }

    private void setViewPubDsAct() {
        pageLink = "pubdata/viewDataset.jspx";
        pageSuffix = ActionConts.PAGINATION_SUFFUX;
    }

    private void setNavAndTitleForViewPubDs() {
        setPageTitle(getText("experiment.view.dataset.action.title"));
        String secondNav = this.namePrefix + experiment.getId();
        String secondNavLink = "pubdata/viewExperiment.jspx?experiment.id=" + experiment.getId();
        String thirdNav = getText("experiment.view.dataset.action.title");
        String thirdNavLink = "pubdata/viewDataset.jspx?experiment.id=" + experiment.getId() + "&dataset.id=" + dataset.getId();
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    public void setNavAndTitleForViewPubDsExc() {
        setPageTitle(getText("experiment.view.dataset.action.title"));
        String secondNav = null;
        String secondNavLink = "pubdata/listExperiments.jspx";
        String thirdNav = getText("experiment.view.dataset.action.title");
        navBar = createNavBar("Experiment", null, secondNav, secondNavLink, thirdNav, null);
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public List<NameValueBean> getNameValueBeans() {
        return nameValueBeans;
    }

    public void setNameValueBeans(List<NameValueBean> nameValueBeans) {
        this.nameValueBeans = nameValueBeans;
    }

    public String getViewExpActName() {
        return viewExpActName;
    }

    public void setViewExpActName(String viewExpActName) {
        this.viewExpActName = viewExpActName;
    }

    public Pagination<SearchResultRow> getDataPagination() {
        return dataPagination;
    }

    public void setDataPagination(Pagination<SearchResultRow> dataPagination) {
        this.dataPagination = dataPagination;
    }

    public String getGenBankLink() {
        return genBankLink;
    }

    public void setGenBankLink(String genBankLink) {
        this.genBankLink = genBankLink;
    }

    public String getEnsemblLink() {
        return ensemblLink;
    }

    public void setEnsemblLink(String ensemblLink) {
        this.ensemblLink = ensemblLink;
    }
}
