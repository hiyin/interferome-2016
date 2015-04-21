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
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * A kind of HostConfiguration that gets its Host from a factory. This is useful
 * for integrating a specialized Protocol or SocketFactory; for example, a
 * SecureSocketFactory that authenticates via SSL. Use
 * HttpClient.setHostConfiguration to install a HostConfigurationWithHostFactory
 * that contains the specialized HostFactory, Protocol or SocketFactory.
 * <p/>
 * An alternative is to use Protocol.registerProtocol to register a specialized
 * Protocol. But that has drawbacks: it makes it hard to integrate modules (e.g.
 * web applications in a servlet container) with different strategies, because
 * they share the specialized Protocol (Protocol.PROTOCOLS is static). And it
 * can't support different Protocols for different hosts or ports (since the
 * host and port aren't parameters to Protocol.getProtocol).
 *
 * @author John Kristian
 */
class HostConfigurationWithHostFactory extends HostConfiguration {
    public HostConfigurationWithHostFactory(HttpHostFactory factory) {
        this.factory = factory;
    }

    private HostConfigurationWithHostFactory(HostConfigurationWithHostFactory that) {
        super(that);
        this.factory = that.factory;
    }

    private final HttpHostFactory factory;

    public Object clone() {
        return new HostConfigurationWithHostFactory(this);
    }

    private static final String DEFAULT_SCHEME = new String(HttpURL.DEFAULT_SCHEME);

    public void setHost(String host) {
        setHost(host, Protocol.getProtocol(DEFAULT_SCHEME).getDefaultPort());
    }

    public void setHost(final String host, int port) {
        setHost(host, port, DEFAULT_SCHEME);
    }

    public synchronized void setHost(String host, int port, String scheme) {
        setHost(factory.getHost(this, scheme, host, port));
    }

}
