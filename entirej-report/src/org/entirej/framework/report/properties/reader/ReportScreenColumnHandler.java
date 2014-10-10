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

import org.entirej.framework.report.interfaces.EJReportBorderProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportBorderProperties;
import org.entirej.framework.report.properties.EJCoreReportColumnProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportScreenColumnHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportProperties       _formProperties;
    private EJCoreReportBlockProperties  _blockProperties;
    private EJCoreReportColumnProperties _column;
    private static final String          ELEMENT_ITEM            = "columnitem";
    private static final String          ELEMENT_HEADER          = "headerScreen";
    private static final String          ELEMENT_DETAIL          = "detailScreen";
    private static final String          ELEMENT_FOOTER          = "footerScreen";

    private static final String          ELEMENT_SCREEN_WIDTH    = "width";
    private static final String          ELEMENT_SCREEN_HEIGHT   = "height";

    private static final String          ELEMENT_SHOW_TOPLINE    = "showTopLine";
    private static final String          ELEMENT_SHOW_BOTTOMLINE = "showBottomLine";
    private static final String          ELEMENT_SHOW_LEFTLINE   = "showLeftLine";
    private static final String          ELEMENT_SHOW_RIGHTLINE  = "showRightLine";
    private static final String          ELEMENT_LINE_WIDTH      = "lineWidth";
    private static final String          ELEMENT_LINE_STYLE      = "lineStyle";
    private static final String          ELEMENT_LINE_VA         = "lineVA";

    private static final String          ELEMENT_SCREEN_ITEM     = "screenitem";

    public ReportScreenColumnHandler(EJCoreReportBlockProperties blockProperties)
    {
        _formProperties = blockProperties.getReportProperties();
        _blockProperties = blockProperties;

    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {

            String itemname = attributes.getValue("name");

            _column = new EJCoreReportColumnProperties(_blockProperties);
            _column.setName(itemname);
            _column.setShowHeader(Boolean.valueOf(attributes.getValue("showHeader")));
            _column.setShowFooter(Boolean.valueOf(attributes.getValue("showFooter")));
            _blockProperties.getLayoutScreenProperties().getColumnContainer().addColumnProperties(_column);
        }
        else if (name.equals(ELEMENT_HEADER))
        {
            setDelegate(new ColumnHandler(name, _column.getHeaderScreen(), _column.getHeaderBorderProperties()));
            return;
        }
        else if (name.equals(ELEMENT_DETAIL))
        {
            setDelegate(new ColumnHandler(name, _column.getDetailScreen(), _column.getDetailBorderProperties()));
            return;
        }
        else if (name.equals(ELEMENT_FOOTER))
        {
            setDelegate(new ColumnHandler(name, _column.getFooterScreen(), _column.getFooterBorderProperties()));
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
        private final String                 tag;
        private EJCoreReportScreenProperties screenProperties;
        private EJCoreReportBorderProperties borderProperties;

        public ColumnHandler(String tag, EJCoreReportScreenProperties screenProperties, EJCoreReportBorderProperties borderProperties)
        {

            this.screenProperties = screenProperties;
            this.borderProperties = borderProperties;
            this.tag = tag;
        }

        public void startLocalElement(String name, Attributes attributes) throws SAXException
        {
            if (name.equals(tag))
            {

            }
            else if (name.equals(ELEMENT_SCREEN_ITEM))
            {

                setDelegate(new ReportScreenItemHandler(screenProperties));
                return;
            }

        }

        public void endLocalElement(String name, String value, String untrimmedValue)
        {
            if (name.equals(tag))
            {
                quitAsDelegate();
                return;
            }
            else if (name.equals(ELEMENT_SCREEN_WIDTH))
            {
                screenProperties.setWidth(Integer.parseInt(value));
            }
            else if (name.equals(ELEMENT_SCREEN_HEIGHT))
            {
                screenProperties.setHeight(Integer.parseInt(value));
            }
            else if (name.equals(ELEMENT_LINE_WIDTH))
            {
                borderProperties.setLineWidth(Double.parseDouble(value));
            }
            else if (name.equals(ELEMENT_LINE_STYLE))
            {
                borderProperties.setLineStyle(EJReportBorderProperties.LineStyle.valueOf(value));
            }
            else if (name.equals(ELEMENT_LINE_VA))
            {
                borderProperties.setVisualAttributeName(value);
            }
            else if (name.equals(ELEMENT_SHOW_TOPLINE))
            {
                borderProperties.setShowTopLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_BOTTOMLINE))
            {
                borderProperties.setShowBottomLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_LEFTLINE))
            {
                borderProperties.setShowLeftLine(Boolean.valueOf(value));
            }
            else if (name.equals(ELEMENT_SHOW_RIGHTLINE))
            {
                borderProperties.setShowRightLine(Boolean.valueOf(value));
            }

        }

    }

}
