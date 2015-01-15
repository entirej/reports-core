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

package org.entirej.report.jasper.data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportBlock;

public class EJReportDataSource implements JRDataSource, Serializable, EjReportActionContextContext
{

    private final EJReport report;

    private AtomicBoolean  firstRUn = new AtomicBoolean(true);

    public EJReportDataSource(EJReport report)
    {
        this.report = report;

    }

    @Override
    public Object getFieldValue(JRField field) throws JRException
    {

        if (field.getName().startsWith("EJRJ_BLOCK_DS_"))
        {
            EJReportBlock block = null;

            String blockName = field.getName().substring("EJRJ_BLOCK_DS_".length());
            block = report.getBlock(blockName);

            if (block != null)
            {
                if (!block.isControlBlock())
                {
                    block.executeQuery();
                }
                return new EJReportBlockDataSource(block);
            }

        }

        if ("_EJ_AP_CONTEXT".equals(field.getName()))
        {

            return this;
        }

        return null;
    }

    @Override
    public boolean next() throws JRException
    {
        return firstRUn.getAndSet(false);
    }

    @Override
    public boolean canShowBlock(String blockName)
    {
        return report.getActionController().canShowBlock(report, blockName);
    }

}
