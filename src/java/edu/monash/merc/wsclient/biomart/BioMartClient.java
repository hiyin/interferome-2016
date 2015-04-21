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

package edu.monash.merc.wsclient.biomart;

import au.com.bytecode.opencsv.CSVReader;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.dto.GeneOntologyBean;
import edu.monash.merc.dto.ProbeGeneBean;
import edu.monash.merc.exception.WSException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 10:21 AM
 */
public class BioMartClient {

    private String wsUrl = "http://www.biomart.org/biomart/martservice/result?query=";

    private String species;

    //only used by probe ws call
    private String platform;

    private boolean configured;

    private GetMethod httpget = null;

    private static final String GENE_TYPE = "gene";

    private static final String GENE_ONTOLOGY_TYPE = "geneOntology";


    public boolean configure() {
        if (StringUtils.isNotBlank(wsUrl) && StringUtils.isNotBlank(species)) {
            configured = true;
            return configured;
        } else {
            return configured;
        }
    }

    public boolean configure(String wsUrl, String species, String platform) {
        this.wsUrl = wsUrl;
        this.species = species;
        if (StringUtils.isNotBlank(platform)) {
            this.platform = platform;
        }
        this.configured = true;
        return configured;
    }

    private InputStream getWsResponse(String type) {
        try {
            HttpClient httpclient = new HttpClient();
            String query = null;

            if (StringUtils.equals(GENE_TYPE, type)) {
                query = geneQueryString(this.species);
            }
            if (StringUtils.equals(GENE_ONTOLOGY_TYPE, type)) {
                query = geneOntologyQueryString(this.species);
            }
            if (StringUtils.isBlank(query)) {
                throw new WSException("The query string is null");
            }
            String url = wsUrl + URLEncoder.encode(query, "UTF-8");
            System.out.println("-- url : " + url);
            httpget = new GetMethod(url);

            int statusCode = httpclient.executeMethod(httpget);
            if (statusCode != HttpStatus.SC_OK) {
                throw new WSException("failed to get the gene information");
            } else {
                return httpget.getResponseBodyAsStream();
            }
        } catch (Exception ex) {
            throw new WSException(ex);
        }
    }


    private InputStream getWSProbeResponse(String platform) {
        try {
            HttpClient httpclient = new HttpClient();

            String query = probeQueryString(this.species, this.platform);

            if (StringUtils.isBlank(query)) {
                throw new WSException("The query string is null");
            }
            String url = wsUrl + URLEncoder.encode(query, "UTF-8");
            System.out.println("-- url : " + url);
            httpget = new GetMethod(url);

            int statusCode = httpclient.executeMethod(httpget);
            if (statusCode != HttpStatus.SC_OK) {
                throw new WSException("failed to get the gene information");
            } else {
                return httpget.getResponseBodyAsStream();
            }
        } catch (Exception ex) {
            throw new WSException(ex);
        }
    }

    public List<Gene> importGenes() {
        if (!configured) {
            throw new WSException("The configure method must be called first.");
        }

        CSVReader csvReader = null;
        List<Gene> genes = new ArrayList<Gene>();
        try {
            InputStream responseIns = getWsResponse(GENE_TYPE);
            csvReader = new CSVReader(new InputStreamReader(responseIns));
            String[] columnsLines = csvReader.readNext();

            String[] columnValuesLines;
            while ((columnValuesLines = csvReader.readNext()) != null) {
                CSVGeneCreator geneCreator = new CSVGeneCreator();
                for (int i = 0; i < columnsLines.length; i++) {
                    geneCreator.getColumns().add(new CSVColumn(columnsLines[i], columnValuesLines[i]));
                }

                Gene gene = geneCreator.createGene();
                if (StringUtils.isNotBlank(gene.getEnsgAccession())) {
                    genes.add(gene);
                }
            }

            //TODO:
            //Check the gene list size, if size is zero,
            //then check the response whether it contains Query ERROR:
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
            } catch (Exception x) {
                //ignore whatever caught
            }
        }
        return genes;
    }

    public List<GeneOntologyBean> importGeneOntology() {
        if (!configured) {
            throw new WSException("The configure method must be called first.");
        }

        CSVReader csvReader = null;
        List<GeneOntologyBean> geneOntologyBeans = new ArrayList<GeneOntologyBean>();
        try {
            InputStream responseIns = getWsResponse(GENE_ONTOLOGY_TYPE);
            csvReader = new CSVReader(new InputStreamReader(responseIns));
            String[] columnsLines = csvReader.readNext();

            String[] columnValuesLines;
            while ((columnValuesLines = csvReader.readNext()) != null) {
                GeneOntologyCreator geneOntologyCreator = new GeneOntologyCreator();
                for (int i = 0; i < columnsLines.length; i++) {
                    // System.out.println("==========> " + columnsLines[i]+  "=" + columnValuesLines[i]);
                    geneOntologyCreator.getColumns().add(new CSVColumn(columnsLines[i], columnValuesLines[i]));
                }

                GeneOntologyBean geneOntologyBean = geneOntologyCreator.createGeneOntology();
                if (StringUtils.isNotBlank(geneOntologyBean.getEnsembleGeneId()) && StringUtils.isNotBlank(geneOntologyBean.getGoTermAccession())
                        && StringUtils.isNotBlank(geneOntologyBean.getGoTermEvidenceCode()) && StringUtils.isNotBlank(geneOntologyBean.getGoDomain())) {
                    geneOntologyBeans.add(geneOntologyBean);
                }
            }

            //TODO:
            //Check the geneOntologyBeans list size, if size is zero,
            //then check the response whether it contains Query ERROR:
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
            } catch (Exception x) {
                //ignore whatever caught
            }
        }
        return geneOntologyBeans;
    }

    public List<ProbeGeneBean> importProbes(String speciesName) {
        if (!configured) {
            throw new WSException("The configure method must be called first.");
        }

        CSVReader csvReader = null;
        List<ProbeGeneBean> probeGeneBeans = new ArrayList<ProbeGeneBean>();
        try {
            InputStream responseIns = getWSProbeResponse(this.platform);
            csvReader = new CSVReader(new InputStreamReader(responseIns));
            String[] columnsLines = csvReader.readNext();

            String[] columnValuesLines;
            while ((columnValuesLines = csvReader.readNext()) != null) {
                ProbeGeneBeanCreator probeGeneBeanCreator = new ProbeGeneBeanCreator();
                for (int i = 0; i < columnsLines.length; i++) {
                    probeGeneBeanCreator.getColumns().add(new CSVColumn(columnsLines[i], columnValuesLines[i]));
                }
               // String platformColumnName = ProbeGeneField.PLATFORM;
               // String platformColumnValue = columnsLines[1];
                String probeSpeciesColumnName = ProbeGeneField.SPECIES;
                String probeSpeciesColumnValue = speciesName;

               // probeGeneBeanCreator.getColumns().add(new CSVColumn(platformColumnName, platformColumnValue));
                probeGeneBeanCreator.getColumns().add(new CSVColumn(probeSpeciesColumnName, probeSpeciesColumnValue));
                ProbeGeneBean probeGeneBean = probeGeneBeanCreator.createProbeGeneBean();
                if (StringUtils.isNotBlank(probeGeneBean.getEnsgAccession()) && StringUtils.isNotBlank(probeGeneBean.getProbeId())) {
                    probeGeneBeans.add(probeGeneBean);
                }
            }
           //TODO:
            //Check the probeGeneBeans list size, if size is zero,
            //then check the response whether it contains Query ERROR:
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
            } catch (Exception x) {
                //ignore whatever caught
            }
        }
        return probeGeneBeans;
    }

    public String geneQueryString(String species) {
        StringBuilder query = new StringBuilder();
        query.append("<?xml version='1.0' encoding='UTF-8'?>");
        query.append("<!DOCTYPE Query>");
        query.append("<Query  virtualSchemaName = 'default' formatter = 'CSV' header = '1' uniqueRows = '1' count = '' >");
        query.append("<Dataset name = '").append(species).append("' interface = 'default' >");
        query.append("<Attribute name = 'ensembl_gene_id' />");
        query.append("<Attribute name = 'external_gene_id' />");
        query.append("<Attribute name = 'description' />");
        query.append("<Attribute name = 'chromosome_name' />");
        query.append("<Attribute name = 'start_position' />");
        query.append("<Attribute name = 'end_position' />");
        query.append("<Attribute name = 'strand' />");
        query.append("<Attribute name = 'band' />");
        query.append("<Attribute name = 'unigene' />");
        query.append("<Attribute name = 'protein_id' />");
        query.append("<Attribute name = 'entrezgene' />");
        query.append("</Dataset>").append("</Query>");
        return query.toString();
    }

    //TODO:
    //add another ws call method to get  refseq_dna and ensembl_gene_id

    public String geneOntologyQueryString(String species) {
        StringBuilder query = new StringBuilder();
        query.append("<?xml version='1.0' encoding='UTF-8'?>");
        query.append("<!DOCTYPE Query>");
        query.append("<Query  virtualSchemaName = 'default' formatter = 'CSV' header = '1' uniqueRows = '1' count = '' >");
        query.append("<Dataset name = '").append(species).append("' interface = 'default' >");
        query.append("<Attribute name = 'ensembl_gene_id' />");
        query.append("<Attribute name = 'go_id' />");
        query.append("<Attribute name = 'name_1006' />");
        query.append("<Attribute name = 'definition_1006' />");
        query.append("<Attribute name = 'go_linkage_type' />");
        query.append("<Attribute name = 'namespace_1003' />");
        query.append("</Dataset>").append("</Query>");
        return query.toString();
    }

    public String probeQueryString(String species, String platform) {
    //public String probeQueryString(String species) {
        StringBuilder query = new StringBuilder();
        query.append("<?xml version='1.0' encoding='UTF-8'?>");
        query.append("<!DOCTYPE Query>");
        query.append("<Query  virtualSchemaName = 'default' formatter = 'CSV' header = '1' uniqueRows = '1' count = '' >");
        query.append("<Dataset name = '").append(species).append("' interface = 'default' >");
        query.append("<Filter name = 'with_").append(platform).append("' excluded = '0' />");
        query.append("<Attribute name = 'ensembl_gene_id' />");
        query.append("<Attribute name = '").append(platform).append("'  />");
        query.append("</Dataset>").append("</Query>");
        return query.toString();
    }

    public void releaseConnection() {
        // release httpclient connection
        if (httpget != null) {
            httpget.releaseConnection();
            httpget = null;
        }
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public static void main(String[] args) throws Exception {
        String wsUrl = "http://www.biomart.org/biomart/martservice/result?query=";
        String species = "hsapiens_gene_ensembl";

        BioMartClient bioMartClient = new BioMartClient();


//        bioMartClient.configure(wsUrl, species, null);
//
//        long startTime = System.currentTimeMillis();
//
//        List<Gene> tpbGeneList = bioMartClient.importGenes();
//        System.out.println(" size : " + tpbGeneList.size());
//        for (Gene gene : tpbGeneList) {
//            System.out.println(gene.getEnsgAccession() + " - " + gene.getDescription() + " - " + gene.getChromosome() + " - " + gene.getStartPosition() +
//                    " - " + gene.getEndPosition() + " - " + gene.getStrand() + " - " + gene.getBand() + " - " + gene.getUnigene() + " - " +
//                    gene.getEntrezId() + " - " + gene.getGenbankId());
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("=====> The total gene process time: " + (endTime - startTime) / 1000 + "seconds");
//
//        long goStartTime = System.currentTimeMillis();
//        List<GeneOntologyBean> geneOntologyBeans = bioMartClient.importGeneOntology();
//        System.out.println(" GeneOntology size : " + geneOntologyBeans.size());
//        for (GeneOntologyBean go : geneOntologyBeans) {
//            System.out.println(go.getEnsembleGeneId() + " - " + go.getGoTermAccession() + " - " + go.getGoTermName() + " - " + go.getGoTermDefinition() +
//                    " - " + go.getGoTermEvidenceCode() + " - " + go.getGoDomain());
//        }
//        long goEndTime = System.currentTimeMillis();
//
//        System.out.println("=====> The total GeneOntology process time: " + (goEndTime - goStartTime) / 1000 + "seconds");


//        bioMartClient.configure(wsUrl, species, "efg_agilent_wholegenome_4x44k_v1");
//        List<ProbeGeneBean> probeGeneBeans = bioMartClient.importProbes("Human");

//        for (ProbeGeneBean probeGeneBean : probeGeneBeans) {
//            //System.out.println(probeGeneBean.getEnsgAccession() + " - " + probeGeneBean.getProbeId() + " - " + probeGeneBean.getPlatform() + " - " + probeGeneBean.getProbeType());
//            System.out.println( probeGeneBean.getProbeId() + " - " + probeGeneBean.getEnsgAccession()+ " - " + probeGeneBean.getSpeciesName());
//
//        }
    }
}
