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

import org.entirej.framework.report.interfaces.EJReportException;

/**
 * This exception is the main exception within the <b>EntireJ Framework</b>
 */

public class EJReportRuntimeException extends RuntimeException implements EJReportException
{
    private boolean         _stopProcessing = false;
    private EJReportMessage _message        = null;

    public EJReportRuntimeException()
    {
        _stopProcessing = true;
    }

    /**
     * Indicates if this exception was just thrown to stop processing
     * <p>
     * If an empty exception is thrown, then the user wanted to just stop the
     * current processing without giving any user or log message
     * 
     * @return <code>true</code> if this exception was only used to stop
     *         processing
     * 
     */
    public boolean stopProcessing()
    {
        return _stopProcessing;
    }

    public EJReportRuntimeException(String message)
    {
        super(message);
        _message = new EJReportMessage(message);
    }

    public EJReportRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
        _message = new EJReportMessage(message);
    }

    public EJReportRuntimeException(EJReportMessage message)
    {
        super(message.getMessage());
        _message = message;
    }

    public EJReportRuntimeException(Throwable cause)
    {
        super(cause);
        if (cause instanceof EJReportException)
        {
            _stopProcessing = ((EJReportException) cause).stopProcessing();
        }
        this.setStackTrace(cause.getStackTrace());
        extractMessage(cause);
    }

    public EJReportRuntimeException(EJReportMessage message, Throwable cause)
    {
        super(message.getMessage(), cause);
        this.setStackTrace(cause.getStackTrace());
        _message = message;
    }

    private void extractMessage(Throwable cause)
    {
        if (cause instanceof EJReportException)
        {
            _message = ((EJReportException) cause).getFrameworkMessage();
        }
        else
        {
            _message = new EJReportMessage(cause.getMessage());
        }
    }

    public EJReportMessage getFrameworkMessage()
    {
        return _message;
    }
}
