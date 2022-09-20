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
package com.autoshipservices.dao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * AutoshipDao provides functionality related to Autoship carts.
 *
 * @author naveec
 */
public interface AutoshipDao
{

	/**
	 * Gets the autoship carts count with the set of status.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @param status
	 *           the status
	 * @return the autoship carts count
	 */
	Integer getAutoshipCartsCount(CustomerModel customer, BaseSiteModel baseSite, Set<AutoShipStatus> status);

	/**
	 * Gets the autoship carts with the set of status.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @param status
	 *           the status
	 * @return the autoship carts
	 */
	List<AutoshipCartModel> getAutoshipCarts(CustomerModel customer, BaseSiteModel baseSite, Set<AutoShipStatus> status);

	/**
	 * Gets the autoship carts to process whos next run date is the current date.
	 *
	 * @param baseSite
	 *           the base site
	 * @param status
	 *           the status
	 * @param currentDate
	 *           the current date
	 * @return the autoship carts
	 */
	List<AutoshipCartModel> getAutoshipCartsToProcess(BaseSiteModel baseSite, Set<AutoShipStatus> status, Date currentDate);
}
