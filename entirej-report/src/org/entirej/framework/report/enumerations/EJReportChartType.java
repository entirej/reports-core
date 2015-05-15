package org.entirej.framework.report.enumerations;

public enum EJReportChartType 
{
    BAR_CHART, PIE_CHART;

    public String toString()
    {
        switch (this)
        {

            case BAR_CHART:
                return "Bar Chart";
            case PIE_CHART:
                return "Pie Chart";

            default:
                return super.toString();
        }
    }

}
