package org.entirej.framework.report.properties;

import java.util.List;

import org.entirej.framework.report.enumerations.EJReportScreenItemType;

public interface EJCoreReportSreenItemContainer
{

    public abstract EJCoreReportBlockProperties getBlockProperties();

    public abstract void addItemProperties(int index, EJCoreReportScreenItemProperties itemProperties);

    public abstract List<EJCoreReportScreenItemProperties> getAllItemProperties();

    public abstract EJCoreReportScreenItemProperties getItemProperties(String name);

    public abstract void removeItem(EJCoreReportScreenItemProperties item);

    public abstract EJCoreReportScreenItemProperties createItem(EJReportScreenItemType type, String name, int index);

}