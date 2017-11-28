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
package org.entirej.framework.report.data.controllers;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportParameterList;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportData;
import org.entirej.framework.report.data.EJReportDataBlock;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportController implements Serializable
{
    private final Logger                             LOGGER            = LoggerFactory.getLogger(EJReportController.class);

    private EJReportFrameworkManager                 _frameworkManager;
    private EJReportParameterList                    _parameterList;

    private EJReportActionController                 _reportActionController;
    private EJInternalReport                         _report;
    private EJReport                                 _ejReport;
    private EJReportData                             _dataReport;
    private HashMap<String, EJReportBlockController> _blockControllers = new HashMap<String, EJReportBlockController>();

    EJReportController(EJReportFrameworkManager frameworkManager, EJReportData reportData)
    {
        LOGGER.trace("START Constructor");

        if (reportData == null)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(
                    EJReportFrameworkMessage.NULL_DATA_REPORT_PASSED_TO_REPORT_CONTROLLER));
        }

        _frameworkManager = frameworkManager;
        _dataReport = reportData;
        createParameterList();

        // First create the actionProcessor instance as this is can be used
        // within the block renderer initialization
        // If none has been defined, use the framework default action processor
        _reportActionController = new EJReportActionController(this);

        _report = new EJInternalReport(this);
        _ejReport = new EJReport(_report);

        initialiseController();

        LOGGER.trace("END Constructor");
    }



    private void createParameterList()
    {
        EJReportParameterList list = new EJReportParameterList();
        for (EJApplicationLevelParameter parameter : _dataReport.getProperties().getAllReportParameters())
        {
            EJReportParameter listParameter = new EJReportParameter(parameter.getName(), parameter.getDataType());
            list.addParameter(listParameter);
        }
        _parameterList = list;
    }

    public EJReportParameterList getParameterList()
    {
        return _parameterList;
    }

    private void initialiseController()
    {
        LOGGER.trace("START initialiseController");
        EJCoreReportProperties reportProperties = _dataReport.getProperties();

        LOGGER.trace("Setting up blocks");

        for (EJReportDataBlock block : _dataReport.getAllBlocks())
        {
            EJCoreReportBlockProperties blockProps = _dataReport.getProperties().getBlockProperties(block.getName());
            EJReportBlockController blockController = new EJReportBlockController(this, blockProps, block);

            if (blockProps.isControlBlock())
            {
                blockController.createRecord();
            }
            _blockControllers.put(blockProps.getName(), blockController);

        }
        LOGGER.trace("DONE setting up blocks");

        LOGGER.trace("Initialising Pojo Helpers");
        for (EJReportBlockController parentController : _blockControllers.values())
        {
            parentController.getBlock().initialiseServicePojoHelper();
        }
        LOGGER.trace("DONE initialising Pojo Helpers");

        LOGGER.trace("END initialiseController");
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    public EJReportActionController getActionController()
    {
        return _reportActionController;
    }

    /**
     * Return the manager for this report
     * <p>
     * The report manager is used by developers within the action processor to
     * interact with the report and the <code>EntireJ Framework</code>
     * 
     * @return
     */
    public EJInternalReport getInternalReport()
    {
        return _report;
    }

    public EJReport getEJReport()
    {
        return _ejReport;
    }

    /**
     * Returns a collection of all <code>BlockControllers</code> for this report
     * controller
     * 
     * @return A <code>Collection</code> of <code>BlockControllers</code> that
     *         have been added to this report controller
     */
    public Collection<EJReportBlockController> getAllBlockControllers()
    {
        return _blockControllers.values();
    }

    /**
     * Returns the underlying properties of this report
     * 
     * @return The report properties of this reports underlying data report
     */
    public EJCoreReportProperties getProperties()
    {
        return _dataReport.getProperties();
    }

    /**
     * Returns a given <code>BlockController</code> for a given block
     * 
     * @param blockName
     *            The name of the blocks for which the controller should be
     *            returned
     * @return The given blocks controller or <code>null</code> if there is no
     *         block controller with the given name
     */
    public EJReportBlockController getBlockController(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return null;
        }

        return _blockControllers.get(blockName);
    }

    /**
     * Clears the reports blocks and any embedded reports of any data
     * <p>
     * 
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the underlying
     *            blocks of this report should also be cleared
     */
    public void clearReport()
    {
        Iterator<EJReportBlockController> blockControllers = getAllBlockControllers().iterator();
        while (blockControllers.hasNext())
        {
            EJReportBlockController controller = blockControllers.next();
            controller.clearBlock();
        }

    }
}
