package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.ValueBaseItem;

public class EJReportValueBaseScreenItem extends EJReportAlignmentBaseScreenItem
{

    ValueBaseItem item;

    public EJReportValueBaseScreenItem(ValueBaseItem item)
    {
        super(item);
        this.item = item;
    }

    public EJReportVisualAttributeProperties getVisualAttributeProperties()
    {
        return item.getVisualAttributeProperties();
    }

    public EJReportScreenItemType getType()
    {
        return item.getType();
    }

    public int getWidth()
    {
        return item.getWidth();
    }

    public int getHeight()
    {
        return item.getHeight();
    }

    public boolean isWidthAsPercentage()
    {
        return item.isWidthAsPercentage();
    }

    public boolean isHeightAsPercentage()
    {
        return item.isHeightAsPercentage();
    }

    public int getX()
    {
        return item.getX();
    }

    public int getY()
    {
        return item.getY();
    }

    public boolean isVisible()
    {
        return item.isVisible();
    }

    public String getVisualAttributeName()
    {
        return item.getVisualAttributeName();
    }

    public String getName()
    {
        return item.getName();
    }

    public EJReportScreenAlignment getHAlignment()
    {
        return item.getHAlignment();
    }

    public EJReportScreenAlignment getVAlignment()
    {
        return item.getVAlignment();
    }

    public String getValue()
    {
        return item.getValue();
    }

    public boolean isExpandToFit()
    {
        return item.isExpandToFit();
    }

    public EJReportMarkupType getMarkup()
    {
        return item.getMarkup();
    }
    
    
}