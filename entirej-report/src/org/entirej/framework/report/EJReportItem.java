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

import org.entirej.framework.report.data.EJReportDataItem;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJReportItem implements Serializable
{
    private EJInternalReportBlock _block;
    private EJReportDataItem      _dataItem;
    private EJReportDataRecord    _record;

    public EJReportItem(EJInternalReportBlock block, EJReportDataRecord record, EJReportDataItem item)
    {
        _block = block;
        _record = record;
        _dataItem = item;
    }

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return _dataItem.getName();
    }

    /**
     * Sets the item with the given name to the given value
     * 
     * @param value
     *            The value to set
     */
    public void setValue(Object value)
    {
        _dataItem.setValue(value);
    }

    /**
     * Used to set the item instance visual attribute
     * <P>
     * An item instance visual attribute will be displayed when the record this
     * item is contained within is displayed to the user. Depending on how the
     * block renderer is displaying the blocks records will depend on how this
     * visual attribute is displayed. If for example the block is displaying
     * records in a table, then the item instance visual attribute will be a
     * cell in the table. Whereas setting the screen item visual attribute will
     * set the visual attribute to the entire column
     * <p>
     * Setting the visual attribute to <code>null</code> will return the item
     * instance to its default visual attributes
     * <p>
     * 
     * @param visualAttributeName
     *            The name of the visual attribute to set
     * @throws {@link IllegalArgumentException} if there is no visual attribute
     *         with the given name
     * 
     * @see #getVisualAttribute()
     */
    public void setVisualAttribute(String visualAttributeName)
    {
        _dataItem.setVisualAttribute(visualAttributeName);

    }

    /**
     * Returns the visual attribute properties set on this item or
     * <code>null</code> if no visual attribute has been set
     * 
     * @return The visual attribute properties of this item or <code>null</code>
     *         if no visual attribute has been set
     * 
     * @see #setVisualAttribute(String)
     */
    public EJReportVisualAttributeProperties getVisualAttribute()
    {
        return _dataItem.getVisualAttribute();
    }



    /**
     * Used to retrieve this items value
     * 
     * @return The items value
     */
    public Object getValue()
    {
        return _dataItem.getValue();
    }

    public String toString()
    {
        return _dataItem.toString();
    }

}
