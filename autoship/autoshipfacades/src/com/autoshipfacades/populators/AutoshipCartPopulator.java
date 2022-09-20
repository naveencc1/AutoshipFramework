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
package com.autoshipfacades.populators;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.autoshipfacades.data.AutoshipData;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * This class is responsible for populating Autoship cart attributes.
 *
 * @author naveec
 */
public class AutoshipCartPopulator implements Populator<CartModel, CartData>
{

	/**
	 * Populates autoship cart related Attributes.
	 */
	@Override
	public void populate(final CartModel source, final CartData target) throws ConversionException
	{
		if (source instanceof AutoshipCartModel)
		{
			final AutoshipCartModel autoshipCart = (AutoshipCartModel) source;
			final AutoshipData autoshipData = new AutoshipData();
			autoshipData.setFrequency(autoshipCart.getFrequency());
			autoshipData.setNextRundate(autoshipCart.getNextRunDate());
			autoshipData.setLastRunDate(autoshipCart.getLastRunDate());
			autoshipData.setAutoshipStatus(autoshipCart.getAutoShipStatus());
			target.setAutoshipData(autoshipData);
		}
	}
}
