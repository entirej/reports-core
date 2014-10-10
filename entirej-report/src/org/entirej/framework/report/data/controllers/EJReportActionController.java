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
import org.entirej.framework.report.service.EJQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportActionController implements Serializable
{
    final Logger                                          logger = LoggerFactory.getLogger(EJReportActionController.class);

    private EJReportController                            _formController;
    private EJReportActionProcessor                       _formLevelActionProcessor;
    private HashMap<String, EJReportBlockActionProcessor> _blockLevelActionProcessors;

    public EJReportActionController(EJReportController formController)
    {
        _formController = formController;
        _blockLevelActionProcessors = new HashMap<String, EJReportBlockActionProcessor>();

        loadActionProcessors();
    }

    private void loadActionProcessors()
    {
        if (_formController.getProperties().getActionProcessorClassName() != null && _formController.getProperties().getActionProcessorClassName().length() > 0)
        {
            _formLevelActionProcessor = EJReportActionProcessorFactory.getInstance().getActionProcessor(_formController.getProperties());
        }
        else
        {
            _formLevelActionProcessor = new EJDefaultReportActionProcessor();
        }

        // Get all the block level action processors
        Iterator<EJCoreReportBlockProperties> allBlockProperties = _formController.getProperties().getBlockContainer().getAllBlockProperties().iterator();
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

    public void newBlockInstance(EJReport form, String blockName)
    {
        logger.trace("START newBlockInstance. Form: {}, Block: {}", form.getName(), blockName);
        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.newBlockInstance(form, blockName);
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
        logger.trace("END newBlockInstance");
    }

    public void newFormInstance(EJReport form)
    {
        logger.trace("START newFormInstance. Form: {}", form.getName());
        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.newReportInstance(form);
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
        logger.trace("END newFormInstance");
    }

    public void postQuery(EJReport form, EJReportRecord record)
    {
        logger.trace("START postQuery. Form: {}", form.getName());

        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level postQuery. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).postQuery(form, record);
                logger.trace("Called block level postQuery");
            }
            else
            {
                logger.trace("Calling form level postQuery");
                _formLevelActionProcessor.postQuery(form, record);
                logger.trace("Called form level postQuery");
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

    public void preQuery(EJReport form, EJQueryCriteria queryCriteria)
    {
        logger.trace("START preQuery. Form: {}", form.getName());

        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    logger.trace("Calling block level preQuery. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).preQuery(form, queryCriteria);
                    logger.trace("Called block level preQuery");
                }
                else
                {
                    logger.trace("Calling form level preQuery");
                    _formLevelActionProcessor.preQuery(form, queryCriteria);
                    logger.trace("Called form level preQuery");
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

    public void newRecordInstance(EJReport form, EJReportRecord record)
    {
        logger.trace("START newRecordInstance. Form: {}", form.getName());

        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();

            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    logger.trace("Calling block level newRecordInstance. Block: {}", record.getBlockName());
                    _blockLevelActionProcessors.get(blockName).newRecordInstance(form, record);
                    logger.trace("Called block level newRecordInstance");
                }
                else
                {
                    logger.trace("Calling form level newRecordInstance");
                    _formLevelActionProcessor.newRecordInstance(form, record);
                    logger.trace("Called form level newRecordInstance");
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
        logger.trace("END newRecordInstance");
    }

    public void validateQueryCriteria(EJReport form, EJQueryCriteria queryCriteria)
    {
        logger.trace("START validateQueryCriteria. Form: {}", form.getName());

        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                logger.trace("Calling block level validateQueryCiteria. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).validateQueryCriteria(form, queryCriteria);
                logger.trace("Called block level validateQueryCriteria");
            }
            else
            {
                logger.trace("Calling form level validateQueryCriteria");
                _formLevelActionProcessor.validateQueryCriteria(form, queryCriteria);
                logger.trace("Called form level validateQueryCriteria");
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

    public void postBlockQuery(EJReport form, EJReportBlock block)
    {
        logger.trace("START postBlockQuery. Form: {}, Block: {}", form.getName(), block.getName());
        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(block.getName()))
            {
                logger.trace("Calling block level postBlockQuery. Block: {}", block.getName());
                _blockLevelActionProcessors.get(block.getName()).postBlockQuery(form, block);
                logger.trace("Called block level postBlockQuery");
            }
            else
            {
                logger.trace("Calling form level postBlockQuery");
                _formLevelActionProcessor.postBlockQuery(form, block);
                logger.trace("Called form level postBlockQuery");
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

    public void initialiseRecord(EJReport form, EJReportRecord record)
    {
        logger.trace("START initialiseRecord. Form: {}", form.getName());

        EJManagedReportFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();

            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    logger.trace("Calling block level initialiseRecord. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).initialiseRecord(form, record);
                    logger.trace("Called block level initialiseRecord");
                }
                else
                {
                    logger.trace("Calling form level initialiseRecord");
                    _formLevelActionProcessor.initialiseRecord(form, record);
                    logger.trace("Called form level initialiseRecord");
                }
            }
        }
        catch (Exception e)
        {
            connection.rollback();
            throw new EJReportRuntimeException(e);
        }
        finally
        {
            connection.close();
        }
        logger.trace("END initialiseRecord");
    }
}
