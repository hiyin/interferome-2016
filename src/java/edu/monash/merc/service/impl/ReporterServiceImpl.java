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

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.impl.ReporterDAO;
import edu.monash.merc.domain.Reporter;
import edu.monash.merc.service.ReporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ReporterService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class ReporterServiceImpl implements ReporterService {

    @Autowired
    private ReporterDAO reporterDao;

    public void setReporterDao(ReporterDAO reporterDao) {
        this.reporterDao = reporterDao;
    }

    @Override
    public Reporter getReporterById(long id) {
        return this.reporterDao.get(id);
    }

    @Override
    public void saveReporter(Reporter reporter) {
        this.reporterDao.add(reporter);
    }

    @Override
    public int saveReporters(List<Reporter> reporters) {
        return this.reporterDao.saveAll(reporters);
    }

    @Override
    public void updateReporter(Reporter reporter) {
        this.reporterDao.update(reporter);
    }

    @Override
    public void mergeReporter(Reporter reporter) {
        this.reporterDao.merge(reporter);
    }

    @Override
    public int updateReporters(List<Reporter> reporters) {
        return this.reporterDao.updateAll(reporters);
    }

    @Override
    public void deleteReporter(Reporter reporter) {
        this.reporterDao.remove(reporter);
    }

    @Override
    public Pagination<Reporter> getReporters(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.reporterDao.getReporters(startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public Reporter getReporterByProbeId(String probeId) {
        return this.reporterDao.getReporterByProbeId(probeId);
    }
}
