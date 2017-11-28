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

public class EJReportStoredProcedureStatementParameter extends EJReportStatementParameter
{
    public EJReportStoredProcedureStatementParameter(EJReportParameterType paramType)
    {
        this(null, paramType, null);
    }

    public EJReportStoredProcedureStatementParameter(Object value)
    {
        this(null, EJReportParameterType.IN, value);
    }

    public EJReportStoredProcedureStatementParameter(EJReportParameterType paramType, Object value)
    {
        this(null, paramType, value);
    }

    public EJReportStoredProcedureStatementParameter(Class<?> type)
    {
        this(type, EJReportParameterType.IN, null);
    }

    public EJReportStoredProcedureStatementParameter(Class<?> type, EJReportParameterType paramType)
    {
        this(type, paramType, null);
    }

    public EJReportStoredProcedureStatementParameter(Class<?> type, Object value)
    {
        this(type, EJReportParameterType.IN, value);
    }

    public EJReportStoredProcedureStatementParameter(Class<?> type, EJReportParameterType paramType, Object value)
    {
        super(null, type, paramType, value);
    }
}
