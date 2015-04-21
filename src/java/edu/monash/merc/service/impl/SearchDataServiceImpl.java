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
import edu.monash.merc.common.results.SearchResultRow;
import edu.monash.merc.dao.impl.SearchDataDAO;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.dto.GeneExpressionRecord;
import edu.monash.merc.dto.SearchBean;
import edu.monash.merc.service.SearchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SearchDataService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class SearchDataServiceImpl implements SearchDataService {

    @Autowired
    private SearchDataDAO searchDataDao;

    public void setSearchDataDao(SearchDataDAO searchDataDao) {
        this.searchDataDao = searchDataDao;
    }

    @Override
    public Pagination<SearchResultRow> search(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.search(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public Pagination<Probe> searchProbes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchProbes(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public Pagination<Gene> searchGenes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchGenes(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public List<Object[]> searchChromosome(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchChromosome(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public List<GeneExpressionRecord> searchTissueExpression(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchTissueExpression(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public List<Gene> searchChromosomeGeneList(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchChromosomeGeneList(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }



    @Override
    public List<Object[]> searchTFSite(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchTFSite(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

    @Override
    public List<List<Object[]>> searchOntology(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchOntology(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }


    @Override
    public Object[] searchSubtypes(SearchBean searchBean, int startPageNo, int recordPerPage, String orderBy, String sortBy) {
        return this.searchDataDao.searchSubtypes(searchBean, startPageNo, recordPerPage, orderBy, sortBy);
    }

}
