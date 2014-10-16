/*******************************************************************************
 * Copyright 2014 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.report.jasper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperDesignViewer;
import net.sf.jasperreports.view.JasperViewer;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportRuntimeLevelParameter;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.report.jasper.builder.EJReportJasperReportBuilder;
import org.entirej.report.jasper.data.EJReportBlockDataSource;

public class EJJasperReports
{

    static Map<String, Object> toParameters(EJJasperReportParameter... parameters)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        for (EJJasperReportParameter reportParameter : parameters)
        {
            map.put(reportParameter.getName(), reportParameter.getValue());
        }

        return map;
    }

    public static JasperPrint fillReport(String reportFile, JRDataSource dataSource, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), dataSource);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(InputStream reportFile, JRDataSource dataSource, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), dataSource);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(JasperReport reportFile, JRDataSource dataSource, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), dataSource);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(String reportFile, Connection connection, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), connection);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(InputStream reportFile, Connection connection, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), connection);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(JasperReport reportFile, Connection connection, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), connection);
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static void exportReport(String reportFile, String outputFile, EJReportExportType type, JRDataSource dataSource,
            EJJasperReportParameter... parameters)
    {
        JasperPrint jasperPrint = fillReport(reportFile, dataSource, parameters);
        exportReport(type, jasperPrint, outputFile);
    }

    public static void exportReport(String reportFile, String outputFile, EJReportExportType type, Connection connection, EJJasperReportParameter... parameters)
    {
        JasperPrint jasperPrint = fillReport(reportFile, connection, parameters);
        exportReport(type, jasperPrint, outputFile);
    }

    public static void exportReport(InputStream reportFile, String outputFile, EJReportExportType type, JRDataSource dataSource,
            EJJasperReportParameter... parameters)
    {
        JasperPrint jasperPrint = fillReport(reportFile, dataSource, parameters);
        exportReport(type, jasperPrint, outputFile);
    }

    public static void exportReport(InputStream reportFile, String outputFile, EJReportExportType type, Connection connection,
            EJJasperReportParameter... parameters)
    {
        JasperPrint jasperPrint = fillReport(reportFile, connection, parameters);
        exportReport(type, jasperPrint, outputFile);
    }

    public static void exportReport(EJReportExportType type, JasperPrint print, String outputFile)
    {
        try
        {
            switch (type)
            {
                case PDF:
                    JasperExportManager.exportReportToPdfFile(print, outputFile);

                    break;
                case HTML:
                    JasperExportManager.exportReportToHtmlFile(print, outputFile);

                    break;
                case XML:
                    JasperExportManager.exportReportToXmlFile(print, outputFile, true);

                    break;
                case DOCX:
                {
                    File destFile = new File(outputFile);

                    JRDocxExporter exporter = new JRDocxExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

                    exporter.exportReport();
                }

                    break;
                case ODT:
                {
                    File destFile = new File(outputFile);

                    JROdtExporter exporter = new JROdtExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

                    exporter.exportReport();
                }

                    break;
                case ODS:
                {
                    File destFile = new File(outputFile);

                    JROdsExporter exporter = new JROdsExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

                    exporter.exportReport();
                }

                    break;
                case CSV:
                {
                    File destFile = new File(outputFile);

                    JRCsvExporter exporter = new JRCsvExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

                    exporter.exportReport();
                }
                case RTF:
                {
                    File destFile = new File(outputFile);

                    JRRtfExporter exporter = new JRRtfExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

                    exporter.exportReport();
                }

                    break;
                case XLS:
                {
                    File destFile = new File(outputFile);

                    JRXlsExporter exporter = new JRXlsExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
                    SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
                    configuration.setOnePagePerSheet(false);
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();
                }

                    break;
                case XLSX:
                {
                    File destFile = new File(outputFile);

                    JRXlsxExporter exporter = new JRXlsxExporter();

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
                    exporter.exportReport();
                }

                    break;

                default:
                    break;
            }
        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static void tempRUN(String reportFile, JRDataSource dataSource)
    {
        File temp = null;
        try
        {
            temp = File.createTempFile("ej-report", "pdf");
            temp.deleteOnExit();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            return;
        }

        System.out.println(temp.getAbsolutePath());
        try
        {
            JasperPrint print = fillReport(reportFile, dataSource);
            exportReport(org.entirej.framework.report.enumerations.EJReportExportType.PDF, print, temp.getAbsolutePath());
            Desktop.getDesktop().open(temp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void tempEJReportRun(EJReportFrameworkManager manager, EJReport report, EJJasperReportParameter... parameters)
    {

        File temp = null;
        try
        {
            temp = File.createTempFile("ej-report", "pdf");
            temp.deleteOnExit();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            return;
        }

        System.out.println(temp.getAbsolutePath());

        EJReportJasperReportBuilder builder = new EJReportJasperReportBuilder();

        EJReportBlock block = report.getBlock("ReportCountry");

        builder.buildDesign(block);

        List<EJJasperReportParameter> reportParameters = new ArrayList<EJJasperReportParameter>(Arrays.asList(parameters));


        for (EJReportRuntimeLevelParameter parameter : manager.getRuntimeLevelParameters())
        {
            EJJasperReportParameter jasperReportParameter = new EJJasperReportParameter(parameter.getName(), parameter.getValue());
            reportParameters.add(jasperReportParameter);
        }

        JasperReport jasperReport = builder.toReport();

        try
        {
            JasperDesignViewer.viewReportDesign(jasperReport);

            EJReportBlockDataSource dataSource = new EJReportBlockDataSource(block);
            JasperPrint print = fillReport(jasperReport, dataSource, reportParameters.toArray(parameters));
            JasperViewer.viewReport(print);
            // exportReport(EJReportExportType.PDF, print,
            // temp.getAbsolutePath());
            // Desktop.getDesktop().open(temp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}