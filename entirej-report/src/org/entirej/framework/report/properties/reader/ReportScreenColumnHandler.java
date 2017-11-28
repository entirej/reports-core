/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties.reader;

import org.entirej.framework.report.interfaces.EJReportBorderProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnSectionProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportScreenColumnHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportBlockProperties        _blockProperties;
    private EJCoreReportScreenColumnProperties _column;
    private static final String                ELEMENT_ITEM            = "columnitem";
    private static final String                ELEMENT_HEADER          = "headerScreen";
    private static final String                ELEMENT_DETAIL          = "detailScreen";
    private static final String                ELEMENT_FOOTER          = "footerScreen";
    private static final String                ELEMENT_SCREEN_HEIGHT   = "height";
    private static final String                ELEMENT_SHOW_TOPLINE    = "showTopLine";
    private static final String                ELEMENT_SHOW_BOTTOMLINE = "showBottomLine";
    private static final String                ELEMENT_SHOW_LEFTLINE   = "showLeftLine";
    private static final String                ELEMENT_SHOW_RIGHTLINE  = "showRightLine";
    private static final String                ELEMENT_LINE_WIDTH      = "lineWidth";
    private static final String                ELEMENT_LINE_STYLE      = "lineStyle";
    private static final String                ELEMENT_LINE_VA         = "lineVA";

    private static final String                ELEMENT_SCREEN_ITEM     = "screenitem";

    public ReportScreenColumnHandler(EJCoreReportBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;

        _blockProperties.getScreenProperties();
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {

            String itemname = attributes.getValue("name");

            _column = new EJCoreReportScreenColumnProperties(_blockProperties);
            _column.setName(itemname);
            if (attributes.getValue("width") != null)
                _column.setWidth(Integer.valueOf(attributes.getValue("width")));

            _column.setShowHeader(Boolean.valueOf(attributes.getValue("showHeader")));
            _column.setShowFooter(Boolean.valueOf(attributes.getValue("showFooter")));
            _blockProperties.getScreenProperties().getColumnContainer().addColumnProperties(_column);
        }
        else if (name.equals(ELEMENT_HEADER))
        {
            setDelegate(new ColumnHandler(name, _column, _column.getHeaderSectionProperties()));
            return;
        }
        else if (name.equals(ELEMENT_DETAIL))
        {
            setDelegate(new ColumnHandler(name, _column, _column.getDetailSectionProperties()));
            return;
        }
        else if (name.equals(ELEMENT_FOOTER))
        {
            setDelegate(new ColumnHandler(name, _column, _column.getFooterSectionProperties()));
            return;
        }
    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            quitAsDelegate();
            return;
        }

    }

    @Override
    protected void cleanUpAfterDelegate(String name, EJCoreReportPropertiesTagHandler currentDelegate)
    {

    }

    private static class ColumnHandler extends EJCoreReportPropertiesTagHandler
    {
        private final String                              _tag;
        private EJCoreReportScreenColumnSectionProperties _sectionProperties;
        EJCoreReportScreenColumnProperties                _column;

        public ColumnHandler(String tag, EJCoreReportScreenColumnProperties column, EJCoreReportScreenColumnSectionProperties sectionProperties)
        {
            _sectionProperties = sectionProperties;
            _tag = tag;
            _column = column;
        }

        public void startLocalElement(String name, Attributes attributes) throws SAXException
        {
            if (name.equals(_tag))
            {

            }
            else if (name.equals(ELEMENT_SCREEN_ITEM))
            {

                setDelegate(new ReportScreenItemHandler(_sectionProperties.getSectionItemContainer()));
                return;
            }

        }

        public void endLocalElement(String name, String value, String untrimmedValue)
        {
            if (name.equals(_tag))
            {
                quitAsDelegate();
                return;
            }
            else if (name.equals(ELEMENT_SCREEN_HEIGHT))
            {
                _sectionProperties.setHeight(Integer.parseInt(value));
            }

            else if (name.equals("width"))
            {
                _column.setWidth(Integer.parseInt(value));// old format
            }
            else if (name.equals(ELEMENT_LINE_WIDTH))
            {
                _sectionProperties.setLineWidth(Double.parseDouble(value));
            }
            else if (name.equals(ELEMENT_LINE_STYLE))
            {
                _sectionProperties.setLineStyle(EJReportBorderProperties.LineStyle.valueOf(value));
            }
            else if (name.equals(ELEMENT_LINE_VA))
            {
                _sectionProperties.setLineVisualAttributeName(value);
            }
            else if (name.equals(ELEMENT_SHOW_TOPLINE))
            {
                _sectionProperties.setShowTopLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_BOTTOMLINE))
            {
                _sectionProperties.setShowBottomLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_LEFTLINE))
            {
                _sectionProperties.setShowLeftLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_RIGHTLINE))
            {
                _sectionProperties.setShowRightLine(Boolean.valueOf(value));
            }

        }

    }

}
