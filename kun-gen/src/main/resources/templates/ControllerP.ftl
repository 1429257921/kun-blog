package ${package.Controller};

import ${package.Service}.I${entity}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${table.comment!}控制层
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class ${entity}Controller {

    private final I${entity}Service ${entity?uncap_first}Service;



}
