package com.kun.blog.entity.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author gzc
 * @since 2023/1/18 5:02
 **/
@Data
@ToString
public class ScanVideoResponse implements Serializable {
//    @NotBlank(message = "签名值为空")
    private String checksum = "";
    //    @NotBlank(message = "校验结果json字符串为空")
    private String content = "";

    public Content getContentObject() {
        return JSON.parseObject(this.content, ScanVideoResponse.Content.class);
    }

    @lombok.Data
    @ToString
    public static class Content {
        private String msg = "";
        private Integer code = 0;
        private String dataId = "";
        private String requestId = "";
        private ScanVideoResponse.Content.Extras extras = new Extras();
        /**
         * 视频检测结果
         */
        private List<ScanVideoResponse.Content.Result> results = new ArrayList<>();
        /**
         * 语音检测结果
         */
        private ScanVideoResponse.Content.AudioScanResult audioScanResults = new ScanVideoResponse.Content.AudioScanResult();
        private String taskId = "";
        private String url = "";


//        private List<Data> data = new ArrayList<>();

        @lombok.Data
        @ToString
        public static class Extras {
            private String frameCount = "";
            private String framePrefix = "";
        }

        /**
         * 视频检测结果
         */
        @lombok.Data
        @ToString
        public static class Result {
            private Double rate = 0D;
            private String suggestion = "";
            /**
             * 视频检测结果的分类。不同检测场景的结果分类不同，具体如下：
             * 视频智能鉴黄（porn）结果分类：
             * normal：正常
             * porn：色情
             * 视频暴恐涉政（terrorism）结果分类：
             * normal：正常
             * terrorism：暴恐涉政
             * 视频不良场景（live）结果分类：
             * normal：正常
             * live：包含不良场景
             * 视频logo（logo）结果分类：
             * normal：正常
             * logo：包含logo
             * 视频图文违规（ad）结果分类：
             * normal：正常
             * ad：包含广告或文字违规信息
             */
            private String label = "";
            private String scene = "";
            private List<ScanVideoResponse.Content.Result.Frame> frames = new ArrayList<>();


            @lombok.Data
            @ToString
            public static class Frame {
                private Integer offset = 0;
                private Double rate = 0D;
                private String label = "";
                private String url = "";

                private List<ScanVideoResponse.Content.Result.Frame.QrcodeLocation> qrcodeLocations = new ArrayList<>();
                private String[] qrcodeData = new String[0];

                @lombok.Data
                @ToString
                public static class QrcodeLocation {
                    private String qrcode = "";
                    private Double w = 0D;
                    private Double h = 0D;
                    private Double x = 0D;
                    private Double y = 0D;
                }
            }
        }

        /**
         * 语音检测结果
         */
        @lombok.Data
        @ToString
        public static class AudioScanResult {
            private String scene = "";
            /**
             * 该句语音的检测结果分类。取值：
             * normal：正常
             * spam：包含垃圾信息
             * ad：广告
             * politics：涉政
             * terrorism：暴恐
             * abuse：辱骂
             * porn：色情
             * flood：灌水
             * contraband：违禁
             * customized：自定义（例如命中自定义关键词）
             */
            private String label = "";
            private String suggestion = "";
            private Double rate = 0D;
            private List<ScanVideoResponse.Content.AudioScanResult.Detail> details = new ArrayList<>();

            @lombok.Data
            @ToString
            public static class Detail {
                private Integer startTime = 0;
                private Integer endTime = 0;
                private String text = "";
                private String label = "";
                private String keyword = "";
                private String libName = "";
            }

        }
    }


    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

//        String jsonStr = "{\"checksum\":\"b12b9bff612003cd1212ab25de79264b2c90e74dfa8f6422bcab1b9e95ba3cbd\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/1814b830/106816_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1814b830/106816_1673894848103.mp4-frames/f00001.jpg?Expires=1673990826&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=FxUlsJrwscBx%2BSF85RzFguKYZzM%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1814b830/106816_1673894848103.mp4-frames/f00002.jpg?Expires=1673990826&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=zCzMfGhHnSrK1knVH4j0eh9pBPk%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1814b830/106816_1673894848103.mp4-frames/f00004.jpg?Expires=1673990826&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=2S8BNvBgJpudpqQxxaLmOJBxLDM%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1814b830/106816_1673894848103.mp4-frames/f00005.jpg?Expires=1673990826&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=HSC6ggneUwBZEEKxc3HYKkDTb1I%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vi4dcRBGE1Anw4mewuSjBuJm-1xs65m\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";
        String jsonStr = "{\"checksum\":\"b12b9bff612003cd1212ab25de79264b2c90e74dfa8f6422bcab1b9e95ba3cbd\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00001.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=6IOI%2B0djv6ktns5SasRML1pIeqA%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00002.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=xp3IS2ZQxcbai3o81FvRulT%2FyCo%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00004.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=d6B08B26n6S%2BGRQ04BFeUE3yhoE%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00005.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=GbJoJQJ%2BybD9GYRN9rUVsnqi%2BUk%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vi7kZX3OZ0bEy7ScDElLnu@r-1xsDKF\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";
        ScanVideoResponse scanVideoResponse = JSON.parseObject(jsonStr, ScanVideoResponse.class);
//        System.out.println(JSON.toJSONString(scanVideoResponse, true));
        System.out.println("=====================================");
//        String checksum = scanVideoResponse.getChecksum();
//        System.out.println(scanVideoResponse.getChecksum());
//        System.out.println(scanVideoResponse.getDataContent());
        String uId = "1639145065557555";
        String seed = "hahaha";
//        String content = JSON.toJSONString();
//        System.out.println(JSON.parseObject(content).toString(SerializerFeature.PrettyFormat));
        Content content = scanVideoResponse.getContentObject();
        List<ScanVideoResponse.Content.Result> resultList = content.getResults();
        if (CollUtil.isNotEmpty(resultList)) {
            for (ScanVideoResponse.Content.Result result : resultList) {
                String label = result.getLabel();
//                    log.info("获取视频检测结果label->{}", label);
                if (!CheckResultEnum.NORMAL.getName().equals(label)) {
                    sb.append(CheckResultEnum.getDesc(label)).append("、");
                }
            }
        }
        // 获取语音检测结果
        ScanVideoResponse.Content.AudioScanResult audioScanResult = content.getAudioScanResults();
        if (audioScanResult != null) {
            String label = audioScanResult.getLabel();
//                log.info("获取视频语音检测结果label->{}", label);
            if (!CheckResultEnum.NORMAL.getName().equals(label)) {
                sb.append(CheckResultEnum.getDesc(label)).append("、");
            }
        }

        // 涉及违规
        String error = sb.toString();
        System.out.println("视频检测处理结果，业务ID->" + 111 + ", 业务类型->" + 0 + ", 违规内容->" + error);


        String code = uId + seed + content;
        String sha256StrJava = getSHA256StrJava(code);
        System.out.println(sha256StrJava);
        System.out.println("=======================================");
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        byte[] digest = sha256.digest(code);
        String s = HexUtil.encodeHexStr(digest);
        System.out.println(s);
    }


    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 检测结果枚举类
     */
    @Getter
    public enum CheckResultEnum {
        /**
         * 检测结果枚举类
         */
        NORMAL("normal", "正常"),
        PORN("porn", "色情"),
        TERRORISM("terrorism", "暴恐涉政"),
        LIVE("live", "不良场景"),
        LOGO("logo", "logo"),
        AD("ad", "广告或文字违规"),
        ;

        /**
         * 英文描述
         */
        private String name;
        /**
         * 中文描述
         */
        private String desc;

        CheckResultEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public static String getDesc(String name) {
            if (StrUtil.isNotBlank(name)) {
                for (CheckResultEnum value : CheckResultEnum.values()) {
                    if (value.getName().equals(name)) {
                        return value.getDesc();
                    }
                }
            }
            return "";
        }
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
