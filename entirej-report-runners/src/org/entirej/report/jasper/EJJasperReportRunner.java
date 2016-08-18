package org.entirej.report.jasper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.framework.report.interfaces.EJReportRunner;

public class EJJasperReportRunner implements EJReportRunner
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

            String name = report.getName();
            if(report.getOutputName()!=null &&!report.getOutputName().isEmpty())
            {
                name = report.getOutputName();
            }
            temp = File.createTempFile("EJR_" + name, "." + type.toString().toLowerCase());
            temp.delete();
            temp.mkdirs();
            File export = new  File(temp,name+ "." + type.toString().toLowerCase());
            export.deleteOnExit();
            temp.deleteOnExit();
            runReport(report, type, export.getAbsolutePath());
            return export.getAbsolutePath();

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
        try
        {
          
            JasperPrint jasperPrint = EJJasperReports.fillReport(manager, report);

            Collection<EJReportPage> pages = report.getPages();
            List<String> pageNames = new ArrayList<String>(pages.size());
            for (EJReportPage page : pages)
            {
                pageNames.add(page.getName());
            }
            
            EJJasperReports.exportReport(type, jasperPrint, outputFile,pageNames.toArray(new String[0]));
   
        }
        catch (Throwable t)
        {
            throw new EJReportRuntimeException(t.getMessage(), t);
        }

    }
}
