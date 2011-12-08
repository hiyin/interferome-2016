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

import edu.monash.merc.dao.impl.PermissionRequestDAO;
import edu.monash.merc.domain.PermissionRequest;
import edu.monash.merc.service.PermissionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PermissionRequestService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

@Scope("prototype")
@Service
@Transactional
public class PermissionRequestServiceImpl implements PermissionRequestService {

    @Autowired
    private PermissionRequestDAO permRequestDao;

    public void setPermRequestDao(PermissionRequestDAO permRequestDao) {
        this.permRequestDao = permRequestDao;
    }

    @Override
    public void savePermissionRequest(PermissionRequest permRequest) {
        this.permRequestDao.add(permRequest);
    }

    @Override
    public PermissionRequest getPermissionRequestById(long id) {
        return this.permRequestDao.get(id);
    }

    @Override
    public void updatePermissionRequest(PermissionRequest permRequest) {
        this.permRequestDao.update(permRequest);
    }

    @Override
    public void deletePermissionRequest(PermissionRequest permRequest) {
        this.permRequestDao.remove(permRequest);
    }

    @Override
    public List<PermissionRequest> getPermissionRequestsByOwner(long ownerId) {
        return this.permRequestDao.getPermissionRequestsByOwner(ownerId);
    }

    @Override
    public void deletePermissionRequestsByExpId(long expId) {
        this.permRequestDao.deletePermissionRequestsByExpId(expId);
    }

    @Override
    public void deletePermissionRequestById(long pmReqId) {
        this.permRequestDao.deletePermissionRequestById(pmReqId);
    }

    @Override
    public PermissionRequest getExpPermissionRequestByReqUser(long expId, long reqUserId) {
        return this.permRequestDao.getExpPermissionRequestByReqUser(expId, reqUserId);
    }

    @Override
    public long getTotalPermRequests(long ownerId) {
        return this.permRequestDao.getTotalPermRequests(ownerId);
    }
}
