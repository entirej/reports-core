package org.entirej.framework.report.properties;

import java.math.BigDecimal;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportBlockProperties;

public class EJCoreReportColumnProperties
{

    private EJCoreReportBlockProperties  blockProperties;

    private String                       _name;
    private boolean                      _showHeader;
    private boolean                      _showFooter;
    private int                          _width;

    private EJCoreReportScreenProperties _header;
    private EJCoreReportScreenProperties _detail;
    private EJCoreReportScreenProperties _footer;

    private EJCoreReportBorderProperties _headerBorderProperties = new EJCoreReportBorderProperties();
    private EJCoreReportBorderProperties _detailBorderProperties = new EJCoreReportBorderProperties();
    private EJCoreReportBorderProperties _footerBorderProperties = new EJCoreReportBorderProperties();

    public EJCoreReportColumnProperties(EJCoreReportBlockProperties blockProperties)
    {
        this.blockProperties = blockProperties;
        _header = new EJCoreReportScreenProperties(blockProperties);
        _detail = new EJCoreReportScreenProperties(blockProperties);
        _footer = new EJCoreReportScreenProperties(blockProperties);
        _header.setScreenType(EJReportScreenType.FORM_LATOUT);
        _detail.setScreenType(EJReportScreenType.FORM_LATOUT);
        _footer.setScreenType(EJReportScreenType.FORM_LATOUT);
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public EJReportBlockProperties getBlockProperties()
    {
        return blockProperties;
    }

    public boolean showHeader()
    {
        return _showHeader;
    }

    public void setShowHeader(boolean showHeader)
    {
        _showHeader = showHeader;
    }

    public boolean showFooter()
    {
        return _showFooter;
    }

    public void setShowFooter(boolean showFooter)
    {
        _showFooter = showFooter;
    }
    
    public void setWidth(int width)
    {
        _width = width;
    }
    
    public int getWidth()
    {
        return _width;
    }

    public EJCoreReportScreenProperties getHeaderScreenProperties()
    {
        return _header;
    }

    public EJCoreReportScreenProperties getDetailScreenProperties()
    {
        return _detail;
    }

    public EJCoreReportScreenProperties getFooterScreenProperties()
    {
        return _footer;
    }

    public EJCoreReportBorderProperties getHeaderBorderProperties()
    {
        return _headerBorderProperties;
    }

    public EJCoreReportBorderProperties getDetailBorderProperties()
    {
        return _detailBorderProperties;
    }

    public EJCoreReportBorderProperties getFooterBorderProperties()
    {
        return _footerBorderProperties;
    }

}
