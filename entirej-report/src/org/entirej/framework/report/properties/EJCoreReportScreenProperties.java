package org.entirej.framework.report.properties;

import java.util.Collection;
import java.util.List;

import org.entirej.framework.report.enumerations.EJReportScreenType;
import org.entirej.framework.report.properties.EJReportBlockContainer.BlockGroup;

public class EJCoreReportScreenProperties
{

    private EJCoreReportBlockProperties    blockProperties;

    private int                            x, y, width, height;

    private int                            headerColumnHeight;
    private int                            detailColumnHeight;
    private int                            footerColumnHeight;
    private EJReportScreenType             screenType = EJReportScreenType.NONE;

    private final BlockGroup               subBlocks  = new BlockGroup("Sub Blocks");

    private final EJCoreReportSreenItemContainer _screenItemContainer;
    private final EJReportColumnContainer        _columnContainer;

    private String                         oddRowVAName;
    private String                         evenRowVAName;
    
    private final EJCoreReportChartProperties    chartProperties;

    public EJCoreReportScreenProperties(EJCoreReportBlockProperties blockProperties)
    {
        this.blockProperties = blockProperties;
        _screenItemContainer = new EJReportScreenItemContainer(blockProperties, this);
        _columnContainer = new EJReportColumnContainer(blockProperties);
        chartProperties = new EJCoreReportChartProperties(this);
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return blockProperties;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {

        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public EJReportScreenType getScreenType()
    {
        return screenType;
    }

    public void setScreenType(EJReportScreenType screenType)
    {
        this.screenType = screenType;
    }

    public List<EJCoreReportBlockProperties> getAllSubBlocks()
    {
        return subBlocks.getAllBlockProperties();
    }

    public BlockGroup getSubBlocks()
    {
        return subBlocks;
    }

    public Collection<EJCoreReportScreenItemProperties> getScreenItems()
    {
        return _screenItemContainer.getAllItemProperties();
    }

    public EJCoreReportSreenItemContainer getScreenItemContainer()
    {
        return _screenItemContainer;
    }

    public EJReportColumnContainer getColumnContainer()
    {
        return _columnContainer;
    }

    public Collection<? extends EJCoreReportScreenColumnProperties> getAllColumns()
    {
        return _columnContainer.getAllColumnProperties();
    }

    public String getOddRowVAName()
    {
        return oddRowVAName;
    }

    public void setOddRowVAName(String oddRowVAName)
    {
        this.oddRowVAName = oddRowVAName;
    }

    public String getEvenRowVAName()
    {
        return evenRowVAName;
    }

    public void setEvenRowVAName(String evenRowVAName)
    {
        this.evenRowVAName = evenRowVAName;
    }

    public int getHeaderColumnHeight()
    {
        return headerColumnHeight;
    }

    public void setHeaderColumnHeight(int headerColumnHeight)
    {
        this.headerColumnHeight = headerColumnHeight;
    }

    public int getDetailColumnHeight()
    {
        return detailColumnHeight;
    }

    public void setDetailColumnHeight(int detailColumnHeight)
    {
        this.detailColumnHeight = detailColumnHeight;
    }

    public int getFooterColumnHeight()
    {
        return footerColumnHeight;
    }

    public void setFooterColumnHeight(int footerColumnHeight)
    {
        this.footerColumnHeight = footerColumnHeight;
    }

    public EJReportVisualAttributeProperties getOddVAProperties()
    {
        return EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(oddRowVAName);
    }

    public EJReportVisualAttributeProperties getEvenVAProperties()
    {
        return EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(evenRowVAName);
    }
    
    
    public EJCoreReportChartProperties getChartProperties()
    {
        return chartProperties;
    }
}
