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
package com.autoshipservices.jobs;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.dao.AutoshipDao;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.model.AutoshipProcessCronJobModel;
import com.autoshipservices.service.AutoshipCheckoutService;


/**
 * The Class ProcessAutoshipCartCronjobTest.
 */
@UnitTest
public class ProcessAutoshipCartCronjobTest
{

	/** The process autoship cart cronjob. */
	@InjectMocks
	private final ProcessAutoshipCartCronjob processAutoshipCartCronjob = new ProcessAutoshipCartCronjob();

	/** The autoship dao. */
	@Mock
	private AutoshipDao autoshipDao;

	/** The autoship checkout service. */
	@Mock
	private AutoshipCheckoutService autoshipCheckoutService;

	/** The autoship process cron job model. */
	@Mock
	private AutoshipProcessCronJobModel autoshipProcessCronJobModel;

	/** The base site model. */
	@Mock
	private BaseSiteModel baseSiteModel;

	/** The time service. */
	@Mock
	private TimeService timeService;

	/** The autoship cart. */
	@Mock
	private AutoshipCartModel autoshipCart;

	/** The impersonation service. */
	@Mock
	private ImpersonationService impersonationService;

	/**
	 * Sets the up.
	 *
	 * @throws Throwable
	 */
	@Before
	public void setUp() throws Throwable
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(autoshipProcessCronJobModel.getSites()).thenReturn(Arrays.asList(baseSiteModel));
		Mockito.when(autoshipDao.getAutoshipCartsToProcess(Mockito.any(), Mockito.anySet(), Mockito.any()))
				.thenReturn(Arrays.asList(autoshipCart));
		Mockito.when(autoshipCheckoutService.processAutoshipCart(Mockito.any())).thenReturn(new OrderModel());


	}

	/**
	 * Test perform without sites.
	 */
	@Test
	public void testPerformWithoutSites()
	{
		Mockito.when(autoshipProcessCronJobModel.getSites()).thenReturn(Collections.EMPTY_LIST);
		final PerformResult result = processAutoshipCartCronjob.perform(autoshipProcessCronJobModel);
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Mockito.verify(autoshipDao, Mockito.times(0)).getAutoshipCartsToProcess(Mockito.any(), Mockito.anySet(), Mockito.any());
	}

	/**
	 * Test perform with sites and autoship carts.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testPerformWithSitesAndAutoshipCarts() throws Throwable
	{
		Mockito.when(impersonationService.executeInContext(Mockito.any(), Mockito.any())).thenReturn(new Object());
		final PerformResult result = processAutoshipCartCronjob.perform(autoshipProcessCronJobModel);
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Mockito.verify(autoshipDao, Mockito.times(1)).getAutoshipCartsToProcess(Mockito.any(), Mockito.anySet(), Mockito.any());
	}

	/**
	 * Test perform with sites and no autoship carts.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testPerformWithSitesAndNoAutoshipCarts() throws InvalidCartException
	{
		Mockito.when(autoshipDao.getAutoshipCartsToProcess(Mockito.any(), Mockito.anySet(), Mockito.any()))
				.thenReturn(Collections.EMPTY_LIST);
		final PerformResult result = processAutoshipCartCronjob.perform(autoshipProcessCronJobModel);
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Mockito.verify(autoshipDao, Mockito.times(1)).getAutoshipCartsToProcess(Mockito.any(), Mockito.anySet(), Mockito.any());
		Mockito.verify(autoshipCheckoutService, Mockito.times(0)).processAutoshipCart(Mockito.any());
	}

	/**
	 * Test perform with exception.
	 */
	@Test
	public void testPerformWithException()
	{
		Mockito.doThrow(Exception.class).when(autoshipProcessCronJobModel).getSites();
		final PerformResult result = processAutoshipCartCronjob.perform(autoshipProcessCronJobModel);
		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
	}
}
