package org.entirej.report.jasper.builder;

import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportLabelScreenItem;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.EJReportScreen;
import org.entirej.framework.report.EJReportScreenColumn;
import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.data.controllers.EJReportActionController;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportProperties;
import org.entirej.framework.report.interfaces.EJReportProperties.ORIENTATION;

public class EJReportExcelReportBuilder
{
    private static final int COL_SET = 900;

    public void build(EJReport report, EJReportExportType type, String outputFile)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        try
        {

            Collection<EJReportPage> pages = report.getPages();
            for (EJReportPage page : pages)
            {

                Sheet sheet = wb.createSheet(page.getName());

                // turn off grid-lines
                // sheet.setDisplayGridlines(false);
                // sheet.setPrintGridlines(false);
                sheet.setFitToPage(true);
                sheet.setHorizontallyCenter(true);
                PrintSetup printSetup = sheet.getPrintSetup();

                printSetup.setLandscape(report.getProperties().getOrientation() == ORIENTATION.LANDSCAPE);

                fillSheet(report, page, sheet);

            }

            // save generate excel to file
            FileOutputStream out = new FileOutputStream(outputFile);
            wb.write(out);
            out.close();

        }
        catch (Throwable t)
        {
            throw new EJReportRuntimeException(t.getMessage(), t);
        }
        finally
        {
            try
            {
                wb.close();
            }
            catch (IOException e)
            {
                throw new EJReportRuntimeException(e.getMessage(), e);
            }
        }
    }

    private void fillSheet(EJReport report, EJReportPage page, Sheet sheet)
    {

        // TODO HEADER & FOOTER

        int cRaw = 0;

        Collection<EJReportBlock> rootBlocks = page.getRootBlocks();

        // calculate Pixels in grid slot
        sheet.setColumnWidth(0, COL_SET);
        float colInPixels = sheet.getColumnWidthInPixels(0);

        EJReportProperties properties = report.getProperties();
        int pageWidth = properties.getReportWidth() - (properties.getMarginLeft() + properties.getMarginRight());

        // setup grid columns
        int colCount = (int) (pageWidth / colInPixels) + 1;// add offset
        for (int i = 0; i < colCount; i++)
        {
            sheet.setColumnWidth(i, 900);

        }

        for (EJReportBlock block : rootBlocks)
        {
            EJReportScreen screen = block.getScreen();
            if (screen.getType() == EJReportScreenType.TABLE_LAYOUT)
            {
                cRaw = buildTableLayout(report, block, screen, sheet, cRaw, colInPixels,colCount);
            }
        }

    }

    private int buildTableLayout(EJReport report, EJReportBlock block, EJReportScreen screen, Sheet sheet, int cRaw, float colInPixels,int colCount)
    {
        List<EJReportScreenColumn> allColumns = new ArrayList<EJReportScreenColumn>();
        EJReportActionController controller = block.getReport().getActionController();

        for (EJReportScreenColumn column : screen.getScreenColumns())
        {
            if (column.isVisible() && controller.canShowScreenColumn(block.getReport(), block.getName(), column.getName()))
            {
                allColumns.add(column);
            }
        }

        // create all ref fields

        boolean addHeaderBand = false;
        boolean addFooterBand = false;
        int headerHeight = screen.getDefaultHeaderHeight();
        int detailHeight = screen.getDefaultDetailHeight();
        int footerHeight = screen.getDefaultFooterHeight();

        boolean canShowBlockHeader = block.getReport().getActionController().canShowBlockHeader(block.getReport(), block.getName());
        boolean canShowBlockFooter = block.getReport().getActionController().canShowBlockFooter(block.getReport(), block.getName());

        for (EJReportScreenColumn col : allColumns)
        {

            if (!col.isVisible())
                continue;

            if (canShowBlockHeader && col.showHeader())
            {
                if (headerHeight < col.getHeaderSection().getHeight())
                {
                    headerHeight = col.getHeaderSection().getHeight();
                }
                addHeaderBand = true;
                for (EJReportScreenItem item : col.getHeaderSection().getScreenItems())
                {
                    if (!item.isVisible())
                        continue;
                    int height = item.getHeight();
                    if (item.isHeightAsPercentage())
                    {
                        height = (int) (((double) screen.getDefaultHeaderHeight() / 100) * height);
                    }
                    if (headerHeight < (item.getYPos() + height))
                    {
                        headerHeight = (item.getYPos() + height);
                    }

                }
            }

            // details---
            if (detailHeight < col.getDetailSection().getHeight())
            {
                detailHeight = col.getDetailSection().getHeight();
            }
            for (EJReportScreenItem item : col.getDetailSection().getScreenItems())
            {
                if (!item.isVisible())
                    continue;
                int height = item.getHeight();
                if (item.isHeightAsPercentage())
                {
                    height = (int) (((double) screen.getDefaultDetailHeight() / 100) * height);
                }
                if (detailHeight < (item.getYPos() + height))
                {
                    detailHeight = (item.getYPos() + height);
                }

            }
            // ----------
            if (canShowBlockFooter && col.showFooter())
            {
                if (footerHeight < col.getFooterSection().getHeight())
                {
                    footerHeight = col.getFooterSection().getHeight();
                }
                addFooterBand = true;
                for (EJReportScreenItem item : col.getFooterSection().getScreenItems())
                {
                    if (!item.isVisible())
                        continue;
                    int height = item.getHeight();
                    if (item.isHeightAsPercentage())
                    {
                        height = (int) (((double) screen.getDefaultFooterHeight() / 100) * height);
                    }
                    if (footerHeight < (item.getYPos() + height))
                    {
                        footerHeight = (item.getYPos() + height);
                    }

                }
            }

        }

  
        if (addHeaderBand)
        {

            
            Row row = sheet.createRow(cRaw);
            for (int i = 0; i < colCount; i++)
            {
                row.createCell(i);

            }
           // row.setHeight((short)(headerHeight/colInPixels));
            
        
            int cColumn = 0;
            for (EJReportScreenColumn col : allColumns)
            {

                if (!col.isVisible())
                    continue;
                
                final int width = col.getWidth();
                
                
                int coLUnits = Math.round(width/colInPixels);
                if(coLUnits==0)
                    coLUnits = 1;
                
                
                sheet.addMergedRegion(new CellRangeAddress(cRaw, cRaw, cColumn, cColumn+coLUnits));
               
               
               
                
                
                boolean screenColumnSectionH = canShowBlockHeader
                        && block.getReport().getActionController()
                                .canShowScreenColumnSection(block.getReport(), block.getName(), col.getName(), EJReportScreenSection.HEADER);

             

                if (canShowBlockHeader && col.showHeader())
                {

                    @SuppressWarnings("unchecked")
                    Collection<EJReportScreenItem> screenItems = (Collection<EJReportScreenItem>) col.getHeaderSection().getScreenItems();

                    int sectionHeight = col.getHeaderSection().getHeight();
                    if (sectionHeight == 0)
                        sectionHeight = screen.getDefaultHeaderHeight();
                    if (screenItems.size() == 0)
                        continue;
                    if (screenItems.size() > 1)
                    {
                        throw new EJReportRuntimeException("Only one Screen Item for Section support by EJReportExcelReportBuilder");
                    }

                    for (EJReportScreenItem item : screenItems)
                    {
                        if (!item.isVisible())
                        {
                            continue;
                        }
                        int itemWidth = item.getWidth();
                        int itemHeight = item.getHeight();

                        if (item.isWidthAsPercentage())
                        {
                            itemWidth = (int) (((double) width / 100) * itemWidth);
                        }
                        if (item.isHeightAsPercentage())
                        {
                            itemHeight = (int) (((double) sectionHeight / 100) * itemHeight);
                        }

                        if (width < (item.getXPos() + itemWidth))
                        {
                            itemWidth = itemWidth - ((item.getXPos() + itemWidth) - width);
                            if (itemWidth < 0)
                                itemWidth = 0;
                        }

                        if (screenColumnSectionH)
                        {

                            Cell cell = row.getCell(cColumn);
                            EJReportLabelScreenItem labelItem = item.typeAs(EJReportLabelScreenItem.class);
                            cell.setCellValue(labelItem.getText());
                            
//                            JRDesignElement element = createScreenItem(block, item);
//
//                            if (element != null)
//                            {
//
//                                element.setX(currentX + item.getXPos());
//                                element.setY(item.getYPos());
//                                element.setWidth(itemWidth);
//
//                                element.setHeight(itemHeight);
//                                header.addElement(element);
//
//                                processItemStyle(item, element, EJReportScreenSection.HEADER);
//
//                                element.setPositionType(PositionTypeEnum.FLOAT);
//                                element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
//                            }
                        }

                    }
                    
                    

                }
                cColumn+=coLUnits+1;
                
                
            }

            
        }

        return cRaw;
    }
}
