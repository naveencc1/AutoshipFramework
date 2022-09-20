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
import de.hybris.platform.order.InvalidCartException;

import com.autoshipfacades.data.AutoshipData;


/**
 * The Interface AutoshipScheduleFacade provides functionality for scheduling Autoship carts.
 */
public interface AutoshipScheduleFacade
{

	/**
	 * Adds the schedule data.
	 *
	 * @param autoshipData
	 *           the autoship data
	 * @return the cart data
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	CartData addScheduleData(AutoshipData autoshipData) throws InvalidCartException;
}
