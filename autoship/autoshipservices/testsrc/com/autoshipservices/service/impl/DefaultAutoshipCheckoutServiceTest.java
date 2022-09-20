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
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.hooks.AutoshipCartProcessHook;
import com.autoshipservices.hooks.impl.ScheduleAutoshipCartProcessHook;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Class DefaultAutoshipCheckoutServiceTest.
 */
@UnitTest
public class DefaultAutoshipCheckoutServiceTest
{

	/** The default autoship checkout service. */
	@InjectMocks
	private final DefaultAutoshipCheckoutService defaultAutoshipCheckoutService = new DefaultAutoshipCheckoutService();

	/** The autoship cart process hooks. */
	@Mock
	private ScheduleAutoshipCartProcessHook scheduleAutoshipCartProcessHook;

	/** The commerce place order strategy. */
	@Mock
	private CommercePlaceOrderStrategy commercePlaceOrderStrategy;

	/** The commerce order result. */
	@Mock
	private CommerceOrderResult commerceOrderResult;

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
		Mockito.when(commercePlaceOrderStrategy.placeOrder(Mockito.any(CommerceCheckoutParameter.class)))
				.thenReturn(commerceOrderResult);
		Mockito.when(commerceOrderResult.getOrder()).thenReturn(new OrderModel());

		final List<AutoshipCartProcessHook> autoshipHooks = new ArrayList<>();
		autoshipHooks.add(scheduleAutoshipCartProcessHook);
		defaultAutoshipCheckoutService.setAutoshipCartProcessHooks(autoshipHooks);
		Mockito.doNothing().when(scheduleAutoshipCartProcessHook).afterProcessAutoshipCart(Mockito.any(AutoshipCartModel.class),
				Mockito.any(OrderModel.class));
		Mockito.doNothing().when(scheduleAutoshipCartProcessHook).beforeProcessAutoshipCart(Mockito.any(AutoshipCartModel.class));


	}

	/**
	 * Test process autoship cart for in active autoship cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testProcessAutoshipCartForInActiveAutoshipCart() throws InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.SAVED);
		defaultAutoshipCheckoutService.processAutoshipCart(autoshipCart);
	}

	/**
	 * Test process autoship cart for active autoship cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testProcessAutoshipCartForActiveAutoshipCart() throws InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.ACTIVE);
		final OrderModel order = defaultAutoshipCheckoutService.processAutoshipCart(autoshipCart);
		Assert.assertNotNull(order);
	}
}
