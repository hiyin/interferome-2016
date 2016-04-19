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
package edu.monash.merc.common.sql;

import org.hibernate.criterion.Order;

/**
 * OrderBy class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class OrderBy extends Condition {

	private OrderType orderType;

	public static enum OrderType {
		ASC, DESC
	}

	protected OrderBy(String fieldName, OrderType orderType) {
		this.fieldName = fieldName;
		this.orderType = orderType;
	}

	public static OrderBy asc(String fieldName) {
		return new OrderBy(fieldName, OrderType.ASC);
	}

	public static OrderBy desc(String fieldName) {
		return new OrderBy(fieldName, OrderType.DESC);
	}

	public Order getOrder() {
		Order order = null;
		if (OrderType.ASC == orderType) {
			order = Order.asc(getFieldName());
		} else if (OrderType.DESC == orderType) {
			order = Order.desc(getFieldName());
		}
		return order;
	}

	public OrderType getOrderTypeCode() {
		return orderType;
	}

	// public static void main(String[] args) {
	//
	// OrderBy orderBy = new OrderBy("createdTime", OrderType.DESC);
	//
	// }
}
