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
import edu.monash.merc.domain.Species;
import edu.monash.merc.dao.impl.SpeciesDAO;
import edu.monash.merc.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: irinar
 * Date: 16/01/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */

@Scope("prototype")
@Service
@Transactional
public class SpeciesServiceImpl implements SpeciesService {


    @Autowired
     private SpeciesDAO speciesDao;

     public void setSpeciesDao(SpeciesDAO speciesDao) {
         this.speciesDao = speciesDao;
     }

     @Override
     public Species getSpeciesById(long id) {
         return this.speciesDao.get(id);
     }

     @Override
     public void saveSpecies(Species species) {
         this.speciesDao.add(species);
     }

     @Override
     public int saveSpecieses(List<Species> specieses) {
         return this.speciesDao.saveAll(specieses);
     }

     @Override
     public void updateSpecies(Species species) {
         this.speciesDao.update(species);
     }

     @Override
     public void mergeSpecies(Species species) {
         this.speciesDao.merge(species);
     }

     @Override
     public int updateSpecieses(List<Species> specieses) {
         return this.speciesDao.updateAll(specieses);
     }

     @Override
     public void deleteSpecies(Species species) {
         this.speciesDao.remove(species);
     }

    @Override
    public Pagination<Species> getSpecies(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
         return this.speciesDao.getSpecies(startPageNo, recordsPerPage, orderBys);
    }

    @Override
    public Species getSpeciesByName(String speciesName){
        return this.speciesDao.getSpeciesByName(speciesName);
    }

//    @Override
//    public Species getSpeciesBySpeciesId(String speciesId) {
//        return this.speciesDao.getSpeciesBySpeciesId(speciesId);
//    }

}

