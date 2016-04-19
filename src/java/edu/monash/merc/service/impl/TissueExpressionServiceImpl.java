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
import edu.monash.merc.dao.impl.TissueExpressionDAO;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.Tissue;
import edu.monash.merc.domain.TissueExpression;
import edu.monash.merc.service.TissueExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 13/02/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */

@Scope("prototype")
@Service
@Transactional
public class TissueExpressionServiceImpl implements TissueExpressionService {

    @Autowired
    private TissueExpressionDAO tissueExpressionDAO;

    public void setTissueExpressionDAO(TissueExpressionDAO tissueExpressionDAO) {
        this.tissueExpressionDAO = tissueExpressionDAO;
    }

    @Override
    public TissueExpression getTissueExpressionById(long id) {
        return this.tissueExpressionDAO.get(id);
    }

    @Override
    public void saveTissueExpression(TissueExpression tissueExpression) {
        this.tissueExpressionDAO.add(tissueExpression);
    }

    @Override
    public int saveTissueExpression(List<TissueExpression> tissueExpressions) {
        return this.tissueExpressionDAO.saveAll(tissueExpressions);
    }

    @Override
    public void updateTissueExpression(TissueExpression tissueExpression) {
        this.tissueExpressionDAO.update(tissueExpression);
    }

    @Override
    public void mergeTissueExpression(TissueExpression tissueExpression) {
        this.tissueExpressionDAO.merge(tissueExpression);
    }

    @Override
    public int updateTissueExpression(List<TissueExpression> tissueExpressions) {
        return this.tissueExpressionDAO.updateAll(tissueExpressions);
    }

    @Override
    public void deleteTissueExpression(TissueExpression tissueExpression) {
        this.tissueExpressionDAO.remove(tissueExpression);
    }

    @Override
    public List<TissueExpression> getTissueByTissueId(String tissueId){
        return this.tissueExpressionDAO.getTissueByTissueId(tissueId);
    }

    @Override
    public List<TissueExpression> getTissueByProbeId(String probeId) {
        return this.tissueExpressionDAO.getTissueByProbeId(probeId);
    }

    @Override
    public TissueExpression getTissueExpressionByProbeAndTissue(Probe probe, Tissue tissue){
        return this.tissueExpressionDAO.getTissueExpressionByProbeAndTissue(probe, tissue);
    }
}
