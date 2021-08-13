package com.cttexpress.utils;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1;

public class CryptoUtils {

    public static String generateClientId() {
        Random random = new Random(System.currentTimeMillis());
        String seed = new BigInteger(50, random).toString(32) + UUID.randomUUID().toString();
        return new DigestUtils(MD5).digestAsHex(seed);
    }

    public static String generateClientSecret() {
        Random random = new Random(System.currentTimeMillis());
        String seed = new BigInteger(50, random).toString(32) + UUID.randomUUID().toString();
        return new DigestUtils(SHA_1).digestAsHex(seed);
    }
}
