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
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 1:27 PM
 */
@Entity
@Table(name = "ontology")
@org.hibernate.annotations.Table(appliesTo = "ontology",
        indexes = {@Index(name = "idx_go_term_acc", columnNames = {"go_term_acc"}),
                @Index(name = "idx_go_term_name", columnNames = {"go_term_name"}),
                @Index(name = "idx_go_count", columnNames = {"go_count"})
        })
public class Ontology extends Domain {

    @Id
    @GeneratedValue(generator = "ontology_seq")
    @GenericGenerator(name = "ontology_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "go_term_acc")
    private String goTermAccession;

    @Basic
    @Column(name = "go_term_name")
    private String goTermName;

    @Basic
    @Column(name = "go_term_definition", columnDefinition = "longtext")
    private String goTermDefinition;

    @Basic
    @Column(name = "go_count", columnDefinition = "int")
    private String goCount;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "go_domain_id")
    private GoDomain goDomain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoTermAccession() {
        return goTermAccession;
    }

    public void setGoTermAccession(String goTermAccession) {
        this.goTermAccession = goTermAccession;
    }

    public String getGoTermName() {
        return goTermName;
    }

    public void setGoTermName(String goTermName) {
        this.goTermName = goTermName;
    }

    public String getGoTermDefinition() {
        return goTermDefinition;
    }

    public void setGoTermDefinition(String goTermDefinition) {
        this.goTermDefinition = goTermDefinition;
    }

    public GoDomain getGoDomain() {
        return goDomain;
    }

    public void setGoDomain(GoDomain goDomain) {
        this.goDomain = goDomain;
    }

    public String getGoCount() {
        return goCount;
    }

    public void setGoCount(String goCount) {
        this.goCount = goCount;
    }
}
