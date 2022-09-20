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
package com.autoshipservices.service.impl;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;

import org.apache.log4j.Logger;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.hooks.AutoshipCartProcessHook;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipCheckoutService;
import com.autoshipservices.validator.AutoshipCartValidator;


/**
 * The Class DefaultAutoshipCheckoutService.
 */
public class DefaultAutoshipCheckoutService extends DefaultCommerceCheckoutService implements AutoshipCheckoutService
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipCheckoutService.class);

	/** The autoship cart process hooks. */
	private List<AutoshipCartProcessHook> autoshipCartProcessHooks;

	private AutoshipCartValidator autoshipCartValidator;

	/**
	 * Process autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public OrderModel processAutoshipCart(final AutoshipCartModel autoshipCart) throws InvalidCartException
	{
		if (AutoShipStatus.ACTIVE.equals(autoshipCart.getAutoShipStatus()))
		{
			//Validates the Autoship carts there is no current implementation for AutoshipCartValidator.
			//AutoshipCartValidator shoud be implemented if we need to have validation on Autoship cart and add the autoship validator bean as a property.
			if (null != getAutoshipCartValidator())
			{
				getAutoshipCartValidator().validateAutoshipCart(autoshipCart);
			}
			beforePlaceAutoshipOrder(autoshipCart);
			final OrderModel order = placeOrder(autoshipCart);
			afterPlaceAutoshipOrder(autoshipCart, order);
			return order;
		}
		else
		{
			throw new UnsupportedOperationException("Cannot process Inactive Autoship carts");
		}
	}

	/**
	 * Place order.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @return the order model
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	private OrderModel placeOrder(final AutoshipCartModel autoshipCart) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(autoshipCart);
		parameter.setSalesApplication(SalesApplication.WEB);
		return placeOrder(parameter).getOrder();
	}

	/**
	 * Before place autoship order.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	public void beforePlaceAutoshipOrder(final AutoshipCartModel autoshipCart)
	{
		if (getAutoshipCartProcessHooks() != null)
		{
			for (final AutoshipCartProcessHook autoshipCartProcessHook : getAutoshipCartProcessHooks())
			{
				autoshipCartProcessHook.beforeProcessAutoshipCart(autoshipCart);
			}
		}
	}

	/**
	 * After place autoship order.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	public void afterPlaceAutoshipOrder(final AutoshipCartModel autoshipCart, final OrderModel order)
	{
		if (getAutoshipCartProcessHooks() != null)
		{
			for (final AutoshipCartProcessHook autoshipCartProcessHook : getAutoshipCartProcessHooks())
			{
				autoshipCartProcessHook.afterProcessAutoshipCart(autoshipCart, order);
			}
		}
	}

	/**
	 * Gets the autoship cart process hooks.
	 *
	 * @return the autoshipCartProcessHooks
	 */
	public List<AutoshipCartProcessHook> getAutoshipCartProcessHooks()
	{
		return autoshipCartProcessHooks;
	}

	/**
	 * Sets the autoship cart process hooks.
	 *
	 * @param autoshipCartProcessHooks
	 *           the autoshipCartProcessHooks to set
	 */
	public void setAutoshipCartProcessHooks(final List<AutoshipCartProcessHook> autoshipCartProcessHooks)
	{
		this.autoshipCartProcessHooks = autoshipCartProcessHooks;
	}

	/**
	 * @return the autoshipCartValidator
	 */
	public AutoshipCartValidator getAutoshipCartValidator()
	{
		return autoshipCartValidator;
	}

	/**
	 * @param autoshipCartValidator
	 *           the autoshipCartValidator to set
	 */
	public void setAutoshipCartValidator(final AutoshipCartValidator autoshipCartValidator)
	{
		this.autoshipCartValidator = autoshipCartValidator;
	}


}
