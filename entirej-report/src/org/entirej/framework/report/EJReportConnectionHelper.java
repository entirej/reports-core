package org.entirej.framework.report;

import java.lang.ref.WeakReference;

public class EJReportConnectionHelper
{
    private static volatile WeakReference<EJReportFrameworkManager> ref;
    private static String entireJPropertiesFileName;

    static synchronized void setEJFrameworkManager(EJReportFrameworkManager manager, String entireJFileName)
    {
        if (ref == null || ref.get() == null)
        {
            ref = new WeakReference<EJReportFrameworkManager>(manager);
        }
        entireJPropertiesFileName = entireJFileName;
    }

    public static EJReportManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().getConnection();
        else
        {
            try
            {
                EJReportFrameworkManager manager = new EJReportFrameworkManager(entireJPropertiesFileName);
                ref = new WeakReference<EJReportFrameworkManager>(manager);
                return manager.getConnection();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
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
