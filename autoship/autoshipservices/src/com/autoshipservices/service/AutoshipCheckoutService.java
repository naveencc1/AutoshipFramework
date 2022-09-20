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

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Interface AutoshipCheckoutService.
 */
public interface AutoshipCheckoutService
{

	/**
	 * Process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @return the order model
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	OrderModel processAutoshipCart(AutoshipCartModel autoshipCart) throws InvalidCartException;
}
