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
import java.util.Collection;
import java.util.HashMap;

public class EJReportSelectResult implements Serializable
{
    private HashMap<String, EJReportSelectResultItem> _items;

    public EJReportSelectResult()
    {
        _items = new HashMap<String, EJReportSelectResultItem>();
    }

    void addItem(String name, Object value)
    {
        if (name != null && (!_items.containsKey(name)))
        {
            _items.put(name, new EJReportSelectResultItem(name, value));
        }
    }

    public Collection<String> getNames()
    {
        return _items.keySet();
    }

    public Object getItemValue(String name)
    {
        EJReportSelectResultItem item = _items.get(name);
        if (item != null)
        {
            return item.getValue();
        }

        return null;
    }
}
