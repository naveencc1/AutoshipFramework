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

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.InvalidCartException;

import org.apache.log4j.Logger;

import com.autoshipfacades.facades.AutoshipCartFacade;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * This Class will have the functionality related to updating Autoship carts.
 */
public class DefaultAutoshipCartFacade extends DefaultCartFacade implements AutoshipCartFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipCartFacade.class);

	/**
	 * Adds product to Autoship cart. This method takes tryOnceProduct flag as parameter which is added to autoship cart
	 * and processed only once. This is for Pickup order.
	 *
	 * @param code
	 *           the code
	 * @param quantity
	 *           the quantity
	 * @param storeId
	 *           the store id
	 * @param tryOnceProduct
	 *           the try once product
	 * @return the cart modification data
	 * @throws InvalidCartException
	 */
	@Override
	public CartModificationData addToCart(final String code, final long quantity, final String storeId,
			final boolean tryOnceProduct) throws CommerceCartModificationException, InvalidCartException
	{
		if (!(getCartService().getSessionCart() instanceof AutoshipCartModel))
		{
			LOG.error("Session cart is not Autoship cart");
			throw new InvalidCartException("Invalid session cart, cart is not of type AutoshipCartModel");
		}
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setStoreId(storeId);
		params.setTryOnceProduct(tryOnceProduct);

		return addToCart(params);
	}

	/**
	 * Adds product to Autoship cart. This method takes tryOnceProduct flag as parameter which is added to autoship cart
	 * and processed only once.
	 *
	 * @param code
	 *           the code
	 * @param quantity
	 *           the quantity
	 * @param tryOnceProduct
	 *           the try once product
	 * @return the cart modification data
	 * @throws InvalidCartException
	 */
	@Override
	public CartModificationData addToCart(final String code, final long quantity, final boolean tryOnceProduct)
			throws CommerceCartModificationException, InvalidCartException
	{
		if (!(getCartService().getSessionCart() instanceof AutoshipCartModel))
		{
			LOG.error("Session cart is not Autoship cart");
			throw new InvalidCartException("Invalid session cart, cart is not of type AutoshipCartModel");
		}

		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setTryOnceProduct(tryOnceProduct);

		return addToCart(params);
	}

	/**
	 * Adds product to Autoship cart with cart params.
	 *
	 * @param addToCartParams
	 *           the add to cart params
	 * @return the cart modification data
	 */
	@Override
	public CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(addToCartParams);
		final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);

		// updates the tryOnce flag in order entry to know if the associated product is tryOnce product.
		updateTryonceFlagInEntry(addToCartParams.isTryOnceProduct(), modification);

		return getCartModificationConverter().convert(modification);
	}


	/**
	 * This method updates the tryOnce flag in cart entry.
	 */
	private void updateTryonceFlagInEntry(final boolean isTryOnceProduct, final CommerceCartModification modification)
	{
		final AbstractOrderEntryModel entry = modification.getEntry();
		entry.setTryOnce(isTryOnceProduct);
		getModelService().save(entry);
	}

	/**
	 * Updates cart entry with tryOnce product flag.
	 *
	 * @param entryNumber
	 *           the entry number
	 * @param quantity
	 *           the quantity
	 * @param tryOnceProduct
	 *           the try once product
	 * @return the cart modification data
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity, final boolean tryOnceProduct)
			throws CommerceCartModificationException
	{
		final AddToCartParams dto = new AddToCartParams();
		dto.setQuantity(quantity);
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(dto);
		parameter.setEnableHooks(true);
		parameter.setEntryNumber(entryNumber);

		final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(parameter);

		// updates the tryOnce flag in order entry to know if the associated product is tryOnce product.
		updateTryonceFlagInEntry(tryOnceProduct, modification);

		return getCartModificationConverter().convert(modification);
	}
}
