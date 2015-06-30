package org.entirej.report.jasper;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.framework.report.interfaces.EJReportRunner;
import org.entirej.report.jasper.builder.EJReportExcelReportBuilder;

public class EJExcelReportRunner implements EJReportRunner
{

    private EJReportFrameworkManager manager;

    @Override
    public void init(EJReportFrameworkManager manager)
    {
        this.manager = manager;

    }

    @Override
    public String runReport(EJReport report)
    {
        return runReport(report, report.getExportType());
    }

    @Override
    public String runReport(EJReport report, EJReportExportType type)
    {
        File temp = null;
        try
        {

            temp = File.createTempFile("EJR_" + report.getName(), "." + type.toString().toLowerCase());
            temp.deleteOnExit();

            runReport(report, type, temp.getAbsolutePath());
            return temp.getAbsolutePath();

        }
        catch (IOException e1)
        {
            e1.printStackTrace();

        }

        return null;

    }

    @Override
    public void runReport(EJReport report, String outputFile)
    {
        runReport(report, report.getExportType(), outputFile);

    }

    @Override
    public void runReport(EJReport report, EJReportExportType type, String outputFile)
    {
        if (manager == null)
        {
            throw new EJReportRuntimeException("EJReportFrameworkManager not initialised");
        }
        if(type != EJReportExportType.XLSX)
        {
            throw new EJReportRuntimeException("EJExcelReportRunner  only support XLSX output type.");
        }
        
        
        EJReportExcelReportBuilder builder = new EJReportExcelReportBuilder();
       builder.build(report, type, outputFile);

    }
}
