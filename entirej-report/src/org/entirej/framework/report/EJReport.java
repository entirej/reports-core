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
    private EJInternalReport _form;

    public EJReport(EJInternalReport form)
    {
        _form = form;
    }

    /**
     * Returns the properties for of this form
     * 
     * @return This forms properties
     */
    public EJReportProperties getProperties()
    {
        return _form.getProperties();
    }

    /**
     * Returns the action controller for this form
     * 
     * @return This forms {@link EJReportActionController}
     */
    public EJReportActionController getActionController()
    {
        return _form.getActionController();
    }

    /**
     * Returns the name of this form
     * 
     * @return The name of this form
     */
    public String getName()
    {
        return _form.getProperties().getName();
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
        _form.handleException(exception);
    }

    /**
     * Instructs EntireJ to clear the form
     * <p>
     * If <code>disregardChanges</code> is <code>true</code> then all changes
     * made within the form will be disregarded
     * 
     */
    public void clear()
    {
        _form.clear();
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
        _form.getFrameworkManager().changeLocale(locale);
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
        return _form.getFrameworkManager().getCurrentLocale();
    }

    /**
     * Retrieves an instance of the forms current connection
     * 
     * @return The form connection
     */
    public EJManagedReportFrameworkConnection getConnection()
    {
        return _form.getFrameworkManager().getConnection();
    }

    /**
     * Returns an immutable collection of all blocks available within this form
     * 
     * @return All blocks within this form
     */
    public Collection<EJReportBlock> getAllBlocks()
    {
        ArrayList<EJReportBlock> blocks = new ArrayList<EJReportBlock>();

        for (EJInternalReportBlock block : _form.getAllBlocks())
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
        EJInternalReportBlock block = _form.getBlock(blockName);
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
        return _form.getVisualAttribute(vaName);
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
        _form.addVisualAttribute(vaProperties);
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
        _form.setApplicationLevelParameter(valueName, value);
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
    public EJReportRuntimeLevelParameter getApplicationLevelParameter(String paramName)
    {
        return _form.getApplicationLevelParameter(paramName);
    }

    /**
     * Sets the given form parameter to the given value
     * 
     * @param name
     *            The name of the parameter to set
     * @param value
     *            The value of the parameter
     * @throws EJReportRuntimeException
     *             id there is no property with the given name or the data type
     *             of the given object is not the same as defined within the
     *             form
     */
    public void setFormParameter(String name, Object value)
    {
        _form.setFormParameter(name, value);
    }

    /**
     * Returns the From Level Parameter with the given name
     * 
     * @param name
     *            The name of the required application parameter
     * @return The form parameter
     * @throws EJReportRuntimeException
     *             if there is no parameter with the given value
     */
    public EJReportParameter getFormParameter(String name)
    {
        return _form.getFormParameter(name);
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
        return _form.translateText(textKey);
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
        return _form.translateText(textKey, locale);
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
        return _form.translateMessageText(textKey, null);
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
        return _form.translateMessageText(textKey, locale);
    }

    /**
     * Returns this forms parameter list
     * <p>
     * the parameter list is a list of properties that were declared for the
     * form within the EntireJ Form Plugin. These parameters are used when
     * either calling another form or when another form calls this form. They
     * are used to pass values to and from the calling forms
     * 
     * @return This forms parameter list
     */
    public EJReportParameterList getParameterList()
    {
        return _form.getParameterList();
    }

    public EJReportDateHelper createDateHelper()
    {
        return _form.createDateHelper();
    }

    @Override
    public EJReportTranslatorHelper getTranslatorHelper()
    {
        return _form.getFrameworkManager().getTranslatorHelper();
    }

}
