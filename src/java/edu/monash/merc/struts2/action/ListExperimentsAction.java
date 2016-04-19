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

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * ListExperimentsAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.listExperimentsAction")
public class ListExperimentsAction extends DMBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private String viewExpActName;

	@PostConstruct
	public void initAct() {
		initExpPageParams();
		setExpDefaultPageParams();
	}

	public String listMyExperiments() {
		setNavAndTitleForMyExps();
		// set the view experiment details action name
		viewExpActName = ActionConts.VIEW_MY_EXP_DETAILS_ACTION;
		try {
			user = getCurrentUser();
			populatePaginationLink(ActionConts.LIST_MY_EXPERIMENTS_ACTION, ActionConts.PAGINATION_SUFFUX);
			exPagination = this.dmService.getExperimentsByUserId(user.getId(), pageNo, pageSize, populateOrderBy());

		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("experiments.get.my.experiments.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

	private void setNavAndTitleForMyExps() {
		setPageTitle(getText("experiment.list.my.all.experiments.action.title"));
		String secondNav = getText("experiment.list.my.all.experiments.action.title");
		String secondNavLink = "data/listMyExperiments.jspx";
		navBar = createNavBar("Experiment", null, secondNav, secondNavLink, null, null);
	}

	/**
	 * List all experiments for logged in users.
	 * 
	 * @return A String represents it is succeed or not.
	 */
	public String listExperiments() {
		setNavAndTitleForExps();
		// set the view experiment details action name
		viewExpActName = ActionConts.VIEW_EXP_DETAILS_ACTION;
		try {
			user = getCurrentUser();
			populatePaginationLink(ActionConts.LIST_EXPERIMENTS_ACTION, ActionConts.PAGINATION_SUFFUX);
			exPagination = this.dmService.getAllExperiments(pageNo, pageSize, populateOrderBy());
		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("experiments.get.all.experiments.failed"));
		}
		return SUCCESS;
	}

	private void setNavAndTitleForExps() {
		setPageTitle(getText("experiment.list.all.experiments.action.title"));
		String secondNav = getText("experiment.list.all.experiments.action.title");
		String secondNavLink = "data/listExperiments.jspx";
		navBar = createNavBar("Experiment", null, secondNav, secondNavLink, null, null);
	}

	/**
	 * @return the viewExpActName
	 */
	public String getViewExpActName() {
		return viewExpActName;
	}

	/**
	 * @param viewExpActName
	 *            the viewExpActName to set
	 */
	public void setViewExpActName(String viewExpActName) {
		this.viewExpActName = viewExpActName;
	}
}
