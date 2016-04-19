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

package edu.monash.merc.util.csv;

import edu.monash.merc.domain.Reporter;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVRepGenerator class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class CSVRepGenerator {

    private List<ReporterColumn> columns = new ArrayList<ReporterColumn>();

    public List<ReporterColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ReporterColumn> columns) {
        this.columns = columns;
    }

    public Reporter genReport() {
        Reporter reporter = new Reporter();
        for (ReporterColumn rpcolumn : columns) {
            String columnName = rpcolumn.getColumnName();
            String columnValue = rpcolumn.getColumnValue();
            if (columnName.equalsIgnoreCase(RField.PROBEID)) {
                reporter.setProbeId(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.TRANSCRIPT_ID_ARRAY_DESIGN)) {
                reporter.setTranscriptIdArrayDesign(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.TARGET_DESCRIPTION)) {
                reporter.setTargetDescription(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.GEN_BANK_ACCESSION)) {
                reporter.setGenBankAccession(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.UNIGENE_ID)) {
                reporter.setUniGeneId(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.ALIGNMENTS)) {
                reporter.setAlignments(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.GENE_TITLE)) {
                reporter.setGeneTitle(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.GENE_SYMBOL)) {
                reporter.setGeneSymbol(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.CHROMOSOMAL_LOCATION)) {
                reporter.setChromosomalLocation(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.ENSEMBL)) {
                reporter.setEnsembl(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.ENTREZ_GENE)) {
                reporter.setEntrezGene(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.REFSEQ_PROTEIN_ID)) {
                reporter.setRefSeqProteinId(columnValue);
            }
            if (columnName.equalsIgnoreCase(RField.REFSEQ_TRANSCRIPT_ID)) {
                reporter.setRefSeqTranscriptId(columnValue);
            }
        }
        return reporter;
    }
}
