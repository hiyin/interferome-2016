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
import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.repository.IAuditEventRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuditEventDAO class which provides data access functionality for AuditEvent domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class AuditEventDAO extends HibernateGenericDAO<AuditEvent> implements IAuditEventRepository {

    @Override
    public void deleteAuditEventById(long eId) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ae WHERE ae.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", eId);
        query.executeUpdate();
    }

    @Override
    public void deleteEventByIdWithUserId(long eId, long userId) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ae WHERE ae.id = :eid AND ae.eventOwner.id = :uid";
        Query query = this.session().createQuery(del_hql);
        query.setLong("eid", eId);
        query.setLong("uid", userId);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<AuditEvent> getEventByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria userCriteria = criteria.createCriteria("eventOwner");
        userCriteria.add(Restrictions.eq("id", uid));
        criteria.setProjection(Projections.rowCount());
        // int total = ((Integer) criteria.list().get(0)).intValue();
        int total = ((Long) criteria.uniqueResult()).intValue();
        Pagination<AuditEvent> pev = new Pagination<AuditEvent>(startPageNo, recordsPerPage, total);
        // query collections by size-per-page
        Criteria queryCriteria = this.session().createCriteria(this.persistClass);
        Criteria qownerCrit = queryCriteria.createCriteria("eventOwner");
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
            queryCriteria.addOrder(Order.desc("createdTime"));
        }
        queryCriteria.setFirstResult(pev.getFirstResult());
        // set the max results (size-per-page)
        queryCriteria.setMaxResults(pev.getSizePerPage());
        List<AuditEvent> evlist = queryCriteria.list();
        pev.setPageResults(evlist);
        return pev;
    }

}
