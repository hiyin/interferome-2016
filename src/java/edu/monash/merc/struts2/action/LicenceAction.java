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

import edu.monash.merc.common.service.ccl.CreativeLicenseService;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.Licence;
import edu.monash.merc.dto.CCLicense;
import edu.monash.merc.dto.CCLicenseField;
import edu.monash.merc.dto.CCWSField;
import edu.monash.merc.util.MercUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LicenceAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("data.licenceAction")
public class LicenceAction extends DMBaseAction {

    private Licence licence;

    private CCWSField commercialField;

    private CCWSField derivativesField;

    private CCWSField jurisdictionField;

    private Map<String, String> jurisMap = new HashMap<String, String>();

    private Map<String, String> licenceMap = new HashMap<String, String>();

    private boolean confirmed;

    private boolean understood;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private CreativeLicenseService creativeLicenseService;

    public void setCreativeLicenseService(CreativeLicenseService creativeLicenseService) {
        this.creativeLicenseService = creativeLicenseService;
    }

    @PostConstruct
    public void licenceInit() {
        licenceMap.put(ActionConts.LICENSE_CCCL_TYPE, ActionConts.LICENSE_CCCL_LABEL);
        licenceMap.put(ActionConts.LICENSE_USER_DEFINE_TYPE, ActionConts.LICENSE_USER_DEFINE_LABEL);
    }


    public String showLicenceOpts() {
        try {
            if (licence == null) {
                Licence existedLicence = this.dmService.getLicenceByExpId(experiment.getId());
                if (existedLicence == null) {
                    licence = new Licence();
                    licence.setLicenceType(ActionConts.LICENSE_CCCL_TYPE);
                } else {
                    licence = existedLicence;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.md.reg.licence.show.options.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String selectLicence() {
        try {
            Licence existedLicence = this.dmService.getLicenceByExpId(experiment.getId());
            if (licence.getLicenceType().equals(ActionConts.LICENSE_CCCL_TYPE)) {
                //populate the creative licence type for ws
                populateCCCLicense();
                if (existedLicence != null && (existedLicence.getLicenceType().equals(ActionConts.LICENSE_CCCL_TYPE))) {

                    licence.setCommercial(existedLicence.getCommercial());
                    licence.setDerivatives(existedLicence.getDerivatives());
                    licence.setJurisdiction(existedLicence.getJurisdiction());
                } else {
                    if (StringUtils.isBlank(licence.getCommercial())) {
                        licence.setCommercial("y");
                    }
                    if (StringUtils.isBlank(licence.getDerivatives())) {
                        licence.setDerivatives("y");
                    }
                    if (StringUtils.isBlank(licence.getJurisdiction())) {
                        licence.setJurisdiction("au");
                    }
                }
                return SUCCESS;
            }

            if (licence.getLicenceType().equals(ActionConts.LICENSE_USER_DEFINE_TYPE)) {
                if ((existedLicence != null) && (existedLicence.getLicenceType().equals(ActionConts.LICENSE_USER_DEFINE_TYPE))) {
                    licence = existedLicence;
                }
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("experiment.md.reg.show.selected.licence.type.failed"));
            licence.setLicenceType(ActionConts.LICENSE_CCCL_TYPE);
            return ERROR;
        }
        return SUCCESS;
    }

    public String ccLicence() {
		try {
			if (licence.getLicenceType().equals(ActionConts.LICENSE_CCCL_TYPE)) {
				String wsurl = appSetting.getPropValue(AppPropSettings.CCLICENSE_REST_WS_URL);
				String licenseParams = "commercial=" + licence.getCommercial() + "&derivatives=" + licence.getDerivatives() + "&jurisdiction="
						+ licence.getJurisdiction();
				CCLicense ccl = this.creativeLicenseService.getCCLicense(wsurl, licenseParams);
				String tempRights = ccl.getLicenseHtml() + " " + ccl.getLicenseHrefText() + " (" + ccl.getLicenseURI() + ").";
				licence.setLicenceContents(tempRights);
			}
		} catch (Exception e) {
			logger.error(e);
			addActionError(getText("experiment.md.reg.get.cclicence.failed"));
			return ERROR;
		}
		return SUCCESS;
	}

    public void validateSelectLicence() {
        if (StringUtils.isBlank(licence.getLicenceType())) {
            addFieldError("licenceType", getText("experiment.md.reg.licence.type.must.be.provided"));
            licence.setLicenceType(ActionConts.LICENSE_CCCL_TYPE);
        }
    }

    private void populateCCCLicense() {
        String cclWsUrl = appSetting.getPropValue(AppPropSettings.CCLICENSE_REST_WS_URL);
        List<CCWSField> ccwsField = creativeLicenseService.genCCWSField(cclWsUrl);
        for (CCWSField ccf : ccwsField) {
            String id = ccf.getId();
            if (id.equals(ActionConts.COMMERCIAL_ID)) {
                commercialField = ccf;
            }
            if (id.equals(ActionConts.DERIVATIVEs_ID)) {
                derivativesField = ccf;
            }
            if (id.equals(ActionConts.JURISDICTION_ID)) {
                jurisdictionField = ccf;
                List<CCLicenseField> jlist = jurisdictionField.getCcLicenseFields();
                populateJurisMap(jlist);
            }
        }
    }

    private void populateJurisMap(List<CCLicenseField> jlist) {
        Map<String, String> tmpMap = new HashMap<String, String>();
        for (CCLicenseField lf : jlist) {
            tmpMap.put(lf.getId(), lf.getLabel());
        }
        jurisMap = MercUtil.sortByValue(tmpMap);
    }


    public Licence getLicence() {
        return licence;
    }

    public void setLicence(Licence licence) {
        this.licence = licence;
    }

    public CCWSField getCommercialField() {
        return commercialField;
    }

    public void setCommercialField(CCWSField commercialField) {
        this.commercialField = commercialField;
    }

    public CCWSField getDerivativesField() {
        return derivativesField;
    }

    public void setDerivativesField(CCWSField derivativesField) {
        this.derivativesField = derivativesField;
    }

    public CCWSField getJurisdictionField() {
        return jurisdictionField;
    }

    public void setJurisdictionField(CCWSField jurisdictionField) {
        this.jurisdictionField = jurisdictionField;
    }

    public Map<String, String> getJurisMap() {
        return jurisMap;
    }

    public void setJurisMap(Map<String, String> jurisMap) {
        this.jurisMap = jurisMap;
    }

    public Map<String, String> getLicenceMap() {
        return licenceMap;
    }

    public void setLicenceMap(Map<String, String> licenceMap) {
        this.licenceMap = licenceMap;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isUnderstood() {
        return understood;
    }

    public void setUnderstood(boolean understood) {
        this.understood = understood;
    }
}
