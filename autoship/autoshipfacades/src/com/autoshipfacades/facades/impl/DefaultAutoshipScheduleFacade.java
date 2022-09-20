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

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import com.autoshipfacades.data.AutoshipData;
import com.autoshipfacades.facades.AutoshipScheduleFacade;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Class DefaultAutoshipScheduleFacade.
 */
public class DefaultAutoshipScheduleFacade implements AutoshipScheduleFacade
{

	/** The cart service. */
	private CartService cartService;

	/** The model service. */
	private ModelService modelService;

	/** The cart converter. */
	private Converter<CartModel, CartData> cartConverter;

	/**
	 * Adds the schedule data.
	 *
	 * @param autoshipData
	 *           the autoship data
	 * @return the cart data
	 * @throws InvalidCartException
	 */
	@Override
	public CartData addScheduleData(final AutoshipData autoshipData) throws InvalidCartException
	{
		if (getCartService().hasSessionCart() && getCartService().getSessionCart() instanceof AutoshipCartModel)
		{
			final AutoshipCartModel autoshipCart = (AutoshipCartModel) getCartService().getSessionCart();
			if (null != autoshipData.getFrequency())
			{
				autoshipCart.setFrequency(autoshipData.getFrequency());
			}
			if (null != autoshipData.getNextRundate())
			{
				autoshipCart.setNextRunDate(autoshipData.getNextRundate());
			}
			getModelService().save(autoshipCart);

			return getCartConverter().convert(getCartService().getSessionCart());
		}
		else
		{
			throw new InvalidCartException("Current session cart is not valid Autoship cart");
		}

	}

	/**
	 * Gets the cart service.
	 *
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * Sets the cart service.
	 *
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * Gets the model service.
	 *
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets the model service.
	 *
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * Gets the cart converter.
	 *
	 * @return the cartConverter
	 */
	public Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	/**
	 * Sets the cart converter.
	 *
	 * @param cartConverter
	 *           the cartConverter to set
	 */
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

}
