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
package com.autoshipfacades.setup;

import static com.autoshipfacades.constants.AutoshipfacadesConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.autoshipfacades.constants.AutoshipfacadesConstants;
import com.autoshipfacades.service.AutoshipfacadesService;


@SystemSetup(extension = AutoshipfacadesConstants.EXTENSIONNAME)
public class AutoshipfacadesSystemSetup
{
	private final AutoshipfacadesService autoshipfacadesService;

	public AutoshipfacadesSystemSetup(final AutoshipfacadesService autoshipfacadesService)
	{
		this.autoshipfacadesService = autoshipfacadesService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		autoshipfacadesService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return AutoshipfacadesSystemSetup.class.getResourceAsStream("/autoshipfacades/sap-hybris-platform.png");
	}
}
