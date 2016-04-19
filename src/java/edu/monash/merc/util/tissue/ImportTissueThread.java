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

package edu.monash.merc.util.tissue;

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.TissueExpression;
import edu.monash.merc.service.DMService;
import edu.monash.merc.dto.TissueCounter;
import edu.monash.merc.dto.TissueBean;
import org.apache.log4j.Logger;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12/02/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportTissueThread implements Runnable {

    private Thread tissueThread;

    private DMService dmService;

    private TissueBean tissueBean;

    private static String MAIL_SUBJECT = "Tissue Importing Results";

    private static String MAIL_TEMPLATE_FILE = "tissueImportResultMailTemplate.ftl";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ImportTissueThread() {
        this.tissueThread = new Thread(this);
    }

    public ImportTissueThread(DMService dmService, TissueBean tissueBean) {
        this.tissueThread = new Thread(this);
        this.dmService = dmService;
        this.tissueBean = tissueBean;
    }

    public void importTissue() {
        if (this.tissueThread == null) {
            this.tissueThread = new Thread(this);
        }
        this.tissueThread.start();
    }

    @Override
    public void run() {
        Thread runThread = Thread.currentThread();
        if ((tissueThread != null) && (tissueThread == runThread)) {
            //here we use the singleton process id which mean only one importing tissue process allowed at one time.
            if (registerProcess(TissueManager.PROCESS_ID)) {
                String eventMsg = null;
                try {
                    List<TissueExpression> tissues = this.tissueBean.getTissues();
                    if (tissues != null && tissues.size() > 0) {
                        TissueCounter tissueCounter = this.dmService.importAllTissues(tissues);
                        eventMsg = genEventMsg(tissueCounter);
                    }

                } catch (Exception ex) {
                    logger.error("Failed to importing the tissue, " + ex);
                    eventMsg = "Failed to importing the tissue, " + ex.getMessage();
                } finally {
                    // finally to unlock this process.
                    unRegisterProcess(TissueManager.PROCESS_ID);
                }
                //save the audit event
                recordAuditEventForImport(eventMsg);
                boolean sendMailRequired = this.tissueBean.isSendMailRequired();
                if (sendMailRequired) {
                    try {
                        sendMail(eventMsg);
                    } catch (Exception mex) {
                        logger.error("Failed to send the tissue importing result to user, " + mex.getMessage());
                    }
                }
            } else {
                logger.error("Another tissue importing process working in progress");
            }
        }
    }

    private boolean registerProcess(long processId) {
        TissueProcess process = new TissueProcess();
        process.setProcessId(processId);
        return TissueManager.registerProcess(processId, process);
    }

    private boolean unRegisterProcess(long processId) {
        return TissueManager.unRegisterProcess(processId);
    }

    private String genEventMsg(TissueCounter counter) {
        String tissueName = this.tissueBean.getTissueName();
        String eventMsg = "The " + tissueName + " tissue file has been imported successfully. A total of " + counter.getTotalNew() + " tissue have been added and a total of " + counter.getTotalUpdated() + " tissue have been updated";
        return eventMsg;
    }

    // set the auditing event
    private void recordAuditEventForImport(String msg) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        String tissueName = this.tissueBean.getTissueName();
        User user = this.tissueBean.getUser();
        ev.setEvent(msg);
        ev.setOperator(user);
        ev.setEventOwner(user);
        try {
            this.dmService.saveAuditEvent(ev);
        } catch (Exception ex) {
            logger.error("Failed to save the auditing event of the tissue importing - " + msg + ", " + ex.getMessage());
        }
    }

    private void sendMail(String msg) {
        String appName = this.tissueBean.getAppName();
        String serverName = this.tissueBean.getServerName();
        String fromMail = this.tissueBean.getFromMail();
        String toMail = this.tissueBean.getToMail();
        String userName = this.tissueBean.getUser().getDisplayName();
        String tissueName = this.tissueBean.getTissueName();
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userName);
        templateMap.put("TissueName", tissueName);
        templateMap.put("ImportingMsg", msg);
        templateMap.put("SiteName", serverName);
        templateMap.put("AppName", appName);
        this.dmService.sendMail(fromMail, toMail, MAIL_SUBJECT, templateMap, MAIL_TEMPLATE_FILE, true);
    }
}
