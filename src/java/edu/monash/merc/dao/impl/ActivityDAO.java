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

import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.Activity;
import edu.monash.merc.repository.IActivityRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ActivityDAO class which provides data access functionality for Activity domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class ActivityDAO extends HibernateGenericDAO<Activity> implements IActivityRepository {

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IActivityRepository#getActivityByActKey(java.lang.String)
      */
    @Override
    public Activity getActivityByActKey(String activityKey) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("activityKey", activityKey));
        return (Activity) criteria.uniqueResult();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IActivityRepository#getAllActivities()
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Activity> getAllActivities() {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.setComment("ActivityDAO.getAllActivities()");
        return criteria.list();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IActivityRepository#getActivitiesByExpId(long)
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Activity> getActivitiesByExpId(long expId) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        // create the alias for experiments
        Criteria expCrit = criteria.createAlias("experiments", "exps");
        expCrit.add(Restrictions.eq("exps.id", expId));
        return criteria.list();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IActivityRepository#deleteActivityById(long)
      */
    @Override
    public void deleteActivityById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS act WHERE act.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IActivityRepository#deleteActivityByActKey(java.lang.String)
      */
    @Override
    public void deleteActivityByActKey(String activityKey) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS act WHERE act.activityKey = :activityKey";
        Query query = this.session().createQuery(del_hql);
        query.setString("activityKey", activityKey);
        query.executeUpdate();
    }

}
