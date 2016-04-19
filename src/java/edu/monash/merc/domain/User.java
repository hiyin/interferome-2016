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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * User domain class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Entity
@Table(name = "user")
public class User extends Domain {

	@Id
	@GeneratedValue(generator = "user_pk_seq")
	@GenericGenerator(name = "user_pk_seq", strategy = "seqhilo")
	@Column(name = "id", nullable = false)
	private long id;

	@Basic
	@Column(name = "unique_id", length = 100)
	private String uniqueId;

	@Basic
	@Column(name = "uid_hashcode", length = 100)
	private String uidHashCode;

	@Basic
	@Column(name = "first_name", length = 25)
	private String firstName;

	@Basic
	@Column(name = "last_name", length = 25)
	private String lastName;

	@Basic
	@Column(name = "display_name", length = 55)
	private String displayName;

	@Basic
	@Column(name = "password", length = 50)
	private String password;

	@Basic
	@Column(name = "email", length = 100)
	private String email;

	@Basic
	@Column(name = "activation_hash_code", length = 50)
	private String activationHashCode;

	@Basic
	@Column(name = "is_activated")
	private boolean isActivated;

	@Basic
	@Column(name = "is_rejected")
	private boolean isRejected;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registed_date")
	private Date registedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "activated_date")
	private Date activatedDate;

	@Basic
	@Column(name = "reset_password_code", length = 50)
	private String resetPasswdHashCode;

	@OneToOne(mappedBy = "user", targetEntity = Profile.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Profile profile;

	@OneToOne(mappedBy = "user", targetEntity = Avatar.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Avatar avatar;

	@Column(name = "user_type", columnDefinition = "integer", nullable = false)
	private int userType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUidHashCode() {
		return uidHashCode;
	}

	public void setUidHashCode(String uidHashCode) {
		this.uidHashCode = uidHashCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationHashCode() {
		return activationHashCode;
	}

	public void setActivationHashCode(String activationHashCode) {
		this.activationHashCode = activationHashCode;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public boolean isRejected() {
		return isRejected;
	}

	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}

	public Date getRegistedDate() {
		return registedDate;
	}

	public void setRegistedDate(Date registedDate) {
		this.registedDate = registedDate;
	}

	public Date getActivatedDate() {
		return activatedDate;
	}

	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}

	public String getResetPasswdHashCode() {
		return resetPasswdHashCode;
	}

	public void setResetPasswdHashCode(String resetPasswdHashCode) {
		this.resetPasswdHashCode = resetPasswdHashCode;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
