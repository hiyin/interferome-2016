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
package edu.monash.merc.dto;

import java.io.Serializable;

/**
 * PermissionBean class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class PermissionBean implements Serializable {

	private long id;

	private long userId;

	private String name;

	private boolean viewAllowed;

	private boolean updateAllowed;

	private boolean importAllowed;

	private boolean exportAllowed;

	private boolean deleteAllowed;

	private boolean changePermAllowed;

	private boolean mdregisterAllowed;

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
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the viewAllowed
	 */
	public boolean isViewAllowed() {
		return viewAllowed;
	}

	/**
	 * @param viewAllowed
	 *            the viewAllowed to set
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
	 * @param updateAllowed
	 *            the updateAllowed to set
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
	 * @param importAllowed
	 *            the importAllowed to set
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
	 * @param exportAllowed
	 *            the exportAllowed to set
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
	 * @param deleteAllowed
	 *            the deleteAllowed to set
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
	 * @param changePermAllowed
	 *            the changePermAllowed to set
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
	 * @param mdregisterAllowed
	 *            the mdregisterAllowed to set
	 */
	public void setMdregisterAllowed(boolean mdregisterAllowed) {
		this.mdregisterAllowed = mdregisterAllowed;
	}

	public void setFullPermissions() {
		this.viewAllowed = true;
		this.updateAllowed = true;
		this.importAllowed = true;
		this.exportAllowed = true;
		this.deleteAllowed = true;
		this.changePermAllowed = true;
		this.mdregisterAllowed = true;
	}

	public boolean isNonePerm() {
		if (!this.viewAllowed && !this.updateAllowed && !this.importAllowed && !this.exportAllowed && !this.deleteAllowed && !this.changePermAllowed
				&& !this.mdregisterAllowed) {
			return true;
		} else {
			return false;
		}
	}

}
