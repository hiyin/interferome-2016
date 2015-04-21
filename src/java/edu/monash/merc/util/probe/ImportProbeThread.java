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

package edu.monash.merc.util.probe;

import edu.monash.merc.domain.AuditEvent;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.User;
import edu.monash.merc.dto.ProbCounter;
import edu.monash.merc.dto.ProbeBean;
import edu.monash.merc.service.DMService;
import org.apache.log4j.Logger;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: irinar
 * Date: 14/12/12
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportProbeThread implements Runnable {
    private Thread probeThread;

    private DMService dmService;

    private ProbeBean probeBean;

    private static String MAIL_SUBJECT = "Probes Importing Results";

    private static String MAIL_TEMPLATE_FILE = "probeImportResultMailTemplate.ftl";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ImportProbeThread() {
        this.probeThread = new Thread(this);
    }

    public ImportProbeThread(DMService dmService, ProbeBean probeBean) {
        this.probeThread = new Thread(this);
        this.dmService = dmService;
        this.probeBean = probeBean;
    }

    public void importProbe() {
        if (this.probeThread == null) {
            this.probeThread = new Thread(this);
        }
        this.probeThread.start();
    }

    @Override
    public void run() {
        Thread runThread = Thread.currentThread();
        if ((probeThread != null) && (probeThread == runThread)) {
            //here we use the singleton process id which mean only one importing probes process allowed at one time.
            if (registerProcess(ProbeManager.PROCESS_ID)) {
                String eventMsg = null;
                try {
                    List<Probe> probes = this.probeBean.getProbes();
                    if (probes != null && probes.size() > 0) {
                        ProbCounter probeCounter = this.dmService.importAllProbes(probes);
                        eventMsg = genEventMsg(probeCounter);
                        eventMsg += probeImportErrors();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Failed to importing the probes, " + ex);
                    eventMsg = "Failed to importing the probes, " + ex.getMessage();
                } finally {
                    // finally to unlock this process.
                    unRegisterProcess(ProbeManager.PROCESS_ID);
                }
                //save the audit event
                recordAuditEventForImport(eventMsg);
                boolean sendMailRequired = this.probeBean.isSendMailRequired();
                if (sendMailRequired) {
                    try {
                        sendMail(eventMsg);
                    } catch (Exception mex) {
                        logger.error("Failed to send the probes importing result to user, " + mex.getMessage());
                    }
                }
            } else {
                logger.error("Another probes importing process working in progress");
            }
        }
    }

    /**
     * @return    A error message string representing any probes that didn't import successfully, or the empty string
     * if no errors occurred.
     */
    private String probeImportErrors() {
        List<Probe> failedProbes = dmService.getProbeImportErrors();
        List<String> failureMessages = dmService.getProbeImportErrorMessages();
        String err = "";
        if (failedProbes.size() > 0) {
            err = "The following probes failed to import: \n\n";
            err += "ProbeID, EnsemblId, Species, Reason for failure";
            for (int i=0; i<failedProbes.size(); i++){
                Probe failure = failedProbes.get(i);
                String failureMessage = failureMessages.get(i);
                err += "\n"+failure.getProbeId() +", "+failure.getEnsemblId()+", "+failure.getSpeciesName()+", "+failureMessage;
            }
            err += "\n";
        }
        return err;
    }

    private boolean registerProcess(long processId) {
        ProbeProcess process = new ProbeProcess();
        process.setProcessId(processId);
        return ProbeManager.registerProcess(processId, process);
    }

    private boolean unRegisterProcess(long processId) {
        return ProbeManager.unRegisterProcess(processId);
    }

    private String genEventMsg(ProbCounter counter) {
        String probeName = this.probeBean.getProbeName();
        String eventMsg = "The " + probeName + " probes file has been imported successfully. A total of " + counter.getTotalNew() + " probes have been added and a total of " + counter.getTotalUpdated() + " probes have been updated";
        return eventMsg;
    }

    // set the auditing event
    private void recordAuditEventForImport(String msg) {
        AuditEvent ev = new AuditEvent();
        ev.setCreatedTime(GregorianCalendar.getInstance().getTime());
        String probeName = this.probeBean.getProbeName();
        User user = this.probeBean.getUser();
        ev.setEvent(msg);
        ev.setOperator(user);
        ev.setEventOwner(user);
        try {
            this.dmService.saveAuditEvent(ev);
        } catch (Exception ex) {
            logger.error("Failed to save the auditing event of the probe importing - " + msg + ", " + ex.getMessage());
        }
    }

    private void sendMail(String msg) {
        String appName = this.probeBean.getAppName();
        String serverName = this.probeBean.getServerName();
        String fromMail = this.probeBean.getFromMail();
        String toMail = this.probeBean.getToMail();
        String userName = this.probeBean.getUser().getDisplayName();
        String probeName = this.probeBean.getProbeName();
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userName);
        templateMap.put("ProbeName", probeName);
        templateMap.put("ImportingMsg", msg);
        templateMap.put("SiteName", serverName);
        templateMap.put("AppName", appName);
        this.dmService.sendMail(fromMail, toMail, MAIL_SUBJECT, templateMap, MAIL_TEMPLATE_FILE, true);
    }

}
