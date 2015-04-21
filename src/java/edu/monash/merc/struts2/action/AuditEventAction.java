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

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.domain.AuditEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * AuditEventAction Action class provides the 'managing the auditing information
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("manage.auditEventAction")
public class AuditEventAction extends DMBaseAction {

    private AuditEvent auditEvent;

    Pagination<AuditEvent> eventPagination;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void initAct() {
        // page size per page values
        pageSizeMap.put(5, 5);
        pageSizeMap.put(10, 10);
        pageSizeMap.put(15, 15);
        pageSizeMap.put(20, 20);
        pageSizeMap.put(25, 25);
        pageSizeMap.put(30, 30);
        pageSizeMap.put(40, 40);
        pageSizeMap.put(50, 50);

        // order by values
        orderByMap.put("createdTime", "created date");
        // sort type values
        orderByTypeMap.put("DESC", "desc");
        orderByTypeMap.put("ASC", "asc");

        // populate the pagination link
        populatePaginationLink(ActionConts.LIST_USER_EVENTS_ACTION, ActionConts.PAGINATION_SUFFUX);
        // populate the page title and nav bar
        setNavBarAndTitle();
    }

    public String deleteEvent() {
        user = getCurrentUser();
        try {
            this.dmService.deleteEventByIdWithUserId(auditEvent.getId(), user.getId());
            listUserAllEvents();
            // set success action message
            setSuccessActMsg(getText("audit.event.deleted.success"));
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("audit.event.delete.event.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String getUserEvents() {
        try {
            user = getCurrentUser();
            listUserAllEvents();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("audit.event.list.events.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    private void setDefaultPaginationParams() {
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "createdTime";
        }
        if (StringUtils.isBlank(orderByType)) {
            orderByType = ActionConts.DESC_SORT_TYPE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = ActionConts.DEFAULT_PAGE_SIZE;
        }
    }

    private void listUserAllEvents() {
        // set the default orderBy and sortType if not setup
        setDefaultPaginationParams();
        eventPagination = this.dmService.getEventByUserId(user.getId(), pageNo, pageSize, populateOrderBy());
    }

    private void setNavBarAndTitle() {
        setPageTitle(getText("user.audit.event.action.title"));
        String secondNav = getText("user.audit.event.action.title");
        String secondNavLink = "manage/getUserEvents.jspx";
        navBar = createNavBar("User", null, secondNav, secondNavLink, null, null);
    }

    public AuditEvent getAuditEvent() {
        return auditEvent;
    }

    public void setAuditEvent(AuditEvent auditEvent) {
        this.auditEvent = auditEvent;
    }

    public Pagination<AuditEvent> getEventPagination() {
        return eventPagination;
    }

    public void setEventPagination(Pagination<AuditEvent> eventPagination) {
        this.eventPagination = eventPagination;
    }

}
