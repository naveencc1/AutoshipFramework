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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.exceptions.CalculationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipTryOnceProductService;


/**
 * The Class TryOnceProductAutoshipCartProcessHookTest.
 */
@UnitTest
public class TryOnceProductAutoshipCartProcessHookTest
{

	/** The try once product autoship cart process hook. */
	@InjectMocks
	private final TryOnceProductAutoshipCartProcessHook tryOnceProductAutoshipCartProcessHook = new TryOnceProductAutoshipCartProcessHook();

	/** The autoship try once product service. */
	@Mock
	private AutoshipTryOnceProductService autoshipTryOnceProductService;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test after process autoship cart with exception.
	 *
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Test
	public void testAfterProcessAutoshipCartWithException() throws CalculationException
	{
		Mockito.doThrow(CalculationException.class).when(autoshipTryOnceProductService)
				.removeTryonceProductsFromAutoshipCart(Mockito.any());
		tryOnceProductAutoshipCartProcessHook.afterProcessAutoshipCart(new AutoshipCartModel(), new OrderModel());
	}

	/**
	 * Test after process autoship cart.
	 *
	 * @throws CalculationException
	 *            the calculation exception
	 */
	@Test
	public void testAfterProcessAutoshipCart() throws CalculationException
	{
		Mockito.doNothing().when(autoshipTryOnceProductService).removeTryonceProductsFromAutoshipCart(Mockito.any());
		tryOnceProductAutoshipCartProcessHook.afterProcessAutoshipCart(new AutoshipCartModel(), new OrderModel());
		Mockito.verify(autoshipTryOnceProductService, Mockito.times(1)).removeTryonceProductsFromAutoshipCart(Mockito.any());
	}
}
