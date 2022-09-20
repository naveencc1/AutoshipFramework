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

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.order.InvalidCartException;


/**
 * The Interface AutoshipCheckoutFacade.
 */
public interface AutoshipCheckoutFacade
{

	/**
	 * Process autoship cart in session.
	 *
	 * @return the order data
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	OrderData processAutoshipCartInSession() throws InvalidCartException;
}
