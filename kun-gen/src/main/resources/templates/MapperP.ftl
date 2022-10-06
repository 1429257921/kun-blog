package ${package.Mapper};

import ${package.Entity}.${entity};
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${table.comment!}${table.name}表持久层接口
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${table.mapperName} extends CoreMapper<${entity}> {

}
