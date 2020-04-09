package com.mail.MailClient.entity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Helper {
    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Base64 Encode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String Encode(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * Base64 Encode
     * @param data Target String Data
     * @return Target String
     */
    public static String Encode(String data) {
        return Encode(data.getBytes());
    }

    /**
     * Base64 Decode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String Decode(byte[] data) {
        return new String(decoder.decode(data), StandardCharsets.UTF_8);
    }

    /**
     * Base64 Decode
     * @param data Target String Data
     * @return Target String
     */
    public static String Decode(String data) {
        return Decode(data.getBytes());
    }
}
