package com.kun.common.http.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.io.KunIoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * OkHttp工具类
 *
 * @author gzc
 * @since 2022/9/30 20:38
 */
@RequiredArgsConstructor
@Slf4j
public class OkHttpUtil {

    public static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static MediaType XML_MEDIA_TYPE = MediaType.parse("application/xml; charset=utf-8");
    public static MediaType PNG_MEDIA_TYPE = MediaType.parse("image/png; charset=utf-8");
    public static MediaType PDF_MEDIA_TYPE = MediaType.parse("application/pdf; charset=utf-8");
    public static MediaType FROM_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private static Charset CHARSET = CharsetUtil.CHARSET_UTF_8;

    private static OkHttpClient staticOkHttpClient;

    private final OkHttpClient okHttpClient;

    /**
     * bean对象赋值给static属性
     */
    @PostConstruct
    private void init() {
        OkHttpUtil.staticOkHttpClient = okHttpClient;
    }


    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public static String doGet(String apiMsg, String url) {
        return executeGet(apiMsg, url, null, null);
    }


    /**
     * get 请求
     *
     * @param url      请求url地址
     * @param paramMap 请求参数 map
     * @return string
     */
    public static String doGet(String apiMsg, String url, Map<String, String> paramMap) {
        return executeGet(apiMsg, url, paramMap, null);
    }

    /**
     * get 请求
     *
     * @param url 请求url地址
     *            //	 * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public static String doGetHeader(String apiMsg, String url, Map<String, String> headMap) {
        return executeGet(apiMsg, url, null, headMap);
    }


    /**
     * get 请求
     *
     * @param url      请求url地址
     * @param paramMap 请求参数 map
     *                 //	 * @param headers  请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public static String executeGet(String apiMsg, String url, Map<String, String> paramMap,
                                    Map<String, String> headMap) {
        log.info("调用{}, 请求路径为{}, 请求参数为{}", apiMsg, url, JSON.toJSONString(paramMap));
        StringBuilder sb;
        Request.Builder builder;
        try {
            Assert.notBlank(url, "请求路径为空");
            sb = new StringBuilder(url);
            if (paramMap != null && paramMap.keySet().size() > 0) {
                boolean firstFlag = true;
                for (String key : paramMap.keySet()) {
                    if (firstFlag) {
                        sb.append("?").append(key).append("=").append(paramMap.get(key));
                        firstFlag = false;
                    } else {
                        sb.append("&").append(key).append("=").append(paramMap.get(key));
                    }
                }
            }

            builder = new Request.Builder();
//			if (headers != null && headers.length > 0) {
//				if (headers.length % 2 == 0) {
//					for (int i = 0; i < headers.length; i = i + 2) {
//						builder.addHeader(headers[i], headers[i + 1]);
//					}
//				} else {
//					log.warn("headers's length[{}] is error.", headers.length);
//				}
//			}
            if (CollUtil.isNotEmpty(headMap)) {
                log.info("调用{}, 请求头为{}", apiMsg, JSON.toJSONString(headMap));
                headMap.forEach((k, v) -> builder.addHeader(k, v));
            }

        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + " 拼接GET参数发生异常", e);
        }

        try {
            Request request = builder.url(sb.toString()).build();
            byte[] bytes = executeBytes(request);
            String responseText = StrUtil.str(bytes, CHARSET);
            if (StrUtil.isBlank(responseText)) {
                throw new BizException("返回结果为空");
            }
            log.info("调用{}返回结果为{}", apiMsg, responseText);
            return responseText;
        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + "发生异常, 异常原因为" + e.getMessage(), e);
        }
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
//	public String doPost(String apiMsg, String url, Map<String, String> params) {
//		FormBody.Builder builder = new FormBody.Builder();
//
//		if (params != null && params.keySet().size() > 0) {
//			for (String key : params.keySet()) {
//				builder.add(key, params.get(key));
//			}
//		}
//		Request request = new Request.Builder().url(url).post(builder.build()).build();
//		log.info("do post request and url[{}]", url);
//
//		return execute(request);
//	}


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public static String doPostJson(String apiMsg, String url, String json) {
        return executePost(apiMsg, url, json, null, JSON_MEDIA_TYPE);
    }

    public static byte[] doPostJson(String url, String json) {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return executeBytes(request);
    }

    public static byte[] doPostJson(String url, String json, Map<String, String> headMap) {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request.Builder reqBuilder = new Request.Builder().url(url).post(requestBody);

        if (CollUtil.isNotEmpty(headMap)) {
            headMap.forEach((k, v) -> reqBuilder.addHeader(k, v));
        }

		/*if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
			request.addHeader("Connection", "close");
		}*/
        Request request = reqBuilder.build();
        return executeBytes(request);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public static String doPostXml(String apiMsg, String url, String xml) {
        return executePost(apiMsg, url, xml, null, XML_MEDIA_TYPE);
    }


    public static String executePost(String apiMsg, String url, String data,
                                     Map<String, String> headMap, MediaType contentType) {
        try {
            log.info("调用{}, 请求路径为{}, 请求参数为{}", apiMsg, url, data);
            Assert.notBlank(url, "请求路径为空");
            RequestBody requestBody = RequestBody.create(contentType, data);
            Request.Builder reqBuilder = new Request.Builder().url(url).post(requestBody);
            if (CollUtil.isNotEmpty(headMap)) {
                log.info("调用{}, 请求头为{}", JSON.toJSONString(headMap));
                headMap.forEach((k, v) -> reqBuilder.addHeader(k, v));
            }
            Request request = reqBuilder.build();
            // 发起请求
            byte[] bytes = executeBytes(request);
            String responseText = StrUtil.str(bytes, CHARSET);
            if (StrUtil.isBlank(responseText)) {
                throw new BizException("调用接口返回结果为空");
            }
            log.info("调用{}返回结果为{}", apiMsg, responseText);
            return responseText;
        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + "发生异常, 异常原因为" + e.getMessage(), e);
        }
    }

    /**
     * 发起请求
     *
     * @param request
     * @return 响应字节数组
     */
    public static InputStream executeStream(Request request) {
        ByteArrayOutputStream byteArrayOutputStream = executeByteOutStream(request);
        if (byteArrayOutputStream != null) {
            return KunIoUtil.getInputStream(byteArrayOutputStream);
        }
        return null;
    }

    /**
     * 发起请求
     *
     * @param request
     * @return 字节输出流
     */
    public static ByteArrayOutputStream executeByteOutStream(Request request) {
        byte[] bytes = executeBytes(request);
        if (ArrayUtil.isNotEmpty(bytes)) {
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(bytes);
            return KunIoUtil.cloneInputStream(byteArrayInputStream);
        }
        return null;
    }

    /**
     * 发起请求
     *
     * @param request
     * @return 输入流
     */
    public static byte[] executeBytes(Request request) {
        Response response = null;
        try {
            response = staticOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return new byte[0];
    }

    /**
     * Post请求 文件上传
     *
     * @param url       请求地址
     * @param file      文件对象
     * @param mediaType 媒体类型
     * @return 上传结果
     */
    public static byte[] upload(String url, File file,
                                com.kun.common.http.entity.MediaType mediaType) {
        // 封装文件实体
        RequestBody fileBody = RequestBody.create(MediaType.parse(mediaType.getValue()), file);
        // 封装请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", UUID.fastUUID().toString(true), fileBody)
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return executeBytes(request);
    }


}
