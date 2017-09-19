package org.entirej.report.poi.layout;

import org.entirej.framework.report.enumerations.EJReportScreenAlignment;

public class EJReportPOIAlignment
{

    private EJReportScreenAlignment hAlignment = EJReportScreenAlignment.NONE;
    private EJReportScreenAlignment vAlignment = EJReportScreenAlignment.NONE;

    public EJReportScreenAlignment getHAlignment()
    {
        return hAlignment;
    }

    public void setHAlignment(EJReportScreenAlignment hAlignment)
    {
        this.hAlignment = hAlignment;
    }

    public EJReportScreenAlignment getVAlignment()
    {
        return vAlignment;
    }

    public void setVAlignment(EJReportScreenAlignment vAlignment)
    {
        this.vAlignment = vAlignment;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hAlignment == null) ? 0 : hAlignment.hashCode());
        result = prime * result + ((vAlignment == null) ? 0 : vAlignment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EJReportPOIAlignment other = (EJReportPOIAlignment) obj;
        if (hAlignment != other.hAlignment)
            return false;
        if (vAlignment != other.vAlignment)
            return false;
        return true;
    }
    
    
    

}
