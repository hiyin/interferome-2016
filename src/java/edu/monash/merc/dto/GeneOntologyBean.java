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

import java.io.Serializable;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 27/06/12
 *        Time: 2:20 PM
 */
public class GeneOntologyBean implements Serializable {

    private String ensembleGeneId;

    private String goTermAccession;

    private String goTermName;

    private String goTermDefinition;

    private String goTermEvidenceCode;

    private String goDomain;

    public String getEnsembleGeneId() {
        return ensembleGeneId;
    }

    public void setEnsembleGeneId(String ensembleGeneId) {
        this.ensembleGeneId = ensembleGeneId;
    }

    public String getGoTermAccession() {
        return goTermAccession;
    }

    public void setGoTermAccession(String goTermAccession) {
        this.goTermAccession = goTermAccession;
    }

    public String getGoTermName() {
        return goTermName;
    }

    public void setGoTermName(String goTermName) {
        this.goTermName = goTermName;
    }

    public String getGoTermDefinition() {
        return goTermDefinition;
    }

    public void setGoTermDefinition(String goTermDefinition) {
        this.goTermDefinition = goTermDefinition;
    }

    public String getGoTermEvidenceCode() {
        return goTermEvidenceCode;
    }

    public void setGoTermEvidenceCode(String goTermEvidenceCode) {
        this.goTermEvidenceCode = goTermEvidenceCode;
    }

    public String getGoDomain() {
        return goDomain;
    }

    public void setGoDomain(String goDomain) {
        this.goDomain = goDomain;
    }
}
