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
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Class DefaultAutoshipScheduleServiceTest.
 */
@UnitTest
public class DefaultAutoshipScheduleServiceTest
{

	/** The default autoship schedule service. */
	@InjectMocks
	private final DefaultAutoshipScheduleService defaultAutoshipScheduleService = new DefaultAutoshipScheduleService();

	/** The model service. */
	@Mock
	private ModelService modelService;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(modelService).save(Mockito.any());
	}

	/**
	 * Test reschedule autoship cart without frequency.
	 */
	@Test
	public void testRescheduleAutoshipCartWithoutFrequency()
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		defaultAutoshipScheduleService.rescheduleAutoshipCart(autoshipCart);
		Assert.assertNull(autoshipCart.getNextRunDate());
		Assert.assertNull(autoshipCart.getLastRunDate());
	}

	/**
	 * Test reschedule autoship cart with frequency.
	 */
	@Test
	public void testRescheduleAutoshipCartWithFrequency()
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setFrequency(10);
		defaultAutoshipScheduleService.rescheduleAutoshipCart(autoshipCart);
		Assert.assertEquals(10, getDaysDifferenceForDates(Calendar.getInstance().getTime(), autoshipCart.getNextRunDate()));
		Assert.assertNotNull(autoshipCart.getLastRunDate());
	}

	/**
	 * Gets the days difference for dates.
	 *
	 * @param date1
	 *           the date 1
	 * @param date2
	 *           the date 2
	 * @return the days difference for dates
	 */
	private int getDaysDifferenceForDates(final Date date1, final Date date2)
	{
		int days = Days.daysBetween(new LocalDate(date1),
				new LocalDate(date2)).getDays();
		return days;

	}
}
