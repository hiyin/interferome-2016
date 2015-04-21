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

package edu.monash.merc.util.tfsite;

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.TFSite;
import edu.monash.merc.domain.User;
import edu.monash.merc.dto.TFSiteBean;
import edu.monash.merc.dto.TfSiteCounter;
import edu.monash.merc.service.DMService;
import org.apache.log4j.Logger;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImportTFSiteThread class which runs importing annotation tfsite backend process.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ImportTFSiteThread implements Runnable {

    private Thread tfSiteThread;

    private DMService dmService;

    private TFSiteBean tfSiteBean;

    private static String MAIL_SUBJECT = "Transcript Factor Importing Results";

    private static String MAIL_TEMPLATE_FILE = "tfSiteImportResultMailTemplate.ftl";


    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ImportTFSiteThread() {
        this.tfSiteThread = new Thread(this);
    }

    public ImportTFSiteThread(DMService dmService, TFSiteBean tfSiteBean) {
        this.tfSiteThread = new Thread(this);
        this.dmService = dmService;
        this.tfSiteBean = tfSiteBean;
    }

    public void importTFSite() {
        if (this.tfSiteThread == null) {
            this.tfSiteThread = new Thread(this);
        }
        this.tfSiteThread.start();
    }

    @Override
    public void run() {
        Thread runThread = Thread.currentThread();
        if ((tfSiteThread != null) && (tfSiteThread == runThread)) {
            //here we use the singleton process id which mean only one importing tfSite process allowed at one time.
            if (registerProcess(TFSiteManager.PROCESS_ID)) {
                String eventMsg = null;
                try {
                    List<TFSite> tfSites = this.tfSiteBean.getTfSites();
                    if (tfSites != null && tfSites.size() > 0) {
                        TfSiteCounter tfSiteCounter = this.dmService.importAllTFSites(tfSites);
                        eventMsg = genEventMsg(tfSiteCounter);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Failed to importing the transcription factor site, " + ex);
                    eventMsg = "Failed to importing the transcription factor site, " + ex.getMessage();
                } finally {
                    // finally to unlock this process.
                    unRegisterProcess(TFSiteManager.PROCESS_ID);
                }
                //save the audit event
                recordAuditEventForImport(eventMsg);
                boolean sendMailRequired = this.tfSiteBean.isSendMailRequired();
                if (sendMailRequired) {
                    try {
                        sendMail(eventMsg);
                    } catch (Exception mex) {
                        logger.error("Failed to send the transcription factor site importing result to user, " + mex.getMessage());
                    }
                }
            } else {
                logger.error("Another transcription factor site importing process working in progress");
            }
        }
    }

    private boolean registerProcess(long processId) {
        TFSiteProcess process = new TFSiteProcess();
        process.setProcessId(processId);
        return TFSiteManager.registerProcess(processId, process);
    }

    private boolean unRegisterProcess(long processId) {
        return TFSiteManager.unRegisterProcess(processId);
    }

    private String genEventMsg(TfSiteCounter counter) {
        String tfSiteName = this.tfSiteBean.getTfSiteName();
        String eventMsg = "The " + tfSiteName + " transcription factor file has been imported successfully. A total of " + counter.getTotalNew() + " transcription factors have been added and a total of " + counter.getTotalUpdated() + " transcription factor have been updated";
        return eventMsg;
    }

    // set the auditing event
    private void recordAuditEventForImport(String msg) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        String tfSiteName = this.tfSiteBean.getTfSiteName();
        User user = this.tfSiteBean.getUser();
        ev.setEvent(msg);
        ev.setOperator(user);
        ev.setEventOwner(user);
        try {
            this.dmService.saveAuditEvent(ev);
        } catch (Exception ex) {
            logger.error("Failed to save the auditing event of the annotation importing - " + msg + ", " + ex.getMessage());
        }
    }

    private void sendMail(String msg) {
        String appName = this.tfSiteBean.getAppName();
        String serverName = this.tfSiteBean.getServerName();
        String fromMail = this.tfSiteBean.getFromMail();
        String toMail = this.tfSiteBean.getToMail();
        String userName = this.tfSiteBean.getUser().getDisplayName();
        String tfSiteName = this.tfSiteBean.getTfSiteName();
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userName);
        templateMap.put("tfSiteName", tfSiteName);
        templateMap.put("ImportingMsg", msg);
        templateMap.put("SiteName", serverName);
        templateMap.put("AppName", appName);
        this.dmService.sendMail(fromMail, toMail, MAIL_SUBJECT, templateMap, MAIL_TEMPLATE_FILE, true);
    }
}