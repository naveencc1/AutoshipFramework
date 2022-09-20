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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Class DefaultAutoshipTryOnceProductServiceTest.
 */
@UnitTest
public class DefaultAutoshipTryOnceProductServiceTest
{

	/** The Default autoship try once product service. */
	@InjectMocks
	private final DefaultAutoshipTryOnceProductService DefaultAutoshipTryOnceProductService = new DefaultAutoshipTryOnceProductService();

	/** The model service. */
	@Mock
	private ModelService modelService;

	/** The commerce cart service. */
	@Mock
	private CommerceCartService commerceCartService;

	/**
	 * Sets the up.
	 *
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Before
	public void setUp() throws CalculationException
	{
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(modelService).removeAll(Mockito.anyCollection());
		Mockito.doNothing().when(modelService).refresh(Mockito.any());
		Mockito.doNothing().when(commerceCartService).recalculateCart(Mockito.any(CommerceCartParameter.class));
	}

	/**
	 * Test remove tryonce products from autoship cart without tryonce products.
	 *
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Test
	public void testRemoveTryonceProductsFromAutoshipCartWithoutTryonceProducts() throws CalculationException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		entries.add(entry);
		autoshipCart.setEntries(entries);
		DefaultAutoshipTryOnceProductService.removeTryonceProductsFromAutoshipCart(autoshipCart);
		Mockito.verify(modelService, Mockito.times(0)).removeAll(Mockito.anyCollection());
		Mockito.verify(modelService, Mockito.times(0)).refresh(Mockito.any());
		Mockito.verify(commerceCartService, Mockito.times(0)).recalculateCart(Mockito.any(CommerceCartParameter.class));
	}

	/**
	 * Test remove tryonce products from autoship cart with tryonce products.
	 *
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Test
	public void testRemoveTryonceProductsFromAutoshipCartWithTryonceProducts() throws CalculationException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setTryOnce(true);
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		entries.add(entry);
		autoshipCart.setEntries(entries);
		DefaultAutoshipTryOnceProductService.removeTryonceProductsFromAutoshipCart(autoshipCart);
		Mockito.verify(modelService, Mockito.times(1)).removeAll(Mockito.anyCollection());
		Mockito.verify(modelService, Mockito.times(1)).refresh(Mockito.any());
		Mockito.verify(commerceCartService, Mockito.times(1)).recalculateCart(Mockito.any(CommerceCartParameter.class));
	}

}
