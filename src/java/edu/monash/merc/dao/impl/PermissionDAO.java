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
package edu.monash.merc.dao.impl;

import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.PermType;
import edu.monash.merc.domain.Permission;
import edu.monash.merc.repository.IPermissionRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PermissionDAO class which provides data access functionality for Permission domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class PermissionDAO extends HibernateGenericDAO<Permission> implements IPermissionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#getExpPermsByUsrIdExpId(long, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getExpPermsByUsrIdExpId(long permForUsrId, long expId) {
		Criteria permCriteria = this.session().createCriteria(this.persistClass);
		// create a alias for criteria
		permCriteria.createAlias("permForUser", "permUser");
		Criterion permtype1 = Restrictions.eq("permType", PermType.ALLREGUSER.code());
		Criterion permtype2 = Restrictions.eq("permType", PermType.ANONYMOUS.code());
		Criterion permuser = Restrictions.eq("permUser.id", permForUsrId);

		// create a disjunction (or), then add the restrictions
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(permtype1);
		disjunction.add(permtype2);
		disjunction.add(permuser);
		permCriteria.add(disjunction);

		Criteria expCriteria = permCriteria.createCriteria("experiment");
		expCriteria.add(Restrictions.eq("id", expId));
		return permCriteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#getAnonymousPermsByExpId(long)
	 */
	@Override
	public Permission getAnonymousPermsByExpId(long expId) {
		Criteria permCriteria = this.session().createCriteria(this.persistClass);
		permCriteria.add(Restrictions.eq("permType", PermType.ANONYMOUS.code()));
		Criteria expCriteria = permCriteria.createCriteria("experiment");
		expCriteria.add(Restrictions.eq("id", expId));
		return (Permission) permCriteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#getExpDefaultPerms(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getExpDefaultPerms(long expId) {
		Criteria permCriteria = this.session().createCriteria(this.persistClass);
		permCriteria.add(Restrictions.disjunction().add(Restrictions.eq("permType", PermType.ALLREGUSER.code()))
				.add(Restrictions.eq("permType", PermType.ANONYMOUS.code())));
		Criteria expCriteria = permCriteria.createCriteria("experiment");
		expCriteria.add(Restrictions.eq("id", expId));
		return permCriteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#getExpPermsByExpId(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getExpPermsByExpId(long expId) {
		Criteria permCriteria = this.session().createCriteria(this.persistClass);
		Criteria expCriteria = permCriteria.createCriteria("experiment");
		expCriteria.add(Restrictions.eq("id", expId));
		return permCriteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#deletePermByPermId(long)
	 */
	@Override
	public void deletePermByPermId(long permId) {
		String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS perm WHERE perm.id = :id";
		Query query = this.session().createQuery(del_hql);
		query.setLong("id", permId);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.IPermissionRepository#deleteAllPermsByExpId(long)
	 */
	@Override
	public void deleteAllPermsByExpId(long expId) {
		String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS perm WHERE perm.experiment.id = :id";
		Query query = this.session().createQuery(del_hql);
		query.setLong("id", expId);
		query.executeUpdate();
	}

}
