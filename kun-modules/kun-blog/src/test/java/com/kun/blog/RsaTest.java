package com.kun.blog;

import com.kun.blog.util.RsaUtil;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/11 2:23
 **/
public class RsaTest {

    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfY8P8qLYlMjd2cClF8P6MX2gJ70pbqlS5H+MHhag0yQ2TkQx/LM/okLAE2pkabl8tHllssn8PlJBWJuXdPX6W64jTOxGqifRE0ly1raw2UHYQx26n7I7DYXdSa8m+A38uCucV1tzpmtb37dDjGU5aN6zcX2x7owxO6jhZBSLCrwIDAQAB";
    private final static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN9jw/yotiUyN3ZwKUXw/oxfaAnvSluqVLkf4weFqDTJDZORDH8sz+iQsATamRpuXy0eWWyyfw+UkFYm5d09fpbriNM7EaqJ9ETSXLWtrDZQdhDHbqfsjsNhd1Jryb4Dfy4K5xXW3Oma1vft0OMZTlo3rNxfbHujDE7qOFkFIsKvAgMBAAECgYAmOH6W4xLPCGl+5S/anKIgyE4XrQ5u4pS+W9tPNSoTZfbhsaaOt88WvnKvUetyH0I/iPrJ4zxkIQoVA3C+yr+2pt6yGWjJqvxSaT6piFZx9x6DljCVonH3jFx72IpqceFgVkdeWS8dszfaAegoVIIAuKG7/RCXnyruLLDMIdNVKQJBAPBJ3pe6spWAzSahIE3y8xazQmBSB3KZ3Rh9IOFVZsUNfHJngifT7JcubwHGmRw6JMRKnj46vF1GnyYW+57mGAMCQQDt/wfQOjNAUUfrRMz9R0j9KFFpae94QOFM7SExi8PvEOjgZhVlE3hiKiOmm8Kt04BjpJ67D0HYspekjmKdBRjlAkB8bqR4ilQEm6ihQntKIm/D55yN6Ky2qfsWvWWTivgnPy6cyAFcwqAYQhF2J/QN7AYyKDn+G8iDvRiZTvmKdbklAkEAh0tbnbv4Nq/+Oni6L6G+lGu/HDXktG/tpFWJcSkLEXSDbk2aDAOtg+CAs84INdjuO7bxpVVfEVk1a46l8K1dIQJAM9jHr/C6qAbgKJT0ztw+Wy3QoML9kOkLFQ+yPSPN5eGajmTngw4PrPaAMg1Wou5rzjxzg3O86sKKNOnHUtDSgA==";

    public static void main(String[] args) {
        String encode = RsaUtil.encryptByPublicKey("admin123", publicKey);
        System.out.println(encode);
        String decode = RsaUtil.decryptByPrivateKey(encode, privateKey);
        System.out.println(decode);
    }


}
