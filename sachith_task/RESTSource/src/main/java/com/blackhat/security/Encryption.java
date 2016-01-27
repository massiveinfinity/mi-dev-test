package com.blackhat.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Sachith Dickwela
 */
public class Encryption {

    public static enum Algorithm {
        SHA_1, SHA_2, SHA_256, SHA_512, MD5
    }

    public static String encrypt(Algorithm method, String phrase)
            throws NoSuchAlgorithmException {
        String algorithm;
        switch (method) {
            case SHA_1:
                algorithm = "SHA-1";
                break;
            case SHA_2:
                algorithm = "SHA-2";
                break;
            case SHA_256:
                algorithm = "SHA-256";
                break;
            case SHA_512:
                algorithm = "SHA-512";
                break;
            case MD5:
                algorithm = "MD5";
                break;
            default:
                algorithm = "MD5";
                break;
        }
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] buffer = digest.digest(phrase.getBytes(Charset.forName("UTF-8")));
        return toHexaString(buffer);
    }

    private static String toHexaString(byte[] buffer) {
        StringBuilder builder = new StringBuilder();
        for (byte bit : buffer) {
            builder.append(String.format("%x", bit));
        }
        return builder.toString();
    }
}
