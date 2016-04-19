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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Dataset domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "dataset")
@org.hibernate.annotations.Table(appliesTo = "dataset",
        indexes = {@Index(name = "treattime", columnNames = {"treatment_time"}),
                @Index(name = "treatcon", columnNames = {"treatment_con"})
        })
public class Dataset extends Domain {

    @Id
    @GeneratedValue(generator = "dataset_pk_seq")
    @GenericGenerator(name = "dataset_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Basic
    @Column(name = "treatment_time")
    private double treatmentTime;

    @Basic
    @Column(name = "treatment_con")
    private double treatmentCon;

    @Basic
    @Column(name = "is_vivo")
    private boolean inVivo;

    @Basic
    @Column(name = "description", columnDefinition = "longtext")
    private String description;

    @Basic
    @Column(name = "comment", columnDefinition = "longtext")
    private String comment;

    @Basic
    @Column(name = "sample_chars", columnDefinition = "longtext")
    private String sampleChars;

    @OneToOne(targetEntity = IFNType.class)
    @JoinColumn(name = "ifn_type_id", referencedColumnName = "id", nullable = false)
    @Cascade({CascadeType.REFRESH})
    private IFNType ifnType;

    @OneToOne(targetEntity = IFNVariation.class)
    @JoinColumn(name = "ifn_var_id", referencedColumnName = "id", nullable = false)
    @Cascade({CascadeType.REFRESH})
    private IFNVariation ifnVar;

    @ManyToOne(targetEntity = Experiment.class)
    @JoinColumn(name = "experiment_id", referencedColumnName = "id", nullable = false)
    private Experiment experiment;

    @ManyToMany(targetEntity = ExperimentFactorValue.class)
    @JoinTable(name = "dataset_factorvalue", joinColumns = {@JoinColumn(name = "dataset_id",
            referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "factorvalue_id", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"dataset_id", "factorvalue_id"})})
    private List<ExperimentFactorValue> factorValues;

    @OneToMany(mappedBy = "dataset", targetEntity = Data.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.SAVE_UPDATE})
    private List<Data> data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(double treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public double getTreatmentCon() {
        return treatmentCon;
    }

    public void setTreatmentCon(double treatmentCon) {
        this.treatmentCon = treatmentCon;
    }

    public boolean isInVivo() {
        return inVivo;
    }

    public void setInVivo(boolean inVivo) {
        this.inVivo = inVivo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSampleChars() {
        return sampleChars;
    }

    public void setSampleChars(String sampleChars) {
        this.sampleChars = sampleChars;
    }

    public IFNType getIfnType() {
        return ifnType;
    }

    public void setIfnType(IFNType ifnType) {
        this.ifnType = ifnType;
    }

    public IFNVariation getIfnVar() {
        return ifnVar;
    }

    public void setIfnVar(IFNVariation ifnVar) {
        this.ifnVar = ifnVar;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public List<ExperimentFactorValue> getFactorValues() {
        return factorValues;
    }

    public void setFactorValues(List<ExperimentFactorValue> factorValues) {
        this.factorValues = factorValues;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
