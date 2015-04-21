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

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/**
 * Permission domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "permission")
public class Permission extends Domain {
    @Id
    @GeneratedValue(generator = "perm_pk_seq")
    @GenericGenerator(name = "perm_pk_seq", strategy = "seqhilo")
    @Column(name = "id", length = 20)
    private long id;

    @Basic
    @Column(name = "name", length = 200)
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "view_allowed")
    private boolean viewAllowed;

    @Basic
    @Column(name = "update_allowed")
    private boolean updateAllowed;

    @Basic
    @Column(name = "import_allowed")
    private boolean importAllowed;

    @Basic
    @Column(name = "export_allowed")
    private boolean exportAllowed;

    @Basic
    @Column(name = "delete_allowed")
    private boolean deleteAllowed;

    @Basic
    @Column(name = "change_perm_allowed")
    private boolean changePermAllowed;

    @Basic
    @Column(name = "mdregister_allowed")
    private boolean mdregisterAllowed;

    @Basic
    @Column(name = "perm_type", nullable = false)
    private String permType;

    @ManyToOne(targetEntity = Experiment.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "experiment_id")
    private Experiment experiment;

    @ManyToOne(targetEntity = User.class)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "perm_for_user_id")
    private User permForUser;

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
     * @return the viewAllowed
     */
    public boolean isViewAllowed() {
        return viewAllowed;
    }

    /**
     * @param viewAllowed the viewAllowed to set
     */
    public void setViewAllowed(boolean viewAllowed) {
        this.viewAllowed = viewAllowed;
    }

    /**
     * @return the updateAllowed
     */
    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    /**
     * @param updateAllowed the updateAllowed to set
     */
    public void setUpdateAllowed(boolean updateAllowed) {
        this.updateAllowed = updateAllowed;
    }

    /**
     * @return the importAllowed
     */
    public boolean isImportAllowed() {
        return importAllowed;
    }

    /**
     * @param importAllowed the importAllowed to set
     */
    public void setImportAllowed(boolean importAllowed) {
        this.importAllowed = importAllowed;
    }

    /**
     * @return the exportAllowed
     */
    public boolean isExportAllowed() {
        return exportAllowed;
    }

    /**
     * @param exportAllowed the exportAllowed to set
     */
    public void setExportAllowed(boolean exportAllowed) {
        this.exportAllowed = exportAllowed;
    }

    /**
     * @return the deleteAllowed
     */
    public boolean isDeleteAllowed() {
        return deleteAllowed;
    }

    /**
     * @param deleteAllowed the deleteAllowed to set
     */
    public void setDeleteAllowed(boolean deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
    }

    /**
     * @return the changePermAllowed
     */
    public boolean isChangePermAllowed() {
        return changePermAllowed;
    }

    /**
     * @param changePermAllowed the changePermAllowed to set
     */
    public void setChangePermAllowed(boolean changePermAllowed) {
        this.changePermAllowed = changePermAllowed;
    }

    /**
     * @return the mdregisterAllowed
     */
    public boolean isMdregisterAllowed() {
        return mdregisterAllowed;
    }

    /**
     * @param mdregisterAllowed the mdregisterAllowed to set
     */
    public void setMdregisterAllowed(boolean mdregisterAllowed) {
        this.mdregisterAllowed = mdregisterAllowed;
    }

    /**
     * @return the permType
     */
    public String getPermType() {
        return permType;
    }

    /**
     * @param permType the permType to set
     */
    public void setPermType(String permType) {
        this.permType = permType;
    }

    /**
     * @return the experiment
     */
    public Experiment getExperiment() {
        return experiment;
    }

    /**
     * @param experiment the experiment to set
     */
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     * @return the permForUser
     */
    public User getPermForUser() {
        return permForUser;
    }

    /**
     * @param permForUser the permForUser to set
     */
    public void setPermForUser(User permForUser) {
        this.permForUser = permForUser;
    }

    @Transient
    public boolean isNonePerm() {
        if (!this.viewAllowed && !this.updateAllowed && !this.importAllowed && !this.exportAllowed && !this.deleteAllowed && !this.changePermAllowed
                && !this.mdregisterAllowed) {
            return true;
        } else {
            return false;
        }
    }
}
