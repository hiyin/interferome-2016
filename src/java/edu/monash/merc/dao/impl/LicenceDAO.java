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
import edu.monash.merc.domain.Licence;
import edu.monash.merc.repository.ILicenceRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * LicenceDAO class which provides data access functionality for Licence domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class LicenceDAO extends HibernateGenericDAO<Licence> implements ILicenceRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.ILicenceRepository#getLicenceByExpId(long)
	 */
	@Override
	public Licence getLicenceByExpId(long expId) {
		Criteria licenceCriteria = this.session().createCriteria(this.persistClass);
		Criteria expCriteria = licenceCriteria.createCriteria("experiment");
		expCriteria.add(Restrictions.eq("id", expId));
		return (Licence) licenceCriteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.repository.ILicenceRepository#deleteLicenceById(long)
	 */
	@Override
	public void deleteLicenceById(long id) {
		String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS licence WHERE licence.id = :id";
		Query query = this.session().createQuery(del_hql);
		query.setLong("id", id);
		query.executeUpdate();
	}

}
