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

package edu.monash.merc.util.zip;

import edu.monash.merc.exception.DCFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: simonyu
 * Date: 26/10/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileZipper {

    private static int BUFFER_SIZE = 10 * 1024;

    public byte[] zipFiles(List<FileZipEntry> fileZipEntryList) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ZipOutputStream zipOuts = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            zipOuts = new ZipOutputStream(byteArrayOutputStream);
            for (FileZipEntry fileZipEntry : fileZipEntryList) {
                InputStream inputStream = fileZipEntry.getInputStream();
                String fileName = fileZipEntry.getFileName();
                ZipEntry ze = new ZipEntry(fileName);
                zipOuts.putNextEntry(ze);

                int len = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOuts.write(buffer, 0, len);
                }
            }
            zipOuts.closeEntry();
            zipOuts.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            throw new DCFileException(ex);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    //ignore whatever
                }
            }
        }
    }

    public byte[] zipFile(InputStream inputStream, String fileName) {
        ZipOutputStream zipOuts = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            zipOuts = new ZipOutputStream(byteArrayOutputStream);

            ZipEntry ze = new ZipEntry(fileName);
            zipOuts.putNextEntry(ze);
            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = inputStream.read(buffer)) > 0) {
                zipOuts.write(buffer, 0, len);
            }
            zipOuts.closeEntry();
            zipOuts.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            throw new DCFileException(ex);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    //ignore whatever
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FileInputStream in1 = new FileInputStream(new File("./testData/experiment-1.export.xml"));
        String f1 = "experiment-1.export.xml";
        FileInputStream in2 = new FileInputStream(new File("./testData/reporter_full.csv"));
        String f2 = "reporter_full.csv";
        String zipFile = "./testData/zipped.zip";
        FileZipEntry fileZipEntry1 = new FileZipEntry(in1, f1);
        FileZipEntry fileZipEntry2 = new FileZipEntry(in2, f2);
        List<FileZipEntry> fileZipEntryList = new ArrayList<FileZipEntry>();
        fileZipEntryList.add(fileZipEntry1);
        fileZipEntryList.add(fileZipEntry2);

        FileZipper fileZipper = new FileZipper();
        byte[] zipped = fileZipper.zipFiles(fileZipEntryList);
       // byte[] zipped = fileZipper.zipFile(in1, f1);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(zipFile));
        fileOutputStream.write(zipped);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
