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

import com.opensymphony.xwork2.ActionSupport;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.common.sql.OrderBy.OrderType;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.User;
import edu.monash.merc.dto.ActionNavBar;
import edu.monash.merc.service.UserService;
import edu.monash.merc.util.encrypt.MD5;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BaseAction Action class is a base Action class, the other action classed must extend this action
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

    protected User user;

    @Autowired
    protected UserService userService;

    @Autowired
    protected AppPropSettings appSetting;

    protected Map<String, Object> session;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public static final String JSON = "json";

    public static final String REDIRECT = "redirect";

    protected static String REDIRECTEXP = "redirectexp";

    protected static String REDIRECTALLEXP = "redirectallexp";

    protected static String REDIRECTDS = "redirectds";

    protected static String REDIRECTALLDS = "redirectallds";

    protected static String NOTFOUND = "notfound";

    public static final String CHAIN = "chain";

    protected String pageTitle;

    protected ActionNavBar navBar;

    protected int pageNo;

    protected int pageSize;

    protected String orderBy;

    protected String orderByType;

    protected String pageLink;

    protected String pageSuffix;

    protected Map<String, String> orderByMap = new HashMap<String, String>();

    protected Map<String, String> orderByTypeMap = new HashMap<String, String>();

    protected Map<Integer, Integer> pageSizeMap = new LinkedHashMap<Integer, Integer>();

    private String successActMsg;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAppSetting(AppPropSettings appSetting) {
        this.appSetting = appSetting;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getServletResponse() {
        return response;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getServletRequest() {
        return request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void storeInSession(String key, Object obj) {
        this.session.put(key, obj);
    }

    public void removeFromSession(String sessionKey) {
        this.session.remove(sessionKey);
    }

    public Object findFromSession(String key) {
        return this.session.get(key);
    }

    public String getAppRealPath(String path) {
        return ServletActionContext.getServletContext().getRealPath(path);
    }

    public String getAppRoot() {
        return getAppRealPath("/");
    }

    public String getAppContextPath() {
        return ServletActionContext.getRequest().getContextPath();
    }

    public int getServerPort() {
        return ServletActionContext.getRequest().getServerPort();
    }

    public String getAppHostName() {
        return ServletActionContext.getRequest().getServerName();
    }

    public String getServerQName() {
        String scheme = request.getScheme();
        String hostName = request.getServerName();
        int port = request.getServerPort();

        StringBuffer buf = new StringBuffer();
        if (scheme.equals(ActionConts.HTTP_SCHEME)) {
            buf.append(ActionConts.HTTP_SCHEME).append(ActionConts.HTTP_SCHEME_DELIM);
        } else {
            buf.append(ActionConts.HTTPS_SCHEME).append(ActionConts.HTTP_SCHEME_DELIM);
        }
        buf.append(hostName);
        if (port == 80 || port == 443) {
            return new String(buf);
        }
        buf.append(ActionConts.COLON_DEIM).append(port);
        return new String(buf);
    }

    protected String generateSecurityHash(String value) {
        String systemHash = MD5.hash(appSetting.getPropValue(AppPropSettings.SECURITY_HASH_SEQUENCE) + value);
        return MD5.hash(System.currentTimeMillis() + systemHash);
    }

    protected boolean isSecurityCodeError(String securityCode) {
        String code = (String) findFromSession(ActionConts.SESS_SECURITY_CODE);
        if (StringUtils.isBlank(code)) {
            return true;
        } else if (StringUtils.equalsIgnoreCase(securityCode, code)) {
            return false;
        } else {
            return true;
        }
    }

    protected long getCurrentUserId() {
        Object login_uid = findFromSession(ActionConts.SESS_AUTHEN_USER_ID);
        if (login_uid == null) {
            return 0;
        } else {
            return ((Long) login_uid).longValue();
        }
    }

    protected User getCurrentUser() {
        long userId = getCurrentUserId();
        return this.userService.getUserById(userId);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public ActionNavBar getNavBar() {
        return navBar;
    }

    public void setNavBar(ActionNavBar navBar) {
        this.navBar = navBar;
    }

    protected ActionNavBar createNavBar(String firstNav, String firstNavLink, String secondNav, String secondNavLink, String thirdNav,
                                        String thirdNavLink) {
        return new ActionNavBar(firstNav, firstNavLink, secondNav, secondNavLink, thirdNav, thirdNavLink);
    }

    protected String newLineToBr(String textArea) {
        return StringUtils.replace(textArea, "\n", "<br/>");
    }

    protected OrderBy[] populateOrderBy() {
        if (orderByType.equals(OrderType.ASC.toString())) {
            return new OrderBy[]{OrderBy.asc(orderBy)};
        } else {
            return new OrderBy[]{OrderBy.desc(orderBy)};
        }
    }

    protected void populatePaginationLink(String pageActionName, String pageSuffix) {
        this.pageLink = pageActionName;
        this.pageSuffix = pageSuffix;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getPageSuffix() {
        return pageSuffix;
    }

    public void setPageSuffix(String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }

    public String getSuccessActMsg() {
        return successActMsg;
    }

    public void setSuccessActMsg(String successActMsg) {
        this.successActMsg = successActMsg;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderByType() {
        return orderByType;
    }

    public void setOrderByType(String orderByType) {
        this.orderByType = orderByType;
    }

    public Map<String, String> getOrderByMap() {
        return orderByMap;
    }

    public void setOrderByMap(Map<String, String> orderByMap) {
        this.orderByMap = orderByMap;
    }

    public Map<String, String> getOrderByTypeMap() {
        return orderByTypeMap;
    }

    public void setOrderByTypeMap(Map<String, String> orderByTypeMap) {
        this.orderByTypeMap = orderByTypeMap;
    }

    public Map<Integer, Integer> getPageSizeMap() {
        return pageSizeMap;
    }

    public void setPageSizeMap(Map<Integer, Integer> pageSizeMap) {
        this.pageSizeMap = pageSizeMap;
    }
}
