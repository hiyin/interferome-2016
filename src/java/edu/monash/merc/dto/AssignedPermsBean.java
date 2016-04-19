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
import java.util.ArrayList;
import java.util.List;

import edu.monash.merc.domain.Permission;

/**
 * AssignedPermsBean class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class AssignedPermsBean implements Serializable {

	private List<Permission> newPerms = new ArrayList<Permission>();

	private List<Permission> updatedPerms = new ArrayList<Permission>();

	private List<Long> deletedPermIds = new ArrayList<Long>();

	/**
	 * @return the newPerms
	 */
	public List<Permission> getNewPerms() {
		return newPerms;
	}

	/**
	 * @param newPerms
	 *            the newPerms to set
	 */
	public void setNewPerms(List<Permission> newPerms) {
		this.newPerms = newPerms;
	}

	/**
	 * @return the updatedPerms
	 */
	public List<Permission> getUpdatedPerms() {
		return updatedPerms;
	}

	/**
	 * @param updatedPerms
	 *            the updatedPerms to set
	 */
	public void setUpdatedPerms(List<Permission> updatedPerms) {
		this.updatedPerms = updatedPerms;
	}

	/**
	 * @return the deletedPermIds
	 */
	public List<Long> getDeletedPermIds() {
		return deletedPermIds;
	}

	/**
	 * @param deletedPermIds
	 *            the deletedPermIds to set
	 */
	public void setDeletedPermIds(List<Long> deletedPermIds) {
		this.deletedPermIds = deletedPermIds;
	}
}
