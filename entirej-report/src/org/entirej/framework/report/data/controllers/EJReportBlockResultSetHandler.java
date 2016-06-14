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

import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.enumerations.EJReportMessageLevel;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.entirej.framework.report.service.EJReportResultSet;
import org.entirej.framework.report.service.EJReportResultSetBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportBlockResultSetHandler implements EJReportBlockDataHandler
{
    final Logger                    _logger        = LoggerFactory.getLogger(EJReportBlockResultSetHandler.class);

    private EJReportController      _reportController;
    private EJReportDataBlock       _dataBlock;
    private EJReportDataRecord      _focusedRecord;

    private EJReportResultSet       _resultSet;
    private EJReportBlockController _blockController;
    private EJReportQueryCriteria   _queryCriteria = null;
    private int                     _index         = -1;

    public EJReportBlockResultSetHandler(EJReportController reportController, EJReportBlockController controller, EJReportDataBlock dataBlock)
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

        // Clear the block so that it is ready for the newly queried records
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

            _blockController.getBlock();

            _resultSet = ((EJReportResultSetBlockService) _blockController.getBlockService()).executeQuery(_reportController.getEJReport(), _queryCriteria);
            _logger.trace("Execute query on block service completed.");
        }
        catch (Exception e)
        {
            throw new EJReportRuntimeException(e);
        }
    }

    /**
     * Clears this controllers underlying data block
     */
    public void clearBlock()
    {
        if (_resultSet != null)
        {
            _resultSet.close();
        }
    }

    /**
     * Returns the blocks current record
     * <p>
     * 
     * @return The record upon which the report has retrieved for this block
     */
    public EJReportDataRecord getCurrentRecord()
    {
        return _focusedRecord;

    }

    /**
     * Navigates to the next record and returns it
     * 
     * @return the next record or <code>null</code> if there are no more records
     */
    public EJReportDataRecord getNextRecord()
    {
        _focusedRecord = new EJReportDataRecord(_reportController, _blockController.getBlock(), _resultSet.getNext());
        return _focusedRecord;

    }
    
    @Override
    public void reset()
    {
        if (_resultSet != null)
        {
            _resultSet.close();
        }
        
    }

}
