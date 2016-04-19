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

package edu.monash.merc.dto;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * VariationCondtion class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class SearchBean implements Serializable {

    /**
     * selected interferon type
     */
    private String ifnType;

    /**
     * selected interferon subtype
     */
    private String ifnSubType;

    /**
     * select any dose or by range
     */
    private String anyRangeDose = "any";

    /**
     * dose started range
     */
    private double fromDose;

    /**
     * dose ended range
     */
    private double toDose;

    /**
     * select any treatment time or by range
     */
    private String anyRangeTime = "any";

    /**
     * treatment time started range
     */
    private double fromTime;

    /**
     * treatment time ended range
     */
    private double toTime;

    /**
     * selected vivo or vitro condition
     */
    private String vivoVitro;

    /**
     * selected species
     */
    private String species;

    /**
     * selected system
     */
    private String system;

    /**
     * selected organs
     */
    private List<String> organs = new LinkedList<String>();

    /**
     * selected cells
     */
    private List<String> cells = new LinkedList<String>();

    /**
     * selected cellines
     */
    private List<String> cellLines = new LinkedList<String>();

    /**
     * default normal or abnormal variation value
     */
    private String variation = "any";

    /**
     * selected abnormal variation value
     */
    private String abVariation;

    /**
     * select any fold change value or by range
     */
    // private String anyRangeFold = "any";

    /**
     * flag if up value provide
     */
    // private boolean upProvided;

    /**
     * select up to value
     */
    private double upValue = 2.0;

    /**
     * flag if up value provide
     */
    // private boolean downProvided;

    /**
     * select down to value
     */
    private double downValue = 2.0;

    /**
     * provided the gene list, separated by comma
     */
    private String genes;

    /**
     * provided gen bank accession, separated by comma
     */
    private String genBanks;

    /**
     * provided the ensembl id list, separated by comma
     */
    private String ensembls;

    /**
     * dose conditions
     */
    private RangeCondition doseRangeCondition;

    /**
     * treatment conditions
     */
    private RangeCondition timeRangeCondition;

    /**
     * Normal or abnormal variation conditions
     */
    private VariationCondtion variationCondtion;


    private boolean noneDsCondition;


    public String getIfnType() {
        return ifnType;
    }

    public void setIfnType(String ifnType) {
        this.ifnType = ifnType;
    }

    public String getIfnSubType() {
        return ifnSubType;
    }

    public void setIfnSubType(String ifnSubType) {
        this.ifnSubType = ifnSubType;
    }

    public String getAnyRangeDose() {
        return anyRangeDose;
    }

    public void setAnyRangeDose(String anyRangeDose) {
        this.anyRangeDose = anyRangeDose;
    }

    public double getFromDose() {
        return fromDose;
    }

    public void setFromDose(double fromDose) {
        this.fromDose = fromDose;
    }

    public double getToDose() {
        return toDose;
    }

    public void setToDose(double toDose) {
        this.toDose = toDose;
    }

    public String getAnyRangeTime() {
        return anyRangeTime;
    }

    public void setAnyRangeTime(String anyRangeTime) {
        this.anyRangeTime = anyRangeTime;
    }

    public double getFromTime() {
        return fromTime;
    }

    public void setFromTime(double fromTime) {
        this.fromTime = fromTime;
    }

    public double getToTime() {
        return toTime;
    }

    public void setToTime(double toTime) {
        this.toTime = toTime;
    }

    public String getVivoVitro() {
        return vivoVitro;
    }

    public void setVivoVitro(String vivoVitro) {
        this.vivoVitro = vivoVitro;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<String> getOrgans() {
        return organs;
    }

    public void setOrgans(List<String> organs) {
        this.organs = organs;
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCells(List<String> cells) {
        this.cells = cells;
    }

    public List<String> getCellLines() {
        return cellLines;
    }

    public void setCellLines(List<String> cellLines) {
        this.cellLines = cellLines;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getAbVariation() {
        return abVariation;
    }

    public void setAbVariation(String abVariation) {
        this.abVariation = abVariation;
    }

//    public String getAnyRangeFold() {
//        return anyRangeFold;
//    }
//
//    public void setAnyRangeFold(String anyRangeFold) {
//        this.anyRangeFold = anyRangeFold;
//    }

//    public boolean isUpProvided() {
//        return upProvided;
//    }
//
//    public void setUpProvided(boolean upProvided) {
//        this.upProvided = upProvided;
//    }

    public double getUpValue() {
        return upValue;
    }

    public void setUpValue(double upValue) {
        this.upValue = upValue;
    }

//    public boolean isDownProvided() {
//        return downProvided;
//    }
//
//    public void setDownProvided(boolean downProvided) {
//        this.downProvided = downProvided;
//    }

    public double getDownValue() {
        return downValue;
    }

    public void setDownValue(double downValue) {
        this.downValue = downValue;
    }

    public String getGenes() {
        return genes;
    }

    public void setGenes(String genes) {
        this.genes = genes;
    }

    public String getGenBanks() {
        return genBanks;
    }

    public void setGenBanks(String genBanks) {
        this.genBanks = genBanks;
    }

    public String getEnsembls() {
        return ensembls;
    }

    public void setEnsembls(String ensembls) {
        this.ensembls = ensembls;
    }

    public RangeCondition getDoseRangeCondition() {

        doseRangeCondition = new RangeCondition();
        if (StringUtils.equalsIgnoreCase(anyRangeDose, "any")) {
            doseRangeCondition.setRangeProvided(false);
            doseRangeCondition.setFromValue(0);
            doseRangeCondition.setToValue(0);
        } else {
            doseRangeCondition.setRangeProvided(true);
            doseRangeCondition.setFromValue(fromDose);
            doseRangeCondition.setToValue(toDose);
        }

        return doseRangeCondition;
    }

    public void setDoseRangeCondition(RangeCondition doseRangeCondition) {
        this.doseRangeCondition = doseRangeCondition;
    }

    public RangeCondition getTimeRangeCondition() {
        timeRangeCondition = new RangeCondition();
        if (StringUtils.equalsIgnoreCase(anyRangeTime, "any")) {
            timeRangeCondition.setRangeProvided(false);
            timeRangeCondition.setFromValue(0);
            timeRangeCondition.setToValue(0);
        } else {
            timeRangeCondition.setRangeProvided(true);
            timeRangeCondition.setFromValue(fromTime);
            timeRangeCondition.setToValue(toTime);
        }
        return timeRangeCondition;
    }

    public void setTimeRangeCondition(RangeCondition timeRangeCondition) {
        this.timeRangeCondition = timeRangeCondition;
    }

    public VariationCondtion getVariationCondtion() {
        variationCondtion = new VariationCondtion();
        if (StringUtils.equalsIgnoreCase(variation, "any")) {
            variationCondtion.setVarProvided(false);
            variationCondtion.setVarValue(variation);
        }
        if (StringUtils.equalsIgnoreCase(variation, "normal")) {
            variationCondtion.setVarProvided(true);
            variationCondtion.setAbnormal(false);
            variationCondtion.setVarValue(variation);
        }

        if (StringUtils.equalsIgnoreCase(variation, "abnormal")) {
            variationCondtion.setVarProvided(true);
            variationCondtion.setAbnormal(true);
            variationCondtion.setVarValue(abVariation);
        }
        return variationCondtion;
    }

    public void setVariationCondtion(VariationCondtion variationCondtion) {
        this.variationCondtion = variationCondtion;
    }

    public boolean isDefaultSearchCondition() {
        if (!isNoneDsCondition()) {
            return false;
        }

        if (StringUtils.isNotBlank(genes)) {
            return false;
        }
        if (StringUtils.isNotBlank(genBanks)) {
            return false;
        }
        if (StringUtils.isNotBlank(ensembls)) {
            return false;
        }
        return true;
    }

    public boolean isNoneDsCondition() {
        //not select all interferon type
        if (!StringUtils.equals(ifnType, "-1")) {
            return false;
        }
        //not select any treatment concentration
        if (!StringUtils.equalsIgnoreCase(anyRangeDose, "any")) {
            return false;
        }
        //not select any treatment time
        if (!StringUtils.equalsIgnoreCase(anyRangeTime, "any")) {
            return false;
        }
        //not select all vivo vitro
        if (!StringUtils.equals(vivoVitro, "-1")) {
            return false;
        }
        //not select all system
        if (!StringUtils.equals(system, "-1")) {
            return false;
        }
        //not select all species
        if (!StringUtils.equals(species, "-1")) {
            return false;
        }
        //not select all organs
        for (String organ : organs) {
            if (!StringUtils.equals(organ, "-1")) {
                return false;
            }
        }
        //not select all cells
        for (String cell : cells) {
            if (!StringUtils.equals(cell, "-1")) {
                return false;
            }
        }
        //not select all celllines
        for (String cellLine : cellLines) {
            if (!StringUtils.equals(cellLine, "-1")) {
                return false;
            }
        }
        //fold change
        if ((upValue != 2.0) || (downValue != 2.0)) {
            return false;
        }
        //not select any normal or abnormal variation
        VariationCondtion variationCondtion = getVariationCondtion();
        if (variationCondtion.isVarProvided()) {
            return false;
        }
        return true;
    }

    public boolean selectOneOfThreeList() {
        if (StringUtils.isBlank(genes) && StringUtils.isBlank(genBanks) && StringUtils.isBlank(ensembls)) {
            return false;
        }
        return true;
    }

    public void setNoneDsCondition(boolean noneDsCondition) {
        this.noneDsCondition = noneDsCondition;
    }
}