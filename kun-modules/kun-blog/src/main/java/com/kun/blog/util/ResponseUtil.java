package com.kun.blog.util;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/10 2:38
 **/
public class ResponseUtil {

    /**
     * 输出
     *
     * @param response
     * @param result
     */
    public static void out(HttpServletResponse response, ResponseEntity<Object> result) {
//        ObjectMapper mapper = new ObjectMapper();
//        PrintWriter writer = null;
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        try {
//            ServletOutputStream outputStream = response.getOutputStream();
//            writer = response.getWriter();
//            mapper.writeValue(writer, result);
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                writer.flush();
//                writer.close();
//            }
//        }
        for (Map.Entry<String, List<String>> header : result.getHeaders().entrySet()) {
            String chave = header.getKey();
            for (String valor : header.getValue()) {
                response.addHeader(chave, valor);
            }
        }
        response.setStatus(result.getStatusCodeValue());
        try {
            response.getWriter().write((String) result.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
