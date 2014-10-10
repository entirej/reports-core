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
import java.util.Locale;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportTranslatorHelper;
import org.entirej.framework.report.interfaces.EJReportTranslator;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;

/**
 * The TranslationController is responsible for translating all application
 * texts by using the framework assigned Translator
 */
public class EJReportTranslationController implements Serializable
{
    private EJReportFrameworkManager _frameworkManager;
    private EJReportTranslator       _appTranslator;
    private Locale                   _currentLocale;

    public EJReportTranslationController(EJReportFrameworkManager frameworkManager, EJReportTranslator appTranslator, Locale locale)
    {
        _frameworkManager = frameworkManager;
        _appTranslator = appTranslator;
        setLocale(locale);
    }

    public void setLocale(Locale locale)
    {
        if (locale == null)
        {
            throw new NullPointerException("The Locale passed to the TranslationController is null");
        }

        _currentLocale = locale;
    }

    /**
     * Translates a given text to the current application <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     * 
     * @see #translateText(String, Locale)
     */
    public String translateText(String textToTranslate)
    {
        return translateText(textToTranslate, _currentLocale);
    }

    /**
     * Translates a given text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     * 
     * @see #translateText(String)
     */
    public String translateText(String textToTranslate, Locale locale)
    {
        if (textToTranslate == null)
        {
            return null;
        }

        EJManagedReportFrameworkConnection connection = _frameworkManager.getConnection();
        try
        {
            if (_appTranslator != null)
            {
                String translatedText = _appTranslator.translateText(new EJReportTranslatorHelper(_frameworkManager), textToTranslate);
                if (translatedText == null || translatedText.trim().length() == 0)
                {
                    return textToTranslate;
                }
                else
                {
                    return translatedText;
                }
            }
            else
            {
                return textToTranslate;
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
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
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textToTranslate)
    {
        return translateMessageText(textToTranslate, _currentLocale);
    }

    /**
     * Translates a given message text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @param locale
     *            The current <code>{@link Locale}</code> to use for the
     *            translation
     * @return The translated text for the given key
     */
    public String translateMessageText(String textToTranslate, Locale locale)
    {
        if (textToTranslate == null)
        {
            return null;
        }

        EJManagedReportFrameworkConnection connection = _frameworkManager.getConnection();
        try
        {
            if (_appTranslator != null)
            {
                String translatedText = _appTranslator.translateMessageText(new EJReportTranslatorHelper(_frameworkManager), textToTranslate);
                if (translatedText == null || translatedText.trim().length() == 0)
                {
                    return textToTranslate;
                }
                else
                {
                    return translatedText;
                }
            }
            else
            {
                return textToTranslate;
            }

        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }

    public EJReportDateHelper createDateHelper()
    {
        return new EJReportDateHelper(_currentLocale == null ? Locale.ENGLISH : _currentLocale);
    }

    public void translateReport(EJCoreReportProperties reportToTranslate, EJReportFrameworkManager fwManager)
    {
        EJManagedReportFrameworkConnection connection = fwManager.getConnection();

        try
        {
            // Translate the form properties
            // formToTranslate.setTranslatedTitle(translateText(formToTranslate.getTitle()));

            // Translate the forms blocks
            for (EJCoreReportBlockProperties blockProps : reportToTranslate.getBlockContainer().getAllBlockProperties())
            {
                translateBlockProperties(blockProps);
            }

        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }

    }

    private void translateBlockProperties(EJCoreReportBlockProperties blockProperties)
    {

        for (EJCoreReportItemProperties itemProperties : blockProperties.getItemContainer().getAllItemProperties())
        {
            translateBlockItemProperties(itemProperties);
        }

    }

    private void translateBlockItemProperties(EJCoreReportItemProperties itemProperties)
    {

    }
}
