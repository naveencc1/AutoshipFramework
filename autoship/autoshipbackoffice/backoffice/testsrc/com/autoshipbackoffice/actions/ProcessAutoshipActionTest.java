/**
 *
 */
package com.autoshipbackoffice.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.enums.AutoShipStatus;
import com.autoshipservices.model.AutoshipCartModel;
import com.autoshipservices.service.AutoshipCheckoutService;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;


/**
 * The Class ProcessAutoshipActionTest.
 *
 * @author naveec
 */
@UnitTest
public class ProcessAutoshipActionTest
{

	/** The process autoship action. */
	@InjectMocks
	private final ProcessAutoshipAction processAutoshipAction = new ProcessAutoshipAction();

	/** The autoship checkout service. */
	@Mock
	private AutoshipCheckoutService autoshipCheckoutService;

	/** The ctx. */
	@Mock
	private ActionContext<Object> ctx;

	@Mock
	private ImpersonationService impersonationService;

	@Mock
	private NotificationService notificationService;

	/**
	 * Sets the up.
	 * 
	 * @throws Throwable
	 */
	@Before
	public void setUp() throws Throwable
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(impersonationService.executeInContext(Mockito.any(), Mockito.any())).thenReturn(new OrderModel());
		Mockito.doNothing().when(notificationService).notifyUser(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any());
	}

	/**
	 * Test can perform for active autoship cart.
	 */
	@Test
	public void testCanPerformForActiveAutoshipCart()
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.ACTIVE);
		Mockito.when(ctx.getData()).thenReturn(autoshipCart);
		final boolean result = processAutoshipAction.canPerform(ctx);
		Assert.assertTrue(result);
	}

	/**
	 * Test can perform for in active autoship cart.
	 */
	@Test
	public void testCanPerformForInActiveAutoshipCart()
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.SAVED);
		Mockito.when(ctx.getData()).thenReturn(autoshipCart);
		final boolean result = processAutoshipAction.canPerform(ctx);
		Assert.assertFalse(result);
	}

	/**
	 * Test perform method.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testPerform() throws InvalidCartException
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.ACTIVE);
		Mockito.when(ctx.getData()).thenReturn(autoshipCart);
		Mockito.when(autoshipCheckoutService.processAutoshipCart(Mockito.any())).thenReturn(new OrderModel());
		final ActionResult<String> result = processAutoshipAction.perform(ctx);
		Assert.assertEquals(ActionResult.SUCCESS, result.getResultCode());
	}

	/**
	 * Test perform method with exception.
	 *
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Test
	public void testPerformWithException() throws Throwable
	{
		final AutoshipCartModel autoshipCart = new AutoshipCartModel();
		autoshipCart.setAutoShipStatus(AutoShipStatus.ACTIVE);
		Mockito.when(ctx.getData()).thenReturn(autoshipCart);
		Mockito.doThrow(InvalidCartException.class).when(impersonationService).executeInContext(Mockito.any(), Mockito.any());
		final ActionResult<String> result = processAutoshipAction.perform(ctx);
		Assert.assertEquals(ActionResult.ERROR, result.getResultCode());
	}
}
