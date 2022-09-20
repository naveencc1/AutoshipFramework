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
package com.autoshipservices.utils;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.autoshipservices.enums.AutoShipStatus;


/**
 * The Class AutoshipUtilsTest.
 */
@UnitTest
public class AutoshipUtilsTest
{
	@InjectMocks
	private final AutoshipUtils autoshipUtils = new AutoshipUtils();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test get active and saved autoship status.
	 */
	@Test
	public void testGetActiveAndSavedAutoshipStatus()
	{
		final Set<AutoShipStatus> result = AutoshipUtils.getActiveAndSavedAutoshipStatus();
		Assert.assertTrue(result.contains(AutoShipStatus.ACTIVE));
		Assert.assertTrue(result.contains(AutoShipStatus.SAVED));
		Assert.assertFalse(result.contains(AutoShipStatus.DRAFT));
	}

	/**
	 * Test get active autoship status.
	 */
	@Test
	public void testGetActiveAutoshipStatus()
	{
		final Set<AutoShipStatus> result = AutoshipUtils.getActiveAutoshipStatus();
		Assert.assertTrue(result.contains(AutoShipStatus.ACTIVE));
		Assert.assertFalse(result.contains(AutoShipStatus.SAVED));
		Assert.assertFalse(result.contains(AutoShipStatus.DRAFT));
	}
}
