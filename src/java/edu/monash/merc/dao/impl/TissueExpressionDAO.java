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
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.Tissue;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.TissueExpression;
import edu.monash.merc.repository.ITissueExpressionRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12/02/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Scope("prototype")
@Repository
 public class TissueExpressionDAO extends HibernateGenericDAO<TissueExpression> implements ITissueExpressionRepository {


        @SuppressWarnings("unchecked")
        @Override
        public List<TissueExpression> getTissueByProbeId(String probeId) {
            Criteria criteria = this.session().createCriteria(this.persistClass);
            // create the alias for probe
            Criteria probeCriteria = criteria.createAlias("probeId","probeId");
            probeCriteria.add(Restrictions.eq("probe.probeId", probeId));
            return criteria.list();
        }

        @SuppressWarnings("unchecked")
        @Override
        public TissueExpression getTissueExpressionByProbeAndTissue(Probe probe, Tissue tissue) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria tissueCriteria = criteria.createCriteria("tissue");
        tissueCriteria.add(Restrictions.eq("id", tissue.getId()));
        Criteria probeCriteria = criteria.createCriteria("probe");
        probeCriteria.add(Restrictions.eq("id", probe.getId()));
        return (TissueExpression) criteria.uniqueResult();
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<TissueExpression> getTissueByTissueId(String tissueId) {
            Criteria criteria = this.session().createCriteria(this.persistClass);
            // create the alias for tissues
            Criteria probeCriteria = criteria.createAlias("tissueId","tissueId");
            probeCriteria.add(Restrictions.eq("tissue.tissueId", tissueId));
            return criteria.list();
        }
 }
