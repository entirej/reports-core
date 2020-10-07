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
package org.entirej.framework.report;

import java.io.Serializable;

import org.entirej.framework.report.interfaces.EJReportConnectionFactory;
import org.entirej.framework.report.interfaces.EJReportFrameworkConnection;
import org.entirej.framework.report.properties.EJCoreReportRuntimeProperties;

public class EJReportConnectionRetriever implements Serializable
{
    private boolean                     _closed            = true;
    private EJReportFrameworkConnection _frameworkConnection;
    private EJReportFrameworkManager    _frameworkManager;
    private EJReportConnectionFactory   _connectionFactory = EJCoreReportRuntimeProperties.getInstance().getConnectionFactory();

    EJReportConnectionRetriever(EJReportFrameworkManager manager)
    {
        _frameworkManager = manager;
    }

    public void close()
    {
        _closed = true;
        if (_frameworkConnection != null)
        {
            _frameworkConnection.close();
            _frameworkConnection = null;
        }
    }

    boolean isClosed()
    {
        return _closed;
    }
    
     void setClosed(boolean closed)
    {
        this._closed = closed;
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    synchronized EJReportFrameworkConnection getConnection()
    {
        if (_frameworkConnection == null)
        {
            _frameworkConnection = makeConnection();
        }
        return _frameworkConnection;
    }

    private EJReportFrameworkConnection makeConnection()
    {
        if (_connectionFactory == null)
        {
            // Don't pass the framework manager as a parameter as this could
            // cause a loop in the application if the message tries to retrieve
            // a managed connection which causes the same exception again and
            // again
            throw new EJReportRuntimeException(new EJReportMessage("Unable to retrieve connection factory: "
                    + EJCoreReportRuntimeProperties.getInstance().getConnectionFactoryClassName()));
        }

        return _connectionFactory.createConnection(_frameworkManager);
    }
}
