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

import com.autoshipservices.model.AutoshipCartModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The Class DefaultAutoshipCartFacadeTest.
 */
@UnitTest
public class DefaultAutoshipCartFacadeTest
{

	/** The default autoship cart facade. */
	@InjectMocks
	private final DefaultAutoshipCartFacade defaultAutoshipCartFacade = new DefaultAutoshipCartFacade();

	/** The commerce cart parameter converter. */
	@Mock
	private Converter<AddToCartParams, CommerceCartParameter> commerceCartParameterConverter;

	/** The commerce cart service. */
	@Mock
	private CommerceCartService commerceCartService;

	/** The cart modification converter. */
	@Mock
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;

	/** The model service. */
	@Mock
	private ModelService modelService;

	/** The commerce cart parameter. */
	@Mock
	private CommerceCartParameter commerceCartParameter;

	/** The commerce cart modification. */
	@Mock
	private CommerceCartModification commerceCartModification;

	@Mock
	private CartService cartService;

	/** The abstract order entry model. */
	private AbstractOrderEntryModel abstractOrderEntryModel;

	/**
	 * Sets the up.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Before
	public void setUp() throws CommerceCartModificationException
	{
		MockitoAnnotations.initMocks(this);
		defaultAutoshipCartFacade.setCommerceCartParameterConverter(commerceCartParameterConverter);
		abstractOrderEntryModel = new AbstractOrderEntryModel();
		Mockito.when(commerceCartParameterConverter.convert(Mockito.any(AddToCartParams.class))).thenReturn(commerceCartParameter);
		Mockito.when(commerceCartService.addToCart(Mockito.any(CommerceCartParameter.class))).thenReturn(commerceCartModification);
		Mockito.when(commerceCartService.updateQuantityForCartEntry(Mockito.any(CommerceCartParameter.class)))
				.thenReturn(commerceCartModification);
		Mockito.when(commerceCartModification.getEntry()).thenReturn(abstractOrderEntryModel);
		Mockito.when(cartModificationConverter.convert(Mockito.any(CommerceCartModification.class)))
				.thenReturn(new CartModificationData());
		Mockito.when(cartService.getSessionCart()).thenReturn(new AutoshipCartModel());
	}

	/**
	 * Test add to cart with try once.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 */
	@Test
	public void testAddToCartWithTryOnce() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModificationData result = defaultAutoshipCartFacade.addToCart("12345", 1, true);
		Assert.assertTrue(abstractOrderEntryModel.getTryOnce().booleanValue());
	}

	/**
	 * Test add to cart with out try once.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 */
	@Test
	public void testAddToCartWithOutTryOnce() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModificationData result = defaultAutoshipCartFacade.addToCart("12345", 1, false);
		Assert.assertFalse(abstractOrderEntryModel.getTryOnce().booleanValue());
	}

	/**
	 * Test add to cart with try once with store id.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 */
	@Test
	public void testAddToCartWithTryOnceWithStoreId() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModificationData result = defaultAutoshipCartFacade.addToCart("12345", 1, "12345", true);
		Assert.assertTrue(abstractOrderEntryModel.getTryOnce().booleanValue());
	}

	/**
	 * Test add to cart with out try once with store id.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 */
	@Test
	public void testAddToCartWithOutTryOnceWithStoreId() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModificationData result = defaultAutoshipCartFacade.addToCart("12345", 1, "12345", false);
		Assert.assertFalse(abstractOrderEntryModel.getTryOnce().booleanValue());
	}

	/**
	 * Test update entry with try once.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Test
	public void testUpdateEntryWithTryOnce() throws CommerceCartModificationException
	{
		final CartModificationData result = defaultAutoshipCartFacade.updateCartEntry(1, 1, true);
		Assert.assertTrue(abstractOrderEntryModel.getTryOnce().booleanValue());
	}

	/**
	 * Test update entry with out try once.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Test
	public void testUpdateEntryWithOutTryOnce() throws CommerceCartModificationException
	{
		final CartModificationData result = defaultAutoshipCartFacade.updateCartEntry(1, 1, false);
		Assert.assertFalse(abstractOrderEntryModel.getTryOnce().booleanValue());
	}
}
