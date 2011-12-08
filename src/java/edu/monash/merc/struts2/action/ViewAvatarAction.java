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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * ViewAvatarAction Action class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Controller("user.viewAvatarAction")
public class ViewAvatarAction extends DMBaseAction {

	private long avatarUserId;

	protected InputStream imageStream;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public String viewAvatar() {
		try {
			String defaultAvator = getAppRoot() + "avatar" + File.separator + "male.png";
			if (avatarUserId != 0) {
				String avatarFile = null;
				avatarFile = this.dmService.getAvatarByUserId(avatarUserId).getFileName();
				String srcImage = getAppRoot() + avatarFile;
				File file = new File(srcImage);
				if (!file.exists()) {
					file = new File(defaultAvator);
				}
				this.imageStream = new FileInputStream(file);
			} else {
				this.imageStream = new FileInputStream(new File(defaultAvator));
			}
		} catch (Exception e) {
			logger.error(e);
			return ERROR;
		}
		return SUCCESS;
	}

	public long getAvatarUserId() {
		return avatarUserId;
	}

	public void setAvatarUserId(long avatarUserId) {
		this.avatarUserId = avatarUserId;
	}

	public InputStream getImageStream() {
		return imageStream;
	}

	public void setImageStream(InputStream imageStream) {
		this.imageStream = imageStream;
	}
}
