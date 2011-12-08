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
package edu.monash.merc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.impl.ExperimentDAO;
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.service.ExperimentService;

/**
 * ExperimentService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class ExperimentServiceImpl implements ExperimentService {

	@Autowired
	private ExperimentDAO experimentDao;

	/**
	 * @param experimentDao
	 *            the experimentDao to set
	 */
	public void setExperimentDao(ExperimentDAO experimentDao) {
		this.experimentDao = experimentDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getExperimentById(long)
	 */
	@Override
	public Experiment getExperimentById(long id) {
		return this.experimentDao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#saveExperiment(edu.monash.merc.domain.Experiment)
	 */
	@Override
	public void saveExperiment(Experiment exp) {
		this.experimentDao.add(exp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#updateExperiment(edu.monash.merc.domain.Experiment)
	 */
	@Override
	public void updateExperiment(Experiment exp) {
		this.experimentDao.update(exp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#deleteExperiment(edu.monash.merc.domain.Experiment)
	 */
	@Override
	public void deleteExperiment(Experiment exp) {
		this.experimentDao.remove(exp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getExperimentsByUserId(long)
	 */
	@Override
	public List<Experiment> getExperimentsByUserId(long uid) {
		return this.experimentDao.getExperimentsByUserId(uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getExperimentsByUserId(long, int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@Override
	public Pagination<Experiment> getExperimentsByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		return this.experimentDao.getExperimentsByUserId(uid, startPageNo, recordsPerPage, orderBys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getAllPublicExperiments(int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@Override
	public Pagination<Experiment> getAllPublicExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		return this.experimentDao.getAllPublicExperiments(startPageNo, recordsPerPage, orderBys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getAllExperiments(int, int, edu.monash.merc.common.sql.OrderBy[])
	 */
	@Override
	public Pagination<Experiment> getAllExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		return this.experimentDao.getAllExperiments(startPageNo, recordsPerPage, orderBys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getExperimentByExpIdUsrId(long, long)
	 */
	@Override
	public Experiment getExperimentByExpIdUsrId(long expId, long uid) {
		return this.experimentDao.getExperimentByExpIdUsrId(expId, uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#checkExperimentNameExisted(java.lang.String)
	 */
	@Override
	public boolean checkExperimentNameExisted(String expName) {
		return this.experimentDao.checkExperimentNameExisted(expName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getPublishedExperiments()
	 */
	@Override
	public List<Experiment> getPublishedExperiments() {
		return this.experimentDao.getPublishedExperiments();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ExperimentService#getPublishedExpByIdentifier(java.lang.String)
	 */
	@Override
	public Experiment getPublishedExpByIdentifier(String identifier) {
		return this.experimentDao.getPublishedExpByIdentifier(identifier);
	}
}
