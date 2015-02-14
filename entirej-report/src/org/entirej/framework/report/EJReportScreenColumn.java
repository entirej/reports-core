package org.entirej.framework.report;

import org.entirej.framework.report.interfaces.EJReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenColumnProperties;

public class EJReportScreenColumn
{
    private EJCoreReportScreenColumnProperties _properties;
    
    public EJReportScreenColumn(EJCoreReportScreenColumnProperties properties)
    {
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
//
//    public EJCoreReportScreenProperties getHeaderScreenProperties()
//    {
//        return _properties.getHeaderScreenProperties();
//    }
//
//    public EJCoreReportScreenProperties getDetailScreenProperties()
//    {
//        return _properties.getDetailScreenProperties();
//    }
//
//    public EJCoreReportScreenProperties getFooterScreenProperties()
//    {
//        return _properties.getFooterScreenProperties();
//    }
//
//    public EJCoreReportBorderProperties getHeaderBorderProperties()
//    {
//        return _properties.getHeaderBorderProperties();
//    }
//
//    public EJCoreReportBorderProperties getDetailBorderProperties()
//    {
//        return _properties.getDetailBorderProperties();
//    }
//
//    public EJCoreReportBorderProperties getFooterBorderProperties()
//    {
//        return _properties.getFooterBorderProperties();
//    }

}
