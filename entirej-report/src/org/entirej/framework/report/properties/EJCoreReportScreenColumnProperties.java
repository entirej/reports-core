package org.entirej.framework.report.properties;

import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.interfaces.EJReportBlockProperties;

public class EJCoreReportScreenColumnProperties
{
    private EJCoreReportBlockProperties               _blockProperties;
    private String                                    _name;
    private boolean                                   _showHeader;
    private boolean                                   _showFooter;
    private int                                       _width;
    private boolean                                   _visible = true;

    private EJCoreReportScreenColumnSectionProperties _headerSectionProperties;
    private EJCoreReportScreenColumnSectionProperties _detailSectionProperties;
    private EJCoreReportScreenColumnSectionProperties _footerSectionProperties;

    public EJCoreReportScreenColumnProperties(EJCoreReportBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _headerSectionProperties = new EJCoreReportScreenColumnSectionProperties(blockProperties, EJReportScreenSection.HEADER);
        _detailSectionProperties = new EJCoreReportScreenColumnSectionProperties(blockProperties, EJReportScreenSection.DETAIL);
        _footerSectionProperties = new EJCoreReportScreenColumnSectionProperties(blockProperties, EJReportScreenSection.FOOTER);
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
        return _blockProperties;
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
    
    public boolean isVisible()
    {
        return _visible;
    }
    
    public void setVisible(boolean visible)
    {
        this._visible = visible;
    }

    public EJCoreReportScreenColumnSectionProperties getHeaderSectionProperties()
    {
        return _headerSectionProperties;
    }

    public EJCoreReportScreenColumnSectionProperties getDetailSectionProperties()
    {
        return _detailSectionProperties;
    }

    public EJCoreReportScreenColumnSectionProperties getFooterSectionProperties()
    {
        return _footerSectionProperties;
    }
}
