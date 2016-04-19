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
package edu.monash.merc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.monash.merc.dao.impl.PermissionDAO;
import edu.monash.merc.domain.Permission;
import edu.monash.merc.service.PermissionService;

/**
 * PermissionService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDAO perimssionDao;

	/**
	 * @param perimssionDao
	 *            the perimssionDao to set
	 */
	public void setPerimssionDao(PermissionDAO perimssionDao) {
		this.perimssionDao = perimssionDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#savePermission(edu.monash.merc.domain.Permission)
	 */
	@Override
	public void savePermission(Permission permission) {
		this.perimssionDao.add(permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#getPermissionById(long)
	 */
	@Override
	public Permission getPermissionById(long id) {
		return this.perimssionDao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#updatePermission(edu.monash.merc.domain.Permission)
	 */
	@Override
	public void updatePermission(Permission permission) {
		this.perimssionDao.update(permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#deletePermission(edu.monash.merc.domain.Permission)
	 */
	@Override
	public void deletePermission(Permission permission) {
		this.perimssionDao.remove(permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#getExpPermsByUsrIdExpId(long, long)
	 */
	@Override
	public List<Permission> getExpPermsByUsrIdExpId(long permForUsrId, long expId) {
		return this.perimssionDao.getExpPermsByUsrIdExpId(permForUsrId, expId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#getAnonymousPermsByExpId(long)
	 */
	@Override
	public Permission getAnonymousPermsByExpId(long expId) {
		return this.perimssionDao.getAnonymousPermsByExpId(expId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#getExpDefaultPerms(long)
	 */
	@Override
	public List<Permission> getExpDefaultPerms(long expId) {
		return this.perimssionDao.getExpDefaultPerms(expId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#getExpPermsByExpId(long)
	 */
	@Override
	public List<Permission> getExpPermsByExpId(long expId) {
		return this.perimssionDao.getExpPermsByExpId(expId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#deletePermByPermId(long)
	 */
	@Override
	public void deletePermByPermId(long permId) {
		this.perimssionDao.deletePermByPermId(permId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.PermissionService#deleteAllPermsByExpId(long)
	 */
	@Override
	public void deleteAllPermsByExpId(long expId) {
		this.perimssionDao.deleteAllPermsByExpId(expId);
	}
}
