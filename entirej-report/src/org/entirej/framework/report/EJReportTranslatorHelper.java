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
package org.entirej.framework.report;

import java.io.Serializable;
import java.util.Locale;

import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;

public class EJReportTranslatorHelper implements Serializable
{
    private EJReportFrameworkManager _fwkManager;

    public EJReportTranslatorHelper(EJReportFrameworkManager manager)
    {
        _fwkManager = manager;
    }

    public EJReportManagedFrameworkConnection getConnection()
    {
        return _fwkManager.getConnection();
    }

    /**
     * Returns the {@link Locale} that is currently set for this application
     * 
     * @return The {@link Locale} specified for this application
     */
    public Locale getCurrentLocale()
    {
        return _fwkManager.getCurrentLocale();
    }

    /**
     * Adds an Application Level Parameter to this application
     * 
     * @param parameter
     *            The parameter to add
     */
    public void addRuntimeLevelParameter(EJApplicationLevelParameter parameter)
    {
        _fwkManager.addRuntimeLevelParameter(parameter);
    }

    /**
     * Used to set an application level parameter
     * <p>
     * A application level parameter has been defined within the EntireJ
     * Properties
     * 
     * value is not used by the EntireJFramework but allows the user to store
     * values within the report which can be retrieved when needed
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The value
     */
    public void setApplicationLevelParameter(String valueName, Object value)
    {
        _fwkManager.setApplicationLevelParameter(valueName, value);
    }

    /**
     * Retrieves a global value with the given name
     * <p>
     * If there is no parameter with the given name then an exception will be
     * thrown
     * 
     * @param valueName
     *            The name of the required global value
     * @return The application level parameter
     * @throws EJReportRuntimeException
     *             If there is no application level parameter with the given
     *             name
     */
    public EJApplicationLevelParameter getApplicationLevelParameter(String valueName)
    {
        return _fwkManager.getApplicationLevelParameter(valueName);
    }

    /**
     * Checks to see if there is a parameter with the specified name
     * <p>
     * If there is a parameter with the given name, then this method will return
     * <code>true</code>, otherwise <code>false</code>
     * 
     * @param parameterName
     *            The parameter name to check for
     * @return <code>true</code> if there is a parameter with the specified
     *         name, otherwise <code>false</code>
     */
    public boolean applicationLevelParameterExists(String parameterName)
    {
        return _fwkManager.applicationLevelParameterExists(parameterName);
    }

}
