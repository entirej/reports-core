package org.entirej.report.jasper.data;

import net.sf.jasperreports.engine.JasperReport;

public interface EJReportBlockContext
{
    public JasperReport getBlockReport(String blockName);
    public JasperReport getBlockReportFixed(String blockName);

}
