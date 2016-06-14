/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.data.controllers;

import java.io.Serializable;

import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportParameterList;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportData;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.properties.EJCoreReportProperties;

public class EJReportControllerFactory implements Serializable
{
    private EJReportFrameworkManager _frameworkManager;

    public EJReportControllerFactory(EJReportFrameworkManager frameworkManager)
    {
        _frameworkManager = frameworkManager;
    }

    public EJReportController createController(String reportName, EJReportParameterList parameterList)
    {
        if (reportName == null)
        {
            throw new EJReportRuntimeException("The reportName passed to createReport is null");
        }
        if (reportName.trim().length() == 0)
        {
            throw new EJReportRuntimeException("Invalid report name passed to createReport. ReportName: ''");
        }

        EJCoreReportProperties reportProperties = getReportProperties(reportName);

        return createController(reportProperties, parameterList);
    }

    /**
     * Returns the report properties for the named report
     * 
     * @param reportName
     *            - The report name
     * @return a {@link EJCoreReportProperties} object containing the translated
     *         from properties
     */
    public EJCoreReportProperties getReportProperties(String reportName)
    {
        EJCoreReportProperties reportProperties = null;

        reportProperties = _frameworkManager.getReportPropertiesFactory().createReportProperties(reportName);
        if (reportProperties != null)
        {
            _frameworkManager.getTranslationController().translateReport(reportProperties, _frameworkManager);
        }
        return reportProperties;
    }

    /**
     * Creates a <code>ReportController</code> with the given name and
     * <code>IMessenger</code>
     * 
     * @param reportProperties
     *            The properties of the report to create
     * @param parameterList
     *            The list of properties for the report
     * @return The controller of the newly created report
     */
    public EJReportController createController(EJCoreReportProperties reportProperties, EJReportParameterList parameterList)
    {
        if (reportProperties == null)
        {
            EJReportMessage message = EJReportMessageFactory.getInstance()
                    .createMessage(EJReportFrameworkMessage.NULL_REPORT_CONTROLLER_PASSED_TO_REPORT_PROPS);
            throw new EJReportRuntimeException(message);
        }

        EJReportData reportData = new EJReportData(reportProperties);

        // Create the controller and add the messenger to it

        EJReportController reportController = new EJReportController(_frameworkManager, reportData);

        // Now copy parameter values from the parameter list given to the
        // reports parameter list
        if (parameterList != null)
        {
            for (EJReportParameter parameter : parameterList.getAllParameters())
            {
                if (reportController.getParameterList().contains(parameter.getName()))
                {
                    reportController.getParameterList().getParameter(parameter.getName()).setValue(parameter.getValue());
                }
                else if (parameter.getValue() != null)
                {
                    EJReportParameter param = new EJReportParameter(parameter.getName(), parameter.getValue().getClass());
                    param.setValue(parameter.getValue());
                    reportController.getParameterList().addParameter(param);
                }
            }
        }
        return reportController;

    }
}
