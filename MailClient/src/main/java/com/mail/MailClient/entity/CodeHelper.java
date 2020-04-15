package com.mail.MailClient.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
     * Convert UTF-8 To GB18030
     * @param data Source data String
     * @return Target GB18030 bytes
     */
    public static byte[] UTF8ToGB18030(String data) {
        ByteBuffer gb18030 = Charset.forName("GB18030").encode(data);
        return gb18030.array();
    }

    private final static byte TAB 	= 0x09;     // /t
    private final static byte LF 		= 0x0A;     // /n
    private final static byte CR 		= 0x0D;     // /r
    private final static byte SPACE 	= 0x20;		// ' '
    private final static byte EQUALS 	= 0x3D;		// '='

    private final static byte LIT_START = 0x21;
    private final static byte LIT_END	= 0x7e;

    private final static int MAX_LINE_LENGTH = 76;

    private static int mCurrentLineLength = 0;

    /**
     * A method to decode quoted printable encoded data.
     * It overrides the same input byte array to save memory. Can be done
     * because the result is surely smaller than the input.
     * @param qp a byte array to decode.
     * @return the length of the decoded array.
     */
    public static int QPConvertCharset(byte [] qp) {
        int qplen = qp.length;
        int retlen = 0;

        for (int i=0; i < qplen; i++) {
            // Handle encoded chars
            if (qp[i] == '=') {
                if (qplen - i > 2) {
                    // The sequence can be complete, check it
                    if (qp[i+1] == CR && qp[i+2] == LF) {
                        // soft line break, ignore it
                        i += 2;
                        continue;

                    } else if (isHexDigit(qp[i+1]) && isHexDigit(qp[i+2]) ) {
                        // convert the number into an integer, taking
                        // the ascii digits stored in the array.
                        qp[retlen++]=(byte)(getHexValue(qp[i+1])*16
                                + getHexValue(qp[i+2]));

                        i += 2;
                        continue;

                    }
                }
                // In all wrong cases leave the original bytes
                // (see RFC 2045). They can be incomplete sequence,
                // or a '=' followed by non hex digit.
            }
            // RFC 2045 says to exclude control characters mistakenly
            // present (unencoded) in the encoded stream.
            // As an exception, we keep unencoded tabs (0x09)
            if( (qp[i] >= 0x20 && qp[i] <= 0x7f) ||
                    qp[i] == TAB || qp[i] == CR || qp[i] == LF) {
                qp[retlen++] = qp[i];
            }
        }
        return retlen;
    }

    private static boolean isHexDigit(byte b) {
        return ( (b>=0x30 && b<=0x39) || (b>=0x41&&b<=0x46) );
    }

    private static byte getHexValue(byte b) {
        return (byte)Character.digit((char)b, 16);
    }

    /**
     *
     * @param qp Byte array to decode
     * @param enc The character encoding of the returned string
     * @return The decoded string.
     */
    public static String QPConvertCharset(byte[] qp, String enc) {
        int len= QPConvertCharset(qp);
        try {
            return new String(qp, 0, len, enc).replaceAll("_", " ");
        } catch (UnsupportedEncodingException e) {
            return new String(qp, 0, len).replaceAll("_", " ");
        }
    }

    /**
     * A method to encode data in quoted printable
     * @param content The string to be encoded
     * @param enc The character encoding of the content string
     * @return The encoded string. If the content is null, return null.
     */
    public static String QPEncode(String content, String enc) {
        if (content == null) {
            return null;
        }
        byte[] str;
        try {
            str = content.getBytes(enc);
        } catch (UnsupportedEncodingException e) {
            str = content.getBytes();
        }
        return QPEncode(str);
    }

    /**
     * A method to encode data in quoted printable
     * @param content The byte array of the string to be encoded
     * @return The encoded string. If the content is null, return null.
     */
    public static String QPEncode(byte[] content) {
        if (content == null)
            return null;

        StringBuilder out = new StringBuilder();

        mCurrentLineLength = 0;
        int requiredLength;

        for (byte c : content) {
            if (c >= LIT_START && c <= LIT_END && c != EQUALS) {
                requiredLength = 1;
                checkLineLength(requiredLength, out);
                out.append((char) c);
            } else {
                requiredLength = 3;
                checkLineLength(requiredLength, out);
                out.append('=');
                out.append(String.format("%02X", c));
            }
        }
        return out.toString();
    }

    private static void checkLineLength(int required, StringBuilder out) {
        if (required + mCurrentLineLength > MAX_LINE_LENGTH - 1) {
            out.append("=/r/n");
            mCurrentLineLength = required;
        }  else
            mCurrentLineLength += required;
    }

    public static void main(String[] args) {
        System.out.println(CodeHelper.
                QPConvertCharset("=E7=82=B9=E5=87=BB=E8=BF=99=E9=87=8C=E5=8F=96=E6=B6=88=E8=AE=A2=E9=98=85".
                        getBytes(), "utf-8"));
    }
}
