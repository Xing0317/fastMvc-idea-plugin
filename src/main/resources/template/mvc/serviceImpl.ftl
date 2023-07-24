package ${package};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
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
@Service
public class ${simpleName} extends ServiceImpl<${simpleName?replace("ServiceImpl", "")}Dao ,${simpleName?replace("ServiceImpl", "")}> implements ${simpleName?replace("Impl","")} {

    @Resource
    private ${resourceClassName} ${resourceFieldName};

}
