package org.entirej.report.poi;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;
import org.entirej.report.poi.layout.EJReportPOIAlignment;
import org.entirej.report.poi.layout.EJReportPOIBorder;

public class EJPOIStyleHelper
{
    private final SXSSFWorkbook        workbook;
    private Map<String, XSSFCellStyle> nonecache = new HashMap<String, XSSFCellStyle>();
    private Map<String, XSSFCellStyle> wrapcache = new HashMap<String, XSSFCellStyle>();
    private XSSFCellStyle              defaultStyle;
    private XSSFCellStyle              defaultStyleWarp;
    private Locale                     currentLocale;

    public EJPOIStyleHelper(SXSSFWorkbook workbook, EJReport report)
    {
        this.workbook = workbook;

        EJReportVisualAttributeProperties va = report.getProperties().getVisualAttributeProperties();
        if (va != null)
        {
            Font font = workbook.createFont();

            if (!EJCoreReportVisualAttributeProperties.UNSPECIFIED.equals(va.getFontName()))
            {
                font.setFontName(va.getFontName());
            }
            switch (va.getFontWeight())
            {
                case Bold:
                    font.setBold(true);

                    break;
            }
            switch (va.getFontStyle())
            {
                case Italic:
                    font.setItalic(true);
                    break;
                case Underline:
                    font.setUnderline(Font.U_SINGLE);
                    break;
                case StrikeThrough:
                    font.setStrikeout(true);
                    break;
            }

            if (va.isFontSizeSet())
            {
                font.setFontHeight((short) (va.getFontSize() * 20));
            }
            currentLocale = report.getCurrentLocale();
            defaultStyle = getStyle(false, va, null,null,null);
            defaultStyleWarp = getStyle(true, va, null,null,null);
        }
        else
        {
            defaultStyleWarp = (XSSFCellStyle) workbook.createCellStyle();
            defaultStyleWarp.setWrapText(true);
        }
    }

    public XSSFCellStyle getDefaultStyle()
    {
        return defaultStyle;
    }
    public XSSFCellStyle getDefaultWrapStyle()
    {
        return defaultStyleWarp;
    }
    
    

    public XSSFCellStyle getStyle(boolean wrap,EJReportVisualAttributeProperties va, String defaultPattren,EJReportPOIBorder border,EJReportPOIAlignment alignment)
    {

         Map<String, XSSFCellStyle> cache = wrap?wrapcache:nonecache;
        
        if (va == null)
            return  wrap?defaultStyleWarp:defaultStyle;;
        XSSFCellStyle cellStyle;

        String key = va.getName()+(border!=null?border.hashCode():"")+(alignment!=null?alignment.hashCode():"");
        if(defaultPattren!=null)
        {
            key+=defaultPattren; 
        }
        if (cache.containsKey(key))
        {
            cellStyle = cache.get(key);
        }
        else
        {
            cellStyle = (XSSFCellStyle) workbook.createCellStyle();
            cellStyle.setWrapText(wrap);
            cache.put(key, cellStyle);
            XSSFFont font = (XSSFFont) workbook.createFont();

            Color foregroundColor = va.getForegroundColor();
            
            if(foregroundColor!=null)
            {
                font.setColor(new XSSFColor(foregroundColor));
                cellStyle.setFont(font);
            }
            if (!EJCoreReportVisualAttributeProperties.UNSPECIFIED.equals(va.getFontName()))
            {
                font.setFontName(va.getFontName());
                cellStyle.setFont(font);
            }
            switch (va.getFontWeight())
            {
                case Bold:
                    font.setBold(true);
                    cellStyle.setFont(font);
                    break;
            }
            switch (va.getFontStyle())
            {
                case Italic:
                    font.setItalic(true);
                    cellStyle.setFont(font);
                    break;
                case Underline:
                    font.setUnderline(Font.U_SINGLE);
                    cellStyle.setFont(font);
                    break;
                case StrikeThrough:
                    font.setStrikeout(true);
                    cellStyle.setFont(font);
                    break;
            }

            if (va.isFontSizeSet())
            {
                font.setFontHeight((short) (va.getFontSize() * 20));
                cellStyle.setFont(font);
            }

            switch ((alignment!=null && va.getHAlignment()!=EJReportScreenAlignment.NONE)?va.getHAlignment():alignment.getHAlignment())
            {
                case LEFT:
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    break;
                case JUSTIFIED:
                    cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
                    break;
                case CENTER:
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    break;
                case RIGHT:
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                    break;

                default:
                    break;
            }
            switch ((alignment!=null && va.getVAlignment()!=EJReportScreenAlignment.NONE)?va.getVAlignment():alignment.getVAlignment())
            {
                case TOP:
                    cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
                    break;
                case CENTER:
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    break;
                case BOTTOM:
                    cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
                    break;
                case JUSTIFIED:
                    cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
                    break;

                default:
                    break;
            }

        }

        
        String manualPattern = va.getManualPattern();
        if (manualPattern != null && !manualPattern.isEmpty())
        {
            switch (va.getLocalePattern())
            {
                case DATE_FULL:
                case DATE_MEDIUM:
                case DATE_TIME_LONG:
                case DATE_TIME_SHORT:
                case TIME_FULL:
                case TIME_LONG:
                case TIME_MEDIUM:
                case TIME_SHORT:
                case DATE_LONG:
                case DATE_SHORT:
                case DATE_TIME_FULL:
                case DATE_TIME_MEDIUM:
                    //switch to excel mode
                    cellStyle.setDataFormat(workbook.createDataFormat().getFormat(DateFormatConverter.convert(currentLocale, manualPattern)));
                    break;
                default:
                        cellStyle.setDataFormat(workbook.createDataFormat().getFormat( manualPattern)); 
            }
        }
        else
        {
         // DateFormatConverter
            /* as of org.apache.poi.ss.usermodel.BuiltinFormats
                0, "General"
                1, "0"
                2, "0.00"
                3, "#,##0"
                4, "#,##0.00"
                5, "$#,##0_);($#,##0)"
                6, "$#,##0_);[Red]($#,##0)"
                7, "$#,##0.00);($#,##0.00)"
                8, "$#,##0.00_);[Red]($#,##0.00)"
                9, "0%"
                0xa, "0.00%"
                0xb, "0.00E+00"
                0xc, "# ?/?"
                0xd, "# ??/??"
                0xe, "m/d/yy"
                0xf, "d-mmm-yy"
                0x10, "d-mmm"
                0x11, "mmm-yy"
                0x12, "h:mm AM/PM"
                0x13, "h:mm:ss AM/PM"
                0x14, "h:mm"
                0x15, "h:mm:ss"
                0x16, "m/d/yy h:mm"
             */
            switch (va.getLocalePattern())
            {
                case DATE_FULL:
                    cellStyle.setDataFormat(0xf);
                    break;
                case DATE_MEDIUM:
                    cellStyle.setDataFormat(0xe);
                    break;
                case DATE_TIME_LONG:
                    cellStyle.setDataFormat(0x16);
                    break;
                case DATE_TIME_SHORT:
                    cellStyle.setDataFormat(0x16);
                    break;
                case TIME_FULL:
                    cellStyle.setDataFormat(0x13);
                    break;
                case TIME_LONG:
                    cellStyle.setDataFormat(0x15);
                    break;
                case TIME_MEDIUM:
                    cellStyle.setDataFormat(0x12);
                    break;
                case TIME_SHORT:
                    cellStyle.setDataFormat(0x14);
                    break;
                case DATE_LONG:
                    cellStyle.setDataFormat(0xe);
                    break;
                case DATE_SHORT:
                    cellStyle.setDataFormat(0xe);
                    break;
                case DATE_TIME_FULL:
                    cellStyle.setDataFormat(0x16);
                    break;
                case DATE_TIME_MEDIUM:
                    cellStyle.setDataFormat(0x16);
                    break;

                case CURRENCY:

                    if (va.getMaximumDecimalDigits() == 2)
                        cellStyle.setDataFormat(7);
                    else if (va.getMaximumDecimalDigits() > 0)
                    {
                        StringBuilder b = new StringBuilder("$#,##0.");

                        for (int i = 0; i < va.getMaximumDecimalDigits(); i++)

                        {

                            b.append("0");

                        }
                        cellStyle.setDataFormat(workbook.createDataFormat().getFormat(b.toString() + ";(" + b.toString() + ")"));

                    }
                    else
                        cellStyle.setDataFormat(6);
                    break;

                case PERCENT:

                    cellStyle.setDataFormat(0xa);
                    break;

                case INTEGER:

                    cellStyle.setDataFormat(1);
                    break;

                case NUMBER:

                    if (va.getMaximumDecimalDigits() == 2)
                        cellStyle.setDataFormat(4);
                    else if (va.getMaximumDecimalDigits() > 0)
                    {
                        StringBuilder b = new StringBuilder("#,##0.");
                        for (int i = 0; i < va.getMaximumDecimalDigits(); i++)
                        {
                            b.append("0");

                        }
                        cellStyle.setDataFormat(workbook.createDataFormat().getFormat(b.toString()));
                    }
                    else
                        cellStyle.setDataFormat(3);

                    break;

                default:
                    Object opattern = va.toPattern(defaultPattren, currentLocale);
                    if (opattern instanceof String)
                    {
                        String pattern = (String) opattern;
                        if (pattern != null && !pattern.isEmpty())
                        {
                        
                            cellStyle.setDataFormat(workbook.createDataFormat().getFormat(pattern));
                        }
                    }
                    
                    break;
            }
        }
        
        
        
        Color backgroundColor = va.getBackgroundColor();
        if(backgroundColor!=null)
        {
            
            cellStyle.setFillForegroundColor(new XSSFColor(backgroundColor)); 
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
           
        }
        
        if(border!=null)
        {
            BorderStyle style = BorderStyle.THIN;
            switch (border.getLineStyle())
            {
                case DASHED:
                    style = BorderStyle.DASHED;
                    break;
                case DOTTED:
                    style = BorderStyle.DOTTED;
                    break;
                case DOUBLE:
                    style = BorderStyle.DOUBLE;
                    break;
                case SOLID:
                    style = BorderStyle.THIN;
                    break;

            }
            XSSFColor bcolor = null;
            if(border.getVisualAttributeName()!=null && border.getVisualAttributeName().getBackgroundColor()!=null)
            {
                
                bcolor = (new XSSFColor(border.getVisualAttributeName().getBackgroundColor())); 
               
            }
            
            if(border.isShowLeftLine())
            {
                cellStyle.setBorderLeft(style); 
                if(bcolor!=null)
                    cellStyle.setLeftBorderColor(bcolor);
            }
            
            if(border.isShowRightLine())
            {
                cellStyle.setBorderRight(style); 
                if(bcolor!=null)
                    cellStyle.setRightBorderColor(bcolor);
            }
            if(border.isShowBottomLine())
            {
                cellStyle.setBorderBottom(style); 
                if(bcolor!=null)
                    cellStyle.setBottomBorderColor(bcolor);
            }
            if(border.isShowTopLine())
            {
                cellStyle.setBorderTop(style); 
                if(bcolor!=null)
                    cellStyle.setTopBorderColor(bcolor);
            }
            
            
        }
        
        
        return cellStyle;
    }
}
