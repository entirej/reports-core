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
package org.entirej.framework.report.properties.factory;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.reader.EntireJReportPropertiesHandler;

public class EJCoreReportRuntimePropertiesFactory
{
    /**
     * Initializes the EntireJ Properties using the given document
     * 
     * @param manager
     *            The framework manager
     * @param entireJPropertiesFileName
     *            The name of the properties file containing the EntireJ
     *            properties
     * @return An <code>EntireJProperties</code> object containing the
     *         properties read from the given document
     * @throws EJReportRuntimeException
     *             When there is a problem reading any of the EntireJ properties
     * @throws NullPointerException
     *             When the <code>Document</code> passed is null
     */
    public static void initialiseEntireJProperties(EJReportFrameworkManager manager, String entireJPropertiesFileName)
    {
        if (entireJPropertiesFileName == null || entireJPropertiesFileName.trim().length() == 0)
        {
            throw new NullPointerException("The entireJ properties file name passed to createEntireJProperties is either null or of zero length");
        }

        try
        {
            InputStream inStream = EJReportFileLoader.class.getClassLoader().getResourceAsStream(entireJPropertiesFileName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            saxPArser.parse(inStream, new EntireJReportPropertiesHandler(EJCoreReportRuntimeProperties.getInstance()));
        }
        catch (Exception e)
        {
            throw new EJReportRuntimeException(e);
        }
    }
}
