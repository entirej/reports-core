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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.entirej.framework.report.EJReportPojoHelper;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.interfaces.EJReportFrameworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportResultSet<T>
{
    private static Logger               logger      = LoggerFactory.getLogger(EJReportResultSet.class);

    private EJReportPojoHelper          _pojoHelper = new EJReportPojoHelper();
    private EJReportFrameworkConnection _connection;
    private ResultSet                   _resultSet;
    private Class<T>                    _pojoType;

    public EJReportResultSet(EJReportFrameworkConnection con, ResultSet resultSet, Class<T> pojoType)
    {
        _connection = con;
        _resultSet = resultSet;
        _pojoType = pojoType;
    }

    public T getNext()
    {
        try
        {
            ResultSetMetaData metaData = _resultSet.getMetaData();

            if (_resultSet.next())
            {
                T result = _pojoType.newInstance();

                for (int i = 1; i <= metaData.getColumnCount(); i++)
                {
                    _pojoHelper.setFieldValue(metaData.getColumnLabel(i), result, _resultSet.getObject(i));
                }

                return result;
            }
            else
            {
                close();
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new EJReportRuntimeException("Error retrieving next:", e);
        }
        catch (InstantiationException e)
        {
            throw new EJReportRuntimeException("Error retrieving next:", e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJReportRuntimeException("Error retrieving next:", e);
        }
    }

    public void close()
    {
        try
        {
            if (!(_resultSet == null && _resultSet.isClosed()))
            {
                _resultSet.close();
            }

            if (!(_connection == null && ((Connection) _connection.getConnectionObject()).isClosed()))
            {
                _connection.close();
            }
        }
        catch (SQLException e)
        {
            logger.info("Error Executing Query", e);
            e.printStackTrace();
            try
            {
                if (!(_resultSet == null && _resultSet.isClosed()))
                {
                    _resultSet.close();
                }

                if (!(_connection == null && ((Connection) _connection).isClosed()))
                {
                    _connection.close();
                }
            }
            catch (SQLException e2)
            {
            }
        }
        finally
        {
            try
            {
                if (!(_resultSet == null && _resultSet.isClosed()))
                {
                    _resultSet.close();
                }

                if (!(_connection == null && ((Connection) _connection).isClosed()))
                {
                    _connection.close();
                }
            }
            catch (SQLException e)
            {
            }
        }
    }

}
