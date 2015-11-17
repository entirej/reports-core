package ${package_name};


<#list imports as import>
import ${import};
</#list>



public class ${class_name}
{
 
  
<#list columns as column>
    private ${column.data_type} _${column.var_name};
</#list>

    

<#list columns as column>
    @EJReportFieldName("${column.name}")
    public ${column.data_type} get${column.method_name}()
    {
      return _${column.var_name};
    }
    
    @EJReportFieldName("${column.name}")
    public void set${column.method_name}(${column.data_type} ${column.var_name})
    {
        _${column.var_name} = ${column.var_name};
        if (!_initialValues.containsKey(FieldNames.${column.name}))
        {
            _initialValues.put(FieldNames.${column.name}, ${column.var_name});
        }
    }
    
</#list>


}
