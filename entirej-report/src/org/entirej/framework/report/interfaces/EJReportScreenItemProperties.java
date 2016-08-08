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
package org.entirej.framework.report.interfaces;

import java.io.Serializable;

import org.entirej.framework.report.enumerations.EJReportScreenItemType;
import org.entirej.framework.report.properties.EJCoreReportVisualAttributeProperties;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public interface EJReportScreenItemProperties extends Serializable
{

    public void setVisualAttribute(String visualAttributeName);

    /**
     * gets the visual attribute properties that should be used for this item
     * <p>
     * If the visual attribute properties are set to <code>null</code> then the
     * item should be displayed using its default display properties
     * 
     * @return The {@link EJCoreReportVisualAttributeProperties} that should be
     *         used for this item
     */
    public EJReportVisualAttributeProperties getVisualAttributeProperties();

    /**
     * Indicates if this item is to be made visible
     * <p>
     * 
     * @return <code>true</code> if the item should be made visible, otherwise
     *         <code>false</code>
     */
    public boolean isVisible();

    public void setVisible(boolean visible);

    public EJReportScreenItemType getType();

    /**
     * @return Returns the width of this canvas
     */
    public int getWidth();

    /**
     * @return Returns the height of this canvas
     */
    public int getHeight();

    public boolean isWidthAsPercentage();

    public boolean isHeightAsPercentage();

    /**
     * @return Returns the X of this canvas
     */
    public int getX();

    /**
     * @return Returns the Y of this canvas
     */
    public int getY();

    public String getName();

    public int getRightPadding();

    public int getLeftPadding();


}
