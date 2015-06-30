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
package org.entirej.framework.report.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.entirej.framework.report.data.controllers.EJReportBlockController;
import org.entirej.framework.report.properties.EJCoreReportBlockProperties;

public class EJReportDataBlock implements Serializable
{
    private String            _name;
    private ArrayList<Object> _blockRecords;

    public EJReportDataBlock(EJCoreReportBlockProperties blockProperties)
    {
        _blockRecords = new ArrayList<Object>();
        _name = blockProperties.getName();
    }

    public String getName()
    {
        return _name;
    }

    /**
     * Indicates if the given record is part of this blocks records
     * 
     * @param record
     *            The record to check for
     * @return <code>true</code> if the record exists otherwise
     *         <code>false</code>
     */
    public boolean containsRecord(EJReportDataRecord record)
    {
        return _blockRecords.contains(record);
    }

    /**
     * Clears all the blocks records and returns its dirty state, if it was
     * changed, to not dirty.
     * 
     * 
     */
    public void clearBlock()
    {
        _blockRecords.clear();
    }

    /**
     * Return the number of records within this block
     * 
     * @return The number of records
     */
    public int getBlockRecordCount()
    {
        return _blockRecords.size();
    }

    /**
     * Adds a a given record to this block. The record must be the correct type
     * for this block otherwise things will not work correctly. No check is made
     * upon the validity of the record as this will have a bad effect on
     * performance.
     * 
     * @param pRecords
     *            The records to add
     */
    public void addRecord(EJReportDataRecord queriedRecord)
    {
        
        _blockRecords.add(queriedRecord);
    }
    public void addRecords(List<Object> reObjects)
    {
        
        _blockRecords.addAll(reObjects);
    }

 

    /**
     * Returns the record number of the given record or <code>-1</code> if the
     * given record does not exist within the block
     * 
     * @param record
     *            The record number of this record will be returned
     * @return The record number of the specified record or <code>-1</code> if
     *         the record does not exist within the block
     */
    public int getRecordNumber(EJReportDataRecord record)
    {
        if (record == null)
        {
            return -1;
        }

        return _blockRecords.indexOf(record);
    }

    public void removeTop()
    {
        if (_blockRecords.size() > 0)
            _blockRecords.remove(0);
    }

    /**
     * Returns the <code>DataRecord</code> for the record number given
     * <p>
     * The lowest allowable record number is 0 and the highest is the amount of
     * records within this block -1
     * 
     * @param recordNumber
     *            The record number of the required record
     * @return The record at the given position
     * @throws ArrayOutOfBoundsException
     */
    public EJReportDataRecord getTopRecord(EJReportBlockController controller)
    {
        if (_blockRecords.size() <= 0)
        {
            return null;
        }

        Object object = _blockRecords.get(0);

        if (object instanceof EJReportDataRecord)
            return (EJReportDataRecord) _blockRecords.get(0);
        else
        {
            EJReportDataRecord ejReportDataRecord = new EJReportDataRecord(controller.getReportController(), controller.getBlock(), object);
            _blockRecords.set(0, ejReportDataRecord);
            return ejReportDataRecord;
        }

    }

}
