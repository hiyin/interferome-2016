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

import edu.monash.merc.dao.impl.FactorValueDAO;
import edu.monash.merc.domain.ExperimentFactorValue;
import edu.monash.merc.dto.NameValueBean;
import edu.monash.merc.service.FactorValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FactorValueService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class FactorValueServiceImpl implements FactorValueService {

    @Autowired
    private FactorValueDAO factorValueDao;

    public void setFactorValueDao(FactorValueDAO factorValueDao) {
        this.factorValueDao = factorValueDao;
    }

    @Override
    public ExperimentFactorValue getFactorValueByNameValuePair(String fname, String fvalue) {
        return this.factorValueDao.getFactorValueByNameValuePair(fname, fvalue);
    }

    @Override
    public List<ExperimentFactorValue> getFactorValuesByDatasetId(long dsId) {
        return this.factorValueDao.getFactorValuesByDatasetId(dsId);
    }

    @Override
    public List<NameValueBean> getFactorValuesBeanByDatasetId(long dsId) {
        return this.factorValueDao.getFactorValuesBeanByDatasetId(dsId);
    }

    @Override
    public void saveFactorValue(ExperimentFactorValue factorValue) {
        this.factorValueDao.add(factorValue);
    }

    @Override
    public void updateFactorValue(ExperimentFactorValue factorValue) {
        this.factorValueDao.update(factorValue);
    }

    @Override
    public ExperimentFactorValue getFactorValueById(long factorValueId) {
        return this.factorValueDao.get(factorValueId);
    }

    @Override
    public void deleteFactorValue(ExperimentFactorValue factorValue) {
        this.factorValueDao.remove(factorValue);
    }

    @Override
    public List<ExperimentFactorValue> getFactorValuesByFactorId(long factorId) {
        return this.factorValueDao.getFactorValuesByFactorId(factorId);
    }

    @Override
    public List<String> getFactorValuesByFactorName(String factorName) {
        return this.factorValueDao.getFactorValuesByFactorName(factorName);
    }
}
