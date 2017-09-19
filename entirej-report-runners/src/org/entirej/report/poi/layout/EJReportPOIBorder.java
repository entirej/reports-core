package org.entirej.report.poi.layout;

import org.entirej.framework.report.interfaces.EJReportBorderProperties.LineStyle;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportPOIBorder
{

    private boolean                           showTopLine;
    private boolean                           showBottomLine;
    private boolean                           showLeftLine;
    private boolean                           showRightLine;
    private EJReportVisualAttributeProperties visualAttribute;

    private LineStyle                         lineStyle = LineStyle.SOLID;
    private double                            lineWidth = 1;

    public boolean isShowTopLine()
    {
        return showTopLine;
    }

    public void setShowTopLine(boolean showTopLine)
    {
        this.showTopLine = showTopLine;
    }

    public boolean isShowBottomLine()
    {
        return showBottomLine;
    }

    public void setShowBottomLine(boolean showBottomLine)
    {
        this.showBottomLine = showBottomLine;
    }

    public boolean isShowLeftLine()
    {
        return showLeftLine;
    }

    public void setShowLeftLine(boolean showLeftLine)
    {
        this.showLeftLine = showLeftLine;
    }

    public boolean isShowRightLine()
    {
        return showRightLine;
    }

    public void setShowRightLine(boolean showRightLine)
    {
        this.showRightLine = showRightLine;
    }

    public EJReportVisualAttributeProperties getVisualAttributeName()
    {
        return visualAttribute;
    }

    public void setVisualAttribute(EJReportVisualAttributeProperties visualAttribute)
    {
        this.visualAttribute = visualAttribute;
    }
    
    public void setLineStyle(LineStyle lineStyle)
    {
        this.lineStyle = lineStyle;
    }
    
    public LineStyle getLineStyle()
    {
        return lineStyle;
    }
    
    public double getLineWidth()
    {
        return lineWidth;
    }
    public void setLineWidth(double lineWidth)
    {
        this.lineWidth = lineWidth;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lineStyle == null) ? 0 : lineStyle.hashCode());
        long temp;
        temp = Double.doubleToLongBits(lineWidth);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (showBottomLine ? 1231 : 1237);
        result = prime * result + (showLeftLine ? 1231 : 1237);
        result = prime * result + (showRightLine ? 1231 : 1237);
        result = prime * result + (showTopLine ? 1231 : 1237);
        result = prime * result + ((visualAttribute == null) ? 0 : visualAttribute.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EJReportPOIBorder other = (EJReportPOIBorder) obj;
        if (lineStyle != other.lineStyle)
            return false;
        if (Double.doubleToLongBits(lineWidth) != Double.doubleToLongBits(other.lineWidth))
            return false;
        if (showBottomLine != other.showBottomLine)
            return false;
        if (showLeftLine != other.showLeftLine)
            return false;
        if (showRightLine != other.showRightLine)
            return false;
        if (showTopLine != other.showTopLine)
            return false;
        if (visualAttribute == null)
        {
            if (other.visualAttribute != null)
                return false;
        }
        else if (!visualAttribute.equals(other.visualAttribute))
            return false;
        return true;
    }

    
    
}
