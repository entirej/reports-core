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
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.data.EJReportDataScreenItem;
import org.entirej.framework.report.enumerations.EJReportScreenSection;

public class EJReportRecord implements Serializable
{
    private EJReportDataRecord _dataRecord;

    public EJReportRecord(EJReportDataRecord dataRecord)
    {
        _dataRecord = dataRecord;
    }

    /**
     * Protected method that will return this records underlying DataRecord
     * <p>
     * This method must be kept protected so that end users cannot access it
     * 
     * @return This records underlying DataRecord
     */
    protected EJReportDataRecord getDataRecord()
    {
        return _dataRecord;
    }

    /**
     * This is a convenience method that returns the name of the block to which
     * this record belongs
     * 
     * @return The name of the block to which this record belongs
     */
    public String getBlockName()
    {
        return _dataRecord.getBlockName();
    }

    /**
     * Checks if a specific item name exists within the record. This can be used
     * before setting an items value so that no
     * <code>InvalidColumnNameException</code> is thrown.
     * 
     * @param itemName
     *            The item name to check for
     * @return <code>true</code> if the item exists otherwise <code>false</code>
     */
    public boolean containsItem(String itemName)
    {
        return _dataRecord.containsItem(itemName);
    }

    /**
     * Returns the {@link EJReportScreenItem} from this record with the name
     * specified
     * 
     * @param itemName
     *            The item to return
     * @param section
     *            The EJReportScreenSection where screen item belong
     * @param itemName
     *            The item to return
     * @return The {@link EJReportScreenItem} with the given name
     */
    public EJReportDataScreenItem getScreenItem(String itemName, EJReportScreenSection section)
    {
        return _dataRecord.getScreenItem(itemName, section);
    }

    /**
     * Sets the item with the given name to the value specified
     * <p>
     * The changes are automatically synchronized to the block renderer. If
     * multiple items should be set then it is advisable to use the
     * {@link #setValue(String, Object, boolean)} passing <code>true</code> to
     * the delaySynchronization parameter. This results in the values not being
     * automatically sent to the renderer, thus less gui refreshes. After all
     * items have been set, the {@link #synchronize()} method should be called
     * 
     * @param itemName
     *            The item name to set
     * @param value
     *            The value to set
     */
    public void setValue(String itemName, Object value)
    {
        _dataRecord.setValue(itemName, value);
    }

    /**
     * Returns the value of the data item with the given name
     * 
     * @param itemName
     *            The name of the item for which the value is required
     * 
     * @return The value of the required item or <code><b>null</b></code> if
     *         there is no item with the specified name
     */
    public Object getValue(String itemName)
    {
        return _dataRecord.getValue(itemName);
    }

    /**
     * This returns the underlying data entity of the block
     * <p>
     * The Data Entity is the POJO that is used by the EntireJ Block Service
     * 
     * @return This record as defined by its underlying entity object
     */
    public Object getBlockServicePojo()
    {
        return _dataRecord.getServicePojo();
    }

    /**
     * Returns a <code>Collection</code> of the column names as
     * <code>String</code> objects
     * 
     * @return A <code>Collection</code> of the column names in
     *         <code>String</code> format
     */
    public Collection<String> getColumnNames()
    {
        return Collections.unmodifiableCollection(_dataRecord.getColumnNames());
    }

    /**
     * Indicates how many columns the record has
     * 
     * @return The number of columns
     */
    public int getColumnCount()
    {
        return _dataRecord.getColumnCount();
    }

    /**
     * Indicates if the record has been retrieved from a datasource
     * 
     * @return <code>true</code> if the record was returned from a datasource
     *         otherwise <code>false</code>
     */
    public boolean isMarkedAsQueried()
    {
        return _dataRecord.isMarkedAsQueried();
    }

    public EJReportRecord copyValuesToRecord(EJReportRecord record)
    {
        return new EJReportRecord(_dataRecord.copyValuesToRecord(record.getDataRecord()));
    }

    /**
     * If the entity object is of the same type as defined for this record, then
     * all values from the given object will be copied to this record
     * 
     * @param entityObject
     *            The object containing the values to copy
     */
    public void copyValuesFromEntityObject(Object entityObject)
    {
        _dataRecord.copyValuesFromEntityObject(entityObject);
    }

}
