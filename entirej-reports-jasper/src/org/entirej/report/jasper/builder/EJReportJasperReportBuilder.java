package org.entirej.report.jasper.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import net.sf.jasperreports.charts.type.ScaleTypeEnum;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportBlockItem;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportRuntimeLevelParameter;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.AlignmentBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Date.DateFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Label;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineDirection;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Number.NumberFormats;
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

    
    
    void createParamaters(EJReport report) throws JRException
    {
        
        Collection<EJReportRuntimeLevelParameter> runtimeLevelParameters = report.getRuntimeLevelParameters();
        
        for (EJReportRuntimeLevelParameter parameter : runtimeLevelParameters)
        {
            JRDesignParameter designParameter = new JRDesignParameter();
            designParameter.setName(parameter.getName());
            designParameter.setValueClass(parameter.getDataType());
            design.addParameter(designParameter);
        }
        
    }
    
    public void buildDesign(EJReportBlock block)
    {
        
        try
        {
            createParamaters(block.getReport());
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
                        text.setBlankWhenNull(true);
                    }
                        break;
                    case NUMBER:
                    {
                        EJCoreReportScreenItemProperties.Number textItem = (EJCoreReportScreenItemProperties.Number) item;
                        JRDesignTextField text = new JRDesignTextField();
                        element = text;
                        text.setExpression(createValueExpression(block.getReport(), textItem.getValue()));
                        
                        setAlignments(text, textItem);
                        setRotation(text, textItem);
                        text.setBlankWhenNull(true);
                        if(textItem.getManualFormat()!=null && !textItem.getManualFormat().isEmpty())
                        {
                            text.setPattern(textItem.getManualFormat());
                        }
                        else
                        {
                            Locale defaultLocale = block.getReport().getFrameworkManager().getCurrentLocale();
                            NumberFormats localeFormat = textItem.getLocaleFormat();
                            switch (localeFormat)
                            {
                                case CURRENCY:
                                    text.setPattern(((java.text.DecimalFormat)java.text.NumberFormat.getCurrencyInstance(defaultLocale)).toPattern());
                                    break;
                                case PERCENT:
                                    text.setPattern(((java.text.DecimalFormat)java.text.NumberFormat.getPercentInstance(defaultLocale)).toPattern());
                                    break;
                                case INTEGER:
                                    text.setPattern(((java.text.DecimalFormat)java.text.NumberFormat.getIntegerInstance(defaultLocale)).toPattern());
                                    break;
                                case NUMBER:
                                    text.setPattern(((java.text.DecimalFormat)java.text.NumberFormat.getNumberInstance(defaultLocale)).toPattern());
                                    break;

                                default:
                                    break;
                            }
                        }
                        
                    }
                    break;
                    case DATE:
                    {
                        EJCoreReportScreenItemProperties.Date textItem = (EJCoreReportScreenItemProperties.Date) item;
                        JRDesignTextField text = new JRDesignTextField();
                        element = text;
                        text.setExpression(createValueExpression(block.getReport(), textItem.getValue()));
                        
                        setAlignments(text, textItem);
                        setRotation(text, textItem);
                        text.setBlankWhenNull(true);
                        
                        Locale defaultLocale = block.getReport().getFrameworkManager().getCurrentLocale();
                        
                        if(textItem.getManualFormat()!=null && !textItem.getManualFormat().isEmpty())
                        {
                            text.setPattern(textItem.getManualFormat());
                        }
                        else
                        {
                            DateFormats localeFormat = textItem.getLocaleFormat();
                            SimpleDateFormat dateFormat = null; 
                            switch (localeFormat)
                            {
                                case DATE_FULL:
                                    dateFormat =(SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.FULL,defaultLocale));
                                    break;
                                case DATE_LONG:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.LONG,defaultLocale));
                                    break;
                                case DATE_MEDIUM:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.MEDIUM,defaultLocale));
                                    break;
                                case DATE_SHORT:
                                    dateFormat =(SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.SHORT,defaultLocale));
                                    break;
                                case DATE_TIME_FULL:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,defaultLocale));
                                    break;    
                                case DATE_TIME_LONG:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,defaultLocale));
                                    break;    
                                case DATE_TIME_MEDIUM:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM,defaultLocale));
                                    break;    
                                case DATE_TIME_SHORT:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,defaultLocale));
                                    break; 
                                    
                                case TIME_FULL:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.FULL,defaultLocale));
                                    break;
                                case TIME_LONG:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.LONG,defaultLocale));
                                    break;
                                case TIME_MEDIUM:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.MEDIUM,defaultLocale));
                                    break;
                                case TIME_SHORT:
                                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.SHORT,defaultLocale));
                                    break;
                            }
                            if(dateFormat!=null)
                            {
                                text.setPattern(dateFormat.toPattern());
                            }
                        }
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
                    case LINE:
                    {
                        EJCoreReportScreenItemProperties.Line lineItem = (EJCoreReportScreenItemProperties.Line) item;
                        JRDesignLine line = new JRDesignLine();
                        element = line;

                        line.setDirection(lineItem.getLineDirection() == LineDirection.BOTTOM_UP ? LineDirectionEnum.BOTTOM_UP : LineDirectionEnum.TOP_DOWN);
                        JRPen linePen = line.getLinePen();
                        linePen.setLineWidth((float) lineItem.getLineWidth());
                        switch (lineItem.getLineStyle())
                        {
                            case DASHED:
                                linePen.setLineStyle(LineStyleEnum.DASHED);
                                break;
                            case DOTTED:
                                linePen.setLineStyle(LineStyleEnum.DOTTED);
                                break;
                            case DOUBLE:
                                linePen.setLineStyle(LineStyleEnum.DOUBLE);
                                break;

                            default:
                                break;
                        }
                    }
                     break;
                    case RECTANGLE:
                    {
                        EJCoreReportScreenItemProperties.Rectangle lineItem = (EJCoreReportScreenItemProperties.Rectangle) item;
                        JRDesignRectangle line = new JRDesignRectangle();
                        element = line;
                        
                        line.setRadius(lineItem.getRadius());
                        JRPen linePen = line.getLinePen();
                        linePen.setLineWidth((float) lineItem.getLineWidth());
                        switch (lineItem.getLineStyle())
                        {
                            case DASHED:
                                linePen.setLineStyle(LineStyleEnum.DASHED);
                                break;
                            case DOTTED:
                                linePen.setLineStyle(LineStyleEnum.DOTTED);
                                break;
                            case DOUBLE:
                                linePen.setLineStyle(LineStyleEnum.DOUBLE);
                                break;
                                
                            default:
                                break;
                        }
                    }
                    break;
                    case IMAGE:
                    {
                        EJCoreReportScreenItemProperties.Image imageItem = (EJCoreReportScreenItemProperties.Image) item;
                        JRDefaultStyleProvider styleProvider = new JRDefaultStyleProvider()
                        {
                            
                            @Override
                            public JRStyle getDefaultStyle()
                            {
                                return null;
                            }
                        }; 
                        JRDesignImage image = new JRDesignImage(styleProvider) ;
                        
                        element = image;
                        
                        image.setExpression(createImageValueExpression(block.getReport(), imageItem.getValue()));
                        
                        image.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
                        image.setUsingCache(true);
                        setAlignments(image, imageItem);
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
    JRDesignExpression createImageValueExpression(EJReport report, String defaultValue)
    {
        JRDesignExpression expression = createValueExpression(report, defaultValue);
        
        if(expression.getText()!=null && !expression.getText().isEmpty())
        {
             expression.setText(String.format("new ByteArrayInputStream((byte[]) %s)", expression.getText()));
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
