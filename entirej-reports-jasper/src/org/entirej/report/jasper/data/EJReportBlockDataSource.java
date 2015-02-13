/*******************************************************************************
 * Copyright 2014 Mojave Innovations GmbH
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

package org.entirej.report.jasper.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportVAPattern;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportBlockDataSource implements JRDataSource, Serializable, EJReportBlockItemVAContext, EJReportActionContext
{

    private final EJReportBlock block;
    private int                 index      = -1;
    private Map<String, Object> fieldCache = new HashMap<String, Object>();
    private Locale              defaultLocale;

    public EJReportBlockDataSource(EJReportBlock block)
    {
        this.block = block;
        defaultLocale = block.getReport().getFrameworkManager().getCurrentLocale();
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException
    {

        Object value = getValue(field);

        return value;
    }

    private Object getValue(JRField field)
    {
        if ("_EJ_VA_CONTEXT".equals(field.getName()))
        {
            return this;
        }
        if ("_EJ_AP_CONTEXT".equals(field.getName()))
        {

            return this;
        }

        if (field.getName().startsWith("EJRJ_BLOCK_DS_"))
        {
            EJReportBlock subBlock = null;

            String blockName = field.getName().substring("EJRJ_BLOCK_DS_".length());
            subBlock = this.block.getReport().getBlock(blockName);

            if (subBlock != null)
            {
                if (!subBlock.isControlBlock())
                {
                    subBlock.executeQuery();
                }
                return new EJReportBlockDataSource(subBlock);
            }

        }

        String name = field.getName();

        if (fieldCache.containsKey(name))
        {
            return fieldCache.get(name);
        }

        if (name.contains("."))
        {
            String blockName = name.substring(0, name.indexOf('.'));
            String itemName = name.substring(name.indexOf('.') + 1);
            if (blockName.equals(block.getName()))
            {
                EJReportRecord record = block.getFocusedRecord();

                if (record == null)
                {
                    System.out.println(block.getBlockRecords().size());
                }

                Object value = record.getValue(itemName);
                fieldCache.put(name, value);
                return value;
            }
            else
            {
                EJReportBlock otherBlock = block.getReport().getBlock(blockName);
                if (otherBlock != null)
                {
                    EJReportRecord focusedRecord = otherBlock.getFocusedRecord();
                    if (focusedRecord != null)
                    {
                        Object value = focusedRecord.getValue(itemName);
                        fieldCache.put(name, value);
                        return value;
                    }

                }
            }

        }
        else
        {
            EJReportRecord record = block.getFocusedRecord();
            Object value = record.getValue(name);
            fieldCache.put(name, value);
            return value;
        }

        return null;
    }

    @Override
    public boolean next() throws JRException
    {
        fieldCache.clear();
        index++;

        boolean hasRecord = index < block.getBlockRecordCount();
        if (hasRecord)
        {
            block.navigateToNextRecord();
        }
        if (!hasRecord)
        {
            index = -1;
        }
        return hasRecord;
    }

    @Override
    public boolean isActive(String item, String section, String vaName)
    {
        EJReportScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

        // System.err.println(item +" B="+block.getName());
        if (reportItem == null)
            return false;
        EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
        // if(visualAttribute!=null)
        // {
        // System.err.println("FOUND="+item +" B="+block.getName());
        // }
        return visualAttribute != null && visualAttribute.getName().equals(vaName);
    }

    @Override
    public boolean isVisible(String item, String section)
    {
        EJReportScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

        if (reportItem == null)
            return false;

        return reportItem.isVisible();
    }

    @Override
    public Object getVABaseValue(Object value, String item, String section)
    {
        if (value instanceof String)
        {
            EJReportScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

            if (reportItem == null)
                return value;
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            if (visualAttribute == null)
                return value;
            EJReportVAPattern localePattern = visualAttribute.getLocalePattern();
            switch (localePattern)
            {
                case CURRENCY:
                case NUMBER:
                case INTEGER:
                case PERCENT:
                    try
                    {

                        return new BigDecimal((String) value);
                    }
                    catch (NumberFormatException e)
                    {
                        // ignore
                    }

                default:
                    break;
            }
        }

        return value;
    }

    private EJReportScreenItem getReportScreenItem(String item, EJReportScreenSection section)
    {

        EJReportScreenItem reportItem = null;

        EJReportRecord record = block.getFocusedRecord();
        reportItem = record.getScreenItem(item, section);

        return reportItem;
    }

    @Override
    public boolean canShowBlock(String blockName)
    {
        return block.getReport().getActionController().canShowBlock(block.getReport(), blockName);
    }

    @Override
    public boolean canShowScreenItem(String blockName, String screenItem, String section)
    {

        return block.getReport().getActionController().canShowScreenItem(block.getReport(), blockName, screenItem, EJReportScreenSection.valueOf(section));
    }

}
