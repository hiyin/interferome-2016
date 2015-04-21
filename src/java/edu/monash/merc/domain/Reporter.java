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

package edu.monash.merc.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.List;

/**
 * Reporter domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "reporter")
@org.hibernate.annotations.Table(appliesTo = "reporter",
        indexes = {@Index(name = "reporter_probe_id", columnNames = {"probe_id"})
        })
public class Reporter extends Domain {
    @Id
    @GeneratedValue(generator = "reporter_pk_seq")
    @GenericGenerator(name = "reporter_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "probe_id")
    private String probeId;

    @Basic
    @Column(name = "transcript_id_array_design")
    private String transcriptIdArrayDesign;

    @Column(name = "target_description", columnDefinition = "longtext")
    private String targetDescription;

    @Basic
    @Column(name = "gen_bank_accession")
    private String genBankAccession;

    @Basic
    @Column(name = "unigene_id")
    private String uniGeneId;

    @Column(name = "alignments", columnDefinition = "longtext")
    private String alignments;

    @Column(name = "gene_title", columnDefinition = "longtext")
    private String geneTitle;

    @Column(name = "gene_symbol", columnDefinition = "longtext")
    private String geneSymbol;

    @Column(name = "chromosomal_location", columnDefinition = "longtext")
    private String chromosomalLocation;

    @Column(name = "ensembl", columnDefinition = "longtext")
    private String ensembl;

    @Column(name = "entrez_gene", columnDefinition = "longtext")
    private String entrezGene;

    @Column(name = "refseq_protein_id", columnDefinition = "longtext")
    private String refSeqProteinId;

    @Column(name = "refseq_transcript_id", columnDefinition = "longtext")
    private String refSeqTranscriptId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProbeId() {
        return probeId;
    }

    public void setProbeId(String probeId) {
        this.probeId = probeId;
    }

    public String getTranscriptIdArrayDesign() {
        return transcriptIdArrayDesign;
    }

    public void setTranscriptIdArrayDesign(String transcriptIdArrayDesign) {
        this.transcriptIdArrayDesign = transcriptIdArrayDesign;
    }

    public String getTargetDescription() {
        return targetDescription;
    }

    public void setTargetDescription(String targetDescription) {
        this.targetDescription = targetDescription;
    }

    public String getGenBankAccession() {
        return genBankAccession;
    }

    public void setGenBankAccession(String genBankAccession) {
        this.genBankAccession = genBankAccession;
    }

    public String getUniGeneId() {
        return uniGeneId;
    }

    public void setUniGeneId(String uniGeneId) {
        this.uniGeneId = uniGeneId;
    }

    public String getAlignments() {
        return alignments;
    }

    public void setAlignments(String alignments) {
        this.alignments = alignments;
    }

    public String getGeneTitle() {
        return geneTitle;
    }

    public void setGeneTitle(String geneTitle) {
        this.geneTitle = geneTitle;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getChromosomalLocation() {
        return chromosomalLocation;
    }

    public void setChromosomalLocation(String chromosomalLocation) {
        this.chromosomalLocation = chromosomalLocation;
    }

    public String getEnsembl() {
        return ensembl;
    }

    public void setEnsembl(String ensembl) {
        this.ensembl = ensembl;
    }

    public String getEntrezGene() {
        return entrezGene;
    }

    public void setEntrezGene(String entrezGene) {
        this.entrezGene = entrezGene;
    }

    public String getRefSeqProteinId() {
        return refSeqProteinId;
    }

    public void setRefSeqProteinId(String refSeqProteinId) {
        this.refSeqProteinId = refSeqProteinId;
    }

    public String getRefSeqTranscriptId() {
        return refSeqTranscriptId;
    }

    public void setRefSeqTranscriptId(String refSeqTranscriptId) {
        this.refSeqTranscriptId = refSeqTranscriptId;
    }
}
