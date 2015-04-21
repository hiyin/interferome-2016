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

package edu.monash.merc.common.results;

import edu.monash.merc.domain.Data;
import edu.monash.merc.domain.Dataset;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.struts2.action.DMBaseAction;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 1/29/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultRow {

    public Data data;
    public Dataset dataset;
    public Probe probe;
    public Gene gene;

    public Data getData() {
        return data;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Probe getProbe() {
        return probe;
    }

    public Gene getGene() {
        return gene;
    }

    public SearchResultRow (Data d, Dataset ds, Probe p, Gene g){
        this.data = d;
        this.dataset = ds;
        this.probe = p;
        this.gene = g;
    }

    /**
     * Produces a list of search result rows from an appropriate Hibernate query.
     *
     * The query should return data, dataset, probe and gene in order for each row.
     *
     * @param query
     * @return
     */
    public static List<SearchResultRow> listFromQuery(Query query) {
        ArrayList<SearchResultRow> searchResultRowArrayList = new ArrayList<SearchResultRow>();
        Iterator rows = query.iterate();
        while (rows.hasNext()) {
            Object[] row = (Object[]) rows.next();
            searchResultRowArrayList.add(new SearchResultRow((Data) row[0], (Dataset) row[1], (Probe) row[2], (Gene) row[3]));
        }
        return searchResultRowArrayList;
    }
}
