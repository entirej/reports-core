package org.entirej.report;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.framework.report.interfaces.EJReportRunner;
import org.entirej.report.jasper.EJJasperReportRunner;
import org.entirej.report.poi.EJExcelPOIReportRunner;

public class EJReportDefaultRunner implements EJReportRunner
{

    private EJReportFrameworkManager manager;
    private EJJasperReportRunner     jasperReportRunner;
    private EJExcelPOIReportRunner   excelPOIReportRunner;

    @Override
    public void init(EJReportFrameworkManager manager)
    {
        this.manager = manager;
        jasperReportRunner = new EJJasperReportRunner();
        jasperReportRunner.init(this.manager);
        excelPOIReportRunner = new EJExcelPOIReportRunner();
        excelPOIReportRunner.init(this.manager);

    }

    @Override
    public String runReport(EJReport report)
    {
        return runReport(report, report.getExportType());
    }

    @Override
    public String runReport(EJReport report, EJReportExportType exportType)
    {
        if (exportType == EJReportExportType.XLSX_LARGE)
        {
            return excelPOIReportRunner.runReport(report, exportType);
        }
        else
        {
            return jasperReportRunner.runReport(report, exportType);
        }
    }

    @Override
    public void runReport(EJReport report, String output)
    {
        runReport(report, report.getExportType(), output);

    }

    @Override
    public void runReport(EJReport report, EJReportExportType exportType, String output)
    {
        if (exportType == EJReportExportType.XLSX)
        {
            excelPOIReportRunner.runReport(report, exportType, output);
        }
        else
        {
            jasperReportRunner.runReport(report, exportType, output);
        }

    }

}
