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

    public EJReportController createController(String formName, EJReportParameterList parameterList)
    {
        if (formName == null)
        {
            throw new EJReportRuntimeException("The formName passed to createForm is null");
        }
        if (formName.trim().length() == 0)
        {
            throw new EJReportRuntimeException("Invalid form name passed to createForm. FormName: ''");
        }

        EJCoreReportProperties formProperties = getReportProperties(formName);

        return createController(formProperties, parameterList);
    }

    /**
     * Returns the form properties for the named form
     * 
     * @param formName
     *            - The form name
     * @return a {@link EJCoreReportProperties} object containing the translated
     *         from properties
     */
    public EJCoreReportProperties getReportProperties(String formName)
    {
        EJCoreReportProperties formProperties = null;

        formProperties = _frameworkManager.getFormPropertiesFactory().createReportProperties(formName);
        if (formProperties != null)
        {
            _frameworkManager.getTranslationController().translateReport(formProperties, _frameworkManager);
        }
        return formProperties;
    }

    /**
     * Creates a <code>FormController</code> with the given name and
     * <code>IMessenger</code>
     * 
     * @param formProperties
     *            The properties of the form to create
     * @param parameterList
     *            The list of properties for the form
     * @return The controller of the newly created form
     */
    public EJReportController createController(EJCoreReportProperties formProperties, EJReportParameterList parameterList)
    {
        if (formProperties == null)
        {
            EJReportMessage message = EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.NULL_FORM_CONTROLLER_PASSED_TO_FORM_PROPS);
            throw new EJReportRuntimeException(message);
        }

        EJReportData dataForm = new EJReportData(formProperties);

        // Create the controller and add the messenger to it

        EJReportController formController = new EJReportController(_frameworkManager, dataForm);

        // Now copy parameter values from the parameter list given to the
        // forms parameter list
        if (parameterList != null)
        {
            for (EJReportParameter parameter : parameterList.getAllParameters())
            {
                if (formController.getParameterList().contains(parameter.getName()))
                {
                    formController.getParameterList().getParameter(parameter.getName()).setValue(parameter.getValue());
                }
                else if (parameter.getValue() != null)
                {
                    EJReportParameter param = new EJReportParameter(parameter.getName(), parameter.getValue().getClass());
                    param.setValue(parameter.getValue());
                    formController.getParameterList().addParameter(param);
                }
            }
        }
        return formController;

    }
}
