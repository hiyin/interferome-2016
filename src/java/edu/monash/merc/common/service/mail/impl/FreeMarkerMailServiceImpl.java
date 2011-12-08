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
package edu.monash.merc.common.service.mail.impl;

import java.util.Map;

import edu.monash.merc.common.service.mail.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import edu.monash.merc.exception.MailException;
import edu.monash.merc.util.mail.MailSenderThread;
import freemarker.template.Template;

/**
 * MailService Implementation
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Qualifier("freemarkerMailService")
public class FreeMarkerMailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml) {
        try {
            MailSenderThread sendThread = new MailSenderThread(sender, emailFrom, emailTo, emailSubject, emailBody, isHtml);
            sendThread.startSendMail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MailException(e);
        }
    }

    @Override
    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile,
                         boolean isHtml) {
        try {
            String emailBody = createMailBody(templateValues, templateFile);
            MailSenderThread sendThread = new MailSenderThread(sender, emailFrom, emailTo, emailSubject, emailBody, isHtml);
            sendThread.startSendMail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MailException(e);
        }

    }

    /**
     * Create a mail body base on the mail template file and template values.
     *
     * @param templateValueMap
     * @param templateFile
     * @return
     * @throws Exception
     */
    private String createMailBody(Map<String, String> templateValueMap, String templateFile) throws Exception {
        String htmlText = "";
        Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(templateFile);
        htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, templateValueMap);
        return htmlText;
    }
}
