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
import java.util.Date;
import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 10:24 AM
 */
@Entity
@Table(name = "gene")
@org.hibernate.annotations.Table(appliesTo = "gene",
        indexes = {@Index(name = "idx_ensg_accession", columnNames = {"ensg_accession"}),
                @Index(name = "idx_gene_name", columnNames = {"gene_name"}),
                @Index(name = "idx_chromosome", columnNames = {"chromosome"}),
                @Index(name = "idx_refseq_dna", columnNames = {"refseq_dna"}),
                @Index(name = "idx_unigene", columnNames = {"unigene"}),
                @Index(name = "idx_genbank_id", columnNames = {"genbank_id"}),
                @Index(name = "idx_entrez_id", columnNames = {"entrez_id"})
        })
public class Gene extends Domain {
    @Id
    @GeneratedValue(generator = "gene_pk_seq")
    @GenericGenerator(name = "gene_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "gene_name")
    private String geneName;

    @Basic
    @Column(name = "ensg_accession")
    private String ensgAccession;

    @Basic
    @Column(name = "description", columnDefinition = "longtext")
    private String description;

    @Basic
    @Column(name = "chromosome")
    private String chromosome;

    @Basic
    @Column(name = "start_position")
    private long startPosition;

    @Basic
    @Column(name = "end_position")
    private long endPosition;

    @Basic
    @Column(name = "strand")
    private String strand;

    @Basic
    @Column(name = "band")
    private String band;

    @Basic
    @Column(name = "refseq_dna")
    private String refseq_dna;

    @Basic
    @Column(name = "unigene")
    private String unigene;

    @Basic
    @Column(name = "genbank_id")
    private String genbankId;

    @Basic
    @Column(name = "entrez_id")
    private String entrezId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "imported_time")
    private Date importedTime;

    @ManyToMany(mappedBy = "genes")
    private List<Probe> probe;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getEnsgAccession() {
        return ensgAccession;
    }

    public void setEnsgAccession(String ensgAccession) {
        this.ensgAccession = ensgAccession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getRefseq_dna() {
        return refseq_dna;
    }

    public void setRefseq_dna(String refseq_dna) {
        this.refseq_dna = refseq_dna;
    }

    public String getUnigene() {
        return unigene;
    }

    public void setUnigene(String unigene) {
        this.unigene = unigene;
    }

    public String getGenbankId() {
        return genbankId;
    }

    public void setGenbankId(String genbankId) {
        this.genbankId = genbankId;
    }

    public String getEntrezId() {
        return entrezId;
    }

    public void setEntrezId(String entrezId) {
        this.entrezId = entrezId;
    }

    public Date getImportedTime() {
        return importedTime;
    }

    public void setImportedTime(Date importedTime) {
        this.importedTime = importedTime;
    }

    public List<Probe> getProbe() {
        return probe;
    }

    public void setProbe(List<Probe> probe) {
        this.probe = probe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gene)) return false;

        Gene gene = (Gene) o;

        if (id != gene.id) return false;
        if (ensgAccession != null ? !ensgAccession.equals(gene.ensgAccession) : gene.ensgAccession != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (ensgAccession != null ? ensgAccession.hashCode() : 0);
        return result;
    }
}
