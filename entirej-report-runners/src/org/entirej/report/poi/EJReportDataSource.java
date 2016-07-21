/*******************************************************************************
 * Copyright 2014 Mojave Innovations GmbH
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

package org.entirej.report.poi;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.enumerations.EJReportScreenSection;

public class EJReportDataSource implements  Serializable, EJReportActionContext
{

    private final EJReport report;

    private AtomicBoolean  firstRUn = new AtomicBoolean(true);

    public EJReportDataSource(EJReport report)
    {
        this.report = report;

    }

   
    public Object getFieldValue(String name) 
    {

        if (name.startsWith("EJRJ_BLOCK_DS_"))
        {
            
            String blockName = name.substring("EJRJ_BLOCK_DS_".length());
            return getBlockDataSource(blockName);

        }

        if ("_EJ_AP_CONTEXT".equals(name))
        {

            return this;
        }

        return null;
    }


    public EJReportBlockDataSource getBlockDataSource(String blockName)
    {
        
        EJReportBlock block;
        block = report.getBlock(blockName);

        if (block != null)
        {
            if (!block.isControlBlock())
            {
                block.executeQuery();
            }
            return new EJReportBlockDataSource(block);
        }
        return null;
    }

   
    public boolean next() 
    {
        return firstRUn.getAndSet(false);
    }

    @Override
    public boolean canShowBlock(String blockName)
    {
        return report.getActionController().canShowBlock(report, blockName);
    }

    @Override
    public boolean canShowScreenItem(String blockName, String screenItem, String section)
    {

        return report.getActionController().canShowScreenItem(report, blockName, screenItem, EJReportScreenSection.valueOf(section));
    }
}
