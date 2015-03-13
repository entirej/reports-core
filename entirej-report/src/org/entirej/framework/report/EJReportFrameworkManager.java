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

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.report.data.controllers.EJReportController;
import org.entirej.framework.report.data.controllers.EJReportControllerFactory;
import org.entirej.framework.report.data.controllers.EJReportTranslationController;
import org.entirej.framework.report.interfaces.EJReportRunner;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.factory.EJCoreReportPropertiesFactory;
import org.entirej.framework.report.properties.factory.EJCoreReportRuntimePropertiesFactory;
import org.entirej.framework.report.properties.factory.EJReportPropertiesFactory;
import org.entirej.framework.report.service.EJReportBlockServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportFrameworkManager implements EJReportFrameworkHelper
{
    private final Logger                                 LOGGER         = LoggerFactory.getLogger(this.getClass());

    private EJReportConnectionRetriever                  _connectionRetriever;
    private Locale                                       _currentLocale = Locale.ENGLISH;
    private EJReportPropertiesFactory                    _reportPropertiesFactory;
    private EJReportControllerFactory                    _reportControllerFactory;
    private EJReportTranslationController                _translationController;
    private EJReportBlockServiceFactory                  _blockServiceFactory;

    private HashMap<String, EJApplicationLevelParameter> _applicationLevelParameters;

    protected EJReportFrameworkManager(String entireJPropertiesFileName)
    {

        _applicationLevelParameters = new HashMap<String, EJApplicationLevelParameter>();
        _reportPropertiesFactory = createReportPropertiesFactory();
        _reportControllerFactory = createReportControllerFactory();
        _blockServiceFactory = createBlockServiceFactory();

        initialiseCore(entireJPropertiesFileName);
    }

    public synchronized EJManagedReportFrameworkConnection getConnection()
    {
        if (_connectionRetriever == null || _connectionRetriever.isClosed())
        {
            _connectionRetriever = new EJReportConnectionRetriever(this);
            return new EJManagedReportFrameworkConnection(_connectionRetriever, true);
        }
        else
        {
            return new EJManagedReportFrameworkConnection(_connectionRetriever, false);
        }

    }

    public Collection<EJApplicationLevelParameter> getApplicationLevelParameters()
    {
        return _applicationLevelParameters.values();
    }

    protected EJReportPropertiesFactory createReportPropertiesFactory()
    {
        return new EJCoreReportPropertiesFactory(this);
    }

    protected EJReportControllerFactory createReportControllerFactory()
    {
        return new EJReportControllerFactory(this);
    }

    protected EJReportBlockServiceFactory createBlockServiceFactory()
    {
        return new EJReportBlockServiceFactory();
    }

    public EJReportBlockServiceFactory getBlockServiceFactory()
    {
        return _blockServiceFactory;
    }

    /**
     * Called once from the Application Renderer when the core framework must be
     * initialized
     * <p>
     * The EntireJProperties file passed as a parameter will be read and the
     * properties added to {@link EJCoreReportRuntimeProperties}. EntireJ Core
     * Framework will automatically cache all reports, lookups and color
     * properties.
     * <p>
     * 
     * @param eintireJPropertiesFileName
     *            The fully qualified name of the
     *            <code>EntireJ.properties</code> file.
     * @throws EJReportRuntimeException
     *             If there was an error initializing the Framework
     */
    private void initialiseCore(String entireJPropertiesFileName)
    {
        if (entireJPropertiesFileName == null || entireJPropertiesFileName.trim().length() == 0)
        {
            throw new EJReportRuntimeException("The entireJProperitesFileName passed to FrameworkInitialise.initialiseCore is either null or of zero length");
        }

        EJCoreReportRuntimePropertiesFactory.initialiseEntireJProperties(this, entireJPropertiesFileName);

        _translationController = new EJReportTranslationController(this, EJCoreReportRuntimeProperties.getInstance().getApplicationTranslator(), _currentLocale);

        EJCoreReportRuntimeProperties.getInstance().copyRuntimeLevelParameters(this);

    }

    /**
     * Returns the framework translator
     * <p>
     * The translation controller can be used to translate texts and messages
     * used within the application. The applications implementation of the
     * translator is defined within the <code>EntireJFramework.properties</code>
     * 
     * @return The frameworks translator
     */
    public EJReportTranslationController getTranslationController()
    {
        return _translationController;
    }

    public EJReportRunner createReportRunner()
    {
        EJReportRunner newReportRunner = EJCoreReportRuntimeProperties.getInstance().newReportRunner();
        if (newReportRunner != null)
        {
            newReportRunner.init(this);
        }
        return newReportRunner;
    }

    /**
     * Returns the framework translator
     * <p>
     * The translation controller can be used to translate texts and messages
     * used within the application. The applications implementation of the
     * translator is defined within the <code>EntireJFramework.properties</code>
     * 
     * @return The frameworks translator
     */
    public EJReportTranslatorHelper getTranslatorHelper()
    {
        return new EJReportTranslatorHelper(this);
    }

    /**
     * Changes the {@link Locale} of the application
     * <p>
     * All texts and messages will be translated using the given {@link Locale}.
     * If no other {@link Locale} has been set, then the default of
     * <code>Locale.ENGLISH</code> will be used
     * 
     * @param locale
     *            The new {@link Locale} for the application
     */
    public void changeLocale(Locale locale)
    {
        if (locale != null)
        {
            _currentLocale = locale;
            _translationController.setLocale(_currentLocale);
        }
    }

    /**
     * Returns the {@link Locale} that is currently set for this application
     * <p>
     * The {@link Locale} is used internally within EntireJ and within the
     * applications translator. The application can change the {@link Locale} as
     * required
     * 
     * @return The {@link Locale} specified for this application
     */
    public Locale getCurrentLocale()
    {
        return _currentLocale;
    }

    public void handleMessage(EJReportMessage message)
    {

        LOGGER.error(message.getMessage());

    }

    public void handleException(Exception exception)
    {
        handleException(exception, true);
    }

    public void handleException(Exception exception, boolean displayUserMessage)
    {
        if (exception instanceof EJReportRuntimeException)
        {
            if (((EJReportRuntimeException) exception).stopProcessing())
            {
                // If the user has thrown an empty exception then nothing should
                // be done as they only want to stop processing
                return;
            }
        }

        LOGGER.error(exception.getMessage(), exception);
    }

    /**
     * Creates a <code>Report</code> with the given name
     * 
     * @param reportName
     *            The name of the report to create
     * 
     * @return The newly created report
     */
    public EJReport createReport(String reportName)
    {
        try
        {
            return createReport(reportName, null);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }

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
        return _reportControllerFactory.getReportProperties(reportName);
    }

    /**
     * Creates a <code>Report</code> with the given name
     * <p>
     * 
     * @param reportName
     *            The name of the report to create
     * @param parameterList
     *            A parameter list containing parameters for the report to open.
     * 
     * @return The newly created report
     */
    public EJReport createReport(String reportName, EJReportParameterList parameterList)
    {
        EJReportController controller = _reportControllerFactory.createController(reportName, parameterList);
        if (controller != null)
        {
            return controller.getEJReport();
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates a <code>EJInternalReport</code> with the given name
     * <p>
     * <b>This method should only used within the Applicaton Framework and not
     * within any of the action processors</b>
     * 
     * @param reportName
     *            The name of the report to create
     * @param parameterList
     *            A parameter list containing parameters for the report to open.
     * 
     * @return The newly created report
     */
    public EJInternalReport createInternalReport(String reportName, EJReportParameterList parameterList)
    {
        EJReportController controller = _reportControllerFactory.createController(reportName, parameterList);
        if (controller != null)
        {
            return controller.getInternalReport();
        }
        else
        {
            return null;
        }
    }

    /**
     * Retrieve the application instance of the
     * <code>ReportPropertiesFactory</code>
     * 
     * @return The application instance of the
     *         <code>ReportPropertiesFactory</code>
     */
    public EJReportPropertiesFactory getReportPropertiesFactory()
    {
        return _reportPropertiesFactory;
    }

    /**
     * Adds an Application Level Parameter to this application
     * 
     * @param parameter
     *            The parameter to add
     */
    public void addRuntimeLevelParameter(EJApplicationLevelParameter parameter)
    {
        if (parameter != null)
        {
            _applicationLevelParameters.put(parameter.getName(), parameter);
        }
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
        EJApplicationLevelParameter parameter = _applicationLevelParameters.get(valueName);

        if (parameter != null)
        {
            parameter.setValue(value);
        }
        else
        {
            throw new EJReportRuntimeException(new EJReportMessage("Trying to set an application level parameter with the name " + valueName
                    + ", but there is no parameter with this name. All parameters are defiined within the EntireJ.properties file"));
        }
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
        if (valueName == null)
        {
            throw new EJReportRuntimeException("Trying to retrieve an application level parameter without specifying a name");
        }

        if (_applicationLevelParameters.containsKey(valueName))
        {
            return _applicationLevelParameters.get(valueName);
        }
        else
        {
            throw new EJReportRuntimeException("Trying to get an application level parameter value with the name " + valueName
                    + ", but there is no parameter with this name. All parameters are defiined within the EntireJ.properties file");
        }
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
        return _applicationLevelParameters.containsKey(parameterName);
    }
}
