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
import java.util.HashMap;
import java.util.Iterator;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.actionprocessor.EJDefaultReportActionProcessor;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportActionProcessor;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportBlockActionProcessor;
import org.entirej.framework.report.processorfactories.EJReportActionProcessorFactory;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.service.EJReportQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportActionController implements Serializable
{
    final Logger                                          logger = LoggerFactory.getLogger(EJReportActionController.class);

    private EJReportController                            _reportController;
    private EJReportActionProcessor                       _reportLevelActionProcessor;
    private HashMap<String, EJReportBlockActionProcessor> _blockLevelActionProcessors;

    public EJReportActionController(EJReportController reportController)
    {
        _reportController = reportController;
        _blockLevelActionProcessors = new HashMap<String, EJReportBlockActionProcessor>();

        loadActionProcessors();
    }

    private void loadActionProcessors()
    {
        if (_reportController.getProperties().getActionProcessorClassName() != null
                && _reportController.getProperties().getActionProcessorClassName().length() > 0)
        {
            _reportLevelActionProcessor = EJReportActionProcessorFactory.getInstance().getActionProcessor(_reportController.getProperties());
        }
        else
        {
            _reportLevelActionProcessor = new EJDefaultReportActionProcessor();
        }

        // Get all the block level action processors
        Iterator<EJCoreReportBlockProperties> allBlockProperties = _reportController.getProperties().getBlockContainer().getAllBlockProperties().iterator();
        while (allBlockProperties.hasNext())
        {
            EJCoreReportBlockProperties blockProperties = allBlockProperties.next();
            EJReportBlockActionProcessor processor = EJReportActionProcessorFactory.getInstance().getActionProcessor(blockProperties);
            if (processor != null)
            {
                _blockLevelActionProcessors.put(blockProperties.getName(), processor);
            }
        }

    }



    public void newReportInstance(EJReport report)
    {
        logger.trace("START newReportInstance. Report: {}", report.getName());
        EJManagedReportFrameworkConnection connection = report.getConnection();
        try
        {
            _reportLevelActionProcessor.newReportInstance(report);
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
            connection.close();
        }
        logger.trace("END newReportInstance");
    }

    public void postQuery(EJReport report, EJReportRecord record)
    {
        logger.trace("START postQuery. Report: {}", report.getName());

        EJManagedReportFrameworkConnection connection = report.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level postQuery. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).postQuery(report, record);
                logger.trace("Called block level postQuery");
            }
            else
            {
                logger.trace("Calling report level postQuery");
                _reportLevelActionProcessor.postQuery(report, record);
                logger.trace("Called report level postQuery");
            }

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
            connection.close();
        }
        logger.trace("END postQuery");
    }

    public void preQuery(EJReport report, EJReportQueryCriteria queryCriteria)
    {
        logger.trace("START preQuery. Report: {}", report.getName());

        EJManagedReportFrameworkConnection connection = report.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    logger.trace("Calling block level preQuery. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).preQuery(report, queryCriteria);
                    logger.trace("Called block level preQuery");
                }
                else
                {
                    logger.trace("Calling report level preQuery");
                    _reportLevelActionProcessor.preQuery(report, queryCriteria);
                    logger.trace("Called report level preQuery");
                }
            }
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
            connection.close();
        }
        logger.trace("END preQuery");
    }

    public void validateQueryCriteria(EJReport report, EJReportQueryCriteria queryCriteria)
    {
        logger.trace("START validateQueryCriteria. Report: {}", report.getName());

        EJManagedReportFrameworkConnection connection = report.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level validateQueryCiteria. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).validateQueryCriteria(report, queryCriteria);
                logger.trace("Called block level validateQueryCriteria");
            }
            else
            {
                logger.trace("Calling report level validateQueryCriteria");
                _reportLevelActionProcessor.validateQueryCriteria(report, queryCriteria);
                logger.trace("Called report level validateQueryCriteria");
            }

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
            connection.close();
        }
        logger.trace("END validateQueryCriteria");
    }

    public void postBlockQuery(EJReport report, EJReportBlock block)
    {
        logger.trace("START postBlockQuery. Report: {}, Block: {}", report.getName(), block.getName());
        EJManagedReportFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(block.getName()))
            {
                logger.trace("Calling block level postBlockQuery. Block: {}", block.getName());
                _blockLevelActionProcessors.get(block.getName()).postBlockQuery(report, block);
                logger.trace("Called block level postBlockQuery");
            }
            else
            {
                logger.trace("Calling report level postBlockQuery");
                _reportLevelActionProcessor.postBlockQuery(report, block);
                logger.trace("Called report level postBlockQuery");
            }

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
            connection.close();
        }
        logger.trace("END postBlockQuery");
    }
}
