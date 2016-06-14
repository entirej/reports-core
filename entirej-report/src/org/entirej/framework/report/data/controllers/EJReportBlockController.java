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
import java.util.LinkedHashMap;

import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.service.EJReportBlockService;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.entirej.framework.report.service.EJReportResultSetBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportBlockController implements Serializable
{
    final Logger                                              logger          = LoggerFactory.getLogger(EJReportBlockController.class);

    private EJReportFrameworkManager                          _frameworkManager;
    private EJReportController                                _reportController;
    private EJReportBlockDataHandler                          _dataHandler;
    private EJCoreReportBlockProperties                       _blockProperties;

    private LinkedHashMap<String, EJCoreReportItemProperties> _itemProperties = new LinkedHashMap<String, EJCoreReportItemProperties>();
    private final EJInternalReportBlock                       _internalBlock;

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
        _blockProperties = blockProperties;

        _internalBlock = new EJInternalReportBlock(this);
        initialiseItems();

        if (blockProperties.getBlockService() instanceof EJReportResultSetBlockService)
        {
            _dataHandler = new EJReportBlockResultSetHandler(_reportController, this, dataBlock);
        }
        else
        {
            _dataHandler = new EJReportBlockEntitiesHandler(_reportController, this, dataBlock);
        }

    }

    protected void initialiseItems()
    {
        logger.trace("START initialiseItems");

        for (EJCoreReportItemProperties props : _blockProperties.getItemContainer().getAllItemProperties())
        {
            _itemProperties.put(props.getName(), props);

        }

        logger.trace("END initialiseItems");
    }

    public EJInternalReport getReport()
    {
        return getBlock().getReport();
    }

    public EJInternalReportBlock getBlock()
    {
        return _internalBlock;
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
        _dataHandler.setQueryCriteria(queryCriteria);
    }

    /**
     * Returns the query criteria that has been set within this block
     * 
     * @return The blocks saved query criteria
     */
    public EJReportQueryCriteria getQueryCriteria()
    {
        return _dataHandler.getQueryCriteria();
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

    /**
     * Returns the blocks current record
     * <p>
     * 
     * @return The record upon which the report has retrieved for this block
     */
    public EJReportDataRecord getCurrentRecord()
    {
        return _dataHandler.getCurrentRecord();
    }
 
    /**
     * Navigates to the next record and returns it
     * 
     * @return the next record or <code>null</code> if there are no more records
     */
    public EJReportDataRecord getNextRecord()
    {
        return _dataHandler.getNextRecord();
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
        return new EJReportDataRecord(_reportController, getBlock());
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
        _dataHandler.executeQuery(queryCriteria);
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
        _dataHandler.clearBlock();
    }

    public void reset()
    {
        _dataHandler.reset();
    }
}
