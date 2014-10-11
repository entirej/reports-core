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
package org.entirej.framework.report.service;

import java.util.List;
import java.util.Properties;

public class EJReportServiceGeneratorType
{
    private Class<?>                  _pojo;
    private String                    _tableName;
    private String                    _serviceName;
    private String                    _packageName;
    private String                    _queryStatement;
    private String                    _selectProcedureName;
    private List<EJReportTableColumn> _selectProcedureParameters;
    private final Properties          _properties = new Properties();

    public Class<?> getPojo()
    {
        return _pojo;
    }

    public void setPojo(Class<?> pojo)
    {
        _pojo = pojo;
    }

    public String getTableName()
    {
        return _tableName;
    }

    public void setTableName(String tableName)
    {
        _tableName = tableName;
    }

    public String getServiceName()
    {
        return _serviceName;
    }

    public void setServiceName(String serviceName)
    {
        _serviceName = serviceName;
    }

    public String getPackageName()
    {
        return _packageName;
    }

    public void setPackageName(String packageName)
    {
        _packageName = packageName;
    }

    public String getQueryStatement()
    {
        return _queryStatement;
    }

    public void setQueryStatement(String queryStatement)
    {
        _queryStatement = queryStatement;
    }

    public String getSelectProcedureName()
    {
        return _selectProcedureName;
    }

    public void setSelectProcedureName(String selectProcedureName)
    {
        _selectProcedureName = selectProcedureName;
    }

    public List<EJReportTableColumn> getSelectProcedureParameters()
    {
        return _selectProcedureParameters;
    }

    public void setSelectProcedureParameters(List<EJReportTableColumn> selectProcedureParameters)
    {
        this._selectProcedureParameters = selectProcedureParameters;
    }

    public String getProperty(String key, String defaultVlaue)
    {
        return _properties.getProperty(key, defaultVlaue);
    }

    public String getProperty(String key)
    {
        return _properties.getProperty(key);
    }

    public Object setProperty(String key, String vlaue)
    {
        return _properties.setProperty(key, vlaue);
    }
}
