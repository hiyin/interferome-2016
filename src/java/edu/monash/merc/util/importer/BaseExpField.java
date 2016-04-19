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

package edu.monash.merc.util.importer;

import edu.monash.merc.exception.DCException;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseExpField class for experiment importing.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class BaseExpField {

    private static String NAME = "name";

    private static String TITLE = "title";

    private static String OWNERNAME = "owner.name";

    private static String DESCRIPTION = "description";

    private static String RAWDATATYPE = "rawDataType";

    private static String ENTRYDATE = "entryDate";

    private static String BYTES = "bytes";

    private static String DIRECTORYNAME = "directory.name";

    private static String PUBMEDID = "pubMedId";

    private static String PUBLICATION = "publication";

    private static String PUBLICATIONDATE = "publicationDate";

    private static String EXPERIMENTDESIGN = "experimentDesign";

    private static String EXPERIMENTTYPE = "experimentType";

    private static String AFFILIATIONS = "affiliations";

    private static String AUTHORS = "authors";

    private static String ABSTRACT = "abstract";

    private static int TOTAL_REQUIRED = 16;

    private List<ExperimentProp> experimentProps = new ArrayList<ExperimentProp>();

    public List<ExperimentProp> getExperimentProps() {
        return experimentProps;
    }

    public void setExperimentProps(List<ExperimentProp> experimentProps) {
        this.experimentProps = experimentProps;
    }

    public BaseExp genExp() {
        int count = 0;
        BaseExp exp = new BaseExp();
        for (ExperimentProp field : experimentProps) {
            String fieldId = field.getName();

            if (StringUtils.equals(fieldId, NAME)) {
                count++;
                exp.setName(field.getValue());
            }
            if (StringUtils.equals(fieldId, TITLE)) {
                count++;
                exp.setTitle(field.getValue());
            }
            if (StringUtils.equals(fieldId, OWNERNAME)) {
                count++;
                exp.setOwnerName(field.getValue());
            }
            if (StringUtils.equals(fieldId, DESCRIPTION)) {
                count++;
                exp.setDescription(field.getValue());
            }
            if (StringUtils.equals(fieldId, RAWDATATYPE)) {
                count++;
                exp.setRawDataType(field.getValue());
            }
            if (StringUtils.equals(fieldId, ENTRYDATE)) {
                count++;
                if (StringUtils.isNotBlank(field.getValue())) {
                    exp.setEntryDate(MercUtil.formatYMDDate(field.getValue()));
                }
            }
            if (StringUtils.equals(fieldId, BYTES)) {
                count++;
                exp.setBytes(Integer.valueOf(field.getValue()));
            }
            if (StringUtils.equals(fieldId, DIRECTORYNAME)) {
                count++;
                exp.setDirectoryName(field.getValue());
            }
            if (StringUtils.equals(fieldId, PUBMEDID)) {
                count++;
                exp.setPubMedId(field.getValue());
            }
            if (StringUtils.equals(fieldId, PUBLICATION)) {
                count++;
                exp.setPublication(field.getValue());
            }
            if (StringUtils.equals(fieldId, PUBLICATIONDATE)) {
                count++;
                if (StringUtils.isNotBlank(field.getValue())) {
                    exp.setPublicationDate(MercUtil.formatYMDDate(field.getValue()));
                }
            }
            if (StringUtils.equals(fieldId, EXPERIMENTDESIGN)) {
                count++;
                exp.setExperimentDesign(field.getValue());
            }
            if (StringUtils.equals(fieldId, EXPERIMENTTYPE)) {
                count++;
                exp.setExperimentType(field.getValue());
            }
            if (StringUtils.equals(fieldId, AFFILIATIONS)) {
                count++;
                exp.setAffiliations(field.getValue());
            }
            if (StringUtils.equals(fieldId, AUTHORS)) {
                count++;
                exp.setAuthors(field.getValue());
            }
            if (StringUtils.equals(fieldId, ABSTRACT)) {
                count++;
                exp.setAbstraction(field.getValue());
            }
        }
        //check how many fields have been provided
        if (count != TOTAL_REQUIRED) {
            exp.setValid(false);
        } else {
            validate(exp);
            //set valid flag as true after validate method called
            exp.setValid(true);
        }
        return exp;
    }

    private void validate(BaseExp exp) {
        if (StringUtils.isBlank(exp.getName())) {
            throw new DCException("The experiment name not found");
        }
        if (StringUtils.isBlank(exp.getDescription())) {
            throw new DCException("The experiment description not found");
        }
        if (StringUtils.isBlank(exp.getOwnerName())) {
            throw new DCException("The experiment base owner not found");
        }
        if (exp.getEntryDate() == null) {
            throw new DCException("The experiment created date not found");
        }
        //check the publication info
        if (StringUtils.isNotBlank(exp.getPubMedId())) {
            if (exp.getPublicationDate() == null) {
                throw new DCException("The publication date not found");
            }
            if (StringUtils.isBlank(exp.getTitle())) {
                throw new DCException("The publication title not found");
            }
            if (StringUtils.isBlank(exp.getAbstraction())) {
                throw new DCException("The publication abstract not found");
            }
            if (StringUtils.isBlank(exp.getAuthors())) {
                throw new DCException("The publication authors not found");
            }
            if (StringUtils.isBlank(exp.getPublication())) {
                throw new DCException("The publication not found");
            }
        }
    }
}
