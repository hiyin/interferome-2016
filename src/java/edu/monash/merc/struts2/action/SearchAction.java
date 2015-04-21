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

package edu.monash.merc.struts2.action;

import au.com.bytecode.opencsv.CSVWriter;
import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.results.SearchResultRow;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.*;
import edu.monash.merc.dto.GeneExpressionRecord;
import edu.monash.merc.dto.RangeCondition;
import edu.monash.merc.dto.SearchBean;
import edu.monash.merc.dto.VariationCondtion;
import edu.monash.merc.exception.DCConfigException;
import edu.monash.merc.exception.DCException;
import edu.monash.merc.service.SearchDataService;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.*;


/**
 * SearchAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("search.searchAction")
public class SearchAction extends DMBaseAction {

    @Autowired
    private SearchDataService searchDataService;

    /**
     * pre-defined interferon type
     */
    private Map<String, String> ifnTypeMap = new LinkedHashMap<String, String>();

    /**
     * interferon sub type loaded from db
     */
    private Map<String, String> ifnSubTypeMap = new LinkedHashMap<String, String>();

    /**
     * pre-defined experimental conditions
     */
    private Map<String, String> vivoVitroMap = new LinkedHashMap<String, String>();


    /**
     * species map loaded from db
     */
    private Map<String, String> speciesMap = new LinkedHashMap<String, String>();

    /**
     * system map loaded from db
     */
    private Map<String, String> systemMap = new LinkedHashMap<String, String>();


    /**
     * organ maps loaded from db
     */
    private Map<String, String> organMap = new LinkedHashMap<String, String>();


    /**
     * cell maps loaded from db
     */
    private Map<String, String> cellMap = new LinkedHashMap<String, String>();


    /**
     * cell lines loaded from db
     */
    private Map<String, String> cellLineMap = new LinkedHashMap<String, String>();


    /**
     * pre-defined any or by range searching
     */
    private Map<String, String> anyOrRanges = new LinkedHashMap<String, String>();

    /**
     * pre-defined up or down condition for fold change
     */
    private Map<String, String> upDown = new LinkedHashMap<String, String>();

    /**
     * pre-defined normal or abnormal radio options
     */
    private Map<String, String> variationMap = new LinkedHashMap<String, String>();


    /**
     * abnormal variation values loaded from db
     */
    private Map<String, String> abnormalMap = new LinkedHashMap<String, String>();


    /**
     * search bean
     */
    private SearchBean searchBean;

    /**
     * check the search condition is loaded or not flag
     */
    private boolean conditionLoaded;

    /**
     * searched flag
     */
    private boolean searched;

    /**
     * search result dataset
     */
    private Pagination<SearchResultRow> dataPagination;

    private String viewDsAct;

    /**
     * genbank link
     */
    private String geneBankLink;

    /**
     * ensembl link
     */
    private String ensemblLink;

    /**
     * Entrez Id link
     */
    private String entrezIdLink;

    /**
     * Refseq Id link
     */
    private String refseqIdLink;

    /**
     * GO link
     */
    private String goLink;

    /**
     * pic file inputstream
     */
    private InputStream imageStream;

    // For searching result csv file exporting
    private String contentType;

    /**
     * cvs file inputstream
     */
    private InputStream csvInputStream;

    /**
     * csv file content disposition
     */
    private String contentDisposition;

    /**
     * csv file exporting buffer size
     */
    private int bufferSize;


    /**
     * csv file maximum records
     */
    private int maxRecords;

    /**
     * search result gene
     */
    private Pagination<Gene> genePagination;


    private Pagination<Probe> probePagination;

    /**
     * TFSite Search Results
     */

    private HashMap<String, List<TFSite>> tfSiteList;

    /**
     *
     */

    private List<List<Object[]>> ontologyList;

    /**
     *
     */

    private List<Object[]> chromosomeList;

    /**
     *
     */

    private List<Gene> chromosomeGeneList;

    /**
     *
     */

    private List<GeneExpressionRecord> humanGeneExpressionList;
    private List<GeneExpressionRecord> mouseGeneExpressionList;


//    private List<TissueExpression> tissueExprList;

    private Object[] subtypeList;

    /**
     * Search Type: 1. gene,  2. data, 3. geneOntology, 4. chromosome, 5. transcript, 6. subtype
     */
    private String searchType;

    protected Map<String, String> dataOrderByMap = new HashMap<String, String>();

    /**
     * Logger
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String SEMICOLON = ";";

    private static final String GENE_TYPE = "gene";

    private static final String DATA_TYPE = "data";

    private static final String GO_TYPE = "geneontology";

    private static final String CHROM_TYPE = "chromosome";

    private static final String TRANS_TYPE = "transcript";

    private static final String SUBTYPE_TYPE = "subtype";

    private static final String TISSUE_EXP_TYPE = "tissueexp";

    public void setSearchDataService(SearchDataService searchDataService) {
        this.searchDataService = searchDataService;
    }

    @PostConstruct
    public void initSearchConds() {
        // load the out site  resource links
        loadResourceLinks();

        //pre-define the interferon type
        populateINFTypes();

        //pre-define the vivo vitro type
        populateVivoVitros();

        // pre-define the any or by range
        populateAnyOrByRanges();

        //pre-define the normal or abnormal
        populateVariations();

        //pre-define the up or down option
        populateUpDown();

        //init pageSizeMap
        initPageSizeMap();

        //init OrderByMap
        initOrderByMap();

        try {
            //maximum search records to a csv file
            maxRecords = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            //load species
            loadSpecies();

            //load system
            loadSystem();

            //load organs, cell, cell lines map
            loadMutipleChoices();

            //load abnormal values
            loadAbnormals();
            //set condition loaded true
            conditionLoaded = true;
        } catch (Exception ex) {
            logger.error("Failed to load the pre-defined search condition, " + ex);
            conditionLoaded = false;
        }
    }

    private void loadResourceLinks() {
        this.ensemblLink = this.appSetting.getPropValue(AppPropSettings.ENSEMBL_SUMMARY_LINK);
        this.geneBankLink = this.appSetting.getPropValue(AppPropSettings.GENBANK_SUMMARY_LINK);
        this.entrezIdLink = this.appSetting.getPropValue(AppPropSettings.ENTREZ_ID_LINK);
        this.refseqIdLink = this.appSetting.getPropValue(AppPropSettings.REFSEQ_ID_LINK);
        this.goLink = this.appSetting.getPropValue(AppPropSettings.GO_LINK);
    }

    private void populateINFTypes() {
        ifnTypeMap.put("I", "I");
        ifnTypeMap.put("II", "II");
        ifnTypeMap.put("III", "III");
    }

    private void populateVivoVitros() {
        vivoVitroMap.put("In Vivo", "In Vivo");
        vivoVitroMap.put("In Vitro", "In Vitro");
    }

    //radio options: any or by range
    private void populateAnyOrByRanges() {
        anyOrRanges.put("any", "Any");
        anyOrRanges.put("byrange", "By Range");
    }

    //folder change up or down option
    private void populateUpDown() {
        upDown.put("up", "Up");
        upDown.put("down", "Down");
    }

    ///normal or abnormal radio option
    private void populateVariations() {
        variationMap.put("any", "Any");
        variationMap.put("Normal", "Normal");
        variationMap.put("Abnormal", "Abnormal");
    }

    private void loadSpecies() {
        List<String> speciesList = this.dmService.getFactorValuesByFactorName("Species");
        for (String species : speciesList) {
            speciesMap.put(species, species);
        }
    }

    private void loadSystem() {
        List<String> systemNames = this.dmService.getFactorValuesByFactorName("System");
        for (String system : systemNames) {
            systemMap.put(system, system);
        }
    }

    private void loadMutipleChoices() {
        //load the organs if any
        List<String> organs = this.dmService.getFactorValuesByFactorName("Organ");
        if (organs != null) {
            for (String organ : organs) {
                organMap.put(organ, organ);
            }
        }

        //load the cells if any
        List<String> cells = this.dmService.getFactorValuesByFactorName("Cell");
        if (cells != null) {
            for (String cell : cells) {
                cellMap.put(cell, cell);
            }
        }

        //load the cell lines if any
        List<String> celllines = this.dmService.getFactorValuesByFactorName("Cell Lines");
        if (celllines != null) {
            for (String cline : celllines) {
                cellLineMap.put(cline, cline);
            }
        }
    }

    private void loadAbnormals() {
        //load the abnormal variations if any
        List<String> abnormals = this.dmService.getAbnormalFactors();
        if (abnormals != null) {
            for (String abn : abnormals) {
                abnormalMap.put(abn, abn);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public String showSearch() {
        try {
            user = getCurrentUser();
            //remove the previous search conditions if any
            removeFromSession(ActionConts.SEARCH_CON_KEY);
            //create a search bean
            searchBean = new SearchBean();
            //set the default  values for multiple options
            setMultipleOptionsDefaultValues();
            //check the search conditions are loaded or not
            if (!conditionLoaded) {
                new DCConfigException("failed to load the search conditions");
            }
        } catch (Exception ex) {
            addActionError(getText("data.search.show.search.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    private void setMultipleOptionsDefaultValues() {
        searchBean.getOrgans().add("-1");
        searchBean.getCells().add("-1");
        searchBean.getCellLines().add("-1");
    }


    @SuppressWarnings("unchecked")
    public String searchData() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            //set the data pagination parameters
            setDataPageParams();
            //query the data by pagination
            dataPagination = this.searchDataService.search(searchBean, pageNo, pageSize, orderBy, orderByType);
            //set the searched flag as true
            searched = true;
            searchType = DATA_TYPE;

            //sub type post process
            subTypePostProcess();
            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.data.failed"));
            return ERROR;
        }
        return SUCCESS;
    }
    @SuppressWarnings("unchecked")
    public String searchGenes() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            //set gene pagination parameters
            setGenePageParams();

            //query the data by pagination
            genePagination = this.searchDataService.searchGenes(searchBean, pageNo, pageSize, orderBy, orderByType);

            //set the searched flag as true
            searched = true;
            searchType = GENE_TYPE;
            //sub type post process
            subTypePostProcess();
            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.genes.failed"));
            return ERROR;
        }
        return SUCCESS;
    }
    @SuppressWarnings("unchecked")
    public String searchOntology() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }

            //validation failed
            if (!validConds()) {
                return ERROR;
            }

            //query the data by pagination
            ontologyList = this.searchDataService.searchOntology(searchBean, pageNo, pageSize, orderBy, orderByType);

            //set the searched flag as true
            searched = true;
            searchType = GO_TYPE;
            //sub type post process

            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.ontology.failed"));
            return ERROR;
        }
        return SUCCESS;
    }
    @SuppressWarnings("unchecked")
    public String searchTFSite() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                return ERROR;
            }

            //query the data
            List<Object[]> results = this.searchDataService.searchTFSite(searchBean, pageNo, pageSize, orderBy, orderByType);

            //Convert results into Hash of object[x][4] with gene as hash and
            //start, end, core match, matrix match ad

            tfSiteList = new HashMap<String, List<TFSite>>();

            for (Object[] row : results) {
                String geneName = ((Gene) row[0]).getGeneName();
                if (tfSiteList.containsKey(geneName)) {
                    List<TFSite> existingResults = tfSiteList.get(geneName);
                    existingResults.add((TFSite) row[1]);
                } else {
                    List<TFSite> newTFSiteList = new ArrayList<TFSite>();
                    newTFSiteList.add((TFSite) row[1]);
                    tfSiteList.put(geneName, newTFSiteList);
                }
            }

            // System.out.println("TF Site Size: " + tfSiteList.size());

            //set the searched flag as true
            searched = true;
            searchType = TRANS_TYPE;
            //sub type post process

            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.tf.analysis.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public String searchChromosome() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                return ERROR;
            }

            //query the data by pagination
            chromosomeList = this.searchDataService.searchChromosome(searchBean, pageNo, pageSize, orderBy, orderByType);
            chromosomeGeneList = this.searchDataService.searchChromosomeGeneList(searchBean, pageNo, pageSize, orderBy, orderByType);


            //set the searched flag as true
            searched = true;
            searchType = CHROM_TYPE;
            //sub type post process

            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.chromosome.failed"));
            return ERROR;
        }
        return SUCCESS;
    }
    @SuppressWarnings("unchecked")
    public String searchTissueExpression() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                return ERROR;
            }
            List<GeneExpressionRecord> te = this.searchDataService.searchTissueExpression(searchBean, pageNo, pageSize, orderBy, orderByType);
            if (searchBean.getSpecies().equalsIgnoreCase("Homo sapiens")) {
                this.humanGeneExpressionList = combineByGeneAndProbe(te, "Human");
            } else if (searchBean.getSpecies().equalsIgnoreCase("Mus musculus")) {
                this.mouseGeneExpressionList = combineByGeneAndProbe(te, "Mouse");
            } else {
                this.humanGeneExpressionList = combineByGeneAndProbe(te, "Human");
                this.mouseGeneExpressionList = combineByGeneAndProbe(te, "Mouse");
            }
            //set the searched flag as true
            searched = true;
            searchType = TISSUE_EXP_TYPE;
            //sub type post process
            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            addActionError(getText("data.search.tissue.expression.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * GeneExpressionRecords are uniquely identified by their probe and gene.
     * <p/>
     * What we need is for each unique gene and probe, a list of the related tissue expressions and tissue names.
     * <p/>
     * Therefore, we use a HashMap to combine the tissue expression values and names from all our retrieved rows
     *
     * @param species Only use this species name
     * @return A list of gene expression records with their lists of matching tissues filled out
     */
    private ArrayList<GeneExpressionRecord> combineByGeneAndProbe(List<GeneExpressionRecord> te, String species) {
        ArrayList<GeneExpressionRecord> geneExpressionRecords = new ArrayList<GeneExpressionRecord>();
        Iterator<GeneExpressionRecord> i = te.iterator();
        HashMap<GeneExpressionRecord, GeneExpressionRecord> geneAndProbe = new HashMap<GeneExpressionRecord, GeneExpressionRecord>();

        while (i.hasNext()) {
            GeneExpressionRecord current = i.next();
            if (!current.getSpeciesName().equalsIgnoreCase(species)) {
                continue;
            }
            if (geneAndProbe.containsKey(current)) {
                GeneExpressionRecord stored = geneAndProbe.get(current);
                stored.addTissueExpression(current.getTissueExpression());
            } else {
                geneAndProbe.put(current, current);
            }
        }

        for (GeneExpressionRecord geneExpressionRecord : geneAndProbe.keySet()) {
            geneExpressionRecords.add(geneExpressionRecord);
        }

        return geneExpressionRecords;

    }


    @SuppressWarnings("unchecked")
    public String searchSubtypes() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                return ERROR;
            }

            //query the data by pagination
            //T1, T2, T3, T1T2, T1T3, T2T3, T1T2T3
            subtypeList = this.searchDataService.searchSubtypes(searchBean, pageNo, pageSize, orderBy, orderByType);

            //set the searched flag as true
            searched = true;
            searchType = SUBTYPE_TYPE;
            //sub type post process

            storeInSession(ActionConts.SEARCH_CON_KEY, searchBean);

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.subtype.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFile() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            dataPagination = this.searchDataService.search(searchBean, 1, maxRecords, orderBy, orderByType);
            System.out.println("dataPagination: " + dataPagination);
            this.csvInputStream = createCSVFile(searchBean, dataPagination);

            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_DataSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.data.csv.file.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileGene() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }

            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            genePagination = this.searchDataService.searchGenes(searchBean, 1, maxRecords, orderBy, orderByType);
            this.csvInputStream = createCSVFileGene(searchBean, genePagination);
            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_GeneSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.gene.csv.file.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileOntology() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            ontologyList = this.searchDataService.searchOntology(searchBean, 1, maxRecords, orderBy, orderByType);
            this.csvInputStream = createCSVFileOntology(searchBean, ontologyList);
            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_OntologySearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.ontology.csv.file.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileTFanalysis() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            List<Object[]> results = this.searchDataService.searchTFSite(searchBean, 1, maxRecords, orderBy, orderByType);
            //Convert results into Hash of object[x][4] with gene as hash and
            //start, end, core match, matrix match ad

            tfSiteList = new HashMap<String, List<TFSite>>();

            for (Object[] row : results) {
                String geneName = ((Gene) row[0]).getGeneName();
                if (tfSiteList.containsKey(geneName)) {
                    List<TFSite> existingResults = tfSiteList.get(geneName);
                    existingResults.add((TFSite) row[1]);
                } else {
                    List<TFSite> newTFSiteList = new ArrayList<TFSite>();
                    newTFSiteList.add((TFSite) row[1]);
                    tfSiteList.put(geneName, newTFSiteList);
                }
            }


            this.csvInputStream = createCSVFileTFanalysis(searchBean, tfSiteList);
            String FileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + FileName + "_TFanalysisSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.tf.analysis.csv.file.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileChromosome() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            chromosomeList = this.searchDataService.searchChromosome(searchBean, 1, maxRecords, orderBy, orderByType);
            chromosomeGeneList = this.searchDataService.searchChromosomeGeneList(searchBean, 1, maxRecords, orderBy, orderByType);

            this.csvInputStream = createCSVFileChromosome(searchBean, chromosomeGeneList, chromosomeList);
            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_ChromosomeSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.chromosome.csv.file.failed "));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileTypes() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            subtypeList = this.searchDataService.searchSubtypes(searchBean, 1, maxRecords, orderBy, orderByType);

            this.csvInputStream = createCSVFileSubtypes(searchBean, subtypeList);
            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_TypesSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.subtype.csv.file.failed "));
            return ERROR;
        }
        return SUCCESS;
    }

    public String exportCsvFileTissueExpression() {
        try {
            //get the logged in user if existed
            user = getCurrentUser();
            if (user != null) {
                viewDsAct = ActionConts.VIEW_DATASET_ACTION;
            } else {
                viewDsAct = ActionConts.VIEW_PUB_DATASET_ACTION;
            }
            //validation failed
            if (!validConds()) {
                //sub type post process
                subTypePostProcess();
                return ERROR;
            }
            int maxRecordLimit = Integer.valueOf(appSetting.getPropValue(AppPropSettings.SEARCH_RESULT_TO_CSV_MAX_RECORD));

            if (maxRecords == 0) {
                maxRecords = maxRecordLimit;
            }
            if (maxRecords > maxRecordLimit) {
                maxRecords = maxRecordLimit;
            }

            //query the data by pagination
            List<GeneExpressionRecord> te = this.searchDataService.searchTissueExpression(searchBean, 1, maxRecords, orderBy, orderByType);
            this.humanGeneExpressionList = combineByGeneAndProbe(te, "Human");
            this.mouseGeneExpressionList = combineByGeneAndProbe(te, "Mouse");
            this.csvInputStream = createCSVFileTissueExpression(searchBean, humanGeneExpressionList, mouseGeneExpressionList);
            String csvFileName = MercUtil.genCurrentTimestamp();

            this.contentDisposition = "attachment;filename=\"" + csvFileName + "_TissueExpressionSearchResults.txt" + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";

            //set the searched flag as true
            searched = true;
            //sub type post process
            subTypePostProcess();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("data.search.export.tissue.expression.csv.file.failed "));
            return ERROR;
        }
        return SUCCESS;
    }

    private boolean validConds() {
        //check the searchBean first
        if (searchBean == null) {
            searchBean = (SearchBean) findFromSession(ActionConts.SEARCH_CON_KEY);
            if (searchBean == null) {
                searchBean = new SearchBean();
            }
        }
        boolean hasError = false;
        //check at least one non-default conditon is provided
        boolean defaultSearch = searchBean.isDefaultSearchCondition();
        if (user == null && defaultSearch) {
            addFieldError("onecondreq", getText("data.search.at.least.one.condition.required"));
            hasError = true;
        }
        //check at least one of Gene list. Gene Bank list and Gene Ensembl Id list is provide
        boolean selectOneOfThree = searchBean.selectOneOfThreeList();

        if (user == null && !selectOneOfThree) {
            addFieldError("oneofthreecondreq", getText("data.search.at.least.one.of.three.conditions.required"));
            hasError = true;
        }
        //if not meet the above condition, we just return back immediately
        if (hasError) {
            return false;
        }

        //check individual conditions
        hasError = false;
        RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
        if (doseRangeCond.isRangeProvided()) {
            double fromDose = doseRangeCond.getFromValue();
            double toDose = doseRangeCond.getToValue();
            if (fromDose < 0) {
                addFieldError("fromDose", getText("data.search.invalid.dose.from.value"));
                hasError = true;
            }
            if (toDose < 0) {
                addFieldError("toDose", getText("data.search.invalid.dose.to.value"));
                hasError = true;
            }
            if (fromDose > 0 && toDose > 0 & (toDose < fromDose)) {
                addFieldError("toDose", getText("data.search.invalid.dose.range"));
                hasError = true;
            }
        }

        RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
        if (ttimeRange.isRangeProvided()) {
            double fromTime = ttimeRange.getFromValue();
            double toTime = ttimeRange.getToValue();
            if (fromTime < 0) {
                addFieldError("fromTime", getText("data.search.invalid.ttime.from.value"));
                hasError = true;
            }
            if (toTime < 0) {
                addFieldError("toTime", getText("data.search.invalid.ttime.to.value"));
                hasError = true;
            }
            if (fromTime > 0 && toTime > 0 & (toTime < fromTime)) {
                addFieldError("fromToTime", getText("data.search.invalid.ttime.range"));
                hasError = true;
            }
        }


        double upValue = searchBean.getUpValue();
        if (upValue < 1) {
            addFieldError("upValue", getText("data.search.invalid.foldchange.up.value"));
            hasError = true;
        }


        double downValue = searchBean.getDownValue();
        if (downValue < 1) {
            addFieldError("downValue", getText("data.search.invalid.foldchange.down.value"));
            hasError = true;
        }

        //if validation has an error, just return false
        if (hasError) {
            return false;
        }
        return true;
    }

    private InputStream createCSVFile(SearchBean searchBean, Pagination<SearchResultRow> dPagination) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            //wrtie total records
            csvWriter.writeNext(new String[]{"Found a total of " + dPagination.getTotalRecords() + " Data"});
            //write empty line
            csvWriter.writeNext(new String[]{""});
            //write a search results data column headers
            csvWriter.writeNext(new String[]{"Dataset ID", "Fold Change", "Inteferome Type", "Treatment Time", "Gene Name", "Description", "GenBank Accession", "Ensembl ID", "Probe ID"});

            List<SearchResultRow> dataList = dPagination.getPageResults();
            for (SearchResultRow searchResultRow : dataList) {
                //get dataset
                Dataset dataset = searchResultRow.dataset;
                long datasetId = dataset.getId();
                double foldChange = searchResultRow.data.getValue();
                String searchIfnType = dataset.getIfnType().getTypeName();
                double treatmentTime = dataset.getTreatmentTime();

                //get Probe /reporter
                Probe probe = searchResultRow.probe;
                String probeId = probe.getProbeId();

                Gene gene = searchResultRow.gene;
                String geneName = gene.getGeneName();
                String geneDesc = gene.getDescription();
                String genBankId = gene.getGenbankId();
                String ensemblId = gene.getEnsgAccession();
                csvWriter.writeNext(new String[]{String.valueOf(datasetId), String.valueOf(foldChange), searchIfnType, String.valueOf(treatmentTime), geneName, geneDesc, genBankId, ensemblId, probeId});
            }
            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileGene(SearchBean searchBean, Pagination<Gene> gPagination) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            //wrtie total records
            csvWriter.writeNext(new String[]{"Found a total of " + gPagination.getTotalRecords() + " Gene(s)"});
            //write empty line
            csvWriter.writeNext(new String[]{""});
            //write a search results data column headers
            csvWriter.writeNext(new String[]{"Ensembl Id", "Gene Name", "Description", "Entrez", "Genbank", "UniGene"});
            List<Gene> geneList = gPagination.getPageResults();
            for (Gene gene : geneList) {
                //get geneTable
                String geneName = gene.getGeneName();
                String description = gene.getDescription();
                String genbankId = gene.getGenbankId();
                String ensgAccession = gene.getEnsgAccession();
                String unigene = gene.getUnigene();
                String entrezId = gene.getEntrezId();
                //write the csv into OutputStream
                csvWriter.writeNext(new String[]{ensgAccession, geneName, description, entrezId, genbankId, unigene});
            }
            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileOntology(SearchBean searchBean, List<List<Object[]>> ontologyList) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            //create Go Functions (Cellular component, Molecular Function, Biological process)
            String[] goFunctions = new String[]{"Cellular component", "Molecular Function", "Biological process"};
            int it = 0;
            List<List<Object[]>> ontologyResult = ontologyList;
            for (List<Object[]> list1 : ontologyResult) {
                //write Go Functions (Cellular component, Molecular Function, Biological process)
                csvWriter.writeNext(new String[]{"Search GO " + goFunctions[it]});
                //write a search results data column headers
                csvWriter.writeNext(new String[]{"Accession", "Link", "Term Name", "Term Definition", "Gene Count", "p Value"});
                for (Object[] objarray : list1) {
                    Ontology ont1 = (Ontology) objarray[0];
                    //get String
                    String TermAccession = ont1.getGoTermAccession();
                    String TermName = ont1.getGoTermName();
                    String newDelimTermName = MercUtil.replaceAllDelimsByNewDelim(TermName, SEMICOLON, new String[]{",", "\t", "\n"});
                    String TermDefinition = ont1.getGoTermDefinition();
                    String newDelimTermDefinition = MercUtil.replaceAllDelimsByNewDelim(TermDefinition, SEMICOLON, new String[]{",", "\t", "\n"});
                    //get result
                    long gCount = (Long) objarray[1];
                    double pvalue = (Double) objarray[2];
                    this.goLink = this.appSetting.getPropValue(AppPropSettings.GO_LINK);
                    //write total records
                    csvWriter.writeNext(new String[]{TermAccession, goLink + TermAccession, newDelimTermName, newDelimTermDefinition, String.valueOf(gCount), String.valueOf(pvalue)});
                }
                //write empty line
                csvWriter.writeNext(new String[]{""});
                it++;
            }
            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileTFanalysis(SearchBean searchBean, HashMap<String, List<TFSite>> tfSiteList) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Name List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            csvWriter.writeNext(new String[]{"GeneName", "Site", "Core Match", "Matrix Match", "Start Site", "End Site"});
            Iterator<Map.Entry<String, List<TFSite>>> it = tfSiteList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<TFSite>> mapEntry = it.next();
                String tfgene = mapEntry.getKey();
                csvWriter.writeNext(new String[]{tfgene});
                List<TFSite> tfSites1 = mapEntry.getValue();
                for (TFSite tfSite2 : tfSites1) {
                    String site = tfSite2.getFactor();
                    double coreMatch = tfSite2.getCoreMatch();
                    double matrixMatch = tfSite2.getMatrixMatch();
                    int startSite = tfSite2.getStart();
                    int endSite = tfSite2.getEnd();
                    csvWriter.writeNext(new String[]{"", site, String.valueOf(coreMatch), String.valueOf(matrixMatch), String.valueOf(startSite),String.valueOf(endSite)});
                }
            }
            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileChromosome(SearchBean searchBean, List<Gene> chromosomeGeneList, List<Object[]> chromosomeList) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            if (StringUtils.equals(species, "-1")|| StringUtils.contains(species, "Homo sapiens")) {
                csvWriter.writeNext(new String[]{"Human Chromosomal Location"});
                csvWriter.writeNext(new String[]{"GeneName", "Chromosome", "Start Position", "End Position", "Ensembl Id"});
                //write data result
                List<String> finishedHGenes = new ArrayList<String>();
                for (Gene chr : chromosomeGeneList) {
                    String ensembl = chr.getEnsgAccession();
                    if (!finishedHGenes.contains(ensembl)) {
                        if (StringUtils.startsWith(ensembl, "ENSG")) {
                            //This is humman
                            String GeneName = chr.getGeneName();
                            String Chromosome = chr.getChromosome();
                            long StartPosition = chr.getStartPosition();
                            long EndPosition = chr.getEndPosition();
                            csvWriter.writeNext(new String[]{GeneName, Chromosome, String.valueOf(StartPosition), String.valueOf(EndPosition), ensembl});
                        }
                        finishedHGenes.add(ensembl);
                    }
                }

            }
            csvWriter.writeNext(new String[]{""});
                if (StringUtils.equals(species, "-1")|| StringUtils.contains(species, "Mus musculus")) {
                    csvWriter.writeNext(new String[]{"Mouse Chromosomal Location"});
                    csvWriter.writeNext(new String[]{"GeneName", "Chromosome", "Start Position", "End Position", "Ensembl Id"});
                    List<String> finishedMGenes = new ArrayList<String>();
                    for (Gene chr : chromosomeGeneList) {
                        String ensemblM = chr.getEnsgAccession();
                        if (!finishedMGenes.contains(ensemblM)) {
                            if (StringUtils.startsWith(ensemblM, "ENSMUSG")) {
                                //This is mouse
                                String GeneName = chr.getGeneName();
                                String Chromosome = chr.getChromosome();
                                long StartPosition = chr.getStartPosition();
                                long EndPosition = chr.getEndPosition();
                                csvWriter.writeNext(new String[]{GeneName, Chromosome, String.valueOf(StartPosition), String.valueOf(EndPosition), ensemblM});
                            }
                            finishedMGenes.add(ensemblM);
                        }
                    }
                }


            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileSubtypes(SearchBean searchBean, Object[] subtypeList) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            //write table of Subtypes
            int type1 = ((Integer) subtypeList[0]).intValue();
            int type2 = ((Integer) subtypeList[1]).intValue();
            int type3 = ((Integer) subtypeList[2]).intValue();
            int type12 = ((Integer) subtypeList[3]).intValue();
            int type13 = ((Integer) subtypeList[4]).intValue();
            int type23 = ((Integer) subtypeList[5]).intValue();
            int type123 = ((Integer) subtypeList[6]).intValue();

            //write a search results data subtypes table
            csvWriter.writeNext(new String[]{"Type I ", String.valueOf(type1)});
            csvWriter.writeNext(new String[]{"Type II ", String.valueOf(type2)});
            csvWriter.writeNext(new String[]{"Type III ", String.valueOf(type3)});
            csvWriter.writeNext(new String[]{"Type I+II ", String.valueOf(type12)});
            csvWriter.writeNext(new String[]{"Type I+III ", String.valueOf(type13)});
            csvWriter.writeNext(new String[]{"Type II+III ", String.valueOf(type23)});
            csvWriter.writeNext(new String[]{"Type I+II+III ", String.valueOf(type123)});
            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }

    }

    private InputStream createCSVFileTissueExpression(SearchBean searchBean, List<GeneExpressionRecord> humanGeneExpressionList, List<GeneExpressionRecord> mouseGeneExpressionList) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.NO_QUOTE_CHARACTER);
            //write the conditions
            csvWriter.writeNext(new String[]{"Search Conditions"});
            //write new empty line
            csvWriter.writeNext(new String[]{""});

            //interferome type
            String ifnType = searchBean.getIfnType();
            if (StringUtils.equals("-1", ifnType)) {
                ifnType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome Type", ifnType});

            //interferome sub-type
            String subType = searchBean.getIfnSubType();
            if (StringUtils.equals("-1", subType)) {
                subType = "Any";
            }
            csvWriter.writeNext(new String[]{"Interferome SubType", subType});

            //treatment concentration
            RangeCondition doseRangeCond = searchBean.getDoseRangeCondition();
            if (doseRangeCond.isRangeProvided()) {
                double fromDose = doseRangeCond.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Concentration From", String.valueOf(fromDose)});

                double toDose = doseRangeCond.getToValue();
                if (toDose > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Concentration To", String.valueOf(toDose)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Concentration", "Any"});
            }

            //treatment time
            RangeCondition ttimeRange = searchBean.getTimeRangeCondition();
            if (ttimeRange.isRangeProvided()) {
                double fromTime = ttimeRange.getFromValue();
                csvWriter.writeNext(new String[]{"Treatment Time From", String.valueOf(fromTime)});
                double toTime = ttimeRange.getToValue();
                if (toTime > 0) {
                    csvWriter.writeNext(new String[]{"Treatment Time To", String.valueOf(toTime)});
                }
            } else {
                csvWriter.writeNext(new String[]{"Treatment Time", "Any"});
            }

            //vivo vitro
            String vivoVitro = searchBean.getVivoVitro();
            if (StringUtils.equals(vivoVitro, "-1")) {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Vivo/Vitro", vivoVitro});
            }

            //sepcies
            String species = searchBean.getSpecies();
            if (StringUtils.equals(species, "-1")) {
                csvWriter.writeNext(new String[]{"Species", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Species", species});
            }

            //system
            String system = searchBean.getSystem();
            if (StringUtils.equals(system, "-1")) {
                csvWriter.writeNext(new String[]{"System", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"System", system});
            }

            //organ
            List<String> organs = searchBean.getOrgans();
            String organTemp = "";
            int i = 0;
            for (String organ : organs) {
                organTemp += organ;
                if (!StringUtils.equals(organ, "-1")) {
                    if (i < organs.size() - 1) {
                        organTemp += SEMICOLON;
                    }
                }
                i++;
            }
            if (StringUtils.equals("-1", organTemp)) {
                csvWriter.writeNext(new String[]{"Organ", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Organ", organTemp});
            }

            //cell
            List<String> cells = searchBean.getCells();
            String cellTemp = "";
            int j = 0;
            for (String cell : cells) {
                cellTemp += cell;
                if (!StringUtils.equals(cell, "-1")) {
                    if (j < cells.size() - 1) {
                        cellTemp += SEMICOLON;
                    }
                }
                j++;
            }
            if (StringUtils.equals("-1", cellTemp)) {
                csvWriter.writeNext(new String[]{"Cell", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell", cellTemp});
            }

            //cellLine
            List<String> cellLines = searchBean.getCellLines();
            String cellLineTemp = "";
            int k = 0;
            for (String cline : cellLines) {
                cellLineTemp += cline;
                if (StringUtils.equals(cline, "-1")) {
                    if (k < cellLines.size() - 1) {
                        cellLineTemp += SEMICOLON;
                    }
                }
                k++;
            }
            if (StringUtils.equals("-1", cellLineTemp)) {
                csvWriter.writeNext(new String[]{"Cell Line", "Any"});
            } else {
                csvWriter.writeNext(new String[]{"Cell Line", cellLineTemp});
            }

            //normal or abnormal or any variations
            VariationCondtion variationCondtion = searchBean.getVariationCondtion();
            if (!variationCondtion.isVarProvided()) {
                csvWriter.writeNext(new String[]{"Normal/Abnormal", "Any"});
            } else {
                //if it's abnormal, we need to set it as abnormal and a value for abnormal
                if (variationCondtion.isAbnormal()) {
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Abnormal"});
                    csvWriter.writeNext(new String[]{"Abnormal", variationCondtion.getVarValue()});
                } else { //just set it as normal
                    csvWriter.writeNext(new String[]{"Normal/Abnormal", "Normal"});
                }
            }

            //fold changes
            double upValue = searchBean.getUpValue();
            csvWriter.writeNext(new String[]{"Fold Change Up", String.valueOf(upValue)});

            double downValue = searchBean.getDownValue();
            csvWriter.writeNext(new String[]{"Fold Change Down", String.valueOf(downValue)});

            //gene symbol ids
            String genes = searchBean.getGenes();
            if (StringUtils.isNotBlank(genes)) {
                String newDelimGenes = MercUtil.replaceAllDelimsByNewDelim(genes, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Gene Symbol List", newDelimGenes});
            }
            //gen bank ids
            String genBanks = searchBean.getGenBanks();
            if (StringUtils.isNotBlank(genBanks)) {
                String newDelimGenBanks = MercUtil.replaceAllDelimsByNewDelim(genBanks, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"GenBank Accession List", newDelimGenBanks});
            }

            //ensembl ids
            String ensembls = searchBean.getEnsembls();
            if (StringUtils.isNotBlank(ensembls)) {
                String newDelimEnsembls = MercUtil.replaceAllDelimsByNewDelim(ensembls, SEMICOLON, new String[]{",", "\t", "\n"});
                csvWriter.writeNext(new String[]{"Ensembl Id List", newDelimEnsembls});
            }
            //write new empty line
            csvWriter.writeNext(new String[]{""});
            if (StringUtils.equals(species, "-1") || StringUtils.contains(species, "Homo sapiens")) {
                csvWriter.writeNext(new String[]{"Human Expression in Unstimulated Tissues"});
                if (!humanGeneExpressionList.isEmpty()) {
                    //write data result
                    List<String> hmheads = new ArrayList<String>();
                    hmheads.add("Gene Name");
                    hmheads.add("Probe Id");
                    //will be a head of csv file
                    List<TissueExpression> hmHeades = humanGeneExpressionList.get(0).getTissueExpressionList();

                    for (TissueExpression hmheadesList : hmHeades) {
                        Tissue hmtissue = hmheadesList.getTissue();
                        String hmtissueVal = hmtissue.getTissueId();
                        hmheads.add(hmtissueVal);
                    }
                    csvWriter.writeNext(hmheads.toArray(new String[hmheads.size()]));
                    //for individual row in csv file
                    for (GeneExpressionRecord hgerecord : humanGeneExpressionList) {
                        List<String> hrowvalues = new ArrayList<String>();
                        // add the first two columns - probe and gene id
                        hrowvalues.add(hgerecord.getGeneName());
                        hrowvalues.add(hgerecord.getProbe().getProbeId());
                        // add the list of expressions
                        List<TissueExpression> htissueExpressions = hgerecord.getTissueExpressionList();
                        for (TissueExpression htissueExpression : htissueExpressions) {
                            double hmexpression = htissueExpression.getExpression();
                            hrowvalues.add(String.valueOf(hmexpression));
                        }
                        csvWriter.writeNext(hrowvalues.toArray(new String[hrowvalues.size()]));
                    }
                }

            }
            csvWriter.writeNext(new String[]{""});
                if (StringUtils.equals(species, "-1") ||StringUtils.contains(species, "Mus musculus")) {
                    csvWriter.writeNext(new String[]{"Mouse Expression in Unstimulated Tissues"});
                    //write data result
                    List<String> mmheads = new ArrayList<String>();
                    mmheads.add("Gene Name");
                    mmheads.add("Probe Id");
                    //will be a head of csv file
                    List<TissueExpression> mmHeades = mouseGeneExpressionList.get(0).getTissueExpressionList();

                    for (TissueExpression headesList : mmHeades) {
                        Tissue tissue = headesList.getTissue();
                        String tissueVal = tissue.getTissueId();
                        mmheads.add(tissueVal);
                    }
                    csvWriter.writeNext(mmheads.toArray(new String[mmheads.size()]));
                    //for individual row in csv file
                    for (GeneExpressionRecord mgerecord : mouseGeneExpressionList) {
                        List<String> mrowvalues = new ArrayList<String>();
                        // add the first two columns - probe and gene id
                        mrowvalues.add(mgerecord.getGeneName());
                        mrowvalues.add(mgerecord.getProbe().getProbeId());
                        // add the list of expressions
                        List<TissueExpression> mtissueExpressions = mgerecord.getTissueExpressionList();
                        for (TissueExpression mtissueExpression : mtissueExpressions) {
                            double expression = mtissueExpression.getExpression();
                            mrowvalues.add(String.valueOf(expression));
                        }
                        csvWriter.writeNext(mrowvalues.toArray(new String[mrowvalues.size()]));
                    }
                }

            //flush out
            csvWriter.flush();
            this.csvInputStream = new ByteArrayInputStream(csvOutputStream.toByteArray());
            return this.csvInputStream;
        } catch (Exception ex) {
            throw new DCException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                    cex.printStackTrace();
                }
            }
        }

    }


    private void subTypePostProcess() {
        //Load the interferon subtypes based on the type selected
        //and if the type is not all value -1, then loaded the subtype again
        if (searchBean.getIfnType() != "-1") {
            List<IFNType> ifnTypes = this.dmService.getIfnTypes(searchBean.getIfnType());
            if (ifnTypes != null) {
                for (IFNType ifnType : ifnTypes) {
                    ifnSubTypeMap.put(ifnType.getSubTypeName(), ifnType.getSubTypeName());
                }
            }
        }
    }

    protected void initPageSizeMap() {
        // page size per page values
        pageSizeMap.put(20, 20);
        pageSizeMap.put(30, 30);
        pageSizeMap.put(50, 50);
        pageSizeMap.put(100, 100);
        pageSizeMap.put(150, 150);
        pageSizeMap.put(200, 200);
    }

    protected void initOrderByMap() {
        // orderby type values
        orderByTypeMap.put("ASC", "asc");
        orderByTypeMap.put("DESC", "desc");
    }

    //initialize the pagination parameters
    protected void initDataPagination() {
        // orderby values
        orderByMap.clear();
        orderByMap.put("dataset", "Dataset");
        orderByMap.put("foldchange", "Fold Change");
        orderByMap.put("ifntype", "Interferon Type");
        orderByMap.put("ttime", "Treatment Time");
        orderByMap.put("geneName", "Gene Symbol");
        orderByMap.put("genbank", "GenBank");
        orderByMap.put("ensemblid", "Ensembl Id");
        orderByMap.put("probeid", "Probe Id");
    }

    //initialize the pagination parameters
    protected void initGenePagination() {
        // orderby values
        orderByMap.clear();
        orderByMap.put("geneName", "Gene Symbol");
        orderByMap.put("ensgAccession", "Ensembl Id");
    }


    //set the default pagination parameters
    protected void setDataPageParams() {
        initDataPagination();
        pageLink = "search/searchData.jspx";
        pageSuffix = ActionConts.PAGINATION_SUFFUX;
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "geneName";
        }
        if (StringUtils.isBlank(orderByType)) {
            orderByType = ActionConts.ASC_SORT_TYPE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = 30;
        }
    }

    //set the default pagination parameters
    protected void setGenePageParams() {
        initGenePagination();
        pageLink = "search/searchGene.jspx";
        pageSuffix = ActionConts.PAGINATION_SUFFUX;
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "geneName";
        }
        if (StringUtils.isBlank(orderByType)) {
            orderByType = ActionConts.ASC_SORT_TYPE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = 30;
        }
    }

    /**
     * Ajax call for finding the interferon subtype based on which interferon type is selected.
     *
     * @return a success string if succeed
     */
    public String findSubType() {
        try {
            List<IFNType> ifnTypes = this.dmService.getIfnTypes(searchBean.getIfnType());
            if (ifnTypes != null) {
                for (IFNType ifnType : ifnTypes) {
                    ifnSubTypeMap.put(ifnType.getSubTypeName(), ifnType.getSubTypeName());
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    public Map<String, String> getIfnTypeMap() {
        return ifnTypeMap;
    }

    public void setIfnTypeMap(Map<String, String> ifnTypeMap) {
        this.ifnTypeMap = ifnTypeMap;
    }

    public Map<String, String> getIfnSubTypeMap() {
        return ifnSubTypeMap;
    }

    public void setIfnSubTypeMap(Map<String, String> ifnSubTypeMap) {
        this.ifnSubTypeMap = ifnSubTypeMap;
    }

    public Map<String, String> getVivoVitroMap() {
        return vivoVitroMap;
    }

    public void setVivoVitroMap(Map<String, String> vivoVitroMap) {
        this.vivoVitroMap = vivoVitroMap;
    }

    public Map<String, String> getSpeciesMap() {
        return speciesMap;
    }

    public void setSpeciesMap(Map<String, String> speciesMap) {
        this.speciesMap = speciesMap;
    }

    public Map<String, String> getSystemMap() {
        return systemMap;
    }

    public void setSystemMap(Map<String, String> systemMap) {
        this.systemMap = systemMap;
    }

    public Map<String, String> getOrganMap() {
        return organMap;
    }

    public void setOrganMap(Map<String, String> organMap) {
        this.organMap = organMap;
    }

    public Map<String, String> getCellMap() {
        return cellMap;
    }

    public void setCellMap(Map<String, String> cellMap) {
        this.cellMap = cellMap;
    }

    public Map<String, String> getCellLineMap() {
        return cellLineMap;
    }

    public void setCellLineMap(Map<String, String> cellLineMap) {
        this.cellLineMap = cellLineMap;
    }

    public Map<String, String> getAnyOrRanges() {
        return anyOrRanges;
    }

    public void setAnyOrRanges(Map<String, String> anyOrRanges) {
        this.anyOrRanges = anyOrRanges;
    }

    public Map<String, String> getUpDown() {
        return upDown;
    }

    public void setUpDown(Map<String, String> upDown) {
        this.upDown = upDown;
    }

    public Map<String, String> getVariationMap() {
        return variationMap;
    }

    public void setVariationMap(Map<String, String> variationMap) {
        this.variationMap = variationMap;
    }

    public Map<String, String> getAbnormalMap() {
        return abnormalMap;
    }

    public void setAbnormalMap(Map<String, String> abnormalMap) {
        this.abnormalMap = abnormalMap;
    }

    public SearchBean getSearchBean() {
        return searchBean;
    }

    public void setSearchBean(SearchBean searchBean) {
        this.searchBean = searchBean;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public Pagination<SearchResultRow> getDataPagination() {
        return dataPagination;
    }

    public void setDataPagination(Pagination<SearchResultRow> dataPagination) {
        this.dataPagination = dataPagination;
    }

    public String getViewDsAct() {
        return viewDsAct;
    }

    public void setViewDsAct(String viewDsAct) {
        this.viewDsAct = viewDsAct;
    }

    public boolean isConditionLoaded() {
        return conditionLoaded;
    }

    public void setConditionLoaded(boolean conditionLoaded) {
        this.conditionLoaded = conditionLoaded;
    }

    public String getGeneBankLink() {
        return geneBankLink;
    }

    public void setGeneBankLink(String geneBankLink) {
        this.geneBankLink = geneBankLink;
    }

    public String getEnsemblLink() {
        return ensemblLink;
    }

    public void setEnsemblLink(String ensemblLink) {
        this.ensemblLink = ensemblLink;
    }

    public String getEntrezIdLink() {
        return entrezIdLink;
    }

    public void setEntrezIdLink(String entrezIdLink) {
        this.entrezIdLink = entrezIdLink;
    }

    public String getRefseqIdLink() {
        return refseqIdLink;
    }

    public void setRefseqIdLink(String refseqIdLink) {
        this.refseqIdLink = refseqIdLink;
    }

    public String getGoLink() {
        return goLink;
    }

    public void setGoLink(String goLink) {
        this.goLink = goLink;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getCsvInputStream() {
        return csvInputStream;
    }

    public void setCsvInputStream(InputStream csvInputStream) {
        this.csvInputStream = csvInputStream;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getMaxRecords() {
        return maxRecords;
    }

    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Map<String, String> getDataOrderByMap() {
        return dataOrderByMap;
    }

    public void setDataOrderByMap(Map<String, String> dataOrderByMap) {
        this.dataOrderByMap = dataOrderByMap;
    }

    public Pagination<Probe> getProbePagination() {
        return probePagination;
    }

    public void setProbePagination(Pagination<Probe> probePagination) {
        this.probePagination = probePagination;
    }

    public Pagination<Gene> getGenePagination() {
        return genePagination;
    }

    public void setGenePagination(Pagination<Gene> genePagination) {
        this.genePagination = genePagination;
    }

    public List<List<Object[]>> getOntologyList() {
        return ontologyList;
    }

    public void setOntologyList(List<List<Object[]>> ontologyList) {
        this.ontologyList = ontologyList;
    }

    public HashMap<String, List<TFSite>> getTfSiteList() {
        return tfSiteList;
    }

    public void setTfSiteList(HashMap<String, List<TFSite>> tfSiteList) {
        this.tfSiteList = tfSiteList;
    }

    public List<Object[]> getChromosomeList() {
        return chromosomeList;
    }

    public void setChromosomeList(List<Object[]> chromosomeList) {
        this.chromosomeList = chromosomeList;
    }

    public List<Gene> getChromosomeGeneList() {
        return chromosomeGeneList;
    }

    public void setChromosomeGeneList(List<Gene> chromosomeGeneList) {
        this.chromosomeGeneList = chromosomeGeneList;
    }

    public Object[] getSubtypeList() {
        return subtypeList;
    }

    public void setSubtypeList(Object[] subtypeList) {
        this.subtypeList = subtypeList;
    }

    public List<GeneExpressionRecord> getHumanGeneExpressionList() {
        return humanGeneExpressionList;
    }

    public List<GeneExpressionRecord> getMouseGeneExpressionList() {
        return mouseGeneExpressionList;
    }

    public void setHumanGeneExpressionList(List<GeneExpressionRecord> tissueExpressioList) {
        this.humanGeneExpressionList = tissueExpressioList;
    }

    public void setMouseGeneExpressionList(List<GeneExpressionRecord> tissueExpressioList) {
        this.mouseGeneExpressionList = tissueExpressioList;
    }
}
