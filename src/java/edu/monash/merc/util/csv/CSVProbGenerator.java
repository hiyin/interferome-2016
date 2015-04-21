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

package edu.monash.merc.util.csv;

import java.util.ArrayList;
import java.util.List;
import edu.monash.merc.domain.Probe;

/**
 * Created with IntelliJ IDEA.
 * User: irinar
 * Date: 14/12/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVProbGenerator {
    private List<ProbeColumn> columns = new ArrayList<ProbeColumn>();

    public List<ProbeColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ProbeColumn> columns) {
        this.columns = columns;
    }

    public Probe genProbe() {
        Probe probes = new Probe();
         for (ProbeColumn prcolumn : columns) {
             String columnName = prcolumn.getColumnName();
             String columnValue = prcolumn.getColumnValue();
             if (columnName.equalsIgnoreCase(PField.PROBEID)) {
                 probes.setProbeId(columnValue);
             }
             if (columnName.equalsIgnoreCase(PField.ENSEMBLID)) {
                 probes.setEnsemblId(columnValue);
             }
             if (columnName.equalsIgnoreCase(PField.SPECIES)) {
                 probes.setSpeciesName(columnValue);
             }
         }
         return probes;
    }
}
