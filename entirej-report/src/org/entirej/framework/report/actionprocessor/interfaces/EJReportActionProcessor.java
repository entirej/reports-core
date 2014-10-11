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
package org.entirej.framework.report.actionprocessor.interfaces;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportActionProcessorException;

public interface EJReportActionProcessor extends EJReportBlockActionProcessor
{

    /**
     * Called when a user selects a new report to be opened. This will be called
     * before displaying the report to the user
     * <p>
     * 
     * @param report
     *            The report that is opening
     */
    public void newReportInstance(EJReport report) throws EJReportActionProcessorException;

    /**
     * Called whenever the user navigates to a different block within the report
     * 
     * @param report
     *            The report from which this method is called
     * @param blockName
     *            The name of the block that gained focus
     */
    public void newBlockInstance(EJReport report, String blockName) throws EJReportActionProcessorException;
}
