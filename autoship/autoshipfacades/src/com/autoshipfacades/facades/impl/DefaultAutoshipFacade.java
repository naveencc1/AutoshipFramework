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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.apache.log4j.Logger;

import com.autoshipfacades.facades.AutoshipFacade;
import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipService;


/**
 * Class to provide functionality related to Autoship cart.
 *
 * @author naveec
 */
public class DefaultAutoshipFacade implements AutoshipFacade
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipFacade.class);

	/** The base site service. */
	private BaseSiteService baseSiteService;

	/** The user service. */
	private UserService userService;

	/** The autoship service. */
	private AutoshipService autoshipService;

	/** The cart converter. */
	private Converter<CartModel, CartData> cartConverter;

	/** The cart service. */
	private CartService cartService;

	/** The customer account service. */
	private CustomerAccountService customerAccountService;

	/** The base store service. */
	private BaseStoreService baseStoreService;

	/**
	 * create autoship cart for current user and current site.
	 *
	 * @return CartData
	 */
	@Override
	public CartData createAutoshipCartForCurrentUserAndSite()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final AutoshipCartModel autoshipCart = getAutoshipService().createAutoshipCartForUserAndSite(customer, baseSite);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Autoship cart with code : " + autoshipCart.getCode() + ", is created for user " + customer.getUid()
					+ " and site " + baseSite.getUid());
		}

		return getCartConverter().convert(autoshipCart);

	}

	/**
	 * Convert session cart to autoship cart.
	 *
	 * @return the cart data
	 */
	@Override
	public CartData convertSessionCartToAutoshipCart()
	{
		if (getCartService().hasSessionCart())
		{
			return getCartConverter().convert(getAutoshipService().convertCartToAutoshipCart(getCartService().getSessionCart()));
		}
		return null;
	}

	/**
	 * Convert order to autoship cart.
	 *
	 * @param orderCode
	 *           the order code
	 * @return the cart data
	 */
	@Override
	public CartData convertOrderToAutoshipCart(final String orderCode)
	{
		final OrderModel order = getCustomerAccountService().getOrderForCode(orderCode,
				getBaseStoreService().getCurrentBaseStore());
		return getCartConverter().convert(getAutoshipService().convertOrderToAutoshipCart(order));
	}


	/**
	 * Gets the active autoship carts for session user and site.
	 *
	 * @return the active autoship carts for session user and site
	 */
	@Override
	public List<CartData> getActiveAutoshipCartsForSessionUserAndSite()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		return getCartConverter().convertAll(getAutoshipService().getActiveAutoshipCartsForUserAndSite(customer, baseSite));
	}


	/**
	 * Gets the autoship carts for session user and site.
	 *
	 * @return the autoship carts for session user and site
	 */
	@Override
	public List<CartData> getAutoshipCartsForSessionUserAndSite()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		return getCartConverter().convertAll(getAutoshipService().getAutoshipCartsForUserAndSite(customer, baseSite));
	}


	/**
	 * Save autoship session cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public void saveAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModel sessionCart = getCartService().getSessionCart();
		if (sessionCart instanceof AutoshipCartModel)
		{
			getAutoshipService().setAutoshipStatus((AutoshipCartModel) sessionCart, AutoShipStatus.SAVED);
		}
		else
		{
			LOG.error("Session cart with code : " + sessionCart.getCode() + ", is not Autoship cart");
			throw new UnsupportedOperationException("Session cart is not Autoship cart");
		}

	}

	/**
	 * Activate autoship session cart.
	 *
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public void activateAutoshipSessionCart() throws CommerceCartModificationException, InvalidCartException
	{
		final CartModel sessionCart = getCartService().getSessionCart();
		if (sessionCart instanceof AutoshipCartModel)
		{
			getAutoshipService().setAutoshipStatus((AutoshipCartModel) sessionCart, AutoShipStatus.ACTIVE);
		}
		else
		{
			LOG.error("Session cart with code : " + sessionCart.getCode() + ", is not Autoship cart");
			throw new UnsupportedOperationException("Session cart is not Autoship cart");
		}

	}

	/**
	 * Gets the base site service.
	 *
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * Gets the autoship service.
	 *
	 * @return the autoshipService
	 */
	public AutoshipService getAutoshipService()
	{
		return autoshipService;
	}

	/**
	 * Gets the cart converter.
	 *
	 * @return the cartConverter
	 */
	public Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	/**
	 * Gets the cart service.
	 *
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * Gets the customer account service.
	 *
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * Sets the customer account service.
	 *
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * Gets the base store service.
	 *
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * Sets the base store service.
	 *
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * Sets the base site service.
	 *
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Sets the autoship service.
	 *
	 * @param autoshipService
	 *           the autoshipService to set
	 */
	public void setAutoshipService(final AutoshipService autoshipService)
	{
		this.autoshipService = autoshipService;
	}

	/**
	 * Sets the cart converter.
	 *
	 * @param cartConverter
	 *           the cartConverter to set
	 */
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

	/**
	 * Sets the cart service.
	 *
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}
