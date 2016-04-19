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

package edu.monash.merc.struts2.action;

import edu.monash.merc.common.results.DBStats;
import edu.monash.merc.service.DBStatisticsService;
import edu.monash.merc.service.impl.DBStatisticsServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author: Simon Yu
 * @email: Xiaoming.Yu@monash.edu
 *
 * Date: 20/02/12
 * Time: 10:33 AM
 * @version: 1.0
 */

@Scope("prototype")
@Controller("site.dbStatAction")
public class DBStatisticAction extends DMBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private DBStatisticsService dbStatisticsService;
    private DBStats stats;

    public void setDbStatisticsService(DBStatisticsService dbStatisticsService) {
        this.dbStatisticsService = dbStatisticsService;
    }

    public DBStats getStats() {
        return stats;
    }

    public String dbStat() {
        try {
            user = getCurrentUser();
            stats = dbStatisticsService.getDBStatistics();
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.error(ex);
            addActionError(getText("interferome.site.show.db.statistic.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

}
