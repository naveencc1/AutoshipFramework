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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.autoshipservices.enums.AutoShipStatus;


/**
 * The Class AutoshipUtils.
 *
 * @author naveec
 */
public class AutoshipUtils
{

	/**
	 * Returns set of ACTIVE and SAVED Autoship Status.
	 *
	 * @return the active and saved autoship status
	 */
	public static Set<AutoShipStatus> getActiveAndSavedAutoshipStatus()
	{
		return new HashSet<>(Arrays.asList(AutoShipStatus.ACTIVE, AutoShipStatus.SAVED));
	}

	/**
	 * Returns set of ACTIVE Autoship Status.
	 *
	 * @return the active autoship status
	 */
	public static Set<AutoShipStatus> getActiveAutoshipStatus()
	{
		return new HashSet<>(Arrays.asList(AutoShipStatus.ACTIVE));
	}

	/**
	 * Adds the days to current date.
	 *
	 * @param days
	 *           the days
	 * @return the date
	 */
	public static Date addDaysToCurrentDate(final int days)
	{
		final LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime datePlusDays = currentDate.plusDays(days);
		return Date.from(datePlusDays.atZone(ZoneId.systemDefault()).toInstant());
	}
}
