package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.interfaces.EJReportBorderProperties.LineStyle;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.AlignmentBaseItem;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportAlignmentBaseScreenItem
{

    AlignmentBaseItem item;

    public EJReportAlignmentBaseScreenItem(AlignmentBaseItem item)
    {
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

    public int getRightPadding()
    {
        return item.getRightPadding();
    }

    public int getLeftPadding()
    {
        return item.getLeftPadding();
    }

    public LineStyle getLineStyle()
    {
        return item.getLineStyle();
    }

    public double getLineWidth()
    {
        return item.getLineWidth();
    }

    public boolean showTopLine()
    {
        return item.showTopLine();
    }

    public boolean showBottomLine()
    {
        return item.showBottomLine();
    }

    public boolean showLeftLine()
    {
        return item.showLeftLine();
    }

    public boolean showRightLine()
    {
        return item.showRightLine();
    }

    public EJReportVisualAttributeProperties getLineVisualAttributes()
    {
        return item.getLineVisualAttributeProperties();
    }

    
    
    
     
    
    
}