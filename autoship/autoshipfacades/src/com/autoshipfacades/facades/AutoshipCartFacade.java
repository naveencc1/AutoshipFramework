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
package com.autoshipfacades.facades;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.InvalidCartException;


/**
 * This Class will have the functionality related to updating Autoship carts.
 */
public interface AutoshipCartFacade
{

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
	CartModificationData addToCart(final String code, final long quantity, final String storeId, final boolean tryOnceProduct)
			throws CommerceCartModificationException, InvalidCartException;

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
	CartModificationData addToCart(final String code, final long quantity, final boolean tryOnceProduct)
			throws CommerceCartModificationException, InvalidCartException;

	/**
	 * Adds product to Autoship cart with cart params.
	 *
	 * @param addToCartParams
	 *           the add to cart params
	 * @return the cart modification data
	 */
	CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException;

	/**
	 * Update cart entry.
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
	CartModificationData updateCartEntry(long entryNumber, long quantity, boolean tryOnceProduct)
			throws CommerceCartModificationException;
}
