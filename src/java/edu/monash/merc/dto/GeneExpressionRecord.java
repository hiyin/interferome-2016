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

package edu.monash.merc.dto;

import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.TissueExpression;

import java.util.ArrayList;
import java.util.List;

public class GeneExpressionRecord {

    private String geneName;

    private TissueExpression tissueExpression;

    /**
     * This list contains all of the tissues where this unique combination of probe and gene are combined
     */
    private List<TissueExpression> tissueExpressionList = new ArrayList<TissueExpression>();
    private String speciesName;

    public GeneExpressionRecord(String geneName, TissueExpression t, String speciesName){
        this.geneName = geneName;
        this.tissueExpression = t;
        this.speciesName = speciesName;
        this.addTissueExpression(t);
    }

    public Probe getProbe() {
        return tissueExpression.getProbe();
    }

    public String getGeneName() {
        return geneName;
    }

    public TissueExpression getTissueExpression() {
        return tissueExpression;
    }


    public List<TissueExpression> getTissueExpressionList() {
        return tissueExpressionList;
    }

    public void addTissueExpression(TissueExpression tissueExpression) {
        this.tissueExpressionList.add(tissueExpression);
    }

    /**
     * Implemented to match on geneName and probe
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneExpressionRecord)) return false;

        GeneExpressionRecord that = (GeneExpressionRecord) o;

        if (geneName != null ? !geneName.equals(that.geneName) : that.geneName != null) return false;
        if (getProbe() != null ? !getProbe().equals(that.getProbe()) : that.getProbe() != null)
            return false;

        return true;
    }

    /**
     * Implemented to match on geneName and probe
     * @return
     */
    @Override
    public int hashCode() {
        int result = geneName != null ? geneName.hashCode() : 0;
        result = 31 * result + (getProbe() != null ? getProbe().hashCode() : 0);
        return result;
    }

    public String getSpeciesName() {
        return speciesName;
    }
}
