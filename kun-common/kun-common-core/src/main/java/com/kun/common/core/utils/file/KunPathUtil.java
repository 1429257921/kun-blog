package com.kun.common.core.utils.file;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 公共路径工具类
 *
 * @author gzc
 * @since 2022/9/30 20:31
 */
public class KunPathUtil {

    /**
     * 获取文件父级目录
     *
     * @param filePath 文件路径 (示例 /PDF/2022-02-25/0a2986e3-bfd7-4a82-a65b-31bb9cb9e9fe.pdf)
     * @return 父级目录 (示例 \PDF\2022-02-25)(\和/根据操作系统划分)
     */
    public static String getFileParent(String filePath) {
        if (StrUtil.isNotBlank(filePath)) {
            Path path = Paths.get(filePath);
            if (ObjectUtil.isNotNull(path)) {
                if (ObjectUtil.isNotNull(path.getFileName())) {
                    return path.getParent().toString();
                }
            }
        }
        return null;
    }

    /**
     * 路径分割符\替换成/
     *
     * @param filePath
     * @return
     */
    public static String getFileParent1(String filePath) {
        String fileParent = getFileParent(filePath);
        if (StrUtil.isNotBlank(fileParent)) {
            fileParent = fileParent.replace("\\", "/");
        }
        return fileParent;
    }


    /**
     * 获取文件路径中的文件名称
     *
     * @param filePath 文件路径 (示例 /PDF/2022-02-25/0a2986e3-bfd7-4a82-a65b-31bb9cb9e9fe.pdf)
     * @return 文件名称（不带文件类型） (示例 0a2986e3-bfd7-4a82-a65b-31bb9cb9e9fe)
     */
    public static String getFileName(String filePath) {
        String fileNameAndType = getFileNameAndType(filePath);
        if (StrUtil.isNotBlank(fileNameAndType)) {
            String[] split = fileNameAndType.split("\\.");
            if (ArrayUtil.isNotEmpty(split)) {
                return split[0];
            }
        }
        return null;
    }

    /**
     * 获取文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型(例如txt 、 pdf 、 png 、 jpg等)
     */
    public static String getFileType(String filePath) {
        String fileNameAndType = getFileNameAndType(filePath);
        if (StrUtil.isNotBlank(fileNameAndType)) {
            String[] split = fileNameAndType.split("\\.");
            if (ArrayUtil.isNotEmpty(split) && split.length == 2) {
                return split[1];
            }
        }
        return null;
    }

    /**
     * 获取文件名称和文件类型
     *
     * @param filePath 文件路径
     * @return 文件名称.文件类型（例如 aaa.pdf、bbb.png等）
     */
    public static String getFileNameAndType(String filePath) {
        if (StrUtil.isNotBlank(filePath)) {
            Path path = Paths.get(filePath);
            if (ObjectUtil.isNotNull(path)) {
                if (ObjectUtil.isNotNull(path.getFileName())) {
                    return path.getFileName().toString();
                }
            }
        }
        return null;
    }

}
