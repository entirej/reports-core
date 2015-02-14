package org.entirej.framework.report;

import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;

public class EJReportScreen
{
    private EJReportBlock                _block;
    private EJCoreReportScreenProperties _properties;

    public EJReportScreen(EJReportBlock block, EJCoreReportScreenProperties properties)
    {
        _block = block;
        _properties = properties;
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

    /**
     * @return Returns the Y position of this screen
     */
    public int getYPos()
    {
        return _properties.getY();
    }

    /**
     * Returns the type of this screen
     * 
     * @return The screens type
     */
    public EJReportScreenType getScreenType()
    {
        return _properties.getScreenType();
    }

    public Collection<? extends EJReportScreenItem> getScreenItems()
    {
        ArrayList<EJReportScreenItem> items = new ArrayList<EJReportScreenItem>();
        for (EJReportScreenItemProperties itemProps : _properties.getScreenItems())
        {
            EJReportScreenItem item = new EJReportScreenItem(itemProps);
            items.add(item);
        }
        return items;
    }

    public Collection<? extends EJReportScreenColumn> getScreenColumns()
    {
        ArrayList<EJReportScreenColumn> columns = new ArrayList<EJReportScreenColumn>();
        for (EJCoreReportScreenColumnProperties col : _properties.getColumnContainer().getAllColumnProperties())
        {
            columns.add(new EJReportScreenColumn(col));
        }

        return columns;
    }

    public EJReportBlock getSubBlock(String blockName)
    {
        return _block.getReport().getBlock(blockName);
    }

    public Collection<? extends EJReportBlock> getSubBlocks()
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
