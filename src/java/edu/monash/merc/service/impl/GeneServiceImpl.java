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
import edu.monash.merc.dao.impl.GeneDAO;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.service.GeneService;
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
 *        Date: 26/06/12
 *        Time: 6:39 PM
 */

@Scope("prototype")
@Service
@Transactional
public class GeneServiceImpl implements GeneService {
    /**
     * {@inheritDoc}
     */
    @Autowired
    private GeneDAO geneDao;


    /**
     * {@inheritDoc}
     */
    public void setGeneDao(GeneDAO geneDao) {
        this.geneDao = geneDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Gene getGeneById(long id) {
        return this.geneDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGene(Gene gene) {
        this.geneDao.add(gene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeGene(Gene gene) {
        this.geneDao.merge(gene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGene(Gene gene) {
        this.geneDao.update(gene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGene(Gene gene) {
        this.geneDao.remove(gene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Gene getGeneByEnsgAccession(String ensgAccession) {
        return this.geneDao.getGenesByEnsgAccession(ensgAccession);
    }

    @Override
    public List<Gene> getGenesByProbeId(String probeId) {
        return this.geneDao.getGenesByProbeId(probeId);
   }

    @Override
    public Pagination<Gene> getGenes(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        return this.geneDao.getGenes(startPageNo, recordsPerPage, orderBys);
    }
}
