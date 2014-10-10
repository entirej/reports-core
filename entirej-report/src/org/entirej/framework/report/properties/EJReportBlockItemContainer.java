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

public class EJReportBlockItemContainer
{
    private List<EJCoreReportItemProperties> _itemProperties;
    private EJCoreReportBlockProperties      _blockProperties;

    public EJReportBlockItemContainer(EJCoreReportBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _itemProperties = new ArrayList<EJCoreReportItemProperties>();
    }

    public void dispose()
    {
        if (_itemProperties != null)
        {
            _itemProperties.clear();
        }

        _blockProperties = null;
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public boolean containsItemProperty(String name)
    {
        Iterator<EJCoreReportItemProperties> iti = _itemProperties.iterator();

        while (iti.hasNext())
        {
            EJCoreReportItemProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public void addItemProperties(EJCoreReportItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(itemProperties);

        }
    }

    public void addItemProperties(int index, EJCoreReportItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(index, itemProperties);

        }
    }

    public List<EJCoreReportItemProperties> getAllItemProperties()
    {
        return _itemProperties;
    }

    public boolean contains(String itemName)
    {
        Iterator<EJCoreReportItemProperties> iti = _itemProperties.iterator();

        while (iti.hasNext())
        {
            EJCoreReportItemProperties item = iti.next();

            if (item.getName() != null && item.getName().equalsIgnoreCase(itemName))
            {
                return true;
            }

        }
        return false;
    }

    public EJCoreReportItemProperties getItemProperties(String itemName)
    {

        Iterator<EJCoreReportItemProperties> props = _itemProperties.iterator();

        while (props.hasNext())
        {
            EJCoreReportItemProperties item = props.next();

            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }

    public void removeItem(String itemName)
    {
        Iterator<EJCoreReportItemProperties> props = _itemProperties.iterator();

        while (props.hasNext())
        {
            EJCoreReportItemProperties item = props.next();

            if (item.getName().equalsIgnoreCase(itemName))
            {

                removeItem(item);

                break;
            }
        }
    }

    public void sync(List<EJCoreReportItemProperties> newItems)
    {

        List<EJCoreReportItemProperties> markedRemove = new ArrayList<EJCoreReportItemProperties>(_itemProperties);
        for (EJCoreReportItemProperties newItem : new ArrayList<EJCoreReportItemProperties>(newItems))
        {
            for (EJCoreReportItemProperties item : new ArrayList<EJCoreReportItemProperties>(markedRemove))
            {
                if (!item.isBlockServiceItem())
                {
                    markedRemove.remove(item);
                    continue;
                }

                if (newItem.getName() != null && newItem.getDataTypeClassName() != null && newItem.getName().equals(item.getName())
                        && newItem.getDataTypeClassName().equals(item.getDataTypeClassName()))
                {
                    markedRemove.remove(item);
                    newItems.remove(newItem);
                    break;
                }

            }
        }
        for (EJCoreReportItemProperties EJPluginReportItemProperties : markedRemove)
        {
            removeItem(EJPluginReportItemProperties);
        }
        for (EJCoreReportItemProperties EJPluginReportItemProperties : newItems)
        {

            addItemProperties(EJPluginReportItemProperties);
        }

    }

    public int getItemCount()
    {
        return _itemProperties.size();
    }

    public void removeItem(EJCoreReportItemProperties item)
    {
        // FIXME
        // EJPluginItemChanger.deleteItemOnForm(_blockProperties,
        // item.getName());
        _itemProperties.remove(item);
    }

}
