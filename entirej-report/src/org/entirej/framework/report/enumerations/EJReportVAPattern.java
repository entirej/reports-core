package org.entirej.framework.report.enumerations;

public enum EJReportVAPattern
{
    NONE, NUMBER, INTEGER, CURRENCY, PERCENT, DATE_LONG, DATE_MEDIUM, DATE_SHORT, DATE_FULL,

    DATE_TIME_LONG, DATE_TIME_MEDIUM, DATE_TIME_SHORT, DATE_TIME_FULL,

    TIME_LONG, TIME_MEDIUM, TIME_SHORT, TIME_FULL;

    public String toString()
    {
        switch (this)
        {

            case NONE:
                return "None";

            default:
                return super.toString();
        }
    }
}
