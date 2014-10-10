package org.entirej.framework.report.properties;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.interfaces.EJReportBlockProperties;
import org.entirej.framework.report.interfaces.EJReportColumnProperties;

public class EJCoreReportColumnProperties implements EJReportColumnProperties
{

    private EJCoreReportBlockProperties  blockProperties;

    private String                       name;
    private boolean                      showHeader;
    private boolean                      showFooter;

    private EJCoreReportScreenProperties header;
    private EJCoreReportScreenProperties detail;
    private EJCoreReportScreenProperties footer;

    private EJCoreReportBorderProperties headerBorderProperties = new EJCoreReportBorderProperties();
    private EJCoreReportBorderProperties detailBorderProperties = new EJCoreReportBorderProperties();
    private EJCoreReportBorderProperties footerBorderProperties = new EJCoreReportBorderProperties();

    public EJCoreReportColumnProperties(EJCoreReportBlockProperties blockProperties)
    {
        this.blockProperties = blockProperties;
        header = new EJCoreReportScreenProperties(blockProperties);
        detail = new EJCoreReportScreenProperties(blockProperties);
        footer = new EJCoreReportScreenProperties(blockProperties);
        header.setScreenType(EJReportScreenType.FORM_LATOUT);
        detail.setScreenType(EJReportScreenType.FORM_LATOUT);
        footer.setScreenType(EJReportScreenType.FORM_LATOUT);
    }

    @Override
    public EJReportBlockProperties getBlockProperties()
    {
        return blockProperties;
    }

    public boolean isShowHeader()
    {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader)
    {
        this.showHeader = showHeader;
    }

    public boolean isShowFooter()
    {
        return showFooter;
    }

    public void setShowFooter(boolean showFooter)
    {
        this.showFooter = showFooter;
    }

    @Override
    public EJCoreReportScreenProperties getHeaderScreen()
    {
        return header;
    }

    @Override
    public EJCoreReportScreenProperties getDetailScreen()
    {
        return detail;
    }

    @Override
    public EJCoreReportScreenProperties getFooterScreen()
    {
        return footer;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public EJCoreReportBorderProperties getHeaderBorderProperties()
    {
        return headerBorderProperties;
    }

    @Override
    public EJCoreReportBorderProperties getDetailBorderProperties()
    {
        return detailBorderProperties;
    }

    @Override
    public EJCoreReportBorderProperties getFooterBorderProperties()
    {
        return footerBorderProperties;
    }

}
