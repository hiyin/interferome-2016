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
import edu.monash.merc.domain.Party;
import edu.monash.merc.repository.IPartyRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyDAO class which provides data access functionality for Party domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class PartyDAO extends HibernateGenericDAO<Party> implements IPartyRepository {

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#getPartyByPartyKey(java.lang.String)
      */
    @Override
    public Party getPartyByPartyKey(String partyKey) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("partyKey", partyKey));
        return (Party) criteria.uniqueResult();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#getPartyByUsrNameAndEmail(java.lang.String, java.lang.String, java.lang.String)
      */
    @Override
    public Party getPartyByUsrNameAndEmail(String firstName, String lastName, String email) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("personGivenName", firstName)).add(Restrictions.eq("personFamilyName", lastName)).add(Restrictions.eq("email", email));
        return (Party) criteria.uniqueResult();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#getAllParties()
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Party> getAllParties() {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.setComment("PartyDAO.getAllParties()");
        return criteria.list();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#getPartiesByExpId(long)
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Party> getPartiesByExpId(long expId) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        // create a alias for collections
        Criteria expCrit = criteria.createAlias("experiments", "exps");
        expCrit.add(Restrictions.eq("exps.id", expId));
        return criteria.list();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#deletePartyById(long)
      */
    @Override
    public void deletePartyById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS pty WHERE pty.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.repository.IPartyRepository#deletePartyByPartyKey(java.lang.String)
      */
    @Override
    public void deletePartyByPartyKey(String partyKey) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS pty WHERE pty.partyKey = :partyKey";
        Query query = this.session().createQuery(del_hql);
        query.setString("partyKey", partyKey);
        query.executeUpdate();
    }

}
