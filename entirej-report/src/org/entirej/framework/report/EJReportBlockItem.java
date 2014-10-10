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

import org.entirej.framework.report.properties.EJCoreReportItemProperties;

public class EJReportBlockItem implements Serializable
{
    private EJCoreReportItemProperties _itemProperties;

    public EJReportBlockItem(EJCoreReportItemProperties itemProps)
    {
        _itemProperties = itemProps;
    }

    /**
     * Returns the data type of this item
     * 
     * @return The Class of the datatype of this item
     */
    public Class<?> getDataType()
    {
        return _itemProperties.getDataTypeClass();
    }

    /**
     * Returns the name of the item
     * 
     * @return The name of this item
     */
    public String getName()
    {
        return _itemProperties.getName();
    }

    /**
     * Returns the name of the block to which this item belongs
     * 
     * @return the name of the block to which this item belongs
     */
    public String getBlockName()
    {
        return _itemProperties.getBlockName();
    }

    /**
     * Returns the value for the annotation defined for this items corresponding
     * method in the block pojo
     * <p>
     * If no fieldName annotation has been defined for the method then, this
     * method will return the name of this item
     * 
     * @return The field name of this item
     */
    public String getFieldName()
    {
        return _itemProperties.getFieldName();
    }

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether this
     * item receives its value from the blocks service or if it is entered
     * manually by the application developer
     * 
     * @return <code>true</code> if this is item receives its value from the
     *         blocks service otherwise <code>false</code>
     */
    public boolean isBlockServiceItem()
    {
        return _itemProperties.isBlockServiceItem();
    }

    /**
     * Returns the value to be used as a default when executing a query on the
     * block to which this item belongs
     * <p>
     * This could be a block item a form parameter or an application parameter
     * 
     * @return The default query value
     */
    public String getDefaultQueryValue()
    {
        return _itemProperties.getDefaultQueryValue();
    }

}
