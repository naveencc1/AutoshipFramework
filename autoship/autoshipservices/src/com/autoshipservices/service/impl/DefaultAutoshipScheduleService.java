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
package com.autoshipservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipScheduleService;
import com.autoshipservices.utils.AutoshipUtils;


/**
 * The Class DefaultAutoshipScheduleService.
 */
public class DefaultAutoshipScheduleService implements AutoshipScheduleService
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipScheduleService.class);

	/** The model service. */
	private ModelService modelService;

	/**
	 * Update autoship schedule.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	@Override
	public void rescheduleAutoshipCart(final AutoshipCartModel autoshipCart)
	{
		validateParameterNotNull(autoshipCart, "Autoship cart cannot be null");

		//update nextRunDate and lastRunDate of the autoship cart.
		if (null != autoshipCart.getFrequency())
		{
			autoshipCart.setNextRunDate(AutoshipUtils.addDaysToCurrentDate(autoshipCart.getFrequency()));
			autoshipCart.setLastRunDate(Calendar.getInstance().getTime());
			getModelService().save(autoshipCart);
		}
		else
		{
			LOG.error("Cannot reschedule Autoship cart : " + autoshipCart.getCode() + " there is no frequency defined for the cart");
		}

	}

	/**
	 * Gets the model service.
	 *
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets the model service.
	 *
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
