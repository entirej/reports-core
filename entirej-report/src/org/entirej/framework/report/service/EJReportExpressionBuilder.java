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
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class EJReportExpressionBuilder implements Serializable
{
    public static String buildExpression(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        String restrictionGen = null;
        switch (restriction.getType())
        {
            case EQUAL:
                restrictionGen = buildEquals(restriction, values);
                break;
            case EQUAL_IGNORE_CASE:
                restrictionGen = buildEqualsIgnoreCase(restriction, values);
                break;
            case BETWEEN:
                restrictionGen = buildBetween(restriction, values);
                break;
            case GREATER_THAN:
                restrictionGen = buildGreaterThan(restriction, values);
                break;
            case GREATER_THAN_EQUAL_TO:
                restrictionGen = buildGreaterThanOrEqualTo(restriction, values);
                break;
            case IN:
                restrictionGen = buildIn(restriction, values);
                break;
            case IS_NOT_NULL:
                restrictionGen = buildIsNotNull(restriction, values);
                break;
            case IS_NULL:
                restrictionGen = buildIsNull(restriction, values);
                break;
            case LESS_THAN:
                restrictionGen = buildLessThan(restriction, values);
                break;
            case LESS_THAN_EQUAL_TO:
                restrictionGen = buildLessThanOrEqualTo(restriction, values);
                break;
            case LIKE:
                restrictionGen = buildLike(restriction, values);
                break;
            case LIKE_IGNORE_CASE:
                restrictionGen = buildLikeIgnoreCase(restriction, values);
                break;
            case NOT_EQUAL:
                restrictionGen = buildNotEquals(restriction, values);
                break;
            case NOT_IN:
                restrictionGen = buildNotIn(restriction, values);
                break;
            case NOT_LIKE:
                restrictionGen = buildNotLike(restriction, values);
                break;
            case NOT_LIKE_IGNORE_CASE:
                restrictionGen = buildNotLikeIgnoreCase(restriction, values);
                break;
            case FREE_TEXT:
                restrictionGen = buildFreeText(restriction, values);
                break;
            default:
                restrictionGen = "";
        }
        List<Entry<EJReportRestrictionJoin, EJReportRestriction<?>>> subRestrictions = restriction.getSubRestrictions();
        if (subRestrictions != null && !subRestrictions.isEmpty())
        {

            StringBuilder group = new StringBuilder(" ( ");
            group.append(restrictionGen);

            for (Entry<EJReportRestrictionJoin, EJReportRestriction<?>> entry : subRestrictions)
            {
                group.append(" ");
                group.append(entry.getKey().name());
                group.append(" ");
                group.append(buildExpression(entry.getValue(), values));
            }
            group.append(" ) ");
            return group.toString();
        }

        return restrictionGen;
    }

    private static String buildEquals(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" = ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildEqualsIgnoreCase(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append("UPPER(");
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(") = UPPER(?) ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildNotEquals(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" != ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildIsNull(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" IS NULL");
        return str.toString();
    }

    private static String buildIsNotNull(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" IS NOT NULL");
        return str.toString();
    }

    private static String buildGreaterThan(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" > ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildGreaterThanOrEqualTo(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" >= ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildLessThan(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" < ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildLessThanOrEqualTo(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" <= ? ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildLike(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" LIKE ? ");
        values.add(new EJReportStatementParameter(restriction.getValue() == null ? restriction.getValue() : restriction.getValue() + "%"));
        return str.toString();
    }

    private static String buildFreeText(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {

        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" ");
        if (restriction.getValue() != null)
            str.append(restriction.getValue());
        return str.toString();
    }

    private static String buildNotLike(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(" NOT LIKE ? ");
        values.add(new EJReportStatementParameter(restriction.getValue() == null ? restriction.getValue() : restriction.getValue() + "%"));
        return str.toString();
    }

    private static String buildLikeIgnoreCase(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append("UPPER(");
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(") LIKE UPPER(?) ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildNotLikeIgnoreCase(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuilder str = new StringBuilder();
        str.append("UPPER(");
        str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        str.append(") NOT LIKE UPPER(?) ");
        values.add(new EJReportStatementParameter(restriction.getValue()));
        return str.toString();
    }

    private static String buildBetween(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        if (restriction.getBetweenHigherValue() != null && restriction.getBetweenLowerValue() != null)
        {
            StringBuilder str = new StringBuilder();
            str.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
            str.append(" BETWEEN ? AND ? ");
            values.add(new EJReportStatementParameter(restriction.getBetweenLowerValue()));
            values.add(new EJReportStatementParameter(restriction.getBetweenHigherValue()));
            return str.toString();
        }
        else if (restriction.getBetweenHigherValue() != null)
        {
            return buildLessThan(restriction, values);
        }
        else if (restriction.getBetweenLowerValue() != null)
        {
            return buildGreaterThan(restriction, values);
        }
        else
        {
            return "";
        }
    }

    private static String buildIn(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        buffer.append(" IN (");
        boolean first = true;
        for (Object value : restriction.getValueList())
        {
            if (first)
            {
                buffer.append("?");
                first = false;
            }
            else
            {
                buffer.append(",?");
            }
            values.add(new EJReportStatementParameter(value));
        }
        buffer.append(") ");
        return buffer.toString();
    }

    private static String buildNotIn(EJReportRestriction<?> restriction, ArrayList<EJReportStatementParameter> values)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(restriction.getAlias() == null ? restriction.getFieldName() : restriction.getAlias());
        buffer.append(" NOT IN (");
        boolean first = true;
        for (Object value : restriction.getValueList())
        {
            if (first)
            {
                buffer.append("?");
                first = false;
            }
            else
            {
                buffer.append(",?");
            }
            values.add(new EJReportStatementParameter(value));
        }
        buffer.append(") ");
        return buffer.toString();
    }
}
