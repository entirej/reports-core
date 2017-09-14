/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.report.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.report.enumerations.EJReportScreenItemType;

public class EJReportScreenColumnSectionItemContainer implements EJCoreReportSreenItemContainer
{
    private List<EJCoreReportScreenItemProperties>    _itemProperties;
    private EJCoreReportBlockProperties               _blockProperties;
    private EJCoreReportScreenColumnSectionProperties _sectionProperties;

    public EJReportScreenColumnSectionItemContainer(EJCoreReportBlockProperties blockProperties, EJCoreReportScreenColumnSectionProperties sectionProperties)
    {
        _blockProperties = blockProperties;
        _sectionProperties = sectionProperties;
        _itemProperties = new ArrayList<EJCoreReportScreenItemProperties>();
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public EJCoreReportScreenColumnSectionProperties getSectionProperties()
    {
        return _sectionProperties;
    }

    public void addItemProperties(EJCoreReportScreenItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(itemProperties);

        }
    }

    public void addItemProperties(int index, EJCoreReportScreenItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(index, itemProperties);

        }
    }

    public List<EJCoreReportScreenItemProperties> getAllItemProperties()
    {
        return _itemProperties;
    }

    public boolean contains(String name)
    {
        Iterator<EJCoreReportScreenItemProperties> iti = _itemProperties.iterator();

        while (iti.hasNext())
        {
            EJCoreReportScreenItemProperties item = iti.next();

            if (item.getName() != null && item.getName().equalsIgnoreCase(name))
            {
                return true;
            }

        }
        return false;
    }

    public EJCoreReportScreenItemProperties getItemProperties(String name)
    {

        Iterator<EJCoreReportScreenItemProperties> props = _itemProperties.iterator();

        while (props.hasNext())
        {
            EJCoreReportScreenItemProperties item = props.next();

            if (item.getName().equalsIgnoreCase(name))
            {
                return item;
            }
        }
        return null;
    }

    public int getItemCount()
    {
        return _itemProperties.size();
    }

    public void removeItem(EJCoreReportScreenItemProperties item)
    {

        _itemProperties.remove(item);
    }

    public EJCoreReportScreenItemProperties createItem(EJReportScreenItemType type, String name, int index)
    {
        EJCoreReportScreenItemProperties itemProperties = null;
        switch (type)
        {
            case LABEL:
                itemProperties = new EJCoreReportScreenItemProperties.Label(_blockProperties);
                break;
            case TEXT:
                itemProperties = new EJCoreReportScreenItemProperties.Text(_blockProperties);
                break;
            case NUMBER:
                itemProperties = new EJCoreReportScreenItemProperties.Number(_blockProperties);
                break;
            case DATE:
                itemProperties = new EJCoreReportScreenItemProperties.Date(_blockProperties);
                break;
            case IMAGE:
                itemProperties = new EJCoreReportScreenItemProperties.Image(_blockProperties);
                break;
            case LINE:
                itemProperties = new EJCoreReportScreenItemProperties.Line(_blockProperties);
                break;
            case RECTANGLE:
                itemProperties = new EJCoreReportScreenItemProperties.Rectangle(_blockProperties);
                break;

            default:
                return null;
        }

        itemProperties.setName(name);

        if (index == -1)
        {
            addItemProperties(itemProperties);
        }
        else
        {
            addItemProperties(index, itemProperties);
        }

        return itemProperties;
    }

}
