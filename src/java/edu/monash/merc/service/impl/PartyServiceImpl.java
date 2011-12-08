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

import edu.monash.merc.dao.impl.PartyDAO;
import edu.monash.merc.domain.Party;
import edu.monash.merc.service.PartyService;

/**
 * PartyService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class PartyServiceImpl implements PartyService {

    @Autowired
    private PartyDAO partyDao;

    /**
     * @param partyDao the partyDao to set
     */
    public void setPartyDao(PartyDAO partyDao) {
        this.partyDao = partyDao;
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#saveParty(edu.monash.merc.domain.Party)
      */
    @Override
    public void saveParty(Party party) {
        this.partyDao.add(party);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#getPartyById(long)
      */
    @Override
    public Party getPartyById(long id) {
        return this.partyDao.get(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#deleteParty(edu.monash.merc.domain.Party)
      */
    @Override
    public void deleteParty(Party party) {
        this.partyDao.remove(party);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#updateParty(edu.monash.merc.domain.Party)
      */
    @Override
    public void updateParty(Party party) {
        this.partyDao.update(party);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#getPartyByPartyKey(java.lang.String)
      */
    @Override
    public Party getPartyByPartyKey(String partyKey) {
        return this.partyDao.getPartyByPartyKey(partyKey);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#getPartyByUsrNameAndEmail(java.lang.String, java.lang.String, , java.lang.String)
      */
    @Override
    public Party getPartyByUsrNameAndEmail(String firstName, String lastName, String email) {
        return this.partyDao.getPartyByUsrNameAndEmail(firstName, lastName, email);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#getAllParties()
      */
    @Override
    public List<Party> getAllParties() {
        return this.partyDao.getAllParties();
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#getPartiesByExpId(long)
      */
    @Override
    public List<Party> getPartiesByExpId(long expId) {
        return this.partyDao.getPartiesByExpId(expId);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#deletePartyById(long)
      */
    @Override
    public void deletePartyById(long id) {
        this.partyDao.deletePartyById(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see edu.monash.merc.service.PartyService#deletePartyByPartyKey(java.lang.String)
      */
    @Override
    public void deletePartyByPartyKey(String partyKey) {
        this.partyDao.deletePartyByPartyKey(partyKey);
    }

}
