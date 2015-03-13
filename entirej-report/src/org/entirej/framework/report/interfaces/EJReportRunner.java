package org.entirej.framework.report.interfaces;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.enumerations.EJReportExportType;

public interface EJReportRunner
{

    void init(EJReportFrameworkManager manager);

    String runReport(EJReport report);

    String runReport(EJReport report, EJReportExportType type);

    void runReport(EJReport report, String outputFile);

    void runReport(EJReport report, EJReportExportType type, String outputFile);

}
