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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Licence domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "licence")
public class Licence extends Domain {

	@Id
	@GeneratedValue(generator = "licence_pk_seq")
	@GenericGenerator(name = "licence_pk_seq", strategy = "seqhilo")
	@Column(name = "id", nullable = false)
	private long id;

	@Basic
	@Column(name = "type", length = 50)
	private String licenceType;

	@Basic
	@Column(name = "commercial", length = 10)
	private String commercial;

	@Basic
	@Column(name = "derivaties", length = 10)
	private String derivatives;

	@Basic
	@Column(name = "jurisdiction", length = 20)
	private String jurisdiction;

	@Basic
	@Column(name = "contents", length = 2000)
	private String licenceContents;

	@OneToOne(targetEntity = Experiment.class)
	@JoinColumn(name = "experiment_id", referencedColumnName = "id", nullable = false)
	private Experiment experiment;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the licenceType
	 */
	public String getLicenceType() {
		return licenceType;
	}

	/**
	 * @param licenceType
	 *            the licenceType to set
	 */
	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}

	/**
	 * @return the commercial
	 */
	public String getCommercial() {
		return commercial;
	}

	/**
	 * @param commercial
	 *            the commercial to set
	 */
	public void setCommercial(String commercial) {
		this.commercial = commercial;
	}

	/**
	 * @return the derivatives
	 */
	public String getDerivatives() {
		return derivatives;
	}

	/**
	 * @param derivatives
	 *            the derivatives to set
	 */
	public void setDerivatives(String derivatives) {
		this.derivatives = derivatives;
	}

	/**
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * @param jurisdiction
	 *            the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * @return the licenceContents
	 */
	public String getLicenceContents() {
		return licenceContents;
	}

	/**
	 * @param licenceContents
	 *            the licenceContents to set
	 */
	public void setLicenceContents(String licenceContents) {
		this.licenceContents = licenceContents;
	}

	/**
	 * @return the experiment
	 */
	public Experiment getExperiment() {
		return experiment;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
