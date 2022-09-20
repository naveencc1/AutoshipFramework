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

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;


/**
 * Inteface to provide functionality related to Autoship cart.
 */
public interface AutoshipFacade
{
	/**
	 * create autoship cart for current user and current site
	 *
	 * @return CartData
	 */
	CartData createAutoshipCartForCurrentUserAndSite();

	/**
	 * Convert session cart to autoship cart.
	 *
	 * @return the cart data
	 */
	CartData convertSessionCartToAutoshipCart();

	/**
	 * Convert order to autoship cart.
	 *
	 * @param orderCode
	 *           the order code
	 * @return the cart data
	 */
	CartData convertOrderToAutoshipCart(String orderCode);

	/**
	 * Gets the active autoship carts for session user and site.
	 *
	 * @return the active autoship carts for session user and site
	 */
	List<CartData> getActiveAutoshipCartsForSessionUserAndSite();

	/**
	 * Gets the active autoship carts for session user and site.
	 *
	 * @return the active autoship carts for session user and site
	 */
	List<CartData> getAutoshipCartsForSessionUserAndSite();

	/**
	 * Save autoship session cart.
	 *
	 * @throws InvalidCartException
	 * @throws CommerceCartModificationException
	 */
	void saveAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException;

	/**
	 * Activate autoship session cart.
	 *
	 * @throws InvalidCartException
	 * @throws CommerceCartModificationException
	 */
	void activateAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException;
}
