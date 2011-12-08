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
package edu.monash.merc.dao.impl;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.domain.PermType;
import edu.monash.merc.repository.IExperimentRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ExperimentDAO class which provides data access functionality for Experiment domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class ExperimentDAO extends HibernateGenericDAO<Experiment> implements IExperimentRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getExperimentsByUserId(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Experiment> getExperimentsByUserId(long uid) {
		Criteria criteria = this.session().createCriteria(this.persistClass);
		Criteria userCrit = criteria.createCriteria("owner");
		userCrit.add(Restrictions.eq("id", uid));
		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getExperimentsByUserId(long, int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pagination<Experiment> getExperimentsByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		// count total
		Criteria criteria = this.session().createCriteria(this.persistClass);
		Criteria userCriteria = criteria.createCriteria("owner");
		userCriteria.add(Restrictions.eq("id", uid));
		criteria.setProjection(Projections.rowCount());
		int total = ((Long) criteria.uniqueResult()).intValue();
		Pagination<Experiment> p = new Pagination<Experiment>(startPageNo, recordsPerPage, total);
		// query experiments by size-per-page
		Criteria queryCriteria = this.session().createCriteria(this.persistClass);
		Criteria qownerCrit = queryCriteria.createCriteria("owner");
		qownerCrit.add(Restrictions.eq("id", uid));
		// add orders
		if (orderBys != null && orderBys.length > 0) {
			for (int i = 0; i < orderBys.length; i++) {
				Order order = orderBys[i].getOrder();
				if (order != null) {
					queryCriteria.addOrder(order);
				}
			}
		} else {
			queryCriteria.addOrder(Order.desc("name"));
		}
		// calculate the first result from the pagination and set this value into the start search index
		queryCriteria.setFirstResult(p.getFirstResult());
		// set the max results (size-per-page)
		queryCriteria.setMaxResults(p.getSizePerPage());
		List<Experiment> explist = queryCriteria.list();
		p.setPageResults(explist);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getAllPublicExperiments(int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pagination<Experiment> getAllPublicExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		// count total
		Criteria criteria = this.session().createCriteria(this.persistClass);
		// we only list experiments which are approved and allowed the anonymous user to view
		criteria.add(Restrictions.eq("approved", true));
		Criteria permCriteria = criteria.createCriteria("permissions");
		permCriteria.add(Restrictions.eq("viewAllowed", true)).add(Restrictions.eq("permType", PermType.ANONYMOUS.code()));

		criteria.setProjection(Projections.rowCount());
		int total = ((Long) criteria.uniqueResult()).intValue();

		Pagination<Experiment> p = new Pagination<Experiment>(startPageNo, recordsPerPage, total);

		Criteria findCriteria = this.session().createCriteria(this.persistClass);
		findCriteria.add(Restrictions.eq("approved", true));

		Criteria findPermCriteria = findCriteria.createCriteria("permissions");
		findPermCriteria.add(Restrictions.eq("viewAllowed", true)).add(Restrictions.eq("permType", PermType.ANONYMOUS.code()));

		// add orders
		if (orderBys != null && orderBys.length > 0) {
			for (int i = 0; i < orderBys.length; i++) {
				Order order = orderBys[i].getOrder();
				if (order != null) {
					findCriteria.addOrder(order);
				}
			}
		} else {
			findCriteria.addOrder(Order.desc("name"));
		}

		// calculate the first result from the pagination and set this value into the start search index
		findCriteria.setFirstResult(p.getFirstResult());
		// set the max results (size-per-page)
		findCriteria.setMaxResults(p.getSizePerPage());

		List<Experiment> explist = findCriteria.list();

		p.setPageResults(explist);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getAllExperiments(int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pagination<Experiment> getAllExperiments(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		// count total
		Criteria criteria = this.session().createCriteria(this.persistClass);
		criteria.setProjection(Projections.rowCount());
		int total = ((Long) criteria.uniqueResult()).intValue();
		Pagination<Experiment> p = new Pagination<Experiment>(startPageNo, recordsPerPage, total);

		// query experiment by size-per-page
		Criteria queryCriteria = this.session().createCriteria(this.persistClass);
		// add orders
		if (orderBys != null && orderBys.length > 0) {
			for (int i = 0; i < orderBys.length; i++) {
				Order order = orderBys[i].getOrder();
				if (order != null) {
					queryCriteria.addOrder(order);
				}
			}
		} else {
			queryCriteria.addOrder(Order.desc("name"));
		}

		// calculate the first result from the pagination and set this value into the start search index
		queryCriteria.setFirstResult(p.getFirstResult());
		// set the max results (size-per-page)
		queryCriteria.setMaxResults(p.getSizePerPage());
		queryCriteria.setComment("getAllExperiments");
		List<Experiment> explist = queryCriteria.list();
		p.setPageResults(explist);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getExperimentByExpIdUsrId(long, long)
	 */
	@Override
	public Experiment getExperimentByExpIdUsrId(long expId, long uid) {
		Criteria criteria = this.session().createCriteria(this.persistClass);
		Criteria usrCrit = criteria.createCriteria("owner");
		criteria.add(Restrictions.eq("id", expId));
		usrCrit.add(Restrictions.eq("id", uid));
		criteria.setComment("getExperimentByExpIdUsrId");
		return (Experiment) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#checkExperimentNameExisted(java.lang.String)
	 */
	@Override
	public boolean checkExperimentNameExisted(String expName) {
		Criteria criteria = this.session().createCriteria(this.persistClass);
		int num = ((Long) criteria.setProjection(Projections.rowCount()).add(Restrictions.eq("name", expName)).uniqueResult()).intValue();
		if (num == 1) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getPublishedExperiments()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Experiment> getPublishedExperiments() {
		Criteria criteria = this.session().createCriteria(this.persistClass);
		criteria.add(Restrictions.eq("published", true));
		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IExperimentRepository#getPublishedExpByIdentifier(java.lang.String)
	 */
	@Override
	public Experiment getPublishedExpByIdentifier(String identifier) {
		Criteria criteria = this.session().createCriteria(this.persistClass);
		criteria.add(Restrictions.eq("published", true)).add(Restrictions.eq("pIdentifier", identifier));
		return (Experiment) criteria.uniqueResult();
	}

}
