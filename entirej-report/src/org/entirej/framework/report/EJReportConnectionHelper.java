package org.entirej.framework.report;

public class EJReportConnectionHelper
{
    private static volatile EJFrameworkManagerProvider ref;
    private static EJReportFrameworkManager SYS_BASE = EJReportFrameworkInitialiser.newFramework("report.ejprop");

    public static synchronized void setProvider(EJFrameworkManagerProvider manager)
    {
        if (ref == null  )
        {
            ref = manager;
        }
    }

    public static EJReportManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().getConnection();
        else
        {
            throw new EJReportRuntimeException("EJReportManagedFrameworkConnection not initialized ");
        }
        
    }
    
    public static EJReportManagedFrameworkConnection newConnection()
    {
        if (ref != null && ref.get() != null)
        {
            EJReportConnectionRetriever _connectionRetriever = new EJReportConnectionRetriever(ref.get());
            return new EJReportManagedFrameworkConnection(_connectionRetriever, true);
        }
           
        
        EJReportConnectionRetriever _connectionRetriever = new EJReportConnectionRetriever(SYS_BASE);
        return new EJReportManagedFrameworkConnection(_connectionRetriever, true);
    }
    
    public static interface EJFrameworkManagerProvider
    {
        EJReportFrameworkManager get();
    }

}
