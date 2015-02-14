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

import org.entirej.framework.report.data.EJReportDataScreenItem;
import org.entirej.framework.report.interfaces.EJReportScreenItemProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJReportScreenItem implements Serializable
{
    private boolean                      _useDataItem = true;

    private EJReportDataScreenItem       _dataItem;
    private EJReportScreenItemProperties _itemProps;

    public EJReportScreenItem(EJReportScreenItemProperties item)
    {
        _useDataItem = false;
        _itemProps = item;
    }

    public EJReportScreenItem(EJReportDataScreenItem item)
    {
        _useDataItem = true;
        _dataItem = item;
    }

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        if (_useDataItem)
        {
            return _dataItem.getName();
        }
        else
        {
            return _itemProps.getName();
        }
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
        if (_useDataItem)
        {
            _dataItem.setVisualAttribute(visualAttributeName);
        }
        else
        {
            _itemProps.setVisualAttribute(visualAttributeName);
        }
    }

    /**
     * Returns the Visible set on this item true/false
     * 
     * @return The Visible set to this item o
     */
    public boolean isVisible()
    {
        if (_useDataItem)
        {
            return _dataItem.isVisible();
        }
        else
        {
            return _itemProps.isVisible();
        }
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
        if (_useDataItem)
        {
            _dataItem.setVisible(visible);
        }
        else
        {
            _itemProps.setVisible(visible);
        }
    }

    public String toString()
    {
        if (_useDataItem)
        {
            return _dataItem.toString();
        }
        else
        {
            return _itemProps.getName();
        }
    }
}
