package org.entirej.report.poi.layout;

import org.entirej.framework.report.EJReportScreenItem;
import org.entirej.framework.report.EJReportTextScreenItem;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJReportPOIElement
{

    private final EJReportScreenItem          item;
    private int                               startCell    = -1;
    private int                               endCell      = -1;
    private int                               width        = 0;
    private int                               x            = 0;
    private int                               rawSpan      = 0;
    private boolean                           supportMerge = false;
    private boolean                           wrap         = false;
    private EJReportVisualAttributeProperties va;
    private String                            defaultPattren;

    public EJReportPOIElement(EJReportScreenItem item)
    {
        this.item = item;
        if(item!=null && item.getType()==EJReportScreenItemType.TEXT)
        {
            EJReportTextScreenItem textScreenItem = item.typeAs(EJReportTextScreenItem.class);
            wrap = textScreenItem.isExpandToFit();
        }
        
    }

    public EJReportScreenItem getId()
    {
        return item;
    }

    public int getStartCell()
    {
        return startCell;
    }

    public void setStartCell(int startCell)
    {
        this.startCell = startCell;
    }

    public int getEndCell()
    {
        return endCell;
    }

    public void setEndCell(int endCell)
    {
        this.endCell = endCell;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setRawSpan(int rawSpan)
    {
        this.rawSpan = rawSpan;
    }

    public int getRawSpan()
    {
        return rawSpan;
    }

    public void setSupportMerge(boolean supportMerge)
    {
        this.supportMerge = supportMerge;
    }

    public boolean isSupportMerge()
    {
        return supportMerge;
    }

    public EJReportVisualAttributeProperties getVa()
    {
        return va;
    }

    public void setVa(EJReportVisualAttributeProperties va)
    {
        this.va = va;
    }

    public void setDefaultPattren(String defaultPattren)
    {
        this.defaultPattren = defaultPattren;
    }

    public String getDefaultPattren()
    {
        return defaultPattren;
    }

    public void setWrap(boolean wrap)
    {
        this.wrap = wrap;
    }

    public boolean isWrap()
    {
        return wrap;
    }

}
