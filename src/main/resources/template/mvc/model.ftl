package ${package};

import lombok.Data;
<#list imports as import>
import ${import};
</#list>

/**
 * ${comment}
 * @author ${author}
 * @date ${.now?datetime}
 */
@Data
public class ${simpleName} {
<#list fields as field>
    /**
    * ${field.comment}
    */
    private ${field.typeSimpleName} ${field.name};
</#list>
}
