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

package edu.monash.merc.util.reporter;

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.Reporter;
import edu.monash.merc.domain.User;
import edu.monash.merc.dto.RepCounter;
import edu.monash.merc.dto.ReporterBean;
import edu.monash.merc.service.DMService;
import org.apache.log4j.Logger;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImportReporterThread class which runs importing annotation reporter backend process.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ImportReporterThread implements Runnable {

    private Thread reporterThread;

    private DMService dmService;

    private ReporterBean reporterBean;

    private static String MAIL_SUBJECT = "Annotation Importing Results";

    private static String MAIL_TEMPLATE_FILE = "annotationImportResultMailTemplate.ftl";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ImportReporterThread() {
        this.reporterThread = new Thread(this);
    }

    public ImportReporterThread(DMService dmService, ReporterBean reporterBean) {
        this.reporterThread = new Thread(this);
        this.dmService = dmService;
        this.reporterBean = reporterBean;
    }

    public void importReporter() {
        if (this.reporterThread == null) {
            this.reporterThread = new Thread(this);
        }
        this.reporterThread.start();
    }

    @Override
    public void run() {
        Thread runThread = Thread.currentThread();
        if ((reporterThread != null) && (reporterThread == runThread)) {
            //here we use the singleton process id which mean only one importing reporter process allowed at one time.
            if (registerProcess(ReporterManager.PROCESS_ID)) {
                String eventMsg = null;
                try {
                    List<Reporter> reporters = this.reporterBean.getReporters();
                    if (reporters != null && reporters.size() > 0) {
                        RepCounter reporterCounter = this.dmService.importAllReporters(reporters);
                        eventMsg = genEventMsg(reporterCounter);
                    }
                } catch (Exception ex) {
                    logger.error("Failed to importing the annotations, " + ex);
                    eventMsg = "Failed to importing the annotations, " + ex.getMessage();
                } finally {
                    // finally to unlock this process.
                    unRegisterProcess(ReporterManager.PROCESS_ID);
                }
                //save the audit event
                recordAuditEventForImport(eventMsg);
                boolean sendMailRequired = this.reporterBean.isSendMailRequired();
                if (sendMailRequired) {
                    try {
                        sendMail(eventMsg);
                    } catch (Exception mex) {
                        logger.error("Failed to send the annotation importing result to user, " + mex.getMessage());
                    }
                }
            } else {
                logger.error("Another annotation importing process working in progress");
            }
        }
    }

    private boolean registerProcess(long processId) {
        ReporterProcess process = new ReporterProcess();
        process.setProcessId(processId);
        return ReporterManager.registerProcess(processId, process);
    }

    private boolean unRegisterProcess(long processId) {
        return ReporterManager.unRegisterProcess(processId);
    }

    private String genEventMsg(RepCounter counter) {
        String reporterName = this.reporterBean.getReporterName();
        String eventMsg = "The " + reporterName + " annotation file has been imported successfully. A total of " + counter.getTotalNew() + " annotation have been added and a total of " + counter.getTotalUpdated() + " annotation have been updated";
        return eventMsg;
    }

    // set the auditing event
    private void recordAuditEventForImport(String msg) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        String reporterName = this.reporterBean.getReporterName();
        User user = this.reporterBean.getUser();
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
        String appName = this.reporterBean.getAppName();
        String serverName = this.reporterBean.getServerName();
        String fromMail = this.reporterBean.getFromMail();
        String toMail = this.reporterBean.getToMail();
        String userName = this.reporterBean.getUser().getDisplayName();
        String reporterName = this.reporterBean.getReporterName();
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userName);
        templateMap.put("ReporterName", reporterName);
        templateMap.put("ImportingMsg", msg);
        templateMap.put("SiteName", serverName);
        templateMap.put("AppName", appName);
        this.dmService.sendMail(fromMail, toMail, MAIL_SUBJECT, templateMap, MAIL_TEMPLATE_FILE, true);
    }
}