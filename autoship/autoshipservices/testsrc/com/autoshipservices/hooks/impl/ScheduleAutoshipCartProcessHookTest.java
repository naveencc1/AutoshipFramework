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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipScheduleService;


/**
 * The Class ScheduleAutoshipCartProcessHookTest.
 */
@UnitTest
public class ScheduleAutoshipCartProcessHookTest
{

	/** The schedule autoship cart process hook. */
	@InjectMocks
	private final ScheduleAutoshipCartProcessHook scheduleAutoshipCartProcessHook = new ScheduleAutoshipCartProcessHook();

	/** The autoship schedule service. */
	@Mock
	private AutoshipScheduleService autoshipScheduleService;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(autoshipScheduleService).rescheduleAutoshipCart(Mockito.any());
	}

	/**
	 * Test after process autoship cart.
	 */
	@Test
	public void testAfterProcessAutoshipCart()
	{
		scheduleAutoshipCartProcessHook.afterProcessAutoshipCart(new AutoshipCartModel(), new OrderModel());
		Mockito.verify(autoshipScheduleService, Mockito.times(1)).rescheduleAutoshipCart(Mockito.any());
	}
}
