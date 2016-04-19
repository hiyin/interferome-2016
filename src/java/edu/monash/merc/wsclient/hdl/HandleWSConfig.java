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

/**
 * HandleWSConfig class is a handle persistent identifier web service client configuration class.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class HandleWSConfig {

    private boolean ignoreCertError;

    /**
     * The ANDS Persisten Identifier Web Service Host Name
     */
    private String handleServiceHost;

    /**
     * The ANDS Persistent Identifier port number.
     */
    private int handleServicePort;

    /**
     * The ANDS Persistent Identifier path name (web application context name).
     */
    private String handleServicePath;

    /**
     * The ANDS Persistent Identifier mint method
     */
    private String handleServiceMinMethod;

    /**
     * the application identifier registered by ANDS
     */
    private String appId;

    /**
     * The authentication identifier registered by ANDS
     */
    private String identifier;

    /**
     * The authentication domain registered by ANDS
     */
    private String authDomain;

    public boolean isIgnoreCertError() {
        return ignoreCertError;
    }

    public void setIgnoreCertError(boolean ignoreCertError) {
        this.ignoreCertError = ignoreCertError;
    }

    public String getHandleServiceHost() {
        return handleServiceHost;
    }

    public void setHandleServiceHost(String handleServiceHost) {
        this.handleServiceHost = handleServiceHost;
    }

    public int getHandleServicePort() {
        return handleServicePort;
    }

    public void setHandleServicePort(int handleServicePort) {
        this.handleServicePort = handleServicePort;
    }

    public String getHandleServicePath() {
        return handleServicePath;
    }

    public void setHandleServicePath(String handleServicePath) {
        this.handleServicePath = handleServicePath;
    }

    public String getHandleServiceMinMethod() {
        return handleServiceMinMethod;
    }

    public void setHandleServiceMinMethod(String handleServiceMinMethod) {
        this.handleServiceMinMethod = handleServiceMinMethod;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAuthDomain() {
        return authDomain;
    }

    public void setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
    }
}
