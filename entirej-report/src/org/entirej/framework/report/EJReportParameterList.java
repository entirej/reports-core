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
package org.entirej.framework.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.entirej.framework.report.data.controllers.EJReportParameter;

public class EJReportParameterList implements Serializable
{
    private HashMap<String, EJReportParameter> _parameterList;

    public EJReportParameterList()
    {
        _parameterList = new HashMap<String, EJReportParameter>();
    }

    public void addParameter(EJReportParameter parameter)
    {
        if (parameter != null)
        {
            _parameterList.put(parameter.getName(), parameter);
        }
    }

    public boolean contains(String name)
    {
        return _parameterList.containsKey(name);
    }

    public EJReportParameter getParameter(String name)
    {
        return _parameterList.get(name);
    }

    /**
     * Returns an immutable list of parameters contained within this list
     * <p>
     * If a new parameter is to be added, use the
     * {@link #addParameter(EJReportParameter)} method
     * 
     * @return An immutable list of parameters contained within this list
     */
    public Collection<EJReportParameter> getAllParameters()
    {
        return Collections.unmodifiableCollection(_parameterList.values());
    }

    /**
     * Returns an immutable list of parameter names contained within this list
     * <p>
     * If a new parameter is to be added, use the
     * {@link #addParameter(EJReportParameter)} method
     * 
     * @return An immutable list of parameter names contained within this list
     */
    public Collection<String> getAllParameterNames()
    {
        return Collections.unmodifiableCollection(_parameterList.keySet());
    }

}
