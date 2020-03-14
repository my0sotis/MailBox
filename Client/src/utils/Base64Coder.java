package utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Coder {
    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Base64 Encode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String EncodeBase64(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * Base64 Decode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String DecodeBase64(byte[] data) {
        return new String(decoder.decode(data), StandardCharsets.UTF_8);
    }
}
