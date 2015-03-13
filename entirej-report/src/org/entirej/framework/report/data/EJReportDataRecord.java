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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import org.entirej.framework.report.data.controllers.EJReportController;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;

public class EJReportDataRecord implements Serializable
{
    private EJReportController                      _reportController;
    private EJReportDataRecord                      _baseRecord;
    private Object                                  _servicePojo;
    private EJInternalReportBlock                   _block;

    private HashMap<String, EJReportDataItem>       _itemList;
    private HashMap<String, EJReportDataScreenItem> _itemListH;
    private HashMap<String, EJReportDataScreenItem> _itemListD;
    private HashMap<String, EJReportDataScreenItem> _itemListF;
    private boolean                                 _queriedRecord = false;

    private AtomicBoolean                           init           = new AtomicBoolean();

    /**
     * Returns the properties of the block that contains this record
     * 
     * @return The properties of the block that contains this record
     */
    public EJReportDataRecord(EJReportController reportController, EJInternalReportBlock block)
    {
        _reportController = reportController;
        _block = block;
        _servicePojo = getBlock().getServicePojoHelper().createNewPojoFromService();
        _itemList = new HashMap<String, EJReportDataItem>();

        if (_block.isControlBlock())
        {
            initialise();
        }

    }

    /**
     * Returns the properties of the block that contains this record
     * 
     * @return The properties of the block that contains this record
     */
    public EJReportDataRecord(EJReportController reportController, EJInternalReportBlock block, Object servicePojo)
    {
        _reportController = reportController;
        _block = block;
        _servicePojo = servicePojo;
        _itemList = new HashMap<String, EJReportDataItem>();
        if (_block.isControlBlock())
        {
            initialise();
        }
    }

    /**
     * If the user wishes to update the current record, EntireJ will wrap the
     * current record within another record and then ask the block renderer to
     * update the wrapper record. This functionality will ensure that
     * modifications to the block records are only made after all validation
     * routines are successful. If validation fails, the block record remains
     * untouched
     * 
     * @param dataRecord
     *            The block record to wrap
     */
    public void setBaseRecord(EJReportDataRecord dataRecord)
    {
        _baseRecord = dataRecord;
    }

    /**
     * Returns the block record that is being updated
     * <p>
     * The base record will only have its values modified after all validation
     * routines are successful. The base record will only be set when the
     * standard editing feature is used. If the record is updated manually by
     * the developer within the action processors then the changes will be made
     * directly to the block record and no validation will be performed
     * 
     * @return The block record being updated or <code>null</code> if the record
     *         is not being updated by the framework update functionality
     */
    public EJReportDataRecord getBaseRecord()
    {
        return _baseRecord;
    }

    /**
     * Returns the underlying pojo for this record
     * 
     * @return The pojo used by the block service
     */
    public Object getServicePojo()
    {
        return _servicePojo;
    }

    public boolean isInitialised()
    {
        return init.get();
    }

    public void initialise()
    {

        initialiseRecord(_reportController, _servicePojo);
        init.set(true);
    }

    public void dispose()
    {
        init.set(false);
        _itemList = null;
        _itemListF = null;
        _itemListD = null;
        _itemListH = null;
        _servicePojo = null;
        _reportController = null;
        _block = null;
    }

    private void initialiseRecord(EJReportController reportController, Object sourceEntityObject)
    {
        for (EJCoreReportItemProperties itemProps : _block.getProperties().getItemContainer().getAllItemProperties())
        {
            EJReportDataItem item = new EJReportDataItem(reportController, itemProps);
            addItem(item);
        }

        if (sourceEntityObject != null)
        {
            copyValuesFromEntityObject(sourceEntityObject);
        }

    }

    public void copyValuesFromEntityObject(Object servicePojo)
    {
        // Check that both the data entity passed is compatible with the one
        // assigned to this record. If either are null, then nothing can be
        // done, so return
        if (_servicePojo != null && servicePojo != null)
        {
            if (!_servicePojo.getClass().isAssignableFrom(servicePojo.getClass()))
            {
                return;
            }
        }
        else
        {
            return;
        }

        _block.getServicePojoHelper().copyValuesFromServicePojo(_itemList.values(), servicePojo);
    }

    public EJInternalReportBlock getBlock()
    {
        return _block;
    }

    //
    // /**
    // * Returns the properties of the block to which this record belongs
    // *
    // * @return This records containing block
    // */
    // public EJCoreReportBlockProperties getBlockProperties()
    // {
    // return _block.getProperties();
    // }

    /**
     * This is a convenience method that returns the name of the block to which
     * this record belongs
     * 
     * @return The name of the block to which this record belongs
     */
    public String getBlockName()
    {
        return _block.getName();
    }

    /**
     * Adds a new dataItem to this record. Only items added to the record can be
     * modified. Trying to change an item that has not been added to the record
     * will result in an <code>InvalidColumnNameException</code>
     * 
     * @exception EJDuplicateColumnException
     *                Thrown if an attempt is made to add an item that has
     *                already been added
     */
    private void addItem(EJReportDataItem item)
    {
        if (item != null)
        {
            if (_itemList.containsKey(item.getName().toLowerCase()))
            {
                throw new IllegalArgumentException("This record already contains an item. Item name: " + item.getName());
            }
            _itemList.put(item.getName().toLowerCase(), item);
        }
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
        if (itemName == null || itemName.trim().length() == 0)
        {
            return false;
        }

        return _itemList.containsKey(itemName.toLowerCase());
    }

    /**
     * Returns the <code>DataItem</code> from this record with the name
     * specified
     * 
     * @param itemName
     *            The item to return
     * @return The <code>DataItem</code> with the given name, or
     *         <code>null</code> if there is no item within this record with the
     *         given name
     */
    public EJReportDataItem getItem(String itemName)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            throw new IllegalArgumentException("The item name passd to getItem is either a zero lenght string or null");
        }

        EJReportDataItem item = _itemList.get(itemName.toLowerCase());

        if (item != null)
        {
            return item;
        }

        throw new IllegalArgumentException("No such item called " + itemName + " within block " + getBlockName());
    }

    /**
     * Returns the <code>EJReportDataScreenItem</code> from this record with the
     * name specified
     * 
     * @param itemName
     *            The item to return
     * @param section
     *            The EJReportScreenSection where screen item belong
     * @return The <code>EJReportDataScreenItem</code> with the given name,
     */
    public EJReportDataScreenItem getScreenItem(String itemName, EJReportScreenSection section)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            throw new IllegalArgumentException("The screen item name passd to getItem is either a zero lenght string or null");
        }

        HashMap<String, EJReportDataScreenItem> map;

        switch (section)
        {
            case HEADER:
                if (_itemListH == null)
                    _itemListH = new HashMap<String, EJReportDataScreenItem>();
                map = _itemListH;
                break;
            case DETAIL:
                if (_itemListD == null)
                    _itemListD = new HashMap<String, EJReportDataScreenItem>();
                map = _itemListD;
                break;
            case FOOTER:
                if (_itemListF == null)
                    _itemListF = new HashMap<String, EJReportDataScreenItem>();
                map = _itemListF;
                break;

            default:
                return null;
        }

        EJReportDataScreenItem item = map.get(itemName);

        if (item == null)
        {
            /*
             * TODO:EJCoreReportScreenProperties layoutScreenProperties =
             * _block.getProperties().getLayoutScreenProperties();
             * if(layoutScreenProperties
             * .getScreenType()==EJReportScreenType.FORM_LATOUT) {
             * layoutScreenProperties.get } switch (section) { case HEADER:
             * 
             * break; case DETAIL:
             * 
             * break; case FOOTER:
             * 
             * break;
             * 
             * default: return null; }
             */

            item = new EJReportDataScreenItem(_reportController, itemName);
            map.put(itemName, item);
        }
        return item;
    }

    /**
     * Sets the item with the given name to the given value
     * <p>
     * If a value has been set and needs to be refreshed on the screen, then the
     * {@link EditableBlock#synchronise(EJReportDataRecord)} method must be
     * called.
     * <p>
     * <b>Call the synchronise method after all items have been modified in
     * order to reduce client side synchronisation performance</b>
     * 
     * @param itemName
     *            The name of the item to set
     * @param value
     *            The value to set
     * 
     * @see EJReportDataItem#setValue(Object)
     */
    public void setValue(String itemName, Object value)
    {
        getItem(itemName).setValue(value);
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
        EJReportDataItem item = getItem(itemName);

        if (item == null)
        {
            return null;
        }
        return item.getValue();
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
        return _itemList.keySet();
    }

    /**
     * Returns a <code>Collection</code> of <code>DataItems</code> contained
     * within this record
     * 
     * @return The <code>Collection</code> of <code>DataItems</code>
     */
    public Collection<EJReportDataItem> getAllItems()
    {
        return _itemList.values();
    }

    //
    // /**
    // * Returns the properties for a given item
    // *
    // * @param name
    // * The name of the item
    // * @return The properties for the given item
    // * @throws EJReportRuntimeException
    // * if the name passed is either null or of zero length
    // */
    // public EJCoreReportItemProperties getItemProperties(String name)
    // {
    // if (name == null || name.trim().length() == 0)
    // {
    // throw new EJReportRuntimeException(new
    // EJReportMessage("The name passed to getItemProperties is either null or of zero length."));
    // }
    //
    // EJReportDataItem item = getItem(name);
    // return item.getProperties();
    // }
    //
    // /**
    // * Returns a <code>Collection</code> containing all
    // * <code>ItemProperties</code> contained within this
    // <code>DataRecord</code>
    // *
    // * @return A <code>Collection</code> of this records
    // * <code>ItemProperties</code>
    // */
    // public Collection<EJCoreReportItemProperties> getAllItemProperties()
    // {
    // ArrayList<EJCoreReportItemProperties> properties = new
    // ArrayList<EJCoreReportItemProperties>();
    //
    // Iterator<EJReportDataItem> values = _itemList.values().iterator();
    //
    // while (values.hasNext())
    // {
    // EJReportDataItem item = values.next();
    //
    // properties.add(item.getProperties());
    // }
    // return properties;
    // }

    /**
     * Indicates how many columns the record has
     * 
     * @return The number of columns
     */
    public int getColumnCount()
    {
        return getColumnNames().size();
    }

    /**
     * Marking the record as queried indicates that the record has been
     * retrieved from a data source
     * 
     * @param queriedIndicator
     */
    public void markAsQueried(boolean queriedIndicator)
    {
        _queriedRecord = queriedIndicator;
    }

    /**
     * Indicates if the record has been retrieved from a datasource
     * 
     * @return <code>true</code> if the record was returned from a datasource
     *         otherwise <code>false</code>
     */
    public boolean isMarkedAsQueried()
    {
        return _queriedRecord;
    }

    /**
     * Clears all values of the record
     * 
     */
    public void clear()
    {
        for (EJReportDataItem item : getAllItems())
        {
            item.setValue(null);
        }
    }

    /**
     * This will set the service pojo with the value given value
     * <p>
     * This method is called by the data item each time the items value has been
     * set
     */
    public void valueChanged(String itemName, Object value)
    {
        // Set the data entity to the value specified
        _block.getServicePojoHelper().setValue(itemName, _servicePojo, value);

    }

    public EJReportDataRecord copyValuesToRecord(EJReportDataRecord record)
    {
        Iterator<EJReportDataItem> dataItems = getAllItems().iterator();
        while (dataItems.hasNext())
        {
            EJReportDataItem item = dataItems.next();
            if (record.containsItem(item.getName()))
            {
                record.getItem(item.getName()).setValue(item.getValue());
            }
        }
        return record;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Record:\n");

        Iterator<EJReportDataItem> items = _itemList.values().iterator();
        while (items.hasNext())
        {
            buffer.append("    ");
            buffer.append(items.next());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
