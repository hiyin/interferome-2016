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
package edu.monash.merc.common.service.file.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.monash.merc.common.service.file.FileSystemSerivce;
import edu.monash.merc.util.DCFileUtils;

/**
 * FileSystemService Implementation
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */

@Scope("prototype")
@Service
public class FileSystemServiceImpl implements FileSystemSerivce {
    @Override
    public boolean checkWritePermission(String pathName) {
        return DCFileUtils.checkWritePermission(pathName);
    }

    @Override
    public void createDirectory(String dirName) {
        DCFileUtils.createDirectory(dirName);
    }

    @Override
    public void deleteDirectory(String dirName) {
        DCFileUtils.deleteDirectory(dirName);
    }

    @Override
    public void changeDirectory(String olderDirName, String newDirName) {
        DCFileUtils.moveDirectory(olderDirName, newDirName);
    }

    @Override
    public void copyFile(String srcFile, String destFile) {
        DCFileUtils.copyFile(srcFile, destFile, true);
    }

    @Override
    public void moveFile(File srcFile, String destFileName, boolean override) {
        DCFileUtils.moveFile(srcFile, destFileName, override);
    }

    @Override
    public void moveFile(String srcFileName, String destFileName, boolean override) {
        DCFileUtils.moveFile(srcFileName, destFileName, override);
    }

    @Override
    public void deleteFile(String fileName) {
        DCFileUtils.deleteFile(fileName);
    }

    @Override
    public void renameFile(String olderFileName, String newFileName) {
        DCFileUtils.moveFile(olderFileName, newFileName, true);
    }

    @Override
    public byte[] readFileToByteArray(String fileName) {
        return DCFileUtils.readFileToByteArray(fileName);
    }

    @Override
    public InputStream downloadFile(String fileName) {
        return DCFileUtils.readFileToInputStream(fileName);
    }

    @Override
    public List<String> discoverFiles(String stagePath, FilenameFilter filter) {
        return DCFileUtils.discoverFileNames(stagePath, filter);
    }
}
