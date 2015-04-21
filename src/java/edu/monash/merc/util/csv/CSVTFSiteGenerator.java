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

import edu.monash.merc.domain.TFSite;

import java.util.ArrayList;
import java.util.List;



/**
 * Created with IntelliJ IDEA.
 * User: samf
 * Date: 27/06/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVTFSiteGenerator {
    private List<TFSiteColumn> columns = new ArrayList<TFSiteColumn>();

    public List<TFSiteColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TFSiteColumn> columns) {
        this.columns = columns;
    }
    public TFSite genTFSite() {
        TFSite tfSite = new TFSite();
        for (TFSiteColumn tfSiteColumncolumn : columns) {
            String columnName = tfSiteColumncolumn.getColumnName();
            String columnValue = tfSiteColumncolumn.getColumnValue();
            if (columnName.equalsIgnoreCase(TFField.ENSEMBLID)) {
                tfSite.setEnsemblID(columnValue);
            }
            if (columnName.equalsIgnoreCase(TFField.FACTOR)) {
               tfSite.setFactor(columnValue);
            }
            if (columnName.equalsIgnoreCase(TFField.START)) {
               tfSite.setStart(Integer.parseInt(columnValue));
            }
            if (columnName.equalsIgnoreCase(TFField.END)) {
                tfSite.setEnd(Integer.parseInt(columnValue));
            }
            if (columnName.equalsIgnoreCase(TFField.COREMATCH)) {
               tfSite.setCoreMatch(Double.parseDouble(columnValue));
            }
            if (columnName.equalsIgnoreCase(TFField.MATRIXMATCH)) {
                tfSite.setMatrixMatch(Double.parseDouble(columnValue));
            }
        }
        return tfSite;
    }
}
