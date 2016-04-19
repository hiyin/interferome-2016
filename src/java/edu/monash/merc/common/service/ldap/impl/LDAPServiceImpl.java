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
package edu.monash.merc.common.service.ldap.impl;

import javax.annotation.PostConstruct;

import edu.monash.merc.common.service.ldap.LDAPService;
import edu.monash.merc.wsclient.ldapws.LdapWSClient;
import edu.monash.merc.wsclient.ldapws.WSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.dto.LDAPProperty;
import edu.monash.merc.dto.LDAPUser;
import edu.monash.merc.util.ldap.LDAPUtil;

/**
 * Interface LDAPService Implementation
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

@Scope("prototype")
@Service
public class LDAPServiceImpl implements LDAPService {

    @Autowired
    private LDAPUtil ldapUtil;

    @Autowired
    private AppPropSettings appSettings;

    private LdapWSClient ldapWSClient;

    private boolean ldapWsEnabled = false;

    public void setLdapUtil(LDAPUtil ldapUtil) {
        this.ldapUtil = ldapUtil;
    }

    public void setAppSettings(AppPropSettings appSettings) {
        this.appSettings = appSettings;
    }

    @PostConstruct
    public void ldapEnvInit() {
        ldapWsEnabled = Boolean.valueOf(appSettings.getPropValue(AppPropSettings.LDAP_AUTH_WS_ENABLED));
        if (ldapWsEnabled) {
            String ldapWsHost = appSettings.getPropValue(AppPropSettings.LDAP_AUTH_WS_HOST);
            int ldapWsPort = Integer.valueOf(appSettings.getPropValue(AppPropSettings.LDAP_AUTH_WS_PORT)).intValue();
            boolean ignoreCertError = Boolean.valueOf(appSettings.getPropValue(AppPropSettings.LDAP_AUTH_WS_CERT_ERROR_IGNORE));
            WSConfig ldapWsConfig = new WSConfig();
            ldapWsConfig.setLdapAuthenServiceHost(ldapWsHost);
            ldapWsConfig.setLdapAuthenServicePort(ldapWsPort);
            ldapWsConfig.setIgnoreCertError(ignoreCertError);
            ldapWSClient = new LdapWSClient(ldapWsConfig);
        } else {
            LDAPProperty ldapProp = new LDAPProperty();
            ldapProp.setLdapFactory(appSettings.getPropValue(AppPropSettings.LDAP_FACTORY));
            ldapProp.setLdapServer(appSettings.getPropValue(AppPropSettings.LDAP_SERVER_URL));
            ldapProp.setProtocol(appSettings.getPropValue(AppPropSettings.LDAP_SECURITY_PROTOCOL));
            ldapProp.setAuthentication(appSettings.getPropValue(AppPropSettings.LDAP_AUTHENTICATION));
            ldapProp.setBaseDN(appSettings.getPropValue(AppPropSettings.LDAP_BASE_DN));
            ldapProp.setBindBaseDnRequired(Boolean.valueOf(appSettings.getPropValue(AppPropSettings.LDAP_BIND_BASE_DN_REQUIRED)));
            ldapProp.setAttUID(appSettings.getPropValue(AppPropSettings.LDAP_UID_ATTR_NAME));
            ldapProp.setAttMail(appSettings.getPropValue(AppPropSettings.LDAP_MAIL_ATTR_NAME));
            ldapProp.setAttGender(appSettings.getPropValue(AppPropSettings.LDAP_GENDER_ATTR_NAME));
            ldapProp.setAttCN(appSettings.getPropValue(AppPropSettings.LDAP_CN_ATTR_NAME));
            ldapProp.setAttSn(appSettings.getPropValue(AppPropSettings.LDAP_SN_ATTR_NAME));
            ldapProp.setAttGivenname(appSettings.getPropValue(AppPropSettings.LDAP_GIVENNAME_ATTR_NAME));
            ldapProp.setAttPersonalTitle(appSettings.getPropValue(AppPropSettings.LDAP_PERSONAL_TITLE_ATTR_NAME));
            this.ldapUtil.initEnv(ldapProp);
        }
    }

    @Override
    public boolean ldapUserLogin(String uid, String password) {
        if (ldapWsEnabled) {
            return this.ldapWSClient.login(uid, password);
        } else {
            return this.ldapUtil.login(uid, password);
        }
    }

    @Override
    public LDAPUser validateLdapUser(String uid, String password) {
        if (ldapWsEnabled) {
            return this.ldapWSClient.verifyLdapUser(uid, password);
        } else {
            return this.ldapUtil.validateLdapUser(uid, password);
        }
    }

    @Override
    public LDAPUser searchLdapUser(String cnOrEmail) {
        if (ldapWsEnabled) {
            return this.ldapWSClient.lookup(cnOrEmail);
        } else {
            return this.ldapUtil.findUserInfo(cnOrEmail);
        }
    }
}
