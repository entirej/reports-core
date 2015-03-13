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
package org.entirej.framework.report.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportParameterList;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.report.data.controllers.EJReportActionController;
import org.entirej.framework.report.data.controllers.EJReportBlockController;
import org.entirej.framework.report.data.controllers.EJReportController;
import org.entirej.framework.report.data.controllers.EJReportDateHelper;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJInternalReport implements Serializable
{
    private EJReportController _reportController;

    public EJInternalReport(EJReportController reportController)
    {
        _reportController = reportController;
    }

    public EJReportController getReportController()
    {
        return _reportController;
    }

    /**
     * Returns a managed framework connection
     * 
     * @return The connection
     */
    public EJManagedReportFrameworkConnection getFrameworkConnection()
    {
        return getFrameworkManager().getConnection();
    }

    /**
     * This method should be called when to handle an exception in a
     * standardized manner
     * <p>
     * The exception will be sent to the applications messenger so that a
     * correct message will be displayed to the user. If the application has a
     * logger, then the exception will also be logged
     * 
     * @param exception
     *            The exception to handle
     */
    public void handleException(Exception exception)
    {
        _reportController.getFrameworkManager().handleException(exception);
    }

    /**
     * Returns the framework manager
     * 
     * @return The framework manager
     */
    public EJReportFrameworkManager getFrameworkManager()
    {
        return _reportController.getFrameworkManager();
    }

    /**
     * Returns the action controller for this report
     * 
     * @return
     */
    public EJReportActionController getActionController()
    {
        return _reportController.getActionController();
    }

    /**
     * Returns an immutable collection of all blocks available within this
     * report
     * 
     * @return All blocks within this report
     */
    public Collection<EJInternalReportBlock> getAllBlocks()
    {
        ArrayList<EJInternalReportBlock> blocks = new ArrayList<EJInternalReportBlock>();

        for (EJReportBlockController controller : _reportController.getAllBlockControllers())
        {
            blocks.add(controller.getBlock());
        }

        return Collections.unmodifiableCollection(blocks);
    }

    /**
     * Retrieves the required block
     * <p>
     * If there is no block with the given name then an exception will be thrown
     * 
     * @param blockName
     *            The name of the required block
     * @return The required block or <code>null</code> if no block exists with
     *         the given name.
     */
    public EJInternalReportBlock getBlock(String blockName)
    {
        EJReportBlockController blockController = _reportController.getBlockController(blockName);
        if (blockController == null)
        {
            return null;
        }

        return blockController.getBlock();
    }

    /**
     * Returns the <code>VisualAttributeProperties</code> with the given name or
     * <code>null</code> if there is no visual attribute with the given name
     * 
     * @param vaName
     *            the name of the required <code>VisualAttribute</code>
     * 
     * @return The required <code>VisualAttributeProperties</code> or
     *         <code>null</code> if there was no Visual Attribute with the given
     *         name
     */
    public EJReportVisualAttributeProperties getVisualAttribute(String vaName)
    {
        return EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(vaName);
    }

    /**
     * Adds the given <code>VisualAttributeProperties</code> to the frameworks
     * list of Visual Attributes
     * <p>
     * The given visual attribute will replace any visual attribute that
     * currently exists with the same name
     * 
     * @param vaProperties
     *            The visual attribute to add
     */
    public void addVisualAttribute(EJCoreReportVisualAttributeProperties vaProperties)
    {
        EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().replaceVisualAttribute(vaProperties);
    }

    /**
     * Used to set the value of an application level parameter
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The parameter value
     */
    public void setApplicationLevelParameter(String valueName, Object value)
    {
        _reportController.getFrameworkManager().setApplicationLevelParameter(valueName, value);
    }

    /**
     * Retrieves an application level parameter value with the given name
     * <p>
     * If there is no parameter with the given name then an exception will be
     * thrown
     * 
     * @param paramName
     *            The name of the required parameter
     * @return The value of the given parameter
     */
    public EJApplicationLevelParameter getApplicationLevelParameter(String paramName)
    {
        return _reportController.getFrameworkManager().getApplicationLevelParameter(paramName);
    }

    public Collection<EJApplicationLevelParameter> getApplicationLevelParameters()
    {
        return _reportController.getFrameworkManager().getApplicationLevelParameters();
    }

    /**
     * Sets the given report parameter to the given value
     * 
     * @param name
     *            The name of the parameter to set
     * @param value
     *            The value of the parameter
     * @throws EJReportRuntimeException
     *             id there is no property with the given name or the data type
     *             of the given object is not the same as defined within the
     *             report
     */
    public void setReportParameter(String name, Object value)
    {
        if (_reportController.getParameterList().contains(name))
        {
            _reportController.getParameterList().getParameter(name).setValue(value);
        }
        else
        {
            EJReportMessage message = EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.NO_REPORT_PARAMETER, name,
                    _reportController.getProperties().getName());
            throw new EJReportRuntimeException(message);
        }
    }

    /**
     * Returns the From Level Parameter with the given name
     * 
     * @param name
     *            The name of the required application parameter
     * @return The report parameter
     */
    public EJReportParameter getReportParameter(String name)
    {
        if (_reportController.getParameterList().contains(name))
        {
            return _reportController.getParameterList().getParameter(name);
        }
        else
        {
            EJReportMessage message = EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.NO_REPORT_PARAMETER, name,
                    _reportController.getProperties().getName());
            throw new EJReportRuntimeException(message);
        }
    }

    /**
     * Return the properties of this report
     * 
     * @return The report properties
     */
    public EJCoreReportProperties getProperties()
    {
        return _reportController.getProperties();
    }

    private boolean hasValue(String value)
    {
        if (value == null || value.trim().length() == 0)
        {
            return false;
        }

        return true;
    }

    /**
     * Translates a given text to the current application <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateText(String textKey)
    {
        EJManagedReportFrameworkConnection localConnection = getFrameworkManager().getConnection();
        try
        {
            return _reportController.getFrameworkManager().getTranslationController().translateText(textKey);
        }
        finally
        {
            localConnection.close();
        }
    }

    /**
     * Translates a given text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateText(String textKey, Locale locale)
    {
        EJManagedReportFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();

            if (locale == null)
            {
                return _reportController.getFrameworkManager().getTranslationController().translateText(textKey);
            }
            else
            {
                return _reportController.getFrameworkManager().getTranslationController().translateText(textKey, locale);
            }
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
    }

    /**
     * Translates a given message text to the current application
     * <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textKey)
    {
        EJManagedReportFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();
            return _reportController.getFrameworkManager().getTranslationController().translateMessageText(textKey);
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
    }

    /**
     * Translates a given message text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textKey, Locale locale)
    {
        EJManagedReportFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();

            if (locale == null)
            {
                return _reportController.getFrameworkManager().getTranslationController().translateMessageText(textKey);
            }
            else
            {
                return _reportController.getFrameworkManager().getTranslationController().translateMessageText(textKey, locale);
            }
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
    }

    /**
     * Returns this reports parameter list
     * <p>
     * the parameter list is a list of properties that were declared for the
     * report within the EntireJ Report Plugin. These parameters are used when
     * either calling another report or when another report calls this report.
     * They are used to pass values to and from the calling reports
     * 
     * @return This reports parameter list
     */
    public EJReportParameterList getParameterList()
    {
        return _reportController.getParameterList();
    }

    public EJReportDateHelper createDateHelper()
    {
        return _reportController.getFrameworkManager().getTranslationController().createDateHelper();
    }
}
