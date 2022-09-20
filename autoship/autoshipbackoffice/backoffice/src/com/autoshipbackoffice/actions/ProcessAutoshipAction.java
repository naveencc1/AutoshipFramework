/**
 *
 */
package com.autoshipbackoffice.actions;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipCheckoutService;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


/**
 * The Class ProcessAutoshipAction process the Autoship cart.
 *
 * @author naveec
 */
public class ProcessAutoshipAction implements CockpitAction<Object, String>
{

	/** The Constant PROCESS_AUTOSHIP_CART. */
	private static final String PROCESS_AUTOSHIP_CART = "process.autoship.cart";

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ProcessAutoshipAction.class);

	/** The Constant AUTOSHIP_PROCESS_SUCCESS. */
	private static final String AUTOSHIP_PROCESS_SUCCESS = "autoshipcart.process.success.message";

	/** The Constant AUTOSHIP_PROCESS_FAILURE. */
	private static final String AUTOSHIP_PROCESS_FAILURE = "autoshipcart.process.failure.message";

	/** The autoship checkout service. */
	@Resource(name = "autoshipCheckoutService")
	private AutoshipCheckoutService autoshipCheckoutService;

	/** The impersonation service. */
	@Resource(name = "impersonationService")
	private ImpersonationService impersonationService;

	/** The notification service. */
	@Resource
	private NotificationService notificationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.cockpitng.actions.CockpitAction#perform(com.hybris.cockpitng.actions.ActionContext)
	 */
	@Override
	public ActionResult<String> perform(final ActionContext<Object> ctx)
	{
		final AutoshipCartModel autoshipCart = (AutoshipCartModel) ctx.getData();
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Processing Autoship cart : " + autoshipCart.getCode());
			}
			final OrderModel order = processAutoshipCart(autoshipCart.getSite(), autoshipCart);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Processed Autoship cart : " + autoshipCart.getCode() + ", Order Created with Id : " + order.getCode());
			}
			final String successMessage = ctx.getLabel(AUTOSHIP_PROCESS_SUCCESS, new String[]
			{ order.getCode() });
			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx),
					NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.SUCCESS, successMessage);
			return new ActionResult<String>(ActionResult.SUCCESS);
		}
		catch (final Exception ex)
		{
			LOG.error("Error occured while processing Autoship cart : " + autoshipCart.getCode(), ex);
			final String failureMessage = ctx.getLabel(AUTOSHIP_PROCESS_FAILURE, new String[]
			{ ex.getMessage() });
			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx),
					NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE, failureMessage);
			return new ActionResult<String>(ActionResult.ERROR);
		}

	}

	/**
	 * Process autoship cart.
	 *
	 * @param site
	 *           the site
	 * @param autoshipCart
	 *           the autoship cart
	 * @return the order model
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	private OrderModel processAutoshipCart(final BaseSiteModel site, final AutoshipCartModel autoshipCart)
			throws InvalidCartException
	{
		final ImpersonationContext ctx = new ImpersonationContext();
		ctx.setSite(site);
		ctx.setUser(autoshipCart.getUser());
		ctx.setLanguage(autoshipCart.getUser() != null ? autoshipCart.getUser().getSessionLanguage() : null);
		return getImpersonationService().executeInContext(ctx,
				() -> getAutoshipCheckoutService().processAutoshipCart(autoshipCart));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.cockpitng.actions.CockpitAction#canPerform(com.hybris.cockpitng.actions.ActionContext)
	 */
	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		final AutoshipCartModel autoshipCart = (AutoshipCartModel) ctx.getData();
		return null != autoshipCart && AutoShipStatus.ACTIVE.equals(autoshipCart.getAutoShipStatus());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.cockpitng.actions.CockpitAction#needsConfirmation(com.hybris.cockpitng.actions.ActionContext)
	 */
	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{

		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.cockpitng.actions.CockpitAction#getConfirmationMessage(com.hybris.cockpitng.actions.ActionContext)
	 */
	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{

		return ctx.getLabel(PROCESS_AUTOSHIP_CART);
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
	 * Gets the impersonation service.
	 *
	 * @return the impersonationService
	 */
	public ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	/**
	 * Sets the impersonation service.
	 *
	 * @param impersonationService
	 *           the impersonationService to set
	 */
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}

	/**
	 * Gets the notification service.
	 *
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * Sets the notification service.
	 *
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}



}
