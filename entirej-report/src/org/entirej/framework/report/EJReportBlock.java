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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.internal.EJReportDefaultServicePojoHelper;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.service.EJReportQueryCriteria;

public class EJReportBlock implements EJReportQueryBlock, Serializable
{
    private EJInternalReportBlock _block;

    public EJReportBlock(EJInternalReportBlock block)
    {
        _block = block;
    }

    /**
     * Return the report to which this block belongs
     * 
     * @return This report to which this block belongs
     */
    public EJReport getReport()
    {
        return new EJReport(_block.getReport());
    }

    public EJReportScreen getScreen()
    {
        return new EJReportScreen(this, _block.getProperties().getScreenProperties());
    }

    public Collection<EJReportBlockItem> getItems()
    {
        ArrayList<EJReportBlockItem> blockItems = new ArrayList<EJReportBlockItem>();

        for (EJCoreReportItemProperties item : _block.getProperties().getAllItemProperties())
        {
            blockItems.add(new EJReportBlockItem(item));
        }

        return blockItems;
    }

    public EJReportBlockItem getItem(String itemName)
    {
        if (_block.getProperties().getItemProperties(itemName) == null)
        {
            throw new IllegalArgumentException("There is no item called " + itemName + " on block " + _block.getProperties().getName());
        }

        return new EJReportBlockItem(_block.getProperties().getItemProperties(itemName));
    }

    /**
     * Indicates if this block is a control block
     * 
     * @return <code>true</code> if this block is a control block, otherwise
     *         <code>false</code>
     */
    public boolean isControlBlock()
    {
        return _block.getProperties().isControlBlock();
    }

    /**
     * Indicates if the block has an item with the given name
     * 
     * @param itemName
     *            the name to check for
     * @return <code>true</code> if the item exists within the block, otherwise
     *         <code>false</code>
     * 
     */
    public boolean containsItem(String itemName)
    {
        EJCoreReportItemProperties itemProps = _block.getProperties().getItemProperties(itemName);
        if (itemProps == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the name of this block
     * 
     * @return This blocks name
     */
    public String getName()
    {
        return _block.getProperties().getName();
    }

    /**
     * Creates an empty query criteria for this block
     * <p>
     * The criteria can be used for executing a query within this block.
     * Criterion can be added to restrict the query
     * 
     * @return
     */
    public EJReportQueryCriteria createQueryCriteria()
    {
        return _block.createQueryCriteria();
    }

    /**
     * Instructs EntireJ to perform a query on the given block using no query
     * criteria
     * <p>
     * The block will create an empty {@link EJReportQueryCriteria}
     */
    public void executeQuery()
    {
        _block.executeQuery(createQueryCriteria());
    }

    /**
     * Retrieves the current record for the given block
     * 
     * @param blockName
     *            The name of the block
     * @return The current record of the given block or <code>null</code> if
     *         there is no current focused
     */
    public EJReportRecord getCurrentRecord()
    {
        EJReportDataRecord record = _block.getCurrentRecord();
        if (record == null)
        {
            return null;
        }
        else
        {
            return new EJReportRecord(record);
        }
    }

    /**
     * Navigates to the next record and returns it
     * 
     * @return the next record or <code>null</code> if there are no more records
     */
    public EJReportRecord getNextRecord()
    {
        EJReportDataRecord record = _block.getNextRecord();
        if (record == null)
        {
            return null;
        }
        else
        {
            return new EJReportRecord(record);
        }
    }
    
    public EJReportDefaultServicePojoHelper getServicePojoHelper()
    {
        return _block.getServicePojoHelper();
    }

    public void reset()
    {
        _block.reset();
        
    }

}
