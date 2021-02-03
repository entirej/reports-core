package org.entirej.framework.report;

public class EJReportConnectionHelper
{
    private static volatile EJFrameworkManagerProvider ref;
    private static EJReportFrameworkManager            SYS_BASE;

    static
    {
        try
        {
            SYS_BASE = EJReportFrameworkInitialiser.newFramework("report.ejprop");
        }
        catch (EJReportRuntimeException e)
        {
           //skip
        }
    }

    public static synchronized void setProvider(EJFrameworkManagerProvider manager)
    {
        
            ref = manager;
        
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
            return ref.get().newConnection();
        }

        return SYS_BASE!=null ? SYS_BASE.newConnection():null;
    }

    public static interface EJFrameworkManagerProvider
    {
        EJReportFrameworkManager get();
    }

}
