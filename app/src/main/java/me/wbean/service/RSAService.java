package me.wbean.service;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAService {

    // 使用SHA-256摘要算法对数据进行处理
    private static final String HASH_ALGORITHM = "SHA-256";

    // 使用PKCS#1 v1.5填充方案
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static KeyPair generateKey(int keyLength) throws NoSuchAlgorithmException {
        // 获取秘钥生成器
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(keyLength);
        // 生成秘钥并返回
        return keyGenerator.genKeyPair();
    }

    public static KeyPair generateKey() throws NoSuchAlgorithmException {
        return generateKey(2048);
    }

    // 对数据进行签名
    public static String sign(String message, String privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(decodePrivateKey(privateKey));
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        return encodeBase64(signatureBytes);
    }

    // 对签名进行验证
    public static boolean verify(String message, String signature, String publicKey) throws Exception {
        Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
        verifier.initVerify(decodePublicKey(publicKey));
        verifier.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = decodeBase64(signature);
        return verifier.verify(signatureBytes);
    }

    // 对byte[]数组进行Base64编码
    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    // 对Base64编码的字符串进行解码
    public static byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    // 对公钥进行编码
    public static String encodePublicKey(PublicKey publicKey) {
        return encodeBase64(publicKey.getEncoded());
    }

    // 对私钥进行编码
    public static String encodePrivateKey(PrivateKey privateKey) {
        return encodeBase64(privateKey.getEncoded());
    }

    // 对公钥进行解码
    public static PublicKey decodePublicKey(String publicKeyBase64) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] publicKeyBytes = decodeBase64(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 对私钥进行解码
    public static PrivateKey decodePrivateKey(String privateKeyBase64) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] privateKeyBytes = decodeBase64(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
