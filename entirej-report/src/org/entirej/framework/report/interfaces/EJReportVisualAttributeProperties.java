package org.entirej.framework.report.interfaces;

import java.awt.Color;

import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportVAPattern;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;

public interface EJReportVisualAttributeProperties
{

    /**
     * Returns the name of this <code>VisualAttribute</code>
     * 
     * @return The name of this <code>VisualAttribute</code>
     */
    public abstract String getName();

    /**
     * Returns the {@link FontWeight} set for this visual attribute
     * <p>
     * If {@link FontWeight#Unspecified} is specified (the default) then the
     * current, default font weight will be used
     * 
     * @return The {@link FontWeight} defined for this visual attribute
     */
    public abstract EJReportFontWeight getFontWeight();

    /**
     * Returns the {@link FontStyle} set for this visual attribute
     * <p>
     * If {@link FontStyle#Unspecified} is specified (the default) then the
     * current, default font style will be used
     * 
     * @return The {@link FontStyle} defined for this visual attribute
     */
    public abstract EJReportFontStyle getFontStyle();

    /**
     * The name of the font
     * 
     * @return the name of the font
     */
    public abstract String getFontName();

    /**
     * Convenience method that is used to indicate if this visual attribute has
     * a font size set
     * <p>
     * If the font size is set to a value less than zero then this is classed as
     * having no font size set
     * 
     * @return <code>true</code> if the font size has been set to a value
     *         greater than zero, otherwise false
     */
    public abstract boolean isFontSizeSet();

    /**
     * The size of the text defined by this <code>VisualAttribute</code>
     * 
     * @return the fontSize
     */
    public abstract int getFontSize();

    /**
     * The size relative to default font size as percentage of the text defined
     * by this <code>VisualAttribute</code>
     * 
     * @return the fontSize
     */
    public abstract boolean isUsedAsDynamicVA();

    /**
     * Returns the color defined as the foreground color for this visual
     * attribute
     * 
     * @return The foreground color or <code>null</code> if no foreground color
     *         has been defined
     */
    public abstract Color getForegroundColor();

    /**
     * Returns the foreground color for this visual attribute
     * <p>
     * The foreground color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @return a String representing the defined color or
     *         {@link EJCoreReportVisualAttributeProperties#UNSPECIFIED}
     */
    public abstract String getForegroundRGB();

    /**
     * Returns the color defined as the background color for this visual
     * attribute
     * 
     * @return The background color or <code>null</code> if no background color
     *         has been defined
     */
    public abstract Color getBackgroundColor();

    /**
     * Returns the background color for this visual attribute
     * <p>
     * The background color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @return a String representing the defined color or
     *         {@link EJCoreReportVisualAttributeProperties#UNSPECIFIED}
     */
    public abstract String getBackgroundRGB();

    public abstract EJReportMarkupType getMarkupType();

    public abstract EJReportScreenAlignment getHAlignment();

    public abstract EJReportScreenAlignment getVAlignment();

    public abstract EJReportVAPattern getLocalePattern();

    public abstract String getManualPattern();
    
    public boolean isExpandToFit();

}