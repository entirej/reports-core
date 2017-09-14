/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.data;

import java.io.Serializable;

import org.entirej.framework.report.data.controllers.EJReportController;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJReportDataScreenItem implements Serializable
{
    private final String                      name;
    private EJReportController                _reportController;
    private EJReportVisualAttributeProperties _vaProperties;
    private boolean                           _visible = true;

    EJReportDataScreenItem(EJReportController reportController, final String name)
    {
        _reportController = reportController;
        this.name = name;

    }

    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return name;
    }

    public void setVisualAttribute(String visualAttributeName)
    {
        if (visualAttributeName == null || visualAttributeName.trim().length() == 0)
        {
            _vaProperties = null;
            return;
        }

        EJReportVisualAttributeProperties vaProperties = _reportController.getInternalReport().getVisualAttribute(visualAttributeName);
        if (vaProperties == null)
        {
            throw new IllegalArgumentException("There is no visual attribute with the name " + visualAttributeName + " on this report.");
        }

        if (!vaProperties.isUsedAsDynamicVA())
        {
            throw new IllegalArgumentException(" Visual attribute with the name " + visualAttributeName
                    + " on this report is not marked as 'Used as Dynamic VA' .");
        }
        _vaProperties = vaProperties;
    }

    public EJReportVisualAttributeProperties getVisualAttribute()
    {
        return _vaProperties;
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
        _visible = visible;
    }

    /**
     * Returns the visible set for this item instance
     * 
     * @return This items visible true/false
     */
    public boolean isVisible()
    {
        return _visible;
    }

    public String toString()
    {
        return name;
    }

}
