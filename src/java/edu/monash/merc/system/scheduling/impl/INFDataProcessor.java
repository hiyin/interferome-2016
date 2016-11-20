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
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.thoughtworks.xstream.io.path.Path;
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
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
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
import org.hibernate.Query;
import org.apache.commons.io.filefilter.WildcardFileFilter;

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
@Transactional
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

        //import human and mouse probes
        //importProbes();

        importCiiiDERAllInput();

        //GeneOntology for HUMAN
        long goStartTime = System.currentTimeMillis();
        //importGeneOntology(HUMAN);

        //import the geneontology for mouse
        // importGeneOntology(MOUSE);
        long goEndTime = System.currentTimeMillis();

        logger.info("=====> The total process time for Gene: " + (endTime - startTime) / 1000 + "seconds");

        logger.info("=====> The total process time for GeneOntology: " + (goEndTime - goStartTime) / 1000 + "seconds");

        logger.info("=====> The total process time for gene and genontology: " + (goEndTime - startTime) / 1000 + "seconds");
    }




    private void importCiiiDERAllInput() {

        System.out.println("Updating the CiiiDER genome files ...");
        // downloadCiiiDERGenome(PROBE_HUMAN_TYPE);
        // downloadCiiiDERGenome(PROBE_MOUSE_TYPE);

        System.out.println("Updating the CiiiDER genome gtf files ...");
        // downloadCiiiDERGenomeGTF(PROBE_HUMAN_TYPE);
        // downloadCiiiDERGenomeGTF(PROBE_MOUSE_TYPE);

        System.out.println("Updating the CiiiDER gene list text files ...");
        // exportCiiiDERGeneList("IFNGene", PROBE_HUMAN_TYPE);
        // exportCiiiDERGeneList("IFNGene", PROBE_MOUSE_TYPE);
        // exportCiiiDERGeneList("BackgroundGene", PROBE_HUMAN_TYPE);
        // exportCiiiDERGeneList("BackgroundGene", PROBE_MOUSE_TYPE);

        System.out.println("Generating the CiiiDER background and IFN gene promoter fasta files ...");
        // generateCiiiDERPromoter("BackgroundGene", PROBE_HUMAN_TYPE);
        // generateCiiiDERPromoter("BackgroundGene", PROBE_MOUSE_TYPE);

         importCiiiDERBgGenePromoter(PROBE_HUMAN_TYPE);
        // importCiiiDERBgGenePromoter(PROBE_MOUSE_TYPE);

        // generateCiiiDERPromoter("IFNGene", PROBE_HUMAN_TYPE);
        // generateCiiiDERPromoter("IFNGene", PROBE_MOUSE_TYPE);

        // importCiiiDERGenePromoter(PROBE_HUMAN_TYPE);
        // importCiiiDERGenePromoter(PROBE_MOUSE_TYPE);

        System.out.println("Generating the CiiiDER tfsite file ...");
        // generateCiiiDERTFSite(PROBE_HUMAN_TYPE);
        // generateCiiiDERTFSite(PROBE_MOUSE_TYPE);

        System.out.println("Importing the CiiiDER tfsite data into database...");
        // importCiiiDERTFSite(PROBE_HUMAN_TYPE);
        // importCiiiDERTFSite(PROBE_MOUSE_TYPE);

        System.out.println("Completed updating ALL CiiiDER input data!");

    }


    private void importCiiiDERBgGenePromoter(String species) {
        try {

            List<Promoter> promoters = new ArrayList<Promoter>();
            Promoter promoter = new Promoter();

            BufferedReader brGeneEnsgs = new BufferedReader(new FileReader(new File(CIIIDER_HOME + "Output/FindPromoter/BackgroundGene/" + species + "BgGenePromoter.fa")));

            String line = null;
            while ((line = brGeneEnsgs.readLine()) != null) {
                if (line.startsWith(">")) {
//                    String[] identifier = line.split("\\|");
//                    String geneName = identifier[0].replaceFirst(">","");
//                    String ensgAccession = identifier[1];
                    String geneName = line.substring(line.indexOf(">")+1, line.indexOf("|"));
                    String ensgAccession = line.substring(line.indexOf("|")+1);

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


    private void generateCiiiDERPromoter(String promoterType, String species) {

        try {
            String runCiiiDER = null;
            if (promoterType == "BackgroundGene") {
                runCiiiDER = "java -jar " + CIIIDER_HOME + "Jar/CiiiDER.jar" + " -n " + CIIIDER_HOME + "Config/FindPromoter/BackgroundGene/" + species + "ConfigFindPromoterBg.ini";
            }
            if (promoterType == "IFNGene") {
                runCiiiDER = "java -jar " + CIIIDER_HOME + "Jar/CiiiDER.jar" + " -n " + CIIIDER_HOME + "Config/FindPromoter/IFNGene/" + species + "ConfigFindPromoterIFN.ini";
            }
            Process processCiiiDER = Runtime.getRuntime().exec(runCiiiDER);
            processCiiiDER.waitFor();

            System.out.println(String.format("Completed generating the CiiiDER %s promoter fasta files for %s ...", promoterType, species));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void importCiiiDERGenePromoter(String species)  {
        try {
            //File promoterFile = new File(path + "findPromoter/" + speciesName + "Promoter.fa");
            // Scanner promoterScanner = new Scanner(promoterFile);

            // List<Promoter> promoters = new ArrayList<Promoter>();
            List<Promoter> promoters = new ArrayList<Promoter>();
            Promoter promoter = new Promoter();


            BufferedReader brGeneEnsgs = new BufferedReader(new FileReader(new File(CIIIDER_HOME + "Output/FindPromoter/IFNGene/" + species + "IFNGenePromoter.fa")));

            String line = null;
            while ((line = brGeneEnsgs.readLine()) != null) {
                if (line.startsWith(">")) {
                    String geneName = line.substring(line.indexOf(">") + 1, line.indexOf("|"));
                    String ensgAccession = line.substring(line.indexOf("|")+1);
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

    private void generateCiiiDERTFSite(String species) {

        try {
            String runCiiiDER = null;
            if (species == PROBE_HUMAN_TYPE) {
                runCiiiDER = "java -jar " + CIIIDER_HOME + "Jar/CiiiDER.jar" + " -n " + CIIIDER_HOME + "Config/ScanTFSite/" + species + "ConfigScanTFSite.ini";
            }
            if (species == PROBE_MOUSE_TYPE) {
                runCiiiDER = "java -jar " + CIIIDER_HOME + "Jar/CiiiDER.jar" + " -n " + CIIIDER_HOME + "Config/ScanTFSite/" + species + "ConfigScanTFSite.ini";
            }
            Process processCiiiDER = Runtime.getRuntime().exec(runCiiiDER);
            processCiiiDER.waitFor();

            System.out.println(String.format("Completed generating the CiiiDER TFSite files for %s ...", species));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        BufferedReader brGeneBSL = new BufferedReader(new FileReader
                (new File (CIIIDER_HOME + "Output/ScanTFSite/" + species + "GeneBindingSiteList.txt")));
        // LineIterator itGeneBSL = FileUtils.lineIterator(new File (CIIIDER_HOME + "Output/ScanTFSite/" + species + "GeneBindingSiteList.txt"));

        String line;
        while ((line = brGeneBSL.readLine()) != null) {
                String ensgAccession = line.substring(line.indexOf("|")+1, StringUtils.ordinalIndexOf(line, ",", 1));
                // int quantifier = Integer.valueOf(line.substring(line.indexOf(",")+1, StringUtils.ordinalIndexOf(line, ",", 2)));
                String factor = line.substring(StringUtils.ordinalIndexOf(line, ",", 2)+1, StringUtils.ordinalIndexOf(line, ",", 3));
                int start = Integer.valueOf(line.substring(StringUtils.ordinalIndexOf(line, ",", 4) + 1, StringUtils.ordinalIndexOf(line, ",", 5)));
                int end = Integer.valueOf(line.substring(StringUtils.ordinalIndexOf(line, ",", 5) + 1, StringUtils.ordinalIndexOf(line, ",", 6)));
                Double coreMatch = Double.valueOf(line.substring(StringUtils.ordinalIndexOf(line, ",", 7) + 1, StringUtils.ordinalIndexOf(line, ",", 8)));
                Double matrixMatch = Double.valueOf(line.substring(StringUtils.ordinalIndexOf(line, ",", 8) + 1));

            tfSite = new TFSite();

            // Gene gene = this.dmService.getGeneByEnsgAccession(ensgAccession);
            // tfSite.setGene(gene);
            tfSite.setFactor(factor);
            tfSite.setStart(start);
            tfSite.setEnd(end);
            tfSite.setCoreMatch(coreMatch);
            tfSite.setMatrixMatch(matrixMatch);
            tfSite.setEnsemblID(ensgAccession);


            if(!tfSites.contains(tfSite)) tfSites.add(tfSite);


        }

            brGeneBSL.close();
            System.out.println(tfSites.size());
            this.dmService.importAllTFSites(tfSites);
            System.out.println("Completed importing CiiiDER tfsite of size :" + tfSites.size());




        } catch (IOException e) {
            e.printStackTrace();
        } catch (HibernateException ex) {
            ex.getCause();
        }
    }




    private void downloadCiiiDERGenome (String species) {

            int BUFFER_SIZE = 8192;
            String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
            String ftpFileLocation = null;
            String ftpFilename = null;
            String host = "ftp.ensembl.org";
            String user = "anonymous";
            String pass = "anonymous";
            String savePath = null;

            try {

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, 21);
            ftpClient.login(user, pass);

            if (species == PROBE_HUMAN_TYPE) {
                ftpFileLocation = "/pub/current_fasta/homo_sapiens/dna/";
                ftpFilename = getFTPFilePath(ftpClient, ftpFileLocation, "Homo_sapiens.*.dna.toplevel.fa.gz");
                savePath = CIIIDER_INPUT + "/Homo_sapiens.GRCh38.dna.toplevel.fa.gz";
            }

            if (species == PROBE_MOUSE_TYPE) {
                ftpFileLocation = "/pub/current_fasta/mus_musculus/dna/";
                ftpFilename = getFTPFilePath(ftpClient, ftpFileLocation, "Mus_musculus.*.dna.toplevel.fa.gz");
                savePath = CIIIDER_INPUT + ftpFilename;

            }

            ftpUrl = String.format(ftpUrl, user, pass, host, ftpFileLocation + ftpFilename);
            System.out.println("URL: " + ftpUrl);

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

    private String getFTPFilePath(FTPClient ftpClient, String ftpFileLocation, String pattern) {
        String ftpFilename = null;
        try {
            FTPFile[] files = ftpClient.listFiles(ftpFileLocation);
            for (FTPFile file : files) {
                System.out.println(file.getName());
                if (file.getName().matches(pattern)) {
                    System.out.println("Matched: " + file.getName());
                    ftpFilename = file.getName();
                }

            }
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }

        return ftpFilename;
    }

    private void downloadCiiiDERGenomeGTF (String species) {

        int BUFFER_SIZE = 8192;
        String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String ftpFileLocation = null;
        String ftpFilename = null;
        String host = "ftp.ensembl.org";
        String user = "anonymous";
        String pass = "anonymous";
        String savePath = null;


        try {

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, 21);
            ftpClient.login(user, pass);

            if (species == PROBE_HUMAN_TYPE) {
                ftpFileLocation = "/pub/current_gtf/homo_sapiens/";
                ftpFilename = getFTPFilePath(ftpClient, ftpFileLocation, "Homo_sapiens.*.[0-9*].gtf.gz");
                savePath = CIIIDER_INPUT + ftpFilename;
            }

            if (species == PROBE_MOUSE_TYPE) {
                ftpFileLocation = "/pub/current_gtf/mus_musculus/";
                ftpFilename = getFTPFilePath(ftpClient, ftpFileLocation, "Mus_musculus.*.[0-9*].gtf.gz");
                savePath = CIIIDER_INPUT + ftpFilename;
            }

        ftpUrl = String.format(ftpUrl, user, pass, host, ftpFileLocation + ftpFilename);

        System.out.println("URL: " + ftpUrl);


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

    private void exportCiiiDERGeneList(String geneType, String species) {
        // Export geneAccession from Interferome to update promoter information

        try {
            if (geneType == "IFNGene") {
                String query = null;
                if (species == PROBE_HUMAN_TYPE) {
                    query = "SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between -1 and 1 and g.ensgAccession LIKE 'ENSG%'";
                }
                if (species == PROBE_MOUSE_TYPE) {
                    query = "SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between -1 and 1 and g.ensgAccession LIKE 'ENSMUSG%'";
                }
                List<String> IFNGeneList = this.session().createQuery(query).setMaxResults(100000).list();
                if (!IFNGeneList.isEmpty()) {
                    FileUtils.writeLines(new File(CIIIDER_INPUT + species + "IFNGeneIdList.txt"), IFNGeneList);
                    System.out.println("Finished writing the gene id/ensg accession list file ...");
                }
            }
            if (geneType == "BackgroundGene") {
                String BgGeneHQL = null;
                if (species == PROBE_HUMAN_TYPE) {
                    BgGeneHQL = "SELECT DISTINCT g.ensgAccession From Gene g WHERE g.ensgAccession LIKE 'ENSG%' AND g.ensgAccession NOT IN (SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between -1 and 1 and g.ensgAccession LIKE 'ENSG%')";
                }
                if (species == PROBE_MOUSE_TYPE) {
                    BgGeneHQL = "SELECT DISTINCT g.ensgAccession From Gene g WHERE g.ensgAccession LIKE 'ENSMUSG%' AND g.ensgAccession NOT IN (SELECT DISTINCT g.ensgAccession FROM Gene g, Probe p, Data d INNER JOIN g.probe pg INNER JOIN d.probe dp WHERE pg.probeId = p.probeId and p.probeId = dp.probeId and d.value not between -1 and 1 and g.ensgAccession LIKE 'ENSMUSG%')";
                }

                List<String> BgGenes = this.session().createQuery(BgGeneHQL).setMaxResults(5000).list();
                FileUtils.writeLines(new File(CIIIDER_INPUT + species + "BgGeneIdList.txt"), BgGenes);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
