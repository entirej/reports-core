package org.entirej.report.jasper.builder;

import java.awt.Color;
import java.awt.Paint;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.util.Rotation;

public class EJReportChartCustomizer implements JRChartCustomizer
{

    @Override
    public void customize(JFreeChart chart, JRChart jasperChart)
    {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getLegend().setBorder(0, 0, 0, 0);

        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.WHITE);

        if (chart.getPlot() instanceof CategoryPlot)
        {
            final CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setRangeGridlinePaint(Color.GRAY);
            plot.setNoDataMessage("No data available");
            plot.setBackgroundPaint(Color.WHITE);
            plot.setBackgroundAlpha(0.4f);

            plot.setForegroundAlpha(0.8f);
            plot.setOutlineVisible(false);
            plot.getDomainAxis().setMaximumCategoryLabelLines(3);
            Paint[] customPaints = getCustomPaints();
            if (customPaints != null)
            {
                
                
                for (int i = 0; (i < customPaints.length); i++)
                {
                    plot.getRenderer().setSeriesPaint(i, customPaints[i]);
                }
            }
        }
        if (chart.getPlot() instanceof PiePlot)
        {
            final PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}",
                            NumberFormat.getNumberInstance(), NumberFormat
                                            .getPercentInstance()));
            plot.setNoDataMessage("No data available");
            plot.setBackgroundPaint(Color.WHITE);
            plot.setBackgroundAlpha(0.4f);
            plot.setDirection(Rotation.CLOCKWISE);
            chart.setBorderPaint(Color.WHITE);
            plot.setForegroundAlpha(0.8f);
            plot.setOutlineVisible(false);
            //plot.setDepthFactor(0.06);
            plot.setCircular(true);
            plot.setLabelOutlinePaint(null);
            plot.setLabelShadowPaint(null);
            plot.setLabelBackgroundPaint(null);
            plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
            plot.setLabelLinkPaint(Color.GRAY);
            plot.setAutoPopulateSectionPaint(false);
            Paint[] customPaints = getCustomPaints();
            if (customPaints != null)
            {
                
                
                for (int i = 0; (i < customPaints.length); i++)
                {
                    plot.setSectionPaint(i, customPaints[i]);
                }
            }
            chart.removeLegend();
        }

    }

    public static Paint[] getCustomPaints()
    {
        List<Paint> list = Arrays.asList(CUSTOM_PAINTS);
        //Collections.shuffle(list);
        return list.toArray(CUSTOM_PAINTS);
    }

    
    
    
    private static final Paint[] CUSTOM_PAINTS = new Paint[] { new Color(0, 107, 42), new Color(255, 171, 255), new Color(255, 249, 125),
            new Color(0, 181, 163), new Color(72, 61, 139), new Color(100, 149, 237), new Color(132, 112, 255), new Color(102, 205, 170),
            new Color(202, 255, 112), new Color(255, 36, 0), new Color(205, 198, 115), new Color(255, 102, 0), new Color(255, 204, 51), new Color(153, 255, 0),
            new Color(255, 102, 204), new Color(255, 255, 102), new Color(0, 204, 0), new Color(204, 153, 204), new Color(51, 153, 255),
            new Color(0, 102, 204), new Color(0, 204, 153), new Color(245, 0, 245), new Color(0, 63, 153), new Color(255, 143, 117), new Color(88, 0, 156),
            new Color(0, 186, 182), new Color(152, 243, 0), new Color(148, 12, 0), new Color(0, 0, 25), new Color(238, 229, 222), new Color(105, 89, 205) };
}
