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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipService;


/**
 * The Class DefaultAutoshipFacadeTest.
 *
 * @author naveec
 */
@UnitTest
public class DefaultAutoshipFacadeTest
{
	@InjectMocks
	private final DefaultAutoshipFacade defaultAutoshipFacade = new DefaultAutoshipFacade();

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private UserService userService;

	@Mock
	private AutoshipService autoshipService;

	@Mock
	private Converter<CartModel, CartData> cartConverter;

	@Mock
	private CartService cartService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(baseSiteService.getCurrentBaseSite()).thenReturn(new BaseSiteModel());
		when(userService.getCurrentUser()).thenReturn(new CustomerModel());

		when(cartConverter.convert(any(AutoshipCartModel.class))).thenReturn(new CartData());
		when(cartConverter.convertAll(Mockito.anyCollection())).thenReturn(Arrays.asList(new CartData()));

		when(cartService.hasSessionCart()).thenReturn(true);
		when(cartService.getSessionCart()).thenReturn(new CartModel());

		when(autoshipService.convertCartToAutoshipCart(any(CartModel.class))).thenReturn(new AutoshipCartModel());
		when(autoshipService.createAutoshipCartForUserAndSite(any(CustomerModel.class), any(BaseSiteModel.class)))
				.thenReturn(new AutoshipCartModel());
		when(autoshipService.getActiveAutoshipCartsForUserAndSite(any(CustomerModel.class), any(BaseSiteModel.class)))
				.thenReturn(Arrays.asList(new AutoshipCartModel()));
	}

	/**
	 * Test create autoship cart for current user and site.
	 */
	@Test
	public void testCreateAutoshipCartForCurrentUserAndSite()
	{
		final CartData result = defaultAutoshipFacade.createAutoshipCartForCurrentUserAndSite();
		Assert.assertNotNull(result);
		verify(baseSiteService, times(1)).getCurrentBaseSite();
		verify(userService, times(1)).getCurrentUser();
		verify(autoshipService, times(1)).createAutoshipCartForUserAndSite(any(CustomerModel.class), any(BaseSiteModel.class));
		verify(cartConverter, times(1)).convert(any(AutoshipCartModel.class));
	}

	/**
	 * Test convert session cart to autoship when cart present.
	 */
	@Test
	public void testConvertSessionCartToAutoshipWhenCartPresent()
	{
		final CartData result = defaultAutoshipFacade.convertSessionCartToAutoshipCart();
		Assert.assertNotNull(result);
		verify(cartService, times(1)).hasSessionCart();
		verify(cartService, times(1)).getSessionCart();
	}

	/**
	 * Test convert session cart to autoship when no session cart.
	 */
	@Test
	public void testConvertSessionCartToAutoshipWhenNoSessionCart()
	{
		when(cartService.hasSessionCart()).thenReturn(false);
		final CartData result = defaultAutoshipFacade.convertSessionCartToAutoshipCart();
		Assert.assertNull(result);
		verify(cartService, times(1)).hasSessionCart();
		verify(cartService, times(0)).getSessionCart();
	}

	/**
	 * Test get active autoship cart for current user and site.
	 */
	@Test
	public void testGetActiveAutoshipCartForCurrentUserAndSite()
	{
		final List<CartData> result = defaultAutoshipFacade.getActiveAutoshipCartsForSessionUserAndSite();
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		verify(baseSiteService, times(1)).getCurrentBaseSite();
		verify(userService, times(1)).getCurrentUser();
		verify(cartConverter, times(1)).convertAll(Mockito.anyCollection());
	}

	/**
	 * Test get active autoship cart for current user and site for no carts.
	 */
	@Test
	public void testGetActiveAutoshipCartForCurrentUserAndSiteForNoCarts()
	{
		when(cartConverter.convertAll(Mockito.anyCollection())).thenReturn(new ArrayList());
		final List<CartData> result = defaultAutoshipFacade.getActiveAutoshipCartsForSessionUserAndSite();
		Assert.assertTrue(CollectionUtils.isEmpty(result));
		verify(baseSiteService, times(1)).getCurrentBaseSite();
		verify(userService, times(1)).getCurrentUser();
		verify(cartConverter, times(1)).convertAll(Mockito.anyCollection());
	}

	/**
	 * Test get autoship cart for current user and site.
	 */
	@Test
	public void testGetAutoshipCartForCurrentUserAndSite()
	{
		final List<CartData> result = defaultAutoshipFacade.getAutoshipCartsForSessionUserAndSite();
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		verify(baseSiteService, times(1)).getCurrentBaseSite();
		verify(userService, times(1)).getCurrentUser();
		verify(cartConverter, times(1)).convertAll(Mockito.anyCollection());
	}

	/**
	 * Test get autoship cart for current user and site for no carts.
	 */
	@Test
	public void testGetAutoshipCartForCurrentUserAndSiteForNoCarts()
	{
		when(cartConverter.convertAll(Mockito.anyCollection())).thenReturn(new ArrayList());
		final List<CartData> result = defaultAutoshipFacade.getAutoshipCartsForSessionUserAndSite();
		Assert.assertTrue(CollectionUtils.isEmpty(result));
		verify(baseSiteService, times(1)).getCurrentBaseSite();
		verify(userService, times(1)).getCurrentUser();
		verify(cartConverter, times(1)).convertAll(Mockito.anyCollection());
	}

	/**
	 * Test save autoship session cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testSaveAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException
	{
		when(cartService.getSessionCart()).thenReturn(new AutoshipCartModel());
		doNothing().when(autoshipService).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
		defaultAutoshipFacade.saveAutoshipSessionCart();
		verify(autoshipService, times(1)).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
	}

	/**
	 * Test save autoship session cart for non autoship cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSaveAutoshipSessionCartForNonAutoshipCart() throws CommerceCartModificationException, InvalidCartException
	{
		when(cartService.getSessionCart()).thenReturn(new CartModel());
		doNothing().when(autoshipService).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
		defaultAutoshipFacade.saveAutoshipSessionCart();
		verify(autoshipService, times(0)).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
	}

	/**
	 * Test activate autoship session cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testActivateAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException
	{
		when(cartService.getSessionCart()).thenReturn(new AutoshipCartModel());
		doNothing().when(autoshipService).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
		defaultAutoshipFacade.activateAutoshipSessionCart();
		verify(autoshipService, times(1)).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
	}

	/**
	 * Test activate autoship session cart for non autoship cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testActivateAutoshipSessionCartForNonAutoshipCart() throws CommerceCartModificationException, InvalidCartException
	{
		when(cartService.getSessionCart()).thenReturn(new CartModel());
		doNothing().when(autoshipService).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
		defaultAutoshipFacade.activateAutoshipSessionCart();
		verify(autoshipService, times(0)).setAutoshipStatus(any(AutoshipCartModel.class), any(AutoShipStatus.class));
	}
}
