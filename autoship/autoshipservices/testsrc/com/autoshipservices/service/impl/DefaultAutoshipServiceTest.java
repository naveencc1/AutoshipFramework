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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.dao.AutoshipDao;
import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.model.AutoshipConfigModel;


/**
 * The Class DefaultAutoshipServiceTest.
 */
public class DefaultAutoshipServiceTest
{
	@InjectMocks
	private final DefaultAutoshipService defaultAutoshipService = new DefaultAutoshipService();

	@Mock
	private ModelService modelService;

	@Mock
	private AutoshipDao autoshipDao;

	@Mock
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	@Mock
	private KeyGenerator keyGenerator;

	@Mock
	private CommerceCartService commerceCartService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private KeyGenerator guidKeyGenerator;

	@Mock
	private TypeService typeService;

	@Mock
	private NetGrossStrategy netGrossStrategy;

	private CustomerModel customer;

	private BaseSiteModel baseSite;

	private AutoshipConfigModel autoshipConfig;

	private AutoshipCartModel autoshipCart;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		customer = new CustomerModel();
		baseSite = new BaseSiteModel();
		autoshipConfig = new AutoshipConfigModel();
		autoshipCart = new AutoshipCartModel();

		autoshipConfig.setMaxAllowedCarts(2);
		baseSite.setAutoshipConfig(autoshipConfig);

		when(modelService.create(any(Class.class))).thenReturn(new AutoshipCartModel());
		doNothing().when(modelService).save(any(AutoshipCartModel.class));
		doNothing().when(modelService).remove(any(CartModel.class));

		when(autoshipDao.getAutoshipCarts(any(CustomerModel.class), any(BaseSiteModel.class), Mockito.anySet()))
				.thenReturn(Arrays.asList(new AutoshipCartModel()));

		when(cloneAbstractOrderStrategy.clone(any(ComposedTypeModel.class), any(ComposedTypeModel.class),
				any(AbstractOrderModel.class), Mockito.anyString(), any(Class.class), any(Class.class))).thenReturn(autoshipCart);
		when(keyGenerator.generate()).thenReturn("key");
		when(typeService.getComposedTypeForClass(Mockito.any())).thenReturn(new ComposedTypeModel());
		when(netGrossStrategy.isNet()).thenReturn(true);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(new BaseStoreModel());
		when(guidKeyGenerator.generate()).thenReturn("1234567890");
		when(commonI18NService.getCurrentCurrency()).thenReturn(new CurrencyModel());
	}

	/**
	 * Test create autoship cart for max qty not reached.
	 */
	@Test
	public void testCreateAutoshipCartForMaxQtyNotReached()
	{
		when(autoshipDao.getAutoshipCartsCount(any(CustomerModel.class), any(BaseSiteModel.class), Mockito.anySet())).thenReturn(1);
		final AutoshipCartModel result = defaultAutoshipService.createAutoshipCartForUserAndSite(customer, baseSite);
		Assert.assertEquals(AutoShipStatus.DRAFT, result.getAutoShipStatus());
		verify(modelService, times(1)).save(any(AutoshipCartModel.class));
		verify(modelService, times(1)).create(any(Class.class));
		verify(autoshipDao, times(1)).getAutoshipCartsCount(any(CustomerModel.class), any(BaseSiteModel.class), Mockito.anySet());
	}

	/**
	 * Test create autoship cart for max qty reached.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testCreateAutoshipCartForMaxQtyReached()
	{
		when(autoshipDao.getAutoshipCartsCount(any(CustomerModel.class), any(BaseSiteModel.class), Mockito.anySet())).thenReturn(3);
		final AutoshipCartModel result = defaultAutoshipService.createAutoshipCartForUserAndSite(customer, baseSite);
		Assert.assertNull(result);
		verify(modelService, times(0)).save(any(AutoshipCartModel.class));
		verify(modelService, times(0)).create(any(Class.class));
		verify(autoshipDao, times(1)).getAutoshipCartsCount(any(CustomerModel.class), any(BaseSiteModel.class), Mockito.anySet());
	}

	/**
	 * Test convert cart to autoship cart.
	 */
	@Test
	public void testConvertCartToAutoshipCart()
	{
		final AutoshipCartModel result = defaultAutoshipService.convertCartToAutoshipCart(new CartModel());
		Assert.assertEquals(result.getAutoShipStatus(), AutoShipStatus.DRAFT);
	}

	/**
	 * Test convert order to autoship cart.
	 */
	@Test
	public void testConvertOrderToAutoshipCart()
	{
		final AutoshipCartModel result = defaultAutoshipService.convertOrderToAutoshipCart(new OrderModel());
		Assert.assertEquals(result.getAutoShipStatus(), AutoShipStatus.SAVED);
	}

	/**
	 * Test get active autoship carts for user and site.
	 */
	@Test
	public void testGetActiveAutoshipCartsForUserAndSite()
	{
		final List<AutoshipCartModel> result = defaultAutoshipService.getActiveAutoshipCartsForUserAndSite(new CustomerModel(),
				new BaseSiteModel());
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
	}

	/**
	 * Test get autoship carts for user and site.
	 */
	@Test
	public void testGetAutoshipCartsForUserAndSite()
	{
		final List<AutoshipCartModel> result = defaultAutoshipService.getAutoshipCartsForUserAndSite(new CustomerModel(),
				new BaseSiteModel());
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
	}

	/**
	 * Test set autoship status for saved.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testSetAutoshipStatusForSaved() throws CommerceCartModificationException, InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.SAVED);
		Assert.assertEquals(AutoShipStatus.SAVED, autoshipCart.getAutoShipStatus());
	}

	/**
	 * Test set active autoship status for no next run date.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testSetActiveAutoshipStatusForNoNextRunDate() throws CommerceCartModificationException, InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
	}

	/**
	 * Test set active autoship status for invalid next run date.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testSetActiveAutoshipStatusForInvalidNextRunDate() throws CommerceCartModificationException, InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setNextRunDate(new Date());
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
	}

	/**
	 * Test set active autoship status for no frequency.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 * @throws ParseException
	 *            the parse exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testSetActiveAutoshipStatusForNoFrequency()
			throws CommerceCartModificationException, InvalidCartException, ParseException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setNextRunDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-01"));
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
	}

	/**
	 * Test set active autoship status for invalid frequency.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 * @throws ParseException
	 *            the parse exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testSetActiveAutoshipStatusForInvalidFrequency()
			throws CommerceCartModificationException, InvalidCartException, ParseException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setNextRunDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-01"));
		autoshipCart.setFrequency(0);
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
	}

	/**
	 * Test set active autoship status for invalid cart entries.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 * @throws ParseException
	 *            the parse exception
	 */
	@Test(expected = InvalidCartException.class)
	public void testSetActiveAutoshipStatusForInvalidCartEntries()
			throws CommerceCartModificationException, InvalidCartException, ParseException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setNextRunDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-01"));
		autoshipCart.setFrequency(10);
		final List<CommerceCartModification> modifications = new ArrayList<>();
		modifications.add(new CommerceCartModification());
		when(commerceCartService.validateCart(any(CommerceCartParameter.class))).thenReturn(modifications);
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
	}

	/**
	 * Test set active autoship status for valid cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 * @throws ParseException
	 *            the parse exception
	 */
	@Test
	public void testSetActiveAutoshipStatusForValidCart()
			throws CommerceCartModificationException, InvalidCartException, ParseException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setNextRunDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-01"));
		autoshipCart.setFrequency(10);
		when(commerceCartService.validateCart(any(CommerceCartParameter.class)))
				.thenReturn(new ArrayList<CommerceCartModification>());
		defaultAutoshipService.setAutoshipStatus(autoshipCart, AutoShipStatus.ACTIVE);
		Assert.assertEquals(AutoShipStatus.ACTIVE, autoshipCart.getAutoShipStatus());
	}

}
