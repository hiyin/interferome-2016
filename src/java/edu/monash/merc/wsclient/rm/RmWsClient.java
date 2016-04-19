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

package edu.monash.merc.wsclient.rm;

import javax.xml.namespace.QName;

import edu.monash.merc.dto.ActivityBean;
import edu.monash.merc.dto.PartyBean;
import edu.monash.merc.dto.ProjectBean;
import edu.monash.merc.exception.WSException;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * RmWsClient class is a Research Master web service client which provides the RIF-CS service for party and activity.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class RmWsClient extends Stub {

    private static String DEFAULT_TARGET_ENDPOINT = "https://gateway.integration.monash.edu.au:443/AIRMANDSService";

    private static String DEFAULT_SERVICE_NAME = "AIRMANDSService";

    private static String GET_NLA_ID_NAMESPACE = "http://monash.edu/AI/AIRMNLAActivityIDPLSQLWS";

    private static String GET_PARTY_REGISTRY_OBJECT_NAMESPACE = "http://monash.edu/AI/AIRMPartyPLSQLWS";

    private static String GET_PROJECTS_NAMESPACE = "http://monash.edu/AI/AIRMNLAActivityIDPLSQLWS";

    private static String GET_ACTIVITY_NAMESPACE = "http://monash.edu/AI/AIRMActivityPLSQLWS";

    private static String DEFAULT_ELEMENT_NAMESPACE_PREFIX = "ns1";

    private static String DEFAULT_RESPONSE_NAMESPACE = "http://ands.org.au/standards/rif-cs/registryObjects";
    // NLA ID
    private static String ACTION_GET_NLAID = "getNlaId";

    private static String OMELEMENT_GET_NLAID_NAME = "getNlaIdElement";

    private static String OME_PAUTHCATE_USER_NAME = "pAuthcateUsername";

    // Party
    private static String ACTION_GET_PARTY_OBJECT = "getPartyregistryobject";

    private static String OMELEMENT_GET_PARTY_NAME = "getPartyregistryobjectElement";

    private static String OME_PARTY_ID_NAME = "pPartyId";

    // Activity Summary - projects
    private static String ACTION_GET_PROJECTS = "getProjects";

    private static String OMELEMENT_GET_PROJECT_NAME = "getProjectsElement";

    private static String OME_PRO_NLA_ID_NAME = "pNlaId";

    // Activity
    private static String ACTION_GET_ACTIVITY_OBJECT = "getActivityregistryobject";

    private static String OMELEMENT_GET_ACTIVITY_NAME = "getActivityregistryobjectElement";

    private static String OME_ACTIVITY_ID_NAME = "pActivityId";

    protected AxisOperation[] operations;

    protected String targetEndpoint;

    protected String serviceName;

    protected long timeout;

    protected ConfigurationContext configurationContext;

    protected boolean useSeparateListener;

    private static int counter = 0;

    private boolean configured;

    private Logger loger = Logger.getLogger(this.getClass().getName());

    public RmWsClient() {
        this(null, null, null, false);
    }

    public RmWsClient(ConfigurationContext configurationContext) {
        this(configurationContext, null, null, false);
    }

    public RmWsClient(String targetEndpoint) {
        this(null, null, targetEndpoint, false);
    }

    public RmWsClient(ConfigurationContext configurationContext, String targetEndpoint) {
        this(configurationContext, null, targetEndpoint, false);
    }

    public RmWsClient(ConfigurationContext configurationContext, String targetEndpoint, boolean useSeparateListener) {
        this(configurationContext, null, targetEndpoint, useSeparateListener);
    }

    public RmWsClient(ConfigurationContext configurationContext, String serviceName, String targetEndpoint, boolean useSeparateListener) {
        this.configurationContext = configurationContext;
        this.serviceName = serviceName;
        this.targetEndpoint = targetEndpoint;
        this.useSeparateListener = useSeparateListener;
    }

    public void serviceInit() {
        try {
            init();
            populateAxisService();
            _serviceClient = new ServiceClient(this.configurationContext, _service);
            _serviceClient.getOptions().setTo(new EndpointReference(this.targetEndpoint));
            _serviceClient.getOptions().setUseSeparateListener(this.useSeparateListener);
            _serviceClient.getOptions().setTimeOutInMilliSeconds(timeout);
            configured = true;
        } catch (Exception e) {
            throw new WSException(e);
        }
    }

    private void init() {
        if (StringUtils.isBlank(serviceName)) {
            this.serviceName = DEFAULT_SERVICE_NAME;
        }

        if (StringUtils.isBlank(targetEndpoint)) {
            this.targetEndpoint = DEFAULT_TARGET_ENDPOINT;
        }
        if (timeout == 0) {
            timeout = 60000;
        }
    }

    protected void populateAxisService() throws AxisFault {
        _service = new AxisService(serviceName + getUniqueSuffix());
        operations = new AxisOperation[4];
        //get nla id
        AxisOperation operation = new OutInAxisOperation(new QName(GET_NLA_ID_NAMESPACE, ACTION_GET_NLAID));
        _service.addOperation(operation);
        operations[0] = operation;

        //get party
        operation = new OutInAxisOperation(new QName(GET_PARTY_REGISTRY_OBJECT_NAMESPACE, ACTION_GET_PARTY_OBJECT));
        _service.addOperation(operation);
        operations[1] = operation;

        //get projects (activity summary)
        operation = new OutInAxisOperation(new QName(GET_PROJECTS_NAMESPACE, ACTION_GET_PROJECTS));
        _service.addOperation(operation);
        operations[2] = operation;

        //get activity
        operation = new OutInAxisOperation(new QName(GET_ACTIVITY_NAMESPACE, ACTION_GET_ACTIVITY_OBJECT));
        _service.addOperation(operation);
        operations[3] = operation;
    }

    private static synchronized String getUniqueSuffix() {
        // reset the counter if it is greater than 99999
        if (counter > 99999) {
            counter = 0;
        }
        counter = counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    public String getNlaId(String authcateId) {
        if (!isConfigured()) {
            throw new WSException("call serviceInit first before getNlaId.");
        }

        try {
            OperationClient operationClient = _serviceClient.createClient(operations[0].getName());
            operationClient.getOptions().setAction(ACTION_GET_NLAID);
            operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            // operationClient.getOptions().setProperty(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            // create a message context
            MessageContext messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;
            SOAPFactory factory = getFactory(operationClient.getOptions().getSoapVersionURI());
            env = createEnvelope(factory, ACTION_GET_NLAID, authcateId);
            // System.out.println("send envelop: " + env);
            // adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);

            // set the message context with that soap envelope
            messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            operationClient.addMessageContext(messageContext);

            // execute the operation client
            operationClient.execute(true);

            MessageContext returnMessageContext = operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

            SOAPEnvelope _returnEnv = returnMessageContext.getEnvelope();

            // System.out.println("_return envelop: " + _returnEnv);

            return parseNlaId(_returnEnv);
        } catch (AxisFault axe) {

            OMElement fault = axe.getDetail();

            if (fault != null) {
                throw new WSException(fault.getFirstElement().getText());
            } else {
                throw new WSException(axe);
            }
        } catch (Exception e) {
            loger.error("research master ws failed, " + e);
            throw new WSException(e);
        } finally {
            try {
                if (_serviceClient != null) {
                    _serviceClient.cleanupTransport();
                }
            } catch (Exception ex) {
                loger.error("clean up the service client transport failed, " + ex);
            }
        }
    }

    /**
     * parse the nla id
     *
     * @param respEnvelope
     * @return
     */
    private String parseNlaId(SOAPEnvelope respEnvelope) {
        OMElement responseElement = respEnvelope.getBody().getFirstElement();
        OMElement resultElement = responseElement.getFirstElement();
        OMElement pnlaidOutElement = resultElement.getFirstElement();
        OMElement nlaIdElement = pnlaidOutElement.getFirstElement();
        return nlaIdElement.getText();
    }

    public PartyBean getPartyRegistryObject(String nlaId) {
        if (!isConfigured()) {
            throw new WSException("call serviceInit first before getPartyRegistryObject.");
        }

        try {
            OperationClient operationClient = _serviceClient.createClient(operations[1].getName());
            operationClient.getOptions().setAction(ACTION_GET_PARTY_OBJECT);
            operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            // operationClient.getOptions().setProperty(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            // create a message context
            MessageContext messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;
            SOAPFactory factory = getFactory(operationClient.getOptions().getSoapVersionURI());
            env = createEnvelope(factory, ACTION_GET_PARTY_OBJECT, nlaId);
            // System.out.println("=== sending envelope: " + env);
            // adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);

            // set the message context with that soap envelope
            messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            operationClient.addMessageContext(messageContext);

            // execute the operation client
            operationClient.execute(true);

            MessageContext returnMessageContext = operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

            SOAPEnvelope _returnEnv = returnMessageContext.getEnvelope();
            //System.out.println("=== Response envelope: " + env);
            return parseParty(_returnEnv);
        } catch (AxisFault axe) {
            OMElement fault = axe.getDetail();
            if (fault != null) {
                String message = fault.getFirstElement().getText();
                throw new WSException(message);
            } else {
                throw new WSException(axe);
            }
        } catch (Exception e) {
            loger.error("research master ws failed, " + e);
            throw new WSException(e);
        } finally {
            try {
                if (_serviceClient != null) {
                    _serviceClient.cleanupTransport();
                }
            } catch (Exception ex) {
                loger.error("clean up the service client transport failed, " + ex);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private PartyBean parseParty(SOAPEnvelope respEnvelope) {
        PartyBean pb = new PartyBean();
        pb.setFromRm(true);
        // System.out.println("=== party envelope: " + respEnvelope);
        OMElement partyRegistryObjectsElement = respEnvelope.getBody().getFirstElement();
        OMElement registryObject = partyRegistryObjectsElement.getFirstElement();

        //System.out.println("====> registryObject: " + registryObject);
        // get the party group
        OMAttribute groupAtt = registryObject.getAttribute(new QName("group"));
        pb.setGroupName(groupAtt.getAttributeValue());

        // set the party key
        Iterator<OMElement> keyit = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "key"));
        if (keyit.hasNext()) {
            OMElement keyElement = keyit.next();
            String key = keyElement.getText();
            pb.setPartyKey(key);
        }

        // originating source
        Iterator<OMElement> origit = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "originatingSource"));
        if (origit.hasNext()) {
            OMElement orig = origit.next();
            OMAttribute typeAtt = orig.getAttribute(new QName("type"));
            String origvalue = orig.getText();

            // set originating source type
            pb.setOriginateSourceType(typeAtt.getAttributeValue());

            // set originating source type value
            pb.setOriginateSourceValue(origvalue);
        }

        // originating source
        Iterator<OMElement> partyIt = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "party"));
        if (partyIt.hasNext()) {
            OMElement partyElement = partyIt.next();
            // identifier
            Iterator<OMElement> identifierIt = partyElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "identifier"));
            if (identifierIt.hasNext()) {
                OMElement identifierEle = identifierIt.next();
                OMAttribute typeAtt = identifierEle.getAttribute(new QName("type"));

                // set the identifier
                pb.setIdentifierType(typeAtt.getAttributeValue());
                pb.setIdentifierValue(identifierEle.getText());
            }

            // person name and name part
            Iterator<OMElement> pNameIt = partyElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "name"));
            if (pNameIt.hasNext()) {
                OMElement pNameElement = pNameIt.next();
                Iterator<OMElement> pNamePartIt = pNameElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "namePart"));
                while (pNamePartIt.hasNext()) {
                    OMElement pnamePartElement = pNamePartIt.next();
                    OMAttribute typeAtt = pnamePartElement.getAttribute(new QName("type"));
                    String typeValue = typeAtt.getAttributeValue();
                    if (typeValue.equals("title")) {
                        // set the person title
                        pb.setPersonTitle(pnamePartElement.getText());
                    }
                    if (typeValue.equals("given")) {
                        // set the person givenname
                        pb.setPersonGivenName(pnamePartElement.getText());
                    }
                    if (typeValue.equals("family")) {
                        // set the person family name
                        pb.setPersonFamilyName(pnamePartElement.getText());
                    }
                }
            }
            // location
            Iterator<OMElement> locationIt = partyElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "location"));
            if (locationIt.hasNext()) {
                OMElement locElement = locationIt.next();
                Iterator<OMElement> addressIt = locElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "address"));
                if (addressIt.hasNext()) {
                    OMElement addressElement = addressIt.next();
                    Iterator<OMElement> electronicaddIt = addressElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "electronic"));
                    while (electronicaddIt.hasNext()) {
                        OMElement electronicElement = electronicaddIt.next();
                        OMAttribute typeAtt = electronicElement.getAttribute(new QName("type"));
                        OMElement valueElement = electronicElement.getFirstElement();
                        String typeValue = typeAtt.getAttributeValue();
                        if (typeValue.equals("url")) {
                            // set url
                            pb.setUrl(valueElement.getText());
                        }
                        if (typeValue.equals("email")) {
                            // set email
                            pb.setEmail(valueElement.getText());
                        }
                    }
                }
            }
        }

        pb.setRifcsContent(normalizerifcs(registryObject.toString()));
        // System.out.println("=====> party rifcs content: " + pb.getRifcsContent());
        return pb;
    }

    public List<ProjectBean> getProjects(String nlaId) {
        if (!isConfigured()) {
            throw new WSException("call serviceInit first before getProjects.");
        }

        try {
            OperationClient operationClient = _serviceClient.createClient(operations[2].getName());
            operationClient.getOptions().setAction(ACTION_GET_PROJECTS);
            operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            // operationClient.getOptions().setProperty(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            // create a message context
            MessageContext messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;
            SOAPFactory factory = getFactory(operationClient.getOptions().getSoapVersionURI());
            env = createEnvelope(factory, ACTION_GET_PROJECTS, nlaId);
            //System.out.println("=== send envelope: " + env);
            // adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);

            // set the message context with that soap envelope
            messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            operationClient.addMessageContext(messageContext);

            // execute the operation client
            operationClient.execute(true);

            MessageContext returnMessageContext = operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = returnMessageContext.getEnvelope();
            // System.out.println("===> return env: " + _returnEnv);
            return parseActivitySummary(_returnEnv);
        } catch (AxisFault axe) {
            OMElement fault = axe.getDetail();
            if (fault != null) {
                String message = fault.getFirstElement().getText();
                throw new WSException(message);
            } else {
                throw new WSException(axe);
            }
        } catch (Exception e) {
            loger.error("research master ws failed, " + e);
            throw new WSException(e);
        } finally {
            try {
                if (_serviceClient != null) {
                    _serviceClient.cleanupTransport();
                }
            } catch (Exception ex) {
                loger.error("clean up the service client transport failed, " + ex);
            }
        }

    }

    /**
     * Parse the project summary from the soap envelope
     *
     * @param respEnvelope
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ProjectBean> parseActivitySummary(SOAPEnvelope respEnvelope) {
        OMElement projectResponseElement = respEnvelope.getBody().getFirstElement();
        OMElement resultElement = projectResponseElement.getFirstElement();
        Iterator<OMElement> projectsElements = resultElement.getChildElements();
        List<ProjectBean> projectsList = new ArrayList<ProjectBean>();

        while (projectsElements.hasNext()) {
            OMElement projElement = projectsElements.next();
            Iterator<OMElement> projit = projElement.getChildElements();
            ProjectBean projbean = new ProjectBean();

            while (projit.hasNext()) {
                OMElement pdetails = projit.next();

                String name = pdetails.getLocalName();
                String textValue = pdetails.getText();
                if (name.equals("projectId")) {
                    projbean.setActivityKey(textValue);
                }
                if (name.equals("projectTitle")) {
                    projbean.setTitle(textValue);
                }
                if (name.equals("grantorCode")) {
                    projbean.setGrantCode(textValue);
                }
                if (name.equals("projectDateApplied")) {
                    projbean.setAppliedDate(textValue);
                }
            }
            projectsList.add(projbean);
        }
        return projectsList;
    }

    public ActivityBean getActivityRegistryObject(String activityId) {
        if (!isConfigured()) {
            throw new WSException("call serviceInit first before getActivityRegistryObject.");
        }

        try {
            OperationClient operationClient = _serviceClient.createClient(operations[3].getName());
            operationClient.getOptions().setAction(ACTION_GET_ACTIVITY_OBJECT);
            operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            operationClient.getOptions().setProperty(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            // create a message context
            MessageContext messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;
            SOAPFactory factory = getFactory(operationClient.getOptions().getSoapVersionURI());
            env = createEnvelope(factory, ACTION_GET_ACTIVITY_OBJECT, activityId);

            // adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);

            // set the message context with that soap envelope
            messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            operationClient.addMessageContext(messageContext);

            // execute the operation client
            operationClient.execute(true);

            MessageContext returnMessageContext = operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

            SOAPEnvelope _returnEnv = returnMessageContext.getEnvelope();

            // System.out.println("===> env: " + _returnEnv);
            return parseActivity(_returnEnv);
        } catch (AxisFault axe) {
            OMElement fault = axe.getDetail();
            if (fault != null) {
                String message = fault.getFirstElement().getText();
                throw new WSException(message);
            } else {
                throw new WSException(axe);
            }
        } catch (Exception e) {
            loger.error("research master ws failed, " + e);
            throw new WSException(e);
        } finally {
            try {
                if (_serviceClient != null) {
                    _serviceClient.cleanupTransport();
                }
            } catch (Exception ex) {
                loger.error("clean up the service client transport failed, " + ex);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private ActivityBean parseActivity(SOAPEnvelope respEnvelope) {
        ActivityBean ab = new ActivityBean();

        OMElement activityRegistryObjectsElement = respEnvelope.getBody().getFirstElement();
        //System.out.println("activity: rif-cs: " + activityRegistryObjectsElement);
        OMElement registryObject = activityRegistryObjectsElement.getFirstElement();

        // get the party group
        OMAttribute groupAtt = registryObject.getAttribute(new QName("group"));
        ab.setGroupName(groupAtt.getAttributeValue());

        // set the party key
        Iterator<OMElement> keyit = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "key"));
        if (keyit.hasNext()) {
            OMElement keyElement = keyit.next();
            String key = keyElement.getText();
            ab.setActivityKey(key);
        }

        // originating source
        Iterator<OMElement> origit = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "originatingSource"));
        if (origit.hasNext()) {
            OMElement orig = origit.next();
            OMAttribute typeAtt = orig.getAttribute(new QName("type"));
            String origvalue = orig.getText();
            // set originating source type
            ab.setOriginateSourceType(typeAtt.getAttributeValue());
            // set originating source type value
            ab.setOriginateSourceValue(origvalue);
        }

        // originating source
        Iterator<OMElement> activityIt = registryObject.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "activity"));
        if (activityIt.hasNext()) {
            OMElement activityElement = activityIt.next();
            // identifier
            Iterator<OMElement> identifierIt = activityElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "identifier"));
            if (identifierIt.hasNext()) {
                OMElement identifierEle = identifierIt.next();
                OMAttribute typeAtt = identifierEle.getAttribute(new QName("type"));
                // set the identifier
                ab.setIdentifierType(typeAtt.getAttributeValue());
                ab.setIdentifierValue(identifierEle.getText());
            }

            // activity name and name part
            Iterator<OMElement> pNameIt = activityElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "name"));
            if (pNameIt.hasNext()) {
                OMElement pNameElement = pNameIt.next();
                Iterator<OMElement> pNamePartIt = pNameElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "namePart"));
                if (pNamePartIt.hasNext()) {
                    OMElement pnamePartElement = pNamePartIt.next();
                    // set the name part value - title
                    ab.setNamePartValue(pnamePartElement.getText());
                }
            }
            // desc
            Iterator<OMElement> descIt = activityElement.getChildrenWithName(new QName(DEFAULT_RESPONSE_NAMESPACE, "description"));
            if (descIt.hasNext()) {
                OMElement descElement = descIt.next();
                OMAttribute typeAtt = descElement.getAttribute(new QName("type"));
                // set the desc type
                ab.setDescType(typeAtt.getAttributeValue());
                // set the desc
                ab.setDescValue(descElement.getText());
            }
        }

        ab.setRifcsContent(normalizerifcs(registryObject.toString()));
        // System.out.println("activity rifcs content: " + ab.getRifcsContent());
        return ab;
    }

    private String normalizerifcs(String rifcs) {
        String searchString = "<registryObject xmlns=\"http://ands.org.au/standards/rif-cs/registryObjects\"";
        String replacement = "<registryObject";
        String rifcsParts = StringUtils.replaceOnce(rifcs, searchString, replacement);
        return rifcsParts;
    }

    private SOAPEnvelope createEnvelope(SOAPFactory factory, String operationName, String param) {
        SOAPEnvelope reqEnvelope = factory.getDefaultEnvelope();


        if (operationName.equals(ACTION_GET_NLAID)) {
            OMNamespace nlaIdOMNamespace = factory.createOMNamespace(GET_NLA_ID_NAMESPACE, DEFAULT_ELEMENT_NAMESPACE_PREFIX);
            OMElement nlaOMElement = factory.createOMElement(OMELEMENT_GET_NLAID_NAME, nlaIdOMNamespace);
            OMElement authencateOMElement = factory.createOMElement(OME_PAUTHCATE_USER_NAME, nlaIdOMNamespace);
            authencateOMElement.setText(param);
            nlaOMElement.addChild(authencateOMElement);
            reqEnvelope.getBody().addChild(nlaOMElement);
        }


        if (operationName.equals(ACTION_GET_PARTY_OBJECT)) {
            OMNamespace partyOMNamespace = factory.createOMNamespace(GET_PARTY_REGISTRY_OBJECT_NAMESPACE, DEFAULT_ELEMENT_NAMESPACE_PREFIX);
            OMElement partyOMElement = factory.createOMElement(OMELEMENT_GET_PARTY_NAME, partyOMNamespace);
            OMElement partyIdOMElement = factory.createOMElement(OME_PARTY_ID_NAME, partyOMNamespace);
            partyIdOMElement.setText(param);
            partyOMElement.addChild(partyIdOMElement);
            reqEnvelope.getBody().addChild(partyOMElement);
        }

        if (operationName.equals(ACTION_GET_PROJECTS)) {
            OMNamespace projectOMNamespace = factory.createOMNamespace(GET_PROJECTS_NAMESPACE, DEFAULT_ELEMENT_NAMESPACE_PREFIX);
            OMElement partyOMElement = factory.createOMElement(OMELEMENT_GET_PROJECT_NAME, projectOMNamespace);
            OMElement partyIdOMElement = factory.createOMElement(OME_PRO_NLA_ID_NAME, projectOMNamespace);
            partyIdOMElement.setText(param);
            partyOMElement.addChild(partyIdOMElement);
            reqEnvelope.getBody().addChild(partyOMElement);
        }

        if (operationName.equals(ACTION_GET_ACTIVITY_OBJECT)) {
            OMNamespace activityOMNamespace = factory.createOMNamespace(GET_ACTIVITY_NAMESPACE, DEFAULT_ELEMENT_NAMESPACE_PREFIX);
            OMElement partyOMElement = factory.createOMElement(OMELEMENT_GET_ACTIVITY_NAME, activityOMNamespace);
            OMElement partyIdOMElement = factory.createOMElement(OME_ACTIVITY_ID_NAME, activityOMNamespace);
            partyIdOMElement.setText(param);
            partyOMElement.addChild(partyIdOMElement);
            reqEnvelope.getBody().addChild(partyOMElement);
        }
        return reqEnvelope;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public AxisOperation[] getOperations() {
        return operations;
    }

    public void setOperations(AxisOperation[] operations) {
        this.operations = operations;
    }

    public String getTargetEndpoint() {
        return targetEndpoint;
    }

    public void setTargetEndpoint(String targetEndpoint) {
        this.targetEndpoint = targetEndpoint;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ConfigurationContext getConfigurationContext() {
        return configurationContext;
    }

    public void setConfigurationContext(ConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
    }

    public boolean isUseSeparateListener() {
        return useSeparateListener;
    }

    public void setUseSeparateListener(boolean useSeparateListener) {
        this.useSeparateListener = useSeparateListener;
    }

    public static void main(String[] args) {
        RmWsClient ws = new RmWsClient();
        ws.setTimeout(90000);
        ws.serviceInit();

        long time1 = System.currentTimeMillis();

        String nlaid = null;
        try {
            //
            // nlaid = ws.getNlaId("virginig");
            //  nlaid = ws.getNlaId("pisaac");
            // nlaid = ws.getNlaId("virginig");
            // nlaid = ws.getNlaId("jberinge");

            nlaid = ws.getNlaId("xiyu");
            System.out.println("====> getNlaid: " + nlaid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // nlaid = "MON:0000015173";

        // nlaid = "MON:0000079309";
        long time2 = System.currentTimeMillis();

        for (int i = 0; i < 1; i++) {
            try {
                PartyBean pb = ws.getPartyRegistryObject(nlaid);

                System.out.println("====> party - key: " + pb.getPartyKey() + " - group: " + pb.getGroupName() + " - identifier type: "
                        + pb.getIdentifierType() + " - identifier value: " + pb.getIdentifierValue() + " - person title: " + pb.getPersonTitle()
                        + " - given name: " + pb.getPersonGivenName() + " - family name: " + pb.getPersonFamilyName() + " - orig type:  "
                        + pb.getOriginateSourceType() + " - orig source value: " + pb.getOriginateSourceValue() + " - url: " + pb.getUrl()
                        + " - email: " + pb.getEmail());
                System.out.println("====> party rifcs: " + pb.getRifcsContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long time3 = System.currentTimeMillis();

        try {
            List<ProjectBean> projs = ws.getProjects(nlaid);
            for (ProjectBean p : projs) {
                if (p != null) {
                    System.out.println(" project summary - key: " + p.getActivityKey() + " - applied date: " + p.getAppliedDate() + " - grant code: "
                            + p.getGrantCode() + " - title: " + p.getTitle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time4 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            try {
                ActivityBean ab = ws.getActivityRegistryObject("MON:2009001018");
                System.out.println("====> activity - key: " + ab.getActivityKey() + " - group: " + ab.getGroupName() + " - identifier type: "
                        + ab.getIdentifierType() + " - identifier value: " + ab.getIdentifierValue() + " - orig type:  "
                        + ab.getOriginateSourceType() + " - orig source value: " + ab.getOriginateSourceValue() + " - name part title: "
                        + ab.getNamePartValue() + " - desc type: " + ab.getDescType() + " - desc: " + ab.getDescValue());
                System.out.println("====> activity rifcs: " + ab.getRifcsContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long time5 = System.currentTimeMillis();

        System.out.println(" ws time1: " + (time2 - time1) / 1000);
        System.out.println(" ws time2: " + (time3 - time2) / 1000);
        System.out.println(" ws time3: " + (time4 - time3) / 1000);
        System.out.println(" ws time4: " + (time5 - time4) / 1000);
    }
}
