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
import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.properties.EJCoreReportBlockProperties;
import org.entirej.framework.report.properties.EJReportBlockContainer.BlockGroup;

public class EJReportPage implements Serializable
{
    private EJReport   _report;
    private BlockGroup _group;

    public EJReportPage(EJReport report, BlockGroup group)
    {
        _report = report;
        _group = group;
    }

    public Collection<EJReportBlock> getRootBlocks()
    {
        ArrayList<EJReportBlock> blocks = new ArrayList<EJReportBlock>();

        for (EJCoreReportBlockProperties blockProp : _group.getAllBlockProperties())
        {

            blocks.add(_report.getBlock(blockProp.getName()));
        }

        return blocks;
    }
    
    public String getName()
    {
        return _group.getName();
    }

}
