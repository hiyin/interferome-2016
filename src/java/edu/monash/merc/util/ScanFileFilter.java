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

package edu.monash.merc.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * ScanFileFilter class is a file filter class.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ScanFileFilter implements FilenameFilter {

    private String fileExt;

    private String filePrefix;

    public ScanFileFilter() {

    }

    public ScanFileFilter(String filePrefix, String fileExt) {
        this.filePrefix = filePrefix;
        this.fileExt = fileExt;
    }

    @Override
    public boolean accept(File file, String fileName) {
        if ((filePrefix != null) && (fileExt != null)) {
            return (fileName.startsWith(filePrefix) && (fileName.indexOf(fileExt) != -1));
        }

        if ((filePrefix == null) && (fileExt != null)) {
            return (fileName.indexOf(fileExt) != -1);
        }

        if ((filePrefix != null) && (fileExt == null)) {
            return (fileName.startsWith(filePrefix));
        }
        return true;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }
}
