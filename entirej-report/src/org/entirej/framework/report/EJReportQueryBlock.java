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
package org.entirej.framework.report;

import java.util.Collection;

import org.entirej.framework.report.internal.EJReportDefaultServicePojoHelper;

public interface EJReportQueryBlock
{
    public String getName();

    public Collection<EJReportBlockItem> getItems();

    /**
     * Indicates if the block has an item with the given name
     * 
     * @param itemName
     *            the name to check for
     * @return <code>true</code> if the item exists within the block, otherwise
     *         <code>false</code>
     * 
     */
    public boolean containsItem(String itemName);

    public EJReportDefaultServicePojoHelper getServicePojoHelper();

    public EJReportBlockItem getItem(String itemName);

    /**
     * Return the report to which this block belongs
     * 
     * @return This report to which this block belongs
     */
    public EJReport getReport();
}
