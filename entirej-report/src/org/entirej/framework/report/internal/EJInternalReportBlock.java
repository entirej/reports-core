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
package org.entirej.framework.report.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.data.controllers.EJReportBlockController;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJInternalReportBlock implements Serializable
{
    final Logger                             logger = LoggerFactory.getLogger(EJInternalReportBlock.class);

    private EJReportDefaultServicePojoHelper _servicePojoHelper;
    private EJReportBlockController          _blockController;

    public EJInternalReportBlock(EJReportBlockController blockController)
    {
        _blockController = blockController;
    }

    public EJReportBlockController getBlockController()
    {
        return _blockController;
    }

    public String getName()
    {
        return _blockController.getProperties().getName();
    }

    public void initialiseServicePojoHelper()
    {
        if (_servicePojoHelper == null)
        {

            _servicePojoHelper = new EJReportDefaultServicePojoHelper(_blockController.getProperties());

            _servicePojoHelper.addFieldNamesToItems();
        }
    }

    public EJReportDefaultServicePojoHelper getServicePojoHelper()
    {
        if (_servicePojoHelper == null)
        {
            initialiseServicePojoHelper();
        }

        return _servicePojoHelper;
    }

    public boolean isControlBlock()
    {
        return _blockController.getProperties().isControlBlock();
    }

    /**
     * Returns the <code>InternalReport</code> to which this block belongs
     * 
     * @return The {@link EJInternalReport} to which this block belongs
     */
    public EJInternalReport getReport()
    {
        return _blockController.getReportController().getInternalReport();
    }

    /**
     * Returns the <code>FrameworkManager</code>
     * 
     * <p>
     * 
     * @return The <code>FrameworkManager</code>
     */
    public EJReportFrameworkManager getFrameworkManager()
    {
        return _blockController.getFrameworkManager();
    }

    /**
     * Instructs EntireJ to clear this block
     * <p>
     * If <code>disregardChanges</code> is <code>true</code> then all changes
     * made to the current block and all of its child blocks will be disregarded
     * 
     */
    public void clear()
    {
        _blockController.clearBlock();
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
        return new EJReportQueryCriteria(new EJReportBlock(_blockController.getBlock()));
    }

    /**
     * Used to create an empty record for this block
     * <p>
     * The whenCreateRecord within the blocks mediator will be fired once the
     * record has been created
     * 
     * @param recordType
     *            The type of record to be created
     * @return The newly created record
     */
    public EJReportDataRecord createRecord()
    {
        return _blockController.createRecord();
    }

    /**
     * Instructs EntireJ to perform a query on the given block using the
     * specified criteria
     * 
     * @param queryCriteria
     *            The criteria for the query
     */
    public void executeQuery(EJReportQueryCriteria queryCriteria)
    {
        logger.trace("START executeQuery");

        EJManagedReportFrameworkConnection connection = getFrameworkManager().getConnection();

        try
        {
            if (queryCriteria == null)
            {
                queryCriteria = new EJReportQueryCriteria(new EJReportBlock(_blockController.getBlock()));
            }
            _blockController.executeQuery(queryCriteria);
        }
        catch (EJReportRuntimeException e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJReportRuntimeException(e.getFrameworkMessage(), e);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJReportRuntimeException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.commit();
                connection.close();
            }
        }

        logger.trace("END executeQuery");
    }

    /**
     * Retrieves the current record for the given block
     * 
     * @param blockName
     *            The name of the block
     * @return The current focused record of the given block or
     *         <code>null</code> if there is no record focused
     */
    public EJReportDataRecord getCurrentRecord()
    {

        EJReportDataRecord focusedRecord = _blockController.getCurrentRecord();
        if (focusedRecord != null && !focusedRecord.isInitialised())
        {
            focusedRecord.initialise();
            _blockController.getReportController().getActionController()
                    .postQuery(_blockController.getReportController().getEJReport(), new EJReportRecord(focusedRecord));
        }
        return focusedRecord;
    }

    /**
     * Returns a collection of item names contained within this block
     * 
     * @return The names of all the items contained within the given block
     */
    public Collection<String> getItemNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        Collection<EJCoreReportItemProperties> itemPropsSet = _blockController.getProperties().getItemContainer().getAllItemProperties();
        for (EJCoreReportItemProperties itemProps : itemPropsSet)
        {
            names.add(itemProps.getName());
        }

        return names;
    }

    /**
     * Instructs the block to navigate to the next record
     * <p>
     * If the user is already on the last record, then nothing will happen
     */
    public boolean navigateToNextRecord()
    {
        logger.trace("START nextRecord");
        try
        {
            return _blockController.navigateToNextRecord();
        }
        finally
        {

            logger.trace("END nextRecord");
        }
    }

  

    /**
     * Returns the amount of records this block currently holds
     * 
     * @return The amount of records within this block
     */
    public int getBlockRecordCount()
    {
        logger.trace("START getBlockRecordCount");
        return _blockController.getDataBlock().getBlockRecordCount();
    }

    /**
     * Returns the properties of this block
     * 
     * @return This blocks properties
     */
    public EJCoreReportBlockProperties getProperties()
    {
        return _blockController.getProperties();
    }

 

    public void reset()
    {
        _blockController.reset();
        
    }

}
