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
package com.autoshipservices.validator;

import de.hybris.platform.order.InvalidCartException;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Interface AutoshipCartValidator can be implemented for validating Autoship before processing the Autoship cart.
 */
public interface AutoshipCartValidator
{

	/**
	 * Validate autoship cart method to be implemented for validating Autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	void validateAutoshipCart(AutoshipCartModel autoshipCart) throws InvalidCartException;
}
