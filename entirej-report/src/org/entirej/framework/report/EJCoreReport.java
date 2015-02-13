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
package org.entirej.framework.report;

import org.entirej.framework.report.data.controllers.EJReportActionController;
import org.entirej.framework.report.interfaces.EJReportProperties;
import org.entirej.framework.report.internal.EJInternalReport;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;

public class EJCoreReport
{
    private EJInternalReport _report;

    public EJCoreReport(EJInternalReport report)
    {
        _report = report;
    }

    protected EJInternalReport getInternalReport()
    {
        return _report;
    }
    
    /**
     * Returns the action controller for this report
     * 
     * @return This reports {@link EJReportActionController}
     */
    public EJReportActionController getActionController()
    {
        return _report.getActionController();
    }
    
    /**
     * Returns the properties for of this report
     * 
     * @return This reports properties
     */
    public EJReportProperties getProperties()
    {
        return _report.getProperties();
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _report.getFrameworkManager();

    }
    
    /**
     * Returns the <code>VisualAttributeProperties</code> with the given name or
     * <code>null</code> if there is no visual attribute with the given name
     * 
     * @param vaName
     *            the name of the required <code>VisualAttribute</code>
     * 
     * @return The required <code>VisualAttributeProperties</code> or
     *         <code>null</code> if there was no Visual Attribute with the given
     *         name
     */
    public EJReportVisualAttributeProperties getVisualAttribute(String vaName)
    {
        return _report.getVisualAttribute(vaName);
    }
}
