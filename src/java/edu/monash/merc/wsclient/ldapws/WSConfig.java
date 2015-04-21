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

package edu.monash.merc.wsclient.ldapws;

/**
 * Created by simonyu on 2/06/2014.
 */
public class WSConfig {

    /**
     * ignore the certificate error flag
     */
    private boolean ignoreCertError;

    /**
     * The LDAP Authentication Web Service Host Name
     */
    private String ldapAuthenServiceHost;

    /**
     * The LDAP Authentication Web Service port number.
     */
    private int ldapAuthenServicePort;

    /**
     * The LDAP Authentication Web Service  path name (web application context name).
     */
    private String ldapAuthenServicePath;

    /**
     * The LDAP Authentication Web Service  authentication method
     */
    private String ldapAuthenServiceMethod;


    public boolean isIgnoreCertError() {
        return ignoreCertError;
    }

    public void setIgnoreCertError(boolean ignoreCertError) {
        this.ignoreCertError = ignoreCertError;
    }

    public String getLdapAuthenServiceHost() {
        return ldapAuthenServiceHost;
    }

    public void setLdapAuthenServiceHost(String ldapAuthenServiceHost) {
        this.ldapAuthenServiceHost = ldapAuthenServiceHost;
    }

    public int getLdapAuthenServicePort() {
        return ldapAuthenServicePort;
    }

    public void setLdapAuthenServicePort(int ldapAuthenServicePort) {
        this.ldapAuthenServicePort = ldapAuthenServicePort;
    }

    public String getLdapAuthenServicePath() {
        return ldapAuthenServicePath;
    }

    public void setLdapAuthenServicePath(String ldapAuthenServicePath) {
        this.ldapAuthenServicePath = ldapAuthenServicePath;
    }

    public String getLdapAuthenServiceMethod() {
        return ldapAuthenServiceMethod;
    }

    public void setLdapAuthenServiceMethod(String ldapAuthenServiceMethod) {
        this.ldapAuthenServiceMethod = ldapAuthenServiceMethod;
    }
}
