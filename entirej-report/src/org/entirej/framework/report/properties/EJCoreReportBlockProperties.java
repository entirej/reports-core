package org.entirej.framework.report.properties;

import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.report.EJReportFrameworkManager;
import org.entirej.framework.report.interfaces.EJReportBlockProperties;
import org.entirej.framework.report.properties.EJReportBlockContainer.BlockContainerItem;
import org.entirej.framework.report.service.EJReportBlockService;

public class EJCoreReportBlockProperties implements EJReportBlockProperties, BlockContainerItem
{

    private boolean                      _isControlBlock           = false;

    private String                       _blockDescription         = "";
    private boolean                      _isReferenced             = false;

    private EJCoreReportProperties       _reportProperties;
    private String                       _name                     = "";

    private EJCoreReportScreenProperties _layoutScreenProperties;

    private String                       _serviceClassName;
    private String                       _actionProcessorClassName = "";
    
    private EJReportBlockService<?>                   _blockService;

    private EJReportBlockItemContainer   _itemContainer;

    
    public EJCoreReportBlockProperties(EJCoreReportProperties reportProperties, String blockName, boolean isCcontrolBlock)
    {

        _reportProperties = reportProperties;
        _name = blockName;
        _isControlBlock = isCcontrolBlock;
        _layoutScreenProperties = new EJCoreReportScreenProperties(this);
        _itemContainer = new EJReportBlockItemContainer(this);
    }

    @Override
    public EJCoreReportScreenProperties getLayoutScreenProperties()
    {
        return _layoutScreenProperties;
    }

    @Override
    public Collection<EJCoreReportItemProperties> getAllItemProperties()
    {
        return new ArrayList<EJCoreReportItemProperties>(_itemContainer.getAllItemProperties());
    }

    @Override
    public EJCoreReportItemProperties getItemProperties(String itemName)
    {
        return _itemContainer.getItemProperties(itemName);
    }

 
    @Override
    public boolean isControlBlock()
    {
        return _isControlBlock;
    }

    public void setControlBlock(boolean isControlBlock)
    {
        _isControlBlock = isControlBlock;
    }

    @Override
    public boolean isReferenceBlock()
    {
        return _isReferenced;
    }

    @Override
    public String getDescription()
    {
        return _blockDescription;
    }

    public void setDescription(String description)
    {
        _blockDescription = description;
    }

    @Override
    public EJCoreReportProperties getReportProperties()
    {
        return _reportProperties;
    }

    public String getName()
    {
        return _name;
    }

    public void internalSetName(String blockName)
    {
        if (blockName != null && blockName.trim().length() > 0)
        {
            _name = blockName;
        }
    }

    public String getActionProcessorClassName()
    {
        return _actionProcessorClassName;
    }

    public void setActionProcessorClassName(String processorClassName)
    {
        _actionProcessorClassName = processorClassName;

    }

    /**
     * Returns the class name of the service that is responsible for the data
     * manipulation for this block
     * 
     * @return the service class name
     */
    public String getServiceClassName()
    {
        return _serviceClassName;
    }

    /**
     * Sets the class name of the service that is responsible for the retrieval
     * and manipulation of this blocks data
     * 
     * @return the service class name
     */
    public void setServiceClassName(String serviceClassName)
    {

        _serviceClassName = serviceClassName;
        
        if (serviceClassName != null && serviceClassName.trim().length() > 0)
        {
            _blockService = _reportProperties.getFrameworkManager().getBlockServiceFactory().createBlockService(serviceClassName);
        }

    }

    public EJReportBlockItemContainer getItemContainer()
    {
        return _itemContainer;
    }

    @Override
    public EJReportBlockService<?> getBlockService()
    {
        return _blockService;
    }

    public EJReportFrameworkManager getFrameworkManager()
    {
        return _reportProperties.getFrameworkManager();
    }

}
