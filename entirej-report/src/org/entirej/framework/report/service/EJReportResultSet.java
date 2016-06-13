package org.entirej.framework.report.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.entirej.framework.report.EJReportManagedFrameworkConnection;
import org.entirej.framework.report.EJReportPojoHelper;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.data.EJReportDataRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportResultSet<T>
{
    private static Logger                      logger      = LoggerFactory.getLogger(EJReportResultSet.class);

    private EJReportPojoHelper                 _pojoHelper = new EJReportPojoHelper();
    private EJReportManagedFrameworkConnection _connection;
    private ResultSet                          _resultSet;
    private Class<T>                           _pojoType;

    public EJReportResultSet(EJReportManagedFrameworkConnection con, ResultSet resultSet, Class<T> pojoType)
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

                }
            }
            catch (SQLException e)
            {
            }
        }
    }

}
