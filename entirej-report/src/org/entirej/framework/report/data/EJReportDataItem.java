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
package org.entirej.framework.report.data;

import java.io.Serializable;

import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.controllers.EJReportController;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJReportDataItem implements Serializable 
{
    private Object                            _value;
    private EJReportController                _reportController;
    private EJCoreReportItemProperties        _itemProperties;

    EJReportDataItem(EJReportController reportController, EJCoreReportItemProperties itemProperties)
    {
        _reportController = reportController;
        _itemProperties = itemProperties;

    }

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return _itemProperties.getName();
    }

    /**
     * Sets the item with the given name to the given value
     * 
     * @param value
     *            The value to set
     */
    public void setValue(Object value)
    {
        if (value != null)
        {
            if (!_itemProperties.getDataTypeClass().isAssignableFrom(value.getClass()))
            {
                throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.INVALID_DATA_TYPE_FOR_ITEM,
                        _itemProperties.getName(), _itemProperties.getDataTypeClassName(), value.getClass().getName()));
            }
        }
        _value = value;

    }

    /**
     * Used to retrieve this items value
     * 
     * @return The items value
     */
    public Object getValue()
    {
        return _value;
    }

    /**
     * Returns the properties object of this report.
     * 
     * @return This reports properties
     */
    public EJCoreReportItemProperties getProperties()
    {
        return _itemProperties;
    }


    public Class<?> getDataTypeClass()
    {
        return _itemProperties.getDataTypeClass();
    }



    /**
     * Returns true or false depending on whether this item receives its value
     * from the blocks service or if it is entered manually by the application
     * developer
     * 
     * @return <code>true</code> if this is item receives its value from the
     *         blocks service otherwise <code>false</code>
     */
    public boolean isBlockServiceItem()
    {
        return _itemProperties.isBlockServiceItem();
    }

    public String toString()
    {
        return _itemProperties.getName() + ": " + (_value == null ? "NULL" : _value);
    }

}
