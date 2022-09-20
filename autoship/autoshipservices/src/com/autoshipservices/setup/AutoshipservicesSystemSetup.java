/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.autoshipservices.setup;

import static com.autoshipservices.constants.AutoshipservicesConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.autoshipservices.constants.AutoshipservicesConstants;
import com.autoshipservices.service.AutoshipservicesService;


@SystemSetup(extension = AutoshipservicesConstants.EXTENSIONNAME)
public class AutoshipservicesSystemSetup
{
	private final AutoshipservicesService autoshipservicesService;

	public AutoshipservicesSystemSetup(final AutoshipservicesService autoshipservicesService)
	{
		this.autoshipservicesService = autoshipservicesService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		autoshipservicesService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return AutoshipservicesSystemSetup.class.getResourceAsStream("/autoshipservices/sap-hybris-platform.png");
	}
}
