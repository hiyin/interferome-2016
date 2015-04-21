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
 */package edu.monash.merc.common.page;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Pagination Implementation
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

public class Pagination<T> extends SimplePagination implements java.io.Serializable {

	protected List<T> pageResults = new LinkedList<T>();

    protected int searchedRecords = 0;
    //protected double searchSuccessPercentage = 0;

	public Pagination() {

	}

    public Pagination(int pageNo, int sizePerPage, int totalRecords, int searchedRecords){
        super(pageNo, sizePerPage, totalRecords);
        this.searchedRecords = searchedRecords;
        //this.searchSuccessPercentage = ((double)totalRecords/(double)searchedRecords)*100;
    }

	public Pagination(int pageNo, int sizePerPage, int totalRecords) {
		super(pageNo, sizePerPage, totalRecords);
	}

	public Pagination(int pageNo, int sizePerPage, int totalRecords, List<T> results) {
		super(pageNo, sizePerPage, totalRecords);
		this.pageResults = results;
	}

	public int getFirstResult() {
		return (pageNo - 1) * sizePerPage;
	}

	public List<T> getPageResults() {
		return pageResults;
	}

	public void setPageResults(List<T> pageResults) {
		this.pageResults = pageResults;
	}

    public int getSearchedRecords(){
        return searchedRecords;
    }

//    public double getSearchSuccessPercentage(){
//        DecimalFormat df = new DecimalFormat("#.#");
//        return Double.valueOf(df.format(searchSuccessPercentage));
//    }
}
