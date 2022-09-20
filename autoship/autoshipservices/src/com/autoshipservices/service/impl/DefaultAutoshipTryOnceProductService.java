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

import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.lang3.BooleanUtils;

import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipTryOnceProductService;


/**
 * The Class DefaultAutoshipTryOnceProductService.
 */
public class DefaultAutoshipTryOnceProductService implements AutoshipTryOnceProductService
{

	/** The model service. */
	private ModelService modelService;

	/** The cart service. */
	private CommerceCartService commerceCartService;

	/**
	 * Removes the tryonce products from autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Override
	public void removeTryonceProductsFromAutoshipCart(final AutoshipCartModel autoshipCart) throws CalculationException
	{
		//Remove tryonce products from the cart
		final List<AbstractOrderEntryModel> entries = autoshipCart.getEntries().stream()
				.filter(entry -> BooleanUtils.isTrue(entry.getTryOnce())).collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(entries))
		{
			getModelService().removeAll(entries);
			getModelService().refresh(autoshipCart);

			//Normalize the entry numbers after removing tryOnce entry
			normalizeEntryNumbers(autoshipCart);

			//Recalculate the cart after removing TryOnce products from the Autoship Cart.
			recalculateCart(autoshipCart);
		}
	}

	/**
	 * Recalculate cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws CalculationException
	 *            the calculation exception
	 */
	private void recalculateCart(final AutoshipCartModel autoshipCart) throws CalculationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(autoshipCart);
		getCommerceCartService().recalculateCart(parameter);
	}

	/**
	 * Normalize entry numbers.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 */
	protected void normalizeEntryNumbers(final AutoshipCartModel autoshipCart)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>(autoshipCart.getEntries());
		Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
		for (int i = 0; i < entries.size(); i++)
		{
			entries.get(i).setEntryNumber(Integer.valueOf(i));
			getModelService().save(entries.get(i));
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

	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}


}
