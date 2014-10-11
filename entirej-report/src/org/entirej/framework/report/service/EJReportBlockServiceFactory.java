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
package org.entirej.framework.report.service;

import java.io.Serializable;

import org.entirej.framework.report.EJReportMessage;
import org.entirej.framework.report.EJReportRuntimeException;

public class EJReportBlockServiceFactory implements Serializable
{
    public EJReportBlockServiceFactory()
    {
    }

    public EJReportBlockService<?> createBlockService(String serviceClassName)
    {
        if (serviceClassName == null || serviceClassName.trim().length() == 0)
        {
            return null;
        }

        try
        {
            Class<?> serviceClass = Class.forName(serviceClassName);
            Object service = serviceClass.newInstance();

            if (service != null && service instanceof EJReportBlockService<?>)
            {
                return (EJReportBlockService<?>) service;
            }
            else
            {
                return null;
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJReportRuntimeException(new EJReportMessage("Unable to find service class: " + serviceClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new EJReportRuntimeException(new EJReportMessage("Unable to instanciate service class: " + serviceClassName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJReportRuntimeException(new EJReportMessage("Illegal access exception when trying to access service class: " + serviceClassName), e);
        }
    }
}
