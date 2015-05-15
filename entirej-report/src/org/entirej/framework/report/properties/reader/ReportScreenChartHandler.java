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

import org.entirej.framework.report.enumerations.EJReportChartType;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportChartProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportScreenChartHandler extends EJCoreReportPropertiesTagHandler
{
    private EJCoreReportBlockProperties  _blockProperties;
    private static final String            ELEMENT_ITEM            = "config";
    private final EJCoreReportChartProperties chartProperties;
   
    
    public ReportScreenChartHandler(EJCoreReportBlockProperties blockProperties)
    {
       
        _blockProperties = blockProperties;
        chartProperties = _blockProperties.getScreenProperties().getChartProperties();
        
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            
            
            chartProperties.setChartType(EJReportChartType.valueOf(attributes.getValue("type")));
            chartProperties.setUse3dView(Boolean.valueOf(attributes.getValue("use3dView")));
            chartProperties.setCategoryItem((attributes.getValue("categoryItem")));
            chartProperties.setLabelItem((attributes.getValue("labelItem")));
            chartProperties.setValue1Item((attributes.getValue("value1Item")));
            chartProperties.setValue2Item((attributes.getValue("value2Item")));
            chartProperties.setValue3Item((attributes.getValue("value3Item")));
            chartProperties.setTitle((attributes.getValue("title")));
            chartProperties.setTitleVA((attributes.getValue("titleVA")));
            chartProperties.setSubtitle((attributes.getValue("subtitle")));
            chartProperties.setSubtitleVA((attributes.getValue("subtitleVA")));
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
    
    
    
}
