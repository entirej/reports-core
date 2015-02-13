/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties;

import java.awt.Color;
import java.io.Serializable;

import org.entirej.framework.report.enumerations.EJReportFontStyle;
import org.entirej.framework.report.enumerations.EJReportFontWeight;
import org.entirej.framework.report.enumerations.EJReportMarkupType;
import org.entirej.framework.report.enumerations.EJReportScreenAlignment;
import org.entirej.framework.report.enumerations.EJReportVAPattern;

public class EJCoreReportVisualAttributeProperties implements Comparable<EJCoreReportVisualAttributeProperties>, Serializable, EJReportVisualAttributeProperties
{
    public static final String      UNSPECIFIED     = "Unspecified";

    /**
     * The available font weights for use within the
     * {@link EJCoreReportVisualAttributeProperties}
     */

    private String                  _name;
    private String                  _foregroundRgb  = UNSPECIFIED;
    private String                  _backgroundRgb  = UNSPECIFIED;

    private String                  _fontName       = UNSPECIFIED;
    private int                     _fontSize       = -1;
    private boolean                 _useAsDYnamicVA = false;
    private EJReportFontStyle       _fontStyle      = EJReportFontStyle.Unspecified;
    private EJReportFontWeight      _fontWeight     = EJReportFontWeight.Unspecified;

    private EJReportMarkupType      _markupType     = EJReportMarkupType.NONE;

    private EJReportScreenAlignment _hAlignment     = EJReportScreenAlignment.NONE;
    private EJReportScreenAlignment _vAlignment     = EJReportScreenAlignment.NONE;

    private EJReportVAPattern       _localePattern  = EJReportVAPattern.NONE;
    private String                  _manualFormat;

    public EJCoreReportVisualAttributeProperties()
    {
        _name = "VA_NO_NAME";
    }

    public EJCoreReportVisualAttributeProperties(String name)
    {
        _name = name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getName()
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Indicates the weight of the font for this VisualAttribute
     * <p>
     * If {@link FontWeight#Unspecified} is specified (the default) then the
     * current, default font weight will be used
     * 
     * @param weight
     *            The weight to use
     * @see FontWeight
     */
    public void setFontWeight(EJReportFontWeight weight)
    {
        _fontWeight = weight;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getFontWeight()
     */
    @Override
    public EJReportFontWeight getFontWeight()
    {
        return _fontWeight;
    }

    /**
     * Indicates the style of the font for this VisualAttribute
     * <p>
     * If {@link FontStyle#Unspecified} is specified (the default) then the
     * current, default font style will be used
     * 
     * @param style
     *            The style to use
     * @see FontStyle
     */
    public void setFontStyle(EJReportFontStyle style)
    {
        _fontStyle = style;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getFontStyle()
     */
    @Override
    public EJReportFontStyle getFontStyle()
    {
        return _fontStyle;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getFontName()
     */
    @Override
    public String getFontName()
    {
        return _fontName;
    }

    /**
     * Sets the name of the font
     * 
     * @param fontName
     *            the name of the font
     */
    public void setFontName(String fontName)
    {
        _fontName = fontName;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#isFontSizeSet()
     */
    @Override
    public boolean isFontSizeSet()
    {
        if (_fontSize < 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getFontSize()
     */
    @Override
    public int getFontSize()
    {
        return _fontSize;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#isUsedAsDynamicVA()
     */
    @Override
    public boolean isUsedAsDynamicVA()
    {
        return _useAsDYnamicVA;
    }

    public void setUsedAsDynamicVA(boolean _usedAsDynamicVA)
    {
        this._useAsDYnamicVA = _usedAsDynamicVA;
    }

    /**
     * Sets the size of the text defined by this <code>VisualAttribute</code>
     * 
     * @param fontSize
     *            the size of the font
     */
    public void setFontSize(int fontSize)
    {
        _fontSize = fontSize;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getForegroundColor()
     */
    @Override
    public Color getForegroundColor()
    {
        if (_foregroundRgb == null || _foregroundRgb.trim().length() == 0)
        {
            return null;
        }

        if (UNSPECIFIED.equals(_foregroundRgb))
        {
            return null;
        }

        return getColor(_foregroundRgb);
    }

    /**
     * Sets the foreground color for this visual attribute
     * <p>
     * The foreground color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @param foregroundColor
     *            The color to use for the foreground color of this visual
     *            attribute
     */
    public void setForegroundRGB(String foregroundColor)
    {
        if (foregroundColor == null || foregroundColor.trim().length() == 0)
        {
            _foregroundRgb = UNSPECIFIED;
        }

        _foregroundRgb = foregroundColor;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getForegroundRGB()
     */
    @Override
    public String getForegroundRGB()
    {
        return _foregroundRgb;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getBackgroundColor()
     */
    @Override
    public Color getBackgroundColor()
    {
        if (_backgroundRgb == null || _backgroundRgb.trim().length() == 0)
        {
            return null;
        }

        if (UNSPECIFIED.equals(_backgroundRgb))
        {
            return null;
        }

        return getColor(_backgroundRgb);

    }

    /**
     * Sets the background color for this visual attribute
     * <p>
     * The background color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @param backgroundColor
     *            The color to use for the background color of this visual
     *            attribute
     */
    public void setBackgroundRGB(String backgroundColor)
    {
        if (backgroundColor == null || backgroundColor.trim().length() == 0)
        {
            _backgroundRgb = UNSPECIFIED;
        }

        _backgroundRgb = backgroundColor;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getBackgroundRGB()
     */
    @Override
    public String getBackgroundRGB()
    {
        return _backgroundRgb;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getMarkupType()
     */
    @Override
    public EJReportMarkupType getMarkupType()
    {
        return _markupType;
    }

    public void setMarkupType(EJReportMarkupType _markupType)
    {
        this._markupType = _markupType;
    }

    public void setHAlignment(EJReportScreenAlignment _hAlignment)
    {
        this._hAlignment = _hAlignment;
    }

    public void setVAlignment(EJReportScreenAlignment _vAlignment)
    {
        this._vAlignment = _vAlignment;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getHAlignment()
     */
    @Override
    public EJReportScreenAlignment getHAlignment()
    {
        return _hAlignment;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getVAlignment()
     */
    @Override
    public EJReportScreenAlignment getVAlignment()
    {
        return _vAlignment;
    }

    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getLocalePattern()
     */
    @Override
    public EJReportVAPattern getLocalePattern()
    {
        return _localePattern;
    }

    public void setLocalePattern(EJReportVAPattern localePattern)
    {
        this._localePattern = localePattern;
    }
    
    
    /* (non-Javadoc)
     * @see org.entirej.framework.report.properties.EJReportVisualAttributeProperties#getManualPattern()
     */
    @Override
    public String getManualPattern()
    {
        return _manualFormat;
    }
    
    public void setManualPattern(String manualFormat)
    {
        this._manualFormat = manualFormat;
    }

    private Color getColor(String colorString)
    {
        if (colorString == null)
        {
            throw new NullPointerException();
        }

        if (!colorString.contains("r") || !colorString.contains("g") || !colorString.contains("b"))
        {
            throw new IllegalArgumentException("The color is not in the correct format. Expected r<color>g<color>b<color>, received: " + colorString);
        }

        try
        {

            String R = colorString.substring(colorString.indexOf('r') + 1, colorString.indexOf('g'));
            String G = colorString.substring(colorString.indexOf('g') + 1, colorString.indexOf('b'));
            String B = colorString.substring(colorString.indexOf('b') + 1);

            Color color = new Color(Integer.parseInt(R), Integer.parseInt(G), Integer.parseInt(B));

            return color;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public EJReportVisualAttributeProperties makeCopy()
    {
        EJCoreReportVisualAttributeProperties props = new EJCoreReportVisualAttributeProperties();
        props.setName(_name);
        props.setForegroundRGB(_foregroundRgb);
        props.setBackgroundRGB(_backgroundRgb);
        props.setFontName(_fontName);
        props.setFontSize(_fontSize);
        props.setFontStyle(_fontStyle);
        props.setFontWeight(_fontWeight);
        props.setUsedAsDynamicVA(_useAsDYnamicVA);
        props.setMarkupType(_markupType);
        props.setHAlignment(_hAlignment);
        props.setVAlignment(_vAlignment);
        props.setLocalePattern(_localePattern);
        props.setManualPattern(_manualFormat);
        return props;
    }

    public int compareTo(EJCoreReportVisualAttributeProperties o)
    {
        return ((EJReportVisualAttributeProperties) o).getName().compareTo(this.getName());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
        EJCoreReportVisualAttributeProperties other = (EJCoreReportVisualAttributeProperties) obj;
        if (_name == null)
        {
            if (other._name != null)
                return false;
        }
        else if (!_name.equals(other._name))
            return false;
        return true;
    }

}
