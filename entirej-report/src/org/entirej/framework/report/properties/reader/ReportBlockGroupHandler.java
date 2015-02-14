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

import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.entirej.framework.report.properties.EJReportBlockContainer.BlockGroup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportBlockGroupHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportProperties _formProperties;
    private BlockGroup             _blockGroup;

    private static final String    ELEMENT_BLOCK        = "block";
    private static final String    ELEMENT_BLOCK_GROUP  = "blockGroup";

    private static final String    ELEMENT_BLOCK_HEADER = "ej.header.blocks";
    private static final String    ELEMENT_BLOCK_FOOTER = "ej.footer.blocks";

    public ReportBlockGroupHandler(EJCoreReportProperties formProperties)
    {
        _formProperties = formProperties;
        _blockGroup = new BlockGroup();
    }

    public ReportBlockGroupHandler(EJCoreReportProperties formProperties, BlockGroup _blockGroup)
    {
        _formProperties = formProperties;
        this._blockGroup = _blockGroup;
    }

    public BlockGroup getBlockGroup()
    {
        return _blockGroup;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_BLOCK_GROUP))
        {

            String value = attributes.getValue("name");
            if (value != null && value.trim().length() > 0)
            {
                _blockGroup.setName(value);
            }
        }
        else if (name.equals(ELEMENT_BLOCK))
        {
            setDelegate(new ReportBlockHandler(_formProperties));
        }
    }

    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_BLOCK_GROUP))
        {
            quitAsDelegate();
        }
        if (name.equals(ELEMENT_BLOCK_HEADER))
        {
            quitAsDelegate();
        }
        if (name.equals(ELEMENT_BLOCK_FOOTER))
        {
            quitAsDelegate();
        }
    }

    public void cleanUpAfterDelegate(String name, EJCoreReportPropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_BLOCK))
        {
            _blockGroup.addBlockProperties(((ReportBlockHandler) currentDelegate).getBlockProperties());
            return;
        }
    }

}
