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
package org.entirej.framework.report.service;

import java.io.Serializable;
import java.util.List;

import org.entirej.framework.report.EJReport;

public interface EJReportBlockService<E> extends Serializable
{
    /**
     * This service will query all entities that match the given query criteria
     * and return them as a <code>List</code>
     * <p>
     * Each entity within the list will made available within the EntireJ
     * Framework
     * <p>
     * The form passed is the form from which this method is called. The form
     * can be used to retrieve values that can be used within the query
     * <p>
     * The <code>{@link EJReportQueryCriteria}</code> contains the methods to
     * know how many records exist per page and what page should be returned. I
     * the user requests a page where no data is available then an empty list
     * will be returned
     * 
     * @param form
     *            The form from which this method is called
     * @param queryCriteria
     *            The query criteria to use for the query
     * 
     * @return A <code>List</code> of entity objects
     */
    public List<E> executeQuery(EJReport form, EJReportQueryCriteria queryCriteria);

}
