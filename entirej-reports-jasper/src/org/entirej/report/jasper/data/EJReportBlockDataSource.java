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

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.data.EJReportDataScreenItem;
import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportBlockDataSource implements JRDataSource, Serializable, EJReportBlockItemVAContext, EJReportActionContext
{

    private final EJReportBlock  block;
    private int                  index      = -1;
    private Map<String, Object>  fieldCache = new HashMap<String, Object>();
    private Map<String, Object>  sitemCache = new HashMap<String, Object>();
    private Map<String, Boolean> vCache     = new HashMap<String, Boolean>();
    private Map<String, Boolean> svCache    = new HashMap<String, Boolean>();
    private Map<String, Boolean> aCache     = new HashMap<String, Boolean>();
    private Locale               defaultLocale;

    public static final Object   EMPTY      = new Object();

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
                EJReportRecord record = block.getCurrentRecord();

                if (record == null)
                {
                    System.out.println(block.getRecords().size());
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
                    EJReportRecord focusedRecord = otherBlock.getCurrentRecord();
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
            EJReportRecord record = block.getCurrentRecord();
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
        sitemCache.clear();
        vCache.clear();
        aCache.clear();
        svCache.clear();
        index++;

        boolean hasRecord = index < block.getRecordCount();
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

        if (!isVisible(item, section))
            return false;

        String key = section + item + vaName;

        Boolean b = aCache.get(key);
        if (b == null)
        {
            EJReportDataScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

            // System.err.println(item +" B="+block.getName());
            if (reportItem == null)
                return false;
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            // if(visualAttribute!=null)
            // {
            // System.err.println("FOUND="+item +" B="+block.getName());
            // }
            b = visualAttribute != null && visualAttribute.getName().equals(vaName);
            aCache.put(key, b);
            return b;
        }

        return b;
    }

    @Override
    public boolean isVisible(String item, String section)
    {
        String key = section + item;
        Boolean b = vCache.get(key);
        if (b == null)
        {
            EJReportDataScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

            if (reportItem == null)
            {
                vCache.put(key, true);
                return true;
            }

            b = reportItem.isVisible();
            vCache.put(key, b);
            return b;
        }
        return b;
    }

    @Override
    public Object getVABaseValue(Object value, String item, String section)
    {
        if (value != null)
        {

            EJReportDataScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

            if (reportItem == null)
                return value;
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            if (visualAttribute == null)
                return value;

            // handle formats

            {

                if (visualAttribute.getManualPattern() != null && !visualAttribute.getManualPattern().isEmpty())
                {
                    String pattern = (visualAttribute.getManualPattern());
                    if (value instanceof Number)
                    {
                        DecimalFormat myFormatter = new DecimalFormat(pattern);
                        value = myFormatter.format((Number) value);
                    }
                    if (value instanceof Date)
                    {
                        SimpleDateFormat myFormatter = new SimpleDateFormat(pattern);
                        value = myFormatter.format((Date) value);
                    }
                }
                else
                {
                    SimpleDateFormat dateFormat = null;
                    switch (visualAttribute.getLocalePattern())
                    {
                        case CURRENCY:
                            value = toNumber(value);
                            if (value instanceof Number)
                            {
                                value = java.text.NumberFormat.getCurrencyInstance(defaultLocale).format((Number) value);
                            }

                            break;
                        case PERCENT:
                            value = toNumber(value);
                            if (value instanceof Number)
                            {
                                value = java.text.NumberFormat.getPercentInstance(defaultLocale).format((Number) value);
                            }
                            
                            break;
                        case INTEGER:
                            value = toNumber(value);
                            if (value instanceof Number)
                            {
                                value = java.text.NumberFormat.getIntegerInstance(defaultLocale).format((Number) value);
                            }
                            break;
                        case NUMBER:
                            value = toNumber(value);
                            if (value instanceof Number)
                            {
                                value = java.text.NumberFormat.getNumberInstance(defaultLocale).format((Number) value);
                            }
                            break;

                        case DATE_FULL:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.FULL, defaultLocale));
                            break;
                        case DATE_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.LONG, defaultLocale));
                            break;
                        case DATE_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.MEDIUM, defaultLocale));
                            break;
                        case DATE_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.SHORT, defaultLocale));
                            break;
                        case DATE_TIME_FULL:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, defaultLocale));
                            break;
                        case DATE_TIME_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, defaultLocale));
                            break;
                        case DATE_TIME_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, defaultLocale));
                            break;
                        case DATE_TIME_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, defaultLocale));
                            break;

                        case TIME_FULL:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.FULL, defaultLocale));
                            break;
                        case TIME_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.LONG, defaultLocale));
                            break;
                        case TIME_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.MEDIUM, defaultLocale));
                            break;
                        case TIME_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.SHORT, defaultLocale));
                            break;

                        default:
                            break;
                    }

                    if (dateFormat != null && value instanceof Date)
                    {
                        value = dateFormat.format(value);
                    }
                }

            }

            return toStyleText(value.toString(), visualAttribute);

            // EJReportVAPattern localePattern =
            // visualAttribute.getLocalePattern();
            // switch (localePattern)
            // {
            // case CURRENCY:
            // case NUMBER:
            // case INTEGER:
            // case PERCENT:
            // try
            // {
            //
            // return new BigDecimal((String) value);
            // }
            // catch (NumberFormatException e)
            // {
            // // ignore
            // }
            //
            // default:
            // break;
            // }
        }

        return value;
    }

    private Object toNumber(Object value)
    {
        if (value instanceof String)
        {
            try
            {

                value = new  BigDecimal((String) value);
            }
            catch (NumberFormatException e)
            {
                // ignore
            }
        }
        return value;
    }

    String toStyleText(String text, EJReportVisualAttributeProperties va)
    {
        StringBuilder builder = new StringBuilder();
        boolean useStyle =false;
        builder.append("<style ");
        // va base styles

        // isBold="true"

        Color backgroundColor = va.getBackgroundColor();
        if (backgroundColor != null)
        {
            useStyle=true;
            builder.append(" backcolor=\"").append(toHex(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue())).append("\"");

        }
        Color foregroundColor = va.getForegroundColor();
        if (foregroundColor != null)
        {
            useStyle=true;
            builder.append(" forecolor=\"").append(toHex(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue())).append("\"");
        }

        String fontName = va.getFontName();
        if (!EJCoreReportVisualAttributeProperties.UNSPECIFIED.equals(fontName))
        {
            useStyle=true;
            builder.append(" fontName=\"").append(fontName).append("\"");
            builder.append(" isPdfEmbedded=\"true\"");
        }

        float fontSize = va.getFontSize();
        if (fontSize != -1)
        {
            useStyle=true;
            builder.append(" size=\"").append(fontSize).append("\"");
        }

        EJReportFontStyle fontStyle = va.getFontStyle();
        switch (fontStyle)
        {
            case Italic:
                useStyle=true;
                builder.append(" isItalic=\"true\"");
                break;
            case Underline:
                useStyle=true;
                builder.append(" isUnderline=\"true\"");
                break;
            case StrikeThrough:
                useStyle=true;
                builder.append(" isStrikeThrough=\"true\"");
                break;

            default:
                break;
        }

        EJReportFontWeight fontWeight = va.getFontWeight();

        switch (fontWeight)
        {
            case Bold:
                useStyle=true;
                builder.append(" isBold=\"true\"");
                builder.append(" pdfFontName=\"Helvetica-Bold\"");
                break;
            default:
                break;
        }

        if( !useStyle)return text;
        
        builder.append(">").append(text).append("</style>");

        return builder.toString();
    }

    public static String toHex(int r, int g, int b)
    {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    private static String toBrowserHexValue(int number)
    {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2)
        {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    private EJReportDataScreenItem getReportScreenItem(String item, EJReportScreenSection section)
    {

        String key = section + item;

        Object object = sitemCache.get(section);
        if (object == null)
        {
            EJReportDataScreenItem reportItem = null;

            EJReportRecord record = block.getCurrentRecord();
            if (record.hasScreenItemData(item, section))
            {
                reportItem = record.getScreenItem(item, section);
                sitemCache.put(key, reportItem);
            }
            else
            {
                sitemCache.put(key, EMPTY);
            }

            return reportItem;
        }

        return object == EMPTY ? null : (EJReportDataScreenItem) object;

    }

    @Override
    public boolean canShowBlock(String blockName)
    {
        return block.getReport().getActionController().canShowBlock(block.getReport(), blockName);
    }

    @Override
    public boolean canShowScreenItem(String blockName, String screenItem, String section)
    {
        String key = section + screenItem;
        Boolean b = svCache.get(key);
        if (b == null)
        {
            b = block.getReport().getActionController().canShowScreenItem(block.getReport(), blockName, screenItem, EJReportScreenSection.valueOf(section));
            svCache.put(key, b);
        }
        return b;
    }

}
