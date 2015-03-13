package org.entirej.framework.report.properties;

import java.util.Collection;

import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.interfaces.EJReportBorderProperties;

public class EJCoreReportScreenColumnSectionProperties implements EJReportBorderProperties
{
    private EJCoreReportBlockProperties              _blockProperties;
    private EJReportScreenSection                    _section;
    private int                                      height    = 0;
    private LineStyle                                lineStyle = LineStyle.SOLID;
    private double                                   lineWidth = 1;
    private boolean                                  showTopLine;
    private boolean                                  showBottomLine;
    private boolean                                  showLeftLine;
    private boolean                                  showRightLine;
    private String                                   visualAttributeName;

    private EJReportScreenColumnSectionItemContainer _screenItemContainer;

    public EJCoreReportScreenColumnSectionProperties(EJCoreReportBlockProperties blockProperties, EJReportScreenSection section)
    {
        _blockProperties = blockProperties;
        _section = section;
        _screenItemContainer = new EJReportScreenColumnSectionItemContainer(blockProperties, this);
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public EJReportScreenSection getSection()
    {
        return _section;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public LineStyle getLineStyle()
    {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle)
    {
        this.lineStyle = lineStyle;
    }

    public double getLineWidth()
    {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth)
    {
        this.lineWidth = lineWidth;
    }

    public boolean showTopLine()
    {
        return showTopLine;
    }

    public void setShowTopLine(boolean showTopLine)
    {
        this.showTopLine = showTopLine;
    }

    public boolean showBottomLine()
    {
        return showBottomLine;
    }

    public void setShowBottomLine(boolean showBottomLine)
    {
        this.showBottomLine = showBottomLine;
    }

    public boolean showLeftLine()
    {
        return showLeftLine;
    }

    public void setShowLeftLine(boolean showLeftLine)
    {
        this.showLeftLine = showLeftLine;
    }

    public boolean showRightLine()
    {
        return showRightLine;
    }

    public void setShowRightLine(boolean showRightLine)
    {
        this.showRightLine = showRightLine;
    }

    public String getVisualAttributeName()
    {
        return visualAttributeName;
    }

    public void setVisualAttributeName(String visualAttributeName)
    {
        this.visualAttributeName = visualAttributeName;
    }

    @Override
    public EJReportVisualAttributeProperties getVisualAttributeProperties()
    {

        return EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(visualAttributeName);
    }

    public Collection<EJCoreReportScreenItemProperties> getScreenItems()
    {
        return _screenItemContainer.getAllItemProperties();
    }

    public EJReportScreenColumnSectionItemContainer getSectionItemContainer()
    {
        return _screenItemContainer;
    }

}
