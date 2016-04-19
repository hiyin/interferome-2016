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

/**
 * ActionConts class defines all constants which are using by the action classes
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public interface ActionConts {

    static String SESS_SECURITY_CODE = "security_code";

    static String SESS_AUTHENTICATION_FLAG = "authentication_flag";

    static String SESS_AUTHEN_USER_ID = "authen_user_id";

    static String SESS_AUTHEN_USER_NAME = "authen_user_name";

    static String SESS_AUTHENCATED = "authenticated";

    static String REQUEST_URL = "request_url_path";

    static String HTTP_SCHEME = "http";

    static String HTTPS_SCHEME = "https";

    static String HTTP_SCHEME_DELIM = "://";

    static String URL_PATH_DEIM = "/";

    static String COLON_DEIM = ":";

    static String LAST_TIME_OF_DAY = " 23:59:59";

    static String RESET_PWD_ACTION_NAME = "ResetPasswd";

    static String ACTIVATION_ACTION_NAME = "activateAccount";

    static String DISPLAY_USER_HOME_ACTION = "manage/userHome.jspx";

    static int DEFAULT_PAGE_SIZE = 10;

    static String DEFAULT_ORDERBY = "name";

    static String DEFAULT_ORDERBY_TYPE = "ASC";

    static String DESC_SORT_TYPE = "DESC";

    static String ASC_SORT_TYPE = "ASC";

    static String PAGINATION_SUFFUX = "?pageNo=";

    static String SEARCH_CON_KEY = "interferon_search";

    static String LIST_USER_EVENTS_ACTION = "manage/getUserEvents.jspx";

    static String LIST_ALL_USERS_ACTION = "admin/listAllUsers.jspx";

    static String LIST_MY_EXPERIMENTS_ACTION = "data/listMyExperiments.jspx";

    static String LIST_EXPERIMENTS_ACTION = "data/listExperiments.jspx";

    static String LIST_PUB_EXPERIMENTS_ACTION = "pubdata/listExperiments.jspx";

    static String VIEW_MY_EXP_DETAILS_ACTION = "data/viewMyExperiment.jspx";

    static String VIEW_EXP_DETAILS_ACTION = "data/viewExperiment.jspx";

    static String DELETE_MY_EXP_ACTION = "data/deleteMyExp.jspx";

    static String DELETE_EXP_ACTION = "data/deleteExp.jspx";

    static String VIEW_DATASET_ACTION = "data/viewDataset.jspx";

    static String VIEW_PUB_DATASET_ACTION = "pubdata/viewDataset.jspx";

    static String ANDS_MD_REG_PARTY_RM_TYPE = "rm_party";

    static String ANDS_MD_REG_PARTY_RM_TYPE_LABEL = "Search a researcher from the Research Master Web Service";

    static String ANDS_MD_REG_PARTY_USER_DEFINED_TYPE = "user_defined_party";

    static String ANDS_MD_REG_PARTY_USER_DEFINED_TYPE_LABEL = "Manually Input a researcher information";

    static String LICENSE_CCCL_TYPE = "cccl_license";

    static String LICENSE_USER_DEFINE_TYPE = "user_license";

    static String LICENSE_CCCL_LABEL = "Creative Commons Copyright License (Recommended)";

    static String LICENSE_USER_DEFINE_LABEL = "Define Your Own License";

    static String COMMERCIAL_ID = "commercial";

    static String DERIVATIVEs_ID = "derivatives";

    static String JURISDICTION_ID = "jurisdiction";

    static String RELATEDINFO_DEFAULT_TYPE = "publication";

    static String PUBMED_ID_DELIM = "PMID:";

    static String RELATEDINFO_IDENT_DEFAULT_TYPE = "uri";

    public static enum UserManageType {
        ACTIVATE("activate"), DEACTIVATE("deactivate"), SETASADMIN("setasadmin"), SETASUSER("setasuser");

        private String type;

        UserManageType(String manageType) {
            this.type = manageType;
        }

        public String manageType() {
            return this.type;
        }

        public String toString() {
            switch (this) {
                case ACTIVATE:
                    return "activate";
                case DEACTIVATE:
                    return "deactivate";
                case SETASADMIN:
                    return "setasadmin";
                case SETASUSER:
                    return "setasuser";
                default:
                    return "activate";

            }
        }
    }
}
