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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 28/06/12
 *        Time: 2:20 PM
 */

@Entity
@Table(name = "probe")
@org.hibernate.annotations.Table(appliesTo = "probe",
        indexes = {@Index(name = "probeId", columnNames = {"probe_id"})
                // @Index(name = "idx_platform", columnNames = {"platform"}),
                //@Index(name = "idx_species", columnNames = {"species"})
        })
public class Probe extends Domain {

    @Id
    @GeneratedValue(generator = "probe_pk_seq")
    @GenericGenerator(name = "probe_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "probe_id")
    private String probeId;

    @Transient
    private String ensemblId;

    @Transient
    private String speciesName;

    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;

    @ManyToMany(targetEntity = Gene.class)
    @JoinTable(name = "probe_gene", joinColumns = {@JoinColumn(name = "probe_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "gene_id", referencedColumnName = "id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {
            "probe_id", "gene_id"})})

    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Gene> genes;

    @OneToMany(targetEntity = Data.class, mappedBy = "probe")
    private List<Data> data;

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

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getEnsemblId() {
        return ensemblId;
    }

    public void setEnsemblId(String ensemblId) {
        this.ensemblId = ensemblId;
    }
}
