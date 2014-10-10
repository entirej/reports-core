package org.entirej.framework.report.properties;

import org.entirej.framework.report.interfaces.EJReportItemProperties;

public class EJCoreReportItemProperties implements EJReportItemProperties
{

    private EJCoreReportBlockProperties blockProperties;

    private String                      _name;
    private String                      _dataTypeClassName = "";
    private boolean                     _blockServiceItem  = false;

    private String                      _defaultQueryValue = "";

    private String                      _fieldName;

    private Class<?>                    _dataTypeClass;

    public EJCoreReportItemProperties(EJCoreReportBlockProperties blockProperties)
    {
        this.blockProperties = blockProperties;
    }

    public EJCoreReportItemProperties(EJCoreReportBlockProperties blockProperties, String itemName)
    {
        this.blockProperties = blockProperties;
        this._name = itemName;
    }

    public EJCoreReportBlockProperties getBlockProperties()
    {
        return blockProperties;
    }

    public String getBlockName()
    {
        return blockProperties.getName();
    }

    public boolean belongsToControlBlock()
    {
        return blockProperties.isControlBlock();
    }

    public void setDefaultQueryValue(String defaultQueryValue)
    {
        _defaultQueryValue = defaultQueryValue;

    }

    public String getDefaultQueryValue()
    {
        return _defaultQueryValue;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public String getFullName()
    {
        StringBuffer thisName = new StringBuffer();
        thisName.append(blockProperties.getReportProperties().getName());
        thisName.append(".");

        thisName.append(blockProperties.getName());
        thisName.append(".");
        thisName.append(this.getName());

        return thisName.toString();
    }

    public void setDataTypeClassName(String dataTypeClassName)
    {
        if (dataTypeClassName == null || dataTypeClassName.trim().length() == 0)
        {
            _dataTypeClassName = null;
            // _dataTypeClass = null;
            return;
        }

        _dataTypeClassName = dataTypeClassName;

        try
        {
            _dataTypeClass = Class.forName(dataTypeClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("The data type passed to setDataType cannot be found on the class path", e);
        }

    }

    public void setBlockServiceItem(boolean isBlockServiceItem)
    {
        _blockServiceItem = isBlockServiceItem;
    }

    @Override
    public boolean isBlockServiceItem()
    {
        return _blockServiceItem;
    }

    @Override
    public String getDataTypeClassName()
    {
        return _dataTypeClassName;
    }

    @Override
    public Class<?> getDataTypeClass()
    {
        return _dataTypeClass;
    }

    public String getFieldName()
    {
        return _fieldName;

    }

    /**
     * Sets this items field name
     * 
     * @param fieldName
     */
    public void setFieldName(String fieldName)
    {
        _fieldName = fieldName;
    }

}
