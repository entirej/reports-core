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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class EJReportBlockDataSource implements JRDataSource, Serializable
{

    private final EJReportBlock block;
    private int           index = -1;

    public EJReportBlockDataSource(EJReportBlock block)
    {
        this.block = block;
        
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException
    {

        if (field.getName().startsWith("EJRJ_BLOCK_DS_"))
        {
            EJReportBlock subBlock = null;

            String blockName = field.getName().substring("EJRJ_BLOCK_DS_".length());
            subBlock = this.block.getReport().getBlock(blockName);

            if (subBlock != null)
            {
                if (!subBlock.isControlBlock())
                {
                    subBlock.executeQuery();
                }
                return new EJReportBlockDataSource(subBlock);
            }

        }
        
        List<EJReportRecord> blockRecords = new ArrayList<EJReportRecord>(block.getBlockRecords());
       
        String name = field.getName();
        
        if(name.contains("."))
        {
            String blockName = name.substring(0, name.indexOf('.'));
            String itemName = name.substring(name.indexOf('.') + 1);
            if(blockName.equals(block.getName()))
            {
                EJReportRecord record = blockRecords.get(index);
                return record.getValue(itemName);
            }
        }
        else
        {
            EJReportRecord record = blockRecords.get(index);
            return record.getValue(name);
        }
            
        return null;
    }

    @Override
    public boolean next() throws JRException
    {
        index++;
        Collection<EJReportRecord> blockRecords = block.getBlockRecords();
        return (index < blockRecords.size());
    }

}
