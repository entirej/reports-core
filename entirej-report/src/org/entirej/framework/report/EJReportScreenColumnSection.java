package org.entirej.framework.report;

import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.interfaces.EJReportBorderProperties.LineStyle;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnSectionProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportScreenColumnSection
{
    private EJReportBlock                             _block;
    private EJCoreReportScreenColumnSectionProperties _properties;

    public EJReportScreenColumnSection(EJReportBlock block, EJCoreReportScreenColumnSectionProperties properties)
    {
        _properties = properties;
        _block = block;
    }

    public EJReportScreenSection getSection()
    {
        return _properties.getSection();
    }

    public int getHeight()
    {
        return _properties.getHeight();
    }

    public LineStyle getLineStyle()
    {
        return _properties.getLineStyle();
    }

    public double getLineWidth()
    {
        return _properties.getLineWidth();
    }

    public boolean showTopLine()
    {
        return _properties.showTopLine();
    }

    public boolean showBottomLine()
    {
        return _properties.showBottomLine();
    }

    public boolean showLeftLine()
    {
        return _properties.showLeftLine();
    }

    public boolean showRightLine()
    {
        return _properties.showRightLine();
    }

    public String getLineVisualAttributeName()
    {
        return _properties.getLineVisualAttributeName();
    }

    public EJReportVisualAttributeProperties getLineVisualAttributes()
    {
        return _properties.getLineVisualAttributeProperties();
    }

    public Collection<EJReportScreenItem> getScreenItems()
    {
        ArrayList<EJReportScreenItem> items = new ArrayList<EJReportScreenItem>();

        for (EJCoreReportScreenItemProperties item : _properties.getScreenItems())
        {
            items.add(new EJReportScreenItem(_block, item));
        }
        return items;
    }
    

    public EJReportScreenItem getScreenItem(String name)
    {
        
        for (EJCoreReportScreenItemProperties item : _properties.getScreenItems())
        {
            if(item.getName().equals(name))
            {
               return  new EJReportScreenItem(_block, item);
            }
        }
        return null;
    }

}
