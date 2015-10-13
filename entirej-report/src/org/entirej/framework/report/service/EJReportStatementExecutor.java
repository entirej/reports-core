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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportPojoHelper;
import org.entirej.framework.report.EJReportRuntimeException;
import org.entirej.framework.report.interfaces.EJReportFrameworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJReportStatementExecutor implements Serializable
{
    final Logger logger = LoggerFactory.getLogger(EJReportStatementExecutor.class);

    public <T> List<T> executeQuery(Class<T> pojoType, EJReport report, String selectStatement, EJReportQueryCriteria queryCriteria)
    {
        return executeQuery(pojoType, report.getConnection(), selectStatement, queryCriteria);
    }

    public <T> List<T> executeQuery(Class<T> pojoType, EJReportFrameworkConnection fwkConnection, String selectStatement, EJReportQueryCriteria queryCriteria)
    {
        logger.info("Executing query to return a list of {}\n{}", pojoType, selectStatement);

        StringBuffer stmt = new StringBuffer(selectStatement);

        ArrayList<EJReportStatementParameter> queryValues = new ArrayList<EJReportStatementParameter>();
        for (EJReportRestriction<?> restriction : queryCriteria.getAllRestrictions())
        {
            if (restriction.isServiceItemRestriction())
            {
                stmt.append(addWhere(stmt.toString(), EJReportExpressionBuilder.buildExpression(restriction, queryValues)));
            }
        }

        logger.info("Added the QueryCriteria expressions {}", stmt.toString());

        for (EJReportQuerySort sort : queryCriteria.getSorts())
        {
            stmt.append(addOrderBy(stmt.toString(), sort));
        }

        logger.info("Added the Order By expressions {}", stmt.toString());

        EJReportStatementParameter[] valuesArray = new EJReportStatementParameter[queryValues.size()];
        return executeQuery(pojoType, fwkConnection, stmt.toString(), queryCriteria, queryValues.toArray(valuesArray));
    }

    private <T> List<T> executeQuery(Class<T> pojoType, EJReportFrameworkConnection fwkConnection, String selectStatement, EJReportQueryCriteria queryCriteria,
            EJReportStatementParameter... parameters)
    {
        EJReportPojoHelper helper = new EJReportPojoHelper();

        ArrayList<T> results = new ArrayList<T>();
        PreparedStatement pstmt = null;
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null || !(conObj instanceof Connection))
            {
                throw new EJReportRuntimeException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }

            Connection connection = (Connection) conObj;

            // I can only add paging to a select if it has been set within the
            // query criteria. If not query criteria has been set, then no
            // paging
            // is possible
            if (queryCriteria != null)
            {
                pstmt = connection.prepareStatement((selectStatement));
            }
            else
            {
                pstmt = connection.prepareStatement(selectStatement);
            }

            int pos = 1;

            for (EJReportStatementParameter parameter : parameters)
            {
                logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                pstmt.setObject(pos++, parameter.getValue());
            }

            logger.info("Executing Query");
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData metaData = rset.getMetaData();
            logger.info("Query Executed");
            try
            {
                while (rset.next())
                {
                    T result = pojoType.newInstance();

                    for (int i = 1; i <= metaData.getColumnCount(); i++)
                    {
                        helper.setFieldValue(metaData.getColumnLabel(i), result, rset.getObject(i));
                    }
                    results.add(result);
                }

                logger.info("Query retrieved {} results", results.size());
            }
            catch (InstantiationException e)
            {
                throw new EJReportRuntimeException("Error creating pojo instance", e);
            }
            catch (IllegalAccessException e)
            {
                throw new EJReportRuntimeException("Error creating pojo instance", e);
            }
            return results;

        }
        catch (SQLException e)
        {
            logger.info("Error Executing Query", e);
            e.printStackTrace();
            try
            {
                pstmt.close();
            }
            catch (SQLException e2)
            {
            }
            fwkConnection.rollback();
            throw new EJReportRuntimeException("Error executing block query", e);
        }
        finally
        {
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException e)
            {
            }
            fwkConnection.close();
        }
    }

    public List<EJReportSelectResult> executeQuery(EJReport report, String selectStatement, EJReportQueryCriteria queryCriteria,
            EJReportStatementParameter... parameters)
    {
        if (report == null)
        {
            throw new NullPointerException("report passed to executeQuery cannot be null");
        }
        return executeQuery(report.getConnection(), selectStatement, queryCriteria, parameters);
    }

    public List<EJReportSelectResult> executeQuery(EJReport report, String selectStatement, EJReportStatementParameter... parameters)
    {
        if (report == null)
        {
            throw new NullPointerException("report passed to executeQuery cannot be null");
        }
        return executeQuery(report.getConnection(), selectStatement, null, parameters);
    }

    public List<EJReportSelectResult> executeQuery(EJReportFrameworkConnection fwkConnection, String selectStatement, EJReportStatementParameter... parameters)
    {
        return executeQuery(fwkConnection, selectStatement, null, parameters);
    }

    public List<EJReportSelectResult> executeQuery(EJReportFrameworkConnection fwkConnection, String selectStatement, EJReportQueryCriteria queryCriteria,
            EJReportStatementParameter... parameters)
    {
        if (fwkConnection == null)
        {
            throw new NullPointerException("No EJReportFrameworkConnection passed to EJStatementExecutor");
        }

        logger.info("Executing generic query {}", selectStatement);

        PreparedStatement pstmt = null;
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null || !(conObj instanceof Connection))
            {
                throw new EJReportRuntimeException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }
            ArrayList<EJReportStatementParameter> allParameters = new ArrayList<EJReportStatementParameter>(Arrays.asList(parameters));
            Connection connection = (Connection) conObj;
            if (queryCriteria != null)
            {
                StringBuffer stmt = new StringBuffer(selectStatement);

                for (EJReportRestriction<?> restriction : queryCriteria.getAllRestrictions())
                {
                    if (restriction.isServiceItemRestriction())
                    {
                        stmt.append(addWhere(stmt.toString(), EJReportExpressionBuilder.buildExpression(restriction, allParameters)));
                    }
                }

                logger.info("Added the QueryCriteria expressions {}", stmt.toString());

                for (EJReportQuerySort sort : queryCriteria.getSorts())
                {
                    stmt.append(addOrderBy(stmt.toString(), sort));
                }

                selectStatement = stmt.toString();
            }
            // I can only add paging to a select if it has been set within the
            // query criteria. If not query criteria has been set, then no
            // paging
            // is possible
            if (queryCriteria != null)
            {
                pstmt = connection.prepareStatement((selectStatement));
            }
            else
            {
                pstmt = connection.prepareStatement(selectStatement);
            }
            int pos = 1;

            for (EJReportStatementParameter parameter : allParameters)
            {
                logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                pstmt.setObject(pos++, parameter.getValue());
            }

            logger.info("Executing Query");
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData metaData = rset.getMetaData();
            logger.info("Query Executed");

            ArrayList<EJReportSelectResult> results = new ArrayList<EJReportSelectResult>();
            while (rset.next())
            {
                EJReportSelectResult result = new EJReportSelectResult();

                for (int i = 1; i <= metaData.getColumnCount(); i++)
                {
                    result.addItem(metaData.getColumnLabel(i), rset.getObject(i));
                }

                results.add(result);
            }

            logger.info("Query retrieved {} results", results.size());
            return results;

        }
        catch (SQLException e)
        {
            logger.info("Error executing query", e);
            e.printStackTrace();
            try
            {
                pstmt.close();
            }
            catch (SQLException e2)
            {
            }
            fwkConnection.rollback();
            throw new EJReportRuntimeException("Error executing query", e);
        }
        finally
        {
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException e)
            {
            }
            fwkConnection.close();
        }
    }

    private String addWhere(String statement, String whereClause)
    {
        if (containsWhere(statement))
        {
            return " AND " + whereClause;
        }
        else
        {
            return " WHERE " + whereClause;
        }
    }

    private String addOrderBy(String statement, EJReportQuerySort sort)
    {
        if (containsOrderBy(statement))
        {
            return " , " + sort.getSort() + " " + (sort.getType() == EJReportQuerySortType.ASCENDING ? "ASC" : "DESC");
        }
        else
        {
            return " ORDER BY " + sort.getSort() + " " + (sort.getType() == EJReportQuerySortType.ASCENDING ? "ASC" : "DESC");
        }
    }

    private boolean containsWhere(String statement)
    {
        int nestingLevel = 0;
        Pattern pattern = Pattern.compile("(\\()|(\\))|(\\s+where\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(" " + statement);
        while (matcher.find())
        {
            if (matcher.start(1) != -1)
            {
                nestingLevel++;
            }
            if (matcher.start(2) != -1)
            {
                nestingLevel--;
            }
            if (matcher.start(3) != -1 && nestingLevel == 0)
            {
                return true;
            }
        }

        return false;
    }

    private boolean containsOrderBy(String statement)
    {
        int nestingLevel = 0;
        Pattern pattern = Pattern.compile("(\\()|(\\))|(\\s+order\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(" " + statement);
        while (matcher.find())
        {
            if (matcher.start(1) != -1)
            {
                nestingLevel++;
            }
            if (matcher.start(2) != -1)
            {
                nestingLevel--;
            }
            if (matcher.start(3) != -1 && nestingLevel == 0)
            {
                return true;
            }
        }
        return false;
    }
}
