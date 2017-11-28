/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties.factory;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.entirej.framework.report.EJReportConstants;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.reader.ReportHandler;

public class EJCoreReportPropertiesFactory implements EJReportPropertiesFactory
{
    private EJReportFrameworkManager _frameworkManager;

    public EJCoreReportPropertiesFactory(EJReportFrameworkManager frameworkManager)
    {
        _frameworkManager = frameworkManager;
    }

    /**
     * 
     * @param formName
     * @return
     */
    public EJCoreReportProperties createReportProperties(String formName)
    {
        if (formName == null)
        {
            throw new NullPointerException("The formName passed to createFormProperties is null");
        }

        try
        {
            InputStream inStream = getFormPropertiesDocument(formName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();

            EJCoreReportProperties reportProperties = new EJCoreReportProperties(formName, _frameworkManager);
            saxPArser.parse(inStream, new ReportHandler(reportProperties));
            return reportProperties;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    private InputStream getFormPropertiesDocument(String formName)
    {
        // Try to find the required form within one of the formProperties
        // directory from the EntireJProperties
        // If the form cannot be found within any of the directories, throw an
        // exception
        String directory;
        InputStream inStream = null;

        Iterator<String> formDirectories = EJCoreReportRuntimeProperties.getInstance().getReportPackageNames().iterator();
        while (formDirectories.hasNext())
        {
            directory = formDirectories.next();
            if (EJReportFileLoader.fileExists(directory + EJReportConstants.DIRECTORY_SEPERATOR + formName + EJReportConstants.report_PROPERTIES_FILE_SUFFIX))
            {

                inStream = EJReportFileLoader.loadFile(directory + EJReportConstants.DIRECTORY_SEPERATOR + formName
                        + EJReportConstants.report_PROPERTIES_FILE_SUFFIX);
                break;
            }
        }

        if (inStream == null)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.UNABLE_TO_LOAD_FORM_FILE, formName));
        }

        return inStream;
    }
}
