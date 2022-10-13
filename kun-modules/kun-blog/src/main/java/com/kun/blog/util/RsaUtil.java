package com.kun.blog.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import com.kun.common.core.exception.BizException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * rsa加解密工具类
 *
 * @author gzc
 * @since 2022/10/11 2:17
 **/
public class RsaUtil {

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 编码格式
     */
    private static final String CHAR_SET = "UTF-8";
    /**
     * 秘钥KEY工厂
     */
    private static KeyFactory keyFactory;
//	public static String PrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK+H0YHxP/AvH1I9yvewyqmYtoe3NfJeI0nhmdp5EHErWmpghSk2qaq+tk+BXNskxtJ2079Mwh039GO+sl+iJ7hwfPX0TlI0jMZQxtcYyEpOQRwgDVfWfkTucO4fkTyIzo1MSgUxoKbN0+pYWHcGKELPlD3vOrueCJLz76qWBtQPAgMBAAECgYBj8e21E2zYkbw/07dx+VQr5SxpckRhUIC/XJmB8FUQWyMMVxD7Ooi5FAYylvIMRZB/3fELh+UvReD9umNOWMJMKrV/PKbx0W3nLvodEQ93MfPDbpPmylNb9EBkDxvSpCw8Gei7JvIL5/WI+qnlqxZExb9Bze62je/f7Z8xt+qdgQJBAPTg/VmQwIKIAjwRVi7a5Ld/UlcOM8VAuxzkKflK+uX606s0DkEp2l/N7iJJdATeY9y9mRZwbTydAL5UYNS8+FECQQC3gJJC1L12Ms7qB2NdeqKw42TzH6ZC1utuEp9vvFMIq2Ju+OWgRbbDy4ddXMAtvnFLAdbyTp/1iB/u+oqa3k5fAkEAnncCO/WKPm4ZVBm79bI9E+nWtPNB2UHcVAPqjaJR3oWEeGPFXbHh2OGAWrvB0my/ntcqu/ShG/pVwtUDnGd1IQJAHg+B5lDTeLl6C/yJ2pZscG3P68QTiH+Msct7MuK294Sb63H6q/a/qfN9iV3YXaYFCTST8b3PlnlmQc/pRNWGIQJABrVuzW8P8nE3i5VoVQk/AB0uq9bngmDk+hbWsvwfibdoJJPvMN7sH3+czdaY3eV2ojTdSRo+HRV0S/mIq8B17g==";
//	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvh9GB8T/wLx9SPcr3sMqpmLaHtzXyXiNJ4ZnaeRBxK1pqYIUpNqmqvrZPgVzbJMbSdtO/TMIdN/RjvrJfoie4cHz19E5SNIzGUMbXGMhKTkEcIA1X1n5E7nDuH5E8iM6NTEoFMaCmzdPqWFh3BihCz5Q97zq7ngiS8++qlgbUDwIDAQAB";

    /**
     *初始化KEY工厂
     */
    static {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new BizException("初始化keyFactor失败", e);
        }
    }

    /**
     * @param dataStr        原文
     * @param signPrivateKey 签名私钥
     * @description: 使用签名私钥对数据进行签名
     * @return: java.lang.String
     * @author: gzc
     * @date: 2021-12-1 16:47
     */
    public static String sign(String dataStr, String signPrivateKey) {
        try {
            byte[] dataStrBytes = dataStr.getBytes(CHAR_SET);
            // 私钥Base64解密
            byte[] keyBytes = Base64.decode(signPrivateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateK);
            signature.update(dataStrBytes);
            // 返回base64加密后的数据
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            throw new BizException("签名失败", e);
        }
    }

    /**
     * @param dataStr       原文
     * @param signPublicKey 签名公钥
     * @param sign          签名值
     * @description: 校验签名
     * @return: boolean
     * @author: gzc
     * @date: 2021-12-1 16:50
     */
    public static void signVerify(String dataStr, String sign, String signPublicKey) {
        try {
            // Base64解密原文
            byte[] keyBytes = Base64.decode(signPublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicK);
            signature.update(dataStr.getBytes(CHAR_SET));
            // Base64解密签名值
            byte[] signDecode = Base64.decode(sign);
            boolean verify = signature.verify(signDecode);
            if (!verify) {
                throw new BizException("验签失败, 结果为false");
            }
        } catch (Exception e) {
            throw new BizException("校验签名失败", e);
        }
    }

    /**
     * @param dataStr          原文
     * @param contentPublicKey 公钥
     * @description: 原文公钥进行分段加密
     * @return: java.lang.String
     * @author: gzc
     * @date: 2021-12-1 16:09
     */
    public static String encryptByPublicKey(String dataStr, String contentPublicKey) throws BizException {
        ByteArrayOutputStream baos = null;
        try {
            byte[] data = dataStr.getBytes(CHAR_SET);
            // 对公钥解密
            Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(contentPublicKey);

            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);
            int inputLen = data.length;
            baos = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                baos.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = baos.toByteArray();
            return Base64.encode(encryptedData);
        } catch (Exception e) {
            throw new BizException("原文公钥分段加密失败", e);
        } finally {
            IoUtil.close(baos);
        }
    }

    /**
     * @param dataStr           原文
     * @param contentPrivateKey 私钥
     * @description: 原文私钥进行分段加密
     * @return: java.lang.String
     * @author: gzc
     * @date: 2021-12-1 16:10
     */
    public static String encryptByPrivateKey(String dataStr, String contentPrivateKey) throws BizException {
        ByteArrayOutputStream baos = null;
        try {
            byte[] data = dataStr.getBytes(CHAR_SET);
            // 获取原文私钥KEY数组
            byte[] bytes = contentPrivateKey.getBytes(CHAR_SET);
            byte[] keyBytes = Base64.decode(bytes);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            baos = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                baos.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = baos.toByteArray();
            return Base64.encode(encryptedData);
        } catch (Exception e) {
            throw new BizException("原文私钥进行分段加密失败", e);
        } finally {
            IoUtil.close(baos);
        }
    }

    /**
     * @param dataStr           加密的原文
     * @param contentPrivateKey 私钥
     * @description: 原文私钥分段解密
     * @return: java.lang.String
     * @author: gzc
     * @date: 2021-12-1 16:10
     */
    public static String decryptByPrivateKey(String dataStr, String contentPrivateKey) throws BizException {
        ByteArrayOutputStream baos = null;
        try {
            // Base64解密
            byte[] encryptedData = Base64.decode(dataStr);
            // 获取原文私钥KEY
            Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(contentPrivateKey);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);
            int inputLen = encryptedData.length;
            baos = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                baos.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = baos.toByteArray();
            return new String(decryptedData, CHAR_SET);
        } catch (Exception e) {
            throw new BizException("原文私钥分段解密失败", e);
        } finally {
            IoUtil.close(baos);
        }
    }

    /**
     * @param dataStr          加密的原文
     * @param contentPublicKey 公钥
     * @description: 原文公钥钥分段解密
     * @return: java.lang.String
     * @author: gzc
     * @date: 2021-12-1 16:11
     */
    public static String decryptByPublicKey(String dataStr, String contentPublicKey) throws BizException {
        ByteArrayOutputStream baos = null;
        try {
            // base64解密
            byte[] encryptedData = Base64.decode(dataStr);
            // 公钥base64解密
            byte[] keyBytes = Base64.decode(contentPublicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            baos = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                baos.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = baos.toByteArray();
            return new String(decryptedData, CHAR_SET);
        } catch (Exception e) {
            throw new BizException("原文公钥钥分段解密失败", e);
        } finally {
            IoUtil.close(baos);
        }
    }

    /**
     * 根据Base64加密的key字符串获取私钥
     */
    public static Key getPrivateKeyFromBase64KeyEncodeStr(String keyStr) {
        try {

            byte[] keyBytes = Base64.decode(keyStr);
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new BizException("获取原文私钥Key失败", e);
        }
    }

    /**
     * 获取base64加密后的字符串的原始公钥
     */
    public static Key getPublicKeyFromBase64KeyEncodeStr(String keyStr) {
        try {
            byte[] keyBytes = Base64.decode(keyStr);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            throw new BizException("获取原文公钥Key失败", e);
        }
    }

//	/**
//	 * 公钥加密方法
//	 *
//	 * @param dataStr 要加密的数据
//	 * @param dataStr 公钥base64字符串
//	 * @return 加密后的base64字符串
//	 * @throws Exception
//	 */
//	public static String encryptPublicKey(String dataStr) throws Exception {
//		byte[] data = dataStr.getBytes();
//		// 对公钥解密
////		Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(publicKey);
//		Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(InitRSAKeyData.getContentPublicKey());
//		// 对数据加密
//		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);
//		byte[] encodedData = cipher.doFinal(data);
//		return Base64.encode(encodedData);
//	}

    //
//	/**
//	 * 解密方法
//	 *
//	 * @param dataStr 要解密的数据
//	 * @return 解密后的原数据
//	 * @throws Exception
//	 */
//	public static String decrypt(String dataStr) throws Exception {
//		//对私钥解密
////		Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(PrivateKey);
//		Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(InitRSAKeyData.getContentPrivateKey());
//		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//		cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);
//		byte[] decodedData = cipher.doFinal(Base64.decode(dataStr));
//		return new String(decodedData, "charSet");
//	}

    /**
     * 随机生成密钥对
     */
    public static void main(String[] args) throws Exception {
//        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//        // 初始化密钥对生成器，密钥大小为96-1024位
//        keyPairGen.initialize(1024, new SecureRandom());
//        // 生成一个密钥对，保存在keyPair中
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        // 得到私钥
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        // 得到公钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        // 得到公钥字符串
//        String publicKeyString = Base64.encode(publicKey.getEncoded());
//        // 得到私钥字符串
//        String privateKeyString = Base64.encode(privateKey.getEncoded());
//        System.out.println("公钥publicKeyString->\n" + publicKeyString);
//        System.out.println("私钥privateKeyString->\n" + privateKeyString);
    }
}
