/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJCoreReportProperties;

/**
 * The <code>DataReport</code> is the actual class that will hold the reports
 * data. The <code>DataReport</code> contains <code>DataBlock</code>s which
 * contain the data per block. The data blocks contain <code>DataRecord</code>s
 * and these records hold </code>DataItems</code>.
 * <p>
 * If you would compare the data classes to a database then the report would be
 * the actual application the block would represent a database table, the data
 * records would represent the records of the table and the data items would
 * represent the actual table columns.
 */
public class EJReportData implements Serializable
{
    private EJCoreReportProperties             _reportProperties;
    private HashMap<String, EJReportDataBlock> _dataBlocks;

    /**
     * Creates an instance of a <code>DataReport</code> using the report
     * properties given.
     * 
     * @param reportProperties
     *            The properties that will be used to initialize this data
     *            report
     */
    public EJReportData(EJCoreReportProperties reportProperties)
    {
        _reportProperties = reportProperties;
        _dataBlocks = new HashMap<String, EJReportDataBlock>();

        initialiseDataReport(reportProperties);
    }

    public EJCoreReportProperties getProperties()
    {
        return _reportProperties;
    }

    /**
     * Clears the block of all records and returns the block to its initial
     * state. Any changes that have been made to the blocks records will be
     * lost.
     * 
     * @param blockName
     *            The name of the block to clear. If the name of the block
     *            doesn't exist then nothing will be done
     * @param clearChanges
     *            Indicates if the dirty records contained within this block
     *            should also be cleared
     */
    public void clearBlock(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return;
        }
        EJReportDataBlock block = getBlock(blockName);
        if (block != null)
        {
            block.clearBlock();
        }
    }

    /**
     * Initializes this report object using the properties within the given
     * <code>EJCoreReportProperties</code> object
     * 
     * @param reportProps
     *            The report properties object
     */
    private void initialiseDataReport(EJCoreReportProperties reportProperties)
    {
        for (EJCoreReportBlockProperties blockProperties : reportProperties.getBlockContainer().getAllBlockProperties())
        {
            EJReportDataBlock block = new EJReportDataBlock(blockProperties);
            _dataBlocks.put(blockProperties.getName(), block);
        }
    }

    /**
     * Retrieve a collection of this reports data blocks.
     * 
     * @return A <code>Collection</code> containing the list of blocks that this
     *         report contains.
     */
    public Collection<EJReportDataBlock> getAllBlocks()
    {
        return _dataBlocks.values();
    }

    /**
     * Return the block with the specified name, if there is no block with the
     * given name or the name is null, then <code><b>null</b></code> will be
     * returned
     * 
     * @param blockName
     *            The name of the required block
     * 
     * @return The required block or <code>null</code> if there is no block with
     *         the given name
     */
    public EJReportDataBlock getBlock(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return null;
        }
        if (_dataBlocks == null)
        {
            return null;
        }
        return (EJReportDataBlock) _dataBlocks.get(blockName);
    }
}
