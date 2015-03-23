package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.enumerations.EJReportScreenRotation;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportLabelScreenItem  extends EJReportAlignmentBaseScreenItem  implements EJReportRotatableScreenItem
{
    EJCoreReportScreenItemProperties.Label item;

    public EJReportLabelScreenItem(EJCoreReportScreenItemProperties.Label item)
    {
        super(item);
        this.item = item;
    }

    
    
    public EJReportVisualAttributeProperties getVisualAttributeProperties()
    {
        return item.getVisualAttributeProperties();
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

    public EJReportScreenItemType getType()
    {
        return item.getType();
    }

    public EJReportScreenRotation getRotation()
    {
        return item.getRotation();
    }

    public EJReportScreenAlignment getHAlignment()
    {
        return item.getHAlignment();
    }

    public EJReportScreenAlignment getVAlignment()
    {
        return item.getVAlignment();
    }



    public String getText()
    {
        return item.getText();
    }

    
    
    

}