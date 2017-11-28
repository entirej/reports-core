package org.entirej.report.poi.layout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.util.DateFormatConverter;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportAlignmentBaseScreenItem;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportDateScreenItem;
import org.entirej.framework.report.EJReportNumberScreenItem;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportScreen;
import org.entirej.framework.report.EJReportScreenColumn;
import org.entirej.framework.report.EJReportScreenColumnSection;
import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Date.DateFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Number.NumberFormats;

public class EJReportPOIPage implements IBlockParent
{
    private List<EJReportPOIBlock> allblocks    = new ArrayList<EJReportPOIBlock>();
    private List<EJReportPOIBlock> blocks    = new ArrayList<EJReportPOIBlock>();
    private Map<Integer, Integer>  colWidths = new HashMap<Integer, Integer>();

    public void addBlock(EJReportPOIBlock block)
    {
        blocks.add(block);

    }

    public Map<Integer, Integer> getColWidths()
    {
        return colWidths;
    }

    public List<EJReportPOIBlock> getBlocks()
    {
        return new ArrayList<EJReportPOIBlock>(blocks);
    }

    public EJReportPOIBlock getBlock(String blockName)
    {
        for (EJReportPOIBlock poiBlock : blocks)
        {
            if (poiBlock.getBlock().getName().equals(blockName))
            {
                return poiBlock;
            }
        }
        return null;
    }

    public void build(EJReport report, EJReportPage page)
    {

        Collection<EJReportBlock> headerBlocks = report.getHeaderBlocks();
        for (EJReportBlock block : headerBlocks)
        {
            buildBlockMapping(report,this, block);
        }

        // support first level blocks
        Collection<EJReportBlock> rootBlocks = page.getRootBlocks();

        for (EJReportBlock block : rootBlocks)
        {
            buildBlockMapping(report,this, block);

        }

        Collection<EJReportBlock> footerBlocks = report.getFooterBlocks();
        for (EJReportBlock block : footerBlocks)
        {
            buildBlockMapping(report,this, block);
        }
        // map columns
        colMapping();
    }

    private void buildBlockMapping(EJReport report,IBlockParent blockParent, EJReportBlock block)
    {
        if (report.getActionController().canShowBlock(report, block.getName()))
        {

            EJReportPOIBlock poiBlock = new EJReportPOIBlock(block);
            blockParent.addBlock(poiBlock);
            allblocks.add(poiBlock);

            EJReportScreen screen = block.getScreen();
            // support table layout
            if (screen.getType() == EJReportScreenType.TABLE_LAYOUT)
            {
                buildTableLayout(report, block, poiBlock, screen);
                List<EJReportBlock> subBlocks = block.getScreen().getSubBlocks();
                for (EJReportBlock subBlock : subBlocks)
                {
                    buildBlockMapping(report, poiBlock, subBlock);
                }
            }
            else if (screen.getType() == EJReportScreenType.FORM_LAYOUT)
            {
                buildFormLayout(report, block, poiBlock, screen);
                List<EJReportBlock> subBlocks = block.getScreen().getSubBlocks();
                for (EJReportBlock subBlock : subBlocks)
                {
                    buildBlockMapping(report, poiBlock, subBlock);
                }
            }

        }
    }

    private String extractDefaultPattern(EJReportScreenItem item, Locale defaultLocale)
    {
        switch (item.getType())
        {
            case NUMBER:
            {
                EJReportNumberScreenItem textItem = item.typeAs(EJReportNumberScreenItem.class);

                if (textItem.getManualFormat() != null && !textItem.getManualFormat().isEmpty())
                {
                    return (textItem.getManualFormat());
                }
                else
                {
                    NumberFormats localeFormat = textItem.getLocaleFormat();
                    switch (localeFormat)
                    {
                        case CURRENCY:
                            return (((java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance(defaultLocale)).toPattern());

                        case PERCENT:
                            return (((java.text.DecimalFormat) java.text.NumberFormat.getPercentInstance(defaultLocale)).toPattern());

                        case INTEGER:
                            return (((java.text.DecimalFormat) java.text.NumberFormat.getIntegerInstance(defaultLocale)).toPattern());

                        case NUMBER:
                            return (((java.text.DecimalFormat) java.text.NumberFormat.getNumberInstance(defaultLocale)).toPattern());

                        default:
                            break;
                    }
                }

            }
                break;
            case DATE:
            {
                EJReportDateScreenItem textItem = item.typeAs(EJReportDateScreenItem.class);

                if (textItem.getManualFormat() != null && !textItem.getManualFormat().isEmpty())
                {
                    return (textItem.getManualFormat());
                }
                else
                {
                    DateFormats localeFormat = textItem.getLocaleFormat();
                    SimpleDateFormat dateFormat = null;
                    switch (localeFormat)
                    {
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
                    }
                    if (dateFormat != null)
                    {
                        return DateFormatConverter.convert(defaultLocale, dateFormat);
                    }
                }

            }
                break;
        }
        return null;
    }

    private void colMapping()
    {

        int maxRawWidth = 0;

        // find max raw width
        for (EJReportPOIBlock block : allblocks)
        {
            for (EJReportPOIRaw raw : block.getRaws())
            {
                if (maxRawWidth < raw.getWidth())
                {
                    maxRawWidth = raw.getWidth();
                }
            }
        }

        // store possible col widths
        int x = 0;
        int colindex1 = 0;

        // build column widths
        while (maxRawWidth > x)
        {
            int minPossibleWidth = findSmallestElement(x);

            colWidths.put(colindex1, minPossibleWidth);
            x += minPossibleWidth;
            colindex1++;
        }

        // map elements to possible columns
        mapColumnsToElemnts();
    }

    void mapColumnsToElemnts()
    {
        for (EJReportPOIBlock block : allblocks)
        {
            for (EJReportPOIRaw raw : block.getRaws())
            {
                List<EJReportPOIElement> elements = raw.getElements();
                for (EJReportPOIElement element : elements)
                {
                    int x = element.getX();
                    int width = element.getWidth();

                    int currentX = 0;
                    for (int col = 0; col < colWidths.size(); col++)
                    {
                        if (currentX == x)
                        {
                            element.setStartCell(col);
                        }
                        currentX += colWidths.get(col);

                        if (currentX == (x + width))
                        {
                            element.setEndCell(col);
                            break;
                        }
                    }
                }
            }
        }

    }

    int findSmallestElement(int currentx)
    {
        int size = -1;
        for (EJReportPOIBlock block : allblocks)
        {
            for (EJReportPOIRaw raw : block.getRaws())
            {
                List<EJReportPOIElement> elements = raw.getElements();
                for (EJReportPOIElement element : elements)
                {
                    if ((element.getX() <= currentx && currentx < (element.getX() + element.getWidth())))
                    {
                        int possibleWidth = (element.getX() + element.getWidth()) - currentx;
                        if (size == -1 || size > possibleWidth)
                        {
                            size = possibleWidth;
                        }
                    }
                }
            }
        }

        return size;
    }

    private void buildFormLayout(EJReport report, EJReportBlock block, EJReportPOIBlock poiBlock, EJReportScreen screen)
    {

        int width = screen.getWidth();
        int height = screen.getHeight();

        // raw height and width calculation
        for (EJReportScreenItem item : screen.getScreenItems())
        {

            if (!item.isVisible())
            {
                continue;
            }
            int itemWidth = item.getWidth();
            int itemHeight = item.getHeight();

            if (item.isWidthAsPercentage())
            {
                itemWidth = (int) Math.round(((double) screen.getWidth() / 100) * itemWidth);
            }
            if (item.isHeightAsPercentage())
            {
                itemHeight = (int) Math.round(((double) screen.getHeight() / 100) * itemHeight);
            }

            if (width < (item.getXPos() + itemWidth))
            {
                width = (item.getXPos() + itemWidth);
            }

            if (height < (item.getYPos() + itemHeight))
            {

                height = (item.getYPos() + itemHeight);
            }

        }
        int y = 0;
        int rawindex1 = 0;
        Map<Integer, Integer> rawheights = new HashMap<Integer, Integer>();
        while (y < height)
        {

            int nextY = -1;
            for (EJReportScreenItem item : screen.getScreenItems())
            {

                if (!item.isVisible())
                {
                    continue;
                }

                if (nextY == -1 || nextY > item.getYPos())
                {
                    if (item.getYPos() >= y)
                    {
                        nextY = item.getYPos();
                    }

                }

            }
            if (nextY > y)
            {
                int gap = nextY - y;
                rawheights.put(rawindex1, gap);
                y += gap;
                rawindex1++;
            }

            int minheight = -1;
            for (EJReportScreenItem item : screen.getScreenItems())
            {

                if (!item.isVisible())
                {
                    continue;
                }

                if ((item.getYPos() >= y && y < (item.getYPos() + item.getHeight())))
                {
                    int possibleHeight = (item.getYPos() + item.getHeight()) - y;
                    if (minheight == -1 || minheight > possibleHeight)
                    {
                        minheight = possibleHeight;
                    }
                }

            }
            if (minheight == -1)
            {

                minheight = 15;// empty raw
            }
            rawheights.put(rawindex1, minheight);
            y += minheight;
            rawindex1++;

        }
        // create raws
        for (Entry<Integer, Integer> entry : rawheights.entrySet())
        {
            EJReportPOIRaw detail = new EJReportPOIRaw(EJReportPOIRaw.Type.DETAIL);
            Integer rawHeight = entry.getValue();
            detail.setHeight(rawHeight);
            detail.setWidth(width);

            poiBlock.addRaw(detail);
        }
        // map screen items
        for (EJReportScreenItem item : screen.getScreenItems())
        {

            if (!item.isVisible())
            {
                continue;
            }
            int ix = item.getXPos();
            int iwidth = item.getWidth();
            int iy = item.getYPos();
            int iheight = item.getHeight();
            if (item.isWidthAsPercentage())
            {
                iwidth = (int) (((double) screen.getWidth() / 100) * iwidth);
            }
            if (item.isHeightAsPercentage())
            {
                iheight = (int) (((double) screen.getHeight() / 100) * iheight);
            }
            EJReportPOIElement element = new EJReportPOIElement(item);
            element.setSupportMerge(true);
            element.setWidth(iwidth);
            element.setX(ix);
            element.setVa(item.getVisualAttributes());
            element.setDefaultPattren(extractDefaultPattern(item, block.getReport().getCurrentLocale()));
            
            element.setAlignment(getAlignment(item));

            EJReportPOIBorder border = getBorder(item);
            if(border!=null)
                element.setBorder(border);
            
            
            int currentY = 0;
            int elmRaw = 0;
            for (int raw = 0; raw < rawheights.size(); raw++)
            {
                if (currentY == iy)
                {
                    elmRaw = raw;
                    poiBlock.getRaws().get(raw).addElement(element);
                }
                currentY += rawheights.get(raw);

                if (currentY == (iy + iheight))
                {
                    element.setRawSpan(raw - elmRaw);
                    break;
                }
            }

        }

        // fill gaps in raw elements
        for (EJReportPOIRaw poiRaw : poiBlock.getRaws())
        {
            List<EJReportPOIElement> elements = poiRaw.getElements();
            if (elements.isEmpty())
            {
                // fill the raw
                EJReportPOIElement empty = new EJReportPOIElement(null);
                empty.setSupportMerge(true);
                empty.setWidth(poiRaw.getWidth());
                poiRaw.addElement(empty);
                continue;
            }
            int currentX = 0;
            int closetX = 0;
            while (currentX < poiRaw.getWidth())
            {
                boolean hasElm = false;
                for (EJReportPOIElement element : elements)
                {
                    if (element.getX() < currentX)
                    {
                        continue;
                    }
                    hasElm = true;
                    if (element.getX() == currentX)
                    {
                        closetX = element.getX() + element.getWidth();
                        currentX += element.getWidth();
                        break;
                    }
                    if (element.getX() < closetX)
                    {
                        closetX = element.getX();
                    }
                }
                if (closetX > currentX)
                {
                    EJReportPOIElement element = new EJReportPOIElement(null);
                    element.setSupportMerge(true);
                    element.setWidth(closetX - currentX);
                    element.setX(currentX);
                    poiRaw.addElement(element);
                    currentX = closetX;
                }

                closetX = currentX;
                if (!hasElm)
                {
                    break;
                }
            }

        }

    }

    private EJReportPOIAlignment getAlignment(EJReportScreenItem item)
    {
        switch (item.getType())
        {
            case DATE:
            case LABEL:
            case TEXT:
            case NUMBER:
            case IMAGE:
                EJReportAlignmentBaseScreenItem typeAs = item.typeAs(EJReportAlignmentBaseScreenItem.class);
                if(typeAs != null)
                {
                    EJReportPOIAlignment alignment = new EJReportPOIAlignment();
                    alignment.setHAlignment(typeAs.getHAlignment());
                    alignment.setVAlignment(typeAs.getVAlignment());
                    return alignment;
                }
                break;

            default:
                break;
        }
        
       
        
        return null;
    }
    private EJReportPOIBorder getBorder(EJReportScreenItem item)
    {
        switch (item.getType())
        {
            case DATE:
            case LABEL:
            case TEXT:
            case NUMBER:
            case IMAGE:
                EJReportAlignmentBaseScreenItem typeAs = item.typeAs(EJReportAlignmentBaseScreenItem.class);
                if(typeAs != null)
                {
                   
                    if(typeAs.showLeftLine()|| typeAs.showRightLine()||typeAs.showTopLine()|| typeAs.showBottomLine())
                        
                    {
                        EJReportPOIBorder border = new EJReportPOIBorder();
                        border.setShowBottomLine(typeAs.showBottomLine());
                        border.setShowTopLine(typeAs.showTopLine());
                        border.setShowLeftLine(typeAs.showLeftLine());
                        border.setShowRightLine(typeAs.showRightLine());
                        border.setVisualAttribute(typeAs.getLineVisualAttributes());
                        border.setLineStyle(typeAs.getLineStyle());
                        border.setLineWidth(typeAs.getLineWidth());
                        return border;
                    }
                }
                break;
                
            default:
                break;
        }
        
        
        
        return null;
    }

    private void buildTableLayout(EJReport report, EJReportBlock block, EJReportPOIBlock poiBlock, EJReportScreen screen)
    {
        List<EJReportScreenColumn> allColumns = new ArrayList<EJReportScreenColumn>();

        // filter columns
        for (EJReportScreenColumn column : screen.getScreenColumns())
        {
            if (column.isVisible())
            {
                allColumns.add(column);
            }
        }

        boolean addHeaderBand = false;
        boolean addFooterBand = false;
        int headerHeight = screen.getDefaultHeaderHeight();
        int detailHeight = screen.getDefaultDetailHeight();
        int footerHeight = screen.getDefaultFooterHeight();
        int rawWidth = 0;

        boolean canShowBlockHeader = block.getReport().getActionController().canShowBlockHeader(block.getReport(), block.getName());
        boolean canShowBlockFooter = block.getReport().getActionController().canShowBlockFooter(block.getReport(), block.getName());

        for (EJReportScreenColumn col : allColumns)
        {

            if (canShowBlockHeader && col.showHeader())
            {
                if (headerHeight < col.getHeaderSection().getHeight())
                {
                    headerHeight = col.getHeaderSection().getHeight();
                }
                addHeaderBand = true;

            }
            rawWidth += col.getWidth();
            // details---
            if (detailHeight < col.getDetailSection().getHeight())
            {
                detailHeight = col.getDetailSection().getHeight();
            }

            // ----------
            if (canShowBlockFooter && col.showFooter())
            {
                if (footerHeight < col.getFooterSection().getHeight())
                {
                    footerHeight = col.getFooterSection().getHeight();
                }
                addFooterBand = true;

            }
        }

        EJReportPOIRaw header = null;
        EJReportPOIRaw footer = null;

        if (addHeaderBand)
        {
            header = new EJReportPOIRaw(EJReportPOIRaw.Type.HEADER);
            poiBlock.addRaw(header);
            header.setHeight(headerHeight);
            header.setWidth(rawWidth);
        }

        EJReportPOIRaw detail = new EJReportPOIRaw(EJReportPOIRaw.Type.DETAIL);
        detail.setHeight(detailHeight);
        detail.setWidth(rawWidth);
        poiBlock.addRaw(detail);
        if (addFooterBand)
        {
            footer = new EJReportPOIRaw(EJReportPOIRaw.Type.FOOTER);
            footer.setHeight(footerHeight);
            footer.setWidth(rawWidth);
            poiBlock.addRaw(footer);
        }

        int x = 0;
        for (EJReportScreenColumn col : allColumns)
        {
            int nextX = x;
            int width = col.getWidth();
            x += width;
            if (addHeaderBand)
            {
                if (canShowBlockHeader && col.showHeader())
                {

                    EJReportScreenColumnSection section = col.getHeaderSection();
                    Collection<EJReportScreenItem> screenItems = section.getScreenItems();

                    boolean added = false;
                    for (EJReportScreenItem item : screenItems)
                    {
                        if (item.isVisible() && report.getActionController().canShowScreenItem(report, block.getName(), item.getName(), EJReportScreenSection.HEADER))
                        {

                            if(added)
                            {
                                System.err.println(block.getName()+":POI Runnner only support one Screen item on table layout column. only first one will used and rest will ignore!");
                                break;
                            }
                            
                            EJReportPOIElement element = new EJReportPOIElement(item);
                            element.setAlignment(getAlignment(item));
                            
                            element.setWidth(width);

                            element.setVa(item.getVisualAttributes());
                            element.setDefaultPattren(extractDefaultPattern(item, block.getReport().getCurrentLocale()));

                            element.setX(nextX);
                            
                            
                            //column base border
                            if(section.showLeftLine()|| section.showRightLine()||section.showTopLine()|| section.showBottomLine())
                            {
                                
                                EJReportPOIBorder border = createPOIBorder(section);
                                element.setBorder(border);
                                
                            }
                            EJReportPOIBorder border = getBorder(item);
                            if(border!=null)
                                element.setBorder(border);
                           
                            
                            
                            
                            header.addElement(element);

                            added = true;
                        }
                    }

                }
            }
            // detail section

            {
                EJReportScreenColumnSection section = col.getDetailSection();
                Collection<EJReportScreenItem> screenItems = section.getScreenItems();

                boolean added =false;
                for (EJReportScreenItem item : screenItems)
                {
                    if (item.isVisible() && report.getActionController().canShowScreenItem(report, block.getName(), item.getName(), EJReportScreenSection.DETAIL))
                    {
                        if(added)
                        {
                            System.err.println(block.getName()+":POI Runnner only support one Screen item on table layout column. only first one will used and rest will ignore!");
                            break;
                        }
                        EJReportPOIElement element = new EJReportPOIElement(item);
                        element.setAlignment(getAlignment(item));
                        element.setWidth(width);

                        element.setVa(item.getVisualAttributes());
                        element.setDefaultPattren(extractDefaultPattern(item, block.getReport().getCurrentLocale()));

                        element.setX(nextX);
                        if(section.showLeftLine()|| section.showRightLine()||section.showTopLine()|| section.showBottomLine())
                        {
                            
                            EJReportPOIBorder border = createPOIBorder(section);
                            element.setBorder(border);
                            
                        }
                        EJReportPOIBorder border = getBorder(item);
                        if(border!=null)
                            element.setBorder(border);
                        detail.addElement(element);
                        

                       added = true;
                    }
                }
            }
            if (addFooterBand)
            {
                if (canShowBlockFooter && col.showFooter())
                {

                    EJReportScreenColumnSection section = col.getFooterSection();
                    Collection<EJReportScreenItem> screenItems = section.getScreenItems();
                    boolean added = false;
                    for (EJReportScreenItem item : screenItems)
                    {
                        if (item.isVisible() && report.getActionController().canShowScreenItem(report, block.getName(), item.getName(), EJReportScreenSection.FOOTER))
                        {
                            if(added)
                            {
                                System.err.println(block.getName()+": POI Runnner only support one Screen item on table layout column. only first one will used and rest will ignore!");
                                break;
                            }
                            EJReportPOIElement element = new EJReportPOIElement(item);
                            element.setAlignment(getAlignment(item));
                            element.setWidth(width);

                            element.setVa(item.getVisualAttributes());
                            element.setDefaultPattren(extractDefaultPattern(item, block.getReport().getCurrentLocale()));

                            element.setX(nextX);
                            if(section.showLeftLine()|| section.showRightLine()||section.showTopLine()|| section.showBottomLine())
                            {
                                
                                EJReportPOIBorder border = createPOIBorder(section);
                                element.setBorder(border);
                                
                            }
                            EJReportPOIBorder border = getBorder(item);
                            if(border!=null)
                                element.setBorder(border);
                            footer.addElement(element);

                            added=true;
                        }
                    }

                }
            }

        }
    }

    private EJReportPOIBorder createPOIBorder(EJReportScreenColumnSection section)
    {
        EJReportPOIBorder border = new EJReportPOIBorder();
        border.setShowBottomLine(section.showBottomLine());
        border.setShowTopLine(section.showTopLine());
        border.setShowLeftLine(section.showLeftLine());
        border.setShowRightLine(section.showRightLine());
        border.setVisualAttribute(section.getLineVisualAttributes());
        border.setLineStyle(section.getLineStyle());
        border.setLineWidth(section.getLineWidth());
        return border;
    }

}
