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

import edu.monash.merc.domain.Reporter;
import edu.monash.merc.domain.User;

import java.io.Serializable;
import java.util.List;

/**
 * ReporterBean class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ReporterBean implements Serializable{

    private String reporterName;

    private List<Reporter> reporters;

    private User user;

    private String appName;

    private String serverName;

    private boolean sendMailRequired;

    private String fromMail;

    private String toMail;

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public List<Reporter> getReporters() {
        return reporters;
    }

    public void setReporters(List<Reporter> reporters) {
        this.reporters = reporters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isSendMailRequired() {
        return sendMailRequired;
    }

    public void setSendMailRequired(boolean sendMailRequired) {
        this.sendMailRequired = sendMailRequired;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }
}
