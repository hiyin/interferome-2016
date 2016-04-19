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
package edu.monash.merc.common.page;

/**
 * SimplePagination - A simple pagination implementation
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

public class SimplePagination implements Paginable {

    public static final int DEFAULT_SIZE_PER_PAGE = 20;

    // total records in database
    protected int totalRecords = 0;

    // display record size per page
    protected int sizePerPage = 20;

    // The current page number
    protected int pageNo = 1;

    public SimplePagination() {

    }

    public SimplePagination(int pageNo, int sizePerPage, int totalRecords) {
        if (totalRecords <= 0) {
            this.totalRecords = 0;
        } else {
            this.totalRecords = totalRecords;
        }
        if (sizePerPage <= 0) {
            this.sizePerPage = DEFAULT_SIZE_PER_PAGE;
        } else {
            this.sizePerPage = sizePerPage;
        }
        if (pageNo <= 0) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }

        if ((this.pageNo - 1) * this.sizePerPage >= totalRecords) {
            this.pageNo = totalRecords / sizePerPage;
        }
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * Caculate the total pages of records in the database
     */
    @Override
    public int getTotalPages() {
        int totalPages = totalRecords / sizePerPage;
        if (totalRecords % sizePerPage != 0 || totalPages == 0) {
            totalPages++;
        }
        return totalPages;
    }

    public void adjustPage() {
        if (totalRecords <= 0) {
            totalRecords = 0;
        }
        if (sizePerPage <= 0) {
            sizePerPage = DEFAULT_SIZE_PER_PAGE;
        }
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if ((pageNo - 1) * sizePerPage >= totalRecords) {
            pageNo = totalRecords / sizePerPage;
        }
    }

    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
    }

    @Override
    public int getSizePerPage() {
        return this.sizePerPage;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public int getPageNo() {
        return this.pageNo;
    }

    @Override
    public boolean isFirstPage() {
        return pageNo <= 1;
    }

    @Override
    public boolean isLastPage() {
        return pageNo >= getTotalPages();
    }

    @Override
    public int getNextPage() {
        if (isLastPage()) {
            return pageNo;
        } else {
            return pageNo + 1;
        }
    }

    @Override
    public int getPrevPage() {
        if (isFirstPage()) {
            return pageNo;
        } else {
            return pageNo - 1;
        }
    }

}
