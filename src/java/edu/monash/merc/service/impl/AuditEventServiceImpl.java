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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.impl.AuditEventDAO;
import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.service.AuditEventService;

/**
 * AuditEventService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class AuditEventServiceImpl implements AuditEventService {

	@Autowired
	private AuditEventDAO eventDAO;

	public void setEventDAO(AuditEventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#saveAuditEvent(edu.monash.merc.domain.AuditEvent)
	 */
	@Override
	public void saveAuditEvent(AuditEvent event) {
		this.eventDAO.add(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#updateAuditEvent(edu.monash.merc.domain.AuditEvent)
	 */
	@Override
	public void updateAuditEvent(AuditEvent event) {
		this.eventDAO.update(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#deleteAuditEvent(edu.monash.merc.domain.AuditEvent)
	 */
	@Override
	public void deleteAuditEvent(AuditEvent event) {
		this.eventDAO.remove(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#deleteAuditEventById(long)
	 */
	@Override
	public void deleteAuditEventById(long eId) {
		this.eventDAO.deleteAuditEventById(eId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#deleteEventByIdWithUserId(long, long)
	 */
	@Override
	public void deleteEventByIdWithUserId(long eId, long userId) {
		this.eventDAO.deleteEventByIdWithUserId(eId, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#getAuditEventById(long)
	 */
	@Override
	public AuditEvent getAuditEventById(long eid) {
		return this.eventDAO.get(eid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.AuditEventService#getEventByUserId(long, int, int,
	 * edu.monash.merc.common.sql.OrderBy[])
	 */
	@Override
	public Pagination<AuditEvent> getEventByUserId(long uid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
		return this.eventDAO.getEventByUserId(uid, startPageNo, recordsPerPage, orderBys);
	}
}
