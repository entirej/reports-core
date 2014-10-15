package org.entirej.report.jasper.builder;

import java.util.Collection;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportBlockItem;
import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.data.controllers.EJReportRuntimeLevelParameter;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.AlignmentBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Label;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.RotatableItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.ValueBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;

public class EJReportJasperReportBuilder
{

    private final JasperDesign design;

    public EJReportJasperReportBuilder()
    {
        design = new JasperDesign();

    }

    public void buildDesign(EJReport report)
    {
        design.setName(report.getName());
    }

    public void buildDesign(EJReportBlock block)
    {
        try
        {
            design.setName(block.getName());

            EJCoreReportBlockProperties properties = block.getProperties();
            Collection<EJReportBlockItem> blockItems = block.getBlockItems();
            for (EJReportBlockItem item : blockItems)
            {

                JRDesignField field = new JRDesignField();

                field.setName(String.format("%s.%s", block.getName(), item.getName()));
                field.setDescription(item.getFieldName());
                field.setValueClass(item.getDataType());
                design.addField(field);
            }

            EJCoreReportScreenProperties screenProperties = properties.getLayoutScreenProperties();
            Collection<EJCoreReportScreenItemProperties> screenItems = screenProperties.getScreenItems();

            for (EJCoreReportScreenItemProperties item : screenItems)
            {
                if (item instanceof ValueBaseItem)
                {
                    ValueBaseItem vaItem = (ValueBaseItem) item;

                    String defaultValue = vaItem.getValue();
                    if (vaItem.getValue() != null && vaItem.getValue().length() > 0)
                    {
                        String paramTypeCode = defaultValue.substring(0, defaultValue.indexOf(':'));
                        String paramValue = defaultValue.substring(defaultValue.indexOf(':') + 1);
                        if ("BLOCK_ITEM".equals(paramTypeCode))
                        {
                            if (!design.getFieldsMap().containsKey(paramValue))
                            {
                                JRDesignField field = new JRDesignField();

                                field.setName(paramValue);
                                field.setValueClass(Object.class);
                                design.addField(field);
                            }

                        }
                    }
                }
            }

            JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
            JRDesignBand detail = new JRDesignBand();
            detail.setHeight(screenProperties.getHeight());

            detailSection.addBand(detail);

            for (EJCoreReportScreenItemProperties item : screenItems)
            {

                JRDesignElement element = null;
                switch (item.getType())
                {
                    case TEXT:
                    {
                        EJCoreReportScreenItemProperties.Text textItem = (EJCoreReportScreenItemProperties.Text) item;
                        JRDesignTextField text = new JRDesignTextField();
                        element = text;
                        text.setExpression(createValueExpression(block.getReport(), textItem.getValue()));

                        setAlignments(text, textItem);
                        setRotation(text, textItem);
                    }
                        break;
                    case LABEL:
                    {
                        EJCoreReportScreenItemProperties.Label labelItem = (Label) item;
                        JRDesignStaticText lbl = new JRDesignStaticText();
                        element = lbl;
                        lbl.setText(labelItem.getText());
                        setAlignments(lbl, labelItem);
                        setRotation(lbl, labelItem);
                    }
                        break;

                    default:
                        break;
                }

                if (element != null)
                {

                    element.setX(item.getX());
                    element.setY(item.getY());
                    element.setWidth(item.getWidth());
                    element.setHeight(item.getHeight());
                    detail.addElement(element);
                }

            }

        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    void setAlignments(JRAlignment elm, AlignmentBaseItem alignmentBaseItem)
    {
        switch (alignmentBaseItem.getVAlignment())
        {
            case TOP:
                elm.setVerticalAlignment(VerticalAlignEnum.TOP);
                break;
            case BOTTOM:
                elm.setVerticalAlignment(VerticalAlignEnum.BOTTOM);
                break;
            case CENTER:
                elm.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
                break;
            case JUSTIFIED:
                elm.setVerticalAlignment(VerticalAlignEnum.JUSTIFIED);
                break;

            default:
                break;
        }
        switch (alignmentBaseItem.getHAlignment())
        {
            case LEFT:
                elm.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                break;
            case RIGHT:
                elm.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                break;
            case CENTER:
                elm.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                break;
            case JUSTIFIED:
                elm.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
                break;

            default:
                break;
        }
    }

    void setRotation(JRCommonText elm, RotatableItem rotatableItem)
    {
        switch (rotatableItem.getRotation())
        {
            case LEFT:
                elm.setRotation(RotationEnum.LEFT);
                break;
            case RIGHT:
                elm.setRotation(RotationEnum.RIGHT);
                break;
            case UPSIDEDOWN:
                elm.setRotation(RotationEnum.UPSIDE_DOWN);
                break;
        }
    }

    JRDesignExpression createValueExpression(EJReport report, String defaultValue)
    {
        JRDesignExpression expression = new JRDesignExpression();

        if (defaultValue == null || defaultValue.trim().length() == 0)
        {
            return expression;
        }

        String paramTypeCode = defaultValue.substring(0, defaultValue.indexOf(':'));
        String paramValue = defaultValue.substring(defaultValue.indexOf(':') + 1);

        if ("APP_PARAMETER".equals(paramTypeCode))
        {
            expression.setText(String.format("$P{%s}", paramValue));
        }
        else if ("REPORT_PARAMETER".equals(paramTypeCode))
        {
            expression.setText(String.format("$P{%s}", paramValue));
        }
        else if ("BLOCK_ITEM".equals(paramTypeCode))
        {
            expression.setText(String.format("$F{%s}", paramValue));
        }
        else if ("CLASS_FIELD".equals(paramTypeCode))
        {

            expression.setText(paramValue);
        }

        return expression;
    }

    public JasperReport toReport()
    {

        try
        {
            return JasperCompileManager.compileReport(design);
        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }

    }
}
