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
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.Data;
import edu.monash.merc.dto.RangeCondition;
import edu.monash.merc.dto.SearchBean;
import edu.monash.merc.dto.VariationCondtion;
import edu.monash.merc.repository.ISearchDataRepository;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchDataDAO class which provides searching functionality for Data domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
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
                // System.out.println("=============== ***** with factors: dataset hql string: " + factorHQL);

                Query findDsQuery = this.session().createQuery(factorHQL);

                findDsQuery.setParameterList(("factorvalues"), factors);
                findDsQuery.setInteger(("fv_count"), factors.size());
                List<Long> foundTmp = findDsQuery.list();
                //System.out.println("=============== ***** search with factors: dataset size: " + foundTmp.size());

                for (Long tmpdsid : foundTmp) {
                    if (!foundDsIds.contains(tmpdsid)) {
                        foundDsIds.add(tmpdsid);
                    }
                }
            }
        } else {
            // System.out.println("=============== ****** without factor dataset hql string: " + baseDatasetHql);
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
            if ((fromDose > 0) && (toDose > 0) && (fromDose > toDose)) {
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
            if ((fromTime > 0) && (toTime > 0) && (fromTime > toTime)) {
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

    @Override
    public Pagination<Data> search(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        boolean noneDsQuery = searchBean.isNoneDsCondition();
        //no dataset level search condition, then just search data only, otherwise we will search data based on dataset conditions
        if (noneDsQuery) {
            return searchDataOnly(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        } else {
            return searchDataWithDs(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
        }
    }


    @SuppressWarnings("unchecked")
    private Pagination<Data> searchDataWithDs(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        //query the dataset first
        List<Long> foundDsIds = queryDatasets(searchBean);
        // System.out.println("============> ***** found dataset id list size: " + foundDsIds.size());

        //just return if no dataset found
        if (foundDsIds.size() == 0) {
            return new Pagination<Data>(startPageNo, recordPerPage, 0);
        }
        //start to search data based on the found dataset list
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitStrByDelim(genes, ",");
        String[] searchGenBanks = MercUtil.splitStrByDelim(genBanks, ",");
        String[] searchEnsembls = MercUtil.splitStrByDelim(ensembls, ",");

        boolean foldChangeUpProvided = searchBean.isUpProvided();
        double upValue = searchBean.getUpValue();
        boolean foldChangeDownProvided = searchBean.isDownProvided();
        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        String dsIdQuery = " ds.id IN (:dsIDs";

        if (searchGenes.length > 0) {
            threeIdQuery.add(" rep.geneSymbol IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" rep.genBankAccession IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" rep.ensembl IN (:ensembls");
        }
        if (foldChangeUpProvided) {
            upDownQuery.add(" d.value >= " + upValue);
        }
        if (foldChangeDownProvided) {
            upDownQuery.add(" d.value <= -" + downValue);
        }
        //create all or conditions
        List<String> orConds = createOrCondsWithDs(threeIdQuery, upDownQuery, dsIdQuery);

        //count data query
        StringBuilder countQL = new StringBuilder();
        String countBaseHQL = "SELECT count(d) FROM Data d INNER JOIN d.reporter rep LEFT JOIN d.dataset ds";
        countQL.append(countBaseHQL);

        //data pagination query
        StringBuilder dataQL = new StringBuilder();
        String dataBaseHQL = "SELECT d FROM Data d INNER JOIN d.reporter rep LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";
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
        // System.out.println("=================================> ds level data cout ql string: " + countQLStr);
        Query dataCountQuery = this.session().createQuery(countQLStr);
        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            dataQL.append(orderByCond);
        }
        String dataQLStr = dataQL.toString();
        // System.out.println("=================================> ds level data query ql string: " + dataQLStr);
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
            return new Pagination<Data>(startPageNo, recordPerPage, total);
        }
        Pagination<Data> dataPagination = new Pagination<Data>(startPageNo, recordPerPage, total);
        dataQuery.setFirstResult(dataPagination.getFirstResult());
        dataQuery.setMaxResults(dataPagination.getSizePerPage());
        List<Data> dataList = dataQuery.list();
        dataPagination.setPageResults(dataList);
        // System.out.println("===========> ds level found total data size: " + dataPagination.getTotalRecords());
        //System.out.println("===========> ds level found total data pages: " + dataPagination.getTotalPages());
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
    private Pagination<Data> searchDataOnly(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        String genes = searchBean.getGenes();
        String genBanks = searchBean.getGenBanks();
        String ensembls = searchBean.getEnsembls();

        String[] searchGenes = MercUtil.splitStrByDelim(genes, ",");
        String[] searchGenBanks = MercUtil.splitStrByDelim(genBanks, ",");
        String[] searchEnsembls = MercUtil.splitStrByDelim(ensembls, ",");

        boolean foldChangeUpProvided = searchBean.isUpProvided();
        double upValue = searchBean.getUpValue();
        boolean foldChangeDownProvided = searchBean.isDownProvided();
        double downValue = searchBean.getDownValue();

        List<String> threeIdQuery = new ArrayList<String>();
        List<String> upDownQuery = new ArrayList<String>();
        if (searchGenes.length > 0) {
            threeIdQuery.add(" rep.geneSymbol IN (:geneNames");
        }
        if (searchGenBanks.length > 0) {
            threeIdQuery.add(" rep.genBankAccession IN (:genBanks");
        }
        if (searchEnsembls.length > 0) {
            threeIdQuery.add(" rep.ensembl IN (:ensembls");
        }
        if (foldChangeUpProvided) {
            upDownQuery.add(" d.value >= " + upValue);
        }
        if (foldChangeDownProvided) {
            upDownQuery.add(" d.value <= -" + downValue);
        }

        //create all or conditions
        List<String> orConds = createOrCondsForDataOnly(threeIdQuery, upDownQuery);

        //count data query
        StringBuilder countQL = new StringBuilder();
        String countBase = "SELECT count(d) FROM Data d INNER JOIN d.reporter rep LEFT JOIN d.dataset ds";
        countQL.append(countBase);
        //data pagination query
        StringBuilder dataQL = new StringBuilder();
        String pQueryBase = "SELECT d FROM Data d INNER JOIN d.reporter rep LEFT JOIN d.dataset ds JOIN ds.ifnType ifnType";
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
        //  System.out.println("=================================> data only count ql string: " + countQLStr);
        Query dataCountQuery = this.session().createQuery(countQLStr);

        String orderByCond = createOrderBy(orderBy, sortBy);
        if (StringUtils.isNotBlank(orderByCond)) {
            dataQL.append(orderByCond);
        }
        //create pagination query
        String dataQLStr = dataQL.toString();
        // System.out.println("=================================> data only query ql string: " + dataQLStr);
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
            return new Pagination<Data>(startPageNo, recordPerPage, total);
        }
        Pagination<Data> dataPagination = new Pagination<Data>(startPageNo, recordPerPage, total);
        dataQuery.setFirstResult(dataPagination.getFirstResult());
        dataQuery.setMaxResults(dataPagination.getSizePerPage());
        List<Data> dataList = dataQuery.list();
        dataPagination.setPageResults(dataList);
        // System.out.println("===========> data only query found total data size: " + dataPagination.getTotalRecords());
        // System.out.println("===========>  data only query found total data pages: " + dataPagination.getTotalPages());
        return dataPagination;
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

        if (StringUtils.equalsIgnoreCase(orderBy, "genesymbol")) {
            return " ORDER BY rep.geneSymbol " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "foldchange")) {
            return " ORDER BY d.value " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "genbank")) {
            return " ORDER BY rep.genBankAccession " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "ensemblid")) {
            return " ORDER BY rep.ensembl " + sortBy;
        }

        if (StringUtils.equalsIgnoreCase(orderBy, "probeid")) {
            return " ORDER BY rep.probeId " + sortBy;
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
