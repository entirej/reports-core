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
import java.util.HashMap;
import java.util.Iterator;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportManagedFrameworkConnection;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.actionprocessor.EJDefaultReportActionProcessor;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportActionProcessor;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportBlockActionProcessor;
import org.entirej.framework.report.enumerations.EJReportTableColumn;
import org.entirej.framework.report.enumerations.EJReportScreenSection;
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

    public void beforeReport(EJReport report)
    {
        logger.trace("START beforeReport. Report: {}", report.getName());
        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {
            _reportLevelActionProcessor.beforeReport(report);
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
        logger.trace("END beforeReport");
    }

    public void afterReport(EJReport report)
    {
        logger.trace("START afterReport. Report: {}", report.getName());
        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {
            _reportLevelActionProcessor.afterReport(report);
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
        logger.trace("END afterReport");
    }

    public void postQuery(EJReport report, EJReportRecord record)
    {
        logger.trace("START postQuery. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
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

    public boolean canShowBlock(EJReport report, String blockName)
    {
        logger.trace("START canShowBlock. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowBlock. Block: {}", blockName);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowBlock(report, blockName);
                logger.trace("Called block level canShowBlock");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowBlock");
                boolean canShowBlock = _reportLevelActionProcessor.canShowBlock(report, blockName);
                logger.trace("Called report level canShowBlock");
                return canShowBlock;
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
            logger.trace("END canShowBlock");
        }

    }

    public boolean canShowScreenItem(EJReport report, String blockName, String screenItem, EJReportScreenSection section)
    {
        logger.trace("START canShowScreenItem. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowBlock. Block: {}", blockName, screenItem, section);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowScreenItem(report, blockName, screenItem, section);
                logger.trace("Called block level canShowBlock");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowScreenItem");
                boolean canShowBlock = _reportLevelActionProcessor.canShowScreenItem(report, blockName, screenItem, section);
                logger.trace("Called report level canShowScreenItem");
                return canShowBlock;
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
            logger.trace("END canShowScreenItem");
        }

    }

    public boolean canShowScreenColumn(EJReport report, String blockName, String columnName)
    {
        logger.trace("START canShowScreenColumn. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowScreenColumn. Block: {}", blockName, columnName);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowScreenColumn(report, blockName, columnName);
                logger.trace("Called block level canShowScreenColumn");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowScreenColumn");
                boolean canShowBlock = _reportLevelActionProcessor.canShowScreenColumn(report, blockName, columnName);
                logger.trace("Called report level canShowScreenColumn");
                return canShowBlock;
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
            logger.trace("END canShowScreenColumn");
        }

    }
    
    public EJReportTableColumn getHiddenColumnLayout(EJReport report, String blockName, String columnName)
    {
        logger.trace("START getHiddenColumnLayout. Report: {}", report.getName());
        
        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {
            
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level getHiddenColumnLayout. Block: {}", blockName, columnName);
                EJReportTableColumn layout = _blockLevelActionProcessors.get(blockName).getHiddenColumnLayout(report, blockName, columnName);
                logger.trace("Called block level getHiddenColumnLayout");
                return layout;
            }
            else
            {
                logger.trace("Calling report level getHiddenColumnLayout");
                EJReportTableColumn layout = _reportLevelActionProcessor.getHiddenColumnLayout(report, blockName, columnName);
                logger.trace("Called report level getHiddenColumnLayout");
                return layout;
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
            logger.trace("END canShowScreenColumn");
        }
        
    }

    public boolean canShowBlockFooter(EJReport report, String blockName)
    {
        logger.trace("START canShowBlockFooter. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowBlockFooter. Block: {}", blockName);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowBlockFooter(report, blockName);
                logger.trace("Called block level canShowBlockFooter");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowBlockFooter");
                boolean canShowBlock = _reportLevelActionProcessor.canShowBlockFooter(report, blockName);
                logger.trace("Called report level canShowBlockFooter");
                return canShowBlock;
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
            logger.trace("END canShowBlockFooter");
        }

    }

    public boolean canShowBlockHeader(EJReport report, String blockName)
    {
        logger.trace("START canShowBlockHeader. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowBlockHeader. Block: {}", blockName);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowBlockHeader(report, blockName);
                logger.trace("Called block level canShowBlockHeader");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowBlockHeader");
                boolean canShowBlock = _reportLevelActionProcessor.canShowBlockHeader(report, blockName);
                logger.trace("Called report level canShowBlockHeader");
                return canShowBlock;
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
            logger.trace("END canShowBlockHeader");
        }

    }

    public boolean canShowScreenColumnSection(EJReport report, String blockName, String columnName, EJReportScreenSection section)
    {
        logger.trace("START canShowScreenColumnSection. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level canShowScreenColumnSection. Block: {}", blockName, columnName, section);
                boolean canShowBlock = _blockLevelActionProcessors.get(blockName).canShowScreenColumnSection(report, blockName, columnName, section);
                logger.trace("Called block level canShowScreenColumnSection");
                return canShowBlock;
            }
            else
            {
                logger.trace("Calling report level canShowScreenColumnSection");
                boolean canShowBlock = _reportLevelActionProcessor.canShowScreenColumnSection(report, blockName, columnName, section);
                logger.trace("Called report level canShowScreenColumnSection");
                return canShowBlock;
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
            logger.trace("END canShowScreenColumnSection");
        }

    }

    public void preBlockQuery(EJReport report, EJReportQueryCriteria queryCriteria)
    {
        logger.trace("START preQuery. Report: {}", report.getName());

        EJReportManagedFrameworkConnection connection = report.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    logger.trace("Calling block level preBlockQuery. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).preBlockQuery(report, queryCriteria);
                    logger.trace("Called block level preBlockQuery");
                }
                else
                {
                    logger.trace("Calling report level preBlockQuery");
                    _reportLevelActionProcessor.preBlockQuery(report, queryCriteria);
                    logger.trace("Called report level preBlockQuery");
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

}
