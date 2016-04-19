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
import edu.monash.merc.exception.DCParseException;
import org.apache.commons.lang.StringUtils;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ExpImporter class for importing experiment from Base.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ExpImporter {

    private SAXBuilder parser = new SAXBuilder();

    @SuppressWarnings("unchecked")
    public BaseExp importExp(InputStream expIns) {

        Document expDoc = null;

        try {
            this.parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            expDoc = this.parser.build(expIns);
        } catch (Exception ex) {
            throw new DCParseException(ex);
        }
        JDOMXPath fieldPath = null;
        JDOMXPath fieldRefPath = null;

        List<Element> fieldElements = new ArrayList<Element>();
        BaseExpField baseExpField = new BaseExpField();
        BaseExp baseExp = new BaseExp();

        try {
            fieldPath = new JDOMXPath("//property-value");
            fieldRefPath = new JDOMXPath("@ref");
            fieldElements = fieldPath.selectNodes(expDoc);

            for (Element field : fieldElements) {
                baseExpField.getExperimentProps().add(new ExperimentProp(((Attribute) fieldRefPath.selectSingleNode(field)).getValue(), field.getText()));
            }
            //create BaseExp
            baseExp = baseExpField.genExp();

        } catch (Exception ex) {
            throw new DCParseException("invalid base experiment output file, " + ex.getMessage());
        }

        //validate the BaseExp
        if (!baseExp.isValid()) {
            throw new DCException("An invalid base experiment output file");
        }

        return baseExp;
    }

    public static void main(String[] args) throws Exception {

        FileInputStream in = new FileInputStream(new File("./testData/experiment-1.export.xml"));

        ExpImporter expImporter = new ExpImporter();

        BaseExp exp = expImporter.importExp(in);

        System.out.println("==========>  name: " + exp.getName());
        System.out.println("==========>  title: " + exp.getTitle());
        System.out.println("==========>  id: " + exp.getId());
        System.out.println("==========>  owner: " + exp.getOwnerName());
        System.out.println("==========>  description: " + exp.getDescription());
        System.out.println("==========>  directory name: " + exp.getDirectoryName());
        System.out.println("==========>  bytes: " + exp.getBytes());
        System.out.println("==========>  entry date: " + exp.getEntryDate());
        System.out.println("==========>  rawDataType: " + exp.getRawDataType());
        System.out.println("==========>  pubmed id: " + exp.getPubMedId());
        System.out.println("==========>  publication: " + exp.getPublication());
        System.out.println("==========>  publicationDate: " + exp.getPublicationDate());
        System.out.println("==========>  experimentDesign: " + exp.getExperimentDesign());
        System.out.println("==========>  experimentType: " + exp.getExperimentType());
        System.out.println("==========>  authors: " + exp.getAuthors());
        System.out.println("==========>  affiliations: " + exp.getAffiliations());
        System.out.println("==========>  abstract: " + exp.getAbstraction());
    }

}
