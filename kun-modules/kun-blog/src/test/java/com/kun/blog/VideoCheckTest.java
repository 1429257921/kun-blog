package com.kun.blog;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import com.kun.blog.entity.vo.ScanVideoResponse;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO
 *
 * @author gzc
 * @since 2023/1/31 15:30
 **/
public class VideoCheckTest {
    //        public static final String ALIYUN_UID = "270571842353286739";
//    public static final String ALIYUN_UID = "201350470916067184";
//    public static final String ALIYUN_UID = "228693142353416397";
    public static final String ALIYUN_UID = "234261988411135526";
//    public static final String VIDEO_SEED = "pfVideoCheckSeed";
    public static final String VIDEO_SEED = "ABC";

    static String jsonStr1 = "{\"checksum\":\"6e9ed7dd166e4e36245139dc2279c2815f5a7f6669779c2398a7c7fb59ee84c6\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/3161b830/2456570_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/3161b830/2456570_1673894848103.mp4-frames/f00001.jpg?Expires=1675151810&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=6P%2FZE12JcY3%2BEJ3FlrkFL8HYSi0%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/3161b830/2456570_1673894848103.mp4-frames/f00002.jpg?Expires=1675151810&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=MGIrZM0WRP496mSo81UwP9XMWQU%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/3161b830/2456570_1673894848103.mp4-frames/f00004.jpg?Expires=1675151810&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=nrFrzTdwE2JcPGwG%2B1%2Fwm%2F6LsUI%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/3161b830/2456570_1673894848103.mp4-frames/f00005.jpg?Expires=1675151810&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=EZ5eeiswFlMNfD9icsSO%2FKLaq98%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"viPQWXe3cXJD75MxZq4Q8H0-1xwqUA\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";
    static String jsonStr2 = "{\"checksum\":\"d076a2640628437924ef9597560d60385b4cc9de5e9ab359b9be42b18e02ed03\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/31120b830/1878943_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/31120b830/1878943_1673894848103.mp4-frames/f00001.jpg?Expires=1675153297&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=Ud%2Fpdiz0zxbOrvJn2Mt2S5qTXN0%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/31120b830/1878943_1673894848103.mp4-frames/f00002.jpg?Expires=1675153297&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=mBaxeUYN3AkJpK1%2FlDtTsB%2FBknE%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/31120b830/1878943_1673894848103.mp4-frames/f00004.jpg?Expires=1675153297&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=EAIDfRHeEe4iFvDprCw8a8MAQ9c%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/31120b830/1878943_1673894848103.mp4-frames/f00005.jpg?Expires=1675153297&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=4u4UV%2FiTAZe14K8u5BK0YS8f1uQ%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vivGJWDqxbzk6FZxL4$61zy-1xwrfh\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";
    static String jsonStr3 = "{\"checksum\":\"226764f2d4cadfc610430fe16239f15ddaecdf780a0a21ba07c5be4268de3d62\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/0168b830/2649317_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0168b830/2649317_1673894848103.mp4-frames/f00001.jpg?Expires=1675235190&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=15b9%2BGTuwm0SCwN%2FA1YYzfTEVo8%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0168b830/2649317_1673894848103.mp4-frames/f00002.jpg?Expires=1675235190&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=E6yUw%2B3upbvA3DA8j7vnbVJtG6w%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0168b830/2649317_1673894848103.mp4-frames/f00004.jpg?Expires=1675235190&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=QWs3lJJypT1YDKf34oYmSQCt1mE%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0168b830/2649317_1673894848103.mp4-frames/f00005.jpg?Expires=1675235190&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=aQ%2BGV4Lkg1T8cHnxiACJbCbab5E%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vi5ElPXDLCAh37bOIuzBYAvi-1xwKMS\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";
    static String jsonStr4 = "{\"checksum\":\"f7fde59eb41c80987c4434cf0471de675f1355c68102f71bf104e27265e6dc33\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/0123b830/2204179_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0123b830/2204179_1673894848103.mp4-frames/f00001.jpg?Expires=1675235320&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=5gUXIKNpNSToUs%2FMCgUpBaNqdxI%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0123b830/2204179_1673894848103.mp4-frames/f00002.jpg?Expires=1675235320&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=%2FhBw9Zd1zUS3xLwKDEGWC2X6P3Q%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0123b830/2204179_1673894848103.mp4-frames/f00004.jpg?Expires=1675235320&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=rNLO6lvnSdTaZRRMFN%2FmBK56ACM%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/0123b830/2204179_1673894848103.mp4-frames/f00005.jpg?Expires=1675235320&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=MLq%2BkLi4nHEMrG07T1RxRlMHnYk%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vi2lpwu23oHe057vYB7ajKMA-1xwKOR\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";

    public static void main(String[] args) throws Exception {
//        sha256();
//        sm3();
        ScanVideoResponse scanVideoResponse = JSON.parseObject(jsonStr4, ScanVideoResponse.class);
        System.out.println(scanVideoResponse.getContent());
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String digestHex = sha256.digestHex(ALIYUN_UID + VIDEO_SEED + JSON.toJSONString(JSON.parseObject(scanVideoResponse.getContent())));
        System.out.println("请求密文->" + scanVideoResponse.getChecksum());
        System.out.println("内容加密后->" + digestHex);

        String sha256Str = getSHA256Str(ALIYUN_UID + VIDEO_SEED + scanVideoResponse.getContent());
        System.out.println("内容加密后1->" + sha256Str);

    }

    static void sha256() {
        ScanVideoResponse scanVideoResponse = JSON.parseObject(jsonStr1, ScanVideoResponse.class);
//        System.out.println(scanVideoResponse);
//        ScanVideoResponse.Content contentObject = scanVideoResponse.getContentObject();
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
//        byte[] digest = sha256.digest(ALIYUN_UID + VIDEO_SEED + scanVideoResponse.getContent());
        String encodeContent = sha256.digestHex(ALIYUN_UID + VIDEO_SEED + JSON.toJSONString(JSON.parseObject(scanVideoResponse.getContent())), CharsetUtil.CHARSET_UTF_8);
//        String encodeContent = HexUtil.encodeHexStr(digest);
//        String s = ALIYUN_UID + VIDEO_SEED + JSON.toJSONString(JSON.parseObject(scanVideoResponse.getContent()));
//        System.out.println(s);
//        String encodeContent = s;
        System.out.println("请求密文->" + scanVideoResponse.getChecksum());
        System.out.println("内容加密后->" + encodeContent);
        System.out.println("=======================================");

    }

    static void sm3() throws Exception {
        ScanVideoResponse scanVideoResponse = JSON.parseObject(jsonStr2, ScanVideoResponse.class);
        String s = ALIYUN_UID + VIDEO_SEED + scanVideoResponse.getContent();
        System.out.println(s);
//        String encodeContent = SM3.create().digestHex(s);
//        String encodeContent = sm3bcHex(s.getBytes(StandardCharsets.UTF_8));
//        String encodeContent1 = sm3Hex(s.getBytes(StandardCharsets.UTF_8));
//        String encodeContent2 = hmacSm3Hex(s.getBytes(StandardCharsets.UTF_8));
//        System.out.println("请求密文->" + scanVideoResponse.getChecksum());
//        System.out.println("内容加密后1->" + encodeContent);
//        System.out.println("内容加密后2->" + encodeContent1);
    }


    /***
     * 利用Apache的工具类实现SHA-256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = org.apache.commons.codec.binary.Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

//    static {
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//
//    public static byte[] sm3(byte[] srcData) {
//        SM3Digest sm3Digest = new SM3Digest();
//        sm3Digest.update(srcData, 0, srcData.length);
//        byte[] hash = new byte[sm3Digest.getDigestSize()];
//        sm3Digest.doFinal(hash, 0);
//        return hash;
//    }
//
//    public static String sm3Hex(byte[] srcData) {
//        byte[] hash = sm3(srcData);
//        String hexString = Hex.toHexString(hash);
//        return hexString;
//    }
//
//    public static byte[] hmacSm3(byte[] key, byte[] srcData) {
//        KeyParameter keyParameter = new KeyParameter(key);
//        SM3Digest digest = new SM3Digest();
//        HMac mac = new HMac(digest);
//        mac.init(keyParameter);
//        mac.update(srcData, 0, srcData.length);
//        byte[] hash = new byte[mac.getMacSize()];
//        mac.doFinal(hash, 0);
//        return hash;
//    }
//
//    public static String hmacSm3Hex(byte[] key, byte[] srcData) {
//        byte[] hash = hmacSm3(key, srcData);
//        String hexString = Hex.toHexString(hash);
//        return hexString;
//    }
//
//    public static byte[] sm3bc(byte[] srcData) throws Exception {
//        MessageDigest messageDigest = MessageDigest.getInstance("SM3", "BC");
//        byte[] digest = messageDigest.digest(srcData);
//        return digest;
//    }
//
//    public static String sm3bcHex(byte[] srcData) throws Exception {
//        byte[] hash = sm3bc(srcData);
//        String hexString = Hex.toHexString(hash);
//        return hexString;
//    }

}
