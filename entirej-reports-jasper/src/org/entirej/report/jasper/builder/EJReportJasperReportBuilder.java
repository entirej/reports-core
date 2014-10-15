package org.entirej.report.jasper.builder;

import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportBlockItem;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
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
                
                field.setName(item.getName());
                field.setDescription(item.getFieldName());
                field.setValueClass(item.getDataType());
                design.addField(field);
            }
            
            
            
            EJCoreReportScreenProperties screenProperties = properties.getLayoutScreenProperties();
            Collection<EJCoreReportScreenItemProperties> screenItems = screenProperties.getScreenItems();
            
            
            JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
            JRDesignBand detail = new JRDesignBand();
            detail.setHeight(screenProperties.getHeight());
            
            detailSection.addBand(detail);
            
            for (EJCoreReportScreenItemProperties item : screenItems)
            {
                JRDesignTextField element = new JRDesignTextField();
                JRDesignExpression expression = new JRDesignExpression();
                expression.setText(String.format("$F{name}"));
                element.setExpression(expression);
                element.setX(item.getX());
                element.setY(item.getY());
                element.setWidth(item.getWidth());
                element.setHeight(item.getHeight());
                detail.addElement(element);
            }
        
        }
        catch (JRException e)
        {
            throw new EJReportRuntimeException(e);
        }
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
