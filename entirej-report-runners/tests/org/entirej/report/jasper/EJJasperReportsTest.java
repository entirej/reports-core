package org.entirej.report.jasper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.enumerations.EJReportExportType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;

class EJJasperReportsTest
{
    private static final String CSV_SENTINEL = "CSV_SENTINEL";

    @TempDir
    Path tempDirectory;

    @Test
    void csvExportDoesNotFallThroughToRtf() throws Exception
    {
        Path csvFile = tempDirectory.resolve("report.csv");

        EJJasperReports.exportReport(EJReportExportType.CSV, createJasperPrint(), csvFile.toString());

        String csv = Files.readString(csvFile, StandardCharsets.UTF_8);
        assertTrue(csv.contains(CSV_SENTINEL), "The CSV exporter must write the report content");
        assertFalse(csv.stripLeading().startsWith("{\\rtf"), "The RTF exporter must not overwrite CSV output");
    }

    @Test
    void outputIOExceptionIsPropagated() throws Exception
    {
        Path outputDirectory = Files.createDirectory(tempDirectory.resolve("report.png"));

        EJReportRuntimeException exception = assertThrows(EJReportRuntimeException.class,
                () -> EJJasperReports.exportReport(EJReportExportType.PNG, createJasperPrint(), outputDirectory.toString()));

        assertInstanceOf(IOException.class, exception.getCause());
    }

    private JasperPrint createJasperPrint() throws JRException
    {
        JasperDesign design = new JasperDesign();
        design.setName("csv-export-regression");
        design.setPageWidth(200);
        design.setPageHeight(100);
        design.setColumnWidth(200);
        design.setLeftMargin(0);
        design.setRightMargin(0);
        design.setTopMargin(0);
        design.setBottomMargin(0);

        JRDesignStaticText text = new JRDesignStaticText();
        text.setX(0);
        text.setY(0);
        text.setWidth(200);
        text.setHeight(20);
        text.setText(CSV_SENTINEL);

        JRDesignBand detail = new JRDesignBand();
        detail.setHeight(20);
        detail.addElement(text);
        ((JRDesignSection) design.getDetailSection()).addBand(detail);

        JasperReport report = JasperCompileManager.compileReport(design);
        return JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(1));
    }
}
