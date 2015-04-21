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
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.GeneExpressionRecord;
import edu.monash.merc.dto.RangeCondition;
import edu.monash.merc.dto.SearchBean;
import edu.monash.merc.dto.VariationCondtion;
import edu.monash.merc.repository.ISearchDataRepository;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * SearchDataDAO class which provides searching functionality for Data domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 *
 * @author Irina Rusinova - irina.rusinova@monash.edu
 * @version 2.01
 */
@Scope("prototype")
@Repository
public class SearchDataDAO extends HibernateGenericDAO<Data> implements ISearchDataRepository {

    @SuppressWarnings("unchecked")
    private List<Long> queryDatasets(SearchBean searchBean) {
        String baseDatasetHql = "SELECT DISTINCT(ds.id) FROM Dataset ds";
        String ifnType = searchBean.getIfnType();
        String ifnSubType = searchBean.getIfnSubType();
        //interferon type and subtype
        if (!StringUtils.equals(ifnType, "-1")) {
            baseDatasetHql += " JOIN ds.ifnType ifnType ";
        }
        //normal or abnormal or any variations
        VariationCondtion variationCondtion = searchBean.getVariationCondtion();
        if (variationCondtion.isVarProvided()) {
            baseDatasetHql += " JOIN ds.ifnVar ifnVar ";
        }
        //factor list;
        List<List<String>> factorLists = genFactorParams(searchBean);
        //create factor values condition
        if (factorLists.size() > 0) {
            baseDatasetHql += " JOIN ds.factorValues fvs ";
        }
        //WHERE Clause flag
        boolean whereClause = false;
        //type conditions
        String typeCond = createTypeCond(searchBean);
        if (StringUtils.isNotBlank(typeCond)) {
            baseDatasetHql += typeCond;
            whereClause = true;
        }

        //treatment concentration dose conditions
        RangeCondition doseRangeCondition = searchBean.getDoseRangeCondition();
        if (doseRangeCondition.isRangeProvided()) {
            String doseCond = createDoseCond(doseRangeCondition);
            if (StringUtils.isNotBlank(doseCond)) {
                if (whereClause) {
                    baseDatasetHql += " AND " + doseCond;
                } else {
                    baseDatasetHql += " WHERE" + doseCond;
                    whereClause = true;
                }
            }
        }

        //treatment time conditions
        RangeCondition ttimeRangeCondition = searchBean.getTimeRangeCondition();
        if (ttimeRangeCondition.isRangeProvided()) {
            String ttimeCond = createTtimeCond(ttimeRangeCondition);
            if (StringUtils.isNotBlank(ttimeCond)) {
                if (whereClause) {
                    baseDatasetHql += " AND" + ttimeCond;
                } else {
                    baseDatasetHql += " WHERE" + ttimeCond;
                    whereClause = true;
                }
            }
        }
        //Vivo or vivtro
        String vivoCond = createVivoVitroCond(searchBean);
        if (StringUtils.isNotBlank(vivoCond)) {
            if (whereClause) {
                baseDatasetHql += " AND" + vivoCond;
            } else {
                baseDatasetHql += " WHERE" + vivoCond;
                whereClause = true;
            }
        }

        //normal abnormal or any variations
        if (variationCondtion.isVarProvided()) {
            String varCond = createVarCond(variationCondtion);
            if (StringUtils.isNotBlank(varCond)) {
                if (whereClause) {
                    baseDatasetHql += " AND" + varCond;
                } else {
                    baseDatasetHql += " WHERE" + varCond;
                    whereClause = true;
                }
            }
        }

        List<Long> foundDsIds = new ArrayList<Long>();
        //factor values;
        if (factorLists.size() > 0) {
            if (!whereClause) {
                baseDatasetHql += " WHERE";
                whereClause = true;
            } else {
                baseDatasetHql += " AND ";
            }

            for (List<String> factors : factorLists) {
                String factorHQL = baseDatasetHql + " fvs.factorValue IN (:factorvalues) GROUP BY ds HAVING COUNT(fvs) = :fv_count";
                Query findDsQuery = this.session().createQuery(factorHQL);
                findDsQuery.setParameterList(("factorvalues"), factors);
                findDsQuery.setInteger(("fv_count"), factors.size());
                List<Long> foundTmp = findDsQuery.list();

                for (Long tmpdsid : foundTmp) {
                    if (!foundDsIds.contains(tmpdsid)) {
                        foundDsIds.add(tmpdsid);
                    }
                }
            }
        } else {
            Query findDsQuery = this.session().createQuery(baseDatasetHql);
            foundDsIds = findDsQuery.list();
        }
        return foundDsIds;
    }

    // create the interferon type conditions
    private String createTypeCond(SearchBean searchBean) {
        String ifnType = searchBean.getIfnType();
        String ifnSubType = searchBean.getIfnSubType();
        if (!StringUtils.equals(ifnType, "-1")) {
            String typeCond = " WHERE ifnType.typeName = '" + ifnType + "'";
            if (StringUtils.isNotBlank(ifnSubType) && !StringUtils.equals(ifnSubType, "-1")) {
                typeCond += " AND ifnType.subTypeName = '" + ifnSubType + "'";
            }
            return typeCond;
        }
        return null;
    }

    //create the treatment concentration conditions
    private String createDoseCond(RangeCondition doseRangeCondition) {
        //treatment concentration dose conditions
        if (doseRangeCondition.isRangeProvided()) {
            double fromDose = doseRangeCondition.getFromValue();
            double toDose = doseRangeCondition.getToValue();
            if ((fromDose > 0) && (toDose > 0) && (fromDose == toDose)) {
                return " ds.treatmentCon = " + fromDose;
            }
            if ((fromDose > 0) && (toDose > 0) && (fromDose < toDose)) {
                return " ds.treatmentCon >= " + fromDose + " AND ds.treatmentCon <= " + toDose;
            }
            if ((fromDose == 0) && (toDose > 0)) {
                return " ds.treatmentCon <= " + toDose;
            }
            if ((fromDose > 0) && (toDose == 0)) {
                return " ds.treatmentCon >= " + fromDose;
            }
        }
        return null;
    }

    //create the treatment time conditions
    private String createTtimeCond(RangeCondition ttimeRangeCondition) {
        //treatment time conditions
        if (ttimeRangeCondition.isRangeProvided()) {
            double fromTime = ttimeRangeCondition.getFromValue();
            double toTime = ttimeRangeCondition.getToValue();
            if ((fromTime > 0) && (toTime > 0) && (fromTime == toTime)) {
                return " ds.treatmentTime = " + fromTime;
            }
            if ((fromTime > 0) && (toTime > 0) && (fromTime < toTime)) {
                return " ds.treatmentTime >= " + fromTime + " AND ds.treatmentTime <= " + toTime;
            }
            if ((fromTime == 0) && (toTime > 0)) {
                return " ds.treatmentTime <= " + toTime;
            }
            if ((fromTime > 0) && (toTime == 0)) {
                return " ds.treatmentTime >= " + fromTime;
            }
        }
        return null;
    }

    //create vivo or vitro conditions
    private String createVivoVitroCond(SearchBean searchBean) {
        //vivo or vitro type
        String vivoVitro = searchBean.getVivoVitro();
        if (!StringUtils.equals(vivoVitro, "-1")) {
            boolean isVivo = false;
            if (StringUtils.equalsIgnoreCase(vivoVitro, "In Vivo")) {
                isVivo = true;
            }
            return " ds.inVivo = " + isVivo;
        }
        return null;
    }

    //create normal or abnormal variation conditions
    private String createVarCond(VariationCondtion variationCondtion) {
        if (variationCondtion.isVarProvided()) {
            String varValue = variationCondtion.getVarValue();
            boolean isAbnormal = variationCondtion.isAbnormal();
            String varCond = " ifnVar.abnormal = " + isAbnormal;
            if (!StringUtils.equals(varValue, "-1")) {
                varCond += " AND ifnVar.value = '" + varValue + "'";
            }
            return varCond;
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    @Override
    public Pagination<SearchResultRow> search(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        boolean noneDsQuery = searchBean.isNoneDsCondition();
        //no dataset level search condition, then just search data only, otherwise we will search data based on dataset conditions
        if (noneDsQuery) {
            return searchDataOnly(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        } else {
            return searchDataWithDs(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<Gene> searchGenes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {

        int searchCount=0, gbCount=0, ensCount=0, gCount = 0;
        String species = searchBean.getSpecies();
        if (StringUtils.equals(species, "-1")||StringUtils.contains(species, "Homo sapiens") || StringUtils.contains(species, "Mus musculus")){
        if (searchBean.getGenBanks().length() > 0){
            gbCount = searchBean.getGenBanks().split(",|\t|\n").length;
            searchCount += gbCount;
        }
        if (searchBean.getEnsembls().length() > 0){
            ensCount = searchBean.getEnsembls().split(",|\t|\n").length;
            searchCount += ensCount;
        }
        if (searchBean.getGenes().length() > 0){
            gCount = searchBean.getGenes().split(",|\t|\n").length;
            searchCount += gCount;
        }
    }

        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);
        List<Probe> probes = uniqueProbesPages.getPageResults();
        if (probes.size() > 0) {

            String geneCountHQL = "SELECT COUNT(DISTINCT g) FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes)";
            Query geneCountQuery = this.session().createQuery(geneCountHQL);
            geneCountQuery.setParameterList(("probes"), probes);
            int total = ((Long) geneCountQuery.uniqueResult()).intValue();
            if (total == 0) {
                return new Pagination<Gene>(startPageNo, recordPerPage, total, searchCount);
            }

            String geneHQL = "SELECT  DISTINCT g  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) ORDER BY g." + orderBy + " " + sortBy;
            Query geneQuery = this.session().createQuery(geneHQL);
            geneQuery.setParameterList(("probes"), probes);

            Pagination<Gene> genePagination = new Pagination<Gene>(startPageNo, recordPerPage, total, searchCount);
            String orderByCond = createGeneOrderBy(orderBy, sortBy);
            if (StringUtils.isNotBlank(orderByCond)) {
                geneHQL += orderByCond;
            }
            geneQuery.setFirstResult(genePagination.getFirstResult());
            geneQuery.setMaxResults(genePagination.getSizePerPage());

            List<Gene> geneList = geneQuery.list();
            genePagination.setPageResults(geneList);
            return genePagination;
        } else {
            return new Pagination<Gene>(startPageNo, recordPerPage, 0, searchCount);
        }
    }

    private String createGeneOrderBy(String orderBy, String sortBy) {
        if (StringUtils.equalsIgnoreCase(orderBy, "geneName")) {
            return " ORDER BY g.geneName " + sortBy;
        }
        if (StringUtils.equalsIgnoreCase(orderBy, "ensemblid")) {
            return " ORDER BY g.ensgAccession " + sortBy;
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<GeneExpressionRecord> searchTissueExpression(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);
        List<Probe> probes = uniqueProbesPages.getPageResults();
        if (probes.size() > 0) {
            List<Gene> gnenNameList = new ArrayList<Gene>();
            String gnHQL = "SELECT DISTINCT g.geneName  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) GROUP BY g.geneName ORDER BY g";
            Query gnQuery = this.session().createQuery(gnHQL);
            gnQuery.setParameterList(("probes"), probes);
            gnenNameList = gnQuery.list();

            String teHQL ="SELECT DISTINCT g.geneName, te, s.speciesName FROM TissueExpression te INNER JOIN te.probe pbs INNER JOIN pbs.species s INNER JOIN pbs.genes g WHERE g.geneName IN (:gnenNameList) ORDER BY te.tissue.tissueId";
            Query teQuery = this.session().createQuery(teHQL);
            teQuery.setParameterList(("gnenNameList"), gnenNameList);

            ArrayList<GeneExpressionRecord> geneExpressionRecords = new ArrayList<GeneExpressionRecord>();
            for (Object rowObject : teQuery.list()) {
                Object[] row = (Object[]) rowObject;
                assert row.length == 3;
                geneExpressionRecords.add(new GeneExpressionRecord((String) row[0], (TissueExpression) row[1],  (String) row[2]));
            }
            return geneExpressionRecords;

        } else {
            return new ArrayList<GeneExpressionRecord>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchChromosome(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);
        List<Probe> probes = uniqueProbesPages.getPageResults();
        if (probes.size() > 0) {
            String chrHQL = "SELECT g.chromosome, count(DISTINCT g)  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) AND g.ensgAccession like 'ENSG' GROUP BY g.chromosome ORDER BY count(distinct g) DESC";
            Query chrQuery = this.session().createQuery(chrHQL);
            chrQuery.setParameterList(("probes"), probes);

            List<Object[]> chromosomeList = chrQuery.list();
            return chromosomeList;
        } else {
            return new ArrayList<Object[]>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer[] searchSubtypes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);
        List<Probe> probes = uniqueProbesPages.getPageResults();
        //T1, T2, T3, T1T2, T1T3, T2T3, T1T2T3
        Integer[] types = {0, 0, 0, 0, 0, 0, 0};
        if (probes.size() > 0) {

            double upValue = searchBean.getUpValue();
            double downValue = searchBean.getDownValue();
            //Get Type 1 Probes
            //String tp1 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
            //        "WHERE p.probeId IN (:probes) AND i.typeName = 'I'";

            //Ross code to test for fold change

            String tp1 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
                            "WHERE p.probeId IN (:probes) AND i.typeName = 'I' and (d.value >= " + upValue + " or d.value <= -" + downValue + ")";
            Query tp1Query = this.session().createQuery(tp1);
            tp1Query.setParameterList(("probes"), probes);
            List<String> t1ProbeList = tp1Query.list();

            //Get Gene
            //sam version
            // String tg1 = "SELECT distinct g  FROM Gene g INNER JOIN g.probes p WHERE p.probeId IN (:t1ProbeList) GROUP BY g";

            //simon version, remove the 'GROUP BY', the 'GROUP BY' only used for counting
            //if T1 probe list is empty. we don't need to query the genes
            List<Gene> t1GeneList = new ArrayList<Gene>();
            if (t1ProbeList != null && t1ProbeList.size() > 0) {
                String tg1 = "SELECT distinct g  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:t1ProbeList) ";
                Query tg1Query = this.session().createQuery(tg1);
                tg1Query.setParameterList("t1ProbeList", t1ProbeList);
                t1GeneList = tg1Query.list();
            }

            //Get Type 2 Probes
            //String tp2 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
            //        "WHERE p.probeId IN (:probes) AND i.typeName = 'II'";

            //Ross code to test for fold change
            String tp2 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
                    "WHERE p.probeId IN (:probes) AND i.typeName = 'II' and (d.value >= " + upValue + " or d.value <= -" + downValue + ")";
            Query tp2Query = this.session().createQuery(tp2);
            tp2Query.setParameterList(("probes"), probes);
            List<String> t2ProbeList = tp2Query.list();

            //Get Gene
            //sam version
            //String tg2 = "SELECT distinct g  FROM Gene g INNER JOIN g.probes p WHERE p.probeId IN (:t2ProbeList) GROUP BY g";

            //simon version, remove the 'GROUP BY', the 'GROUP BY' only used for counting
            //if T2 probe list is empty we don't need to query the genes
            List<Gene> t2GeneList = new ArrayList<Gene>();
            if (t2ProbeList != null && t2ProbeList.size() > 0) {
                String tg2 = "SELECT distinct g  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:t2ProbeList) ";
                Query tg2Query = this.session().createQuery(tg2);
                tg2Query.setParameterList("t2ProbeList", t2ProbeList);
                t2GeneList = tg2Query.list();
            }

            //Get Type III Probes
            //String tp3 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
            //        "WHERE p.probeId IN (:probes) AND i.typeName = 'III'";

            //Ross code to test for fold change
            String tp3 = "SELECT distinct p.probeId  FROM Data d INNER JOIN d.probe p INNER JOIN d.dataset ds INNER JOIN ds.ifnType i " +
                    "WHERE p.probeId IN (:probes) AND i.typeName = 'III' and (d.value >= " + upValue + " or d.value <= -" + downValue + ")";
            Query tp3Query = this.session().createQuery(tp3);
            tp3Query.setParameterList(("probes"), probes);
            List<String> t3ProbeList = tp3Query.list();

            //Get Gene
            //sam version
            // String tg3 = "SELECT distinct g  FROM Gene g INNER JOIN g.probes p WHERE p.probeId IN (:t3ProbeList) GROUP BY g";

            //simon version, remove the 'GROUP BY', the 'GROUP BY' only used for counting
            //if the T3 probe list is empty. we don't need to query the genes
            List<Gene> t3GeneList = new ArrayList<Gene>();
            if (t3ProbeList != null && t3ProbeList.size() > 0) {
                String tg3 = "SELECT distinct g  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:t3ProbeList) ";
                Query tg3Query = this.session().createQuery(tg3);
                tg3Query.setParameterList(("t3ProbeList"), t3ProbeList);
                t3GeneList = tg3Query.list();
            }

            //Count Unique members of each list
            //T1, T2, T3, T1T2, T1T3, T2T3, T1T2T3
            //T1T2T3
            types[6] = findOverlapGenes(findOverlapGenes(t1GeneList, t2GeneList), t3GeneList).size();
            //T2T3
            types[5] = findOverlapGenes(t2GeneList, t3GeneList).size() - types[6];
            //T1T3
            types[4] = findOverlapGenes(t1GeneList, t3GeneList).size() - types[6];
            //T1T2
            types[3] = findOverlapGenes(t1GeneList, t2GeneList).size() - types[6];
            //T3   -> - T123 - T13 - T23
            if (t3GeneList.size() == 0) {
                types[2] = 0;
            } else {
                types[2] = t3GeneList.size() - types[6] - types[4] - types[5];
            }
            //T2  -> - T123 - T12 - T23
            if (t2GeneList.size() == 0) {
                types[1] = 0;
            } else {
                types[1] = t2GeneList.size() - types[6] - types[3] - types[5];
            }
            //T1  -> - T123 - T12 - T13
            if (t1GeneList.size() == 0) {
                types[0] = 0;
            } else {
                types[0] = t1GeneList.size() - types[6] - types[3] - types[4];
            }
        }
        return types;
    }

    //simon version
    private List<Gene> findOverlapGenes(List<Gene> geneList1, List<Gene> geneList2) {
        List<Gene> overlapGenes = new ArrayList<Gene>();
        //genelist1 is empty. or  //genelist2 is empty.
        if ((geneList1 != null && geneList1.size() == 0) || (geneList2 != null && geneList2.size() == 0)) {
            return overlapGenes;
        }
        //genelist1 is not empty and genelist2 is not empty
        if (geneList1 != null && geneList1.size() > 0 && geneList2 != null && geneList2.size() > 0) {
            for (Gene g : geneList1) {
                if (geneList2.contains(g)) {
                    overlapGenes.add(g);
                }
            }
        }
        return overlapGenes;
    }

    //sam version
    //Convenience method for returning count of values overlapping from two lists.
    private List<Gene> overlapping(List<Gene> g1, List<Gene> g2) {
        ArrayList<Gene> overlapList = new ArrayList<Gene>();
        for (Gene g : g1) {
            if (g2.contains(g)) {
                overlapList.add(g);
            }
        }
        return overlapList;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Gene> searchChromosomeGeneList(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);
        List<Probe> probes = uniqueProbesPages.getPageResults();
        if (probes.size() > 0) {
            String chrHQL = "SELECT g  FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) ORDER BY g.chromosome";
            Query chrQuery = this.session().createQuery(chrHQL);
            chrQuery.setParameterList(("probes"), probes);
            List<Gene> chromosomeGeneList = chrQuery.list();
            return chromosomeGeneList;
        } else {
            return new ArrayList<Gene>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<List<Object[]>> searchOntology(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);

        List<Probe> probes = uniqueProbesPages.getPageResults();

        ArrayList<List<Object[]>> goHash = new ArrayList<List<Object[]>>();
        if (probes.size() > 0) {
            String species = searchBean.getSpecies();
            //Get Count of all genes in db (N)
            String geneTotalCountHQL = "SELECT COUNT(DISTINCT g) FROM Gene g";
            Query geneTotalCountQuery = this.session().createQuery(geneTotalCountHQL);
            Long totalGeneN = ((Long) geneTotalCountQuery.uniqueResult());
            //Get Count of all Human genes in db (Nh)
            String geneTotalHumanCountHQL = "SELECT COUNT(DISTINCT g) FROM Gene g INNER JOIN g.probe pbs WHERE pbs.species = 1";
            Query geneTotalHumanCountQuery = this.session().createQuery(geneTotalHumanCountHQL);
            Long totalGeneNh = ((Long) geneTotalHumanCountQuery.uniqueResult());
            //Get Count of all Mouse genes in db (Nm)
            String geneTotalMouseCountHQL = "SELECT COUNT(DISTINCT g) FROM Gene g INNER JOIN g.probe pbs WHERE pbs.species = 2";
            Query geneTotalMouseCountQuery = this.session().createQuery(geneTotalMouseCountHQL);
            Long totalGeneNm = ((Long) geneTotalMouseCountQuery.uniqueResult());

            if (StringUtils.contains(species, "Homo sapiens"))   totalGeneN = totalGeneNh;
            if (StringUtils.contains(species, "Mus musculus"))   totalGeneN = totalGeneNm;

            //Get Count of all genes searched (n)
            String geneCountHQL = "SELECT COUNT(DISTINCT g) FROM Gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes)";
            Query geneCountQuery = this.session().createQuery(geneCountHQL);
            geneCountQuery.setParameterList(("probes"), probes);
            Long searchedGenen = ((Long) geneCountQuery.uniqueResult());

            //Get Count of all GO domains (m)
            HashMap<String, Long> countHash = new HashMap<String, Long>();
            String goCellularTotalHQL = "SELECT o, COUNT(DISTINCT g) FROM  GeneOntology go INNER JOIN go.ontology o INNER JOIN go.gene g GROUP BY o";
            Query goCellularTotalQuery = this.session().createQuery(goCellularTotalHQL);
            List<Object[]> cellResult = goCellularTotalQuery.list();
            Iterator<Object[]> totCountItr = cellResult.iterator();
            while (totCountItr.hasNext()) {
                Object[] ontCountRes = totCountItr.next();
                countHash.put(((Ontology) (ontCountRes[0])).getGoTermAccession(), (Long) ontCountRes[1]);
            }
            //************************
            //Search Cellular
            //************************
            //Get the matching genes for each ontology (k)
            String goCellularHQL = "SELECT o, COUNT(DISTINCT g) FROM GeneOntology go INNER JOIN go.ontology o INNER JOIN o.goDomain gd INNER JOIN go.gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) AND gd.namespace = 'cellular_component' GROUP BY o.id ORDER BY COUNT(DISTINCT g) DESC";
            Query goCellularQuery = this.session().createQuery(goCellularHQL);
            goCellularQuery.setParameterList(("probes"), probes);
            List<Object[]> goCellularList = goCellularQuery.list();
            if (goCellularList.size() > 0) {
                goHash.add(addGOProbability(goCellularList, countHash, searchedGenen, totalGeneN));
            }

            //************************
            //Search Molecular
            //************************

            String goMolecularHQL = "SELECT o, COUNT(DISTINCT g) FROM GeneOntology go INNER JOIN go.ontology o INNER JOIN o.goDomain gd INNER JOIN go.gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) AND gd.namespace = 'molecular_function' GROUP BY o.id ORDER BY COUNT(DISTINCT g) DESC";
            Query goMolecularQuery = this.session().createQuery(goMolecularHQL);
            goMolecularQuery.setParameterList(("probes"), probes);
            List<Object[]> goMolecularList = goMolecularQuery.list();
            if (goMolecularList.size() > 0) {
                goHash.add(addGOProbability(goMolecularList, countHash, searchedGenen, totalGeneN));
            }

            //************************
            //Search Biological
            //************************

            String goBiologicalHQL = "SELECT o, COUNT(DISTINCT g) FROM GeneOntology go INNER JOIN go.ontology o INNER JOIN o.goDomain gd INNER JOIN go.gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes) AND gd.namespace = 'biological_process' GROUP BY o.id ORDER BY COUNT(DISTINCT g) DESC";
            Query goBiologicalQuery = this.session().createQuery(goBiologicalHQL);
            goBiologicalQuery.setParameterList(("probes"), probes);
            List<Object[]> goBiologicalList = goBiologicalQuery.list();
            if (goBiologicalList.size() > 0) {
                goHash.add(addGOProbability(goBiologicalList, countHash, searchedGenen, totalGeneN));
            }
            return goHash;
        } else {
            return new ArrayList<List<Object[]>>();
        }
    }

    /**
     * @param goList           The list of GO ids and associated count (Ontology, int) found for this search (k)
     * @param goCategoryCounts A HashMap of GO Accession Number with a total count (m)
     * @param geneSearched     Number of genes searched (n)
     * @param totalPopulation  Total number of genes in database (N)
     * @return
     */
    private List<Object[]> addGOProbability(List<Object[]> goList, HashMap<String, Long> goCategoryCounts, Long geneSearched, Long totalPopulation) {
        //Assume it is more efficient to get counts for all ontologies first and use this hash in probability calculations
        //This assumption has not been tested and may be incorrect
        //For each GO id found with this search (goList)
        ArrayList<Object[]> returnVal = new ArrayList<Object[]>();
        Iterator<Object[]> goItr = goList.iterator();
        while (goItr.hasNext()) {
            Object[] goVal = goItr.next();
            //System.out.println(((Ontology) (goVal[0])).getGoTermAccession());
            Long m = goCategoryCounts.get(((Ontology) (goVal[0])).getGoTermAccession());
            Double pvalue = null;
            if (m != null) {
                pvalue = calculateGOEnrichPValue(totalPopulation, geneSearched, m, (Long) goVal[1]);
            } else {
                pvalue = (double) 1;
            }
            returnVal.add(new Object[]{goVal[0], goVal[1], pvalue});
        }

        return returnVal;
    }


    private Double calculateGOEnrichPValue(long N, long n, long m, long k) {

        BigDecimal mk = binomialCoefficient(m,k);
        BigDecimal Nmnk = binomialCoefficient((N-m),(n-k));
        BigDecimal Nn = binomialCoefficient(N,n);
        BigDecimal mkXNmnk = mk.multiply(Nmnk);
        BigDecimal pval = mkXNmnk.divide(Nn, 15, RoundingMode.HALF_UP);
        Double pvalue =  pval.doubleValue();
        DecimalFormat df = new DecimalFormat("#.##E0");
        return (Double.valueOf(df.format(pvalue)));
    }

    private BigDecimal binomialCoefficient(long a, long b){
        if (b < 0 || b > a){
            return null;
        }
        if (b > (a - b))  {       // take advantage of symmetry
            b = a - b;
        }

        BigDecimal coeff = new BigDecimal(1.0);
        for (long i = a - b + 1; i <= a; i++) {

            coeff = coeff.multiply(new BigDecimal(i));
        }
        for (long i = 1; i <= b; i++) {
            coeff = coeff.divide(new BigDecimal(i));
        }
        return coeff;
    }




    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchTFSite(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        Pagination<Probe> uniqueProbesPages = searchProbes(searchBean, startPageNo, -1, orderBy, sortBy);

        List<Probe> probes = uniqueProbesPages.getPageResults();

        if (probes.size() > 0) {
            //Search
            String goTFHQL = "SELECT DISTINCT g, tf FROM TFSite tf INNER JOIN tf.gene g INNER JOIN g.probe pbs WHERE pbs.probeId IN (:probes)";
            Query goTFQuery = this.session().createQuery(goTFHQL);
            goTFQuery.setParameterList(("probes"), probes);
            List<Object[]> goTFList = goTFQuery.list();
            return goTFList;
        } else {
            return new ArrayList<Object[]>();
        }
    }


    @Override
    public Pagination<Probe> searchProbes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        boolean noneDsQuery = searchBean.isNoneDsCondition();
        //no dataset level search condition, then just search data level only, otherwise we will search probe based on both data and dataset conditions
        if (noneDsQuery) {
            return searchProbeDataLevel(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        } else {
            return searchProbeDataAndDsLevel(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        }
    }

    @SuppressWarnings("unchecked")
    private Pagination<SearchResultRow> searchDataWithDs(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        //query the dataset first
        List<Long> foundDsIds = queryDatasets(searchBean);

        //just return if no dataset found
        if (foundDsIds.size() == 0) {
            return new Pagination<SearchResultRow>(startPageNo, recordPerPage, 0);
        }
        //start to search data based on the found dataset list
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitByDelims(genes, ",", "\t", "\n");
        String[] searchGenBanks = MercUtil.splitByDelims(genBanks, ",", "\t", "\n");
        String[] searchEnsembls = MercUtil.splitByDelims(ensembls, ",", "\t", "\n");

        // boolean foldChangeUpProvided = searchBean.isUpProvided();
        double upValue = searchBean.getUpValue();
        //boolean foldChangeDownProvided = searchBean.isDownProvided();
        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        String dsIdQuery = " ds.id IN (:dsIDs";

        if (searchGenes.length > 0) {
            threeIdQuery.add(" g.geneName IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" g.genbankId IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" g.ensgAccession IN (:ensembls");
        }

        upDownQuery.add(" d.value >= " + upValue);

        upDownQuery.add(" d.value <= -" + downValue);

        //create all or conditions
        List<String> orConds = createOrCondsWithDs(threeIdQuery, upDownQuery, dsIdQuery);

        //count data query
        StringBuilder countQL = new StringBuilder();
        String countBaseHQL = "SELECT count(d) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds";
        countQL.append(countBaseHQL);

        //data pagination query
        StringBuilder dataQL = new StringBuilder();

        String dataBaseHQL = "SELECT  d, ds, p, g FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";

        dataQL.append(dataBaseHQL);

        if (orConds.size() > 0) {
            countQL.append(" WHERE");
            dataQL.append(" WHERE");
            int i = 0;
            for (String orClause : orConds) {
                countQL.append(" (").append(orClause).append(")");
                dataQL.append(" (").append(orClause).append(")");
                if (i < orConds.size() - 1) {
                    countQL.append(" OR");
                    dataQL.append(" OR");
                }
                i++;
            }
        }


        //create query
        String countQLStr = countQL.toString();
        Query dataCountQuery = this.session().createQuery(countQLStr);
        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            dataQL.append(orderByCond);
        }
        String dataQLStr = dataQL.toString();
        Query dataQuery = this.session().createQuery(dataQLStr);

        if (searchGenes.length > 0) {
            if (countQLStr.indexOf("(:geneNames)") != -1) {
                dataCountQuery.setParameterList("geneNames", searchGenes);
            }
            if (dataQLStr.indexOf("(:geneNames)") != -1) {
                dataQuery.setParameterList("geneNames", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames0)") != -1) {
                dataCountQuery.setParameterList("geneNames0", searchGenes);
            }
            if (dataQLStr.indexOf("(:geneNames0)") != -1) {
                dataQuery.setParameterList("geneNames0", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames1)") != -1) {
                dataCountQuery.setParameterList("geneNames1", searchGenes);
            }
            if (dataQLStr.indexOf("(:geneNames1)") != -1) {
                dataQuery.setParameterList("geneNames1", searchGenes);
            }
        }

        if (searchGenBanks.length > 0) {
            if (countQLStr.indexOf("(:genBanks)") != -1) {
                dataCountQuery.setParameterList("genBanks", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks)") != -1) {
                dataQuery.setParameterList("genBanks", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks0)") != -1) {
                dataCountQuery.setParameterList("genBanks0", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks0)") != -1) {
                dataQuery.setParameterList("genBanks0", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks1)") != -1) {
                dataCountQuery.setParameterList("genBanks1", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks1)") != -1) {
                dataQuery.setParameterList("genBanks1", searchGenBanks);
            }
        }

        if (searchEnsembls.length > 0) {
            if (countQLStr.indexOf("(:ensembls)") != -1) {
                dataCountQuery.setParameterList("ensembls", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls)") != -1) {
                dataQuery.setParameterList("ensembls", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls0)") != -1) {
                dataCountQuery.setParameterList("ensembls0", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls0)") != -1) {
                dataQuery.setParameterList("ensembls0", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls1)") != -1) {
                dataCountQuery.setParameterList("ensembls1", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls1)") != -1) {
                dataQuery.setParameterList("ensembls1", searchEnsembls);
            }
        }

        //set for dataset ids
        if (foundDsIds.size() > 0) {
            //set parameter list for dsIDS
            if (countQLStr.indexOf("(:dsIDs)") != -1) {
                dataCountQuery.setParameterList("dsIDs", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs)") != -1) {
                dataQuery.setParameterList("dsIDs", foundDsIds);
            }

            //set parameter list for dsIDS0
            if (countQLStr.indexOf("(:dsIDs0)") != -1) {
                dataCountQuery.setParameterList("dsIDs0", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs0)") != -1) {
                dataQuery.setParameterList("dsIDs0", foundDsIds);
            }

            //set parameter list for dsIDS1
            if (countQLStr.indexOf("(:dsIDs1)") != -1) {
                dataCountQuery.setParameterList("dsIDs1", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs1)") != -1) {
                dataQuery.setParameterList("dsIDs1", foundDsIds);
            }

            //set parameter list for dsIDS2
            if (countQLStr.indexOf("(:dsIDs2)") != -1) {
                dataCountQuery.setParameterList("dsIDs2", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs2)") != -1) {
                dataQuery.setParameterList("dsIDs2", foundDsIds);
            }

            //set parameter list for dsIDS3
            if (countQLStr.indexOf("(:dsIDs3)") != -1) {
                dataCountQuery.setParameterList("dsIDs3", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs3)") != -1) {
                dataQuery.setParameterList("dsIDs3", foundDsIds);
            }

            //set parameter list for dsIDS4
            if (countQLStr.indexOf("(:dsIDs4)") != -1) {
                dataCountQuery.setParameterList("dsIDs4", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs4)") != -1) {
                dataQuery.setParameterList("dsIDs4", foundDsIds);
            }

            //set parameter list for dsIDS5
            if (countQLStr.indexOf("(:dsIDs5)") != -1) {
                dataCountQuery.setParameterList("dsIDs5", foundDsIds);
            }
            if (dataQLStr.indexOf("(:dsIDs5)") != -1) {
                dataQuery.setParameterList("dsIDs5", foundDsIds);
            }
        }

        int total = ((Long) dataCountQuery.uniqueResult()).intValue();
        if (total == 0) {
            return new Pagination<SearchResultRow>(startPageNo, recordPerPage, total);
        }
        Pagination<SearchResultRow> dataPagination = new Pagination<SearchResultRow>(startPageNo, recordPerPage, total);
        dataQuery.setFirstResult(dataPagination.getFirstResult());
        dataQuery.setMaxResults(dataPagination.getSizePerPage());

        dataPagination.setPageResults(SearchResultRow.listFromQuery(dataQuery));
        return dataPagination;
    }

    private List<String> createOrCondsWithDs(List<String> threeIdQuery, List<String> upDownQuery, String dsIdQuery) {
        List<String> orConds = new ArrayList<String>();
        int sm = 0;
        if (threeIdQuery.size() > 0 && upDownQuery.size() > 0) {
            for (String idQuery : threeIdQuery) {
                int i = 0;
                for (String upDown : upDownQuery) {
                    StringBuilder query = new StringBuilder();
                    query.append(upDown).append(" AND").append(idQuery).append(i).append(")").append(" AND").append(dsIdQuery).append(sm).append(")");
                    orConds.add(query.toString());
                    i++;
                    sm++;
                }
            }
        }

        if (threeIdQuery.size() == 0 && upDownQuery.size() > 0) {
            for (String upDown : upDownQuery) {
                StringBuilder query = new StringBuilder();
                query.append(upDown).append(" AND").append(dsIdQuery).append(sm).append(")");
                orConds.add(query.toString());
                sm++;
            }
        }

        if (threeIdQuery.size() > 0 && upDownQuery.size() == 0) {
            for (String idQuery : threeIdQuery) {
                StringBuilder query = new StringBuilder();
                query.append(idQuery).append(")").append(" AND").append(dsIdQuery).append(sm).append(")");
                orConds.add(query.toString());
                sm++;
            }
        }

        if (threeIdQuery.size() == 0 && upDownQuery.size() == 0) {
            StringBuilder query = new StringBuilder();
            query.append(dsIdQuery).append(")");
            orConds.add(query.toString());
        }
        return orConds;
    }


    @SuppressWarnings("unchecked")
    private Pagination<SearchResultRow> searchDataOnly(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitByDelims(genes, ",", "\t", "\n");
        String[] searchGenBanks = MercUtil.splitByDelims(genBanks, ",", "\t", "\n");
        String[] searchEnsembls = MercUtil.splitByDelims(ensembls, ",", "\t", "\n");


        double upValue = searchBean.getUpValue();

        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        if (searchGenes.length > 0) {
            threeIdQuery.add(" g.geneName IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" g.genbankId IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" g.ensgAccession IN (:ensembls");
        }

        upDownQuery.add(" d.value >= " + upValue);

        upDownQuery.add(" d.value <= -" + downValue);


        //create all or conditions
        List<String> orConds = createOrCondsForDataOnly(threeIdQuery, upDownQuery);

        //count data query
        StringBuilder countQL = new StringBuilder();
        String countBase = "SELECT count(d) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds";
        countQL.append(countBase);
        //data pagination query
        StringBuilder dataQL = new StringBuilder();
        String pQueryBase = "SELECT  d, ds, p, g FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";
        dataQL.append(pQueryBase);

        if (orConds.size() > 0) {
            countQL.append(" WHERE");
            dataQL.append(" WHERE");
            int i = 0;
            for (String orClause : orConds) {
                countQL.append(" (").append(orClause).append(")");
                dataQL.append(" (").append(orClause).append(")");
                if (i < orConds.size() - 1) {
                    countQL.append(" OR");
                    dataQL.append(" OR");
                }
                i++;
            }
        }

        //create count query
        String countQLStr = countQL.toString();
        Query dataCountQuery = this.session().createQuery(countQLStr);

        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            dataQL.append(orderByCond);
        }
        //create pagination query
        String dataQLStr = dataQL.toString();
        Query dataQuery = this.session().createQuery(dataQLStr);

        if (searchGenes.length > 0) {
            if (countQLStr.indexOf("(:geneNames)") != -1) {
                dataCountQuery.setParameterList("geneNames", searchGenes);
            }
            if (dataQLStr.indexOf("(:geneNames)") != -1) {
                dataQuery.setParameterList("geneNames", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames0)") != -1) {
                dataCountQuery.setParameterList("geneNames0", searchGenes);
            }
            if (dataQLStr.indexOf("(:geneNames0)") != -1) {
                dataQuery.setParameterList("geneNames0", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames1)") != -1) {
                dataCountQuery.setParameterList("geneNames1", searchGenes);
            }

            if (dataQLStr.indexOf("(:geneNames1)") != -1) {
                dataQuery.setParameterList("geneNames1", searchGenes);
            }
        }

        if (searchGenBanks.length > 0) {
            if (countQLStr.indexOf("(:genBanks)") != -1) {
                dataCountQuery.setParameterList("genBanks", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks)") != -1) {
                dataQuery.setParameterList("genBanks", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks0)") != -1) {
                dataCountQuery.setParameterList("genBanks0", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks0)") != -1) {
                dataQuery.setParameterList("genBanks0", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks1)") != -1) {
                dataCountQuery.setParameterList("genBanks1", searchGenBanks);
            }
            if (dataQLStr.indexOf("(:genBanks1)") != -1) {
                dataQuery.setParameterList("genBanks1", searchGenBanks);
            }
        }

        if (searchEnsembls.length > 0) {
            if (countQLStr.indexOf("(:ensembls)") != -1) {
                dataCountQuery.setParameterList("ensembls", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls)") != -1) {
                dataQuery.setParameterList("ensembls", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls0)") != -1) {
                dataCountQuery.setParameterList("ensembls0", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls0)") != -1) {
                dataQuery.setParameterList("ensembls0", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls1)") != -1) {
                dataCountQuery.setParameterList("ensembls1", searchEnsembls);
            }
            if (dataQLStr.indexOf("(:ensembls1)") != -1) {
                dataQuery.setParameterList("ensembls1", searchEnsembls);
            }
        }

        int total = ((Long) dataCountQuery.uniqueResult()).intValue();
        if (total == 0) {
            return new Pagination<SearchResultRow>(startPageNo, recordPerPage, total);
        }
        Pagination<SearchResultRow> dataPagination = new Pagination<SearchResultRow>(startPageNo, recordPerPage, total);

        dataQuery.setFirstResult(dataPagination.getFirstResult());
        dataQuery.setMaxResults(dataPagination.getSizePerPage());
        dataPagination.setPageResults(SearchResultRow.listFromQuery(dataQuery));
        return dataPagination;
    }

    //Search Probe By Data level and Dataset Level conditions
    @SuppressWarnings("unchecked")
    private Pagination<Probe> searchProbeDataAndDsLevel(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        //query the dataset first
        List<Long> foundDsIds = queryDatasets(searchBean);

        //just return if no dataset found
        if (foundDsIds.size() == 0) {
            if (recordPerPage < 0) {
                recordPerPage = 10;
            }
            return new Pagination<Probe>(startPageNo, recordPerPage, 0);
        }
        //start to search data based on the found dataset list
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitByDelims(genes, ",", "\t", "\n");
        String[] searchGenBanks = MercUtil.splitByDelims(genBanks, ",", "\t", "\n");
        String[] searchEnsembls = MercUtil.splitByDelims(ensembls, ",", "\t", "\n");


        double upValue = searchBean.getUpValue();

        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        String dsIdQuery = " ds.id IN (:dsIDs";

        if (searchGenes.length > 0) {
            threeIdQuery.add(" g.geneName IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" g.genbankId IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" g.ensgAccession IN (:ensembls");
        }
        upDownQuery.add(" d.value >= " + upValue);
        upDownQuery.add(" d.value <= -" + downValue);

        //create all or conditions
        List<String> orConds = createOrCondsWithDs(threeIdQuery, upDownQuery, dsIdQuery);


        //count data query
        StringBuilder countQL = new StringBuilder();
        String countBaseHQL = "SELECT count(distinct p.probeId) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds";
        countQL.append(countBaseHQL);

        //probe pagination query
        StringBuilder probeQL = new StringBuilder();
        String probeBaseHQL = "SELECT distinct(p.probeId) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";
        probeQL.append(probeBaseHQL);

        if (orConds.size() > 0) {
            countQL.append(" WHERE");
            probeQL.append(" WHERE");
            int i = 0;
            for (String orClause : orConds) {
                countQL.append(" (").append(orClause).append(")");
                probeQL.append(" (").append(orClause).append(")");
                if (i < orConds.size() - 1) {
                    countQL.append(" OR");
                    probeQL.append(" OR");
                }
                i++;
            }
        }


        //create query
        String countQLStr = countQL.toString();
        Query probeCountQuery = this.session().createQuery(countQLStr);
        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            probeQL.append(orderByCond);
        }
        String probeQLStr = probeQL.toString();
        Query probeQuery = this.session().createQuery(probeQLStr);

        if (searchGenes.length > 0) {
            if (countQLStr.indexOf("(:geneNames)") != -1) {
                probeCountQuery.setParameterList("geneNames", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames)") != -1) {
                probeQuery.setParameterList("geneNames", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames0)") != -1) {
                probeCountQuery.setParameterList("geneNames0", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames0)") != -1) {
                probeQuery.setParameterList("geneNames0", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames1)") != -1) {
                probeCountQuery.setParameterList("geneNames1", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames1)") != -1) {
                probeQuery.setParameterList("geneNames1", searchGenes);
            }
        }

        if (searchGenBanks.length > 0) {
            if (countQLStr.indexOf("(:genBanks)") != -1) {
                probeCountQuery.setParameterList("genBanks", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks)") != -1) {
                probeQuery.setParameterList("genBanks", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks0)") != -1) {
                probeCountQuery.setParameterList("genBanks0", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks0)") != -1) {
                probeQuery.setParameterList("genBanks0", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks1)") != -1) {
                probeCountQuery.setParameterList("genBanks1", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks1)") != -1) {
                probeQuery.setParameterList("genBanks1", searchGenBanks);
            }
        }

        if (searchEnsembls.length > 0) {
            if (countQLStr.indexOf("(:ensembls)") != -1) {
                probeCountQuery.setParameterList("ensembls", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls)") != -1) {
                probeQuery.setParameterList("ensembls", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls0)") != -1) {
                probeCountQuery.setParameterList("ensembls0", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls0)") != -1) {
                probeQuery.setParameterList("ensembls0", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls1)") != -1) {
                probeCountQuery.setParameterList("ensembls1", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls1)") != -1) {
                probeQuery.setParameterList("ensembls1", searchEnsembls);
            }
        }

        //set for dataset ids
        if (foundDsIds.size() > 0) {
            //set parameter list for dsIDS
            if (countQLStr.indexOf("(:dsIDs)") != -1) {
                probeCountQuery.setParameterList("dsIDs", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs)") != -1) {
                probeQuery.setParameterList("dsIDs", foundDsIds);
            }

            //set parameter list for dsIDS0
            if (countQLStr.indexOf("(:dsIDs0)") != -1) {
                probeCountQuery.setParameterList("dsIDs0", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs0)") != -1) {
                probeQuery.setParameterList("dsIDs0", foundDsIds);
            }

            //set parameter list for dsIDS1
            if (countQLStr.indexOf("(:dsIDs1)") != -1) {
                probeCountQuery.setParameterList("dsIDs1", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs1)") != -1) {
                probeQuery.setParameterList("dsIDs1", foundDsIds);
            }

            //set parameter list for dsIDS2
            if (countQLStr.indexOf("(:dsIDs2)") != -1) {
                probeCountQuery.setParameterList("dsIDs2", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs2)") != -1) {
                probeQuery.setParameterList("dsIDs2", foundDsIds);
            }

            //set parameter list for dsIDS3
            if (countQLStr.indexOf("(:dsIDs3)") != -1) {
                probeCountQuery.setParameterList("dsIDs3", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs3)") != -1) {
                probeQuery.setParameterList("dsIDs3", foundDsIds);
            }

            //set parameter list for dsIDS4
            if (countQLStr.indexOf("(:dsIDs4)") != -1) {
                probeCountQuery.setParameterList("dsIDs4", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs4)") != -1) {
                probeQuery.setParameterList("dsIDs4", foundDsIds);
            }

            //set parameter list for dsIDS5
            if (countQLStr.indexOf("(:dsIDs5)") != -1) {
                probeCountQuery.setParameterList("dsIDs5", foundDsIds);
            }
            if (probeQLStr.indexOf("(:dsIDs5)") != -1) {
                probeQuery.setParameterList("dsIDs5", foundDsIds);
            }
        }

        int total = ((Long) probeCountQuery.uniqueResult()).intValue();
        if (total == 0) {

            if (recordPerPage < 0) {
                recordPerPage = 10;
            }
            return new Pagination<Probe>(startPageNo, recordPerPage, total);
        }

        if (recordPerPage < 0) {
            recordPerPage = total;
        }
        Pagination<Probe> probePagination = new Pagination<Probe>(startPageNo, recordPerPage, total);
        probeQuery.setFirstResult(probePagination.getFirstResult());
        probeQuery.setMaxResults(probePagination.getSizePerPage());
        List<Probe> probeList = probeQuery.list();
        probePagination.setPageResults(probeList);
        return probePagination;
    }

    //Search Probe By Data level conditions
    @SuppressWarnings("unchecked")
    private Pagination<Probe> searchProbeDataLevel(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitByDelims(genes, ",", "\t", "\n");
        String[] searchGenBanks = MercUtil.splitByDelims(genBanks, ",", "\t", "\n");
        String[] searchEnsembls = MercUtil.splitByDelims(ensembls, ",", "\t", "\n");

        double upValue = searchBean.getUpValue();

        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        if (searchGenes.length > 0) {
            threeIdQuery.add(" g.geneName IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" g.genbankId IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" g.ensgAccession IN (:ensembls");
        }

        upDownQuery.add(" d.value >= " + upValue);

        upDownQuery.add(" d.value <= -" + downValue);

        //create all or conditions
        List<String> orConds = createOrCondsForDataOnly(threeIdQuery, upDownQuery);

        //count report DISTINCT query
        StringBuilder countQL = new StringBuilder();
        String countBase = "SELECT count(distinct p.probeId) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds";
        countQL.append(countBase);

        //probe pagination query
        StringBuilder probeQL = new StringBuilder();
        String pQueryBase = "SELECT distinct(p.probeId) FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";
        probeQL.append(pQueryBase);

        if (orConds.size() > 0) {
            countQL.append(" WHERE");
            probeQL.append(" WHERE");
            int i = 0;
            for (String orClause : orConds) {
                countQL.append(" (").append(orClause).append(")");
                probeQL.append(" (").append(orClause).append(")");
                if (i < orConds.size() - 1) {
                    countQL.append(" OR");
                    probeQL.append(" OR");
                }
                i++;
            }
        }

        //create count query
        String countQLStr = countQL.toString();
        Query probeCountQuery = this.session().createQuery(countQLStr);

        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            probeQL.append(orderByCond);
        }
        //create pagination query
        String probeQLStr = probeQL.toString();
        Query probeQuery = this.session().createQuery(probeQLStr);

        if (searchGenes.length > 0) {
            if (countQLStr.indexOf("(:geneNames)") != -1) {
                probeCountQuery.setParameterList("geneNames", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames)") != -1) {
                probeQuery.setParameterList("geneNames", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames0)") != -1) {
                probeCountQuery.setParameterList("geneNames0", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames0)") != -1) {
                probeQuery.setParameterList("geneNames0", searchGenes);
            }

            if (countQLStr.indexOf("(:geneNames1)") != -1) {
                probeCountQuery.setParameterList("geneNames1", searchGenes);
            }
            if (probeQLStr.indexOf("(:geneNames1)") != -1) {
                probeQuery.setParameterList("geneNames1", searchGenes);
            }
        }

        if (searchGenBanks.length > 0) {
            if (countQLStr.indexOf("(:genBanks)") != -1) {
                probeCountQuery.setParameterList("genBanks", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks)") != -1) {
                probeQuery.setParameterList("genBanks", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks0)") != -1) {
                probeCountQuery.setParameterList("genBanks0", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks0)") != -1) {
                probeQuery.setParameterList("genBanks0", searchGenBanks);
            }

            if (countQLStr.indexOf("(:genBanks1)") != -1) {
                probeCountQuery.setParameterList("genBanks1", searchGenBanks);
            }
            if (probeQLStr.indexOf("(:genBanks1)") != -1) {
                probeQuery.setParameterList("genBanks1", searchGenBanks);
            }
        }

        if (searchEnsembls.length > 0) {
            if (countQLStr.indexOf("(:ensembls)") != -1) {
                probeCountQuery.setParameterList("ensembls", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls)") != -1) {
                probeQuery.setParameterList("ensembls", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls0)") != -1) {
                probeCountQuery.setParameterList("ensembls0", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls0)") != -1) {
                probeQuery.setParameterList("ensembls0", searchEnsembls);
            }

            if (countQLStr.indexOf("(:ensembls1)") != -1) {
                probeCountQuery.setParameterList("ensembls1", searchEnsembls);
            }
            if (probeQLStr.indexOf("(:ensembls1)") != -1) {
                probeQuery.setParameterList("ensembls1", searchEnsembls);
            }
        }

        int total = ((Long) probeCountQuery.uniqueResult()).intValue();
        if (total == 0) {
            if (recordPerPage < 0) {
                recordPerPage = 10;
            }
            return new Pagination<Probe>(startPageNo, recordPerPage, total);
        }
        //if the recordPerPage is less than zero, we say it will return all results in a single page.
        if (recordPerPage < 0) {
            recordPerPage = total;
        }
        Pagination<Probe> probPagination = new Pagination<Probe>(startPageNo, recordPerPage, total);
        probeQuery.setFirstResult(probPagination.getFirstResult());
        probeQuery.setMaxResults(probPagination.getSizePerPage());
        List<Probe> dataList = probeQuery.list();
        probPagination.setPageResults(dataList);
        return probPagination;
    }

    private List<String> createOrCondsForDataOnly(List<String> threeIdQuery, List<String> upDownQuery) {
        List<String> orConds = new ArrayList<String>();
        if (threeIdQuery.size() > 0 && upDownQuery.size() > 0) {
            for (String idQuery : threeIdQuery) {
                int i = 0;
                for (String upDown : upDownQuery) {
                    StringBuilder query = new StringBuilder();
                    query.append(upDown).append(" AND").append(idQuery).append(i).append(")");
                    orConds.add(query.toString());
                    i++;
                }
            }
        }

        if (threeIdQuery.size() == 0 && upDownQuery.size() > 0) {
            for (String upDown : upDownQuery) {
                StringBuilder query = new StringBuilder();
                query.append(upDown);
                orConds.add(query.toString());
            }
        }

        if (threeIdQuery.size() > 0 && upDownQuery.size() == 0) {
            for (String idQuery : threeIdQuery) {
                StringBuilder query = new StringBuilder();
                query.append(idQuery).append(")");
                orConds.add(query.toString());
            }
        }
        return orConds;
    }

    private String createOrderBy(String orderBy, String sortBy) {

        if (StringUtils.equalsIgnoreCase(orderBy, "dataset")) {
            return " ORDER BY ds.id " + sortBy;
        }
        if (StringUtils.equalsIgnoreCase(orderBy, "ifntype")) {
            return " ORDER BY ifnType.typeName " + sortBy;
        }
        if (StringUtils.equalsIgnoreCase(orderBy, "ttime")) {
            return " ORDER BY ds.treatmentTime " + sortBy;
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

    //create the factor search conditions
    private List<List<String>> genFactorParams(SearchBean searchBean) {
        List<List<String>> totalSearchFactors = new ArrayList<List<String>>();
        String system = searchBean.getSystem();
        String species = searchBean.getSpecies();
        List<String> organs = searchBean.getOrgans();
        List<String> cells = searchBean.getCells();
        List<String> cellLines = searchBean.getCellLines();
        for (String organ : organs) {
            for (String cell : cells) {
                for (String cellLine : cellLines) {
                    List<String> factorNames = new ArrayList<String>();
                    if (!StringUtils.equals(organ, "-1")) {
                        factorNames.add(organ);
                    }
                    if (!StringUtils.equals(cell, "-1")) {
                        factorNames.add(cell);
                    }
                    if (!StringUtils.equals(cellLine, "-1")) {
                        factorNames.add(cellLine);
                    }
                    if (!StringUtils.equals(system, "-1")) {
                        factorNames.add(system);
                    }
                    if (!StringUtils.equals(species, "-1")) {
                        factorNames.add(species);
                    }
                    if (factorNames.size() > 0) {
                        totalSearchFactors.add(factorNames);
                    }
                }
            }
        }
        return totalSearchFactors;
    }
}


