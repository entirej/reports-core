/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EJReportColumnContainer
{
    private List<EJCoreReportScreenColumnProperties> _columnProperties;
    private EJCoreReportBlockProperties        _blockProperties;

    public EJReportColumnContainer(EJCoreReportBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _columnProperties = new ArrayList<EJCoreReportScreenColumnProperties>();
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public void addColumnProperties(EJCoreReportScreenColumnProperties ColumnProperties)
    {
        if (ColumnProperties != null)
        {
            _columnProperties.add(ColumnProperties);

        }
    }

    public void addColumnProperties(int index, EJCoreReportScreenColumnProperties ColumnProperties)
    {
        if (ColumnProperties != null)
        {
            _columnProperties.add(index, ColumnProperties);

        }
    }

    public List<EJCoreReportScreenColumnProperties> getAllColumnProperties()
    {
        return _columnProperties;
    }

    public boolean contains(String name)
    {
        Iterator<EJCoreReportScreenColumnProperties> iti = _columnProperties.iterator();

        while (iti.hasNext())
        {
            EJCoreReportScreenColumnProperties Column = iti.next();

            if (Column.getName() != null && Column.getName().equalsIgnoreCase(name))
            {
                return true;
            }

        }
        return false;
    }

    public EJCoreReportScreenColumnProperties getColumnProperties(String name)
    {

        Iterator<EJCoreReportScreenColumnProperties> props = _columnProperties.iterator();

        while (props.hasNext())
        {
            EJCoreReportScreenColumnProperties Column = props.next();

            if (Column.getName().equalsIgnoreCase(name))
            {
                return Column;
            }
        }
        return null;
    }

    public int getColumnCount()
    {
        return _columnProperties.size();
    }

    public void removeColumn(EJCoreReportScreenColumnProperties column)
    {

        _columnProperties.remove(column);
    }

}
