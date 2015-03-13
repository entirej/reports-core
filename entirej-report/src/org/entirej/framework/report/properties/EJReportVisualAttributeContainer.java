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
package org.entirej.framework.report.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EJReportVisualAttributeContainer implements Serializable
{
    private List<EJCoreReportVisualAttributeProperties> _visualAttributeList;
    private EJReportVisualAttributeProperties           _lastAddedVisualAttribute;

    public EJReportVisualAttributeContainer(Collection<EJCoreReportVisualAttributeProperties> properties)
    {
        _visualAttributeList = new ArrayList<EJCoreReportVisualAttributeProperties>();
        if (properties != null)
        {
            _visualAttributeList.addAll(properties);
        }
    }

    public boolean contains(String name)
    {
        Iterator<EJCoreReportVisualAttributeProperties> attributes = _visualAttributeList.iterator();

        while (attributes.hasNext())
        {
            if (attributes.next().getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a <b>copy</b> of the <code>VisualAttributeProperties</code> with
     * the given name
     * <p>
     * A copy is returned as the
     * <code>{@link EJCoreReportVisualAttributeProperties}</code> are reused
     * across the entire application. If one user was to modify a
     * VisualAttribute, then it will be so for the entire application. For this
     * reason, a copy is made. The copy can be modified as required without
     * effecting other forms or users
     * 
     * @param vaPropertiesName
     *            The name of the required visual attribute properties
     * @return The <code>EJCoreVisualAttributeProperties</code> for the given
     *         name or <code>null</code> if there is no visual attribute with
     *         the given name
     */
    public EJReportVisualAttributeProperties getVisualAttributeProperties(String vaPropertiesName)
    {
        if (vaPropertiesName == null || vaPropertiesName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Iterator<EJCoreReportVisualAttributeProperties> iti = _visualAttributeList.iterator();

            while (iti.hasNext())
            {
                EJCoreReportVisualAttributeProperties props = iti.next();

                if (props.getName().equalsIgnoreCase(vaPropertiesName))
                {
                    return props.makeCopy();
                }
            }
            return null;
        }
    }

    public Collection<EJCoreReportVisualAttributeProperties> getVisualAttributes()
    {
        return _visualAttributeList;
    }

    /**
     * Adds the given <code>VisualAttribute</code> to this contain if it doesn't
     * already exist
     * 
     * @param visualAttribute
     *            The <code>VisualAttribute</code> to add
     */
    public void addVisualAttribute(EJCoreReportVisualAttributeProperties visualAttribute)
    {
        if (visualAttribute != null)
        {
            _visualAttributeList.add(visualAttribute);

            _lastAddedVisualAttribute = visualAttribute;
        }
    }

    public void replaceVisualAttribute(EJCoreReportVisualAttributeProperties visualAttribute)
    {
        if (_visualAttributeList.contains(visualAttribute))
        {
            removeVisualAttribute(visualAttribute);
        }

        addVisualAttribute(visualAttribute);
    }

    public void removeVisualAttribute(EJReportVisualAttributeProperties visualAttribute)
    {
        _visualAttributeList.remove(visualAttribute);

        if (visualAttribute == _lastAddedVisualAttribute)
        {
            _lastAddedVisualAttribute = null;
        }
    }

    public EJReportVisualAttributeProperties getLastAddedVisualAttribute()
    {
        return _lastAddedVisualAttribute;
    }

    public Collection<String> getVisualAttributeNames()
    {
        Iterator<EJCoreReportVisualAttributeProperties> visualAttributeNames = _visualAttributeList.iterator();

        ArrayList<String> names = new ArrayList<String>();

        while (visualAttributeNames.hasNext())
        {
            names.add(visualAttributeNames.next().getName());
        }

        return names;
    }
}
