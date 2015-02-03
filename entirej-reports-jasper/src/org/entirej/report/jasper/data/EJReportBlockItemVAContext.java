package org.entirej.report.jasper.data;

import org.entirej.framework.report.enumerations.EJReportScreenSection;

public interface EJReportBlockItemVAContext
{
    boolean isActive(String item, String section, String vaName);

    boolean isVisible(String item, String section);
}
