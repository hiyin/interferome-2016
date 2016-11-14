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

package edu.monash.merc.system.scheduling.impl;
// JDBC connection packages imports are changed to sql's
import java.sql.Connection;
import java.sql.Statement;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import edu.monash.merc.common.results.DBStats;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.dao.impl.GeneDAO;
import edu.monash.merc.dao.impl.SearchDataDAO;
import edu.monash.merc.domain.Data;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.Promoter;
import edu.monash.merc.domain.TFSite;
import edu.monash.merc.dto.GeneOntologyBean;

import edu.monash.merc.dto.ProbeGeneBean;
import edu.monash.merc.service.DBStatisticsService;
import edu.monash.merc.service.GeneService;
import edu.monash.merc.system.scheduling.DataProcessor;
import edu.monash.merc.service.DMService;
import edu.monash.merc.wsclient.biomart.BioMartClient;
import edu.monash.merc.wsclient.biomart.CSVGeneCreator;
import org.apache.axis2.transport.http.util.SOAPUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;

import org.hibernate.annotations.SourceType;
import org.hibernate.cache.ehcache.internal.util.HibernateUtil;
import org.hibernate.cfg.Configuration;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.*;


// SearchDAO impl


import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 11:03 AM
 */
@Service
@Qualifier("infDataProcessor")
public class INFDataProcessor extends HibernateGenericDAO<Data> implements DataProcessor {

    @Autowired
    protected DMService dmService;

    @Autowired
    protected AppPropSettings appSetting;

    public static String CIIIDER_HOME = SearchDataDAO.CIIIDER_HOME;

    public static String CIIIDER_INPUT = SearchDataDAO.CIIIDER_INPUT;

    private static String MOUSE = "mmusculus_gene_ensembl";

    private static String HUMAN = "hsapiens_gene_ensembl";

    private static String PROBE_HUMAN_TYPE = "Human";

    private static String PROBE_MOUSE_TYPE = "Mouse";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmService(DMService dmService) {
        this.dmService = dmService;
    }

    public void setAppSetting(AppPropSettings appSetting) {
        this.appSetting = appSetting;
    }

    @Autowired
    private DBStatisticsService dbStatisticsService;
    private DBStats stats;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        System.out.println("============= start interferome process .........");

        Date importedTime = GregorianCalendar.getInstance().getTime();

        //Gene for HUMAN
        // importEnsemblGenes(HUMAN, importedTime);

        // 9/9/15 Update promoter sequence code goes here (Hibernate read in file)

        //Gene for MOUSE
        //importEnsemblGenes(MOUSE, importedTime);
        long endTime = System.currentTimeMillis();
        System.out.println("Updating the CiiiDER gene list data");
        exportCiiiDERGeneList(PROBE_HUMAN_TYPE);

        // importCiiiDERPromoter(PROBE_HUMAN_TYPE);
        // importCiiiDERPromoter(PROBE_MOUSE_TYPE);


        System.out.println("Updating the CiiiDER genome files ...");
        // downloadCiiiDERGenome(PROBE_HUMAN_TYPE);
        // downloadCiiiDERGenome(PROBE_MOUSE_TYPE);

        System.out.println("Updating the CiiiDER genome gtf files ...");
        // downloadCiiiDERGenomeGTF(PROBE_HUMAN_TYPE);
        // downloadCiiiDERGenomeGTF(PROBE_MOUSE_TYPE);

        System.out.println("I am updating the CiiiDER data ...");

        // importCiiiDERTFSite(PROBE_HUMAN_TYPE);
        // importCiiiDERTFSite(PROBE_MOUSE_TYPE);

        System.out.println("Completed updating the CiiiDER TFSite data!");


        //import human and mouse probes
        //importProbes();

        //GeneOntology for HUMAN
        long goStartTime = System.currentTimeMillis();
        //importGeneOntology(HUMAN);

        //import the geneontology for mouse
        importGeneOntology(MOUSE);
        long goEndTime = System.currentTimeMillis();

        logger.info("=====> The total process time for Gene: " + (endTime - startTime) / 1000 + "seconds");

        logger.info("=====> The total process time for GeneOntology: " + (goEndTime - goStartTime) / 1000 + "seconds");

        logger.info("=====> The total process time for gene and genontology: " + (goEndTime - startTime) / 1000 + "seconds");
    }




    private void importCiiiDERPromoter(String species)  {
        try {
            String runCiiiDER = "java -jar " + CIIIDER_HOME + "Jar/CiiiDER.jar" + " -n " + CIIIDER_HOME + "Config/FindPromoter/IFNGene/" + species + "ConfigFindPromoterIFN.ini";
            Process processCiiiDER = Runtime.getRuntime().exec(runCiiiDER);
            processCiiiDER.waitFor();

            //File promoterFile = new File(path + "findPromoter/" + speciesName + "Promoter.fa");
            // Scanner promoterScanner = new Scanner(promoterFile);

            // List<Promoter> promoters = new ArrayList<Promoter>();
            List<Promoter> promoters = new ArrayList<Promoter>();
            Promoter promoter = new Promoter();


            BufferedReader brGeneEnsgs = new BufferedReader(new FileReader(new File(CIIIDER_HOME + "Output/FindPromoter/IFNGene/" + species + "IFNGenePromoter.fa")));

            String line = null;
            while ((line = brGeneEnsgs.readLine()) != null) {
                if (line.startsWith(">")) {
                    String[] identifier = line.split("\\|");
                    String geneName = identifier[0].replaceFirst(">","");
                    String ensgAccession = identifier[1];
                    promoter = new Promoter();
                    Gene gene = this.dmService.getGeneByEnsgAccession(ensgAccession);

                    promoter.setGene(gene);
                    // String linked_gene_ensgAccession = promoter.getGene().getEnsgAccession();
                    // System.out.println(linked_gene_ensgAccession);
                    promoter.setGeneName(geneName);
                    // promoter.setEnsgAccession(ensgAccession);
                    // promoter.setEnsgAccession(gene.getEnsgAccession());
                } else if (!line.equals("")) {
                    promoter.setSequence(line);
                    if(!promoters.contains(promoter)) promoters.add(promoter);
                }
            }
            this.dmService.importPromoter(promoters);

        } catch (IOException e) {
                e.printStackTrace();
        } catch (HibernateException ex) {
            ex.getCause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void importCiiiDERTFSite (String species) {
        try {
        // Pre-run CiiiDER if bsl.txt file not available

        // Create TFSite array to store TFSite
        List<TFSite> tfSites = new ArrayList<TFSite>();
        TFSite tfSite = new TFSite();
        
        // Read in bsl.txt 
        BufferedReader brGeneBSL = new BufferedReader(new FileReader(new File (CIIIDER_HOME + "Output/ScanTFSite/" + species + "GeneBindingSiteList.txt")));
        // LineIterator itGeneBSL = FileUtils.lineIterator(new File (CIIIDER_HOME + "Output/ScanTFSite/" + species + "GeneBindingSiteList.txt"));

        String line = null;
        while ((line = brGeneBSL.readLine()) != null) {
            // while ((line = itGeneBSL.nextLine()) != null) {
            String[] fields = line.split(",");
            String[] identifier = fields[0].split("\\|");
            String quantifier = fields[1];
            String factor = fields[2];
            String factor_index = fields[3];
            int start = Integer.valueOf(fields[4]);
            int end = Integer.valueOf(fields[5]);
            String strand = fields[6];
            Double core_match = Double.valueOf(fields[7]);
            Double matrix_match = Double.valueOf(fields[8]);


            String geneName = identifier[0];
            String ensgAccession = identifier[1];

            tfSite = new TFSite();

            Gene gene = this.dmService.getGeneByEnsgAccession(ensgAccession);
            tfSite.setGene(gene);
            tfSite.setFactor(factor);
            tfSite.setStart(start);
            tfSite.setEnd(end);
            tfSite.setCoreMatch(core_match);
            tfSite.setMatrixMatch(matrix_match);
            tfSite.setEnsemblID(ensgAccession);


            if(!tfSites.contains(tfSite)) tfSites.add(tfSite);


        }
            System.out.println(tfSites.size());
            this.dmService.importAllTFSites(tfSites);
            brGeneBSL.close();



        } catch (IOException e) {
            e.printStackTrace();
        } catch (HibernateException ex) {
            ex.getCause();
        }
    }




    private void downloadCiiiDERGenome (String species) {

            int BUFFER_SIZE = 8192;
            String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
            String ftpFilePath = null;
            String host = "ftp.ensembl.org";
            String user = "anonymous";
            String pass = "anonymous";
            String savePath = null;

            if (species == PROBE_HUMAN_TYPE) {
                ftpFilePath = "/pub/current_fasta/homo_sapiens/dna/Homo_sapiens.GRCh38.dna.toplevel.fa.gz";
                savePath = CIIIDER_INPUT + "/Homo_sapiens.GRCh38.dna.toplevel.fa.gz";

            }
            if (species == PROBE_MOUSE_TYPE) {
                ftpFilePath = "/pub/current_fasta/mus_musculus/dna/Mus_musculus.GRCm38.dna.toplevel.fa.gz";
                savePath = CIIIDER_INPUT + "/Mus_musculus.GRCm38.dna.toplevel.fa.gz";
            }

            ftpUrl = String.format(ftpUrl, user, pass, host, ftpFilePath);
            System.out.println("URL: " + ftpUrl);

            try {
                URL url = new URL(ftpUrl);
                URLConnection conn = url.openConnection();
                InputStream inputStream = conn.getInputStream();

                FileOutputStream outputStream = new FileOutputStream(savePath);

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("Genome file downloaded");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    private void downloadCiiiDERGenomeGTF (String species) {

        int BUFFER_SIZE = 8192;
        String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String ftpFilePath = null;
        String host = "ftp.ensembl.org";
        String user = "anonymous";
        String pass = "anonymous";
        String savePath = null;

        if (species == PROBE_HUMAN_TYPE) {
            ftpFilePath = "/pub/current_gtf/homo_sapiens/Homo_sapiens.GRCh38.86.gtf.gz";
            savePath = CIIIDER_INPUT + "/Homo_sapiens.GRCh38.86.gtf.gz";

        }
        if (species == PROBE_MOUSE_TYPE) {
            ftpFilePath = "/pub/current_gtf/mus_musculus/Mus_musculus.GRCm38.86.gtf.gz";
            savePath = CIIIDER_INPUT + "/Mus_musculus.GRCm38.86.gtf.gz";
        }

        ftpUrl = String.format(ftpUrl, user, pass, host, ftpFilePath);
        System.out.println("URL: " + ftpUrl);

        try {
            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();

            FileOutputStream outputStream = new FileOutputStream(savePath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("GTF file downloaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void exportCiiiDERGeneList(String species) {
        // Export geneAccession from Interferome to update promoter information
        String host = "localhost";
        String username = "mimr";
        String password = "";
        String database = "infdev2";
        String query = "SELECT DISTINCT g.ensg_accession FROM gene g, probe_gene pg, probe p, data d WHERE g.id = pg.gene_id and pg.probe_id = p.id and p.id = d.probe_id and d.data_value not between -2 and 2 and g.ensg_accession LIKE 'ENSG%' LIMIT 1000000";
        // Changed imported package jdbc.connection to sql.Connection 15 Nov 16

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection geneConnect = null;

            geneConnect = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?"
                    + "user=" + username + "&password=" + password + "");
            Statement geneStmt = geneConnect.createStatement();

            ResultSet geneResult = geneStmt.executeQuery(query);

            ArrayList<String> geneEnsgAccessionList = new ArrayList<String>();
            while (geneResult.next()) {
                String geneEnsgAccession = geneResult.getString("ensg_accession");
                geneEnsgAccessionList.add(geneEnsgAccession);
            }
            File geneList = new File(CIIIDER_INPUT + "GeneList.txt");
            FileUtils.writeLines(geneList, geneEnsgAccessionList, "\n");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            // handle the error
        }

    }


    private void importEnsemblGenes(String species, Date importedTime) {
        BioMartClient client = null;
        try {
            String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

            client = new BioMartClient();
            client.configure(wsURL, species, null);
            List<Gene> geneList = client.importGenes();

            logger.info("============> total genes size : " + geneList.size());
            this.dmService.importGenes(geneList, importedTime);
            logger.info("======== imported the ensembl genes into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }

    }

    private void importProbes() {

        String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

        List<String> wsHumanPlatforms = getProbePlateforms(PROBE_HUMAN_TYPE);

        for (String platform : wsHumanPlatforms) {
            importProbesByType(wsURL, HUMAN, platform, PROBE_HUMAN_TYPE);
        }

        List<String> wsMousePlatforms = getProbePlateforms(PROBE_MOUSE_TYPE);

        for (String platform : wsMousePlatforms) {
            importProbesByType(wsURL, MOUSE, platform, PROBE_MOUSE_TYPE);
        }
    }

    private void importProbesByType(String wsUrl, String species, String platform, String probeType) {
        BioMartClient client = null;
        try {
            client = new BioMartClient();
            client.configure(wsUrl, species, platform);
            List<ProbeGeneBean> probeGeneBeans = client.importProbes(probeType);
            logger.info("============> total probes  size for  " + species + " - " + probeType + " : " + probeGeneBeans.size());
            this.dmService.importProbes(probeGeneBeans);
            //this.dmService.importProbes(null);
            logger.info("======== imported the probes into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }
    }

    private void importGeneOntology(String species) {
        BioMartClient client = null;
        try {
            String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

            client = new BioMartClient();
            client.configure(wsURL, species, null);
            List<GeneOntologyBean> geneOntologyBeans = client.importGeneOntology();

            logger.info("============> total geneontology size : " + geneOntologyBeans.size());
            this.dmService.importGeneOntologies(geneOntologyBeans);
            logger.info("======== imported the geneontology into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }
    }

    private List<String> getProbePlateforms(String type) {
        List<String> probeSearch = new ArrayList<String>();
        if (StringUtils.equals(type, PROBE_HUMAN_TYPE)) {
            probeSearch.clear();
        probeSearch.add("efg_agilent_sureprint_g3_ge_8x60k");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v1");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v2");
        probeSearch.add("affy_hc_g110");
        probeSearch.add("affy_hg_u133_plus_2");
        probeSearch.add("affy_hg_focus");
        probeSearch.add("affy_hg_u133a_2");
        probeSearch.add("affy_hg_u133a");
        probeSearch.add("affy_hg_u95av2");
        probeSearch.add("affy_hg_u133b");
        probeSearch.add("affy_hg_u95b");
        probeSearch.add("affy_hg_u95c");
        probeSearch.add("affy_hg_u95d");
        probeSearch.add("phalanx_onearray");
        probeSearch.add("illumina_humanht_12");
        probeSearch.add("illumina_humanwg_6_v3");
        probeSearch.add("illumina_humanwg_6_v2");
        probeSearch.add("illumina_humanwg_6_v1");
        probeSearch.add("codelink");
        probeSearch.add("agilent_cgh_44b");
        probeSearch.add("affy_u133_x3p");
        probeSearch.add("affy_hugene_1_0_st_v1");
        probeSearch.add("affy_huex_1_0_st_v2");
        probeSearch.add("affy_hugenefl");
        probeSearch.add("affy_hg_u95a");
        probeSearch.add("affy_hg_u95e");

        }
        if (StringUtils.equals(type, PROBE_MOUSE_TYPE)) {
            probeSearch.clear();
        probeSearch.add("efg_agilent_sureprint_g3_ge_8x60k");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v1");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v2");
        probeSearch.add("affy_mg_u74a");
        probeSearch.add("affy_mg_u74av2");
        probeSearch.add("affy_mg_u74b");
        probeSearch.add("affy_mg_u74bv2");
        probeSearch.add("affy_mg_u74c");
        probeSearch.add("affy_mg_u74cv2");
        probeSearch.add("affy_moe430a");
        probeSearch.add("affy_moe430b");
        probeSearch.add("affy_moex_1_0_st_v1");
        probeSearch.add("affy_mogene_1_0_st_v1");
        probeSearch.add("affy_mouse430_2");
        probeSearch.add("affy_mouse430a_2");
        probeSearch.add("affy_mu11ksuba");
        probeSearch.add("affy_mu11ksubb");
        probeSearch.add("codelink");
        probeSearch.add("illumina_mousewg_6_v1");
        probeSearch.add("illumina_mousewg_6_v2");
        probeSearch.add("phalanx_onearray");
      }
        return probeSearch;
    }
}
