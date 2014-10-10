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

import org.entirej.framework.report.interfaces.EJReportFrameworkConnection;

public class EJManagedReportFrameworkConnection implements EJReportFrameworkConnection
{
    private boolean                     _initialiser = false;
    private EJReportConnectionRetriever _connectionRetriever;

    EJManagedReportFrameworkConnection(EJReportConnectionRetriever connectionRetriever, boolean initialiser)
    {
        _connectionRetriever = connectionRetriever;
        _initialiser = initialiser;
    }

    public void commit()
    {
        if (_initialiser)
        {
            _connectionRetriever.getConnection().commit();
        }
    }

    public Object getConnectionObject()
    {
        return _connectionRetriever.getConnection().getConnectionObject();
    }

    public void rollback()
    {
        if (_initialiser)
        {
            _connectionRetriever.getConnection().rollback();
        }
    }

    public void close()
    {
        if (_initialiser)
        {
            _connectionRetriever.getConnection().commit();
            _connectionRetriever.close();
        }
    }
}
