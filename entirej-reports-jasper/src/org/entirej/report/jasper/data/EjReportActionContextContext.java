package org.entirej.report.jasper.data;



public interface EjReportActionContextContext
{
    public boolean canShowBlock(String blockName);
    public boolean canShowScreenItem( String blockName, String screenItem, String section);
    
}
