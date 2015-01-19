package org.entirej.report.jasper.data;

public interface EJReportBlockItemVAContext
{
    boolean isActive(String item,String vaName);
    
    boolean isVisible(String item);
}
