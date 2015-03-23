package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineStyle;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportRectangleScreenItem 
{
    EJCoreReportScreenItemProperties.Rectangle item;

    public EJReportRectangleScreenItem(EJCoreReportScreenItemProperties.Rectangle item)
    {
       
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



    public double getLineWidth()
    {
        return item.getLineWidth();
    }



    public LineStyle getLineStyle()
    {
        return item.getLineStyle();
    }



    public int getRadius()
    {
        return item.getRadius();
    }



   
    
    
    
    

}