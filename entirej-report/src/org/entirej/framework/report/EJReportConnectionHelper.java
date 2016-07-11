package org.entirej.framework.report;

import java.lang.ref.WeakReference;

public class EJReportConnectionHelper
{
    private static volatile WeakReference<EJReportFrameworkManager> ref;

    static synchronized void setEJFrameworkManager(EJReportFrameworkManager manager)
    {
        if (ref == null || ref.get() == null)
        {
            ref = new WeakReference<EJReportFrameworkManager>(manager);
        }

    }

    public static EJReportManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().getConnection();

        throw new EJReportRuntimeException("EJFrameworkManager not initialized ");
    }
    
    public static EJReportManagedFrameworkConnection newConnection()
    {
        if (ref != null && ref.get() != null)
        {
            EJReportConnectionRetriever _connectionRetriever = new EJReportConnectionRetriever(ref.get());
            return new EJReportManagedFrameworkConnection(_connectionRetriever, true);
        }
           
        
        throw new EJReportRuntimeException("EJFrameworkManager not initialized ");
    }

}
