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

import de.hybris.platform.order.exceptions.CalculationException;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Interface AutoshipTryOnceProductService provides the functionality WRT tryonce products.
 */
public interface AutoshipTryOnceProductService
{

	/**
	 * Removes the tryonce products from autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws CalculationException
	 */
	void removeTryonceProductsFromAutoshipCart(AutoshipCartModel autoshipCart) throws CalculationException;
}
