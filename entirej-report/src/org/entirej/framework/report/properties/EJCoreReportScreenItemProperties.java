package org.entirej.framework.report.properties;

import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.enumerations.EJReportScreenRotation;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.Line.LineStyle;

public abstract class EJCoreReportScreenItemProperties implements EJReportScreenItemProperties
{

    private boolean                     _visible = true;
    private String                      _name;
    private String                      _visualAttributeName;
    private int                         _xPos, _yPos, _width, _height;
    private boolean                     _widthAsPercentage, _heightAsPercentage;
    private EJCoreReportBlockProperties _blockProperties;

    public EJCoreReportScreenItemProperties(EJCoreReportBlockProperties blockProperties)
    {
        this._blockProperties = blockProperties;
    }

    @Override
    public EJReportVisualAttributeProperties getVisualAttributeProperties()
    {

        return EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(_visualAttributeName);
    }

    public void setVisualAttribute(String visualAttributeName)
    {
        if (visualAttributeName == null || visualAttributeName.trim().length() == 0)
        {
            _visualAttributeName = null;
            return;
        }

        EJReportVisualAttributeProperties vaProperties = EJCoreReportRuntimeProperties.getInstance().getVisualAttributesContainer()
                .getVisualAttributeProperties(visualAttributeName);
        if (vaProperties == null)
        {
            throw new IllegalArgumentException("There is no visual attribute with the name " + visualAttributeName + " on this report.");
        }

        _visualAttributeName = visualAttributeName;
    }

    @Override
    public int getWidth()
    {
        return _width;
    }

    public void setWidth(int width)
    {
        _width = width;
    }

    @Override
    public int getHeight()
    {
        return _height;
    }

    public void setHeight(int height)
    {
        _height = height;
    }

    public boolean isWidthAsPercentage()
    {
        return _widthAsPercentage;
    }

    public void setWidthAsPercentage(boolean widthAsPercentage)
    {
        _widthAsPercentage = widthAsPercentage;
    }

    public boolean isHeightAsPercentage()
    {
        return _heightAsPercentage;
    }

    public void setHeightAsPercentage(boolean heightAsPercentage)
    {
        _heightAsPercentage = heightAsPercentage;
    }

    @Override
    public int getX()
    {
        return _xPos;
    }

    public void setX(int x)
    {
        _xPos = x;
    }

    @Override
    public int getY()
    {

        return _yPos;
    }

    public void setY(int y)
    {
        _yPos = y;
    }

    @Override
    public boolean isVisible()
    {
        return _visible;
    }

    public void setVisible(boolean visible)
    {
        _visible = visible;
    }

    public String getVisualAttributeName()
    {
        return _visualAttributeName;
    }

    public void setVisualAttributeName(String visualAttributeName)
    {
        _visualAttributeName = visualAttributeName;
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public void setBlockProperties(EJCoreReportBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public static class Label extends AlignmentBaseItem implements RotatableItem
    {

        private String                 text;
        private EJReportScreenRotation rotation = EJReportScreenRotation.NONE;

        public Label(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.LABEL;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public EJReportScreenRotation getRotation()
        {
            return rotation;
        }

        public void setRotation(EJReportScreenRotation rotation)
        {
            this.rotation = rotation;
        }
    }

    public static class Line extends EJCoreReportScreenItemProperties
    {

        private double        lineWidth     = 1.0;
        private LineStyle     lineStyle     = LineStyle.SOLID;
        private LineDirection lineDirection = LineDirection.TO_DOWN;

        public enum LineStyle
        {
            SOLID, DASHED, DOTTED, DOUBLE;

        }

        public enum LineDirection
        {
            TO_DOWN, BOTTOM_UP;

        }

        public Line(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.LINE;
        }

        public double getLineWidth()
        {
            return lineWidth;
        }

        public void setLineWidth(double lineWidth)
        {
            this.lineWidth = lineWidth;
        }

        public LineStyle getLineStyle()
        {
            return lineStyle;
        }

        public void setLineStyle(LineStyle lineStyle)
        {
            this.lineStyle = lineStyle;
        }

        public LineDirection getLineDirection()
        {
            return lineDirection;
        }

        public void setLineDirection(LineDirection lineDirection)
        {
            this.lineDirection = lineDirection;
        }

    }

    public static class Rectangle extends EJCoreReportScreenItemProperties
    {

        private double    lineWidth = 1.0;
        private int       radius;
        private LineStyle lineStyle = LineStyle.SOLID;

        public Rectangle(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.RECTANGLE;
        }

        public double getLineWidth()
        {
            return lineWidth;
        }

        public void setLineWidth(double lineWidth)
        {
            this.lineWidth = lineWidth;
        }

        public LineStyle getLineStyle()
        {
            return lineStyle;
        }

        public void setLineStyle(LineStyle lineStyle)
        {
            this.lineStyle = lineStyle;
        }

        public void setRadius(int radius)
        {
            this.radius = radius;
        }

        public int getRadius()
        {
            return radius;
        }

    }

    public static class Text extends ValueBaseItem implements RotatableItem
    {
        private EJReportScreenRotation rotation = EJReportScreenRotation.NONE;

        public Text(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.TEXT;
        }

        public EJReportScreenRotation getRotation()
        {
            return rotation;
        }

        public void setRotation(EJReportScreenRotation rotation)
        {
            this.rotation = rotation;
        }

    }

    public static interface RotatableItem
    {
        public EJReportScreenRotation getRotation();

        public void setRotation(EJReportScreenRotation rotation);
    }

    public static abstract class AlignmentBaseItem extends EJCoreReportScreenItemProperties
    {

        private EJReportScreenAlignment hAlignment = EJReportScreenAlignment.LEFT;
        private EJReportScreenAlignment vAlignment = EJReportScreenAlignment.CENTER;

        public AlignmentBaseItem(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

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

    }

    public static abstract class ValueBaseItem extends AlignmentBaseItem
    {

        private boolean            expandToFit;
        private EJReportMarkupType markup = EJReportMarkupType.NONE;
        private String             value;

        public ValueBaseItem(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public boolean isExpandToFit()
        {
            return expandToFit;
        }

        public void setExpandToFit(boolean expandToFit)
        {
            this.expandToFit = expandToFit;
        }

        public EJReportMarkupType getMarkup()
        {
            return markup;
        }

        public void setMarkup(EJReportMarkupType markup)
        {
            this.markup = markup;
        }

    }

    public static class Number extends ValueBaseItem implements RotatableItem
    {
        private EJReportScreenRotation rotation     = EJReportScreenRotation.NONE;
        private String                 manualFormat;
        private NumberFormats          localeFormat = NumberFormats.NUMBER;

        public enum NumberFormats
        {
            NUMBER, INTEGER, CURRENCY, PERCENT;

        }

        public Number(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.NUMBER;
        }

        public EJReportScreenRotation getRotation()
        {
            return rotation;
        }

        public void setRotation(EJReportScreenRotation rotation)
        {
            this.rotation = rotation;
        }

        public String getManualFormat()
        {
            return manualFormat;
        }

        public void setManualFormat(String manualFormat)
        {
            this.manualFormat = manualFormat;
        }

        public NumberFormats getLocaleFormat()
        {
            return localeFormat;
        }

        public void setLocaleFormat(NumberFormats localeFormat)
        {
            this.localeFormat = localeFormat;
        }

    }

    public static class Date extends ValueBaseItem implements RotatableItem
    {
        private EJReportScreenRotation rotation     = EJReportScreenRotation.NONE;

        private DateFormats            localeFormat = DateFormats.DATE_SHORT;

        private String                 manualFormat;

        public enum DateFormats
        {
            DATE_LONG, DATE_MEDIUM, DATE_SHORT, DATE_FULL,

            DATE_TIME_LONG, DATE_TIME_MEDIUM, DATE_TIME_SHORT, DATE_TIME_FULL,

            TIME_LONG, TIME_MEDIUM, TIME_SHORT, TIME_FULL,

        }

        public Date(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.DATE;
        }

        public EJReportScreenRotation getRotation()
        {
            return rotation;
        }

        public void setRotation(EJReportScreenRotation rotation)
        {
            this.rotation = rotation;
        }

        public void setLocaleFormat(DateFormats localeFormat)
        {
            this.localeFormat = localeFormat;
        }

        public DateFormats getLocaleFormat()
        {
            return localeFormat;
        }

        public String getManualFormat()
        {
            return manualFormat;
        }

        public void setManualFormat(String manualFormat)
        {
            this.manualFormat = manualFormat;
        }
    }

    public static class Image extends ValueBaseItem
    {

        private String defaultImage;

        public String getDefaultImage()
        {
            return defaultImage;
        }

        public void setDefaultImage(String defaultImage)
        {
            this.defaultImage = defaultImage;
        }

        public Image(EJCoreReportBlockProperties blockProperties)
        {
            super(blockProperties);
        }

        @Override
        public EJReportScreenItemType getType()
        {
            return EJReportScreenItemType.IMAGE;
        }

    }
}
