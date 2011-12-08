/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//httpclient/src/contrib/org/apache/commons/httpclient/contrib/ssl/EasySSLProtocolSocketFactory.java,v 1.7 2004/06/11 19:26:27 olegk Exp $
 * $Revision: 480424 $
 * $Date: 2006-11-29 06:56:49 +0100 (Wed, 29 Nov 2006) $
 *
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package edu.monash.merc.util.ssl;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * A source of HttpHosts.
 */
public class HttpHostFactory {
    /**
     * The default factory.
     */
    public static final HttpHostFactory DEFAULT = new HttpHostFactory(null, // httpProtocol
            new Protocol(new String(HttpsURL.DEFAULT_SCHEME), (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), HttpsURL.DEFAULT_PORT));

    public HttpHostFactory(Protocol httpProtocol, Protocol httpsProtocol) {
        this.httpProtocol = httpProtocol;
        this.httpsProtocol = httpsProtocol;
    }

    protected final Protocol httpProtocol;

    protected final Protocol httpsProtocol;

    /**
     * Get a host for the given parameters. This method need not be thread-safe.
     */
    public HttpHost getHost(HostConfiguration old, String scheme, String host, int port) {
        return new HttpHost(host, port, getProtocol(old, scheme, host, port));
    }

    /**
     * Get a Protocol for the given parameters. The default implementation selects a protocol based only on the scheme.
     * Subclasses can do fancier things, such as select SSL parameters based on the host or port. This method must not
     * return null.
     */
    protected Protocol getProtocol(HostConfiguration old, String scheme, String host, int port) {
        final Protocol oldProtocol = old.getProtocol();
        if (oldProtocol != null) {
            final String oldScheme = oldProtocol.getScheme();
            if (oldScheme == scheme || (oldScheme != null && oldScheme.equalsIgnoreCase(scheme))) {
                // The old protocol has the desired scheme.
                return oldProtocol; // Retain it.
            }
        }
        Protocol newProtocol = (scheme != null && scheme.toLowerCase().endsWith("s")) ? httpsProtocol : httpProtocol;
        if (newProtocol == null) {
            newProtocol = Protocol.getProtocol(scheme);
        }
        return newProtocol;
    }

}
