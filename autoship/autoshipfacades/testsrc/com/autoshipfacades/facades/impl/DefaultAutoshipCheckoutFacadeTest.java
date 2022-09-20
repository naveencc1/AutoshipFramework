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
package com.autoshipfacades.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipCheckoutService;


/**
 * The Class DefaultAutoshipCheckoutFacadeTest.
 */
@UnitTest
public class DefaultAutoshipCheckoutFacadeTest
{

	/** The default autoship checkout facade. */
	@InjectMocks
	private final DefaultAutoshipCheckoutFacade defaultAutoshipCheckoutFacade = new DefaultAutoshipCheckoutFacade();

	/** The cart facade. */
	@Mock
	private CartFacade cartFacade;

	/** The cart service. */
	@Mock
	private CartService cartService;

	/** The autoship checkout service. */
	@Mock
	private AutoshipCheckoutService autoshipCheckoutService;

	/** The order converter. */
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;

	/**
	 * Sets the up.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Before
	public void setUp() throws InvalidCartException
	{
		MockitoAnnotations.initMocks(this);
		defaultAutoshipCheckoutFacade.setOrderConverter(orderConverter);
		Mockito.when(cartFacade.hasSessionCart()).thenReturn(true);
		Mockito.when(autoshipCheckoutService.processAutoshipCart(Mockito.any(AutoshipCartModel.class)))
				.thenReturn(new OrderModel());
		Mockito.when(orderConverter.convert(Mockito.any(OrderModel.class))).thenReturn(new OrderData());
	}

	/**
	 * Test process autoship cart in session for non autoship cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testProcessAutoshipCartInSessionForNonAutoshipCart() throws InvalidCartException
	{
		final CartModel cart = new CartModel();
		Mockito.when(cartService.getSessionCart()).thenReturn(cart);
		defaultAutoshipCheckoutFacade.processAutoshipCartInSession();
	}

	/**
	 * Test process autoship cart in session for autoship cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testProcessAutoshipCartInSessionForAutoshipCart() throws InvalidCartException
	{
		final CartModel cart = new AutoshipCartModel();
		Mockito.when(cartService.getSessionCart()).thenReturn(cart);
		final OrderData order = defaultAutoshipCheckoutFacade.processAutoshipCartInSession();
		Assert.assertNotNull(order);
	}
}
