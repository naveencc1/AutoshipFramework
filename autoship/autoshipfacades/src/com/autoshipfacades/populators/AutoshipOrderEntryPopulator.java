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

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.BooleanUtils;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * Populates the entry data related top Autoship cart.
 */
public class AutoshipOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{


	/**
	 * Populate attributes related to autoship cart.
	 *
	 * @param source
	 *           the source
	 * @param target
	 *           the target
	 * @throws ConversionException
	 *            the conversion exception
	 */
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target) throws ConversionException
	{
		if (source.getOrder() instanceof AutoshipCartModel)
		{
			target.setTryOnceProduct(BooleanUtils.isTrue(source.getTryOnce()));
		}

	}

}
