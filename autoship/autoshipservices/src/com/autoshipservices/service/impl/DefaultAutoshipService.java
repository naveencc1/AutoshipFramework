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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.autoshipservices.dao.AutoshipDao;
import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.model.AutoshipConfigModel;
import com.autoshipservices.service.AutoshipService;
import com.autoshipservices.utils.AutoshipUtils;


/**
 * Autoship Service provides the Creation/Retrieval functionality for Autoship carts.
 *
 * @author naveec
 */
public class DefaultAutoshipService implements AutoshipService
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultAutoshipService.class);

	/** The model service. */
	private ModelService modelService;

	/** The autoship dao. */
	private AutoshipDao autoshipDao;

	/** The clone abstract order strategy. */
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	/** The key generator. */
	private KeyGenerator keyGenerator;

	/** The commerce cart service. */
	private CommerceCartService commerceCartService;

	/** The common I 18 N service. */
	private CommonI18NService commonI18NService;

	/** The base store service. */
	private BaseStoreService baseStoreService;

	/** The guid key generator. */
	private KeyGenerator guidKeyGenerator;

	private TypeService typeService;

	private NetGrossStrategy netGrossStrategy;

	/**
	 * Creates the autoship cart for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the autoship cart model
	 */
	@Override
	public AutoshipCartModel createAutoshipCartForUserAndSite(final CustomerModel customer, final BaseSiteModel baseSite)
	{
		//Checks for  Maximum allowed carts,if count is reached then throws exception. else Creates new Autoship cart.
		if (isAutoshipMaxAllowedCountReached(customer, baseSite))
		{
			throw new UnsupportedOperationException("Maximum Allowed Autoship carts are reached");
		}
		else
		{
			// Autoship carts will be created and Status is set as DRAFT until Autoship cart is saved.
			final AutoshipCartModel autoshipCart = getModelService().create(AutoshipCartModel.class);
			autoshipCart.setUser(customer);
			autoshipCart.setDate(new Date());
			autoshipCart.setCurrency(getCommonI18NService().getCurrentCurrency());
			autoshipCart.setNet(getNetGrossStrategy().isNet());
			autoshipCart.setSite(baseSite);
			autoshipCart.setStore(getBaseStoreService().getCurrentBaseStore());
			autoshipCart.setGuid(getGuidKeyGenerator().generate().toString());
			autoshipCart.setAutoShipStatus(AutoShipStatus.DRAFT);
			getModelService().save(autoshipCart);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Created Autoship cart with code : " + autoshipCart.getCode());
			}

			return autoshipCart;
		}

	}

	/**
	 * Checks if is autoship max allowed count reached.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return true, if is autoship max allowed count reached
	 */
	@Override
	public boolean isAutoshipMaxAllowedCountReached(final CustomerModel customer, final BaseSiteModel baseSite)
	{
		final AutoshipConfigModel autoshipConfig = baseSite.getAutoshipConfig();
		return null != autoshipConfig && autoshipConfig.getMaxAllowedCarts() != null
				&& autoshipConfig.getMaxAllowedCarts() <= getAutoshipDao().getAutoshipCartsCount(customer, baseSite,
						AutoshipUtils.getActiveAndSavedAutoshipStatus());
	}


	/**
	 * Convert cart to autoship cart.
	 *
	 * @param cart
	 *           the cart
	 * @return the autoship cart model
	 */
	@Override
	public AutoshipCartModel convertCartToAutoshipCart(final CartModel cart)
	{
		final AutoshipCartModel autoshipCart = getCloneAbstractOrderStrategy().clone(
				getTypeService().getComposedTypeForClass(AutoshipCartModel.class),
				getTypeService().getComposedTypeForClass(CartEntryModel.class), cart, getKeyGenerator().generate().toString(),
				AutoshipCartModel.class, CartEntryModel.class);
		autoshipCart.setAutoShipStatus(AutoShipStatus.DRAFT);
		getModelService().save(autoshipCart);
		getModelService().remove(cart);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Converted Regular cart to Autoship cart with code : " + autoshipCart.getCode());
		}

		return autoshipCart;
	}


	/**
	 * Convert order to autoship cart.
	 *
	 * @param order
	 *           the order
	 * @return the autoship cart model
	 */
	@Override
	public AutoshipCartModel convertOrderToAutoshipCart(final OrderModel order)
	{
		final AutoshipCartModel autoshipCart = getCloneAbstractOrderStrategy().clone(
				getTypeService().getComposedTypeForClass(AutoshipCartModel.class),
				getTypeService().getComposedTypeForClass(CartEntryModel.class), order, getKeyGenerator().generate().toString(),
				AutoshipCartModel.class, CartEntryModel.class);

		//setting tryonce flag as false in all entries.
		clearTryOnceReferenceInEntries(autoshipCart);
		autoshipCart.setAutoShipStatus(AutoShipStatus.SAVED);
		getModelService().save(autoshipCart);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Converted Order to Autoship cart with code : " + autoshipCart.getCode());
		}

		return autoshipCart;
	}

	/**
	 *
	 */
	private void clearTryOnceReferenceInEntries(final AutoshipCartModel autoshipCart)
	{
		autoshipCart.getEntries().stream().forEach(entry -> entry.setTryOnce(false));
	}


	/**
	 * Gets the active autoship carts for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the active autoship carts for user and site
	 */
	@Override
	public List<AutoshipCartModel> getActiveAutoshipCartsForUserAndSite(final CustomerModel customer, final BaseSiteModel baseSite)
	{
		return getAutoshipDao().getAutoshipCarts(customer, baseSite, AutoshipUtils.getActiveAutoshipStatus());
	}


	/**
	 * Gets the autoship carts for user and site.
	 *
	 * @param customer
	 *           the customer
	 * @param baseSite
	 *           the base site
	 * @return the autoship carts for user and site
	 */
	@Override
	public List<AutoshipCartModel> getAutoshipCartsForUserAndSite(final CustomerModel customer, final BaseSiteModel baseSite)
	{

		return getAutoshipDao().getAutoshipCarts(customer, baseSite, AutoshipUtils.getActiveAndSavedAutoshipStatus());
	}



	/**
	 * Sets the autoshipstatus.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @param status
	 *           the status
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public void setAutoshipStatus(final AutoshipCartModel autoshipCart, final AutoShipStatus status)
			throws CommerceCartModificationException, InvalidCartException
	{
		// Validate the cart for proper entries
		if (AutoShipStatus.ACTIVE.equals(status))
		{
			if (validateAutoshipCart(autoshipCart))
			{
				autoshipCart.setAutoShipStatus(status);
			}
			else
			{
				throw new InvalidCartException("Cart is not valid Autoship cart hence cannot make this cart ACTIVE");
			}
		}
		else
		{
			autoshipCart.setAutoShipStatus(status);
		}
		getModelService().save(autoshipCart);
	}

	/**
	 * Validate autoship cart.
	 *
	 * @param autoshipCart
	 *           the autoship cart
	 * @return true, if successful
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	protected boolean validateAutoshipCart(final AutoshipCartModel autoshipCart) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(autoshipCart);
		if (null != autoshipCart.getNextRunDate() && null != autoshipCart.getFrequency() && autoshipCart.getFrequency() > 0
				&& CollectionUtils.isEmpty(getCommerceCartService().validateCart(parameter)))
		{
			return true;
		}

		return false;
	}

	/**
	 * Gets the model service.
	 *
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Gets the autoship dao.
	 *
	 * @return the autoshipDao
	 */
	public AutoshipDao getAutoshipDao()
	{
		return autoshipDao;
	}


	/**
	 * Gets the clone abstract order strategy.
	 *
	 * @return the cloneAbstractOrderStrategy
	 */
	public CloneAbstractOrderStrategy getCloneAbstractOrderStrategy()
	{
		return cloneAbstractOrderStrategy;
	}


	/**
	 * Gets the key generator.
	 *
	 * @return the keyGenerator
	 */
	public KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}


	/**
	 * Gets the commerce cart service.
	 *
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @param autoshipDao
	 *           the autoshipDao to set
	 */
	public void setAutoshipDao(final AutoshipDao autoshipDao)
	{
		this.autoshipDao = autoshipDao;
	}


	/**
	 * @param cloneAbstractOrderStrategy
	 *           the cloneAbstractOrderStrategy to set
	 */
	public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
	{
		this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
	}


	/**
	 * @param keyGenerator
	 *           the keyGenerator to set
	 */
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}


	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the guidKeyGenerator
	 */
	public KeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	/**
	 * @param guidKeyGenerator
	 *           the guidKeyGenerator to set
	 */
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}

	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}
}
