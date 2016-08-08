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
package org.entirej.framework.report;

import java.io.Serializable;

import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties;
import org.entirej.framework.report.properties.EJCoreReportScreenItemProperties.ValueBaseItem;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJReportScreenItem implements Serializable
{
    private EJReportBlock                _block;
    private EJReportScreenItemProperties _itemProps;

    public EJReportScreenItem(EJReportBlock block, EJReportScreenItemProperties item)
    {
        _block = block;
        _itemProps = item;
    }

    public String getBlockName()
    {
        return _block.getName();
    }

    public EJReportScreenItemType getType()
    {
        return _itemProps.getType();
    }

    public int getWidth()
    {
        return _itemProps.getWidth();
    }

    public int getHeight()
    {
        return _itemProps.getHeight();
    }

    public boolean isWidthAsPercentage()
    {
        return _itemProps.isWidthAsPercentage();
    }

    public boolean isHeightAsPercentage()
    {
        return _itemProps.isHeightAsPercentage();
    }

    public int getXPos()
    {
        return _itemProps.getX();
    }

    public int getYPos()
    {
        return _itemProps.getY();
    }
    
    

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return _itemProps.getName();
    }

    /**
     * Used to set the visual attribute of this ite,
     * 
     * @param visualAttributeName
     *            The name of the visual attribute to set
     * @throws {@link IllegalArgumentException} if there is no visual attribute
     *         with the given name
     */
    public void setVisualAttribute(String visualAttributeName)
    {
        _itemProps.setVisualAttribute(visualAttributeName);
    }

    public EJReportVisualAttributeProperties getVisualAttributes()
    {
        return _itemProps.getVisualAttributeProperties();
    }

    /**
     * Returns the Visible set on this item true/false
     * 
     * @return The Visible set to this item o
     */
    public boolean isVisible()
    {
        return _itemProps.isVisible();
    }

    
    
    public int getRightPadding()
    {
        return _itemProps.getRightPadding();
    }

    public int getLeftPadding()
    {
        return _itemProps.getLeftPadding();
    }

   
    /**
     * Sets this item visible
     * <p>
     * visible set on data items will be displayed when the record containing
     * the item gains focus. Setting the visible to <code>false</code> removes
     * the item from print
     * 
     * @param visible
     *            The Visible to Item true/false
     */
    public void setVisible(boolean visible)
    {
        _itemProps.setVisible(visible);
    }

    public String toString()
    {
        return _itemProps.getName();
    }

    public <T> T typeAs(Class<T> type)
    {
        if (_itemProps.getClass().isAssignableFrom(ValueBaseItem.class))
        {
            return type.cast(new EJReportValueBaseScreenItem((ValueBaseItem) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Text.class))
        {
            return type.cast(new EJReportTextScreenItem((EJCoreReportScreenItemProperties.Text) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.AlignmentBaseItem.class))
        {
            return type.cast(new EJReportAlignmentBaseScreenItem((EJCoreReportScreenItemProperties.AlignmentBaseItem) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Number.class))
        {
            return type.cast(new EJReportNumberScreenItem((EJCoreReportScreenItemProperties.Number) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Date.class))
        {
            return type.cast(new EJReportDateScreenItem((EJCoreReportScreenItemProperties.Date) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Label.class))
        {
            return type.cast(new EJReportLabelScreenItem((EJCoreReportScreenItemProperties.Label) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Image.class))
        {
            return type.cast(new EJReportImageScreenItem((EJCoreReportScreenItemProperties.Image) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Line.class))
        {
            return type.cast(new EJReportLineScreenItem((EJCoreReportScreenItemProperties.Line) _itemProps));
        }
        if (_itemProps.getClass().isAssignableFrom(EJCoreReportScreenItemProperties.Rectangle.class))
        {
            return type.cast(new EJReportRectangleScreenItem((EJCoreReportScreenItemProperties.Rectangle) _itemProps));
        }

        return null;

    }

    public boolean typeOf(Class<?> type)
    {
        if (type.isAssignableFrom(EJReportValueBaseScreenItem.class))
        {
            return _itemProps instanceof ValueBaseItem;
        }
        if (type.isAssignableFrom(EJReportNumberScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Number;
        }
        if (type.isAssignableFrom(EJReportTextScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Text;
        }
        if (type.isAssignableFrom(EJReportDateScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Date;
        }
        if (type.isAssignableFrom(EJReportAlignmentBaseScreenItem.class))
        {
            return _itemProps instanceof  EJCoreReportScreenItemProperties.AlignmentBaseItem;
        }
        if (type.isAssignableFrom(EJReportRotatableScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.RotatableItem;
        }
        if (type.isAssignableFrom(EJReportLabelScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Label;
        }
        if (type.isAssignableFrom(EJReportImageScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Image;
        }
        if (type.isAssignableFrom(EJReportLineScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Line;
        }
        if (type.isAssignableFrom(EJReportRectangleScreenItem.class))
        {
            return _itemProps instanceof EJCoreReportScreenItemProperties.Rectangle;
        }

        return false;

    }

}
