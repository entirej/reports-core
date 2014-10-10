/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties.reader;

import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportItemHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportItemProperties  _itemProperties;
    private EJCoreReportProperties      _formProperties;
    private EJCoreReportBlockProperties _blockProperties;

    private static final String         ELEMENT_ITEM                 = "item";
    private static final String         ELEMENT_BLOCK_SERVICE_ITEM   = "blockServiceItem";
    private static final String         ELEMENT_DATA_TYPE_CLASS_NAME = "dataTypeClassName";
    private static final String         ELEMENT_DEFAULT_QUERY_VALUE  = "defaultQueryValue";

    public ReportItemHandler(EJCoreReportBlockProperties blockProperties)
    {
        _formProperties = blockProperties.getReportProperties();
        _blockProperties = blockProperties;

        _itemProperties = new EJCoreReportItemProperties(blockProperties);
        _itemProperties.setBlockServiceItem(!_blockProperties.isControlBlock());

    }

    public EJCoreReportItemProperties getItemProperties()
    {
        return _itemProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            _itemProperties.setName(attributes.getValue("name"));
        }

    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            quitAsDelegate();
            return;
        }

        else if (name.equals(ELEMENT_BLOCK_SERVICE_ITEM))
        {
            _itemProperties.setBlockServiceItem(Boolean.parseBoolean(value));
        }

        else if (name.equals(ELEMENT_DATA_TYPE_CLASS_NAME))
        {
            _itemProperties.setDataTypeClassName(value);
        }

        else if (name.equals(ELEMENT_DEFAULT_QUERY_VALUE))
        {
            _itemProperties.setDefaultQueryValue(value);
        }
    }

    @Override
    protected void cleanUpAfterDelegate(String name, EJCoreReportPropertiesTagHandler currentDelegate)
    {

    }

}
