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
package edu.monash.merc.dto;

import java.io.Serializable;

/**
 * LDAPProperty class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class LDAPProperty implements Serializable {
    private boolean ldapSupported;

    private String ldapFactory;

    private String ldapServer;

    private String protocol;

    private String authentication;

    private String baseDN;

    private boolean bindBaseDnRequired;

    private String attUID;

    private String attMail;

    private String attCN;

    private String attGender;

    private String attPersonalTitle;

    private String attSn;

    private String attGivenname;

    public boolean isLdapSupported() {
        return ldapSupported;
    }

    public void setLdapSupported(boolean ldapSupported) {
        this.ldapSupported = ldapSupported;
    }

    public String getLdapFactory() {
        return ldapFactory;
    }

    public void setLdapFactory(String ldapFactory) {
        this.ldapFactory = ldapFactory;
    }

    public String getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(String ldapServer) {
        this.ldapServer = ldapServer;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public boolean isBindBaseDnRequired() {
        return bindBaseDnRequired;
    }

    public void setBindBaseDnRequired(boolean bindBaseDnRequired) {
        this.bindBaseDnRequired = bindBaseDnRequired;
    }

    public String getAttUID() {
        return attUID;
    }

    public void setAttUID(String attUID) {
        this.attUID = attUID;
    }

    public String getAttMail() {
        return attMail;
    }

    public void setAttMail(String attMail) {
        this.attMail = attMail;
    }

    public String getAttCN() {
        return attCN;
    }

    public void setAttCN(String attCN) {
        this.attCN = attCN;
    }

    public String getAttGender() {
        return attGender;
    }

    public void setAttGender(String attGender) {
        this.attGender = attGender;
    }

    public String getAttPersonalTitle() {
        return attPersonalTitle;
    }

    public void setAttPersonalTitle(String attPersonalTitle) {
        this.attPersonalTitle = attPersonalTitle;
    }

    public String getAttSn() {
        return attSn;
    }

    public void setAttSn(String attSn) {
        this.attSn = attSn;
    }

    public String getAttGivenname() {
        return attGivenname;
    }

    public void setAttGivenname(String attGivenname) {
        this.attGivenname = attGivenname;
    }
}
