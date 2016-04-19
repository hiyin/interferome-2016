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

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.impl.UserDAO;
import edu.monash.merc.domain.User;
import edu.monash.merc.service.UserService;

/**
 * UserService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDao;

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) {
		return this.userDao.getUserByEmail(email);
	}

	@Override
	public User getUserByNameEmail(String firstName, String lastName, String email) {
		return this.userDao.getUserByNameEmail(firstName, lastName, email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#ggetUserByUnigueId(java.lang.String)
	 */
	@Override
	public User getUserByUnigueId(String uniqueId) {
		return this.userDao.getUserByUnigueId(uniqueId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#saveUser(edu.monash.merc.interferome.domain.User)
	 */
	@Override
	public void saveUser(User user) {
		this.userDao.add(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#getUserById(long)
	 */
	@Override
	public User getUserById(long id) {
		return this.userDao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#updateUser(edu.monash.merc.interferome.domain.User)
	 */
	@Override
	public void updateUser(User user) {
		this.userDao.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#deleteUser(edu.monash.merc.interferome.domain.User)
	 */
	@Override
	public void deleteUser(User user) {
		this.userDao.remove(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#checkUserUniqueIdExisted(java.lang.String)
	 */
	@Override
	public boolean checkUserUniqueIdExisted(String uniqueId) {
		return this.userDao.checkUserUniqueIdExisted(uniqueId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#checkUserDisplayNameExisted(java.lang.String)
	 */
	@Override
	public boolean checkUserDisplayNameExisted(String userName) {
		return this.userDao.checkUserDisplayNameExisted(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.interferome.service.UserService#checkEmailExisted(java.lang.String)
	 */
	@Override
	public boolean checkEmailExisted(String email) {
		return this.userDao.checkEmailExisted(email);
	}

	@Override
	public User getVirtualUser(int userType) {
		return this.userDao.getVirtualUser(userType);
	}

	@Override
	public User checkUserLogin(String uniqueId, String password) {
		return this.userDao.checkUserLogin(uniqueId, password);
	}

	@Override
	public Pagination<User> getAllUsers(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		return this.userDao.getAllUsers(startPageNo, recordsPerPage, orderBys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.UserService#getAllActiveUsers()
	 */
	@Override
	public List<User> getAllActiveUsers() {
		return this.userDao.getAllActiveUsers();
	}
}
