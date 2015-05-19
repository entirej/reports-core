package org.entirej.report.jasper.builder;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
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
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
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
import org.entirej.framework.report.EJReportAlignmentBaseScreenItem;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportBlockItem;
import org.entirej.framework.report.EJReportDateScreenItem;
import org.entirej.framework.report.EJReportImageScreenItem;
import org.entirej.framework.report.EJReportLabelScreenItem;
import org.entirej.framework.report.EJReportLineScreenItem;
import org.entirej.framework.report.EJReportNumberScreenItem;
import org.entirej.framework.report.EJReportPage;
import org.entirej.framework.report.EJReportParameterList;
import org.entirej.framework.report.EJReportRectangleScreenItem;
import org.entirej.framework.report.EJReportRotatableScreenItem;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.EJReportScreen;
import org.entirej.framework.report.EJReportScreenChart;
import org.entirej.framework.report.EJReportScreenColumn;
import org.entirej.framework.report.EJReportScreenColumnSection;
import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.EJReportTextScreenItem;
import org.entirej.framework.report.EJReportValueBaseScreenItem;
import org.entirej.framework.report.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.report.data.controllers.EJReportActionController;
import org.entirej.framework.report.data.controllers.EJReportParameter;
import org.entirej.framework.report.enumerations.EJReportChartType;
import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportProperties;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Date.DateFormats;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineDirection;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Number.NumberFormats;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;
import org.entirej.report.jasper.data.EJReportActionContext;
import org.entirej.report.jasper.data.EJReportBlockContext;

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
            design.setSummaryNewPage(false);
            design.setPageFooter(null);
            design.setSummary(null);
            design.setColumnHeader(null);
            design.setColumnFooter(null);
            design.setNoData(null);
            design.setTitle(null);
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
            // int height = properties.getReportHeight() -
            // (properties.getMarginTop() + properties.getMarginBottom());
            design.setPageWidth(properties.getReportWidth());
            design.setColumnWidth(width);
            design.setPageHeight(properties.getReportHeight());

            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_AP_CONTEXT");
                field.setValueClass(EJReportActionContext.class);
                design.addField(field);
            }

            JRDesignBand header = null;
            if (properties.getHeaderSectionHeight() > 0)
            {
                header = new JRDesignBand();
                // header.setSplitType(SplitTypeEnum.STRETCH);
                header.setHeight(properties.getHeaderSectionHeight());
                design.setPageHeader(header);

            }
            JRDesignBand footer = null;
            if (properties.getFooterSectionHeight() > 0)
            {
                footer = new JRDesignBand();
                // footer.setSplitType(SplitTypeEnum.STRETCH);
                footer.setHeight(properties.getFooterSectionHeight());
                design.setPageFooter(footer);

            }

            for (EJReportBlock block : report.getHeaderBlocks())
            {
                JRDesignSubreport subreport = createSubReport(report, block, true);
                if (subreport == null)
                    continue;
                EJReportScreen screen = block.getScreen();

                subreport.setX(screen.getXPos());
                subreport.setY(screen.getYPos());
                subreport.setWidth(screen.getWidth());
                subreport.setHeight(screen.getHeight());
                subreport.setStretchType(StretchTypeEnum.NO_STRETCH);

                header.addElement(subreport);
            }
            for (EJReportBlock block : report.getFooterBlocks())
            {
                JRDesignSubreport subreport = createSubReport(report, block, true);
                if (subreport == null)
                    continue;
                EJReportScreen screen = block.getScreen();

                subreport.setX(screen.getXPos());
                subreport.setY(screen.getYPos());
                subreport.setWidth(screen.getWidth());
                subreport.setHeight(screen.getHeight());
                subreport.setStretchType(StretchTypeEnum.NO_STRETCH);
                footer.addElement(subreport);
            }

            boolean addPageBrake = false;
            for (EJReportPage page : report.getPages())
            {

                JRDesignGroup group = new JRDesignGroup();
                group.setName(page.getName());
                JRDesignSection groupHeaderSection = (JRDesignSection) group.getGroupHeaderSection();

                JRDesignBand newPageBand = null;
                if (addPageBrake)
                {
                    newPageBand = new JRDesignBand();
                    newPageBand.setHeight(1);
                    groupHeaderSection.addBand(newPageBand);
                }

                JRDesignBand detail = new JRDesignBand();
                detail.setSplitType(SplitTypeEnum.IMMEDIATE);

                groupHeaderSection.addBand(detail);
                design.addGroup(group);

                int bandHeight = 0;
                Collection<EJReportBlock> rootbBlocks = page.getRootBlocks();
                boolean usePageBrake = addPageBrake;
                for (EJReportBlock block : rootbBlocks)
                {

                    if (usePageBrake)
                    {
                        group.setStartNewPage(true);

                        JRDesignStaticText text = new JRDesignStaticText();
                        text.setHeight(1);
                        text.setWidth(block.getScreen().getWidth());

                        text.getPropertiesMap().setProperty(JRXlsAbstractExporter.PROPERTY_BREAK_BEFORE_ROW, "true");
                        JRDesignPropertyExpression expression = new JRDesignPropertyExpression();
                        expression.setName("net.sf.jasperreports.export.xls.sheet.name");
                        expression.setValueExpression(createTextExpression(page.getName()));
                        text.addPropertyExpression(expression);
                        text.setX(0);
                        text.setY(0);

                        newPageBand.addElement(text);
                        text.setMode(ModeEnum.TRANSPARENT);
                        usePageBrake = false;
                    }
                    JRDesignSubreport subreport = createSubReport(report, block, false);
                    if (subreport == null)
                        continue;
                    EJReportScreen screen = block.getScreen();

                    subreport.setX(screen.getXPos());
                    subreport.setY(screen.getYPos());
                    subreport.setWidth(screen.getWidth());
                    subreport.setHeight(screen.getHeight());

                    if (bandHeight < (screen.getYPos() + screen.getHeight()))
                    {
                        bandHeight = (screen.getYPos() + screen.getHeight());
                    }
                    detail.addElement(subreport);

                    subreport.getPropertiesMap().setProperty("net.sf.jasperreports.export.xls.break.before.row", "true");
                }
                detail.setHeight(bandHeight);
                addPageBrake = true;
            }
            // JasperDesignViewer.viewReportDesign(design);

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

    private JRDesignSubreport createSubReport(EJReport report, EJReportBlock block, boolean fixed) throws JRException
    {
        EJReportScreen screen = block.getScreen();

        if (screen.getType() != EJReportScreenType.NONE)
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

            expressionRPT.setText(String.format(!fixed ? "$P{EJRJ_BLOCK_RPT}.getBlockReport(\"%s\")" : "$P{EJRJ_BLOCK_RPT}.getBlockReportFixed(\"%s\")",
                    block.getName()));
            subreport.setExpression(expressionRPT);

            for (EJApplicationLevelParameter parameter : report.getApplicationLevelParameters())
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

            String blockRPTParam = String.format("EJRJ_BLOCK_RPT");
            JRDesignSubreportParameter subreportParameter = new JRDesignSubreportParameter();
            subreportParameter.setName(blockRPTParam);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("$P{EJRJ_BLOCK_RPT}");
            subreportParameter.setExpression(expression);
            subreport.addParameter(subreportParameter);

            subreport.setPrintWhenExpression(createBlockVisibleExpression(block.getName()));
            return subreport;

        }

        return null;
    }

    void createParamaters(EJReport report) throws JRException
    {

        Collection<EJApplicationLevelParameter> runtimeLevelParameters = report.getApplicationLevelParameters();

        for (EJApplicationLevelParameter parameter : runtimeLevelParameters)
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

        createBlockRPTParamater();

    }

    void createBlockRPTParamater() throws JRException
    {
        String blockRPTParam = "EJRJ_BLOCK_RPT";

        JRDesignParameter rptParameter = new JRDesignParameter();
        rptParameter.setName(blockRPTParam);
        rptParameter.setValueClass(EJReportBlockContext.class);
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
            design.setSummaryNewPage(false);
            design.setPageFooter(null);
            design.setSummary(null);
            design.setColumnHeader(null);
            design.setColumnFooter(null);
            design.setNoData(null);
            design.setTitle(null);

            addDefaultFont(block.getReport());

            // EJCoreReportBlockProperties properties = block.getProperties();
            Collection<EJReportBlockItem> blockItems = block.getItems();

            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_VA_CONTEXT");
                field.setValueClass(org.entirej.report.jasper.data.EJReportBlockItemVAContext.class);
                design.addField(field);
            }
            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_AP_CONTEXT");
                field.setValueClass(EJReportActionContext.class);
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

            if (block.getScreen().getType() == EJReportScreenType.FORM_LAYOUT)
            {
                createFormLayout(block, false);
            }
            else if (block.getScreen().getType() == EJReportScreenType.TABLE_LAYOUT)
            {
                createTableLayout(block);
            }
            else if (block.getScreen().getType() == EJReportScreenType.CHART_LAYOUT)
            {
                createChartLayout(block, false);
            }
        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    public void buildDesignFixed(EJReportBlock block)
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
            design.setSummaryNewPage(false);
            design.setPageFooter(null);
            design.setSummary(null);
            design.setColumnHeader(null);
            design.setColumnFooter(null);
            design.setNoData(null);
            design.setTitle(null);

            addDefaultFont(block.getReport());

            // EJCoreReportBlockProperties properties = block.getProperties();
            Collection<EJReportBlockItem> blockItems = block.getItems();

            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_VA_CONTEXT");
                field.setValueClass(org.entirej.report.jasper.data.EJReportBlockItemVAContext.class);
                design.addField(field);
            }
            {

                JRDesignField field = new JRDesignField();

                field.setName("_EJ_AP_CONTEXT");
                field.setValueClass(EJReportActionContext.class);
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

            if (block.getScreen().getType() == EJReportScreenType.FORM_LAYOUT)
            {
                createFormLayout(block, true);
            }
            else if (block.getScreen().getType() == EJReportScreenType.TABLE_LAYOUT)
            {
                throw new EJReportRuntimeException("Header and Footer Sections not support Table layout");
            }
        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    private void createTableLayout(EJReportBlock block) throws JRException
    {
        EJReportScreen screen = block.getScreen();

        List<EJReportScreenColumn> allColumns = new ArrayList<EJReportScreenColumn>();
        EJReportActionController controller = block.getReport().getActionController();

        for (EJReportScreenColumn column : screen.getScreenColumns())
        {
            if (column.isVisible() && controller.canShowScreenColumn(block.getReport(), block.getName(), column.getName()))
            {
                allColumns.add(column);
            }
        }

        // create all ref fields

        boolean addHeaderBand = false;
        boolean addFooterBand = false;
        int headerHeight = screen.getDefaultHeaderHeight();
        int detailHeight = screen.getDefaultDetailHeight();
        int footerHeight = screen.getDefaultFooterHeight();

        JRDesignStyle oddEvenRowStyle = createOddEvenRowStyle(screen);

        boolean canShowBlockHeader = block.getReport().getActionController().canShowBlockHeader(block.getReport(), block.getName());
        boolean canShowBlockFooter = block.getReport().getActionController().canShowBlockFooter(block.getReport(), block.getName());

        for (EJReportScreenColumn col : allColumns)
        {

            if (!col.isVisible())
                continue;

            if (canShowBlockHeader && col.showHeader())
            {
                if (headerHeight < col.getHeaderSection().getHeight())
                {
                    headerHeight = col.getHeaderSection().getHeight();
                }
                addHeaderBand = true;
                for (EJReportScreenItem item : col.getHeaderSection().getScreenItems())
                {
                    if (!item.isVisible())
                        continue;
                    int height = item.getHeight();
                    if (item.isHeightAsPercentage())
                    {
                        height = (int) (((double) screen.getDefaultHeaderHeight() / 100) * height);
                    }
                    if (headerHeight < (item.getYPos() + height))
                    {
                        headerHeight = (item.getYPos() + height);
                    }
                    crateValueRefField(item);
                }
            }

            // details---
            if (detailHeight < col.getDetailSection().getHeight())
            {
                detailHeight = col.getDetailSection().getHeight();
            }
            for (EJReportScreenItem item : col.getDetailSection().getScreenItems())
            {
                if (!item.isVisible())
                    continue;
                int height = item.getHeight();
                if (item.isHeightAsPercentage())
                {
                    height = (int) (((double) screen.getDefaultDetailHeight() / 100) * height);
                }
                if (detailHeight < (item.getYPos() + height))
                {
                    detailHeight = (item.getYPos() + height);
                }
                crateValueRefField(item);
            }
            // ----------
            if (canShowBlockFooter && col.showFooter())
            {
                if (footerHeight < col.getFooterSection().getHeight())
                {
                    footerHeight = col.getFooterSection().getHeight();
                }
                addFooterBand = true;
                for (EJReportScreenItem item : col.getFooterSection().getScreenItems())
                {
                    if (!item.isVisible())
                        continue;
                    int height = item.getHeight();
                    if (item.isHeightAsPercentage())
                    {
                        height = (int) (((double) screen.getDefaultFooterHeight() / 100) * height);
                    }
                    if (footerHeight < (item.getYPos() + height))
                    {
                        footerHeight = (item.getYPos() + height);
                    }
                    crateValueRefField(item);
                }
            }

        }

        JRDesignBand header = null;
        JRDesignBand detail = null;
        JRDesignBand footer = null;

        if (addHeaderBand)
        {
            header = new JRDesignBand();
            // header.setSplitType(SplitTypeEnum.PREVENT);
            header.setHeight(headerHeight);
            design.setColumnHeader(header);
        }
        if (addFooterBand)
        {
            footer = new JRDesignBand();
            // footer.setSplitType(SplitTypeEnum.PREVENT);
            footer.setHeight(footerHeight);
            design.setLastPageFooter(footer);
        }
        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        detail = new JRDesignBand();
        detail.setSplitType(SplitTypeEnum.PREVENT);
        detailSection.addBand(detail);
        detail.setHeight(detailHeight);

        int currentX = 0;
        for (EJReportScreenColumn col : allColumns)
        {

            if (!col.isVisible())
                continue;
            final int width = col.getWidth();
            boolean screenColumnSectionH = canShowBlockHeader
                    && block.getReport().getActionController()
                            .canShowScreenColumnSection(block.getReport(), block.getName(), col.getName(), EJReportScreenSection.HEADER);

            boolean screenColumnSectionD = block.getReport().getActionController()
                    .canShowScreenColumnSection(block.getReport(), block.getName(), col.getName(), EJReportScreenSection.DETAIL);
            boolean screenColumnSectionF = canShowBlockFooter
                    && block.getReport().getActionController()
                            .canShowScreenColumnSection(block.getReport(), block.getName(), col.getName(), EJReportScreenSection.FOOTER);

            if (canShowBlockHeader && col.showHeader())
            {

                @SuppressWarnings("unchecked")
                Collection<EJReportScreenItem> screenItems = (Collection<EJReportScreenItem>) col.getHeaderSection().getScreenItems();

                int sectionHeight = col.getHeaderSection().getHeight();
                if (sectionHeight == 0)
                    sectionHeight = screen.getDefaultHeaderHeight();

                for (EJReportScreenItem item : screenItems)
                {
                    if (!item.isVisible())
                    {
                        continue;
                    }
                    int itemWidth = item.getWidth();
                    int itemHeight = item.getHeight();

                    if (item.isWidthAsPercentage())
                    {
                        itemWidth = (int) (((double) width / 100) * itemWidth);
                    }
                    if (item.isHeightAsPercentage())
                    {
                        itemHeight = (int) (((double) sectionHeight / 100) * itemHeight);
                    }

                    if (width < (item.getXPos() + itemWidth))
                    {
                        itemWidth = itemWidth - ((item.getXPos() + itemWidth) - width);
                        if (itemWidth < 0)
                            itemWidth = 0;
                    }

                    if (screenColumnSectionH)
                    {
                        JRDesignElement element = createScreenItem(block, item);

                        if (element != null)
                        {

                            element.setX(currentX + item.getXPos());
                            element.setY(item.getYPos());
                            element.setWidth(itemWidth);

                            element.setHeight(itemHeight);
                            header.addElement(element);

                            processItemStyle(item, element, EJReportScreenSection.HEADER);

                            element.setPositionType(PositionTypeEnum.FLOAT);
                            element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                        }
                    }

                }

            }

            {

                @SuppressWarnings("unchecked")
                Collection<EJReportScreenItem> screenItems = (Collection<EJReportScreenItem>) col.getDetailSection().getScreenItems();

                int sectionHeight = col.getDetailSection().getHeight();
                if (sectionHeight == 0)
                    sectionHeight = screen.getDefaultDetailHeight();
                for (EJReportScreenItem item : screenItems)
                {
                    if (!item.isVisible())
                    {
                        continue;
                    }
                    int itemWidth = item.getWidth();
                    int itemHeight = item.getHeight();

                    if (item.isWidthAsPercentage())
                    {
                        itemWidth = (int) (((double) width / 100) * itemWidth);
                    }
                    if (item.isHeightAsPercentage())
                    {
                        itemHeight = (int) (((double) sectionHeight / 100) * itemHeight);
                    }
                    if (width < (item.getXPos() + itemWidth))
                    {
                        itemWidth = itemWidth - ((item.getXPos() + itemWidth) - width);
                        if (itemWidth < 0)
                            itemWidth = 0;
                    }

                    if (screenColumnSectionD)
                    {
                        JRDesignElement element = createScreenItem(block, item);

                        if (element != null)
                        {

                            element.setX(currentX + item.getXPos());
                            element.setY(item.getYPos());
                            element.setWidth(itemWidth);
                            element.setHeight(itemHeight);
                            detail.addElement(element);
                            processItemStyle(item, element, EJReportScreenSection.DETAIL);

                            /*
                             * if(element.getStyle() instanceof JRDesignStyle) {
                             * JRDesignStyle style = (JRDesignStyle)
                             * element.getStyle();
                             * 
                             * EJReportVisualAttributeProperties vaOdd =
                             * screenProperties.getOddVAProperties();
                             * EJReportVisualAttributeProperties vaEven =
                             * screenProperties.getEvenVAProperties(); if (vaOdd
                             * != null || vaEven != null) {
                             * 
                             * 
                             * 
                             * buildOddEvenStyle(screenProperties, vaOdd,
                             * vaEven, style);
                             * 
                             * 
                             * } }
                             */

                            element.setPositionType(PositionTypeEnum.FLOAT);
                            element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                        }
                    }

                }

            }

            if (canShowBlockFooter && col.showFooter())
            {

                @SuppressWarnings("unchecked")
                Collection<EJReportScreenItem> screenItems = (Collection<EJReportScreenItem>) col.getFooterSection().getScreenItems();

                int sectionHeight = col.getFooterSection().getHeight();
                if (sectionHeight == 0)
                    sectionHeight = screen.getDefaultFooterHeight();
                for (EJReportScreenItem item : screenItems)
                {

                    if (!item.isVisible())
                    {
                        continue;
                    }
                    int itemWidth = item.getWidth();
                    int itemHeight = item.getHeight();

                    if (item.isWidthAsPercentage())
                    {
                        itemWidth = (int) (((double) width / 100) * itemWidth);
                    }
                    if (item.isHeightAsPercentage())
                    {
                        itemHeight = (int) (((double) sectionHeight / 100) * itemHeight);
                    }
                    if (width < (item.getXPos() + itemWidth))
                    {
                        itemWidth = itemWidth - ((item.getXPos() + itemWidth) - width);
                        if (itemWidth < 0)
                            itemWidth = 0;
                    }
                    if (screenColumnSectionF)
                    {
                        JRDesignElement element = createScreenItem(block, item);

                        if (element != null)
                        {

                            element.setX(currentX + item.getXPos());
                            element.setY(item.getYPos());
                            element.setWidth(itemWidth);
                            element.setHeight(itemHeight);
                            footer.addElement(element);
                            processItemStyle(item, element, EJReportScreenSection.FOOTER);

                            element.setPositionType(PositionTypeEnum.FLOAT);
                            element.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
                        }
                    }

                }

            }

            if (addHeaderBand && screenColumnSectionH)
            {
                createColumnLines(headerHeight, header, currentX, width, col.getHeaderSection());
            }

            if (screenColumnSectionD)
                createColumnLines(detailHeight, detail, currentX, width, col.getDetailSection());

            if (addFooterBand && screenColumnSectionF)
            {
                createColumnLines(footerHeight, footer, currentX, width, col.getFooterSection());
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

    private void processItemStyle(EJReportScreenItem item, JRDesignElement element, EJReportScreenSection section) throws JRException
    {

        if (element instanceof JRDesignTextField)
        {

            JRDesignTextField textField = (JRDesignTextField) element;
            JRDesignExpression expression = (JRDesignExpression) textField.getExpression();
            if (!expression.getText().isEmpty())
            {
                String pattern = textField.getPattern();

                textField.setExpression(createVABaseValueExpression(expression, item.getName(), pattern == null ? "" : pattern, section));
            }
        }

        JRDesignStyle style = (JRDesignStyle) element.getStyle();
        EJReportVisualAttributeProperties va = item.getVisualAttributes();
        if (va != null)
        {
            vaToStyle(va, style);
        }
        createScreenItemBaseStyle(style, item.getName(), section);
        element.setPrintWhenExpression(createItemVisibleExpression(item.getName(), section));

        if (element.getPrintWhenExpression() instanceof JRDesignExpression)
        {
            JRDesignExpression visibleExpression = createScreenItemVisibleExpression(item.getBlockName(), item.getName(), section);

            visibleExpression.setText(String.format("%s && %s", visibleExpression.getText(), (element.getPrintWhenExpression().getText())));
            element.setPrintWhenExpression(visibleExpression);
        }
        else
        {
            element.setPrintWhenExpression(createScreenItemVisibleExpression(item.getBlockName(), item.getName(), section));
        }

    }

    private JRDesignStyle createScreenItemBaseStyle(JRDesignStyle style, String item, EJReportScreenSection section) throws JRException
    {
        // style.setMarkup("styled");
        // style.setMode(ModeEnum.OPAQUE);

        Collection<EJCoreReportVisualAttributeProperties> visualAttributes = EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer()
                .getVisualAttributes();
        boolean addDynamicStyle = false;
        for (EJReportVisualAttributeProperties properties : visualAttributes)
        {
            if (properties.isUsedAsDynamicVA())
            {
                addDynamicStyle = true;
            }
            if (properties.isUsedAsDynamicVA() && hasDynamicVAToStyle(properties))
            {
                JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
                conditionalStyle.setConditionExpression(createItemVAExpression(item, properties.getName(), section));
                vaToStyleAligment(properties, conditionalStyle);
                Color backgroundColor = properties.getBackgroundColor();
                if (backgroundColor != null)
                {
                    conditionalStyle.setBackcolor(backgroundColor);
                    conditionalStyle.setMode(ModeEnum.OPAQUE);
                }

                style.addConditionalStyle(conditionalStyle);
            }
        }
        if (addDynamicStyle)
        {
            JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
            conditionalStyle.setConditionExpression(createItemVAExpression(item, null, section));
            conditionalStyle.setMarkup("styled");
            style.addConditionalStyle(conditionalStyle);
        }

        return style;
    }

    private JRDesignStyle createOddEvenRowStyle(EJReportScreen screen) throws JRException
    {

        EJReportVisualAttributeProperties vaOdd = screen.getOddRecotrdVisualAttributes();
        EJReportVisualAttributeProperties vaEven = screen.getEvenRecotrdVisualAttributes();
        if (vaOdd != null || vaEven != null)
        {

            JRDesignStyle style = new JRDesignStyle();

            buildOddEvenStyle(screen, vaOdd, vaEven, style);
            design.addStyle(style);
            return style;

        }
        return null;
    }

    private void buildOddEvenStyle(EJReportScreen screen, EJReportVisualAttributeProperties vaOdd, EJReportVisualAttributeProperties vaEven, JRDesignStyle style)
    {
        if (vaOdd != null)
        {
            style.setName(String.format("%s.odd", screen.getBlockName()));

            JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
            vaToStyle(vaOdd, conditionalStyle);

            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("new Boolean($V{REPORT_COUNT}.intValue()%2!=0)");
            conditionalStyle.setConditionExpression(expression);

            style.addConditionalStyle(conditionalStyle);
        }
        if (vaEven != null)
        {
            style.setName(String.format("%s.even", screen.getBlockName()));

            JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
            vaToStyle(vaEven, conditionalStyle);

            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("new Boolean($V{REPORT_COUNT}.intValue()%2==0)");
            conditionalStyle.setConditionExpression(expression);

            style.addConditionalStyle(conditionalStyle);
        }
    }

    private void createColumnLines(int bandHeight, JRDesignBand band, int currentX, int width, EJReportScreenColumnSection section) throws JRException
    {

        EJReportVisualAttributeProperties va = section.getVisualAttributes();
        JRDesignStyle style = null;
        if (va != null)
        {

            style = toStyle(va);

        }
        if (section.showLeftLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) section.getLineWidth());
            switch (section.getLineStyle())
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
            line.setWidth(section.getLineWidth() < 1 ? 1 : (int) Math.floor(section.getLineWidth()));
            line.setHeight(bandHeight);

            if (style != null)
            {
                line.setStyle(style);
            }
            line.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
            line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
            band.addElement(0, line);
        }
        if (section.showRightLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) section.getLineWidth());
            switch (section.getLineStyle())
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

            int lineWidth = section.getLineWidth() < 1 ? 1 : (int) Math.floor(section.getLineWidth());
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
        if (section.showBottomLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) section.getLineWidth());
            switch (section.getLineStyle())
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
            int lineWidth = section.getLineWidth() < 1 ? 1 : (int) Math.floor(section.getLineWidth());
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
        if (section.showTopLine())
        {
            JRDesignLine line = new JRDesignLine();

            JRPen linePen = line.getLinePen();
            linePen.setLineWidth((float) section.getLineWidth());
            switch (section.getLineStyle())
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
            int lineWidth = section.getLineWidth() < 1 ? 1 : (int) Math.floor(section.getLineWidth());
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

    private void createFormLayout(EJReportBlock block, boolean fixed) throws JRException
    {
        EJReportScreen screen = block.getScreen();
        Collection<EJReportScreenItem> screenItems = screen.getScreenItems();

        for (EJReportScreenItem item : screenItems)
        {
            crateValueRefField(item);
        }

        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        JRDesignBand detail = new JRDesignBand();
        detail.setSplitType(SplitTypeEnum.PREVENT);

        EJReportProperties reportProperties = block.getReport().getProperties();
        design.setPageHeight((reportProperties.getReportHeight() - (reportProperties.getMarginTop() + reportProperties.getMarginBottom())));

        detailSection.addBand(detail);

        int width = screen.getWidth();
        int height = screen.getHeight();

        for (EJReportScreenItem item : screenItems)
        {

            if (!item.isVisible())
            {
                continue;
            }
            int itemWidth = item.getWidth();
            int itemHeight = item.getHeight();

            if (item.isWidthAsPercentage())
            {
                itemWidth = (int) (((double) screen.getWidth() / 100) * itemWidth);
            }
            if (item.isHeightAsPercentage())
            {
                itemHeight = (int) (((double) screen.getHeight() / 100) * itemHeight);
            }

            if (width < (item.getXPos() + itemWidth))
            {
                width = (item.getXPos() + itemWidth);
            }

            if (height < (item.getYPos() + itemHeight))
            {
                if (!fixed)
                    height = (item.getYPos() + itemHeight);
            }

            JRDesignElement element = createScreenItem(block, item);

            if (element != null)
            {

                element.setX(item.getXPos());
                element.setY(item.getYPos());
                element.setWidth(itemWidth);
                element.setHeight(itemHeight);
                if (fixed && height < (item.getYPos() + itemHeight))
                {
                    element.setHeight(itemHeight - ((item.getYPos() + itemHeight) - height));
                }
                element.setPositionType(PositionTypeEnum.FLOAT);
                detail.addElement(element);

                processItemStyle(item, element, EJReportScreenSection.DETAIL);
            }

        }

        List<EJReportBlock> allSubBlocks = screen.getSubBlocks();
        for (EJReportBlock subBlock : allSubBlocks)
        {
            JRDesignSubreport subreport = createSubReport(block.getReport(), subBlock, false);
            if (subreport == null)
                continue;

            EJReportScreen sub = subBlock.getScreen();
            subreport.setX(sub.getXPos());
            subreport.setY(sub.getYPos());
            subreport.setWidth(sub.getWidth());
            subreport.setHeight(sub.getHeight());

            detail.addElement(subreport);
            if (subBlock.getScreen().getType() != EJReportScreenType.NONE)
            {
                EJReportScreen layoutScreen = subBlock.getScreen();
                if (width < (layoutScreen.getXPos() + layoutScreen.getWidth()))
                {
                    width = (layoutScreen.getXPos() + layoutScreen.getWidth());
                }
                if (height < (layoutScreen.getYPos() + layoutScreen.getHeight()))
                {
                    if (!fixed)
                        height = (layoutScreen.getYPos() + layoutScreen.getHeight());
                    else
                    {
                        subreport.setHeight(sub.getHeight() - ((layoutScreen.getYPos() + layoutScreen.getHeight()) - height));
                    }
                }
            }
        }

        detail.setHeight(height);
        design.setPageWidth(width);
        design.setColumnWidth(width);
    }

    private void createChartLayout(EJReportBlock block, boolean fixed) throws JRException
    {
        EJReportScreen screen = block.getScreen();

        JRDesignBand detail = new JRDesignBand();
        detail.setSplitType(SplitTypeEnum.PREVENT);

        EJReportProperties reportProperties = block.getReport().getProperties();
        design.setPageHeight((reportProperties.getReportHeight() - (reportProperties.getMarginTop() + reportProperties.getMarginBottom())));

        int width = screen.getWidth();
        int height = screen.getHeight();

        //
        EJReportScreenChart screenChart = block.getScreen().getReportScreenChart();
        createField(screenChart.getCategoryItem());
        createField(screenChart.getLabelItem());
        createField(screenChart.getSeriesItem());
        createField(screenChart.getValue1Item());
        createField(screenChart.getValue2Item());
        createField(screenChart.getValue3Item());

        JRDesignChart chart = null;

        switch (screenChart.getChartType())
        {
            case BAR_CHART:
            case STACKED_BAR_CHART:
            case STACKED_AREA_CHART:
            case AREA_CHART:
            case LINE_CHART:
            {
                byte chartType = getChartType(screenChart);
                chart = new JRDesignChart(new JRDefaultStyleProvider()
                {

                    @Override
                    public JRStyle getDefaultStyle()
                    {
                        return null;
                    }
                }, chartType);

                JRDesignCategoryDataset data = new JRDesignCategoryDataset(null);

                JRDesignCategorySeries series = new JRDesignCategorySeries();
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getCategoryItem());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("\" \"");
                    }
                    series.setCategoryExpression(expression);
                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getSeriesItem());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("\" \"");
                    }
                    series.setSeriesExpression(expression);
                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getLabelItem());
                    if (expression.getText() != null && !expression.getText().isEmpty())
                    {
                        series.setLabelExpression(expression);
                    }

                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getValue1Item());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("0");
                    }
                    series.setValueExpression(expression);
                }

                data.addCategorySeries(series);
                chart.setDataset(data);

                //
            }

                break;

                
                
                
             
            case XY_AREA_CHART:
            case XY_BAR_CHART:
            case XY_LINE_CHART:
            {
                byte chartType = getChartType(screenChart);
                chart = new JRDesignChart(new JRDefaultStyleProvider()
                {

                    @Override
                    public JRStyle getDefaultStyle()
                    {
                        return null;
                    }
                }, chartType);

                JRDesignXyDataset data = new JRDesignXyDataset(null);

                JRDesignXySeries series = new JRDesignXySeries();
                
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getSeriesItem());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("\" \"");
                    }
                    series.setSeriesExpression(expression);
                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getLabelItem());
                    if (expression.getText() != null && !expression.getText().isEmpty())
                    {
                        series.setLabelExpression(expression);
                    }

                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getValue1Item());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("0");
                    }
                    series.setXValueExpression(expression);
                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getValue2Item());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("0");
                    }
                    series.setYValueExpression(expression);
                }

                data.addXySeries(series);
                chart.setDataset(data);

                //
            }

                break;
            case PIE_CHART:
            {
                chart = new JRDesignChart(new JRDefaultStyleProvider()
                {

                    @Override
                    public JRStyle getDefaultStyle()
                    {
                        return null;
                    }
                }, screenChart.isUse3dView() ? JRDesignChart.CHART_TYPE_PIE3D : JRDesignChart.CHART_TYPE_PIE);

                JRDesignPieDataset data = new JRDesignPieDataset(null);

                JRDesignPieSeries series = new JRDesignPieSeries();
                
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getSeriesItem());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("\" \"");
                    }
                    series.setKeyExpression(expression);
                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getLabelItem());
                    if (expression.getText() != null && !expression.getText().isEmpty())
                    {
                        series.setLabelExpression(expression);
                    }

                }
                {
                    JRDesignExpression expression = createValueExpression(block.getReport(), screenChart.getValue1Item());
                    if (expression.getText() == null || expression.getText().isEmpty())
                    {
                        expression.setText("0");
                    }
                    series.setValueExpression(expression);
                }

                data.addPieSeries(series);
                chart.setDataset(data);

                //
            }
                break;

            default:
                break;
        }

        //chart.setTheme("aegean");
        chart.setX(0);
        chart.setY(0);
        chart.setWidth(width);
        chart.setHeight(height);
        chart.setEvaluationTime(EvaluationTimeEnum.REPORT);
        detail.addElement(chart);
        detail.setHeight(height);
        design.setPageWidth(width);
        design.setColumnWidth(width);
        design.setTitle(detail);
    }

    private byte getChartType(EJReportScreenChart screenChart)
    {
        
        switch (screenChart.getChartType())
        {
            case BAR_CHART:
                
                return (screenChart.isUse3dView() ? JRDesignChart.CHART_TYPE_BAR3D : JRDesignChart.CHART_TYPE_BAR);
            case STACKED_BAR_CHART:
                
                return (screenChart.isUse3dView() ? JRDesignChart.CHART_TYPE_STACKEDBAR3D : JRDesignChart.CHART_TYPE_STACKEDBAR);
            case AREA_CHART:
                
                return JRDesignChart.CHART_TYPE_AREA ;
            case STACKED_AREA_CHART:
                
                return JRDesignChart.CHART_TYPE_STACKEDAREA;
            case LINE_CHART:
                
                return JRDesignChart.CHART_TYPE_LINE;
            case XY_AREA_CHART:
                
                return JRDesignChart.CHART_TYPE_XYAREA;
            case XY_BAR_CHART:
                
                return JRDesignChart.CHART_TYPE_XYBAR;
            case XY_LINE_CHART:
                
                return JRDesignChart.CHART_TYPE_XYLINE;

          
        }
        
        return JRDesignChart.CHART_TYPE_AREA;
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
        if (!EJCoreReportVisualAttributeProperties.UNSPECIFIED.equals(fontName))
        {
            style.setFontName(fontName);
            style.setPdfEmbedded(true);
        }
        else
        {
            style.setFontName("Arial");

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

        vaToStyleAligment(va, style);

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
                style.setPdfFontName("Helvetica-Bold");
                break;
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

    private void vaToStyleAligment(EJReportVisualAttributeProperties va, JRBaseStyle style)
    {
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
    }

    private boolean hasDynamicVAToStyle(EJReportVisualAttributeProperties va)
    {

        if (va.getHAlignment() != EJReportScreenAlignment.NONE)
            return true;
        if (va.getVAlignment() != EJReportScreenAlignment.NONE)
            return true;
        if (va.getBackgroundColor() != null)
            return true;

        return false;
    }

    private void crateValueRefField(EJReportScreenItem item) throws JRException
    {
        if (item.typeOf(EJReportValueBaseScreenItem.class))
        {
            EJReportValueBaseScreenItem vaItem = item.typeAs(EJReportValueBaseScreenItem.class);

            String defaultValue = vaItem.getValue();
            if (vaItem.getValue() != null && vaItem.getValue().length() > 0)
            {
                createField(defaultValue);
            }
        }
    }

    private void createField(String defaultValue) throws JRException
    {
        if (defaultValue == null || defaultValue.length() == 0)
            return;
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

    private void configMarkup(JRDesignStyle textField, EJReportScreenItem item)
    {
        // http://jasperreports.sourceforge.net/sample.reference/markup/
        if (item.typeOf(EJReportValueBaseScreenItem.class))
        {
            EJReportValueBaseScreenItem vaItem = item.typeAs(EJReportValueBaseScreenItem.class);

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

    private JRDesignElement createScreenItem(EJReportBlock block, EJReportScreenItem item) throws JRException
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
                EJReportTextScreenItem textItem = item.typeAs(EJReportTextScreenItem.class);
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                JRDesignExpression valueExpression = createValueExpression(block.getReport(), textItem.getValue());
                if (valueExpression.getText().equals("$V{MASTER_CURRENT_PAGE}"))
                {
                    text.setEvaluationTime(EvaluationTimeEnum.MASTER);
                }

                if (valueExpression.getText().equals("$V{MASTER_TOTAL_PAGES}"))
                {

                    text.setEvaluationTime(EvaluationTimeEnum.MASTER);
                }
                if (valueExpression.getText().equals("$V{PAGE_NUMBER_OF_TOTAL_PAGES}"))
                {
                    valueExpression.setText("\"Page \" + $V{MASTER_CURRENT_PAGE} + \" of \" + $V{MASTER_TOTAL_PAGES}");
                    text.setEvaluationTime(EvaluationTimeEnum.MASTER);
                }
                text.setExpression(valueExpression);
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
                EJReportNumberScreenItem textItem = item.typeAs(EJReportNumberScreenItem.class);
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                JRDesignExpression valueExpression = createValueExpression(block.getReport(), textItem.getValue());
                if (valueExpression.getText().equals("$V{MASTER_CURRENT_PAGE}"))
                {
                    text.setEvaluationTime(EvaluationTimeEnum.MASTER);
                }

                if (valueExpression.getText().equals("$V{MASTER_TOTAL_PAGES}"))
                {

                    text.setEvaluationTime(EvaluationTimeEnum.MASTER);
                }
                text.setExpression(valueExpression);
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
                EJReportDateScreenItem textItem = item.typeAs(EJReportDateScreenItem.class);
                JRDesignTextField text = new JRDesignTextField();
                element = text;
                JRDesignExpression valueExpression = createValueExpression(block.getReport(), textItem.getValue());

                text.setExpression(valueExpression);
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
                EJReportLabelScreenItem labelItem = item.typeAs(EJReportLabelScreenItem.class);
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
                EJReportLineScreenItem lineItem = item.typeAs(EJReportLineScreenItem.class);
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
                EJReportRectangleScreenItem lineItem = item.typeAs(EJReportRectangleScreenItem.class);
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
                EJReportImageScreenItem imageItem = item.typeAs(EJReportImageScreenItem.class);
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

                image.setExpression(createImageValueExpression(block.getReport(), imageItem.getValue(), imageItem.getDefaultImage()));

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

    void setAlignments(JRDesignStyle elm, EJReportAlignmentBaseScreenItem alignmentBaseItem)
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

    void setRotation(JRDesignStyle elm, EJReportRotatableScreenItem rotatableItem)
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
        else if ("VARIABLE".equals(paramTypeCode))
        {
            if (paramValue.equals("CURRENT_DATE"))
            {
                expression.setText("new java.util.Date()");
            }
            else if (paramValue.equals("PAGE_NUMBER"))
            {
                expression.setText("$V{MASTER_CURRENT_PAGE}");
            }
            else if (paramValue.equals("PAGE_COUNT"))
            {
                expression.setText("$V{MASTER_TOTAL_PAGES}");
            }
            else if (paramValue.equals("PAGE_NUMBER_OF_TOTAL_PAGES"))
            {
                expression.setText("$V{PAGE_NUMBER_OF_TOTAL_PAGES}");
            }

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

    JRDesignExpression createItemVAExpression(String item, String vaName, EJReportScreenSection section)
    {
        JRDesignExpression expression = new JRDesignExpression();

        if (vaName == null)
            expression.setText(String.format("($F{_EJ_VA_CONTEXT}).isActive(\"%s\",\"%s\")", item, section.name()));
        else
            expression.setText(String.format("($F{_EJ_VA_CONTEXT}).isActive(\"%s\",\"%s\",\"%s\")", item, section.name(), vaName));
        return expression;
    }

    JRDesignExpression createVABaseValueExpression(JRDesignExpression valueExpression, String item, String defaultPattren, EJReportScreenSection section)
    {
        JRDesignExpression expression = new JRDesignExpression();

        expression.setText(String.format("($F{_EJ_VA_CONTEXT}).getVABaseValue(%s,\"%s\",\"%s\",\"%s\")", valueExpression.getText(), item, section.name(),
                defaultPattren));
        return expression;
    }

    JRDesignExpression createItemVisibleExpression(String item, EJReportScreenSection section)
    {
        JRDesignExpression expression = new JRDesignExpression();

        expression.setText(String.format("($F{_EJ_VA_CONTEXT}).isVisible(\"%s\",\"%s\")", item, section.name()));
        return expression;
    }

    JRDesignExpression createScreenItemVisibleExpression(String block, String item, EJReportScreenSection section)
    {
        JRDesignExpression expression = new JRDesignExpression();

        expression.setText(String.format("($F{_EJ_AP_CONTEXT}).canShowScreenItem(\"%s\",\"%s\",\"%s\")", block, item, section.name()));
        return expression;
    }

    JRDesignExpression createBlockVisibleExpression(String name)
    {
        JRDesignExpression expression = new JRDesignExpression();

        expression.setText(String.format("($F{_EJ_AP_CONTEXT}).canShowBlock(\"%s\")", name));
        return expression;
    }

    JRDesignExpression createImageValueExpression(EJReport report, String defaultValue, String deafultImage)
    {
        JRDesignExpression expression = createValueExpression(report, defaultValue);

        String text = expression.getText();
        if (text != null && !text.isEmpty())
        {
            if (deafultImage != null && !deafultImage.isEmpty())
            {

                if (deafultImage.startsWith("/"))
                {
                    deafultImage = deafultImage.substring(1);
                }
                if (deafultImage.startsWith("\\s"))
                {
                    deafultImage = deafultImage.substring(1);
                }
                expression.setText(String.format(
                        " %s!=null ? new ByteArrayInputStream((byte[]) %s) : this.getClass().getClassLoader().getResourceAsStream(\"%s\")", text, text,
                        deafultImage));

            }
            else
            {
                expression.setText(String.format(" new ByteArrayInputStream((byte[]) %s) ", text));

            }

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
