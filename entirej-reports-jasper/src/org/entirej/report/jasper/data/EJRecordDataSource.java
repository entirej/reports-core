/*******************************************************************************
 * Copyright 2014 Mojave Innovations GmbH
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

package org.entirej.report.jasper.data;

import java.io.Serializable;
import java.util.Collection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.entirej.framework.report.EJReportRecord;

public class EJRecordDataSource implements JRDataSource, Serializable
{

    private final EJReportRecord[] data;
    private int              index = -1;

    public EJRecordDataSource(EJReportRecord... data)
    {
        this.data = data;
    }
    public EJRecordDataSource(Collection<EJReportRecord> records)
    {
        this.data = records.toArray(new EJReportRecord[0]);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException
    {
        EJReportRecord record = data[index];
        return record.getValue(field.getName());
    }

    @Override
    public boolean next() throws JRException
    {
        index++;

        return (index < data.length);
    }

}
