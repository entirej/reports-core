package org.entirej.framework.report;

import org.entirej.framework.report.enumerations.EJReportTableColumn;
import org.entirej.framework.report.interfaces.EJReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;

public class EJReportScreenColumn
{
    private EJReportBlock                      _block;
    private EJCoreReportScreenColumnProperties _properties;
    private EJReportTableColumn reportTableColumn = EJReportTableColumn.MOVE; 

    public EJReportScreenColumn(EJReportBlock block, EJCoreReportScreenColumnProperties properties)
    {
        _block = block;
        _properties = properties;
    }

    public String getName()
    {
        return _properties.getName();
    }

    public boolean showHeader()
    {
        return _properties.showHeader();
    }

    public boolean showFooter()
    {
        return _properties.showFooter();
    }
    
    public EJReportTableColumn getHiddenColumnLayout()
    {
        return reportTableColumn;
    }

    public int getWidth()
    {
        return _properties.getWidth();
    }

    public void setWidth(int width)
    {
        _properties.setWidth(width);
    }
    
    public void setVisible(boolean visible,EJReportTableColumn reportTableColumn)
    {
        _properties.setVisible(visible);
        this.reportTableColumn = reportTableColumn;
    }
    public boolean isVisible()
    {
        return _properties.isVisible();
    }

    public EJReportScreenColumnSection getHeaderSection()
    {
        return new EJReportScreenColumnSection(_block, _properties.getHeaderSectionProperties());
    }

    public EJReportScreenColumnSection getDetailSection()
    {
        return new EJReportScreenColumnSection(_block, _properties.getDetailSectionProperties());
    }

    public EJReportScreenColumnSection getFooterSection()
    {
        return new EJReportScreenColumnSection(_block, _properties.getFooterSectionProperties());
    }

}
