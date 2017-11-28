package org.entirej.report.poi.layout;

import java.util.ArrayList;
import java.util.List;

public class EJReportPOIRaw
{
    private int                      height   = -1;
    private int                      width    = -1;
    private List<EJReportPOIElement> elements = new ArrayList<EJReportPOIElement>();

    public enum Type
    {
        HEADER, DETAIL, FOOTER
    };

    private final Type type;

    public EJReportPOIRaw(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void addElement(EJReportPOIElement element)
    {
        elements.add(element);
    }

    public List<EJReportPOIElement> getElements()
    {
        return new ArrayList<EJReportPOIElement>(elements);
    }

    public EJReportPOIElement getElement(String id)
    {
        for (EJReportPOIElement element : elements)
        {
            if (element.getId().equals(id))
            {
                return element;
            }
        }
        return null;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getWidth()
    {
        return width;
    }

}
