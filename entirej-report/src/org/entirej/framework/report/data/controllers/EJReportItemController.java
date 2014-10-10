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

import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.internal.EJInternalReportBlock;
import org.entirej.framework.report.properties.EJCoreReportItemProperties;

public class EJReportItemController implements Comparable<EJReportItemController>, Serializable
{
    private EJReportBlockController    _blockController;
    private EJReportFrameworkManager   _frameworkManager;
    private EJCoreReportItemProperties _itemProperties;

    public EJReportItemController(EJReportBlockController blockController, EJCoreReportItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
    }

    public EJInternalReport getReport()
    {
        return _blockController.getBlock().getForm();
    }

    public EJInternalReportBlock getBlock()
    {
        return _blockController.getBlock();
    }

    /**
     * Returns the controller responsible for the form
     * 
     * @return The form controller
     */
    public EJReportController getReportController()
    {
        return _blockController.getFormController();
    }

    public EJReportBlockController getBlockController()
    {
        return _blockController;
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    public EJCoreReportItemProperties getProperties()
    {
        return _itemProperties;
    }

    public int compareTo(EJReportItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }

        return _itemProperties.getName().toUpperCase().compareTo(controller.getProperties().getName().toUpperCase());

    }

}
