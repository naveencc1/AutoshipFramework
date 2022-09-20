/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.autoshipservices.service;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Interface AutoshipScheduleService is responsible for functionalities related to autoship schedule.
 */
public interface AutoshipScheduleService
{

	/**
	 * Updates autoship schedule according to frequency of the Autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	void rescheduleAutoshipCart(AutoshipCartModel autoshipCart);

}
