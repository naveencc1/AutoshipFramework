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
package com.autoshipfacades.facades.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import org.apache.log4j.Logger;

import com.autoshipfacades.facades.AutoshipCheckoutFacade;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipCheckoutService;


/**
 * The Class DefaultAutoshipCheckoutFacade.
 */
public class DefaultAutoshipCheckoutFacade extends DefaultCheckoutFacade implements AutoshipCheckoutFacade
{
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipCheckoutFacade.class);

	private AutoshipCheckoutService autoshipCheckoutService;

	/**
	 * Process autoship cart in session.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public OrderData processAutoshipCartInSession() throws InvalidCartException
	{
		final CartModel cart = getCart();
		if (cart instanceof AutoshipCartModel)
		{

			final AutoshipCartModel autoshipCart = (AutoshipCartModel) cart;
			beforeProcessAutoshipCart(autoshipCart);
			final OrderModel order = getAutoshipCheckoutService().processAutoshipCart(autoshipCart);
			afterProcessAutoshipCart(autoshipCart, order);
			return getOrderConverter().convert(order);
		}
		else
		{
			LOG.error("Session cart with Id : " + cart.getCode() + ", is not a Autoship cart");
			throw new InvalidCartException("Session cart is not a valid Autoship cart");
		}
	}

	/**
	 * Before process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	public void beforeProcessAutoshipCart(final AutoshipCartModel autoshipCart)
	{
		// can be extended for logic before processing Autoship cart.
	}

	/**
	 * After process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	public void afterProcessAutoshipCart(final AutoshipCartModel autoshipCart, final OrderModel order)
	{
		// can be extended for logic after processing Autoship cart.
	}

	/**
	 * @return the autoshipCheckoutService
	 */
	public AutoshipCheckoutService getAutoshipCheckoutService()
	{
		return autoshipCheckoutService;
	}

	/**
	 * @param autoshipCheckoutService
	 *           the autoshipCheckoutService to set
	 */
	public void setAutoshipCheckoutService(final AutoshipCheckoutService autoshipCheckoutService)
	{
		this.autoshipCheckoutService = autoshipCheckoutService;
	}



}
