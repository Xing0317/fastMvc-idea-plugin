package ${package};

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
<#list imports as import>
import ${import};
</#list>

/**
 * ${comment}
 * @author ${author}
 * @date ${.now?datetime}
*/
@Slf4j
@RestController
@RequestMapping("/api-v1/")
public class ${simpleName} {

    @Resource
    private ${resourceClassName} ${resourceFieldName};

}
