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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    //
    // public EJReportBlockProperties getProperties()
    // {
    // return _block.getProperties();
    // }

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

    //
    // /**
    // * Used to create an empty record for this block
    // * <p>
    // * The whenCreateRecord within the blocks processor will be fired once the
    // * record has been created
    // *
    // * @return The newly created record
    // */
    // public EJReportRecord createRecord()
    // {
    // return new EJReportRecord(_block.createRecord());
    // }
    //
    // /**
    // * Clears this blocks data
    // * <p>
    // * Only this controllers underlying data block will be cleared, if the
    // block
    // * is master in a master-detail relationship then its detail blocks will
    // be
    // * left untouched. To clear all detail blocks use the
    // * {@link EJBlock#clearAllDetailRelations(boolean))}
    // *
    // * @see EJReportBlockController#clearAllDetailRelations()
    // */
    // public void clear()
    // {
    // _block.clear();
    // }
    //
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

    //
    // /**
    // * Instructs EntireJ to perform a query on the given block using the
    // * specified criteria
    // *
    // * @param queryCriteria
    // * The criteria for the query
    // */
    // public void executeQuery(EJReportQueryCriteria queryCriteria)
    // {
    // if (queryCriteria.getBlock() == null)
    // {
    // queryCriteria.setBlock(this);
    // }
    // _block.executeQuery(queryCriteria);
    // }

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
     * Navigates to the next record of this block
     * <p>
     * If this block has no records then no action will be performed
     */
    public boolean navigateToNextRecord()
    {
        return _block.navigateToNextRecord();
    }

    public int getRecordCount()
    {
        return _block.getBlockRecordCount();
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
