package org.entirej.framework.report;

import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportColumnProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;

public class EJReportScreen
{
    private EJCoreReportScreenProperties _properties;

    public EJReportScreen(EJCoreReportScreenProperties properties)
    {
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
        ArrayList<EJReportScreenColumn> items = new ArrayList<EJReportScreenColumn>();
        
        for(EJCoreReportColumnProperties column : _properties.getAllColumns())
        {
            EJReportScreenColumn item = new EJReportScreenColumn(column);
            items.add(item);
        }
        return items;

    }

    public EJReportBlock getSubBlock(String blockName)
    {
        for (EJCoreReportBlockProperties subBlock : _properties.getSubBlocks().getAllBlockProperties())
        {
         _properties.get   
        }
        
        EJReportBlock block = new EJReportBlock(block);
        
        return _report.getBlock(blockName);
    }

    public Collection<? extends EJReportBlock> getSubBlocks()
    {

    }

    public Collection<? extends EJReportColumnProperties> getAllColumns();

}
