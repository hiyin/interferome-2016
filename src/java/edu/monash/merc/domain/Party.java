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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Party domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "party")
public class Party extends Domain {

	@Id
	@GeneratedValue(generator = "party_pk_seq")
	@GenericGenerator(name = "party_pk_seq", strategy = "seqhilo")
	@Column(name = "id", nullable = false)
	private long id;

	@Basic
	@Column(name = "party_key")
	private String partyKey;

	@Basic
	@Column(name = "group_name")
	private String groupName;

	@Basic
	@Column(name = "originate_src_type")
	private String originateSourceType;

	@Basic
	@Column(name = "originate_src_value")
	private String originateSourceValue;

	@Basic
	@Column(name = "identifier_type")
	private String identifierType;

	@Basic
	@Column(name = "identifier_value")
	private String identifierValue;

	@Basic
	@Column(name = "person_title")
	private String personTitle;

	@Basic
	@Column(name = "given_name")
	private String personGivenName;

	@Basic
	@Column(name = "family_name")
	private String personFamilyName;

	@Basic
	@Column(name = "url")
	private String url;

	@Basic
	@Column(name = "email")
	private String email;

	@Basic
	@Column(name = "address")
	private String address;

	@Basic
	@Column(name = "from_rm")
	private boolean fromRm;

	@ManyToMany(mappedBy = "parties")
	private List<Experiment> experiments;

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
	 * @return the partyKey
	 */
	public String getPartyKey() {
		return partyKey;
	}

	/**
	 * @param partyKey
	 *            the partyKey to set
	 */
	public void setPartyKey(String partyKey) {
		this.partyKey = partyKey;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the originateSourceType
	 */
	public String getOriginateSourceType() {
		return originateSourceType;
	}

	/**
	 * @param originateSourceType
	 *            the originateSourceType to set
	 */
	public void setOriginateSourceType(String originateSourceType) {
		this.originateSourceType = originateSourceType;
	}

	/**
	 * @return the originateSourceValue
	 */
	public String getOriginateSourceValue() {
		return originateSourceValue;
	}

	/**
	 * @param originateSourceValue
	 *            the originateSourceValue to set
	 */
	public void setOriginateSourceValue(String originateSourceValue) {
		this.originateSourceValue = originateSourceValue;
	}

	/**
	 * @return the identifierType
	 */
	public String getIdentifierType() {
		return identifierType;
	}

	/**
	 * @param identifierType
	 *            the identifierType to set
	 */
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	/**
	 * @return the identifierValue
	 */
	public String getIdentifierValue() {
		return identifierValue;
	}

	/**
	 * @param identifierValue
	 *            the identifierValue to set
	 */
	public void setIdentifierValue(String identifierValue) {
		this.identifierValue = identifierValue;
	}

	/**
	 * @return the personTitle
	 */
	public String getPersonTitle() {
		return personTitle;
	}

	/**
	 * @param personTitle
	 *            the personTitle to set
	 */
	public void setPersonTitle(String personTitle) {
		this.personTitle = personTitle;
	}

	/**
	 * @return the personGivenName
	 */
	public String getPersonGivenName() {
		return personGivenName;
	}

	/**
	 * @param personGivenName
	 *            the personGivenName to set
	 */
	public void setPersonGivenName(String personGivenName) {
		this.personGivenName = personGivenName;
	}

	/**
	 * @return the personFamilyName
	 */
	public String getPersonFamilyName() {
		return personFamilyName;
	}

	/**
	 * @param personFamilyName
	 *            the personFamilyName to set
	 */
	public void setPersonFamilyName(String personFamilyName) {
		this.personFamilyName = personFamilyName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the fromRm
	 */
	public boolean isFromRm() {
		return fromRm;
	}

	/**
	 * @param fromRm
	 *            the fromRm to set
	 */
	public void setFromRm(boolean fromRm) {
		this.fromRm = fromRm;
	}

	/**
	 * @return the experiments
	 */
	public List<Experiment> getExperiments() {
		return experiments;
	}

	/**
	 * @param experiments
	 *            the experiments to set
	 */
	public void setExperiments(List<Experiment> experiments) {
		this.experiments = experiments;
	}
}
