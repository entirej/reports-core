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
package org.entirej.framework.report.data.controllers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportBlockActionProcessor;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.data.EJReportDataItem;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.enumerations.EJReportMessageLevel;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.service.EJReportBlockService;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportBlockController implements Serializable
{
    final Logger                                          logger          = LoggerFactory.getLogger(EJReportBlockController.class);

    /**
     * Used in conjunction with the deferred query property. This criteria will
     * contain the query criteria for the query to be executed
     */
    private EJReportQueryCriteria                         _queryCriteria  = null;

    private EJReportFrameworkManager                      _frameworkManager;
    private EJReportController                            _reportController;
    private EJReportDataBlock                             _dataBlock;
    private EJCoreReportBlockProperties                   _blockProperties;

    private LinkedHashMap<String, EJReportItemController> _itemProperties = new LinkedHashMap<String, EJReportItemController>();
    private final EJInternalReportBlock                   _internalBlock;

    public EJInternalReport getReport()
    {
        return getBlock().getReport();
    }

    private int index = -1;

    public EJInternalReportBlock getBlock()
    {
        return _internalBlock;
    }

    /**
     * Indicates that the user want to navigate to the next record
     */
    public void nextRecord()
    {
        EJReportDataRecord focusedRecord = getFocusedRecord();
        if(index==-1 && focusedRecord!=null)
        {
            index +=1;
            return ;
        }
        boolean hasMore = (index+1)<_dataBlock.getBlockRecordCount();
        if (hasMore&& focusedRecord != null && focusedRecord.isInitialised() && !focusedRecord.getBlock().getProperties().isControlBlock())
        {
            focusedRecord.dispose();
        }
       
        if(hasMore)
        {
            index +=1;
        }
       

        focusedRecord = getFocusedRecord();
        if (focusedRecord != null && !focusedRecord.isInitialised())
        {
            focusedRecord.initialise();
            getReportController().getActionController().postQuery(getReportController().getEJReport(), new EJReportRecord(focusedRecord));
        }
        
    }

  

    /**
     * Creates a controller for the given data block
     * 
     * @param reportController
     * @param blockProperties
     *            the properties of the given block
     * @param dataBlock
     *            the created controller will control this data block
     */
    public EJReportBlockController(EJReportController reportController, EJCoreReportBlockProperties blockProperties, EJReportDataBlock dataBlock)
    {
        if (dataBlock == null)
        {
            throw new EJReportRuntimeException("The DataBlock passed to the BlockController constructor is null");
        }
        _reportController = reportController;
        _frameworkManager = reportController.getFrameworkManager();
        _dataBlock = dataBlock;
        _blockProperties = blockProperties;

        _internalBlock = new EJInternalReportBlock(this);
        initialiseItems();

    }

    protected void initialiseItems()
    {
        logger.trace("START initialiseItems");

        for (EJCoreReportItemProperties props : _blockProperties.getItemContainer().getAllItemProperties())
        {
            _itemProperties.put(props.getName(), new EJReportItemController(this, props));

        }

        logger.trace("END initialiseItems");
    }

    /**
     * Sets the query criteria to be used when querying this blocks data service
     * <p>
     * Query criteria may be used to re-query a block or when deferred querying
     * is being made
     * 
     * @param queryCriteria
     *            The query criteria to set
     */
    public void setQueryCriteria(EJReportQueryCriteria queryCriteria)
    {
        _queryCriteria = queryCriteria;
    }

    /**
     * Returns the query criteria that has been set within this block
     * 
     * @return The blocks saved query criteria
     */
    public EJReportQueryCriteria getQueryCriteria()
    {
        return _queryCriteria;
    }

    /**
     * Returns a collection of all <code>EJDefaultItemController</code> for this
     * block controller
     * 
     * @return A <code>Collection</code> of <code>EJDefaultItemController</code>
     *         that have been added to this block controller
     */
    public Collection<EJReportItemController> getAllBlockItemControllers()
    {
        return _itemProperties.values();
    }

    /**
     * Returns the <code>EJDefaultItemController</code> for the item with the
     * given name
     * 
     * @param itemName
     *            The name of the item
     * @return The block item or <code>null</code> if there is no item with the
     *         given name
     */
    public EJReportItemController getBlockItemController(String itemName)
    {
        return _itemProperties.get(itemName);
    }

    /**
     * Returns the controller responsible for the report
     * 
     * @return The report controller
     */
    public EJReportController getReportController()
    {
        return _reportController;
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    public EJReportBlockService<?> getBlockService()
    {
        return _blockProperties.getBlockService();
    }

    public EJReportDataBlock getDataBlock()
    {
        return _dataBlock;
    }

    public void setDataBlock(EJReportDataBlock dataBlock)
    {
        _dataBlock = dataBlock;
    }

    /**
     * Returns the current focused record
     * <p>
     * The current record will be used within EJ when a user wished to update or
     * delete the current record. The current block record is not necessarily
     * the current displayed record. This is because it is possible for the
     * renderer to only display a subset of records. For example. If a user can
     * filter the displayed records, then the third displayed record is not
     * necessarily the third record the the data block. Therefore allow the
     * renderer to decide which is the current record
     * 
     * @return The record upon which the user currently has focus
     */
    public EJReportDataRecord getFocusedRecord()
    {
        if (index == -1 && _dataBlock.getBlockRecordCount() > 0)
        {
            return getRecord(0);
        }

        if (_dataBlock.getBlockRecordCount() > 0 && index > -1 && index < _dataBlock.getBlockRecordCount())
        {
            return getRecord(index);
        }
        return null;
    }

    public EJReportDataRecord getRecord(int recordNumber)
    {
        return getDataBlock().getRecord(recordNumber);
    }

    /**
     * Creates a new record containing all items defined within the blocks item
     * definitions
     * <p>
     * Each block item can have a default value defined by the
     * {@link EJCoreReportItemProperties#getCopyValueFrom()} property. This
     * method will ignore this property, to create a record and to use this
     * value call the {@link #createNewRecord(boolean)} method.
     * <p>
     * After the record has been created, the reports action processors
     * <code>whenCreateRecord</code> will be called
     * 
     * @param recordType
     *            The type of record to be created
     * @return a new record
     * @see {@link #createRecord(boolean)}
     */
    public EJReportDataRecord createRecord()
    {
        EJReportDataRecord record;
        record = createNewRecord();

        return record;
    }

    /**
     * Creates a record without calling the
     * {@link EJReportBlockActionProcessor#initialiseRecord(EJReport, EJReportRecord,EJRecordType)}
     * method
     * <p>
     * The values defined by the
     * {@link EJCoreReportItemProperties#getDefaultInsertValue()} will be
     * ignored. If these values should be used then use the
     * {@link #createNewRecord(boolean)} method
     * 
     * @return A new record
     * @see {@link #createRecord()}
     */
    public EJReportDataRecord createRecordNoAction()
    {
        return createNewRecord();
    }

    /**
     * Creates a new record based upon the items within defined within the
     * blocks list of items. The record will be neutral, meaning that it is
     * neither dirty or marked as new. These statuses will be added when adding
     * the record to the block. If this is a new record, then calling the
     * <code>recordCreated</code> will mark the record as new and add it to the
     * blocks list of record.
     * 
     * 
     * @return A new record
     */
    protected EJReportDataRecord createNewRecord()
    {
        EJReportDataRecord record = new EJReportDataRecord(_reportController, getBlock());
        _dataBlock.addQueriedRecord(record);
        return record;
    }

    /**
     * Copies the blocks focused record
     * <p>
     * All record items and values will be copied. The record will be marked as
     * insert and therefore can be added to the blocks list of inserted records.
     * If the record contains a primary key, then these values will need to be
     * changed before inserting the record to ensure that no integrity
     * constraints will be broken
     * 
     * After the record has been copied, the reports action processors
     * <code>initialiseRecord</code> will be called
     * 
     * @return The record copied from the current record
     */
    public EJReportDataRecord copyFocusedRecord()
    {
        logger.trace("START copyFocusedRecord");

        if (getFocusedRecord() == null)
        {
            return null;
        }

        EJReportDataRecord record = createRecordNoAction();

        if (getFocusedRecord() == null)
        {
            return record;
        }

        Iterator<EJReportDataItem> dataItems = getFocusedRecord().getAllItems().iterator();
        while (dataItems.hasNext())
        {
            EJReportDataItem item = dataItems.next();
            if (record.containsItem(item.getName()))
            {
                record.getItem(item.getName()).setValue(item.getValue());
            }
        }

        logger.trace("END  copyFocusedRecord");
        return record;
    }

    /**
     * Re-Executes the last query
     * 
     * @throws EJBlockControllerException
     */
    public void executeLastQuery()
    {
        if (_queryCriteria != null)
        {
            executeQuery(_queryCriteria);
        }
    }

    /**
     * Executes a query on this controllers underlying block. If this record is
     * a detail in a master-detail relationship then the relation items will be
     * copied from the master record before a query will be made. If the
     * underlying block is a master in a master-detail relationship then all
     * detail blocks where this block is the master will be issued an execute
     * query command.
     * <p>
     * The query will only be executed if the
     * <code>preventMasterlessOperations</code> is <code>false</code>. This flag
     * indicates that this controller is a detail of a master-detail
     * relationship and the master contains no records. As long as the parent
     * has no records, then the detail should not be able to perform any
     * actions.
     * <p>
     * This controllers renderer will be informed after the query operation has
     * been performed
     * <p>
     * If the block has changes then they will be cleared and the block reset.
     * The changes will be ignored. If the changes should be committed before a
     * new query is executed then the report renderer should check for open
     * changes before allowing the user to execute a new query.
     * 
     * @param queryCriteria
     *            The query criteria to be used for this block
     */
    public void executeQuery(EJReportQueryCriteria queryCriteria)
    {
        if (getBlockService() == null)
        {
            getReportController().getFrameworkManager().handleMessage(
                    new EJReportMessage(EJReportMessageLevel.MESSAGE, "Cannot perform query operation when no data service has been defined. Block: "
                            + getProperties().getName()));
            return;
        }

        if (queryCriteria == null)
        {
            throw new EJReportRuntimeException(new EJReportMessage("The query criteria passed to performQueryOperation is null."));
        }

        // Sets the query criteria for use within paging etc
        setQueryCriteria(queryCriteria);

        EJManagedReportFrameworkConnection connection = getFrameworkManager().getConnection();
        // Clear the block so that it is ready for the newly queried records
        try
        {
            getReportController().getActionController().preBlockQuery(getReportController().getEJReport(), getQueryCriteria());

            // Clear the block so that it is ready for the newly queried
            // records
            clearBlock();

            try
            {

                if (_blockProperties.getBlockService() == null)
                {
                    return;
                }

                _queryCriteria.setQueryAllRows(true);

                logger.trace("Calling execute query on service: {}", _blockProperties.getBlockService().getClass().getName());
                List<?> entities = _blockProperties.getBlockService().executeQuery(getReportController().getEJReport(), _queryCriteria);
                logger.trace("Execute query on block service completed. {} records retrieved", (entities == null ? 0 : entities.size()));

                if (entities != null)
                {

                    // Now loop through the retrieved records and add them to
                    // the
                    // block

                    // Create a post query cache so that lookups on each record
                    // are
                    // optimized

                    for (Object entity : entities)
                    {
                        EJReportDataRecord record = new EJReportDataRecord(_reportController, getBlock(), entity);

                        addQueriedRecord(record);
                    }
                    logger.trace("Completed post queries, clearing post query cache");

                }
            }
            catch (Exception e)
            {
                throw new EJReportRuntimeException(e);
            }
            finally
            {
                connection.close();
            }

        }
        finally
        {
            connection.close();
        }
    }

    /**
     * Adds a record to this controllers underlying list of records
     * <p>
     * This method should be called for each record the data access processor
     * retrieves. The reports action processors post-query method will be called
     * then the record will be added to the blocks underlying list of records
     * 
     * @param record
     */
    protected void addQueriedRecord(EJReportDataRecord record)
    {
        if (record != null)
        {
            _dataBlock.addQueriedRecord(record);

        }
    }

    /**
     * Returns the total number of records held within this controllers
     * underlying data block
     * 
     * @return The total number of records within this controllers underlying
     *         data block
     */
    public int getBlockRecordCount()
    {
        return _dataBlock.getBlockRecordCount();
    }

    /**
     * Returns the properties of this controllers data block
     * 
     * @return The properties of this controllers data block
     */
    public EJCoreReportBlockProperties getProperties()
    {
        return _blockProperties;
    }

    /**
     * Clears this controllers underlying data block
     * <p>
     * Only this controllers underlying data block will be cleared, if the block
     * is master in a master-detail relationship then its detail blocks will be
     * left untouched. To clear all detail blocks use the
     * {@link EJReportBlockController#clearAllDetailRelations()}
     * 
     * 
     * @see EJReportBlockController#clearAllDetailRelations()
     */
    public void clearBlock()
    {
        logger.trace("START clearBlock");

        _dataBlock.clearBlock();
        index = -1;

        logger.trace("END clearBlock");
    }

    /**
     * Returns a <code>Collection</code> of records within this block
     * 
     * @return A <code>Collection</code> containing the blocks records
     */
    public Collection<EJReportDataRecord> getRecords()
    {
        return _dataBlock.getRecords();
    }

}
