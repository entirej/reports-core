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

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportBlockHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportBlockProperties _blockProperties;
    private EJCoreReportProperties      _reportProperties;

    private static final String         ELEMENT_BLOCK                 = "block";
    private static final String         ELEMENT_DESCRIPTION           = "description";
    private static final String         ELEMENT_SERVICE_CLASS_NAME    = "serviceClassName";
    private static final String         ELEMENT_ACTION_PROCESSOR      = "actionProcessorClassName";
    private static final String         ELEMENT_SCREEN_TYPE           = "screenType";
    private static final String         ELEMENT_ODD_VA                = "oddVA";
    private static final String         ELEMENT_EVEN_VA               = "evenVA";
    private static final String         ELEMENT_SCREEN_H_COL_HEIGHT   = "headerColHeight";
    private static final String         ELEMENT_SCREEN_D_COL_HEIGHT   = "detailColHeight";
    private static final String         ELEMENT_SCREEN_F_COL_HEIGHT   = "footerColHeight";
    private static final String         ELEMENT_SCREEN_X              = "x";
    private static final String         ELEMENT_SCREEN_Y              = "y";
    private static final String         ELEMENT_SCREEN_WIDTH          = "width";
    private static final String         ELEMENT_SCREEN_HEIGHT         = "height";

    private static final String         ELEMENT_BLOCK_GROUP           = "blockGroup";
    private static final String         ELEMENT_ITEM                  = "item";
    private static final String         ELEMENT_SCREEN_ITEM           = "screenitem";
    private static final String         ELEMENT_SCREEN_COLUMN         = "columnitem";

    public ReportBlockHandler(EJCoreReportProperties formProperties)
    {
        _reportProperties = formProperties;
    }

    public void dispose()
    {
        _reportProperties = null;
        _blockProperties = null;
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {

        if (name.equals(ELEMENT_ITEM))
        {
            setDelegate(new ReportItemHandler(_blockProperties));
            return;
        }
        else if (name.equals(ELEMENT_SCREEN_ITEM))
        {
            setDelegate(new ReportScreenItemHandler(_blockProperties.getScreenProperties().getScreenItemContainer()));
            return;
        }
        else if (name.equals(ELEMENT_SCREEN_COLUMN))
        {
            setDelegate(new ReportScreenColumnHandler(_blockProperties));
            return;
        }
        else if (name.equals(ELEMENT_BLOCK_GROUP))
        {
            setDelegate(new ReportBlockGroupHandler(_reportProperties, _blockProperties.getScreenProperties().getSubBlocks()));
        }

        if (name.equals(ELEMENT_BLOCK))
        {
            String blockName = attributes.getValue("name");
            String referenced = attributes.getValue("referenced");
            String referencedBlockName = attributes.getValue("referencedBlockName");
            String isControlBlock = attributes.getValue("controlBlock");

            if (Boolean.parseBoolean(referenced))
            {
                // FIXME

            }
            else
            {
                _blockProperties = new EJCoreReportBlockProperties(_reportProperties, blockName, Boolean.parseBoolean(isControlBlock == null ? "false"
                        : isControlBlock));

            }
            if (_blockProperties != null)
            {
                // _blockProperties.setd(Boolean.parseBoolean(referenced));
            }
        }

        if (_blockProperties == null || !_blockProperties.isReferenceBlock())
        {
            // TODO
        }
    }

    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_BLOCK))
        {
            quitAsDelegate();
            return;
        }

        else if (name.equals(ELEMENT_DESCRIPTION))
        {
            _blockProperties.setDescription(value);
        }

        else if (name.equals(ELEMENT_SERVICE_CLASS_NAME))
        {
            _blockProperties.setServiceClassName(value);
        }
        else if (name.equals(ELEMENT_ACTION_PROCESSOR))
        {
            _blockProperties.setActionProcessorClassName(value);
        }
        else if (name.equals(ELEMENT_SCREEN_TYPE))
        {
            _blockProperties.getScreenProperties().setScreenType(EJReportScreenType.valueOf(value));

        }
        
        else if (name.equals(ELEMENT_ODD_VA))
        {
            _blockProperties.getScreenProperties().setOddRowVAName(value);

        }
        else if (name.equals(ELEMENT_EVEN_VA))
        {
            _blockProperties.getScreenProperties().setEvenRowVAName(value);

        }
        else if (name.equals(ELEMENT_SCREEN_X))
        {
            _blockProperties.getScreenProperties().setX(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_H_COL_HEIGHT))
        {
            _blockProperties.getScreenProperties().setHeaderColumnHeight(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_D_COL_HEIGHT))
        {
            _blockProperties.getScreenProperties().setDetailColumnHeight(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_F_COL_HEIGHT))
        {
            _blockProperties.getScreenProperties().setFooterColumnHeight(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_Y))
        {
            _blockProperties.getScreenProperties().setY(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_WIDTH))
        {
            _blockProperties.getScreenProperties().setWidth(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_HEIGHT))
        {
            _blockProperties.getScreenProperties().setHeight(Integer.parseInt(value));
        }
    }

    @Override
    public void cleanUpAfterDelegate(String name, EJCoreReportPropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            EJCoreReportItemProperties itemProperties = ((ReportItemHandler) currentDelegate).getItemProperties();
            if (itemProperties == null)
            {
                return;
            }

            // If the item name is null, then this item is for a screen item and
            // should be ignored
            if (itemProperties.getName() == null)
            {
                return;
            }
            if (_blockProperties.isReferenceBlock())
            {
                // FIXME
            }
            else
            {
                _blockProperties.getItemContainer().addItemProperties(itemProperties);
            }
            return;
        }

        if (_blockProperties == null || !_blockProperties.isReferenceBlock())
        {

        }
    }
}
