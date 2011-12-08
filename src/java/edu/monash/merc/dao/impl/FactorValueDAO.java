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
import edu.monash.merc.domain.ExperimentFactorValue;
import edu.monash.merc.dto.NameValueBean;
import edu.monash.merc.repository.IFactorValueRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FactorValueDAO class which provides data access functionality for ExperimentFactorValue domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class FactorValueDAO extends HibernateGenericDAO<ExperimentFactorValue> implements IFactorValueRepository {

    @Override
    public ExperimentFactorValue getFactorValueByNameValuePair(String fname, String fvalue) {
        Criteria fvCriteria = this.session().createCriteria(this.persistClass);
        Criteria fCriteria = fvCriteria.createCriteria("factor");
        fvCriteria.add(Restrictions.eq("factorValue", fvalue));
        fCriteria.add(Restrictions.eq("name", fname));
        return (ExperimentFactorValue) fvCriteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ExperimentFactorValue> getFactorValuesByDatasetId(long dsId) {
        Criteria fvCriteria = this.session().createCriteria(this.persistClass);
        Criteria dsCriteria = fvCriteria.createAlias("datasets", "ds");
        dsCriteria.add(Restrictions.eq("id", dsId));
        return fvCriteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NameValueBean> getFactorValuesBeanByDatasetId(long dsId) {
        String hql = "SELECT new edu.monash.merc.dto.NameValueBean(fv.id, f.name, fv.factorValue) FROM " + this.persistClass.getSimpleName() + " fv JOIN fv.datasets ds JOIN fv.factor f WHERE ds.id =:dsId";
        Query query = this.session().createQuery(hql);
        query.setLong("dsId", dsId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ExperimentFactorValue> getFactorValuesByFactorId(long factorId) {
        Criteria fvCriteria = this.session().createCriteria(this.persistClass);
        Criteria fCriteria = fvCriteria.createCriteria("factor");
        fCriteria.add(Restrictions.eq("id", factorId));
        return fvCriteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getFactorValuesByFactorName(String factorName) {
        String hql = "SELECT fv.factorValue FROM " + this.persistClass.getSimpleName() + " fv JOIN fv.factor f WHERE f.name =:factorName";
        Query query = this.session().createQuery(hql);
        query.setString("factorName", factorName);
        return query.list();
    }
}
