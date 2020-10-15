package org.entirej.framework.report;

import java.lang.ref.WeakReference;

public class EJReportConnectionHelper
{
    private static volatile WeakReference<EJFrameworkManagerProvider> ref;

    static synchronized void setEJFrameworkManager(EJFrameworkManagerProvider manager)
    {
        if (ref == null || ref.get() == null)
        {
            ref = new WeakReference<EJFrameworkManagerProvider>(manager);
        }
    }

    public static EJReportManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().get().getConnection();
        else
        {
            throw new EJReportRuntimeException("EJReportManagedFrameworkConnection not initialized ");
        }
        
    }
    
    public static EJReportManagedFrameworkConnection newConnection()
    {
        if (ref != null && ref.get() != null)
        {
            EJReportConnectionRetriever _connectionRetriever = new EJReportConnectionRetriever(ref.get().get());
            return new EJReportManagedFrameworkConnection(_connectionRetriever, true);
        }
           
        
        throw new EJReportRuntimeException("EJReportManagedFrameworkConnection not initialized ");
    }
    
    public static interface EJFrameworkManagerProvider
    {
        EJReportFrameworkManager get();
    }

}
