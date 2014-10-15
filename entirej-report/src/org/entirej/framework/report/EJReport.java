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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.entirej.framework.report.data.controllers.EJReportActionController;
import org.entirej.framework.report.data.controllers.EJReportDateHelper;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.data.controllers.EJReportRuntimeLevelParameter;
import org.entirej.framework.report.interfaces.EJReportProperties;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReport implements EJReportFrameworkHelper
{
    private EJInternalReport _report;

    public EJReport(EJInternalReport report)
    {
        _report = report;
    }

    /**
     * Returns the properties for of this report
     * 
     * @return This reports properties
     */
    public EJReportProperties getProperties()
    {
        return _report.getProperties();
    }

    /**
     * Returns the action controller for this report
     * 
     * @return This reports {@link EJReportActionController}
     */
    public EJReportActionController getActionController()
    {
        return _report.getActionController();
    }

    /**
     * Returns the name of this report
     * 
     * @return The name of this report
     */
    public String getName()
    {
        return _report.getProperties().getName();
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
        _report.handleException(exception);
    }

    /**
     * Instructs EntireJ to clear the report
     * <p>
     * If <code>disregardChanges</code> is <code>true</code> then all changes
     * made within the report will be disregarded
     * 
     */
    public void clear()
    {
        _report.clear();
    }

    /**
     * Used to set the current locale of the application
     * <p>
     * EntireJ stores a locale that is used by various item renderers for
     * example the NumberItemRenderer. It is used for the formatting of the
     * number etc. The default for the locale is {@link Locale.ENGLISH} but can
     * be changed via this method
     * 
     * @param locale
     *            The locale to use for this application
     */
    public void changeLocale(Locale locale)
    {
        _report.getFrameworkManager().changeLocale(locale);
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
        return _report.getFrameworkManager().getCurrentLocale();
    }

    /**
     * Retrieves an instance of the reports current connection
     * 
     * @return The report connection
     */
    public EJManagedReportFrameworkConnection getConnection()
    {
        return _report.getFrameworkManager().getConnection();
    }

    /**
     * Returns an immutable collection of all blocks available within this report
     * 
     * @return All blocks within this report
     */
    public Collection<EJReportBlock> getAllBlocks()
    {
        ArrayList<EJReportBlock> blocks = new ArrayList<EJReportBlock>();

        for (EJInternalReportBlock block : _report.getAllBlocks())
        {
            blocks.add(new EJReportBlock(block));
        }

        return blocks;
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
    public EJReportBlock getBlock(String blockName)
    {
        EJInternalReportBlock block = _report.getBlock(blockName);
        if (block == null)
        {
            return null;
        }
        else
        {
            return new EJReportBlock(block);
        }

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
        return _report.getVisualAttribute(vaName);
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
    public void addVisualAttribute(EJReportVisualAttributeProperties vaProperties)
    {
        _report.addVisualAttribute(vaProperties);
    }

    /**
     * Used to set the value of an application level parameter
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The parameter value
     */
    public void setRuntimeLevelParameter(String valueName, Object value)
    {
        _report.setRuntimeLevelParameter(valueName, value);
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
    public EJReportRuntimeLevelParameter getRuntimeLevelParameter(String paramName)
    {
        return _report.getRuntimeLevelParameter(paramName);
    }

    
    @Override
    public Collection<EJReportRuntimeLevelParameter> getRuntimeLevelParameters()
    {
        return _report.getRuntimeLevelParameters();
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
        _report.setReportParameter(name, value);
    }

    /**
     * Returns the From Level Parameter with the given name
     * 
     * @param name
     *            The name of the required application parameter
     * @return The report parameter
     * @throws EJReportRuntimeException
     *             if there is no parameter with the given value
     */
    public EJReportParameter getReportParameter(String name)
    {
        return _report.getReportParameter(name);
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
        return _report.translateText(textKey);
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
        return _report.translateText(textKey, locale);
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
        return _report.translateMessageText(textKey, null);
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
        return _report.translateMessageText(textKey, locale);
    }

    /**
     * Returns this reports parameter list
     * <p>
     * the parameter list is a list of properties that were declared for the
     * report within the EntireJ Report Plugin. These parameters are used when
     * either calling another report or when another report calls this report. They
     * are used to pass values to and from the calling reports
     * 
     * @return This reports parameter list
     */
    public EJReportParameterList getParameterList()
    {
        return _report.getParameterList();
    }

    public EJReportDateHelper createDateHelper()
    {
        return _report.createDateHelper();
    }

    @Override
    public EJReportTranslatorHelper getTranslatorHelper()
    {
        return _report.getFrameworkManager().getTranslatorHelper();
    }

}
