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

package edu.monash.merc.wsclient.hdl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import edu.monash.merc.exception.DCParseException;
import edu.monash.merc.exception.WSException;
import edu.monash.merc.util.ssl.EasyIgnoreSSLProtocolSocketFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * HandleWSClient class is a handle persistent identifier web service client.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class HandleWSClient {

    private HandleWSConfig hdlWsConfig;

    /**
     * The possible types of properties that can be associated with a handle. Each handle (PID) can carry multiple (max
     * of around 100) properties.
     */

    public HandleWSClient() {

    }

    public HandleWSClient(HandleWSConfig hdlWsConfig) {
        this.hdlWsConfig = hdlWsConfig;
    }

    public String mintHandle(HandleType type, String value) {
        this.validateState();
        this.validateMintHandleArguments(type, value);
        NameValuePair[] queryParams = {new NameValuePair("type", type.value()), new NameValuePair("value", value)};
        String xmlResponse = executeMethod(queryParams, hdlWsConfig.getHandleServiceMinMethod());
        return populateAndsPidResponse(xmlResponse);
    }

    private String executeMethod(NameValuePair[] queryParams, String methodName) {
        try {
            // HttpsURL url = new HttpsURL(/* String user */"", /* String password */"", "test.ands.org.au", 8443);
            //
            // // if no path has been specified (i.e. path value is just '/') then we have:
            // // http://test.org.au:80/mint?type=URL&value=<some value>
            // if ("/".equals(url.getPath())) {
            // url.setPath(url.getPath() + this.hdlWsConfig.getHandleServicePath() + "/" + methodName);
            // } else
            // // if a path has been specified then we have: http://test.org.au:80/<some path>/mint?type=URL&value=<some
            // // value>
            // {
            // url.setPath(url.getPath() + "/" + methodName);
            // }

            String wsurl = this.hdlWsConfig.getHandleServiceHost() + ":" + this.hdlWsConfig.getHandleServicePort() + "/"
                    + this.hdlWsConfig.getHandleServicePath() + "/" + methodName;
            String identityXML = toXML(methodName);
            return executePostMethod(wsurl, queryParams, identityXML);
        } catch (Exception e) {
            throw new WSException(e);
        }
    }

    private String populateAndsPidResponse(String xmlResponse) {
        Document doc = parseXML(xmlResponse);
        boolean success = parseForSuccess(doc);
        if (success) {
            return parseForHandles(doc).get(0);
        } else {
            String message = parseForMessage(doc);
            throw new WSException(message);
        }
    }

    private String toXML(String methodName) {
        return MessageFormat.format("<request name=\"{0}\">" + "<properties>" + "<property name=\"appId\" value=\"" + this.hdlWsConfig.getAppId()
                + "\"/>" + "<property name=\"identifier\" value=\"" + this.hdlWsConfig.getIdentifier() + "\"/>"
                + "<property name=\"authDomain\" value=\"" + this.hdlWsConfig.getAuthDomain() + "\"/>" + "</properties>" + "</request>",
                new Object[]{methodName});
    }

    private String executePostMethod(String postMethodURL, NameValuePair[] params, String identityXML) {
        HttpClient client = new HttpClient();

        if (this.hdlWsConfig.isIgnoreCertError() && this.hdlWsConfig.getHandleServiceHost().startsWith("https://")) {
            Protocol easyhttps = new Protocol("https", new EasyIgnoreSSLProtocolSocketFactory(), 8443);
            Protocol.registerProtocol("https", easyhttps);
        }
        client.getParams().setParameter("http.useragent", this.hdlWsConfig.getAuthDomain());
        client.getParams().setParameter("Content-Type", "text/xml");
        client.getParams().setParameter("Content-Encoding", "UTF-8");
        // System.out.println(" === url: " + postMethodURL);
        PostMethod method = new PostMethod(postMethodURL);

        BufferedReader br = null;
        StringBuffer strBuf = new StringBuffer();
        try {
            RequestEntity entity = new StringRequestEntity(identityXML, "text/xml", "UTF-8");
            client.getParams().setParameter("Content-Length", entity.getContentLength());
            method.setRequestEntity(entity);
            method.setQueryString(params);
            int returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                // consume the response
                method.getResponseBodyAsString();
                throw new Exception("The post method is not implemented by this url");
            } else {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    strBuf.append(readLine);
                }
            }
        } catch (Exception e) {
            throw new WSException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception be) {
                    // ignore whatever
                }
            }
            method.releaseConnection();
        }
        return strBuf.toString();
    }

    private void validateMintHandleArguments(HandleType type, String value) throws IllegalArgumentException {
        // we should never accept a null HandleType
        if (type == null) {
            throw new IllegalArgumentException("The method mintHandle() can not be called with a null HandleType argument.\n");
        }

        // we can only accept the arguments if and only if both arguments are empty or both arguments are not empty
        // (logical equality).
        if (type.isEmpty() != StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(MessageFormat.format("The method mintHandle() can only be called if both arguments are empty "
                    + "or both arguments are not empty:\n type= {0}\n value={1}\n", new Object[]{(type == null) ? null : type.value(), value}));
        }
    }

    private void validateState() throws IllegalStateException {
        StringBuffer errorMsg = new StringBuffer();
        if (StringUtils.isBlank(this.hdlWsConfig.getHandleServiceHost())) {
            errorMsg.append("The host name of the ANDS handle service has not been provided.\n");
        }
        if (this.hdlWsConfig.getHandleServicePort() == 0) {
            errorMsg.append("The port of the ANDS handle service has not been provided.\n");
        }
        if (StringUtils.isBlank(this.hdlWsConfig.getHandleServicePath())) {
            errorMsg.append("The path of the ANDS handle service has not been provided.\n");
        }
        if (StringUtils.isBlank(this.hdlWsConfig.getHandleServiceMinMethod())) {
            errorMsg.append("The mint method of the ANDS handle service has not been provided.\n");
        }

        if (StringUtils.isBlank(this.hdlWsConfig.getAppId())) {
            errorMsg.append("The application id of the caller has not been provided.\n");
        }
        if (StringUtils.isBlank(this.hdlWsConfig.getIdentifier())) {
            errorMsg.append("The identifier id of the caller has not been provided.\n");
        }
        if (StringUtils.isBlank(this.hdlWsConfig.getAuthDomain())) {
            errorMsg.append("The authDomain of the caller has not been provided. ");
        }
        // if we have error messages, throw the exception
        if (errorMsg.length() != 0) {
            throw new IllegalStateException(errorMsg.toString());
        }
    }

    private static Document parseXML(String xmlResponse) {
        StringReader reader = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            reader = new StringReader(xmlResponse);
            InputSource inputSource = new InputSource(reader);
            Document doc = builder.parse(inputSource);
            return doc;
        } catch (Exception e) {
            throw new DCParseException(e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private List<String> parseForHandles(Document doc) {
        List<String> handles = new ArrayList<String>();

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        // look for the tag: <identifier handle="XXXX/X">
        try {
            XPathExpression expr = xpath.compile("//identifier[@handle]");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                for (int j = 0; j < nodes.item(i).getAttributes().getLength(); j++) {
                    Node node = (Node) nodes.item(i).getAttributes().item(j);
                    String handle = node.getNodeValue();
                    handles.add(handle);
                }
            }
        } catch (Exception e) {
            throw new DCParseException(e);
        }
        return handles;
    }

    private boolean parseForSuccess(Document doc) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        // whether <response type="success"> was returned
        try {
            XPathExpression expr = xpath.compile("//response[@type]");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                for (int j = 0; j < nodes.item(i).getAttributes().getLength(); ) {
                    Node node = (Node) nodes.item(i).getAttributes().item(j);
                    j++;
                    return "success".equals(node.getNodeValue());
                }
            }
        } catch (Exception e) {
            throw new DCParseException(e);
        }
        return false;
    }

    private String parseForMessage(Document doc) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        try {
            XPathExpression expr = xpath.compile("//response/message");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            // for (int i = 0; i < nodes.getLength();) {
            // return nodes.item(i).getTextContent();
            // }
            return nodes.item(0).getTextContent();
        } catch (Exception e) {
            throw new DCParseException(e);
        }
    }

    public HandleWSConfig getHdlWsConfig() {
        return hdlWsConfig;
    }

    public void setHdlWsConfig(HandleWSConfig hdlWsConfig) {
        this.hdlWsConfig = hdlWsConfig;
    }

    public static void main(String[] args) {
        HandleWSClient hdlwsclient = new HandleWSClient();
        HandleWSConfig hdlWsConfig = new HandleWSConfig();
        hdlWsConfig.setIgnoreCertError(true);
        hdlWsConfig.setHandleServiceHost("https://test.ands.org.au");
        hdlWsConfig.setHandleServicePort(8443);
        hdlWsConfig.setHandleServicePath("pids");
        hdlWsConfig.setHandleServiceMinMethod("mint");
        hdlWsConfig.setAppId("c4b16dc56797f1dfbf545e2397ac7b6bcc54b8ec");
        hdlWsConfig.setIdentifier("ecosystem");
        hdlWsConfig.setAuthDomain("ozflux.its.monash.edu.au");
        hdlwsclient.setHdlWsConfig(hdlWsConfig);
        System.out.println("response: "
                + hdlwsclient.mintHandle(HandleType.URL,
                "http://localhost:8080/ands/pub/viewColDetails.jspx?collection.id=50&collection.owner.id=4&viewType=anonymous"));
    }
}
