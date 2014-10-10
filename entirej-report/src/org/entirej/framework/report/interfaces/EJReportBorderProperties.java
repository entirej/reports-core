package org.entirej.framework.report.interfaces;

import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public interface EJReportBorderProperties
{

    public enum LineStyle
    {
        SOLID, DASHED, DOTTED, DOUBLE;

    }

    LineStyle getLineStyle();

    double getLineWidth();

    EJReportVisualAttributeProperties getVisualAttributeProperties();

    boolean isShowTopLine();

    boolean isShowBottomLine();

    boolean isShowLeftLine();

    boolean isShowRightLine();

}
