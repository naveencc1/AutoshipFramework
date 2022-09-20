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
package com.autoshipservices.hooks;

import de.hybris.platform.core.model.order.OrderModel;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Interface AutoshipCartProcessHook.
 */
public interface AutoshipCartProcessHook
{

	/**
	 * Before process autoship cart.
	 */
	void beforeProcessAutoshipCart(AutoshipCartModel autoshipCart);

	/**
	 * After process autoship cart.
	 */
	void afterProcessAutoshipCart(AutoshipCartModel autoshipCart, OrderModel order);
}
