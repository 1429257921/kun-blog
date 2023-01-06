package com.kun.common.core.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * excel导出工具类
 *
 * @author gzc
 * @since 2022/12/9 14:32
 **/
@Slf4j
public class EasyExcelWriteExportUtils {

    /**
     * 注解导出工具类
     *
     * @param exportList   需要导出的数据
     * @param outFileName  导出excel文件名称
     * @param response     响应对象
     * @param isCloseExcel 是否关闭excel流
     * @return ExcelWriter
     */
    @SuppressWarnings("all")
    public static ExcelWriter exportToResponse(List<?> exportList, String outFileName,
                                               String sheetName,
                                               Class<?> clazz,
                                               HttpServletResponse response,
                                               boolean isCloseExcel) {
        ExcelWriter excelWriter = null;
        try {
            // 设置头策略和内容策略
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(
                            buildHeadCellStyle(),
                            Collections.singletonList(buildContentCellStyle()));

            excelWriter = getExcelWriter(outFileName, clazz,
                    Collections.singletonList(horizontalCellStyleStrategy),
                    response);

            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(exportList, writeSheet);
        } catch (Exception e) {
            log.error("response导出{}发生异常:{}", outFileName, e);
        } finally {
            if (isCloseExcel) {
                // 别忘记关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        }
        if (isCloseExcel) {
            return null;
        } else {
            return excelWriter;
        }
    }

    /**
     * 根据输出文件名获取excelWriter类
     *
     * @param outFileName 导出excel文件名称
     * @param response    响应对象
     */
    public static ExcelWriter getExcelWriter(String outFileName, Class<?> clazz, List<WriteHandler> writeHandlerList, HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter;
        response.reset();
        response.setContentType("application/force-download;charset=utf-8");

        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(outFileName, "UTF-8") + ".xlsx");

        OutputStream out = response.getOutputStream();
        //这里指定需要表头，因为model通常包含表信头息
        if (writeHandlerList != null && !writeHandlerList.isEmpty()) {
            ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write(out, clazz);
            for (WriteHandler writeHandler : writeHandlerList) {
                excelWriterBuilder.registerWriteHandler(writeHandler);
            }
            excelWriter = excelWriterBuilder.build();
        } else {
            excelWriter = EasyExcelFactory.write(out, clazz).build();
        }

        return excelWriter;
    }

    /**
     * 构建头单元格样式
     *
     * @return WriteCellStyle
     */
    public static WriteCellStyle buildHeadCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 自动换行
        headWriteCellStyle.setWrapped(true);
        // 左右居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headWriteCellStyle;
    }

    /**
     * 构建内容单元格样式
     *
     * @return WriteCellStyle
     */
    public static WriteCellStyle buildContentCellStyle() {
        WriteCellStyle contentCellStyle = new WriteCellStyle();

        // 自动换行
        contentCellStyle.setWrapped(true);
        // 下边框(细边框)
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        // 右边框
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        // 上边框
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        // 左右居中
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return contentCellStyle;
    }
}
