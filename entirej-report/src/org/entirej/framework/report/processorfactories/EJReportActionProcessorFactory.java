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
package org.entirej.framework.report.processorfactories;

import java.io.Serializable;
import java.util.HashMap;

import org.entirej.framework.report.EJReportActionProcessorException;
import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.EJReportMessageFactory;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportActionProcessor;
import org.entirej.framework.report.actionprocessor.interfaces.EJReportBlockActionProcessor;
import org.entirej.framework.report.enumerations.EJReportFrameworkMessage;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;

public class EJReportActionProcessorFactory implements Serializable
{
    private static EJReportActionProcessorFactory _instance;
    private HashMap<String, Class<?>>             _actionProcessors;

    static
    {
        _instance = new EJReportActionProcessorFactory();
    }

    private EJReportActionProcessorFactory()
    {
        _actionProcessors = new HashMap<String, Class<?>>();
    }

    /**
     * Returns a Singleton instance of this factory
     * 
     * @return The singleton instance of this factory
     */
    public static EJReportActionProcessorFactory getInstance()
    {
        return _instance;
    }

    /**
     * Retrieve the processor defined within the <code>BlockProperties</code>
     * specified
     * 
     * @param blockProperties
     *            The block properties containing the name of the action
     *            processor
     * @return The action processor specified within the given block properties
     *         or <code>null</code> if no action processor has been specified
     *         for the given block
     * @throws EJReportActionProcessorException
     */
    public EJReportBlockActionProcessor getActionProcessor(EJCoreReportBlockProperties blockProperties)
    {
        if (blockProperties == null)
        {
            return null;
        }

        String actionProcessorName = blockProperties.getActionProcessorClassName();

        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            return null;
        }

        if (_actionProcessors.containsKey(actionProcessorName))
        {
            return createNewBlockActionProcessorInstance(blockProperties.getFrameworkManager(), actionProcessorName);
        }
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            _actionProcessors.put(actionProcessorName, processorClass);

            return createNewBlockActionProcessorInstance(blockProperties.getFrameworkManager(), actionProcessorName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_BLOCK,
                    actionProcessorName, blockProperties.getName()));
        }
    }

    /**
     * Retrieve the processor defined within the <code>FormProperties</code>
     * specified
     * 
     * @param formProperties
     *            The form properties containing the name of the action
     *            processor
     * @return The action processor specified within the given form properties
     * @throws EJReportActionProcessorException
     */
    public EJReportActionProcessor getActionProcessor(EJCoreReportProperties formProperties)
    {
        if (formProperties == null)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(
                    EJReportFrameworkMessage.NULL_FORM_PROPERTIES_PASSED_TO_METHOD, "getActionProcessor"));
        }

        String actionProcessorName = formProperties.getActionProcessorClassName();

        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(
                    EJReportFrameworkMessage.NO_ACTION_PROCESSOR_DEFINED_FOR_FORM, formProperties.getName()));
        }

        if (_actionProcessors.containsKey(actionProcessorName))
        {
            return createNewFormActionProcessorInstance(formProperties.getFrameworkManager(), actionProcessorName);
        }
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            _actionProcessors.put(actionProcessorName, processorClass);

            return createNewFormActionProcessorInstance(formProperties.getFrameworkManager(), actionProcessorName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_FORM,
                    actionProcessorName, formProperties.getName()));
        }
    }

    /**
     * Creates a new <code>EJFormActionProcessor</code> from the class instance
     * stored within the cache
     * 
     * @param processorName
     *            The name of the processor
     * @return The new <code>EJFormActionProcessor</code> instance
     * @throws EJReportActionProcessorException
     */
    private EJReportActionProcessor createNewFormActionProcessorInstance(EJReportFrameworkManager frameworkManager, String processorName)
    {
        Class<?> processorClass = _actionProcessors.get(processorName);

        if (processorClass == null)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(
                    EJReportFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, processorName));
        }

        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJReportActionProcessor)
            {
                return (EJReportActionProcessor) processorObject;
            }
            else
            {
                throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME,
                        processorName, "EJFormActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR,
                    processorName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR,
                    processorName), e);
        }
    }

    /**
     * Creates a new <code>EJBlockActionProcessor</code> from the class instance
     * stored within the cache
     * 
     * @param processorName
     *            The name of the processor
     * @return The new <code>EJFormActionProcessor</code> instance
     * @throws EJReportActionProcessorException
     */
    private EJReportBlockActionProcessor createNewBlockActionProcessorInstance(EJReportFrameworkManager frameworkManager, String processorName)
    {
        Class<?> processorClass = _actionProcessors.get(processorName);

        if (processorClass == null)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(
                    EJReportFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, processorName));
        }

        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJReportBlockActionProcessor)
            {
                return (EJReportBlockActionProcessor) processorObject;
            }
            else
            {
                throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME,
                        processorName, "EJBlockActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR,
                    processorName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJReportRuntimeException(EJReportMessageFactory.getInstance().createMessage(EJReportFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR,
                    processorName), e);
        }
    }
}
