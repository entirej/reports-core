package ${package_name};


<#list imports as import>
import ${import};
</#list>
import java.util.HashMap;



public class ${service_name} implements EJReportBlockService<${pojo_name}>
{
<#if query_statement != "">
  private final EJReportStatementExecutor _statementExecutor   = new EJReportStatementExecutor();
  private String   				    _selectStatement = ${query_statement};
</#if>


    @Override
    public List<${pojo_name}> executeQuery(EJReport report, EJReportQueryCriteria queryCriteria)
    {
<#if query_statement != "">
        return _statementExecutor.executeQuery(${pojo_name}.class, report, _selectStatement, queryCriteria);
</#if>
<#if query_statement == "" >
        return new java.util.ArrayList<${pojo_name}>(0);
</#if>         
    }    
    

  
}
