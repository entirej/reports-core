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

public class EJReportBlockContainer
{
    private List<BlockGroup>       _blockProperties;
    private EJCoreReportProperties _reportProperties;

    private BlockGroup             headerSection = new BlockGroup("Header");
    private BlockGroup             footerSection = new BlockGroup("Footer");

    public EJReportBlockContainer(EJCoreReportProperties reportProperties)
    {
        _reportProperties = reportProperties;
        _blockProperties = new ArrayList<BlockGroup>();
    }

    public EJCoreReportProperties getReportProperties()
    {
        return _reportProperties;
    }

    public boolean isEmpty()
    {
        return _blockProperties.isEmpty();

    }

    public void setFooterSection(BlockGroup footerSection)
    {
        this.footerSection = footerSection;
    }

    public BlockGroup getFooterSection()
    {
        return footerSection;
    }

    public void setHeaderSection(BlockGroup headerSection)
    {
        this.headerSection = headerSection;
    }

    public BlockGroup getHeaderSection()
    {
        return headerSection;
    }

    public boolean contains(String blockName)
    {

        return getBlockProperties(blockName) != null;
    }

    public void addPage(BlockGroup blockProperties)
    {
        if (blockProperties != null)
        {
            _blockProperties.add(blockProperties);
        }
    }

    public void removePage(BlockGroup blockProperties)
    {
        if (blockProperties != null)
        {
            _blockProperties.remove(blockProperties);
        }
    }

    public void addPage(int index, BlockGroup blockProperties)
    {
        if (blockProperties != null)
        {
            _blockProperties.add(index, blockProperties);
        }
    }

    /**
     * Used to retrieve a specific blocks properties.
     * 
     * @return If the block name parameter is a valid block contained within
     *         this form, then its properties will be returned if however the
     *         name is null or not valid, then a <b>null</b> object will be
     *         returned.
     * 
     */
    public EJCoreReportBlockProperties getBlockProperties(String blockName)
    {

        Iterator<BlockGroup> iti = _blockProperties.iterator();

        while (iti.hasNext())
        {

            BlockGroup containerItem = iti.next();

            EJCoreReportBlockProperties blockProperties = (containerItem).getBlockProperties(blockName);
            if (blockProperties != null)
            {
                return blockProperties;
            }

        }

        EJCoreReportBlockProperties blockProperties = headerSection.getBlockProperties(blockName);
        if (blockProperties != null)
        {
            return blockProperties;
        }
        blockProperties = footerSection.getBlockProperties(blockName);
        if (blockProperties != null)
        {
            return blockProperties;
        }
        return null;
    }

    public List<EJCoreReportBlockProperties> getAllBlockProperties()
    {
        List<EJCoreReportBlockProperties> list = new ArrayList<EJCoreReportBlockProperties>();

        list.addAll(headerSection.getAllBlockProperties());
        list.addAll(getRootBlockProperties());

        list.addAll(footerSection.getAllBlockProperties());

        for (EJCoreReportBlockProperties ejCoreReportBlockProperties : new ArrayList<EJCoreReportBlockProperties>(list))
        {
            collectSubBlocks(ejCoreReportBlockProperties, list);
        }
        return list;
    }

    public List<EJCoreReportBlockProperties> getRootBlockProperties()
    {
        List<EJCoreReportBlockProperties> list = new ArrayList<EJCoreReportBlockProperties>();

        Iterator<BlockGroup> iti = _blockProperties.iterator();
        while (iti.hasNext())
        {

            BlockGroup containerItem = iti.next();

            list.addAll((containerItem).getAllBlockProperties());

        }

        return list;
    }

    void collectSubBlocks(EJCoreReportBlockProperties blockProperties, List<EJCoreReportBlockProperties> list)
    {
        List<EJCoreReportBlockProperties> allSubBlocks = blockProperties.getScreenProperties().getAllSubBlocks();
        for (EJCoreReportBlockProperties sub : allSubBlocks)
        {
            list.add(sub);
            collectSubBlocks(sub, list);
        }
    }

    public List<BlockGroup> getPages()
    {
        return _blockProperties;
    }

    public String getDefaultBlockName()
    {
        String blockName = "BLOCK_";

        for (int i = 10;; i += 10)
        {
            if (!contains(blockName + "_" + i))
            {
                return blockName + i;
            }
        }
    }

    public static interface BlockContainerItem
    {
        // marker interface
    }

    public static class BlockGroup implements BlockContainerItem
    {

        public BlockGroup()
        {
        }

        public BlockGroup(String name)
        {
            this.name = name;
        }

        private String name;

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        private List<EJCoreReportBlockProperties> _blockProperties = new ArrayList<EJCoreReportBlockProperties>();

        public boolean isEmpty()
        {
            return _blockProperties.isEmpty();

        }

        public boolean contains(String blockName)
        {
            Iterator<EJCoreReportBlockProperties> iti = _blockProperties.iterator();
            while (iti.hasNext())
            {
                EJCoreReportBlockProperties props = iti.next();
                if (props.getName().equalsIgnoreCase(blockName))
                {
                    return true;
                }
            }
            return false;
        }

        public void addBlockProperties(EJCoreReportBlockProperties blockProperties)
        {
            if (blockProperties != null)
            {
                _blockProperties.add(blockProperties);
            }
        }

        public void replaceBlockProperties(EJCoreReportBlockProperties oldProp, EJCoreReportBlockProperties newProp)
        {
            if (oldProp != null && newProp != null)
            {
                int indexOf = _blockProperties.indexOf(oldProp);
                if (indexOf > -1)
                {
                    _blockProperties.set(indexOf, newProp);
                }
                else
                {
                    _blockProperties.add(newProp);
                }
            }
        }

        public void addBlockProperties(int index, EJCoreReportBlockProperties blockProperties)
        {
            if (blockProperties != null)
            {
                _blockProperties.add(index, blockProperties);
            }
        }

        public void removeBlockProperties(EJCoreReportBlockProperties props)
        {

            if (_blockProperties.contains(props))
            {

                _blockProperties.remove(props);

            }

        }

        public EJCoreReportBlockProperties getBlockProperties(String blockName)
        {

            Iterator<EJCoreReportBlockProperties> iti = _blockProperties.iterator();

            while (iti.hasNext())
            {
                EJCoreReportBlockProperties props = iti.next();

                if (props.getName().equalsIgnoreCase(blockName))
                {
                    return props;
                }
                EJCoreReportBlockProperties blockProperties = props.getScreenProperties().getSubBlocks().getBlockProperties(blockName);
                if (blockProperties != null)
                {
                    return blockProperties;
                }
            }
            return null;
        }

        public List<EJCoreReportBlockProperties> getAllBlockProperties()
        {
            return _blockProperties;
        }
    }

    public BlockGroup getFirstPage()
    {
        if (_blockProperties.size() > 0)
        {
            return _blockProperties.get(0);
        }
        BlockGroup page = new BlockGroup("PAGE1");
        _blockProperties.add(page);
        return page;

    }

}
