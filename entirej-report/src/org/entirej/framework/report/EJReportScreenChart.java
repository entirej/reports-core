package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportChartType;
import org.entirej.framework.report.properties.EJCoreReportChartProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;

public class EJReportScreenChart
{
    private EJCoreReportChartProperties chartProperties;

    public EJReportScreenChart(EJCoreReportChartProperties chartProperties)
    {
        this.chartProperties = chartProperties;
    }

    public EJReportChartType getChartType()
    {
        return chartProperties.getChartType();
    }

    public EJCoreReportScreenProperties getScreenProperties()
    {
        return chartProperties.getScreenProperties();
    }

    public String getValue1Item()
    {
        return chartProperties.getValue1Item();
    }

    public int hashCode()
    {
        return chartProperties.hashCode();
    }

    public String getValue2Item()
    {
        return chartProperties.getValue2Item();
    }

    public String getValue3Item()
    {
        return chartProperties.getValue3Item();
    }

    public String getLabelItem()
    {
        return chartProperties.getLabelItem();
    }

    public String getCategoryItem()
    {
        return chartProperties.getCategoryItem();
    }

    public String getSeriesItem()
    {
        return chartProperties.getSeriesItem();
    }

    public String getTitle()
    {
        return chartProperties.getTitle();
    }

    public String getSubtitle()
    {
        return chartProperties.getSubtitle();
    }

    public String getTitleVA()
    {
        return chartProperties.getTitleVA();
    }

    public String getSubtitleVA()
    {
        return chartProperties.getSubtitleVA();
    }

    public boolean isUse3dView()
    {
        return chartProperties.isUse3dView();
    }

    public boolean equals(Object obj)
    {
        return chartProperties.equals(obj);
    }

    public String toString()
    {
        return chartProperties.toString();
    }
    
    
}
