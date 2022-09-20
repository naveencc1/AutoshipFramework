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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.autoshipservices.dao.AutoshipDao;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.model.AutoshipProcessCronJobModel;
import com.autoshipservices.service.AutoshipCheckoutService;
import com.autoshipservices.utils.AutoshipUtils;


/**
 * The Class ProcessAutoshipCartCronjob.
 */
public class ProcessAutoshipCartCronjob extends AbstractJobPerformable<AutoshipProcessCronJobModel>
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ProcessAutoshipCartCronjob.class);

	/** The autoship dao. */
	private AutoshipDao autoshipDao;

	/** The time service. */
	private TimeService timeService;

	/** The autoship checkout service. */
	private AutoshipCheckoutService autoshipCheckoutService;

	/** The impersonation service. */
	private ImpersonationService impersonationService;

	/**
	 * Perform.
	 *
	 * @param job
	 *           the job
	 * @return the perform result
	 */
	@Override
	public PerformResult perform(final AutoshipProcessCronJobModel job)
	{
		try
		{
			for (final BaseSiteModel site : job.getSites())
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Processing Autoship carts for site : " + site.getUid());
				}
				//Gets all the Autoship carts to process for BaseSite.
				final List<AutoshipCartModel> autoshipCarts = getAutoshipDao().getAutoshipCartsToProcess(site,
						AutoshipUtils.getActiveAutoshipStatus(), new DateTime(getTimeService().getCurrentTime()).toDate());

				for (final AutoshipCartModel autoshipCart : autoshipCarts)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Processing Autoship cart " + autoshipCart.getCode());
					}
					processAutoshipCart(site, autoshipCart);

				}
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception ex)
		{
			LOG.error("Error while processing Autoship carts");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

	}

	/**
	 * Process autoship cart.
	 *
	 * @param site
	 *           the site
	 * @param autoshipCart
	 *           the autoship cart
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	private void processAutoshipCart(final BaseSiteModel site, final AutoshipCartModel autoshipCart) throws InvalidCartException
	{
		final ImpersonationContext ctx = new ImpersonationContext();
		ctx.setSite(site);
		ctx.setUser(autoshipCart.getUser());
		ctx.setLanguage(autoshipCart.getUser() != null ? autoshipCart.getUser().getSessionLanguage() : null);
		getImpersonationService().executeInContext(ctx, () -> getAutoshipCheckoutService().processAutoshipCart(autoshipCart));
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
	 * Sets the autoship dao.
	 *
	 * @param autoshipDao
	 *           the autoshipDao to set
	 */
	public void setAutoshipDao(final AutoshipDao autoshipDao)
	{
		this.autoshipDao = autoshipDao;
	}

	/**
	 * Gets the time service.
	 *
	 * @return the timeService
	 */
	public TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * Sets the time service.
	 *
	 * @param timeService
	 *           the timeService to set
	 */
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	/**
	 * Gets the autoship checkout service.
	 *
	 * @return the autoshipCheckoutService
	 */
	public AutoshipCheckoutService getAutoshipCheckoutService()
	{
		return autoshipCheckoutService;
	}

	/**
	 * Sets the autoship checkout service.
	 *
	 * @param autoshipCheckoutService
	 *           the autoshipCheckoutService to set
	 */
	public void setAutoshipCheckoutService(final AutoshipCheckoutService autoshipCheckoutService)
	{
		this.autoshipCheckoutService = autoshipCheckoutService;
	}

	/**
	 * @return the impersonationService
	 */
	public ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	/**
	 * @param impersonationService
	 *           the impersonationService to set
	 */
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}


}
