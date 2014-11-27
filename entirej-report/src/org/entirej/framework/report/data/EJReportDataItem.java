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

import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public abstract class EJReportDataItem implements Serializable
{
   

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public abstract  String getName();

    /**
     * Sets the item with the given name to the given value
     * 
     * @param value
     *            The value to set
     */
    public  abstract   void setValue(Object value);


    /**
     * Used to retrieve this items value
     * 
     * @return The items value
     */
    public  abstract   Object getValue();


    /**
     * Returns the properties object of this report.
     * 
     * @return This reports properties
     */
    public  abstract   EJCoreReportItemProperties getProperties();

    public  abstract   void setVisualAttribute(String visualAttributeName);


    public  abstract   EJReportVisualAttributeProperties getVisualAttribute();


 

    /**
     * Returns true or false depending on whether this item receives its value
     * from the blocks service or if it is entered manually by the application
     * developer
     * 
     * @return <code>true</code> if this is item receives its value from the
     *         blocks service otherwise <code>false</code>
     */
    public  abstract   boolean isBlockServiceItem();

    public String toString()
    {
        Object _value = getValue();
        return getName() + ": " + (_value == null ? "NULL" : _value);
    }

}
