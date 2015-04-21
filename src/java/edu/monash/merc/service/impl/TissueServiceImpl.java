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

import edu.monash.merc.dao.impl.TissueDAO;
import edu.monash.merc.domain.Tissue;
import edu.monash.merc.service.TissueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12/02/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Scope("prototype")
@Service
@Transactional
public class TissueServiceImpl implements TissueService {

    @Autowired
    private TissueDAO tissueDao;



    public void setTissueDao(TissueDAO tissueDao) {
        this.tissueDao = tissueDao;
    }

    @Override
    public Tissue getTissueById(long id) {
        return this.tissueDao.get(id);
    }

    @Override
    public void saveTissue(Tissue tissue) {
        this.tissueDao.add(tissue);
    }

    @Override
    public int saveTissue(List<Tissue> tissue) {
        return this.tissueDao.saveAll(tissue);
    }

    @Override
    public void updateTissue(Tissue tissue) {
        this.tissueDao.update(tissue);
    }

    @Override
    public void mergeTissue(Tissue tissue) {
        this.tissueDao.merge(tissue);
    }

    @Override
    public int updateTissue(List<Tissue> tissue) {
        return this.tissueDao.updateAll(tissue);
    }

    @Override
    public void deleteTissue(Tissue tissue) {
        this.tissueDao.remove(tissue);
    }

    @Override
    public Tissue getTissueByName(String tissueId) {
        return this.tissueDao.getTissueByName(tissueId);
    }


}
