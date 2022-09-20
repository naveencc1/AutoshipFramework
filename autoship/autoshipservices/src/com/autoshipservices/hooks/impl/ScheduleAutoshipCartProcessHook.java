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
package com.autoshipservices.hooks.impl;

import de.hybris.platform.core.model.order.OrderModel;

import com.autoshipservices.hooks.AutoshipCartProcessHook;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipScheduleService;


/**
 * The Class ScheduleAutoshipCartProcessHook.
 */
public class ScheduleAutoshipCartProcessHook implements AutoshipCartProcessHook
{

	private AutoshipScheduleService autoshipScheduleService;

	/**
	 * Before process autoship cart.
	 */
	@Override
	public void beforeProcessAutoshipCart(final AutoshipCartModel autoshipCart)
	{
		// XXX Auto-generated method stub

	}

	/**
	 * After process autoship cart.
	 */
	@Override
	public void afterProcessAutoshipCart(final AutoshipCartModel autoshipCart, final OrderModel order)
	{
		getAutoshipScheduleService().rescheduleAutoshipCart(autoshipCart);

	}

	/**
	 * @return the autoshipScheduleService
	 */
	public AutoshipScheduleService getAutoshipScheduleService()
	{
		return autoshipScheduleService;
	}

	/**
	 * @param autoshipScheduleService
	 *           the autoshipScheduleService to set
	 */
	public void setAutoshipScheduleService(final AutoshipScheduleService autoshipScheduleService)
	{
		this.autoshipScheduleService = autoshipScheduleService;
	}


}
