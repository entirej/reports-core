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

import java.io.Serializable;

import org.entirej.framework.report.EJManagedReportFrameworkConnection;
import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportActionProcessorException;
import org.entirej.framework.report.EJReportBlock;
import org.entirej.framework.report.EJReportRecord;
import org.entirej.framework.report.service.EJReportQueryCriteria;

public interface EJReportBlockActionProcessor extends Serializable
{

    public enum SECTION
    {
        HEADER, DETAIL, FOOTER
    };

    /**
     * Called before a query is executed. The <code>IQueryCriteria</code> given,
     * holds the query criteria for this block
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @see EJManagedReportFrameworkConnection
     * @param report
     *            The report from which this method is called
     * @param queryCriteria
     *            The query criteria for this block
     */
    public void preBlockQuery(EJReport report, EJReportQueryCriteria queryCriteria) throws EJReportActionProcessorException;

    /**
     * Called for each record of a queried block. The <code>EJrRecord</code> is
     * the retrieved record. Setting values of this record, will set the value
     * of the blocks underlying data.
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @param report
     *            The report from which this method is called
     * @param record
     *            The record retrieved
     */
    public void postQuery(EJReport report, EJReportRecord record) throws EJReportActionProcessorException;

    public boolean canShowBlock(EJReport report, String blockName);

    public boolean canShowScreenItem(EJReport report, String blockName, String screenItem, SECTION section);

}
