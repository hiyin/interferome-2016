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


import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Experiment domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "experiment")
@org.hibernate.annotations.Table(appliesTo = "experiment",
        indexes = {@Index(name = "exp_name", columnNames = {"name"}),
                @Index(name = "exp_pubmed_id", columnNames = "pubmed_id"),
                @Index(name = "exp_unique_key", columnNames = "unique_key")
        })
public class Experiment extends Domain {

    @Id
    @GeneratedValue(generator = "exp_pk_seq")
    @GenericGenerator(name = "exp_pk_seq", strategy = "seqhilo")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "rawdatatype")
    private String rawDataType;

    @Column(name = "directory")
    private String directoy;

    @Column(name = "exp_bytes", scale = 11)
    private int expBytes;

    @Column(name = "base_owner")
    private String baseOwner;

    @Column(name = "description", columnDefinition = "longtext")
    private String description;

    @Column(name = "pubmed_id")
    private String pubMedId;

    @Column(name = "title", columnDefinition = "longtext")
    private String pubTitle;

    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "abstract", columnDefinition = "longtext")
    private String abstraction;

    @Column(name = "experiment_design", columnDefinition = "longtext")
    private String experimentDesign;

    @Column(name = "experiment_type", columnDefinition = "longtext")
    private String experimentType;

    @Column(name = "affiliations", columnDefinition = "longtext")
    private String affiliations;

    @Column(name = "authors", columnDefinition = "longtext")
    private String authors;

    @Column(name = "publication", columnDefinition = "longtext")
    private String publication;

    @Column(name = "approved")
    private boolean approved;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Date modifiedTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "owner_id")
    private User owner;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "modified_by_user")
    private User modifiedByUser;

    @OneToMany(mappedBy = "experiment", fetch = FetchType.LAZY, targetEntity = Permission.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private List<Permission> permissions = new ArrayList<Permission>();

    @Basic
    @Column(name = "persist_identifier")
    private String persistIdentifier;

    @Basic
    @Column(name = "unique_key")
    private String uniqueKey;

    @Basic
    @Column(name = "md_published")
    private boolean mdPublished;

    @ManyToMany(targetEntity = Activity.class)
    @JoinTable(name = "experiment_activity", joinColumns = {@JoinColumn(name = "experiment_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "activity_id", referencedColumnName = "id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {
            "experiment_id", "activity_id"})})
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Activity> activities = new ArrayList<Activity>();

    @ManyToMany(targetEntity = Party.class)
    @JoinTable(name = "experiment_party", joinColumns = {@JoinColumn(name = "experiment_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "party_id", referencedColumnName = "id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {
            "experiment_id", "party_id"})})
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Party> parties = new ArrayList<Party>();

    @OneToOne(mappedBy = "experiment", targetEntity = Licence.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE})
    private Licence licence;

    @OneToMany(mappedBy = "experiment", targetEntity = Dataset.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE})
    private List<Dataset> datasets;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the entryDate
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @param entryDate the entryDate to set
     */
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * @return the rawDataType
     */
    public String getRawDataType() {
        return rawDataType;
    }

    /**
     * @param rawDataType the rawDataType to set
     */
    public void setRawDataType(String rawDataType) {
        this.rawDataType = rawDataType;
    }

    /**
     * @return the directoy
     */
    public String getDirectoy() {
        return directoy;
    }

    /**
     * @param directoy the directoy to set
     */
    public void setDirectoy(String directoy) {
        this.directoy = directoy;
    }

    /**
     * @return the expBytes
     */
    public int getExpBytes() {
        return expBytes;
    }

    /**
     * @param expBytes the expBytes to set
     */
    public void setExpBytes(int expBytes) {
        this.expBytes = expBytes;
    }

    /**
     * @return the baseOwner
     */
    public String getBaseOwner() {
        return baseOwner;
    }

    /**
     * @param baseOwner the baseOwner to set
     */
    public void setBaseOwner(String baseOwner) {
        this.baseOwner = baseOwner;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the pubMedId
     */
    public String getPubMedId() {
        return pubMedId;
    }

    /**
     * @param pubMedId the pubMedId to set
     */
    public void setPubMedId(String pubMedId) {
        this.pubMedId = pubMedId;
    }

    /**
     * @return the pubTitle
     */
    public String getPubTitle() {
        return pubTitle;
    }

    /**
     * @param pubTitle the pubTitle to set
     */
    public void setPubTitle(String pubTitle) {
        this.pubTitle = pubTitle;
    }

    /**
     * @return the publicationDate
     */
    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * @param publicationDate the publicationDate to set
     */
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * @return the abstraction
     */
    public String getAbstraction() {
        return abstraction;
    }

    /**
     * @param abstraction the abstraction to set
     */
    public void setAbstraction(String abstraction) {
        this.abstraction = abstraction;
    }

    /**
     * @return the experimentDesign
     */
    public String getExperimentDesign() {
        return experimentDesign;
    }

    /**
     * @param experimentDesign the experimentDesign to set
     */
    public void setExperimentDesign(String experimentDesign) {
        this.experimentDesign = experimentDesign;
    }

    /**
     * @return the experimentType
     */
    public String getExperimentType() {
        return experimentType;
    }

    /**
     * @param experimentType the experimentType to set
     */
    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

    /**
     * @return the affiliations
     */
    public String getAffiliations() {
        return affiliations;
    }

    /**
     * @param affiliations the affiliations to set
     */
    public void setAffiliations(String affiliations) {
        this.affiliations = affiliations;
    }

    /**
     * @return the authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * @return the publication
     */
    public String getPublication() {
        return publication;
    }

    /**
     * @param publication the publication to set
     */
    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * @return the approved
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * @param approved the approved to set
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the modifiedTime
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * @param modifiedTime the modifiedTime to set
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * @return the modifiedByUser
     */
    public User getModifiedByUser() {
        return modifiedByUser;
    }

    /**
     * @param modifiedByUser the modifiedByUser to set
     */
    public void setModifiedByUser(User modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    /**
     * @return the permissions
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getPersistIdentifier() {
        return persistIdentifier;
    }

    public void setPersistIdentifier(String persistIdentifier) {
        this.persistIdentifier = persistIdentifier;
    }

    /**
     * @return the uniqueKey
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * @param uniqueKey the uniqueKey to set
     */
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
     * @return the mdPublished
     */
    public boolean isMdPublished() {
        return mdPublished;
    }

    /**
     * @param mdPublished the mdPublished to set
     */
    public void setMdPublished(boolean mdPublished) {
        this.mdPublished = mdPublished;
    }

    /**
     * @return the activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    /**
     * @return the parties
     */
    public List<Party> getParties() {
        return parties;
    }

    /**
     * @param parties the parties to set
     */
    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    /**
     * @return the licence
     */
    public Licence getLicence() {
        return licence;
    }

    /**
     * @param licence the licence to set
     */
    public void setLicence(Licence licence) {
        this.licence = licence;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }
}
