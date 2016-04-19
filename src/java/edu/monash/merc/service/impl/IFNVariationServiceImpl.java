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

import edu.monash.merc.dao.impl.IFNVariationDAO;
import edu.monash.merc.domain.IFNVariation;
import edu.monash.merc.service.IFNVariationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IFNVariationService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class IFNVariationServiceImpl implements IFNVariationService {

    @Autowired
    private IFNVariationDAO ifnVariationDao;

    public void setIfnVariationDao(IFNVariationDAO ifnVariationDao) {
        this.ifnVariationDao = ifnVariationDao;
    }

    @Override
    public void saveIFNVariation(IFNVariation ifnVariation) {
        this.ifnVariationDao.add(ifnVariation);
    }

    @Override
    public IFNVariation getIFNVariationById(long id) {
        return this.ifnVariationDao.get(id);
    }

    @Override
    public IFNVariation getIFNVariation(boolean isAbnormal, String value) {
        return this.ifnVariationDao.getIFNVariation(isAbnormal, value);
    }

    @Override
    public void updateIFNVariation(IFNVariation ifnVariation) {
        this.ifnVariationDao.update(ifnVariation);
    }

    @Override
    public void deleteIFNVariation(IFNVariation ifnVariation) {
        this.ifnVariationDao.remove(ifnVariation);
    }

    @Override
    public List<String> getAbnormalFactors() {
        return this.ifnVariationDao.getAbnormalFactors();
    }
}
