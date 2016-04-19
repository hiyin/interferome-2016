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

package edu.monash.merc.wsclient.biomart;

import edu.monash.merc.domain.Gene;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 10:23 AM
 */
public class CSVGeneCreator {
    private List<CSVColumn> columns = new ArrayList<CSVColumn>();

    public List<CSVColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<CSVColumn> columns) {
        this.columns = columns;
    }

    public Gene createGene() {
        Gene gene = new Gene();
        for (CSVColumn csvColumn : columns) {
            String columnName = csvColumn.getColumnName();
            String columnValue = csvColumn.getColumnValue();
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.ENSG_ACCESSION)) {
                gene.setEnsgAccession(columnValue);
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.DESCRIPTION)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setDescription(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.CHROMOSOME)) {
                gene.setChromosome(columnValue);
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.START_POSITION)) {
                gene.setStartPosition(Long.valueOf(columnValue).longValue());
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.END_POSITION)) {
                gene.setEndPosition(Long.valueOf(columnValue).longValue());
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.STRAND)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setStrand(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.BAND)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setBand(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.GENE_NAME)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setGeneName(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneField.UNIGENE)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setUnigene(columnValue);
                }
            }

            if (StringUtils.equalsIgnoreCase(columnName, GeneField.GENBANK)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setGenbankId(columnValue);
                }
            }

            if (StringUtils.equalsIgnoreCase(columnName, GeneField.REFSEQ_DNA)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setRefseq_dna(columnValue);
                }
            }

            if (StringUtils.equalsIgnoreCase(columnName, GeneField.ENTREZ)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    gene.setEntrezId(columnValue);
                }
            }
        }
        return gene;
    }
}
