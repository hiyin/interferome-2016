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

/**
 * Created by mimr on 1/13/16.
 */


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Danqing (Angela) Yin
 * @version 1.0
 * @email angela.yin@hudson.org.au
 * @since 1.0
 *        <p/>
 *        Date: 13/01/16
 *        Time: 12:17 AM
 */
@Entity
@Table(name = "promoter")
@org.hibernate.annotations.Table(appliesTo = "promoter",
        indexes = {@Index(name = "idx_ensg_accession", columnNames = {"ensg_accession"}),
                @Index(name = "idx_gene_name", columnNames = {"gene_name"})

        })

// Requires a ManyToMany mapping from Gene to Promoter

public class Promoter extends Domain {
    @Id
    @GeneratedValue(generator = "promoter_pk_seq")
    @GenericGenerator(name = "promoter_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "gene_name")
    private String geneName;

//    @Basic
//    @Column(name = "ensg_accession")
//    private String ensgAccession;

    @OneToOne(targetEntity = Gene.class)
    @ForeignKey(name = "fk_gene_ensg_accession")
    // @Index(name ="fk_gene_ensg_accession")
    @JoinColumn(name = "ensg_accession", referencedColumnName = "ensg_accession")
    private Gene gene;




    @Basic
    @Column(name = "promoter_sequence", columnDefinition = "text")
    private String sequence;


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

//    public String getEnsgAccession() {
//        return ensgAccession;
//    }
//
//    public void setEnsgAccession(String ensgAccession) {
//        this.ensgAccession = ensgAccession;
//    }

//    public String getEnsgAccession() {
//        return gene.getEnsgAccession();
//    }


    public Gene getGene () {
        return gene;
    }
    public Gene setGene (Gene gene) {
        return this.gene = gene;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promoter)) return false;

        Promoter promoter = (Promoter) o;

        if (id != promoter.id) return false;
        if (gene.getEnsgAccession() != null ? !gene.getEnsgAccession().equals(promoter.getGene().getEnsgAccession()) : promoter.getGene().getEnsgAccession() != null)
            return false;
        return true;
    }

}

