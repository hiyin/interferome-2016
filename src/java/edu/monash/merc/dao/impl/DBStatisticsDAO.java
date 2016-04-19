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

import edu.monash.merc.common.results.DBSpeciesStats;
import edu.monash.merc.common.results.DBStats;
import edu.monash.merc.dao.HibernateGenericDAO;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/*
edu.monash.merc.dao.impld with IntelliJ IDEA.
 * User: alex
 * Date: 1/31/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Scope("prototype")
@Repository
public class DBStatisticsDAO extends HibernateGenericDAO<DBStats> {

    Session s;

    public DBStats getDBStatistics() {
        s = this.session();
        DBStats stats = new DBStats();
        stats.human = querySpecies("Human");
        stats.mouse = querySpecies("Mouse");
        return stats;
    }

    private DBSpeciesStats querySpecies(String species) {
        DBSpeciesStats stats = new DBSpeciesStats();
        // Number of Experiments
        stats.experiments = countQuery("SELECT count(distinct e) from Experiment e inner join e.datasets ds inner join ds.data d where d.probe.species.speciesName = :speciesName", species);
        stats.experimentsI = countQuery("select count(distinct e) from Experiment e inner join e.datasets ds inner join ds.ifnType t inner join ds.data d where t.typeName = 'I' and d.probe.species.speciesName = :speciesName", species);
        stats.experimentsII = countQuery("select count(distinct e) from Experiment e inner join e.datasets ds inner join ds.ifnType t inner join ds.data d where t.typeName = 'II' and d.probe.species.speciesName = :speciesName", species);
        stats.experimentsIII = countQuery("select count(distinct e) from Experiment e inner join e.datasets ds inner join ds.ifnType t inner join ds.data d where t.typeName = 'III' and d.probe.species.speciesName = :speciesName", species);

        // Number of datasets in DB
        stats.datasets = countQuery("select count(distinct ds) from Dataset ds inner join ds.data d where d.probe.species.speciesName = :speciesName", species);
        stats.datasetsI = countQuery("select count(distinct ds) from Dataset ds inner join ds.data d inner join ds.ifnType t where t.typeName = 'I' AND d.probe.species.speciesName = :speciesName", species);
        stats.datasetsII = countQuery("select count(distinct ds) from Dataset ds inner join ds.data d inner join ds.ifnType t where t.typeName = 'II' AND d.probe.species.speciesName = :speciesName", species);
        stats.datasetsIII = countQuery("select count(distinct ds) from Dataset ds inner join ds.data d inner join ds.ifnType t where t.typeName = 'III' AND d.probe.species.speciesName = :speciesName", species);

        // Number of genes that have been shown to be regulated by interferon greater than x2 fold change
        stats.fc = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where abs(d.value) >= 2 and d.probe.species.speciesName = :speciesName", species);
        stats.fcI = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where abs(d.value) >= 2 and d.dataset.ifnType.typeName = 'I' and d.probe.species.speciesName = :speciesName", species);
        stats.fcII = countQuery("SELECT  count(distinct g) from Data d inner join d.probe.genes g where abs(d.value) >= 2 and d.dataset.ifnType.typeName = 'II' and d.probe.species.speciesName = :speciesName", species);
        stats.fcIII = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where abs(d.value) >= 2 and d.dataset.ifnType.typeName = 'III' and d.probe.species.speciesName = :speciesName", species);

        // Number of ALL genes that have been shown to be regulated by interferon in DB
    //    stats.allData = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where d.probe.species.speciesName = :speciesName", species);
    //    stats.allDataI = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where d.dataset.ifnType.typeName = 'I' and d.probe.species.speciesName = :speciesName", species);
    //    stats.allDataII = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where d.dataset.ifnType.typeName = 'II' and d.probe.species.speciesName = :speciesName", species);
    //    stats.allDataIII = countQuery("SELECT count(distinct g) from Data d inner join d.probe.genes g where d.dataset.ifnType.typeName = 'III' and d.probe.species.speciesName = :speciesName", species);

        return stats;
    }

    private long countQuery(String queryString, String species){
        return (Long) s.createQuery(queryString).setString("speciesName", species).list().get(0);
    }

}
