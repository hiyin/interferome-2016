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
package edu.monash.merc.domain;

/**
 * Enum UserType class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public enum UserType {
	SUPERADMIN(1), ADMIN(2), REGUSER(3), ALLREGUSER(4), ANONYMOUS(5), UNKNOWN(6);

	private int code;

	UserType(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

	public static UserType fromCode(int code) {
		switch (code) {
		case 1:
			return SUPERADMIN;
		case 2:
			return ADMIN;
		case 3:
			return REGUSER;
		case 4:
			return ALLREGUSER;
		case 5:
			return ANONYMOUS;
		default:
			return UNKNOWN;
		}
	}

	public String toString() {
		switch (this) {
		case SUPERADMIN:
			return "superAdmin";
		case ADMIN:
			return "Admin";
		case REGUSER:
			return "RegUser";
		case ALLREGUSER:
			return "AllRegUser";
		case ANONYMOUS:
			return "Anonymous";
		default:
			return "Unknown";

		}
	}

	public static void main(String[] args) {
		System.out.println(UserType.ADMIN);
		System.out.println(UserType.ADMIN.code);
	}
}
