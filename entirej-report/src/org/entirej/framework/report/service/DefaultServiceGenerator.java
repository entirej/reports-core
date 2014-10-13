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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.entirej.framework.report.EJReport;
import org.entirej.framework.report.EJReportPojoHelper;
import org.entirej.framework.report.EJReportRuntimeException;

public class DefaultServiceGenerator implements EJReportServiceContentGenerator
{

    protected final static String PACKAGE     = "package ";
    protected final static String IMPORT      = "import ";
    protected final static String PUBLIC      = "public ";
    protected final static String PRIVATE     = "private ";
    protected final static String CLASS       = "class ";
    protected final static String IMPLEMENTS  = "implements ";
    protected final static String NEW_LINE    = "\n";
    protected final static String SEMICOLON   = ";";
    protected final static String COMMA       = ",";
    protected final static String OC_BRACKETS = "{";
    protected final static String O_BRACKETS  = "(";
    protected final static String CC_BRACKETS = "}";
    protected final static String C_BRACKETS  = ")";
    protected final static String QUOTATION   = "\"";
    protected final static String EMPTY       = " ";
    protected final static String UNDERSCORE  = "_";
    protected final static String VOID        = "void ";
    protected final static String RETURN      = "return ";
    protected final static String FALSE       = " false ";
    protected final static String TRUE        = " true ";
    protected final static String SET         = " set";
    protected final static String GET         = " get";
    protected final static String EQUALS      = " = ";
    protected final static String OVERRIDE    = "@Override ";

    protected List<String> getAdditionalImports(EJReportServiceGeneratorType type)
    {
        return Collections.<String> emptyList();
    }

    private List<String> getSystemImports(EJReportServiceGeneratorType type)
    {
        List<String> system = new ArrayList<String>();
        system.add(EJReport.class.getName());
        system.add(EJReportRuntimeException.class.getName());
        system.add(EJReportBlockService.class.getName());
        system.add(EJReportQueryCriteria.class.getName());
        system.add(EJReportStatementCriteria.class.getName());
        system.add(EJReportRestrictions.class.getName());
        system.add(EJReportStatementExecutor.class.getName());
        system.add(EJReportStatementParameter.class.getName());
        system.add(ArrayList.class.getName());
        system.add(List.class.getName());
        system.add(type.getPojo().getName());

        return system;
    }

    @Override
    public String generateContent(EJReportServiceGeneratorType type)
    {
        try
        {
            String pojoName = type.getPojo().getSimpleName();

            StringBuilder fileBuilder = new StringBuilder();

            Set<String> imports = new TreeSet<String>();
            // read system and additional imports
            imports.addAll(getSystemImports(type));
            imports.addAll(getAdditionalImports(type));

            ArrayList<Class<?>> types = EJReportPojoHelper.getDataTypes(type.getPojo());

            for (Class<?> dataType : types)
            {
                if ((!dataType.getName().startsWith("java.lang")))
                {
                    imports.add(dataType.getName());
                }
            }

            // build package name
            fileBuilder.append(PACKAGE).append(type.getPackageName()).append(SEMICOLON);
            fileBuilder.append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
            // add imports
            for (String clazz : imports)
            {
                fileBuilder.append(IMPORT).append(clazz).append(SEMICOLON).append(NEW_LINE);
            }

            fileBuilder.append(NEW_LINE).append(PUBLIC).append(CLASS).append(type.getServiceName()).append(EMPTY).append(IMPLEMENTS).append("EJReportBlockService<")
                    .append(pojoName).append(">").append(NEW_LINE).append(OC_BRACKETS).append(NEW_LINE);

            String baseTableName = type.getTableName();

            boolean hasTable = !(baseTableName == null || baseTableName.trim().length() == 0);
            boolean hasQuery = !(type.getQueryStatement() == null || type.getQueryStatement().trim().length() == 0);

            if (hasTable || hasQuery)
            {
                fileBuilder.append(PRIVATE);
                fileBuilder.append("final EJReportStatementExecutor _statementExecutor").append(SEMICOLON).append(NEW_LINE);
                fileBuilder.append(PRIVATE);
                fileBuilder.append("String _selectStatement").append(EQUALS)
                        .append(hasQuery ? splitStatementToStrBuilder(type.getQueryStatement()) : buildSelectStatement(type)).append(SEMICOLON)
                        .append(NEW_LINE);

                fileBuilder.append(PUBLIC).append(type.getServiceName()).append(O_BRACKETS).append(C_BRACKETS).append(OC_BRACKETS).append(NEW_LINE);
                fileBuilder.append("_statementExecutor");
                fileBuilder.append(EQUALS);
                fileBuilder.append("new EJReportStatementExecutor").append(O_BRACKETS).append(C_BRACKETS).append(SEMICOLON).append(NEW_LINE);
                fileBuilder.append(CC_BRACKETS).append(NEW_LINE);

            }

            fileBuilder.append(OVERRIDE).append(NEW_LINE);
            fileBuilder.append(PUBLIC);

            fileBuilder.append("List<").append(pojoName).append("> executeQuery(EJReport report, EJReportQueryCriteria queryCriteria").append(C_BRACKETS)
                    .append(OC_BRACKETS).append(NEW_LINE);
            if (hasTable || hasQuery)
            {
                fileBuilder.append(RETURN).append("_statementExecutor.executeQuery(" + pojoName + ".class, report, _selectStatement, queryCriteria")
                        .append(C_BRACKETS).append(SEMICOLON).append(NEW_LINE);

            }
            else
            {

                fileBuilder.append(RETURN).append("new ArrayList<").append(pojoName).append(">").append(O_BRACKETS).append(C_BRACKETS).append(SEMICOLON);
            }

            fileBuilder.append(CC_BRACKETS).append(NEW_LINE);

            fileBuilder.append("\n}");

            return fileBuilder.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String buildSelectStatement(EJReportServiceGeneratorType generatorType)
    {
        String baseTableName = generatorType.getTableName();

        if (baseTableName == null || baseTableName.trim().length() == 0)
        {
            return "";
        }

        // Only create a statement if there has been no select statement defined
        // for the block
        StringBuffer selectStatementBuffer = new StringBuffer();
        selectStatementBuffer.append(QUOTATION);
        selectStatementBuffer.append("SELECT ");

        // Add select columns
        int col = 0;
        for (String fieldName : EJReportPojoHelper.getFieldNames(generatorType.getPojo()))
        {
            col++;
            if (col > 1)
            {
                selectStatementBuffer.append(COMMA);
            }
            selectStatementBuffer.append(fieldName);

        }
        selectStatementBuffer.append(" FROM ");
        selectStatementBuffer.append(baseTableName);
        selectStatementBuffer.append(QUOTATION);
        return selectStatementBuffer.toString();
    }

    private String splitStatementToStrBuilder(String stmt)
    {

        StringBuilder builder = new StringBuilder();

        builder.append("new StringBuilder()");

        String[] splits = stmt.split(NEW_LINE);

        for (String split : splits)
        {
            builder.append(".append(\"").append("\\n").append(split.replace('\n', ' ').trim()).append(" \")");
        }

        builder.append(".toString()");

        return builder.toString();

    }

}
