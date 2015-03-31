package org.entirej.report.jasper.data;

public interface EJReportBlockItemVAContext
{
    boolean isActive(String item, String section, String vaName);
    boolean isActive(String item, String section);

    boolean isVisible(String item, String section);

    public Object getVABaseValue(Object value, String item, String section);
}
