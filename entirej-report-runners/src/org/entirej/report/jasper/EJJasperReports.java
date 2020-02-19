/*******************************************************************************
 * Copyright 2014 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.report.jasper;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.entirej.report.jasper.builder.EJReportJasperReportBuilder;
import org.entirej.report.jasper.data.EJReportBlockContext;
import org.entirej.report.jasper.data.EJReportDataSource;
import org.entirej.report.jasper.html.EJHtmlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.util.MarkupProcessorFactory;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class EJJasperReports
{
    final static Logger LOGGER = LoggerFactory.getLogger(EJJasperReports.class);

    static
    {
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        context.setProperty("net.sf.jasperreports.print.keep.full.text", "true");
        context.setProperty("net.sf.jasperreports.text.truncate.at.char", "true");
        context.setProperty("net.sf.jasperreports.text.truncate.suffix", "...");
        context.setProperty("net.sf.jasperreports.xpath.executer.factory", "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
        context.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
        // context.setProperty("net.sf.jasperreports.subreport.runner.factory"
        // ,"net.sf.jasperreports.engine.fill.JRContinuationSubreportRunnerFactory");

        // JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
        // "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

        context.setProperty("net.sf.jasperreports.extension.registry.factory.xml.chart.themes",
                "net.sf.jasperreports.chartthemes.simple.XmlChartThemeExtensionsRegistryFactory");
        context.setProperty("net.sf.jasperreports.xml.chart.theme.aegean", "net/sf/jasperreports/chartthemes/aegean.jrctx");
//
//
//        
//        context.setProperty("net.sf.jasperreports.extension.registry.factory.simple.font.families","net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory");
//        context.setProperty("net.sf.jasperreports.extension.simple.font.families.dejavu","net/sf/jasperreports/fonts/fonts.xml");
//        context.setProperty("net.sf.jasperreports.extension.simple.font.families.courier_new","org/entirej/report/jasper/fonts/ej-fonts.xml");
        
        context.setProperty(MarkupProcessorFactory.PROPERTY_MARKUP_PROCESSOR_FACTORY_PREFIX +"html", EJHtmlFactory.class.getName());
    }

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
            removeBlankPage(reportToFile.getPages());
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
            removeBlankPage(reportToFile.getPages());
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
            removeBlankPage(reportToFile.getPages());
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
            removeBlankPage(reportToFile.getPages());
            return reportToFile;

        }
        catch (JRException e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }
    }

    public static JasperPrint fillReport(EJReportFrameworkManager manager, final EJReport report, EJJasperReportParameter... parameters)
    {
        File tempFile = null;
        try
        {
            report.getActionController().beforeReport(report);
            EJReportJasperReportBuilder builder = new EJReportJasperReportBuilder();

            builder.buildDesign(report);

            long start = System.currentTimeMillis();
            LOGGER.info("START Genarate EjReport -> Jasper Report :" + report.getName());

            JasperReport jasperReport = builder.toReport();

            List<EJJasperReportParameter> reportParameters = new ArrayList<EJJasperReportParameter>();

            tempFile = File.createTempFile(report.getName(), "swap");
            tempFile.delete();
            tempFile.mkdirs();
            net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer virtualizer = new JRSwapFileVirtualizer(1000,
                    new JRSwapFile(tempFile.getAbsolutePath(), 2048, 1024), true);

            EJJasperReportParameter virtualizerParam = new EJJasperReportParameter(JRParameter.REPORT_VIRTUALIZER, JRAbstractLRUVirtualizer.class);
            virtualizerParam.setValue(virtualizer);
            reportParameters.add(virtualizerParam);

            for (EJApplicationLevelParameter parameter : manager.getApplicationLevelParameters())
            {
                EJJasperReportParameter jasperReportParameter = new EJJasperReportParameter(parameter.getName(), parameter.getValue());
                reportParameters.add(jasperReportParameter);
            }

            Collection<EJReportParameter> allParameters = report.getParameterList().getAllParameters();
            for (EJReportParameter parameter : allParameters)
            {
                EJJasperReportParameter jasperReportParameter = new EJJasperReportParameter(parameter.getName(), parameter.getValue());
                reportParameters.add(jasperReportParameter);
            }

            // add Block datasource

            EJReportBlockContext blockContext = new EJReportBlockContext()
            {

                @Override
                public JasperReport getBlockReport(String blockName)
                {
                    EJReportBlock block = report.getBlock(blockName);
                    EJReportJasperReportBuilder sbBuilder = new EJReportJasperReportBuilder();
                    sbBuilder.buildDesign(block);

                    return sbBuilder.toReport();
                }

                @Override
                public JasperReport getBlockReportFixed(String blockName)
                {
                    EJReportBlock block = report.getBlock(blockName);
                    EJReportJasperReportBuilder sbBuilder = new EJReportJasperReportBuilder();
                    sbBuilder.buildDesignFixed(block);

                    return sbBuilder.toReport();
                }
            };

            EJJasperReportParameter subRPTParameter = new EJJasperReportParameter("EJRJ_BLOCK_RPT", blockContext);
            reportParameters.add(subRPTParameter);

            reportParameters.addAll(Arrays.asList(parameters));

            LOGGER.info("END Genarate EjReport -> Jasper Report :" + report.getName() + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);

            // JasperDesignViewer.viewReportDesign(jasperReport);
            LOGGER.info("START Filling  Report :" + report.getName());
            start = System.currentTimeMillis();
            JasperPrint print = fillReport(jasperReport, new EJReportDataSource(report), reportParameters.toArray(parameters));
            report.getActionController().afterReport(report);
            LOGGER.info("END Filling  Report :" + report.getName() + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);

            return print;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new EJReportRuntimeException(e);
        }finally
        {
            if(tempFile!=null)
            {
                File[] listFiles = tempFile.listFiles();
                for (File file : listFiles)
                {
                    file.delete();
                }
                tempFile.delete();
            }
        }
    }

    public static JasperPrint fillReport(InputStream reportFile, Connection connection, EJJasperReportParameter... parameters)
    {
        try
        {
            JasperPrint reportToFile = JasperFillManager.fillReport(reportFile, toParameters(parameters), connection);
            removeBlankPage(reportToFile.getPages());
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
            removeBlankPage(reportToFile.getPages());
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

    public static void exportReport(EJReportFrameworkManager manager, EJReport report, String outputFile, EJJasperReportParameter... parameters)
    {
        JasperPrint jasperPrint = fillReport(manager, report, parameters);
        LOGGER.info("START Export  Report :" + report.getName());
        long start = System.currentTimeMillis();

        Collection<EJReportPage> pages = report.getPages();
        List<String> pageNames = new ArrayList<String>(pages.size());
        for (EJReportPage page : pages)
        {
            pageNames.add(page.getName());
        }

        exportReport(report.getExportType(), jasperPrint, outputFile, pageNames.toArray(new String[0]));
        LOGGER.info("END Export Report :" + report.getName() + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);

    }

    public static void exportReport(EJReportFrameworkManager manager, EJReport report, String outputFile, EJReportExportType type,
            EJJasperReportParameter... parameters)
    {

        JasperPrint jasperPrint = fillReport(manager, report, parameters);
        LOGGER.info("START Export  Report :" + report.getName());
        long start = System.currentTimeMillis();
        Collection<EJReportPage> pages = report.getPages();
        List<String> pageNames = new ArrayList<String>(pages.size());
        for (EJReportPage page : pages)
        {
            pageNames.add(page.getName());
        }
        exportReport(type, jasperPrint, outputFile, pageNames.toArray(new String[0]));
        LOGGER.info("END Export Report :" + report.getName() + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);
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

    public static void exportReport(EJReportExportType type, JasperPrint print, String outputFile, String... pageNames)
    {

        LOGGER.info("START Export  Report :" + outputFile);
        long start = System.currentTimeMillis();

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
                case PNG:
                {

                    FileOutputStream output = new FileOutputStream(new File(outputFile));
                    ImageIO.write(toBufferedImage(JasperPrintManager.printPageToImage(print, 0, 1)), "PNG", output);
                    output.close();
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

                    configuration.setDetectCellType(Boolean.TRUE);
                    configuration.setWhitePageBackground(Boolean.FALSE);
                    configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                    configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                    configuration.setSheetNames(pageNames);
                    configuration.setWrapText(true);
                    configuration.setIgnorePageMargins(true);
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
                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setWrapText(true);
                    configuration.setIgnoreCellBorder(false);
                    configuration.setOnePagePerSheet(false);
                    configuration.setIgnorePageMargins(true);
                    configuration.setDetectCellType(Boolean.TRUE);
                    configuration.setWhitePageBackground(Boolean.FALSE);
                    configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                    configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                    configuration.setSheetNames(pageNames);
                    exporter.setConfiguration(configuration);
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
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            LOGGER.info("END Export  Report :" + outputFile + " TIME(sec):" + (System.currentTimeMillis() - start) / 1000);
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

        try
        {

            JasperPrint print = fillReport(manager, report, parameters);
            // JasperViewer.viewReport(print);
            if (true)
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
                exportReport(EJReportExportType.XLS, print, temp.getAbsolutePath());
                Desktop.getDesktop().open(temp);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static void removeBlankPage(List<JRPrintPage> pages)
    {

        if (pages.size() > 0)
        {
            JRPrintPage lastpage = pages.get(pages.size() - 1);

            if (lastpage.getElements().size() == 0)
                pages.remove(lastpage);
        }

    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            BufferedImage bimage = (BufferedImage) img;
            bimage = bimage.getSubimage(0, 0, img.getWidth(null) - 1, img.getHeight(null) - 1);
            return bimage;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        bimage = bimage.getSubimage(0, 0, img.getWidth(null) - 5, img.getHeight(null) - 5);
        // Return the buffered image
        return bimage;
    }
}
