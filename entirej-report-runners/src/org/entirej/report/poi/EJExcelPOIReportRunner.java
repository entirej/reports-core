package org.entirej.report.poi;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportLabelScreenItem;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.EJReportValueBaseScreenItem;
import org.entirej.framework.report.data.EJReportDataScreenItem;
import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportVAPattern;
import org.entirej.framework.report.interfaces.EJReportProperties.ORIENTATION;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;
import org.entirej.report.poi.layout.EJReportPOIBlock;
import org.entirej.report.poi.layout.EJReportPOIElement;
import org.entirej.report.poi.layout.EJReportPOIPage;
import org.entirej.report.poi.layout.EJReportPOIRaw;
import org.entirej.report.poi.layout.EJReportPOIRaw.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJExcelPOIReportRunner
{

    final static Logger              LOGGER = LoggerFactory.getLogger(EJExcelPOIReportRunner.class);
    private EJReportFrameworkManager manager;

    public static final Object       EMPTY  = new Object();

    public void init(EJReportFrameworkManager manager)
    {
        this.manager = manager;

    }

    public void runReport(EJReport report, String outputFile)
    {
        runReport(report, report.getExportType(), outputFile);

    }

    public void runReport(EJReport report, EJReportExportType type, String outputFile)
    {
        System.err.println("RUN SOURCE");
        if (manager == null)
        {
            throw new EJReportRuntimeException("EJReportFrameworkManager not initialised");
        }
        LOGGER.info("START Filling  Report :" + report.getName());
        long start = System.currentTimeMillis();
        report.getActionController().beforeReport(report);
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook();

            SXSSFWorkbook wb = new SXSSFWorkbook(workbook, 100); // keep 100

            // rows in
            EJPOIStyleHelper styleHelper = new EJPOIStyleHelper(workbook, wb, report);
            // memory, exceeding rows
            // will be flushed to
            // disk
            wb.setCompressTempFiles(true);//

            EJReportDataSource reportDS = new EJReportDataSource(report);

            Collection<EJReportPage> pages = report.getPages();
            for (EJReportPage page : pages)
            {
                EJReportPOIPage reportPOIPage = new EJReportPOIPage();
                reportPOIPage.build(report, page);

                SXSSFSheet sheet = wb.createSheet(page.getName());

                if (report.getProperties().getOrientation() == ORIENTATION.LANDSCAPE)
                {
                    sheet.getPrintSetup().setLandscape(true);
                }
                Map<Integer, Integer> colWidths = reportPOIPage.getColWidths();
                for (Entry<Integer, Integer> entry : colWidths.entrySet())
                {

                    sheet.setColumnWidth(entry.getKey(), EJReportPixelUtil.pixel2WidthUnits(entry.getValue()));
                }

                int rownum = 0;
                List<CellRangeAddress> rangeAddresses = new ArrayList<CellRangeAddress>();
                for (EJReportBlock block : report.getHeaderBlocks())
                {

                    rownum = processBlock(styleHelper, report, reportDS, reportPOIPage, sheet, rownum, reportPOIPage.getBlock(block.getName()), rangeAddresses);

                }

                // support first level blocks
                Collection<EJReportBlock> rootBlocks = page.getRootBlocks();

                for (EJReportBlock block : rootBlocks)
                {

                    rownum = processBlock(styleHelper, report, reportDS, reportPOIPage, sheet, rownum, reportPOIPage.getBlock(block.getName()), rangeAddresses);

                }

                for (EJReportBlock block : report.getFooterBlocks())
                {

                    rownum = processBlock(styleHelper, report, reportDS, reportPOIPage, sheet, rownum, reportPOIPage.getBlock(block.getName()), rangeAddresses);

                }

                for (CellRangeAddress address : rangeAddresses)
                {
                    sheet.addMergedRegionUnsafe(address);
                }

            }
            FileOutputStream out = new FileOutputStream(outputFile);
            wb.write(out);
            out.close();

            wb.close();

            // dispose of temporary files backing this workbook on disk
            wb.dispose();
            LOGGER.info("END Filling  Report :" + report.getName() + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);
            LOGGER.info("Output :" + outputFile);
        }
        catch (Throwable t)
        {
            throw new EJReportRuntimeException(t.getMessage(), t);
        }

    }

    private int processBlock(EJPOIStyleHelper styleHelper, EJReport report, EJReportDataSource reportDS, EJReportPOIPage reportPOIPage, SXSSFSheet sheet,
            int rownum, EJReportPOIBlock poiBlock, List<CellRangeAddress> rangeAddresses)
    {

        if (poiBlock != null)
        {
            EJReportBlock block = poiBlock.getBlock();
            LOGGER.info("START get datasource :" + block.getName());
            long dsStart = System.currentTimeMillis();
            EJReportBlockDataSource blockDataSource = reportDS.getBlockDataSource(block.getName());
            LOGGER.info("END get datasource :" + block.getName() + " TIME(sec):" + (System.currentTimeMillis() - dsStart) / 1000);

            Map<String, SimpleDateFormat> dateMap = new HashMap<String, SimpleDateFormat>();
            
            int initRow = rownum;
            if (blockDataSource.next())
            {

                EJReportRecord currentRecord = block.getCurrentRecord();
                // build header raws
                List<EJReportPOIRaw> raws = poiBlock.getRaws();
                for (EJReportPOIRaw poiRaw : raws)
                {
                    if (poiRaw.getType() == Type.HEADER)
                    {
                        SXSSFRow row = createRaw(styleHelper, sheet, rownum, poiRaw);
                        List<EJReportPOIElement> elements = poiRaw.getElements();
                        Map<String, Object> sitemCache = new HashMap<String, Object>();
                        for (EJReportPOIElement poiElement : elements)
                        {

                            createCell(EJReportScreenSection.HEADER, sitemCache, dateMap, currentRecord, styleHelper, report, reportDS, sheet, rownum, block,
                                    blockDataSource, row, poiElement, rangeAddresses);
                        }
                        rownum++;
                    }
                }

                // details

                do
                {
                    currentRecord = block.getCurrentRecord();
                    Map<String, Object> sitemCache = new HashMap<String, Object>();
                    for (EJReportPOIRaw poiRaw : raws)
                    {
                        if (poiRaw.getType() == Type.DETAIL)
                        {
                            SXSSFRow row = createRaw(styleHelper, sheet, rownum, poiRaw);
                            List<EJReportPOIElement> elements = poiRaw.getElements();

                            for (EJReportPOIElement poiElement : elements)
                            {

                                createCell(EJReportScreenSection.DETAIL, sitemCache, dateMap, currentRecord, styleHelper, report, reportDS, sheet, rownum,
                                        block, blockDataSource, row, poiElement, rangeAddresses);
                            }
                            rownum++;
                        }
                    }
                    // process SUB blocks
                    List<EJReportPOIBlock> subblocks = poiBlock.getBlocks();
                    for (EJReportPOIBlock subBlock : subblocks)
                    {
                        rownum = processBlock(styleHelper, report, reportDS, reportPOIPage, sheet, rownum, subBlock, rangeAddresses);
                    }
                }
                while (blockDataSource.next());
                currentRecord = block.getCurrentRecord();

                // build footer raws
                for (EJReportPOIRaw poiRaw : raws)
                {
                    if (poiRaw.getType() == Type.FOOTER)
                    {
                        SXSSFRow row = createRaw(styleHelper, sheet, rownum, poiRaw);
                        List<EJReportPOIElement> elements = poiRaw.getElements();
                        Map<String, Object> sitemCache = new HashMap<String, Object>();
                        for (EJReportPOIElement poiElement : elements)
                        {

                            createCell(EJReportScreenSection.FOOTER, sitemCache, dateMap, currentRecord, styleHelper, report, reportDS, sheet, rownum, block,
                                    blockDataSource, row, poiElement, rangeAddresses);
                        }
                        rownum++;
                    }
                }
            }
           
            if(initRow<(rownum-1))
            {
                List<EJReportPOIRaw> raws = poiBlock.getRaws();
                int starCell=0;
                int endCell=0;
                boolean setIgnoew = true;
                for (EJReportPOIRaw poiRaw : raws)
                {
                 
                          
                        for (EJReportPOIElement poiElement : poiRaw.getElements())
                        {
    
                           if(   poiElement.isIgnoreWarnings() && poiElement.getStartCell()>-1 )
                           {
                               setIgnoew = true;
                               starCell = Math.min(starCell, poiElement.getStartCell());
                               endCell = Math.min(endCell, poiElement.getStartCell());
                              
                             
                           }
                            
                        }
                    
                }
                if(setIgnoew)
                    styleHelper.addIgnore(sheet.getSheetName(), new CellRangeAddress(initRow, rownum-1, starCell, endCell), IgnoredErrorType.values());
                
            }
        }
        return rownum;
    }

    private SXSSFRow createRaw(EJPOIStyleHelper styleHelper, SXSSFSheet sheet, int rownum, EJReportPOIRaw poiRaw)
    {
        SXSSFRow row = sheet.createRow(rownum);
        row.setRowStyle(styleHelper.getDefaultEmptyStyle());
        if (poiRaw.getHeight() != -1)
            row.setHeight(EJReportPixelUtil.pixel2HeighthUnits(poiRaw.getHeight(), sheet.getDefaultRowHeight()));
        return row;
    }

    private void createCell(EJReportScreenSection section, Map<String, Object> sitemCache, Map<String, SimpleDateFormat> dateMap, EJReportRecord record,
            EJPOIStyleHelper styleHelper, EJReport report, EJReportDataSource reportDS, SXSSFSheet sheet, int rownum, EJReportBlock block,
            EJReportBlockDataSource blockDataSource, SXSSFRow row, EJReportPOIElement poiElement, List<CellRangeAddress> rangeAddresses)
    {
        if (poiElement.getStartCell() == -1)
            throw new EJReportRuntimeException("eliment issue on " + section.name() + "-> " + poiElement.getId());
        EJReportScreenItem id = poiElement.getId();
        if (id != null)
        {
            Object value = extractValue(report, reportDS, block, blockDataSource, id);
            if (value != null)
            {

                EJReportVisualAttributeProperties va = poiElement.getVa();
                EJReportDataScreenItem reportScreenItem = getReportScreenItem(poiElement.getId().getName(), section, sitemCache, record);
                if (reportScreenItem != null && reportScreenItem.getVisualAttribute() != null)
                {
                    va = reportScreenItem.getVisualAttribute();
                    value = getVABaseValue(value, poiElement.getDefaultPattren(), reportScreenItem, block.getReport().getCurrentLocale(), dateMap);
                }
                SXSSFCell cell = row.createCell(poiElement.getStartCell(),
                        (poiElement.getColumnType() == EJReportScreenItemType.NUMBER) || (value instanceof Number) ? CellType.NUMERIC : CellType.STRING);

                cell.setCellStyle(styleHelper.getStyle(poiElement.isWrap() || (va != null && va.isExpandToFit()), va, poiElement.getDefaultPattren(),
                        poiElement.getBorder(), poiElement.getAlignment()));
                if (poiElement.isWrap() || (va != null && va.isExpandToFit()))
                {
                    row.setRowStyle(styleHelper.getDefaultWrapStyle());
                    row.setHeight((short) -1);
                }
                setCellValue(cell, value);
               
            }
            else
            {
                EJReportVisualAttributeProperties va = poiElement.getVa();
                EJReportDataScreenItem reportScreenItem = getReportScreenItem(poiElement.getId().getName(), section, sitemCache, record);
                if (reportScreenItem != null && reportScreenItem.getVisualAttribute() != null)
                {
                    va = reportScreenItem.getVisualAttribute();
                }
                SXSSFCell cell = row.createCell(poiElement.getStartCell(), CellType.BLANK);

                XSSFCellStyle cellStyle = styleHelper.getStyle(poiElement.isWrap() || (va != null && va.isExpandToFit()), va, poiElement.getDefaultPattren(),
                        poiElement.getBorder(), poiElement.getAlignment());

                // cellStyle.setb
                cell.setCellStyle(cellStyle);

                if (poiElement.isWrap() || (va != null && va.isExpandToFit()))
                {
                    row.setRowStyle(styleHelper.getDefaultWrapStyle());
                    row.setHeight((short) -1);
                }
            }
        }

        if (poiElement.isSupportMerge() && (poiElement.getStartCell() != poiElement.getEndCell() || poiElement.getRawSpan() > 0))
        {

            rangeAddresses.add(new CellRangeAddress(rownum, // first
                                                            // row
                                                            // (0-based)
                    rownum + poiElement.getRawSpan(), // last row
                    // (0-based)
                    poiElement.getStartCell(), // first
                                               // column
                                               // (0-based)
                    poiElement.getEndCell() // last
                                            // column
                                            // (0-based)
            ));
        }
    }

    public Object getVABaseValue(Object value, String defaultPattern, EJReportDataScreenItem reportItem, Locale defaultLocale,
            Map<String, SimpleDateFormat> dateMap)
    {
        if (value instanceof String)
        {

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
                    if (pattern != null && !pattern.isEmpty())
                    {
                        try
                        {
                            SimpleDateFormat format = getDateFormat(dateMap, pattern, defaultLocale);
                            return format.parse((String) value);
                        }
                        catch (IllegalArgumentException e)
                        {
                            // ignore
                        }
                        catch (ParseException e)
                        {
                            // ignore
                        }

                    }

                    break;
                case NONE:
                    break;
                default:
                    break;
            }
            //handle manual format
            String manualPattern = visualAttribute.getManualPattern();
            if(manualPattern!=null && !manualPattern.isEmpty())
            {
                try
                {
                    SimpleDateFormat format = getDateFormat(dateMap, manualPattern, defaultLocale);
                    return format.parse((String) value);
                }
                catch (IllegalArgumentException e)
                {
                    // ignore
                }
                catch (ParseException e)
                {
                    // ignore
                }
            }
        }

        return value;
    }

    private SimpleDateFormat getDateFormat(Map<String, SimpleDateFormat> dateMap, String pattern, Locale defaultLocale)
    {

        if (dateMap.containsKey(pattern))
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

    private EJReportDataScreenItem getReportScreenItem(String item, EJReportScreenSection section, Map<String, Object> sitemCache, EJReportRecord record)
    {

        String key = section + item;

        Object object = sitemCache.get(key);
        if (object == null)
        {
            EJReportDataScreenItem reportItem = null;

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

    private void setCellValue(SXSSFCell cell, Object value)
    {
        if (value instanceof String)
        {
            cell.setCellValue((String) value);

        }
        else if (value instanceof Integer)
        {
            cell.setCellValue((Integer) value);
        }
        else if (value instanceof Double)
        {
            cell.setCellValue((Double) value);
        }
        else if (value instanceof BigDecimal)
        {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        }
        else if (value instanceof Boolean)
        {
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof Date)
        {
            cell.setCellValue(((Date) value));
        }
        else if (value instanceof Calendar)
        {
            cell.setCellValue(((Calendar) value));
        }
    }

    private Object extractValue(EJReport report, EJReportDataSource reportDS, EJReportBlock block, EJReportBlockDataSource blockDataSource,
            EJReportScreenItem item)
    {

        if (!report.getActionController().canShowScreenColumn(report, block.getName(), item.getName()))
        {
            return null;
        }
        switch (item.getType())
        {
            case LABEL:
                return item.typeAs(EJReportLabelScreenItem.class).getText();

            case TEXT:
            case DATE:

            case NUMBER:
            {
                EJReportValueBaseScreenItem valueBaseScreenItem = item.typeAs(EJReportValueBaseScreenItem.class);

                String defaultValue = valueBaseScreenItem.getValue();
                if (defaultValue == null || defaultValue.trim().length() == 0)
                {

                    return null;
                }

                String paramTypeCode = defaultValue.substring(0, defaultValue.indexOf(':'));
                String paramValue = defaultValue.substring(defaultValue.indexOf(':') + 1);

                if ("APP_PARAMETER".equals(paramTypeCode))
                {
                    EJApplicationLevelParameter applicationLevelParameter = report.getApplicationLevelParameter(paramValue);
                    if (applicationLevelParameter != null)
                    {
                        return applicationLevelParameter.getValue();
                    }
                }
                else if ("REPORT_PARAMETER".equals(paramTypeCode))
                {
                    EJReportParameter reportParameter = report.getReportParameter(paramValue);
                    if (reportParameter != null)
                    {
                        return reportParameter.getValue();
                    }
                }
                else if ("BLOCK_ITEM".equals(paramTypeCode))
                {

                    return blockDataSource.getFieldValue(paramValue);
                }
                else if ("VARIABLE".equals(paramTypeCode))
                {
                    if (paramValue.equals("CURRENT_DATE"))
                    {
                        return new java.util.Date();
                    }
                    // else if (paramValue.equals("PAGE_NUMBER"))
                    // {
                    // expression.setText("$V{MASTER_CURRENT_PAGE}");
                    // }
                    // else if (paramValue.equals("PAGE_COUNT"))
                    // {
                    // expression.setText("$V{MASTER_TOTAL_PAGES}");
                    // }
                    // else if (paramValue.equals("PAGE_NUMBER_OF_TOTAL_PAGES"))
                    // {
                    // expression.setText("$V{PAGE_NUMBER_OF_TOTAL_PAGES}");
                    // }

                }
                else if ("CLASS_FIELD".equals(paramTypeCode))
                {

                    // expression.setText(paramValue);
                }

            }
                break;
            case IMAGE:
                break;
            case LINE:
                break;
            case RECTANGLE:
                break;
            default:
                break;
        }

        return null;
    }

    public static class CellToColumnInfo
    {
        int cellIndex;
        int numOfCells;
    }
    


}
