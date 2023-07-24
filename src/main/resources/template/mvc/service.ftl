package ${package};

import com.baomidou.mybatisplus.extension.service.IService;
<#list imports as import>
import ${import};
</#list>

/**
 * ${comment}
 * @author ${author}
 * @date ${.now?datetime}
 */
public interface ${simpleName} extends IService<${simpleName?replace("Service", "")}>{
}
