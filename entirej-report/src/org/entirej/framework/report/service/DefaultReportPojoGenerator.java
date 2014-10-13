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

import org.entirej.framework.report.EJReportFieldName;

public class DefaultReportPojoGenerator implements EJReportPojoContentGenerator
{

    protected static final String EJ_FIELD_ANNOTATION = "@EJReportFieldName";
    protected final static String PACKAGE             = "package ";
    protected final static String IMPORT              = "import ";
    protected final static String PUBLIC              = "public ";
    protected final static String PRIVATE             = "private ";
    protected final static String CLASS               = "class ";
    protected final static String NEW_LINE            = "\n";
    protected final static String SEMICOLON           = ";";
    protected final static String COMMA               = ",";
    protected final static String OC_BRACKETS         = "{";
    protected final static String O_BRACKETS          = "(";
    protected final static String CC_BRACKETS         = "}";
    protected final static String C_BRACKETS          = ")";
    protected final static String QUOTATION           = "\"";
    protected final static String EMPTY               = " ";
    protected final static String UNDERSCORE          = "_";
    protected final static String VOID                = "void ";
    protected final static String RETURN              = "return ";
    protected final static String SET                 = " set";
    protected final static String GET                 = " get";
    protected final static String EQUALS              = " = ";

    protected List<String> getAdditionalImports(EJReportPojoGeneratorType type)
    {
        return Collections.<String> emptyList();
    }

    protected String toPropertyName(String columnName)
    {
        if (columnName != null && !columnName.contains("_"))
        {
            if (columnName.toUpperCase().equals(columnName))
            {
                return columnName.toLowerCase();
            }
            return columnName;
        }
        int n = columnName.length();
        StringBuilder fieldName = new StringBuilder(n);
        for (int i = 0, flag = 0; i < n; i++)
        {
            char c = columnName.charAt(i);
            if (c == '_')
            {
                flag = 1;
                continue;
            }
            fieldName.append(flag == 0 ? Character.toLowerCase(c) : Character.toUpperCase(c));
            flag = 0;
        }
        return fieldName.toString();
    }

    private List<String> getSystemImports(EJReportPojoGeneratorType type)
    {
        List<String> system = new ArrayList<String>();
        system.add(EJReportFieldName.class.getName());

        return system;
    }

    public String generateContent(EJReportPojoGeneratorType type)
    {

        Set<String> imports = new TreeSet<String>();
        // read system and additional imports
        imports.addAll(getSystemImports(type));
        imports.addAll(getAdditionalImports(type));

        // read imports for columns
        for (EJReportTableColumn param : type.getColumns())
        {
            if (param.getParameterType() != null)
            {
                continue;
            }

            String pack = null;
            if (param.getDatatypeName().contains("."))
            {
                pack = param.getDatatypeName().substring(0, param.getDatatypeName().lastIndexOf("."));
            }

            // If the type is not in java.lang, then add the import list
            if (pack != null && (!"java.lang".equals(pack)))
            {
                imports.add(param.getDatatypeName());
            }

        }
        StringBuilder fileBuilder = new StringBuilder();

        // build package name
        fileBuilder.append(PACKAGE).append(type.getPackageName()).append(SEMICOLON);
        fileBuilder.append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
        // add imports
        for (String clazz : imports)
        {
            fileBuilder.append(IMPORT).append(clazz).append(SEMICOLON).append(NEW_LINE);
        }
        // build class
        fileBuilder.append(NEW_LINE).append(PUBLIC).append(CLASS).append(type.getClassName()).append(OC_BRACKETS).append(NEW_LINE);

        // build variables for columns
        fileBuilder.append(buildVariablesAndMethods(type));

        // close class file
        fileBuilder.append(NEW_LINE).append(CC_BRACKETS);

        return fileBuilder.toString();
    }

    protected String buildVariablesAndMethods(EJReportPojoGeneratorType type)
    {
        StringBuilder paramaterBuilder = new StringBuilder();
        StringBuilder methodBuilder = new StringBuilder();
        methodBuilder.append(NEW_LINE);
        ArrayList<String> propertyNames = new ArrayList<String>();
        for (EJReportTableColumn param : type.getColumns())
        {
            if (param.getParameterType() != null)
            {
                continue;
            }

            String typeName = null;
            if (param.getDatatypeName().contains("."))
            {
                typeName = param.getDatatypeName().substring(param.getDatatypeName().lastIndexOf(".") + 1);
            }
            else
            {
                typeName = param.getDatatypeName();
            }

            String propertyName = toPropertyName(param.getName());
            paramaterBuilder.append(PRIVATE).append(typeName).append(" ").append(UNDERSCORE).append(propertyName).append(SEMICOLON).append(NEW_LINE);

            propertyNames.add(propertyName);
            methodBuilder.append(getMethods(typeName, param.getName()));
        }

        paramaterBuilder.append(methodBuilder.toString());

        return paramaterBuilder.toString();
    }

    private String getMethods(String dataTypeName, String propertyName1)
    {
        String propertyName = toPropertyName(propertyName1);
        StringBuilder methodBuilder = new StringBuilder();

        // Add the annotation
        StringBuilder annotationString = new StringBuilder();
        annotationString.append(EJ_FIELD_ANNOTATION).append(O_BRACKETS).append(QUOTATION).append(propertyName1).append(QUOTATION).append(C_BRACKETS)
                .append(NEW_LINE);

        methodBuilder.append(annotationString.toString());
        // Add the getter method
        methodBuilder.append(PUBLIC).append(dataTypeName).append(GET);
        methodBuilder.append(propertyName.substring(0, 1).toUpperCase());
        methodBuilder.append(propertyName.substring(1)).append(O_BRACKETS).append(C_BRACKETS).append(NEW_LINE).append(OC_BRACKETS).append(NEW_LINE);
        methodBuilder.append("return ").append(UNDERSCORE).append(propertyName).append(SEMICOLON).append(NEW_LINE).append(CC_BRACKETS).append(NEW_LINE)
                .append(NEW_LINE);

        // Add the annotation
        methodBuilder.append(annotationString.toString());
        // Now add the setter method
        methodBuilder.append(PUBLIC);
        methodBuilder.append(VOID).append(SET);
        methodBuilder.append(propertyName.substring(0, 1).toUpperCase());
        methodBuilder.append(propertyName.substring(1)).append(O_BRACKETS);
        methodBuilder.append(dataTypeName);
        methodBuilder.append(EMPTY).append(propertyName).append(C_BRACKETS).append(NEW_LINE);
        methodBuilder.append(OC_BRACKETS).append(NEW_LINE);
        methodBuilder.append(UNDERSCORE).append(propertyName).append(EQUALS).append(propertyName).append(SEMICOLON).append(NEW_LINE).append(CC_BRACKETS)
                .append(NEW_LINE).append(NEW_LINE);

        return methodBuilder.toString();
    }

}
