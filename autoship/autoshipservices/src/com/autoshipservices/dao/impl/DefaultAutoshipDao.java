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
package com.autoshipservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.autoshipservices.dao.AutoshipDao;
import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * AutoshipDao provides functionality related to Autoship carts.
 *
 * @author naveec
 */
public class DefaultAutoshipDao extends AbstractItemDao implements AutoshipDao
{


	/**
	 * SELECT count({pk}) FROM {AutoshipCart} WHERE {user} = ?customer AND {site} = ?site AND {autoshipStatus} IN ?status
	 */
	protected static final String AUTOSHIP_CART_COUNT_QUERY = "SELECT count({" + AutoshipCartModel.PK + "}) FROM {"
			+ AutoshipCartModel._TYPECODE + "} WHERE {" + AutoshipCartModel.USER + "}=?customer AND {" + AutoshipCartModel.SITE
			+ "}=?site AND {" + AutoshipCartModel.AUTOSHIPSTATUS + "} IN (?status)";

	/**
	 * SELECT {pk} FROM {AutoshipCart} WHERE {user} = ?customer AND {site} = ?site AND {autoshipStatus} IN ?status
	 */
	protected static final String AUTOSHIP_CART_QUERY = "SELECT {" + AutoshipCartModel.PK + "} FROM {"
			+ AutoshipCartModel._TYPECODE + "} WHERE {" + AutoshipCartModel.USER + "}=?customer AND {" + AutoshipCartModel.SITE
			+ "}=?site AND {" + AutoshipCartModel.AUTOSHIPSTATUS + "} IN (?status)";

	protected static final String AUTOSHIP_CART_PROCESS_QUERY = "SELECT {" + AutoshipCartModel.PK + "} FROM {"
			+ AutoshipCartModel._TYPECODE + "} WHERE {" + AutoshipCartModel.SITE + "}=?site AND {" + AutoshipCartModel.AUTOSHIPSTATUS
			+ "} IN (?status) AND {" + AutoshipCartModel.NEXTRUNDATE + "}<=?currentDate";

	/**
	 * Gets the autoship carts count.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @param status
	 *           the status
	 * @return the autoship carts count
	 */
	@Override
	public Integer getAutoshipCartsCount(final CustomerModel customer, final BaseSiteModel baseSite,
			final Set<AutoShipStatus> status)
	{

		validateParameterNotNull(customer, "Customer cannot be null");
		validateParameterNotNull(baseSite, "Base site cannot be null");
		validateParameterNotNull(status, "Status cannot be null");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer", customer);
		params.put("site", baseSite);
		params.put("status", status);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(AUTOSHIP_CART_COUNT_QUERY);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchQuery.setResultClassList(Collections.singletonList(Integer.class));

		final SearchResult<Integer> result = getFlexibleSearchService().search(flexibleSearchQuery);
		return result.getResult().iterator().next();
	}

	/**
	 * Gets the autoship carts.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @param status
	 *           the status
	 * @return the autoship carts
	 */
	@Override
	public List<AutoshipCartModel> getAutoshipCarts(final CustomerModel customer, final BaseSiteModel baseSite,
			final Set<AutoShipStatus> status)
	{
		validateParameterNotNull(customer, "Customer cannot be null");
		validateParameterNotNull(baseSite, "Base site cannot be null");
		validateParameterNotNull(status, "Status cannot be null");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer", customer);
		params.put("site", baseSite);
		params.put("status", status);

		final SearchResult<AutoshipCartModel> result = getFlexibleSearchService().search(AUTOSHIP_CART_QUERY, params);
		return result.getResult() == null ? Collections.EMPTY_LIST : result.getResult();
	}

	@Override
	public List<AutoshipCartModel> getAutoshipCartsToProcess(final BaseSiteModel baseSite, final Set<AutoShipStatus> status,
			final Date currentDate)
	{
		validateParameterNotNull(baseSite, "Base site cannot be null");
		validateParameterNotNull(status, "Status cannot be null");
		validateParameterNotNull(currentDate, "CurrentDate cannot be null");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", baseSite);
		params.put("status", status);
		params.put("currentDate", currentDate);

		final SearchResult<AutoshipCartModel> result = getFlexibleSearchService().search(AUTOSHIP_CART_PROCESS_QUERY, params);
		return result.getResult() == null ? Collections.EMPTY_LIST : result.getResult();
	}


}
