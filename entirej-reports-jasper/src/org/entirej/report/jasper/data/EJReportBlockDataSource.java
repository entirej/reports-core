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
import java.util.Map;
import java.util.WeakHashMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportItem;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.properties.EJReportVisualAttributeProperties;


public class EJReportBlockDataSource implements JRDataSource, Serializable,EjReportBlockItemVAContext
{

    private final EJReportBlock block;
    private int           index = -1;
    private Map<String,Object> fieldCache = new WeakHashMap<String, Object>();
    private Map<String,EJReportItem> itemCache = new WeakHashMap<String, EJReportItem>();

    public EJReportBlockDataSource(EJReportBlock block)
    {
        this.block = block;
        
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException
    {

        if("_EJ_VA_CONTEXT".equals(field.getName()))
        {
            return this;
        }
        
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
        
       
        String name = field.getName();
        
        if(fieldCache.containsKey(name))
        {
            return fieldCache.get(name);
        }
        
        if(name.contains("."))
        {
            String blockName = name.substring(0, name.indexOf('.'));
            String itemName = name.substring(name.indexOf('.') + 1);
            if(blockName.equals(block.getName()))
            {
                EJReportRecord record = block.getFocusedRecord();
                
                Object value = record.getValue(itemName);
                fieldCache.put(name, value);
                return value;
            }
            else
            {
                EJReportBlock otherBlock = block.getReport().getBlock(blockName);
                if(otherBlock!=null)
                {
                    EJReportRecord focusedRecord = otherBlock.getFocusedRecord();
                    if(focusedRecord!=null){
                        Object value = focusedRecord.getValue(itemName);
                        fieldCache.put(name, value);
                        return value;
                    }
                                
                }
            }
            
        }
        else
        {
            EJReportRecord record = block.getFocusedRecord();
            Object value = record.getValue(name);
            fieldCache.put(name, value);
            return value;
        }
            
        return null;
    }

    @Override
    public boolean next() throws JRException
    {
        fieldCache.clear();
        itemCache.clear();
        index++;
      
        boolean hasRecord = index < block.getBlockRecordCount();
        if(hasRecord)
        {
            block.navigateToNextRecord();
        }
        return hasRecord;
    }

    @Override
    public boolean isActive(String item, String vaName)
    {
        String name = item;
       
        if(itemCache.containsKey(item))
        {
            EJReportItem reportItem = itemCache.get(item);
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            return visualAttribute!=null && visualAttribute.getName().equals(vaName);
        }
        
        if(name.contains("."))
        {
            String blockName = name.substring(0, name.indexOf('.'));
            String itemName = name.substring(name.indexOf('.') + 1);
            if(blockName.equals(block.getName()))
            {
                EJReportRecord record = block.getFocusedRecord();
                
              
                EJReportItem reportItem = record.getItem(itemName);
                itemCache.put(item, reportItem);
                EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
                return visualAttribute!=null && visualAttribute.getName().equals(vaName);
            }
            else
            {
                EJReportBlock otherBlock = block.getReport().getBlock(blockName);
                if(otherBlock!=null)
                {
                    EJReportRecord focusedRecord = otherBlock.getFocusedRecord();
                    if(focusedRecord!=null){
                        EJReportItem reportItem = focusedRecord.getItem(itemName);
                        itemCache.put(item, reportItem);
                        EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
                        return visualAttribute!=null && visualAttribute.getName().equals(vaName);
                    }
                                
                }
            }
            
        }
        else
        {
            EJReportRecord record = block.getFocusedRecord();
            EJReportItem reportItem = record.getItem(name);
            itemCache.put(item, reportItem);
            EJReportVisualAttributeProperties visualAttribute = reportItem.getVisualAttribute();
            return visualAttribute!=null && visualAttribute.getName().equals(vaName);
        }
        return false;
    }

}
