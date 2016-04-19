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

package edu.monash.merc.struts2.multipart;

import com.opensymphony.xwork2.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.multipart.MultiPartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This is a multipart request handler, but utilises commons-filupload 1.2 and adds a listener to the file upload
 * request, so that the progress can be monitor by other requests.
 * <p/>
 * This is a copy of JakartaMultiPartRequest with some extra code put in to handle the ProgressMonitor listener class.
 * Strangely enough, extending JakartaMultiPartRequest and overiding the methods didnt seem to work? However, it wasnt
 * important enough to look into why, so i just copied the code and pasted here.
 */
public class MonitoredMultiPartRequest implements MultiPartRequest {

    static final Logger log = Logger.getLogger(MultiPartRequest.class);

    // maps parameter name -> List of FileItem objects
    private Map<String, List<FileItem>> files = new HashMap<String, List<FileItem>>();
    // maps parameter name -> List of param values
    private Map<String, List<String>> params = new HashMap<String, List<String>>();
    // any errors while processing this request
    private List<String> errors = new ArrayList<String>();

    private long maxSize;

    @Inject(StrutsConstants.STRUTS_MULTIPART_MAXSIZE)
    public void setMaxSize(String maxSize) {
        this.maxSize = Long.parseLong(maxSize);
    }

    /**
     * Creates a new request wrapper to handle multi-part data using methods adapted from Jason Pell's multipart classes
     * (see class description).
     *
     * @param saveDir        the directory to save off the file
     * @param servletRequest the request containing the multipart
     * @throws java.io.IOException is thrown if encoding fails.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void parse(HttpServletRequest servletRequest, String saveDir) throws IOException {
        DiskFileItemFactory fac = new DiskFileItemFactory();
        // Make sure that the data is written to file
        fac.setSizeThreshold(0);
        if (saveDir != null) {
            fac.setRepository(new File(saveDir));
        }

        ProgressMonitor monitor = null;
        // Parse the request
        try {
            ServletFileUpload upload = new ServletFileUpload(fac);
            upload.setSizeMax(maxSize);

            monitor = new ProgressMonitor();
            upload.setProgressListener(monitor);
            servletRequest.getSession().setAttribute(ProgressMonitor.SESSION_PROGRESS_MONITOR, monitor);

            List<FileItem> items = upload.parseRequest(createRequestContext(servletRequest));

            for (FileItem item1 : items) {
                FileItem item = item1;

                if (log.isDebugEnabled()) {
                    log.debug("Found item " + item.getFieldName());
                }
                if (item.isFormField()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Item is a normal form field");
                    }
                    List<String> values;
                    if (params.get(item.getFieldName()) != null) {
                        values = params.get(item.getFieldName());
                    } else {
                        values = new ArrayList<String>();
                    }

                    // note: see http://jira.opensymphony.com/browse/WW-633
                    // basically, in some cases the charset may be null, so
                    // we're just going to try to "other" method (no idea if this
                    // will work)
                    String charset = servletRequest.getCharacterEncoding();
                    if (charset != null) {
                        values.add(item.getString(charset));
                    } else {
                        values.add(item.getString());
                    }
                    params.put(item.getFieldName(), values);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Item is a file upload");
                    }
                    List<FileItem> values;
                    if (files.get(item.getFieldName()) != null) {
                        values = files.get(item.getFieldName());
                    } else {
                        values = new ArrayList<FileItem>();
                    }

                    values.add(item);
                    files.put(item.getFieldName(), values);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            if (monitor != null)
                monitor.abort();
            log.error(e);
            errors.add(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            if (monitor != null)
                monitor.abort();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFileParameterNames()
     */
    @Override
    public Enumeration<String> getFileParameterNames() {
        return Collections.enumeration(files.keySet());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getContentType(java.lang.String)
     */
    @Override
    public String[] getContentType(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> contentTypes = new ArrayList<String>(items.size());
        for (int i = 0; i < items.size(); i++) {
            FileItem fileItem = items.get(i);
            contentTypes.add(fileItem.getContentType());
        }

        return (String[]) contentTypes.toArray(new String[contentTypes.size()]);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFile(java.lang.String)
     */
    @Override
    public File[] getFile(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<File> fileList = new ArrayList<File>(items.size());
        for (int i = 0; i < items.size(); i++) {
            DiskFileItem fileItem = (DiskFileItem) items.get(i);
            fileList.add(fileItem.getStoreLocation());
        }

        return (File[]) fileList.toArray(new File[fileList.size()]);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFileNames(java.lang.String)
     */
    @Override
    public String[] getFileNames(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (int i = 0; i < items.size(); i++) {
            DiskFileItem fileItem = (DiskFileItem) items.get(i);
            fileNames.add(getCanonicalName(fileItem.getName()));
        }

        return (String[]) fileNames.toArray(new String[fileNames.size()]);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFilesystemName(java.lang.String)
     */
    @Override
    public String[] getFilesystemName(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (int i = 0; i < items.size(); i++) {
            DiskFileItem fileItem = (DiskFileItem) items.get(i);
            fileNames.add(fileItem.getStoreLocation().getName());
        }

        return (String[]) fileNames.toArray(new String[fileNames.size()]);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return v.get(0);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameterNames()
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return (String[]) v.toArray(new String[v.size()]);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getErrors()
     */
    @Override
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Returns the canonical name of the given file.
     *
     * @param filename the given file
     * @return the canonical name of the given file
     */
    private String getCanonicalName(String filename) {
        int forwardSlash = filename.lastIndexOf("/");
        int backwardSlash = filename.lastIndexOf("\\");
        if (forwardSlash != -1 && forwardSlash > backwardSlash) {
            filename = filename.substring(forwardSlash + 1, filename.length());
        } else if (backwardSlash != -1 && backwardSlash >= forwardSlash) {
            filename = filename.substring(backwardSlash + 1, filename.length());
        }

        return filename;
    }

    /**
     * Creates a RequestContext needed by Jakarta Commons Upload.
     *
     * @param req the request.
     * @return a new request context.
     */
    private RequestContext createRequestContext(final HttpServletRequest req) {
        return new RequestContext() {
            @Override
            public String getCharacterEncoding() {
                return req.getCharacterEncoding();
            }

            @Override
            public String getContentType() {
                return req.getContentType();
            }

            @Override
            public int getContentLength() {
                return req.getContentLength();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return req.getInputStream();
            }
        };
    }
   @Override
    public void cleanUp() {
        //do nothing
    }

}
