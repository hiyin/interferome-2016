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
import edu.monash.merc.domain.Gene;
import edu.monash.merc.repository.ICiiiDERGeneRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dyin on 20/12/16.
 */
@Scope("prototype")
@Repository
public class CiiiDERGeneDAO extends HibernateGenericDAO<Gene> implements ICiiiDERGeneRepository {

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getIFNGeneAccessionBySpecies(int lowerValue, int upperValue, String speciesIdentifier) {
        String goIFNGeneHQL = String.format("SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between %d and %d and g.ensgAccession LIKE \'%s%%\'", lowerValue, upperValue, speciesIdentifier);
        Query goIFNGeneQuery = this.session().createQuery(goIFNGeneHQL);
        List<String> goIFNGeneList = goIFNGeneQuery.setMaxResults(100000).list();

        return goIFNGeneList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getBgGeneAccessionBySpecies(int lowerValue, int upperValue, String speciesIdentifier) {
        String goBgGeneHQL = String.format("SELECT DISTINCT g.ensgAccession From Gene g WHERE g.ensgAccession LIKE \'%s%%\' AND g.ensgAccession NOT IN (SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between %d and %d and g.ensgAccession LIKE \'%s%%\')", speciesIdentifier, lowerValue, upperValue, speciesIdentifier);
        Query goBgGeneQuery = this.session().createQuery(goBgGeneHQL);
        List<String> goBgGeneList = goBgGeneQuery.setMaxResults(5000).list();

        return goBgGeneList;
    }

}
