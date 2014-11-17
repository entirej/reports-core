package org.entirej.report.jasper.builder;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportBlockItem;
import org.entirej.framework.report.EJReportParameterList;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.data.controllers.EJReportRuntimeLevelParameter;
import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportBorderProperties;
import org.entirej.framework.report.interfaces.EJReportColumnProperties;
import org.entirej.framework.report.interfaces.EJReportProperties;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.AlignmentBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Date.DateFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Label;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineDirection;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Number.NumberFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.RotatableItem;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.ValueBaseItem;
import org.entirej.framework.report.properties.EJCoreReportScreenProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportJasperReportBuilder
{

    private final JasperDesign design;
    private Locale             defaultLocale;

    public EJReportJasperReportBuilder()
    {
        design = new JasperDesign();

    }

    public void buildDesign(EJReport report)
    {
        try
        {
            defaultLocale = report.getFrameworkManager().getCurrentLocale();
            createParamaters(report);
            design.setName(report.getName());
            design.setIgnorePagination(report.getProperties().isIgnorePagination());
            addDefaultFont(report);

            EJReportProperties properties = report.getProperties();

            design.setTopMargin(properties.getMarginTop());
            design.setBottomMargin(properties.getMarginBottom());
            design.setLeftMargin(properties.getMarginLeft());
            design.setRightMargin(properties.getMarginRight());

            int width = properties.getReportWidth() - (properties.getMarginLeft() + properties.getMarginRight());
            int height = properties.getReportHeight() - (properties.getMarginTop() + properties.getMarginBottom());
            design.setPageWidth(properties.getReportWidth());
            design.setColumnWidth(width);
            design.setPageHeight(properties.getReportHeight());

            JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();

            JRDesignBand header = null;
            if (properties.getHeaderSectionHeight() > 0)
            {
                header = new JRDesignBand();
                header.setSplitType(SplitTypeEnum.STRETCH);
                header.setHeight(properties.getHeaderSectionHeight());
                design.setPageHeader(header);
                height -= properties.getHeaderSectionHeight();
            }
            JRDesignBand footer = null;
            if (properties.getFooterSectionHeight() > 0)
            {
                footer = new JRDesignBand();
                footer.setSplitType(SplitTypeEnum.STRETCH);
                footer.setHeight(properties.getFooterSectionHeight());
                design.setPageFooter(footer);
                height -= properties.getFooterSectionHeight();
            }

            for (EJReportBlock block : report.getHeaderBlocks())
            {
                JRDesignSubreport subreport = createSubReport(report, block);
                if (subreport == null)
                    continue;
                EJCoreReportScreenProperties screenProperties = block.getProperties().getLayoutScreenProperties();

                subreport.setX(screenProperties.getX());
                subreport.setY(screenProperties.getY());
                subreport.setWidth(screenProperties.getWidth());
                subreport.setHeight(screenProperties.getHeight());
                subreport.setStretchType(StretchTypeEnum.NO_STRETCH);
                header.addElement(subreport);
            }
            for (EJReportBlock block : report.getFooterBlocks())
            {
                JRDesignSubreport subreport = createSubReport(report, block);
                if (subreport == null)
                    continue;
                EJCoreReportScreenProperties screenProperties = block.getProperties().getLayoutScreenProperties();

                subreport.setX(screenProperties.getX());
                subreport.setY(screenProperties.getY());
                subreport.setWidth(screenProperties.getWidth());
                subreport.setHeight(screenProperties.getHeight());
                subreport.setStretchType(StretchTypeEnum.NO_STRETCH);
                footer.addElement(subreport);
            }

            JRDesignBand detail = new JRDesignBand();
            detail.setSplitType(SplitTypeEnum.STRETCH);
            detail.setHeight(height);

            detailSection.addBand(detail);

            Collection<EJReportBlock> rootbBlocks = report.getRootBlocks();
            for (EJReportBlock block : rootbBlocks)
            {
                JRDesignSubreport subreport = createSubReport(report, block);
                if (subreport == null)
                    continue;
                EJCoreReportScreenProperties screenProperties = block.getProperties().getLayoutScreenProperties();

                subreport.setX(screenProperties.getX());
                subreport.setY(screenProperties.getY());
                subreport.setWidth(screenProperties.getWidth());
                subreport.setHeight(screenProperties.getHeight());

                detail.addElement(subreport);

                subreport.getPropertiesMap().setProperty("net.sf.jasperreports.export.xls.break.before.row", "true");
            }

        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    private void addDefaultFont(EJReport report) throws JRException
    {
        JRDesignStyle _EJ_DEAFULT = new JRDesignStyle();
        _EJ_DEAFULT.setName("_EJ_DEAFULT");
        _EJ_DEAFULT.setFontName("Arial");
        _EJ_DEAFULT.setPdfFontName("Helvetica");
        _EJ_DEAFULT.setDefault(true);
        _EJ_DEAFULT.setPdfEmbedded(true);

        EJReportVisualAttributeProperties va = report.getProperties().getVisualAttributeProperties();
        if (va != null)
        {
            vaToStyle(va, _EJ_DEAFULT);
        }

        design.addStyle(_EJ_DEAFULT);
    }

    private JRDesignSubreport createSubReport(EJReport report, EJReportBlock block) throws JRException
    {
        EJCoreReportScreenProperties screenProperties = block.getProperties().getLayoutScreenProperties();

        if (screenProperties.getScreenType() != EJReportScreenType.NONE)
        {

            String blockDataSourceField = String.format("EJRJ_BLOCK_DS_%s", block.getName());
            JRDesignField field = new JRDesignField();

            field.setName(blockDataSourceField);
            field.setValueClass(JRDataSource.class);
            design.addField(field);

            JRDefaultStyleProvider styleProvider = new JRDefaultStyleProvider()
            {

                @Override
                public JRStyle getDefaultStyle()
                {
                    return null;
                }
            };
            JRDesignSubreport subreport = new JRDesignSubreport(styleProvider);
            subreport.setPositionType(PositionTypeEnum.FLOAT);
            subreport.setKey(block.getName());
            subreport.setRemoveLineWhenBlank(true);

            JRDesignExpression expressionDS = new JRDesignExpression();
            expressionDS.setText(String.format("$F{%s}", blockDataSourceField));
            subreport.setDataSourceExpression(expressionDS);

            JRDesignExpression expressionRPT = new JRDesignExpression();
            expressionRPT.setText(String.format("$P{%s}", String.format("EJRJ_BLOCK_RPT_%s", block.getName())));
            subreport.setExpression(expressionRPT);

            for (EJReportRuntimeLevelParameter parameter : report.getRuntimeLevelParameters())
            {
                JRDesignSubreportParameter subreportParameter = new JRDesignSubreportParameter();
                subreportParameter.setName(parameter.getName());
                JRDesignExpression expression = new JRDesignExpression();
                expression.setText(String.format("$P{%s}", parameter.getName()));
                subreportParameter.setExpression(expression);

                subreport.addParameter(subreportParameter);
            }

            EJReportParameterList parameterList = report.getParameterList();
            Collection<EJReportParameter> allParameters = parameterList.getAllParameters();
            for (EJReportParameter parameter : allParameters)
            {
                JRDesignSubreportParameter subreportParameter = new JRDesignSubreportParameter();
                subreportParameter.setName(parameter.getName());
                JRDesignExpression expression = new JRDesignExpression();
                expression.setText(String.format("$P{%s}", parameter.getName()));
                subreportParameter.setExpression(expression);

                subreport.addParameter(subreportParameter);
            }

            Collection<EJReportBlock> allBlocks = report.getAllBlocks();
            for (EJReportBlock ejReportBlock : allBlocks)
            {
                String blockRPTParam = String.format("EJRJ_BLOCK_RPT_%s", ejReportBlock.getName());
                JRDesignSubreportParameter subreportParameter = new JRDesignSubreportParameter();
                subreportParameter.setName(blockRPTParam);
                JRDesignExpression expression = new JRDesignExpression();
                expression.setText(String.format("$P{%s}", blockRPTParam));
                subreportParameter.setExpression(expression);
                subreport.addParameter(subreportParameter);
            }
            return subreport;

        }

        return null;
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

        EJReportParameterList parameterList = report.getParameterList();
        Collection<EJReportParameter> allParameters = parameterList.getAllParameters();
        for (EJReportParameter parameter : allParameters)
        {
            JRDesignParameter designParameter = new JRDesignParameter();
            designParameter.setName(parameter.getName());
            designParameter.setValueClass(parameter.getDataType());
            design.addParameter(designParameter);
        }
        Collection<EJReportBlock> allBlocks = report.getAllBlocks();
        for (EJReportBlock block : allBlocks)
        {
            createBlockRPTParamater(block);
        }
    }

    void createBlockRPTParamater(EJReportBlock block) throws JRException
    {
        String blockRPTParam = String.format("EJRJ_BLOCK_RPT_%s", block.getName());

        JRDesignParameter rptParameter = new JRDesignParameter();
        rptParameter.setName(blockRPTParam);
        rptParameter.setValueClass(JasperReport.class);
        design.addParameter(rptParameter);

    }

    public void buildDesign(EJReportBlock block)
    {

        try
        {
            defaultLocale = block.getReport().getFrameworkManager().getCurrentLocale();
            createParamaters(block.getReport());
            design.setName(block.getName());
            design.setTopMargin(0);
            design.setBottomMargin(0);
            design.setLeftMargin(0);
            design.setRightMargin(0);
         
            addDefaultFont(block.getReport());

            EJCoreReportBlockProperties properties = block.getProperties();
            Collection<EJReportBlockItem> blockItems = block.getBlockItems();

            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_VA_CONTEXT");
                field.setValueClass(org.entirej.report.jasper.data.EjReportBlockItemVAContext.class);
                design.addField(field);
            }

            for (EJReportBlockItem item : blockItems)
            {

                JRDesignField field = new JRDesignField();

                field.setName(String.format("%s.%s", block.getName(), item.getName()));
                field.setDescription(item.getFieldName());
                field.setValueClass(Object.class);
                design.addField(field);
            }

            if (properties.getLayoutScreenProperties().getScreenType() == EJReportScreenType.FORM_LATOUT)
                createFormLayout(block, properties);
            else if (properties.getLayoutScreenProperties().getScreenType() == EJReportScreenType.TABLE_LAYOUT)
                createTableLayout(block, properties);

        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    private void createTableLayout(EJReportBlock block, EJCoreReportBlockProperties properties) throws JRException
    {
        EJCoreReportScreenProperties screenProperties = properties.getLayoutScreenProperties();

        Collection<? extends EJReportColumnProperties> allColumns = screenProperties.getAllColumns();
        // create all ref fields

        boolean addHeaderBand = false;
        boolean addFooterBand = false;
        int headerHeight = screenProperties.getHeaderColumnHeight();
        int detailHeight = screenProperties.getDetailColumnHeight();
        int footerHeight = screenProperties.getFooterColumnHeight();

        JRDesignStyle oddEvenRowStyle = createOddEvenRowStyle(screenProperties);

        for (EJReportColumnProperties col : allColumns)
        {
            if (col.isShowHeader())
            {
                if (headerHeight < col.getHeaderScreen().getHeight())
                {
                    headerHeight = col.getHeaderScreen().getHeight();
                }
                addHeaderBand = true;
                for (EJReportScreenItemProperties item : col.getHeaderScreen().getScreenItems())
                {
                    if (headerHeight < (item.getY() + item.getHeight()))
                    {
                        headerHeight = (item.getY() + item.getHeight());
                    }
                    crateValueRefField((EJCoreReportScreenItemProperties) item);
                }
            }

            // details---
            if (detailHeight < col.getDetailScreen().getHeight())
            {
                detailHeight = col.getDetailScreen().getHeight();
            }
            for (EJReportScreenItemProperties item : col.getDetailScreen().getScreenItems())
            {

                if (detailHeight < (item.getY() + item.getHeight()))
                {
                    detailHeight = (item.getY() + item.getHeight());
                }
                crateValueRefField((EJCoreReportScreenItemProperties) item);
            }
            // ----------
            if (col.isShowFooter())
            {
                if (footerHeight < col.getFooterScreen().getHeight())
                {
                    footerHeight = col.getFooterScreen().getHeight();
                }
                addFooterBand = true;
                for (EJReportScreenItemProperties item : col.getFooterScreen().getScreenItems())
                {
                    if (footerHeight < (item.getY() + item.getHeight()))
                    {
                        footerHeight = (item.getY() + item.getHeight());
                    }
                    crateValueRefField((EJCoreReportScreenItemProperties) item);
                }
            }

        }

        JRDesignBand header = null;
        JRDesignBand detail = null;
        JRDesignBand footer = null;

        if (addHeaderBand)
        {
            header = new JRDesignBand();
            header.setSplitType(SplitTypeEnum.STRETCH);
            header.setHeight(headerHeight);
            design.setColumnHeader(header);
        }
        if (addFooterBand)
        {
            footer = new JRDesignBand();
            footer.setSplitType(SplitTypeEnum.STRETCH);
            footer.setHeight(footerHeight);
            design.setColumnFooter(footer);
        }
        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        detail = new JRDesignBand();
        detail.setSplitType(SplitTypeEnum.STRETCH);
        detailSection.addBand(detail);
        detail.setHeight(detailHeight);
        boolean addBrake = screenProperties.isStartOnNewPage();
        if(addBrake)
        {
            
            JRDesignBand band = new JRDesignBand();
            band.setHeight(1);
            addPageBreak(band, block);
            design.setTitle(band);
        }
        
        
        int currentX = 0;
        for (EJReportColumnProperties col : allColumns)
        {

            int width = col.getDetailScreen().getWidth();

            if (col.isShowHeader())
            {
                if (width < col.getHeaderScreen().getWidth())
                {
                    width = col.getHeaderScreen().getWidth();
                }

                @SuppressWarnings("unchecked")
                Collection<EJCoreReportScreenItemProperties> screenItems = (Collection<EJCoreReportScreenItemProperties>) col.getHeaderScreen()
                        .getScreenItems();

                for (EJCoreReportScreenItemProperties item : screenItems)
                {

                    if (width < (item.getX() + item.getWidth()))
                    {
                        width = (item.getX() + item.getWidth());
                    }

                    JRDesignElement element = createScrrenItem(block, item);

                    if (element != null)
                    {
                      

                        element.setX(currentX + item.getX());
                        element.setY(item.getY());
                        element.setWidth(item.getWidth());
                        element.setHeight(item.getHeight());
                        header.addElement(element);

                        processItemStyle(item, element);

                        element.setPositionType(PositionTypeEnum.FLOAT);
                        element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                    }

                }

            }

            {

                if (width < col.getDetailScreen().getWidth())
                {
                    width = col.getDetailScreen().getWidth();
                }

                @SuppressWarnings("unchecked")
                Collection<EJCoreReportScreenItemProperties> screenItems = (Collection<EJCoreReportScreenItemProperties>) col.getDetailScreen()
                        .getScreenItems();

                for (EJCoreReportScreenItemProperties item : screenItems)
                {

                    if (width < (item.getX() + item.getWidth()))
                    {
                        width = (item.getX() + item.getWidth());
                    }

                    JRDesignElement element = createScrrenItem(block, item);

                    if (element != null)
                    {
                       
                        element.setX(currentX + item.getX());
                        element.setY(item.getY());
                        element.setWidth(item.getWidth());
                        element.setHeight(item.getHeight());
                        detail.addElement(element);
                        processItemStyle(item, element);

                        /*
                         * if(element.getStyle() instanceof JRDesignStyle) {
                         * JRDesignStyle style = (JRDesignStyle)
                         * element.getStyle();
                         * 
                         * EJReportVisualAttributeProperties vaOdd =
                         * screenProperties.getOddVAProperties();
                         * EJReportVisualAttributeProperties vaEven =
                         * screenProperties.getEvenVAProperties(); if (vaOdd !=
                         * null || vaEven != null) {
                         * 
                         * 
                         * 
                         * buildOddEvenStyle(screenProperties, vaOdd, vaEven,
                         * style);
                         * 
                         * 
                         * } }
                         */

                        element.setPositionType(PositionTypeEnum.FLOAT);
                        element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                    }

                }

            }

            if (col.isShowFooter())
            {
                if (width < col.getFooterScreen().getWidth())
                {
                    width = col.getFooterScreen().getWidth();
                }

                @SuppressWarnings("unchecked")
                Collection<EJCoreReportScreenItemProperties> screenItems = (Collection<EJCoreReportScreenItemProperties>) col.getFooterScreen()
                        .getScreenItems();

                for (EJCoreReportScreenItemProperties item : screenItems)
                {

                    if (width < (item.getX() + item.getWidth()))
                    {
                        width = (item.getX() + item.getWidth());
                    }

                    JRDesignElement element = createScrrenItem(block, item);

                    if (element != null)
                    {
                        
                        element.setX(currentX + item.getX());
                        element.setY(item.getY());
                        element.setWidth(item.getWidth());
                        element.setHeight(item.getHeight());
                        footer.addElement(element);
                        processItemStyle(item, element);

                        element.setPositionType(PositionTypeEnum.FLOAT);
                        element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                    }

                }

            }

            if (addHeaderBand)
            {
                createColumnLines(headerHeight, header, currentX, width, col.getHeaderBorderProperties());
            }

            createColumnLines(detailHeight, detail, currentX, width, col.getDetailBorderProperties());

            if (addFooterBand)
            {
                createColumnLines(footerHeight, footer, currentX, width, col.getFooterBorderProperties());
            }

            if (oddEvenRowStyle != null)
            {
                JRDesignStaticText box = new JRDesignStaticText();
                box.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
                box.setStyle(oddEvenRowStyle);
                box.setX(currentX);
                box.setY(0);
                box.setWidth(width);
                box.setHeight(detailHeight);
                detail.addElement(0, box);
            }

            currentX += width;
        }

    }

    private void addPageBreak(JRDesignBand band, EJReportBlock block)
    {
        
        JRDesignStaticText text = new JRDesignStaticText();
        text.setHeight(1);
        text.setWidth(1);
        JRDesignBreak breakPage = new JRDesignBreak();
        text.getPropertiesMap().setProperty(JRXlsAbstractExporter.PROPERTY_BREAK_BEFORE_ROW,"true");
        JRDesignPropertyExpression expression = new JRDesignPropertyExpression();
        expression.setName("net.sf.jasperreports.export.xls.sheet.name");
        expression.setValueExpression(createTextExpression(block.getName()));
        text.addPropertyExpression(expression);
        text.setX(0);
        text.setY(0);
        breakPage.setX(0);
        breakPage.setY(0);
        breakPage.setType(BreakTypeEnum.PAGE);
        band.addElement(breakPage);
        band.addElement(text);
       text.setMode(ModeEnum.TRANSPARENT);
        
    }

    private void processItemStyle(EJCoreReportScreenItemProperties item, JRDesignElement element) throws JRException
    {

        JRDesignStyle style = (JRDesignStyle) element.getStyle();
        EJReportVisualAttributeProperties va = item.getVisualAttributeProperties();
        if (va != null)
        {

            vaToStyle(va, style);
        }
        if (item instanceof ValueBaseItem)
        {
            ValueBaseItem valueBaseItem = (ValueBaseItem) item;

            String defaultValue = valueBaseItem.getValue();
            if (valueBaseItem.getValue() != null && valueBaseItem.getValue().length() > 0)
            {
                String paramTypeCode = defaultValue.substring(0, defaultValue.indexOf(':'));
                String paramValue = defaultValue.substring(defaultValue.indexOf(':') + 1);
                if ("BLOCK_ITEM".equals(paramTypeCode))
                {
                    createItemBaseStyle(style, paramValue);

                    // element.setStyle(style);

                }

            }
        }
    }

    private JRDesignStyle createItemBaseStyle(JRDesignStyle style, String paramValue) throws JRException
    {

        Collection<EJReportVisualAttributeProperties> visualAttributes = EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer()
                .getVisualAttributes();
        for (EJReportVisualAttributeProperties properties : visualAttributes)
        {
            if (properties.isUsedAsDynamicVA())
            {
                JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
                conditionalStyle.setConditionExpression(createItemVAExpression(paramValue, properties.getName()));
                vaToStyle(properties, conditionalStyle);

                style.addConditionalStyle(conditionalStyle);
            }
        }

        return style;
    }

    private JRDesignStyle createOddEvenRowStyle(EJCoreReportScreenProperties screenProperties) throws JRException
    {

        EJReportVisualAttributeProperties vaOdd = screenProperties.getOddVAProperties();
        EJReportVisualAttributeProperties vaEven = screenProperties.getEvenVAProperties();
        if (vaOdd != null || vaEven != null)
        {

            JRDesignStyle style = new JRDesignStyle();

            buildOddEvenStyle(screenProperties, vaOdd, vaEven, style);
            design.addStyle(style);
            return style;

        }
        return null;
    }

    private void buildOddEvenStyle(EJCoreReportScreenProperties screenProperties, EJReportVisualAttributeProperties vaOdd,
            EJReportVisualAttributeProperties vaEven, JRDesignStyle style)
    {
        if (vaOdd != null)
        {
            style.setName(String.format("%s.odd", screenProperties.getBlockProperties().getName()));

            JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
            vaToStyle(vaOdd, conditionalStyle);

            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("new Boolean($V{REPORT_COUNT}.intValue()%2!=0)");
            conditionalStyle.setConditionExpression(expression);

            style.addConditionalStyle(conditionalStyle);
        }
        if (vaEven != null)
        {
            style.setName(String.format("%s.even", screenProperties.getBlockProperties().getName()));

            JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
            vaToStyle(vaEven, conditionalStyle);

            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("new Boolean($V{REPORT_COUNT}.intValue()%2==0)");
            conditionalStyle.setConditionExpression(expression);

            style.addConditionalStyle(conditionalStyle);
        }
    }

    private void createColumnLines(int bandHeight, JRDesignBand band, int currentX, int width, EJReportBorderProperties borderProperties) throws JRException
    {

        EJReportVisualAttributeProperties va = borderProperties.getVisualAttributeProperties();
        JRDesignStyle style = null;
        if (va != null)
        {

            style = toStyle(va);

        }
        if (borderProperties.isShowLeftLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) borderProperties.getLineWidth());
            switch (borderProperties.getLineStyle())
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
            line.setX(currentX);
            line.setY(0);
            line.setWidth(borderProperties.getLineWidth() < 1 ? 1 : (int) Math.floor(borderProperties.getLineWidth()));
            line.setHeight(bandHeight);

            if (style != null)
            {
                line.setStyle(style);
            }
            line.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
            line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
            band.addElement(0, line);
        }
        if (borderProperties.isShowRightLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) borderProperties.getLineWidth());
            switch (borderProperties.getLineStyle())
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

            int lineWidth = borderProperties.getLineWidth() < 1 ? 1 : (int) Math.floor(borderProperties.getLineWidth());
            line.setX((currentX + width) - 1);
            line.setY(0);

            line.setWidth(lineWidth);
            line.setHeight(bandHeight);

            if (style != null)
            {
                line.setStyle(style);
            }
            band.addElement(0, line);
            line.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
            line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
        }
        if (borderProperties.isShowBottomLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) borderProperties.getLineWidth());
            switch (borderProperties.getLineStyle())
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
            line.setX(currentX);
            int lineWidth = borderProperties.getLineWidth() < 1 ? 1 : (int) Math.floor(borderProperties.getLineWidth());
            line.setY(bandHeight - lineWidth);

            line.setWidth(width);
            line.setHeight(lineWidth);

            if (style != null)
            {
                line.setStyle(style);
            }
            line.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
            band.addElement(0, line);
        }
        if (borderProperties.isShowTopLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) borderProperties.getLineWidth());
            switch (borderProperties.getLineStyle())
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
            line.setX(currentX);
            int lineWidth = borderProperties.getLineWidth() < 1 ? 1 : (int) Math.floor(borderProperties.getLineWidth());
            line.setY(0);

            line.setWidth(width);
            line.setHeight(lineWidth);
            line.setPositionType(PositionTypeEnum.FLOAT);
            if (style != null)
            {
                line.setStyle(style);
            }
            band.addElement(0, line);
        }
    }

    private void createFormLayout(EJReportBlock block, EJCoreReportBlockProperties properties) throws JRException
    {
        EJCoreReportScreenProperties screenProperties = properties.getLayoutScreenProperties();
        Collection<EJCoreReportScreenItemProperties> screenItems = screenProperties.getScreenItems();

        for (EJCoreReportScreenItemProperties item : screenItems)
        {
            crateValueRefField(item);
        }
        boolean addBrake = screenProperties.isStartOnNewPage();
        if(addBrake)
        {
            
            JRDesignBand band = new JRDesignBand();
            band.setHeight(1);
            addPageBreak(band, block);
            design.setTitle(band);
        }
        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        JRDesignBand detail = new JRDesignBand();
        detail.setSplitType(SplitTypeEnum.STRETCH);

        EJReportProperties reportProperties = block.getReport().getProperties();
        design.setPageHeight((reportProperties.getReportHeight() - (reportProperties.getMarginTop() + reportProperties.getMarginBottom())));

        detailSection.addBand(detail);

        int width = screenProperties.getWidth();
        int height = screenProperties.getHeight();

        for (EJCoreReportScreenItemProperties item : screenItems)
        {

            if (width < (item.getX() + item.getWidth()))
            {
                width = (item.getX() + item.getWidth());
            }
            if (height < (item.getY() + item.getHeight()))
            {
                height = (item.getY() + item.getHeight());
            }

            JRDesignElement element = createScrrenItem(block, item);

            if (element != null)
            {

                element.setX(item.getX());
                element.setY(item.getY());
                element.setWidth(item.getWidth());
                element.setHeight(item.getHeight());
                element.setPositionType(PositionTypeEnum.FLOAT);
                detail.addElement(element);

                processItemStyle(item, element);

            }

        }

        List<EJCoreReportBlockProperties> allSubBlocks = screenProperties.getAllSubBlocks();
        for (EJCoreReportBlockProperties blockProperties : allSubBlocks)
        {
            EJReportBlock subBlock = block.getReport().getBlock(blockProperties.getName());

            JRDesignSubreport subreport = createSubReport(block.getReport(), subBlock);
            if (subreport == null)
                continue;

            EJCoreReportScreenProperties sub = blockProperties.getLayoutScreenProperties();
            subreport.setX(sub.getX());
            subreport.setY(sub.getY());
            subreport.setWidth(sub.getWidth());
            subreport.setHeight(sub.getHeight());
            detail.addElement(subreport);
            if (blockProperties.getLayoutScreenProperties().getScreenType() != EJReportScreenType.NONE)
            {
                EJCoreReportScreenProperties layoutScreenProperties = blockProperties.getLayoutScreenProperties();
                if (width < (layoutScreenProperties.getX() + layoutScreenProperties.getWidth()))
                {
                    width = (layoutScreenProperties.getX() + layoutScreenProperties.getWidth());
                }
                if (height < (layoutScreenProperties.getY() + layoutScreenProperties.getHeight()))
                {
                    height = (layoutScreenProperties.getY() + layoutScreenProperties.getHeight());
                }
            }
        }

        detail.setHeight(height);
        design.setPageWidth(width);
        design.setColumnWidth(width);
    }

    private JRDesignStyle toStyle(EJReportVisualAttributeProperties va) throws JRException
    {

        JRDesignStyle style = (JRDesignStyle) design.getStylesMap().get(va.getName());
        if (style != null)
        {
            return style;
        }
        style = new JRDesignStyle();

        style.setName(va.getName());
        design.addStyle(style);
        vaToStyle(va, style);

        return style;
    }

    private void vaToStyle(EJReportVisualAttributeProperties va, JRBaseStyle style)
    {
        Color backgroundColor = va.getBackgroundColor();
        if (backgroundColor != null)
        {
            style.setBackcolor(backgroundColor);
            style.setMode(ModeEnum.OPAQUE);
        }
        Color foregroundColor = va.getForegroundColor();
        if (foregroundColor != null)
        {
            style.setForecolor(foregroundColor);
        }

        String fontName = va.getFontName();
        if (!EJReportVisualAttributeProperties.UNSPECIFIED.equals(fontName))
        {
            style.setFontName(fontName);
            style.setPdfEmbedded(true);
        }
        else
        {
            style.setFontName("Arial");
            style.setPdfFontName("Helvetica");
            style.setPdfEmbedded(true);
        }

        float fontSize = va.getFontSize();
        if (fontSize != -1)
        {
            style.setFontSize(fontSize);
        }

        if (va.getMarkupType() == EJReportMarkupType.STYLE)
        {
            style.setMarkup("styled");
        }

        switch (va.getHAlignment())
        {
            case LEFT:
                style.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                break;
            case RIGHT:
                style.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                break;
            case CENTER:
                style.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                break;
            case JUSTIFIED:
                style.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
                break;

            default:
                break;
        }
        switch (va.getVAlignment())
        {
            case TOP:
                style.setVerticalAlignment(VerticalAlignEnum.TOP);
                break;
            case BOTTOM:
                style.setVerticalAlignment(VerticalAlignEnum.BOTTOM);
                break;
            case CENTER:
                style.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
            case JUSTIFIED:
                style.setVerticalAlignment(VerticalAlignEnum.JUSTIFIED);
                break;

            default:
                break;
        }

        EJReportFontStyle fontStyle = va.getFontStyle();
        switch (fontStyle)
        {
            case Italic:
                style.setItalic(true);
                break;
            case Underline:
                style.setUnderline(true);
                break;
            case StrikeThrough:
                style.setStrikeThrough(true);
                break;

            default:
                break;
        }

        EJReportFontWeight fontWeight = va.getFontWeight();

        switch (fontWeight)
        {
            case Bold:
                style.setBold(true);

            default:
                break;
        }

        if (va.getManualPattern() != null && !va.getManualPattern().isEmpty())
        {
            style.setPattern(va.getManualPattern());
        }
        else
        {
            SimpleDateFormat dateFormat = null;
            switch (va.getLocalePattern())
            {
                case CURRENCY:
                    style.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance(defaultLocale)).toPattern());
                    break;
                case PERCENT:
                    style.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getPercentInstance(defaultLocale)).toPattern());
                    break;
                case INTEGER:
                    style.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getIntegerInstance(defaultLocale)).toPattern());
                    break;
                case NUMBER:
                    style.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getNumberInstance(defaultLocale)).toPattern());
                    break;

                case DATE_FULL:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.FULL, defaultLocale));
                    break;
                case DATE_LONG:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.LONG, defaultLocale));
                    break;
                case DATE_MEDIUM:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.MEDIUM, defaultLocale));
                    break;
                case DATE_SHORT:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.SHORT, defaultLocale));
                    break;
                case DATE_TIME_FULL:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, defaultLocale));
                    break;
                case DATE_TIME_LONG:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, defaultLocale));
                    break;
                case DATE_TIME_MEDIUM:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, defaultLocale));
                    break;
                case DATE_TIME_SHORT:
                    dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, defaultLocale));
                    break;

                case TIME_FULL:
                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.FULL, defaultLocale));
                    break;
                case TIME_LONG:
                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.LONG, defaultLocale));
                    break;
                case TIME_MEDIUM:
                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.MEDIUM, defaultLocale));
                    break;
                case TIME_SHORT:
                    dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.SHORT, defaultLocale));
                    break;

                default:
                    break;
            }

            if (dateFormat != null)
            {
                style.setPattern(dateFormat.toPattern());
            }
        }
    }

    private void crateValueRefField(EJCoreReportScreenItemProperties item) throws JRException
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

    private void configMarkup(JRDesignStyle textField, EJCoreReportScreenItemProperties item)
    {
        // http://jasperreports.sourceforge.net/sample.reference/markup/
        if (item instanceof ValueBaseItem)
        {
            ValueBaseItem vaItem = (ValueBaseItem) item;

            switch (vaItem.getMarkup())
            {

                case STYLE:
                    textField.setMarkup("styled");
                    break;

            }
        }
    }

    private JRDesignStyle createScreenItemStyle() throws JRException
    {
        JRDesignStyle style = new JRDesignStyle();

        style.setName(UUID.randomUUID().toString());
        design.addStyle(style);

        return style;
    }

    private JRDesignElement createScrrenItem(EJReportBlock block, EJCoreReportScreenItemProperties item) throws JRException
    {
        JRDesignElement element = null;
        JRDesignStyle itemStyle = createScreenItemStyle();
        EJReportVisualAttributeProperties properties = block.getReport().getProperties().getVisualAttributeProperties();
        if (properties != null)
        {
            vaToStyle(properties, itemStyle);
        }
        switch (item.getType())
        {
            case TEXT:
            {
                EJCoreReportScreenItemProperties.Text textItem = (EJCoreReportScreenItemProperties.Text) item;
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                text.setExpression(createValueExpression(block.getReport(), textItem.getValue()));
                text.getParagraph().setRightIndent(5);
                text.getParagraph().setLeftIndent(5);
                setAlignments(itemStyle, textItem);
                setRotation(itemStyle, textItem);
                text.setStretchWithOverflow(textItem.isExpandToFit());
                text.setBlankWhenNull(true);
                configMarkup(itemStyle, item);
            }
                break;
            case NUMBER:
            {
                EJCoreReportScreenItemProperties.Number textItem = (EJCoreReportScreenItemProperties.Number) item;
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                text.setExpression(createValueExpression(block.getReport(), textItem.getValue()));
                text.getParagraph().setRightIndent(5);
                text.getParagraph().setLeftIndent(5);
                setAlignments(itemStyle, textItem);
                setRotation(itemStyle, textItem);
                text.setBlankWhenNull(true);
                text.setStretchWithOverflow(textItem.isExpandToFit());

                if (textItem.getManualFormat() != null && !textItem.getManualFormat().isEmpty())
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
                            text.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance(defaultLocale)).toPattern());
                            break;
                        case PERCENT:
                            text.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getPercentInstance(defaultLocale)).toPattern());
                            break;
                        case INTEGER:
                            text.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getIntegerInstance(defaultLocale)).toPattern());
                            break;
                        case NUMBER:
                            text.setPattern(((java.text.DecimalFormat) java.text.NumberFormat.getNumberInstance(defaultLocale)).toPattern());
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
                text.getParagraph().setRightIndent(5);
                text.getParagraph().setLeftIndent(5);
                setAlignments(itemStyle, textItem);
                setRotation(itemStyle, textItem);
                text.setBlankWhenNull(true);
                text.setStretchWithOverflow(textItem.isExpandToFit());
                Locale defaultLocale = block.getReport().getFrameworkManager().getCurrentLocale();

                if (textItem.getManualFormat() != null && !textItem.getManualFormat().isEmpty())
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
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.FULL, defaultLocale));
                            break;
                        case DATE_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.LONG, defaultLocale));
                            break;
                        case DATE_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.MEDIUM, defaultLocale));
                            break;
                        case DATE_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateInstance(DateFormat.SHORT, defaultLocale));
                            break;
                        case DATE_TIME_FULL:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, defaultLocale));
                            break;
                        case DATE_TIME_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, defaultLocale));
                            break;
                        case DATE_TIME_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, defaultLocale));
                            break;
                        case DATE_TIME_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, defaultLocale));
                            break;

                        case TIME_FULL:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.FULL, defaultLocale));
                            break;
                        case TIME_LONG:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.LONG, defaultLocale));
                            break;
                        case TIME_MEDIUM:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.MEDIUM, defaultLocale));
                            break;
                        case TIME_SHORT:
                            dateFormat = (SimpleDateFormat) (DateFormat.getTimeInstance(DateFormat.SHORT, defaultLocale));
                            break;
                    }
                    if (dateFormat != null)
                    {
                        text.setPattern(dateFormat.toPattern());
                    }
                }
            }
                break;
            case LABEL:
            {
                EJCoreReportScreenItemProperties.Label labelItem = (Label) item;
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                text.getParagraph().setRightIndent(5);
                text.getParagraph().setLeftIndent(5);
                text.setStretchWithOverflow(true);
                text.setExpression(createTextExpression(labelItem.getText()));
                setAlignments(itemStyle, labelItem);
                setRotation(itemStyle, labelItem);
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
                JRDesignImage image = new JRDesignImage(styleProvider);

                element = image;

                image.setExpression(createImageValueExpression(block.getReport(), imageItem.getValue()));

                image.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
                image.setUsingCache(true);
                setAlignments(itemStyle, imageItem);
            }
                break;
            default:
                break;
        }
        element.setStyle(itemStyle);
        return element;
    }

    void setAlignments(JRDesignStyle elm, AlignmentBaseItem alignmentBaseItem)
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

    void setRotation(JRDesignStyle elm, RotatableItem rotatableItem)
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

    JRDesignExpression createTextExpression(String defaultValue)
    {
        JRDesignExpression expression = new JRDesignExpression();

        if (defaultValue == null || defaultValue.trim().length() == 0)
        {
            return expression;
        }

        expression.setText("\"" + defaultValue.replaceAll("\"", "\\\\\"") + "\"");
        return expression;
    }

    JRDesignExpression createItemVAExpression(String item, String vaName)
    {
        JRDesignExpression expression = new JRDesignExpression();

        expression.setText(String.format("($F{_EJ_VA_CONTEXT}).isActive(\"%s\",\"%s\")", item, vaName));
        return expression;
    }

    JRDesignExpression createImageValueExpression(EJReport report, String defaultValue)
    {
        JRDesignExpression expression = createValueExpression(report, defaultValue);

        if (expression.getText() != null && !expression.getText().isEmpty())
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
