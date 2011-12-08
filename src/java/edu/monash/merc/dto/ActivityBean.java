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

import java.io.Serializable;

/**
 * ActivityBean class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ActivityBean implements Serializable {
    private String groupName;

    private String activityKey;

    private String originateSourceType;

    private String originateSourceValue;

    private String identifierType;

    private String identifierValue;

    private String namePartValue;

    private String descType;

    private String descValue;

    private String rifcsContent;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getOriginateSourceType() {
        return originateSourceType;
    }

    public void setOriginateSourceType(String originateSourceType) {
        this.originateSourceType = originateSourceType;
    }

    public String getOriginateSourceValue() {
        return originateSourceValue;
    }

    public void setOriginateSourceValue(String originateSourceValue) {
        this.originateSourceValue = originateSourceValue;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

    public String getNamePartValue() {
        return namePartValue;
    }

    public void setNamePartValue(String namePartValue) {
        this.namePartValue = namePartValue;
    }

    public String getDescType() {
        return descType;
    }

    public void setDescType(String descType) {
        this.descType = descType;
    }

    public String getDescValue() {
        return descValue;
    }

    public void setDescValue(String descValue) {
        this.descValue = descValue;
    }

    public String getRifcsContent() {
        return rifcsContent;
    }

    public void setRifcsContent(String rifcsContent) {
        this.rifcsContent = rifcsContent;
    }
}
