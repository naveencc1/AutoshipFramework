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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * Autoship Service provides the Creation/Retrieval functionality for Autoship carts.
 *
 * @author naveec
 */
public interface AutoshipService
{
	/**
	 * Creates the autoship cart for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the autoship cart model
	 */
	AutoshipCartModel createAutoshipCartForUserAndSite(CustomerModel customer, BaseSiteModel baseSite);

	/**
	 * Checks for Maximum allowed Autoship cart for customer per site.
	 */
	boolean isAutoshipMaxAllowedCountReached(CustomerModel customer, BaseSiteModel baseSite);

	/**
	 * Convert cart to autoship cart.
	 *
	 * @param cart
	 *           the cart
	 * @return the autoship cart model
	 */
	AutoshipCartModel convertCartToAutoshipCart(CartModel cart);

	/**
	 * Convert order to autoship cart.
	 *
	 * @param cart
	 *           the cart
	 * @return the autoship cart model
	 */
	AutoshipCartModel convertOrderToAutoshipCart(OrderModel cart);

	/**
	 * Gets the active autoship carts for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the active autoship carts for user and site
	 */
	List<AutoshipCartModel> getActiveAutoshipCartsForUserAndSite(CustomerModel customer, BaseSiteModel baseSite);

	/**
	 * Gets the autoship carts for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the autoship carts for user and site
	 */
	List<AutoshipCartModel> getAutoshipCartsForUserAndSite(CustomerModel customer, BaseSiteModel baseSite);

	/**
	 * Sets the autoshipstatus.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @param status
	 *           the status
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	void setAutoshipStatus(AutoshipCartModel autoshipCart, AutoShipStatus status)
			throws CommerceCartModificationException, InvalidCartException;
}
