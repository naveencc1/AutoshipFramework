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
package com.autoshipservices.hooks.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.exceptions.CalculationException;

import org.apache.log4j.Logger;

import com.autoshipservices.hooks.AutoshipCartProcessHook;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipTryOnceProductService;


/**
 * The Class TryOnceProductAutoshipCartProcessHook.
 */
public class TryOnceProductAutoshipCartProcessHook implements AutoshipCartProcessHook
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(TryOnceProductAutoshipCartProcessHook.class);

	/** The autoship try once product service. */
	private AutoshipTryOnceProductService autoshipTryOnceProductService;

	/**
	 * Before process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	@Override
	public void beforeProcessAutoshipCart(final AutoshipCartModel autoshipCart)
	{
		// XXX Auto-generated method stub

	}

	/**
	 * After process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @param order
	 *           the order
	 */
	@Override
	public void afterProcessAutoshipCart(final AutoshipCartModel autoshipCart, final OrderModel order)
	{
		try
		{
			getAutoshipTryOnceProductService().removeTryonceProductsFromAutoshipCart(autoshipCart);
		}
		catch (final CalculationException ex)
		{
			LOG.error("error occured while removing tryonce products from cart " + autoshipCart.getCode(), ex);
		}
	}

	/**
	 * @return the autoshipTryOnceProductService
	 */
	public AutoshipTryOnceProductService getAutoshipTryOnceProductService()
	{
		return autoshipTryOnceProductService;
	}

	/**
	 * @param autoshipTryOnceProductService
	 *           the autoshipTryOnceProductService to set
	 */
	public void setAutoshipTryOnceProductService(final AutoshipTryOnceProductService autoshipTryOnceProductService)
	{
		this.autoshipTryOnceProductService = autoshipTryOnceProductService;
	}


}
