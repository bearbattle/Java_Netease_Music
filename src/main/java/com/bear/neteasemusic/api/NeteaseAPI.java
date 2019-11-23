package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.RandomUtils;

import javax.crypto.Cipher;
import javax.crypto.ExemptionMechanismException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NeteaseAPI {
    private static final int aesKeySize = 16;
    private static byte[] defaultIV = "0102030405060708".getBytes(US_ASCII);
    private static final byte[] presetKey = "0CoJUm6Qyw8W8jud".getBytes(US_ASCII);
    private static final String pubKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";
    private static Cipher rsaCipher = null;
    private static final byte[] base62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456790".getBytes(UTF_8);

    static {
        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(Base64.getDecoder().decode(pubKeyString));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static byte[] rsaEncrypt(byte[] buffer) {
        byte[] reversed = ArrayUtils.clone(buffer);
        ArrayUtils.reverse(reversed);
        try {
            return rsaCipher.doFinal(reversed);
        } catch (Exception ex) {
            throw new RuntimeException("This is gonna never happen");
        }
    }

    private static byte[] aesEncrypt(byte[] buffer, byte[] key, byte[] ivBytes) {
        SecretKeySpec skeySpec = null;
        Cipher cipher = null;
        try {
            skeySpec = new SecretKeySpec(key, "AES");
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            int padLength = aesKeySize - (buffer.length % aesKeySize);
            byte[] pad = new byte[padLength];
            Arrays.fill(pad, (byte) padLength);
            byte[] padded = ArrayUtils.addAll(buffer, pad);
            byte[] encrypted = cipher.doFinal(padded);
            return Base64.getEncoder().encode(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("This is gonna never happen");
        }
    }

    private static String toPercentEncoding(byte[] shit) {
        return new String(URLCodec.encodeUrl(new BitSet(256), shit));
    }

    private static String weapiEncrypt(String t) {
        byte[] text = t.getBytes(UTF_8);
        byte[] secretKey = new byte[aesKeySize];
        for (int i = 0; i < aesKeySize; i++) {
            secretKey[i] = base62[RandomUtils.nextInt(0, base62.length)];
        }
        byte[] result1 = aesEncrypt(text, presetKey, defaultIV);
        byte[] result2 = aesEncrypt(result1, secretKey, defaultIV);
        byte[] rsaResult = Hex.encodeHexString(rsaEncrypt(secretKey)).getBytes(US_ASCII);
        return String.format("params=%s&encSecKey=%s", toPercentEncoding(result2), toPercentEncoding(rsaResult));
    }

    private OkHttpClient httpClient;
    private CookieJar cookieJar;

    public NeteaseAPI() {
        cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        cookieJar.saveFromResponse(HttpUrl.parse("http://music.163.com/"), new ArrayList<Cookie>(Arrays.asList(Cookie.parse(HttpUrl.get("http://music.163.com/"), "os=pc"))));
        httpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }


    public JSONObject postRequest(ApiRequest requestObj) throws IOException {
        String params = weapiEncrypt(JSON.toJSONString(requestObj));
        var body = RequestBody.create(params, MediaType.get("application/x-www-form-urlencoded"));
        Request request = new Request.Builder()
                .url("http://music.163.com/weapi/" + requestObj.getPath())
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            var json = JSONObject.parseObject(response.body().string());
            return json;
        }
    }

}
