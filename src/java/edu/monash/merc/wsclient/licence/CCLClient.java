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

package edu.monash.merc.wsclient.licence;

import edu.monash.merc.dto.CCLicense;
import edu.monash.merc.dto.CCLicenseField;
import edu.monash.merc.dto.CCWSField;
import edu.monash.merc.exception.WSException;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * CCLClient class is a creative common licence web service client.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class CCLClient {

    private SAXBuilder parser = new SAXBuilder();

    private static String license_rest_url = "http://api.creativecommons.org/rest/1.5/license/standard"; // /get?";

    private String serviceURL;

    public CCLClient() {

    }

    public CCLClient(String serviceUrl) {
        this.serviceURL = serviceUrl;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    @SuppressWarnings("unchecked")
    public List<CCWSField> generateLicenseFields() {
        JDOMXPath xp_LicenseField;
        JDOMXPath xp_LicenseID;
        JDOMXPath xp_FieldType;
        JDOMXPath xp_Description;
        JDOMXPath xp_Label;
        JDOMXPath xp_Enum;

        Document fieldDoc = null;

        List<Element> results = null;
        List<Object> enumOptions = null;

        // create XPath expressions
        try {
            xp_LicenseField = new JDOMXPath("//field");
            xp_LicenseID = new JDOMXPath("@id");
            xp_Description = new JDOMXPath("description");
            xp_Label = new JDOMXPath("label");
            xp_FieldType = new JDOMXPath("type");
            xp_Enum = new JDOMXPath("enum");

        } catch (JaxenException e) {
            throw new WSException(e);
        }

        // parse the classes document
        try {
            if (serviceURL == null) {
                serviceURL = license_rest_url;
            }
            URL standardLicenseUrl = new URL(serviceURL);
            fieldDoc = this.parser.build(standardLicenseUrl);
        } catch (Exception e) {
            throw new WSException(e);
        }

        // extract the identifiers and labels using XPath
        try {
            results = xp_LicenseField.selectNodes(fieldDoc);
        } catch (JaxenException e) {
            throw new WSException(e);
        }

        List<CCWSField> fields = new ArrayList<CCWSField>();

        for (int i = 0; i < results.size(); i++) {
            Element field = results.get(i);

            try {
                // create the field object
                CCWSField f = new CCWSField(((Attribute) xp_LicenseID.selectSingleNode(field)).getValue(),
                        ((Element) xp_Label.selectSingleNode(field)).getText());

                // extract additional properties
                f.setDescription(((Element) xp_Description.selectSingleNode(field)).getText());
                f.setType(((Element) xp_FieldType.selectSingleNode(field)).getText());

                enumOptions = xp_Enum.selectNodes(field);

                for (int j = 0; j < enumOptions.size(); j++) {

                    String id = ((Attribute) xp_LicenseID.selectSingleNode(enumOptions.get(j))).getValue();
                    String label = ((Element) xp_Label.selectSingleNode(enumOptions.get(j))).getText();
                    String desc = null;
                    if (!f.getId().equals("jurisdiction")) {
                        desc = ((Element) xp_Description.selectSingleNode(enumOptions.get(j))).getText();
                    }
                    if (id.equals("")) {
                        label = "International";
                    }
                    f.getCcLicenseFields().add(new CCLicenseField(id, label, desc));
                } // for each enum option

                fields.add(f);
            } catch (JaxenException e) {
                throw new WSException(e);
            }
        }
        return fields;
    }

    @SuppressWarnings("unchecked")
    public CCLicense getCCLicense(String licenseParams) {
        Document licenseDoc = null;

        JDOMXPath xp_licenseName;
        JDOMXPath xp_licenseHtml;
        JDOMXPath xp_licenseLink;
        JDOMXPath xp_licenseHref;

        // create XPath expressions
        try {
            xp_licenseName = new JDOMXPath("//license-name");
            xp_licenseHtml = new JDOMXPath("//html");
            xp_licenseLink = new JDOMXPath("//license-uri");
            xp_licenseHref = new JDOMXPath("//a");

        } catch (JaxenException e) {
            throw new WSException(e);
        }
        try {
            if (serviceURL == null) {
                serviceURL = license_rest_url;
            }
            URL licenseUrl = new URL(serviceURL + "/get?" + licenseParams);
            licenseDoc = this.parser.build(licenseUrl);
        } catch (Exception e) {
            throw new WSException(e);
        }

        // extract the identifiers and labels using XPath
        try {
            String licenseName = ((Element) xp_licenseName.selectSingleNode(licenseDoc)).getText();
            String licenseLink = ((Element) xp_licenseLink.selectSingleNode(licenseDoc)).getText();
            String licenseHtml = ((Element) xp_licenseHtml.selectSingleNode(licenseDoc)).getText();
            List<Element> allHrefs = xp_licenseHref.selectNodes(licenseDoc);
            String aHrefText = null;
            for (Element e : allHrefs) {
                String hrefText = e.getText();
                if (StringUtils.isNotBlank(hrefText)) {
                    aHrefText = hrefText;
                }
            }
            licenseHtml = StringUtils.removeEnd(licenseHtml, ".").trim();
            CCLicense license = new CCLicense(licenseName, licenseLink, licenseHtml, aHrefText);
            return license;
        } catch (JaxenException e) {
            throw new WSException(e);
        }
    }

    public static void main(String[] args) {
        CCLClient cct = new CCLClient();
        cct.setServiceURL("http://api.creativecommons.org/rest/1.5/license/standard");
        List<CCWSField> lfds = cct.generateLicenseFields();

        for (CCWSField wsfd : lfds) {
            System.out.println(wsfd.getId() + " ----- " + wsfd.getLabel());
            System.out.println(" desc: " + wsfd.getDescription());

            List<CCLicenseField> lfields = wsfd.getCcLicenseFields();
            if (!wsfd.getId().equals("jurisdiction")) {
                for (CCLicenseField lf : lfields) {
                    System.out.println("id: " + lf.getId() + " = " + lf.getLabel() + "\n = " + lf.getDescription());
                }
            } else {
                for (CCLicenseField lf : lfields) {
                    System.out.println("id: " + lf.getId() + " = " + lf.getLabel());
                }
            }
        }
        CCLicense license = cct.getCCLicense("commercial=n&derivatives=y&jurisdiction=cn");
        System.out.println("html: " + license.getLicenseHtml());
    }

}
