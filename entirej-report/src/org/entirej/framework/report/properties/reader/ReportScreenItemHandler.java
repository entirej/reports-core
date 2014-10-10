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

import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.enumerations.EJReportScreenRotation;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.AlignmentBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Date.DateFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineDirection;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineStyle;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Number.NumberFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.RotatableItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.ValueBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportScreenItemHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportScreenItemProperties _itemProperties;
    private EJCoreReportScreenProperties     _blockProperties;

    private static final String              ELEMENT_ITEM                  = "screenitem";
    private static final String              ELEMENT_SCREEN_X              = "x";
    private static final String              ELEMENT_SCREEN_Y              = "y";
    private static final String              ELEMENT_SCREEN_WIDTH          = "width";
    private static final String              ELEMENT_SCREEN_HEIGHT         = "height";
    private static final String              ELEMENT_SCREEN_VISIBLE        = "visible";
    private static final String              ELEMENT_SCREEN_VA             = "va";
    private static final String              ELEMENT_SCREEN_VALUE_PROVIDER = "valueProvider";
    private static final String              ELEMENT_SCREEN_HALIGNMENT     = "hAlignment";
    private static final String              ELEMENT_SCREEN_VALIGNMENT     = "vAlignment";
    private static final String              ELEMENT_SCREEN_ROTATION       = "rotation";
    private static final String              ELEMENT_SCREEN_TEXT           = "text";
    private static final String              ELEMENT_SCREEN_MANUAL_FORMAT  = "manualFormat";
    private static final String              ELEMENT_SCREEN_LOCALE_FORMAT  = "localeFormat";
    private static final String              ELEMENT_SCREEN_LINE_STYLE     = "lineStyle";
    private static final String              ELEMENT_SCREEN_LINE_WIDTH     = "lineWidth";
    private static final String              ELEMENT_SCREEN_LINE_DIRECTION = "lineDirection";
    private static final String              ELEMENT_SCREEN_RECT_RADIUS    = "rectRadius";

    public ReportScreenItemHandler(EJCoreReportScreenProperties blockProperties)
    {
        _blockProperties = blockProperties;

    }

    public EJCoreReportScreenItemProperties getItemProperties()
    {
        return _itemProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            String type = attributes.getValue("type");
            EJReportScreenItemType screenItemType = EJReportScreenItemType.valueOf(type);

            String itemname = attributes.getValue("name");

            _itemProperties = _blockProperties.getScreenItemContainer().createItem(screenItemType, itemname, -1);

        }

    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            quitAsDelegate();
            return;
        }

        else if (name.equals(ELEMENT_SCREEN_X))
        {
            _itemProperties.setX(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_Y))
        {
            _itemProperties.setY(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_WIDTH))
        {
            _itemProperties.setWidth(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_HEIGHT))
        {
            _itemProperties.setHeight(Integer.parseInt(value));
        }
        else if (name.equals(ELEMENT_SCREEN_VISIBLE))
        {
            _itemProperties.setVisible(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_SCREEN_VA))
        {
            _itemProperties.setVisualAttributeName(value);
        }
        else if (name.equals(ELEMENT_SCREEN_VALUE_PROVIDER))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.ValueBaseItem)
            {
                final EJCoreReportScreenItemProperties.ValueBaseItem item = (ValueBaseItem) _itemProperties;
                item.setValue(value);

            }
        }
        else if (name.equals(ELEMENT_SCREEN_HALIGNMENT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.AlignmentBaseItem)
            {
                final EJCoreReportScreenItemProperties.AlignmentBaseItem item = (AlignmentBaseItem) _itemProperties;
                item.setHAlignment(EJReportScreenAlignment.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_VALIGNMENT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.AlignmentBaseItem)
            {
                final EJCoreReportScreenItemProperties.AlignmentBaseItem item = (AlignmentBaseItem) _itemProperties;
                item.setVAlignment(EJReportScreenAlignment.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_ROTATION))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.RotatableItem)
            {
                final EJCoreReportScreenItemProperties.RotatableItem item = (RotatableItem) _itemProperties;
                item.setRotation(EJReportScreenRotation.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_TEXT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Label)
            {
                final EJCoreReportScreenItemProperties.Label item = (EJCoreReportScreenItemProperties.Label) _itemProperties;
                item.setText(value);

            }
        }
        else if (name.equals(ELEMENT_SCREEN_LINE_WIDTH))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Line)
            {
                final EJCoreReportScreenItemProperties.Line item = (EJCoreReportScreenItemProperties.Line) _itemProperties;
                item.setLineWidth(Double.parseDouble(value));

            }
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Rectangle)
            {
                final EJCoreReportScreenItemProperties.Rectangle item = (EJCoreReportScreenItemProperties.Rectangle) _itemProperties;
                item.setLineWidth(Double.parseDouble(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_RECT_RADIUS))
        {

            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Rectangle)
            {
                final EJCoreReportScreenItemProperties.Rectangle item = (EJCoreReportScreenItemProperties.Rectangle) _itemProperties;
                item.setRadius(Integer.parseInt(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_LINE_STYLE))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Line)
            {
                final EJCoreReportScreenItemProperties.Line item = (EJCoreReportScreenItemProperties.Line) _itemProperties;
                item.setLineStyle(LineStyle.valueOf(value));

            }
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Rectangle)
            {
                final EJCoreReportScreenItemProperties.Rectangle item = (EJCoreReportScreenItemProperties.Rectangle) _itemProperties;
                item.setLineStyle(LineStyle.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_LINE_DIRECTION))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Line)
            {
                final EJCoreReportScreenItemProperties.Line item = (EJCoreReportScreenItemProperties.Line) _itemProperties;
                item.setLineDirection(LineDirection.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_MANUAL_FORMAT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Number)
            {
                final EJCoreReportScreenItemProperties.Number item = (EJCoreReportScreenItemProperties.Number) _itemProperties;
                item.setManualFormat(value);

            }
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Date)
            {
                final EJCoreReportScreenItemProperties.Date item = (EJCoreReportScreenItemProperties.Date) _itemProperties;
                item.setManualFormat(value);

            }
        }

        else if (name.equals(ELEMENT_SCREEN_LINE_STYLE))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Line)
            {
                final EJCoreReportScreenItemProperties.Line item = (EJCoreReportScreenItemProperties.Line) _itemProperties;
                item.setLineStyle(LineStyle.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_LINE_DIRECTION))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Line)
            {
                final EJCoreReportScreenItemProperties.Line item = (EJCoreReportScreenItemProperties.Line) _itemProperties;
                item.setLineDirection(LineDirection.valueOf(value));

            }
        }
        else if (name.equals(ELEMENT_SCREEN_MANUAL_FORMAT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Number)
            {
                final EJCoreReportScreenItemProperties.Number item = (EJCoreReportScreenItemProperties.Number) _itemProperties;
                item.setManualFormat(value);

            }
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Date)
            {
                final EJCoreReportScreenItemProperties.Date item = (EJCoreReportScreenItemProperties.Date) _itemProperties;
                item.setManualFormat(value);

            }
        }

        else if (name.equals(ELEMENT_SCREEN_LOCALE_FORMAT))
        {
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Number)
            {
                final EJCoreReportScreenItemProperties.Number item = (EJCoreReportScreenItemProperties.Number) _itemProperties;
                item.setLocaleFormat(NumberFormats.valueOf(value));

            }
            if (_itemProperties instanceof EJCoreReportScreenItemProperties.Date)
            {
                final EJCoreReportScreenItemProperties.Date item = (EJCoreReportScreenItemProperties.Date) _itemProperties;
                item.setLocaleFormat(DateFormats.valueOf(value));

            }
        }
    }

    @Override
    protected void cleanUpAfterDelegate(String name, EJCoreReportPropertiesTagHandler currentDelegate)
    {

    }

}
