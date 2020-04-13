package com.mail.MailClient.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 编码器
 */
public class CodeHelper {
    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Base64 Encode
     * @param data Target Byte Data
     * @return Target bytes Array
     */
    public static byte[] Base64Encode(byte[] data) {
        return encoder.encode(data);
    }

    /**
     * Base64 Encode
     * @param data Target String Data
     * @return Target Bytes Array
     * @implSpec
     */
    public static byte[] Base64Encode(String data) {
        return Base64Encode(data.getBytes());
    }

    /**
     * Base64 Decode
     * @param data Target Byte Data
     * @return Target String
     */
    public static byte[] Base64Decode(byte[] data) {
        return decoder.decode(data);
    }

    /**
     * Base64 Decode
     * @param data Target String Data
     * @return Target String
     */
    public static byte[] Base64Decode(String data) {
        return Base64Decode(data.getBytes());
    }

    /**
     * Base64 Convert Charset
     * @param data Source String
     * @param charset Target Charset
     * @return Decoded String
     */
    public static String Base64ConvertCharset(String data, String charset) {
        return new String(Base64Decode(data), Charset.forName(charset));
    }

    /**
     * QP Convert Charset
     * @param data Source String
     * @param charset Target Charset
     * @return Decoded String
     * @throws UnsupportedEncodingException Unsupported Encoding
     */
    public static String QPConvertCharset(String data, String charset) throws UnsupportedEncodingException {
        return URLDecoder.decode(data.replaceAll("=", "%").replaceAll("_", " "), charset);
    }

    public static byte[] UTF8ToGB18030(String data) {
        ByteBuffer gb18030 = Charset.forName("GB18030").encode(data);
        return gb18030.array();
    }
}
