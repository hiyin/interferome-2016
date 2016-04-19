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

package edu.monash.merc.struts2.dispatcher;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * RawTextResult class extends StrutsResultSupport which provides the result type of the response
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class RawTextResult extends StrutsResultSupport {

    private static Logger logger = Logger.getLogger(RawTextResult.class.getName());

    private String stringName = "stringResult";

    /**
     * Creates a new instance of XMLStringTransformResult
     */
    public RawTextResult() {
    }

    /**
     * This is executed when the result is called
     */
    @Override
    public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        String stringResult = null;
        PrintWriter out = null;

        try {
            // Find the JDom document from the invocation variable stack
            // This is a org.jdom.Document member variable in your action that
            // uses this class as a result
            stringResult = (String) invocation.getStack().findValue(conditionalParse(stringName, invocation));

            if (stringResult == null) {
                String msg = ("Cannot find a String with the name [" + stringName + "] in the invocation stack. " + "You must have a getStringResult() method in the stack that returns the String.");
                logger.error(msg);
                throw new IllegalArgumentException(msg);
            }

            // Find the Response in context
            HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(HTTP_RESPONSE);

            response.setContentType("text/plain");
            out = response.getWriter();

            out.print(stringResult);

            out.flush();
        } catch (Exception e) {
            logger.error("Problem with outputting the raw string result " + e.getMessage());
            throw e;
        } finally {
            if (out != null)
                out.close();
        }
    }
}
