package org.entirej.framework.report.enumerations;

public enum EJReportChartType 
{
    BAR_CHART, PIE_CHART,STACKED_BAR_CHART,AREA_CHART,STACKED_AREA_CHART;

    public String toString()
    {
        switch (this)
        {

            case BAR_CHART:
                return "Bar Chart";
            case AREA_CHART:
                return "Area Chart";
            case STACKED_AREA_CHART:
                return "Stacked Area Chart";
            case STACKED_BAR_CHART:
                return "Stacked Bar Chart";
            case PIE_CHART:
                return "Pie Chart";

            default:
                return super.toString();
        }
    }

}
