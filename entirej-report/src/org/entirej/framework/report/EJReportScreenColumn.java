package org.entirej.framework.report;

import org.entirej.framework.report.interfaces.EJReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;

public class EJReportScreenColumn
{
    private EJReportBlock                      _block;
    private EJCoreReportScreenColumnProperties _properties;

    public EJReportScreenColumn(EJReportBlock block, EJCoreReportScreenColumnProperties properties)
    {
        _block = block;
        _properties = properties;
    }

    public String getName()
    {
        return _properties.getName();
    }

    public EJReportBlockProperties getBlockProperties()
    {
        return _properties.getBlockProperties();
    }

    public boolean showHeader()
    {
        return _properties.showHeader();
    }

    public boolean showFooter()
    {
        return _properties.showFooter();
    }

    public int getWidth()
    {
        return _properties.getWidth();
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
