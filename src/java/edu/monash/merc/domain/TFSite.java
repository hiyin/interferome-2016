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

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 4:40 PM
 */

@Entity
@Table(name = "tf_site")
@org.hibernate.annotations.Table(appliesTo = "tf_site",
        indexes = {@Index(name = "idx_factor", columnNames = {"factor"})
        })
public class TFSite extends Domain {

    @Id
    @GeneratedValue(generator = "transcript_factor_seq")
    @GenericGenerator(name = "transcript_factor_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "gene_id")
    private Gene gene;

    @Basic
    @Column(name = "start_pos")
    private int start;

    @Basic
    @Column(name = "end_pos")
    private int end;

    @Basic
    @Column(name = "core_match")
    private double coreMatch;

    @Basic
    @Column(name = "matrix_match")
    private double matrixMatch;

    @Basic
    @Column(name = "factor")
    private String factor;

    @Transient
    private String ensemblID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getCoreMatch() {
        return coreMatch;
    }

    public void setCoreMatch(double coreMatch) {
        this.coreMatch = coreMatch;
    }

    public double getMatrixMatch() {
        return matrixMatch;
    }

    public void setMatrixMatch(double matrixMatch) {
        this.matrixMatch = matrixMatch;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getEnsemblID() {
        return ensemblID;
    }

    public void setEnsemblID(String ensemblID) {
        this.ensemblID = ensemblID;
    }


}
