package org.entirej.report;

import java.io.File;
import java.io.IOException;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportParameter;
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

        File tempReportDir = getTempReportDir();
        tempReportDir.mkdirs();
        String ext = exportType.toString().toLowerCase();
        if (exportType == EJReportExportType.XLSX_LARGE)
        {
            ext = EJReportExportType.XLSX.toString().toLowerCase();
        }

        File export;
        EJReportParameter reportParameter = report.getReportParameter("REPORT_NAME");

        if (reportParameter != null && reportParameter.getValue() != null && !((String) reportParameter.getValue()).isEmpty())
        {
            try
            {
                export = File.createTempFile((String) reportParameter.getValue()+"_","."+ ext, tempReportDir);
            }
            catch (IOException e)
            {
                throw new EJReportRuntimeException(e.getMessage());
            }
        }
        else
        {

            try
            {
                export = File.createTempFile(report.getName()+"_","."+ ext, tempReportDir);
            }
            catch (IOException e)
            {
                throw new EJReportRuntimeException(e.getMessage());
            }
        }
        String output = export.getAbsolutePath();
        if (exportType == EJReportExportType.XLSX_LARGE)
        {
            excelPOIReportRunner.runReport(report, exportType, output);
        }
        else
        {
            jasperReportRunner.runReport(report, exportType, output);
        }
        return output;
    }

    @Override
    public void runReport(EJReport report, String output)
    {
        runReport(report, report.getExportType(), output);

    }

    static File getTempReportDir()
    {
        if (System.getProperty("ej.report.tmpdir") != null && !System.getProperty("ej.report.tmpdir").isEmpty())
        {
            return new File(System.getProperty("ej.report.tmpdir"));
        }
        else
        {
            return new File(System.getProperty("java.io.tmpdir") + "/EJ/Reports");
        }
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
