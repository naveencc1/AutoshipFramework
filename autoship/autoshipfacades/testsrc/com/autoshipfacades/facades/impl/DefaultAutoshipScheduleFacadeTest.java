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
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipfacades.data.AutoshipData;
import com.autoshipservices.model.AutoshipCartModel;


/**
 * The Class DefaultAutoshipScheduleFacadeTest.
 */
@UnitTest
public class DefaultAutoshipScheduleFacadeTest
{

	/** The default autoship schedule facade. */
	@InjectMocks
	private final DefaultAutoshipScheduleFacade defaultAutoshipScheduleFacade = new DefaultAutoshipScheduleFacade();

	/** The cart service. */
	@Mock
	private CartService cartService;

	/** The model service. */
	@Mock
	private ModelService modelService;

	/** The cart converter. */
	@Mock
	private Converter<CartModel, CartData> cartConverter;

	/** The autoship cart. */
	@Mock
	private AutoshipCartModel autoshipCart;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultAutoshipScheduleFacade.setCartConverter(cartConverter);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(autoshipCart);
		Mockito.doNothing().when(modelService).save(Mockito.any());
		Mockito.when(cartConverter.convert(Mockito.any())).thenReturn(new CartData());
	}

	/**
	 * Test add schedule data with non autoship cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testAddScheduleDataWithNonAutoshipCart() throws InvalidCartException
	{
		final AutoshipData autoshipData = new AutoshipData();
		Mockito.when(cartService.getSessionCart()).thenReturn(new CartModel());
		defaultAutoshipScheduleFacade.addScheduleData(autoshipData);
	}

	/**
	 * Test add schedule data without session cart.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testAddScheduleDataWithoutSessionCart() throws InvalidCartException
	{
		final AutoshipData autoshipData = new AutoshipData();
		Mockito.when(cartService.hasSessionCart()).thenReturn(false);
		defaultAutoshipScheduleFacade.addScheduleData(autoshipData);
	}

	/**
	 * Test add schedule data with autoship cart and frequency and next run date.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testAddScheduleDataWithAutoshipCartAndFrequencyAndNextRunDate() throws InvalidCartException
	{
		final AutoshipData autoshipData = new AutoshipData();
		autoshipData.setFrequency(10);
		autoshipData.setNextRundate(new Date());
		defaultAutoshipScheduleFacade.addScheduleData(autoshipData);
		Mockito.verify(autoshipCart, Mockito.times(1)).setFrequency(Mockito.anyInt());
		Mockito.verify(autoshipCart, Mockito.times(1)).setNextRunDate(Mockito.any());
		Mockito.verify(modelService, Mockito.times(1)).save(Mockito.any());
	}

	/**
	 * Test add schedule data with autoship cart and no frequency and no next run date.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testAddScheduleDataWithAutoshipCartAndNoFrequencyAndNoNextRunDate() throws InvalidCartException
	{
		final AutoshipData autoshipData = new AutoshipData();
		defaultAutoshipScheduleFacade.addScheduleData(autoshipData);
		Mockito.verify(autoshipCart, Mockito.times(0)).setFrequency(Mockito.anyInt());
		Mockito.verify(autoshipCart, Mockito.times(0)).setNextRunDate(Mockito.any());
		Mockito.verify(modelService, Mockito.times(1)).save(Mockito.any());
	}
}
