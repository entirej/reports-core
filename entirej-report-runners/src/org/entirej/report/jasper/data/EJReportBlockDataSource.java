/*******************************************************************************
 * Copyright 2014 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/

package org.entirej.report.jasper.data;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.data.EJReportDataScreenItem;
import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportVAPattern;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

public class EJReportBlockDataSource implements JRDataSource, Serializable, EJReportBlockItemVAContext, EJReportActionContext, JRRewindableDataSource
{

    private final EJReportBlock  block;
    private Map<String, Object>  fieldCache = new HashMap<String, Object>();
    private Map<String, Object>  sitemCache = new HashMap<String, Object>();
    private Map<String, Boolean> vCache     = new HashMap<String, Boolean>();
    private Map<String, Boolean> svCache    = new HashMap<String, Boolean>();
    private EJReportRecord       focusedRecord;
    private int         index = -1;
    private Locale               defaultLocale;
    
    private Map<String, SimpleDateFormat> dateMap = new HashMap<String, SimpleDateFormat>();

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

                Object value = focusedRecord.getValue(itemName);
                fieldCache.put(name, value);
                return value;
            }
            else
            {
                EJReportBlock otherBlock = block.getReport().getBlock(blockName);
                if (otherBlock != null)
                {
                    EJReportRecord record = otherBlock.getCurrentRecord();
                    if (record != null)
                    {
                        Object value = record.getValue(itemName);
                        fieldCache.put(name, value);
                        return value;
                    }

                }
            }

        }
        else
        {

            Object value = focusedRecord.getValue(name);
            fieldCache.put(name, value);
            return value;
        }

        return null;
    }

    
    @Override
    public int getRecordIndex()
    {
        return index;
    }
    @Override
    public boolean next() throws JRException
    {
        
        fieldCache.clear();
        sitemCache.clear();
        vCache.clear();
        svCache.clear();
        index++;
       focusedRecord = block.getNextRecord();
       if ((focusedRecord == null) && block.isControlBlock())
        {
            block.reset();
            index = -1;
        }
        return focusedRecord!=null;
    }

    @Override
    public boolean isActive(String item, String section, String vaName)
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
        return visualAttribute != null && visualAttribute.getName().equals(vaName);

    }

    public boolean isActive(String item, String section)
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
        return visualAttribute != null;

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
    public Object getVABaseValuePattern(Object value, String item, String section,String defaultPattern)
    {
        if (value != null)
        {

            EJReportDataScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

            if (reportItem == null)
                return defaultPattern;
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            
            if (visualAttribute == null)
                return defaultPattern;

            // handle formats

            return visualAttribute.toPattern(defaultPattern,defaultLocale);
        }
            
       return defaultPattern;

    }

    @Override
    public Object getVABaseValue(Object value, String item, String section,String defaultPattern) 
    {
        if (value instanceof String)
        {

            EJReportDataScreenItem reportItem = getReportScreenItem(item, EJReportScreenSection.valueOf(section));

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
                break;

                case DATE_FULL:
                case DATE_LONG:
                case DATE_MEDIUM:
                case DATE_SHORT:
                case DATE_TIME_FULL:
                case DATE_TIME_LONG:
                case DATE_TIME_MEDIUM:
                case DATE_TIME_SHORT:
                case TIME_FULL:
                case TIME_LONG:
                case TIME_MEDIUM:
                case TIME_SHORT:
                    
                    String pattern = (String) visualAttribute.toPattern(defaultPattern, defaultLocale);
                    if(pattern!=null && !pattern.isEmpty() )
                    {
                        try
                        {
                         SimpleDateFormat format = getDateFormat(pattern);
                         return format.parse((String) value);
                        }
                        catch(IllegalArgumentException e)
                        {
                            //ignore
                        }
                        catch(ParseException e)
                        {
                            //ignore
                        }
                        
                    }
                    
                    break;
            }
        }

        return value;
    }

    private SimpleDateFormat getDateFormat(String pattern)
    {
        
        if(dateMap.containsKey(pattern))
        {
            return dateMap.get(pattern);
        }
        
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2000);
       
        SimpleDateFormat format = new SimpleDateFormat(pattern, defaultLocale);
        format.set2DigitYearStart(cal.getTime());
        dateMap.put(pattern, format);
        
        return format;
    }


    String _toStyleText(String text, EJReportVisualAttributeProperties va)
    {
        StringBuilder builder = new StringBuilder();
        boolean useStyle = false;
        builder.append("<style ");
        // va base styles

        // isBold="true"

        Color backgroundColor = va.getBackgroundColor();
        if (backgroundColor != null)
        {
            useStyle = true;
            builder.append(" backcolor=\"").append(toHex(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue())).append("\"");

        }
        Color foregroundColor = va.getForegroundColor();
        if (foregroundColor != null)
        {
            useStyle = true;
            builder.append(" forecolor=\"").append(toHex(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue())).append("\"");
        }

        String fontName = va.getFontName();
        if (!EJCoreReportVisualAttributeProperties.UNSPECIFIED.equals(fontName))
        {
            useStyle = true;
            builder.append(" fontName=\"").append(fontName).append("\"");
            builder.append(" isPdfEmbedded=\"true\"");
        }

        float fontSize = va.getFontSize();
        if (fontSize != -1)
        {
            useStyle = true;
            builder.append(" size=\"").append(fontSize).append("\"");
        }

        EJReportFontStyle fontStyle = va.getFontStyle();
        switch (fontStyle)
        {
            case Italic:
                useStyle = true;
                builder.append(" isItalic=\"true\"");
                break;
            case Underline:
                useStyle = true;
                builder.append(" isUnderline=\"true\"");
                break;
            case StrikeThrough:
                useStyle = true;
                builder.append(" isStrikeThrough=\"true\"");
                break;

            default:
                break;
        }

        EJReportFontWeight fontWeight = va.getFontWeight();

        switch (fontWeight)
        {
            case Bold:
                useStyle = true;
                builder.append(" isBold=\"true\"");
                builder.append(" pdfFontName=\"Helvetica-Bold\"");
                break;
            default:
                break;
        }

        if (!useStyle)
            return escape(text);

        builder.append(">").append(escape(text)).append("</style>");

        return builder.toString();
    }

    public static String toHex(int r, int g, int b)
    {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    public String escape(String str)
    {
        StringBuilder buf = new StringBuilder(str.length() * 2);
        int i;
        for (i = 0; i < str.length(); ++i)
        {
            char ch = str.charAt(i);

            String entityName = null;
            switch (ch)
            {
                case 38:
                    entityName = "amp";
                    break;
                case 34:
                    entityName = "quot";
                    break;
                case 60:
                    entityName = "lt";
                    break;
                case 62:
                    entityName = "gt";
                    break;
                case 39:
                    entityName = "apos";
                    break;
            }
            if (entityName == null)
            {
                if (ch > 0x7F)
                {
                    int intValue = ch;
                    buf.append("&#");
                    buf.append(intValue);
                    buf.append(';');
                }
                else
                {
                    buf.append(ch);
                }
            }
            else
            {
                buf.append('&');
                buf.append(entityName);
                buf.append(';');
            }
        }
        return buf.toString();
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

        Object object = sitemCache.get(key);
        if (object == null)
        {
            EJReportDataScreenItem reportItem = null;

            EJReportRecord record = block.getCurrentRecord();
            if (record.hasScreenItemData(item, section))
            {
                reportItem = record.getScreenItemNoValidate(item, section);
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

    @Override
    public void moveFirst() throws JRException
    {
        block.reset();

    }

}
