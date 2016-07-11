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

import org.entirej.framework.report.data.EJReportDataRecord;
import org.entirej.framework.report.service.EJReportQueryCriteria;

public interface EJReportBlockDataHandler
{
    /**
     * Sets the query criteria to be used when querying this blocks data service
     * <p>
     * Query criteria may be used to re-query a block or when deferred querying
     * is being made
     * 
     * @param queryCriteria
     *            The query criteria to set
     */
    public void setQueryCriteria(EJReportQueryCriteria queryCriteria);
    
    /**
     * Returns the query criteria that has been set within this block
     * 
     * @return The blocks saved query criteria
     */
    public EJReportQueryCriteria getQueryCriteria();
    
    public void executeQuery(EJReportQueryCriteria queryCriteria);
    /**
     * Clears this controllers underlying data block
     * <p>
     * Only this controllers underlying data block will be cleared, if the block
     * is master in a master-detail relationship then its detail blocks will be
     * left untouched. To clear all detail blocks use the
     * {@link EJReportBlockController#clearAllDetailRelations()}
     * 
     * 
     * @see EJReportBlockController#clearAllDetailRelations()
     */
    public void clearBlock();
    /**
     * Returns the blocks current record
     * <p>
     * 
     * @return The record upon which the report has retrieved for this block
     */
    public EJReportDataRecord getCurrentRecord();

    /**
     * Navigates to the next record and returns it
     * 
     * @return the next record or <code>null</code> if there are no more records
     */
    public EJReportDataRecord getNextRecord();
    
    
    public void reset();

    public void addRecord(EJReportDataRecord ejReportDataRecord);
    
}
