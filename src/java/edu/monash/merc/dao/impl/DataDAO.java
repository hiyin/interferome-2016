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
import edu.monash.merc.common.results.SearchResultRow;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.Data;
import edu.monash.merc.repository.IDataRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * DataDAO class which provides data access functionality for Data domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class DataDAO extends HibernateGenericDAO<Data> implements IDataRepository {

    @Override
    public void deleteDataById(long dataId) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS d WHERE d.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", dataId);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<SearchResultRow> getDataByDatasetId(long dsId, int startPageNo, int recordsPerPage, String orderBy, String sortBy) {
        String countHql = "SELECT count(d) FROM " + this.persistClass.getSimpleName() + " d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds WHERE ds.id=:dsId";
        String dataHql = "SELECT d, ds, p, g FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType WHERE ds.id =:dsId";
        Query countQuery = this.session().createQuery(countHql);
        countQuery.setParameter("dsId", dsId);
        int total = ((Long) countQuery.uniqueResult()).intValue();
        Pagination<SearchResultRow> p = new Pagination<SearchResultRow>(startPageNo, recordsPerPage, total);
        //create an order by conditions if any
        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            dataHql += orderByCond;
        }
        Query dataQuery = this.session().createQuery(dataHql);
        dataQuery.setParameter("dsId", dsId);
        dataQuery.setFirstResult(p.getFirstResult());
        dataQuery.setMaxResults(p.getSizePerPage());


        p.setPageResults(SearchResultRow.listFromQuery(dataQuery));

        return p;
    }

    private String createOrderBy(String orderBy, String sortBy) {
        if (StringUtils.equalsIgnoreCase(orderBy, "dataId")) {
            return " ORDER BY d.id " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "geneName")) {
            return " ORDER BY g.geneName " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "foldchange")) {
            return " ORDER BY d.value " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "genbank")) {
           return " ORDER BY g.genbankId " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "ensemblid")) {
            return " ORDER BY g.ensgAccession " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "probeid")) {
            return " ORDER BY p.probeId " + sortBy;
        }
        return null;
    }
}
