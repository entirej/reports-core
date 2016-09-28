package org.entirej.framework.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportScreen
{
    private EJReportBlock                _block;
    private EJCoreReportScreenProperties _properties;
    
    private EJReportScreenChart reportScreenChart;

    public EJReportScreen(EJReportBlock block, EJCoreReportScreenProperties properties)
    {
        _block = block;
        _properties = properties;
        reportScreenChart = new EJReportScreenChart(properties.getChartProperties());
    }

    public String getBlockName()
    {
        return _block.getName();
    }

    
    public EJReportScreenChart getReportScreenChart()
    {
        return reportScreenChart;
    }
    
    
    /**
     * @return Returns the width of this screen
     */
    public int getWidth()
    {
        return _properties.getWidth();
    }

    /**
     * @return Returns the height of this screen
     */
    public int getHeight()
    {
        return _properties.getHeight();
    }

    /**
     * @return Returns the X position of this screen
     */
    public int getXPos()
    {
        return _properties.getX();
    }

    public boolean isNewPage()
    {
        return _properties.isNewPage();
    }

    
    
    public boolean isFitToPage()
    {
        return _properties.isFitToPage();
    }

    /**
     * @return Returns the Y position of this screen
     */
    public int getYPos()
    {
        return _properties.getY();
    }

    public int getDefaultHeaderHeight()
    {
        return _properties.getHeaderColumnHeight();
    }

    public int getDefaultDetailHeight()
    {
        return _properties.getDetailColumnHeight();
    }

    public int getDefaultFooterHeight()
    {
        return _properties.getFooterColumnHeight();
    }

    public String getOddRecotrdVisualAttributeName()
    {
        return _properties.getOddRowVAName();
    }

    public String getEvenRecotrdVisualAttributeName()
    {
        return _properties.getEvenRowVAName();
    }

    public EJReportVisualAttributeProperties getOddRecotrdVisualAttributes()
    {
        return _properties.getOddVAProperties();
    }

    public EJReportVisualAttributeProperties getEvenRecotrdVisualAttributes()
    {
        return _properties.getEvenVAProperties();
    }

    /**
     * Returns the type of this screen
     * 
     * @return The screens type
     */
    public EJReportScreenType getType()
    {
        return _properties.getScreenType();
    }

    public Collection<EJReportScreenItem> getScreenItems()
    {
        ArrayList<EJReportScreenItem> items = new ArrayList<EJReportScreenItem>();
        for (EJReportScreenItemProperties itemProps : _properties.getScreenItems())
        {
            EJReportScreenItem item = new EJReportScreenItem(_block, itemProps);
            items.add(item);
        }
        return items;
    }

    public Collection<EJReportScreenColumn> getScreenColumns()
    {
        ArrayList<EJReportScreenColumn> columns = new ArrayList<EJReportScreenColumn>();
        for (EJCoreReportScreenColumnProperties col : _properties.getColumnContainer().getAllColumnProperties())
        {
            columns.add(new EJReportScreenColumn(_block, col));
        }

        return columns;
    }
    
    public EJReportScreenColumn getScreenColumn(String name)
    {
        
        EJCoreReportScreenColumnProperties col = _properties.getColumnContainer().getColumnProperties(name);
        if(col!=null)
        {
            return new EJReportScreenColumn(_block, col);
        }
        return null;
    }
    
    public EJReportScreenItem getScreenItem(String name)
    {
        
        EJCoreReportScreenItemProperties itemProps = _properties.getScreenItemContainer().getItemProperties(name);
        if(itemProps!=null)
        {
            return new EJReportScreenItem(_block, itemProps);
        }
        return null;
    }

    public EJReportBlock getSubBlock(String blockName)
    {
        return _block.getReport().getBlock(blockName);
    }

    public List<EJReportBlock> getSubBlocks()
    {
        ArrayList<EJReportBlock> blocks = new ArrayList<EJReportBlock>();
        for (EJCoreReportBlockProperties blockProps : _properties.getSubBlocks().getAllBlockProperties())
        {
            EJReportBlock block = _block.getReport().getBlock(blockProps.getName());
            blocks.add(block);
        }
        return blocks;
    }

}
