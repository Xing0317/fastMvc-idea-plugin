package ${package};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
<#list imports as import>
import ${import};
</#list>

/**
 * ${comment}
 * @author ${author}
 * @date ${.now?datetime}
 */
@Mapper
public interface ${simpleName} extends BaseMapper<${simpleName?replace("Dao", "")}>{

}
