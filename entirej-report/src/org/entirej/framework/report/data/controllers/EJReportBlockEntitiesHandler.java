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

import java.util.List;

import org.entirej.framework.report.EJReportManagedFrameworkConnection;
import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.enumerations.EJReportMessageLevel;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportBlockEntitiesHandler implements EJReportBlockDataHandler
{
    final Logger                    _logger        = LoggerFactory.getLogger(EJReportBlockEntitiesHandler.class);

    private EJReportController      _reportController;
    private EJReportDataBlock       _dataBlock;
    private EJReportBlockController _blockController;
    private EJReportQueryCriteria   _queryCriteria = null;
    private int                     _index         = -1;
    private int                     _count         = 0;

    public EJReportBlockEntitiesHandler(EJReportController reportController, EJReportBlockController controller, EJReportDataBlock dataBlock)
    {
        _reportController = reportController;
        _blockController = controller;
        _dataBlock = dataBlock;
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

    public void executeQuery(EJReportQueryCriteria queryCriteria)
    {
        if (_blockController.getBlockService() == null)
        {
            _reportController.getFrameworkManager().handleMessage(new EJReportMessage(EJReportMessageLevel.MESSAGE,
                    "Cannot perform query operation when no data service has been defined. Block: " + _blockController.getProperties().getName()));
            return;
        }

        if (queryCriteria == null)
        {
            throw new EJReportRuntimeException(new EJReportMessage("The query criteria passed to performQueryOperation is null."));
        }

        // Sets the query criteria for use within paging etc
        _queryCriteria = queryCriteria;

        EJReportManagedFrameworkConnection connection = _reportController.getEJReport().getConnection();
        // Clear the block so that it is ready for the newly queried records
        try
        {
            _reportController.getActionController().preBlockQuery(_reportController.getEJReport(), _queryCriteria);

            // Clear the block so that it is ready for the newly queried
            // records
            clearBlock();

            try
            {

                if (_blockController.getBlockService() == null)
                {
                    return;
                }

                _queryCriteria.setQueryAllRows(true);

                _logger.trace("Calling execute query on service: {}", _blockController.getClass().getName());
                List<?> entities = _blockController.getBlockService().executeQuery(_reportController.getEJReport(), _queryCriteria);
                _logger.trace("Execute query on block service completed. {} records retrieved", (entities == null ? 0 : entities.size()));

                if (entities != null)
                {
                    for (Object entity : entities)
                    {
                        _dataBlock.addRecord(new EJReportDataRecord(_reportController, _blockController.getBlock(), entity));
                    }
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
        _logger.trace("START clearBlock");

        _dataBlock.clearBlock();
        _index = -1;

        _logger.trace("END clearBlock");
    }

    /**
     * Returns the blocks current record
     * <p>
     * 
     * @return The record upon which the report has retrieved for this block
     */
    public EJReportDataRecord getCurrentRecord()
    {

        return _dataBlock.getTopRecord(_blockController);

    }

    /**
     * Navigates to the next record and returns it
     * 
     * @return the next record or <code>null</code> if there are no more records
     */
    public EJReportDataRecord getNextRecord()
    {
        EJReportDataRecord focusedRecord = getCurrentRecord();

        if (_index == -1 && focusedRecord != null)
        {
            _count = _dataBlock.getBlockRecordCount();
            _index++;
            return focusedRecord;
        }

        if (_dataBlock.getBlockRecordCount() > 1)
        {
            _dataBlock.removeTop();
        }
        _index++;
        boolean hasMore = _count > _index;

        if (hasMore)
        {

            focusedRecord = getCurrentRecord();
            if (focusedRecord != null && !focusedRecord.isInitialised())
            {
                focusedRecord.initialise();
                _blockController.getReportController().getActionController().postQuery(_blockController.getReportController().getEJReport(),
                        new EJReportRecord(focusedRecord));
            }
        }

        return hasMore? focusedRecord :null;

    }

    @Override
    public void reset()
    {
        _index = -1;
        _count = 0;
    }

    @Override
    public void addRecord(EJReportDataRecord ejReportDataRecord)
    {
        _dataBlock.addRecord(ejReportDataRecord);
        _count++;
        
    }

}
