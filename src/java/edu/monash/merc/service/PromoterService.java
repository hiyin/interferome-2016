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

package edu.monash.merc.service;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.domain.Promoter;

import java.util.List;

/**
 * Created by mimr on 1/13/16.
 */

public interface PromoterService {

    /**
     * Save a Promoter
     *
     * @param promoter a Promoter object
     */
    void savePromoter(Promoter promoter);

    /**
     * Merge a Promoter object
     *
     * @param promoter a Promoter object
     */
    void mergePromoter(Promoter promoter);

    /**
     * Update a Promoter object
     *
     * @param promoter a Promoter object
     */
    void updatePromoter(Promoter promoter);

    /**
     * Delete a Promoter object
     *
     * @param promoter a Promoter object
     */
    void deletePromoter(Promoter promoter);

    /**
     * Get the Promoter by an ensembl accession id
     *
     * @param ensgAccession an ensembl accession id
     * @return a Promoter
     */
    public Promoter getPromoterByEnsgAccession(String ensgAccession);


}