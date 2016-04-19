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
import edu.monash.merc.dao.impl.ProbeDAO;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.service.ProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 28/06/12
 *        Time: 2:53 PM
 */

@Scope("prototype")
@Service
@Transactional
public class ProbeServiceImpl implements ProbeService {

    @Autowired
    private ProbeDAO probeDao;

    public void setProbeDao(ProbeDAO probeDao) {
        this.probeDao = probeDao;
    }

    @Override
    public Probe getProbeById(long id) {
        return this.probeDao.get(id);
    }

    @Override
    public void saveProbe(Probe probe) {
        this.probeDao.add(probe);
    }

    @Override
    public int saveProbes(List<Probe> probes) {
        return this.probeDao.saveAll(probes);
    }

    @Override
    public int updateProbes(List<Probe> probes) {
        return this.probeDao.updateAll(probes);
    }

    @Override
    public void mergeProbe(Probe probe) {
        this.probeDao.merge(probe);
    }

    @Override
    public void updateProbe(Probe probe) {
        this.probeDao.update(probe);
    }

    @Override
    public void deleteProbe(Probe probe) {
        this.probeDao.remove(probe);
    }

    @Override
    public Probe getProbeByProbeId(String probeId) {
        return this.probeDao.getProbeByProbeId(probeId);
    }

    @Override
    public List<Probe> getProbesByGeneAccession(String geneAccession) {
        return this.probeDao.getProbesByGeneAccession(geneAccession);
    }

   @Override
    public List<Probe> getProbeBySpecies(String speciesName) {
        return this.probeDao.getProbeBySpecies(speciesName);
    }

    @Override
    public List<Probe> getProbesByGeneId(long geneId) {
       return this.probeDao.getProbesByGeneId(geneId);
    }

    @Override
    public Pagination<Probe> getProbes(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
       return this.probeDao.getProbes(startPageNo, recordsPerPage, orderBys);
    }
}
