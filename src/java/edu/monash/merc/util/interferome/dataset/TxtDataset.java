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

package edu.monash.merc.util.interferome.dataset;

import edu.monash.merc.exception.DCParseException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TxtDataset class.
 *
 * @author emilda; Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class TxtDataset implements BaseDataset {
    private static final int RD_OFFSET = 15;

    private ExpFactor[] expFactors;

    private String[] data;

    //private String[] reporters;

    private String[] probes;

    private String name;

    private double treatmentTime;

    private double treatmentCon;

    private boolean inVivo;

    private String description;

    private String comment;

    private String sampleChars;

    private VarFactor varFactor;

    private IFNTypeFactor ifnTypeFactor;

    private boolean vivProvided;

    private boolean ttimeProvided;

    private boolean tconProvided;

    private boolean normalProvided;

    private boolean abnormalProvided;

    private boolean ifnTypeProvided;

    private boolean ifnSubTypeProvided;

    public TxtDataset() {

    }

    public TxtDataset(String fileName, List<String> lines) {
        this.name = fileName;
        setExperimentalFactors(lines);
        //setReportersData(lines);
        setProbesData(lines);
    }

    private void setExperimentalFactors(List<String> lines) {
        if (lines.size() < 7) {
            throw new DCParseException("the base dataset file corrupted, the experimental factors not found");
        }
        String[] lines_array = (String[]) lines.toArray(new String[lines.size()]);
        String[] expf_names = lines_array[2].split("\\t");
        String[] expf_values = lines_array[6].split("\\t");

        if (expf_names.length != expf_values.length + 1) {
            throw new DCParseException("the experimental factor name value pair mismatch");
        }

        List<ExpFactor> factorList = new ArrayList<ExpFactor>();
        IFNTypeFactor ifnType = new IFNTypeFactor();
        try {
            for (int i = 0; i < expf_values.length; i++) {

                String name = expf_names[i + 1];
                String value = expf_values[i];

                //set the description if any
                if (StringUtils.equalsIgnoreCase(name, "description")) {
                    setDescription(value);
                }

                //set the comment if any
                if (StringUtils.equalsIgnoreCase(name, "comment")) {
                    setComment(value);
                }

                //set Sample Characteristics if any
                if (StringUtils.equalsIgnoreCase(name, "sample characteristics")) {
                    setSampleChars(value);
                }

                //set the treatment time if any
                if (StringUtils.equalsIgnoreCase(name, "treatment time")) {
                    setTreatmentTime(Double.valueOf(value));
                    ttimeProvided = true;
                }

                //set the treatment concentration (dose) if any
                if (StringUtils.equalsIgnoreCase(name, "treatment concentration")) {
                    setTreatmentCon(Double.valueOf(value));
                    tconProvided = true;
                }

                //set the genetic variation - normal if any
                if (StringUtils.equalsIgnoreCase(name, "normal")) {
                    VarFactor vf = new VarFactor();
                    if (StringUtils.isNotBlank(value)) {
                        vf.setAbnormal(false);
                        vf.setVarValue(value);
                        setVarFactor(vf);
                        normalProvided = true;
                    }
                }

                //set the genetic variation - abnormal if any
                if (StringUtils.equalsIgnoreCase(name, "abnormal")) {
                    VarFactor vf = new VarFactor();

                    if (StringUtils.isNotBlank(value)) {
                        vf.setAbnormal(true);
                        vf.setVarValue(value);
                        setVarFactor(vf);
                        abnormalProvided = true;
                    }
                }

                //set the in vivo or in vitro if any
                if (StringUtils.equalsIgnoreCase(name, "invivo invitro")) {
                    if (StringUtils.equalsIgnoreCase(value, "in vivo")) {
                        setInVivo(true);
                    } else {
                        setInVivo(false);
                    }
                    vivProvided = true;
                }

                //set the Interferon Type if any
                if (StringUtils.equalsIgnoreCase(name, "interferon type")) {
                    ifnType.setTypeName(value);
                    ifnTypeProvided = true;
                }

                //set the Interferon Type if any
                if (StringUtils.equalsIgnoreCase(name, "interferon subtype")) {
                    ifnType.setSubTypeName(value);
                    ifnSubTypeProvided = true;
                }

                if (!StringUtils.equalsIgnoreCase(name, "id") && !StringUtils.equalsIgnoreCase(name, "name")
                        && !StringUtils.equalsIgnoreCase(name, "description") && !StringUtils.equalsIgnoreCase(name, "comment")
                        && !StringUtils.equalsIgnoreCase(name, "sample characteristics") && !StringUtils.equalsIgnoreCase(name, "treatment time")
                        && !StringUtils.equalsIgnoreCase(name, "treatment concentration") && !StringUtils.equalsIgnoreCase(name, "normal")
                        && !StringUtils.equalsIgnoreCase(name, "abnormal") && !StringUtils.equalsIgnoreCase(name, "invivo invitro")
                        && !StringUtils.equalsIgnoreCase(name, "interferon type") && !StringUtils.equalsIgnoreCase(name, "interferon subtype")
                        ) {
                    ExpFactor factor = new ExpFactor();
                    if (StringUtils.isNotBlank(value)) {
                        factor.setName(name);
                        factor.setValue(value);
                        factorList.add(factor);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DCParseException("invalid experimental factor value");
        }
        //set interferon type and subtype
        setIfnTypeFactor(ifnType);
        //experiment factors
        this.expFactors = factorList.toArray(new ExpFactor[factorList.size()]);

        if (normalProvided && abnormalProvided) {
            throw new DCParseException("both normal and abnormal variations are provided together");
        }
        if (!validate()) {
            throw new DCParseException("some factors not provided");
        }
    }

    private boolean validate() {
        if (!vivProvided || !ttimeProvided || !tconProvided || !ifnTypeProvided || !ifnSubTypeProvided) {
            return false;
        } else {
            return true;
        }

    }

//    private void setReportersData(List<String> lines) {
//        String[] lines_array = (String[]) lines.toArray(new String[lines.size()]);
//        String[] counts = lines_array[13].split("\\t");
//        int no_reporters = Integer.parseInt(counts[1]);

//        if (lines.size() != no_reporters + RD_OFFSET + 1) {
//            throw new DCParseException("the base dataset file corrupted, not enough reporter/ data information");
//        }
//
//        this.data = new String[no_reporters];
//        this.reporters = new String[no_reporters];
//
//        for (int i = 0; i < no_reporters; i++) {
//            try {
//                String[] tokens = lines_array[i + RD_OFFSET].split("\\t");
//                this.reporters[i] = tokens[0];
//                this.data[i] = tokens[1];
//            } catch (Exception e) {
//                throw new DCParseException("dataset file corrupted, null reporter/ data found at line " + i +
//                        ". " + no_reporters + " records expected");
//            }
//        }
//    }

    private void setProbesData(List<String> lines) {
        String[] lines_array = (String[]) lines.toArray(new String[lines.size()]);
        String[] counts = lines_array[13].split("\\t");
        int no_probes = Integer.parseInt(counts[1]);

        if (lines.size() != no_probes + RD_OFFSET + 1) {
            throw new DCParseException("the base dataset file corrupted, not enough probes/ data information");
        }

        this.data = new String[no_probes];
        this.probes = new String[no_probes];

        for (int i = 0; i < no_probes; i++) {
            try {
                String[] tokens = lines_array[i + RD_OFFSET].split("\\t");
                this.probes[i] = tokens[0];
                this.data[i] = tokens[1];
            } catch (Exception e) {
                throw new DCParseException("dataset file corrupted, null probes/ data found at line " + i +
                        ". " + no_probes + " probes expected");
            }
        }
    }


    public void setExpFactors(ExpFactor[] experimentalFactors) {
        this.expFactors = experimentalFactors;
    }

    @Override
    public ExpFactor[] getExpFactors() {
        return this.expFactors;
    }

    @Override
    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

//    @Override
//    public String[] getReporters() {
//        return reporters;
//    }
//
//    public void setReporters(String[] reporters) {
//        this.reporters = reporters;
//    }

    @Override
    public String[] getProbes() {
        return probes;
    }

    public void setProbes(String[] probes) {
        this.probes = probes;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(double treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    @Override
    public double getTreatmentCon() {
        return treatmentCon;
    }

    public void setTreatmentCon(double treatmentCon) {
        this.treatmentCon = treatmentCon;
    }

    @Override
    public boolean isInVivo() {
        return inVivo;
    }

    public void setInVivo(boolean inVivo) {
        this.inVivo = inVivo;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getSampleChars() {
        return sampleChars;
    }

    public void setSampleChars(String sampleChars) {
        this.sampleChars = sampleChars;
    }

    @Override
    public VarFactor getVarFactor() {
        return varFactor;
    }

    public void setVarFactor(VarFactor varFactor) {
        this.varFactor = varFactor;
    }

    @Override
    public IFNTypeFactor getIfnTypeFactor() {
        return ifnTypeFactor;
    }

    public void setIfnTypeFactor(IFNTypeFactor ifnTypeFactor) {
        this.ifnTypeFactor = ifnTypeFactor;
    }
}
