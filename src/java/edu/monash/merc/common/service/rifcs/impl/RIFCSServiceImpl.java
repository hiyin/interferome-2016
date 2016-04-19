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

package edu.monash.merc.common.service.rifcs.impl;

import edu.monash.merc.common.service.rifcs.PartyActivityWSService;
import edu.monash.merc.common.service.rifcs.RIFCSService;
import edu.monash.merc.domain.Experiment;
import edu.monash.merc.dto.*;
import edu.monash.merc.exception.RIFCSException;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * RIFCSServiceImpl class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

@Scope("prototype")
@Service
public class RIFCSServiceImpl implements RIFCSService {

    @Autowired
    private PartyActivityWSService paWsService;

    private static String XML_HEADER = "<?xml version=\"1.0\"?>";

    private static String SCHEMA_LOCATION = "http://services.ands.org.au/documentation/rifcs/1.2.0/schema/registryObjects.xsd";

    private String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    private Logger logger = Logger.getLogger(this.getClass().getName());

    // lock object for synchronized the file writing
    private static Object fileLock = new Object();

    public void setPaWsService(PartyActivityWSService paWsService) {
        this.paWsService = paWsService;
    }

    @Override
    public String publishExpRifcs(MDRegistrationBean mdRegistrationBean) {
        Experiment experiment = mdRegistrationBean.getExperiment();
        StringBuilder rifcsBuilder = new StringBuilder();
        rifcsBuilder.append(getRifcsHeader());
        rifcsBuilder.append("<registryObject group=\"" + mdRegistrationBean.getRifcsGroupName() + "\">");
        rifcsBuilder.append(lineSeparator);
        String identifier = experiment.getPersistIdentifier();
        String key = identifier;
        if (key != null && StringUtils.contains(key, "/")) {
            key = "http://hdl.handle.net" + "/" + key;
        }
        rifcsBuilder.append("<key>" + key + "</key>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<originatingSource>" + mdRegistrationBean.getAppName() + "</originatingSource>");
        rifcsBuilder.append(lineSeparator);

        rifcsBuilder.append("<collection type=\"collection\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<identifier type=\"local\">" + experiment.getUniqueKey() + "</identifier>");
        rifcsBuilder.append(lineSeparator);

        if (identifier != null && StringUtils.contains(identifier, "/")) {
            rifcsBuilder.append("<identifier type=\"handle\">" + identifier + "</identifier>");
            rifcsBuilder.append(lineSeparator);
        }

        rifcsBuilder.append("<name type=\"primary\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<namePart>" + mdRegistrationBean.getNamePrefix() + experiment.getId() + "</namePart>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</name>");
        rifcsBuilder.append(lineSeparator);
        String electronicURL = mdRegistrationBean.getElectronicURL();
        if (StringUtils.isNotBlank(electronicURL)) {
            rifcsBuilder.append("<location>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<address>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<electronic type=\"url\">");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<value>" + mdRegistrationBean.getElectronicURL() + "</value>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</electronic>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</address>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</location>");
            rifcsBuilder.append(lineSeparator);
        }
        String physicalAddress = mdRegistrationBean.getPhysicalAddress();
        if (StringUtils.isNotBlank(physicalAddress)) {
            rifcsBuilder.append("<location>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<address>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<physical type=\"postalAddress\">");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<addressPart type=\"text\">" + mdRegistrationBean.getPhysicalAddress() + "</addressPart>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</physical>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</address>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</location>");
            rifcsBuilder.append(lineSeparator);
        }

        List<PartyBean> partyBeans = mdRegistrationBean.getPartyList();
        StringBuilder relatedPartiesBuilder = new StringBuilder();
        for (PartyBean pb : partyBeans) {
            relatedPartiesBuilder.append("<relatedObject>");
            relatedPartiesBuilder.append(lineSeparator);
            relatedPartiesBuilder.append("<key>" + pb.getPartyKey() + "</key>");
            relatedPartiesBuilder.append(lineSeparator);
            relatedPartiesBuilder.append("<relation type=\"isManagedBy\" />");
            relatedPartiesBuilder.append(lineSeparator);
            relatedPartiesBuilder.append("</relatedObject>");
            relatedPartiesBuilder.append(lineSeparator);
        }
        String partiesXML = relatedPartiesBuilder.toString();
        if (StringUtils.isNotBlank(partiesXML)) {
            rifcsBuilder.append(partiesXML);
        }

        List<ProjectBean> activitySummaryBeans = mdRegistrationBean.getActivityList();
        if (activitySummaryBeans != null) {
            StringBuilder relatedActsBuilder = new StringBuilder();
            for (ProjectBean projb : activitySummaryBeans) {
                relatedActsBuilder.append("<relatedObject>");
                relatedActsBuilder.append(lineSeparator);
                relatedActsBuilder.append("<key>" + projb.getActivityKey() + "</key>");
                relatedActsBuilder.append(lineSeparator);
                relatedActsBuilder.append("<relation type=\"isOutputOf\" />");
                relatedActsBuilder.append(lineSeparator);
                relatedActsBuilder.append("</relatedObject>");
                relatedActsBuilder.append(lineSeparator);
            }
            String actsXML = relatedActsBuilder.toString();
            if (StringUtils.isNotBlank(actsXML)) {
                rifcsBuilder.append(actsXML);
            }
        }
        String anzsrcCodes = mdRegistrationBean.getAnzsrcCode();
        List<String> codes = MercUtil.splitAnzsrcCode(anzsrcCodes);
        for (String code : codes) {
            rifcsBuilder.append("<subject  type=\"anzsrc-for\">" + code + "</subject>");
            rifcsBuilder.append(lineSeparator);
        }
        rifcsBuilder.append("<description type=\"rights\" xml:lang=\"en\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append(mdRegistrationBean.getLicence().getLicenceContents());
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</description>");
        rifcsBuilder.append(lineSeparator);

        rifcsBuilder.append("<description type=\"brief\" xml:lang=\"en\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append(experiment.getDescription());
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</description>");
        rifcsBuilder.append(lineSeparator);

        if (StringUtils.isNotBlank(mdRegistrationBean.getAccessRights())) {
            rifcsBuilder.append("<description type=\"accessRights\" xml:lang=\"en\">");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append(mdRegistrationBean.getAccessRights());
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("</description>");
            rifcsBuilder.append(lineSeparator);
        }
        //RelatedInfo
        RelatedInfo relatedInfo = mdRegistrationBean.getRelatedInfo();
        if (relatedInfo != null) {
            rifcsBuilder.append("<relatedInfo type=\"" + relatedInfo.getType() + "\">");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<identifier type=\"" + relatedInfo.getIdentifierType() + "\">");
            rifcsBuilder.append(relatedInfo.getIdentifier());
            rifcsBuilder.append("</identifier>");
            rifcsBuilder.append(lineSeparator);
            rifcsBuilder.append("<title>");
            rifcsBuilder.append(relatedInfo.getTitle());
            rifcsBuilder.append("</title>");
            rifcsBuilder.append(lineSeparator);
            String notes = relatedInfo.getNotes();
            if (StringUtils.isNotBlank(notes)) {
                rifcsBuilder.append("<notes>");
                rifcsBuilder.append(notes);
                rifcsBuilder.append("</notes>");
                rifcsBuilder.append(lineSeparator);
            }
            rifcsBuilder.append("</relatedInfo>");
            rifcsBuilder.append(lineSeparator);
        }
        rifcsBuilder.append("</collection>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</registryObject>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append(getRifcsEnder());
        String rifcsStoreLocation = mdRegistrationBean.getRifcsStoreLocation();

        synchronized (fileLock) {
            File rifcsFile = new File(rifcsStoreLocation + File.separator + experiment.getUniqueKey() + ".xml");
            try {
                // publish collection
                FileUtils.writeStringToFile(rifcsFile, rifcsBuilder.toString());

                // publish parties
                publishPartyRifcs(mdRegistrationBean);

                // publish activities
                publishActivityRifc(mdRegistrationBean);

            } catch (Exception e) {
                logger.error(e);
                throw new RIFCSException(e);
            }

        }

        return rifcsBuilder.toString();
    }

    private String getRifcsHeader() {
        StringBuilder rifcsHeaderBuilder = new StringBuilder();
        rifcsHeaderBuilder.append(XML_HEADER);
        rifcsHeaderBuilder.append(lineSeparator);
        rifcsHeaderBuilder.append("<registryObjects xmlns=\"http://ands.org.au/standards/rif-cs/registryObjects\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xsi:schemaLocation=\"http://ands.org.au/standards/rif-cs/registryObjects " + SCHEMA_LOCATION + "\">");
        rifcsHeaderBuilder.append(lineSeparator);
        return rifcsHeaderBuilder.toString();
    }

    private String getRifcsEnder() {
        StringBuilder rifcsEnderBuilder = new StringBuilder();
        rifcsEnderBuilder.append("</registryObjects>");
        rifcsEnderBuilder.append(lineSeparator);
        return rifcsEnderBuilder.toString();
    }

    private void publishPartyRifcs(MDRegistrationBean mdRegistrationBean) {
        List<PartyBean> partyBeans = mdRegistrationBean.getPartyList();
        for (PartyBean p : partyBeans) {
            try {
                String rifcs = null;
                if (p.isFromRm()) {
                    PartyBean pb = this.paWsService.getParty(p.getPartyKey());
                    String rifcsContents = pb.getRifcsContent();
                    StringBuilder rifcsbuilder = new StringBuilder();
                    rifcsbuilder.append(getRifcsHeader());
                    rifcsbuilder.append(rifcsContents);
                    rifcsbuilder.append(getRifcsEnder());
                    rifcs = rifcsbuilder.toString();
                } else {
                    rifcs = genUserDefinedPartyRIFCS(p);
                }

                if (rifcs != null) {
                    String rifcsFileName = MercUtil.pathEncode(p.getPartyKey()) + ".xml";
                    File rifcsFile = new File(mdRegistrationBean.getRifcsStoreLocation() + File.separator + rifcsFileName);
                    FileUtils.writeStringToFile(rifcsFile, rifcs);
                }
            } catch (Exception e) {
                logger.error(e);
                throw new RIFCSException(e);
            }
        }
    }

    private String genUserDefinedPartyRIFCS(PartyBean pb) {

        StringBuilder rifcsBuilder = new StringBuilder();
        rifcsBuilder.append(getRifcsHeader());
        rifcsBuilder.append("<registryObject group=\"" + pb.getGroupName() + "\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<key>" + pb.getPartyKey() + "</key>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<originatingSource type=\"" + pb.getOriginateSourceType() + "\">" + pb.getOriginateSourceValue()
                + "</originatingSource>");
        rifcsBuilder.append(lineSeparator);
        Date date = GregorianCalendar.getInstance().getTime();
        String dateModified = MercUtil.formatDateToUTC(date);
        rifcsBuilder.append("<party type=\"person\" dateModified=\"" + dateModified + "\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<identifier type=\"local\">" + pb.getIdentifierValue() + "</identifier>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<name type=\"primary\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<namePart type=\"title\">" + pb.getPersonTitle() + "</namePart>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<namePart type=\"given\">" + pb.getPersonGivenName() + "</namePart>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<namePart type=\"family\">" + pb.getPersonFamilyName() + "</namePart>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</name>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<location>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<address>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<electronic type=\"url\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<value>" + pb.getUrl() + "</value>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</electronic>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<electronic type=\"email\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<value>" + pb.getEmail() + "</value>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</electronic>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<physical type=\"postalAddress\">");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("<addressPart type=\"text\">" + pb.getAddress() + "</addressPart>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</physical>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</address>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</location>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</party>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append("</registryObject>");
        rifcsBuilder.append(lineSeparator);
        rifcsBuilder.append(getRifcsEnder());
        return rifcsBuilder.toString();
    }

    private void publishActivityRifc(MDRegistrationBean mdRegistrationBean) {
        List<ProjectBean> projBeans = mdRegistrationBean.getActivityList();
        if (projBeans != null) {
            for (ProjectBean p : projBeans) {
                ActivityBean ab = this.paWsService.getActivity(p.getActivityKey());
                String rifcsContents = ab.getRifcsContent();
                StringBuilder rifcsbuilder = new StringBuilder();
                rifcsbuilder.append(getRifcsHeader());
                rifcsbuilder.append(rifcsContents);
                rifcsbuilder.append(getRifcsEnder());

                try {
                    File rifcsFile = new File(mdRegistrationBean.getRifcsStoreLocation() + File.separator + MercUtil.pathEncode(p.getActivityKey())
                            + ".xml");
                    FileUtils.writeStringToFile(rifcsFile, rifcsbuilder.toString());
                } catch (Exception e) {
                    logger.error(e);
                    throw new RIFCSException(e);
                }
            }
        }
    }
}
