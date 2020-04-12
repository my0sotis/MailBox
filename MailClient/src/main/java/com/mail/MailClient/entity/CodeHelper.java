package com.mail.MailClient.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * 编码器
 */
public class CodeHelper {
    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Base64 Encode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String Base64Encode(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * Base64 Encode
     * @param data Target String Data
     * @return Target String
     */
    public static String Base64Encode(String data) {
        return Base64Encode(data.getBytes());
    }

    /**
     * Base64 Decode
     * @param data Target Byte Data
     * @return Target String
     */
    public static String Base64Decode(byte[] data) {
        return new String(decoder.decode(data), StandardCharsets.UTF_8);
    }

    /**
     * Base64 Decode
     * @param data Target String Data
     * @return Target String
     */
    public static String Base64Decode(String data) {
        return Base64Decode(data.getBytes());
    }

    public static String GB18030ToUTF8(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        CharBuffer gb18030 = Charset.forName("GB18030").decode(byteBuffer);
        ByteBuffer utf8 = StandardCharsets.UTF_8.encode(gb18030);
        return new String(utf8.array());
    }

    public static String GB18030ToUTF8(String data) {
        return GB18030ToUTF8(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] UTF8ToGB18030(String data) {
        ByteBuffer gb18030 = Charset.forName("GB18030").encode(data);
        return gb18030.array();
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Base64Decode("uN/VvcGi").getBytes(Charset.forName("GB18030"))));
    }

}
