using System;
using System.Text;

namespace MailClient.Utils
{
    public sealed class Base64
    {
        /// <summary>
        /// Base64加密
        /// </summary>
        /// <param name="encodeType">加密使用的编码方式</param>
        /// <param name="source">待加密的原文</param>
        /// <returns>加密后的字符串</returns>
        public static string Base64Encode(Encoding encodeType, string source)
        {
            byte[] bytes = encodeType.GetBytes(source);
            string encode;
            try
            {
                encode = Convert.ToBase64String(bytes);
            }
            catch
            {
                encode = source;
            }
            return encode;
        }

        /// <summary>
        /// Base64加密，采用UTF-8编码加密
        /// </summary>
        /// <param name="source">待加密的明文</param>
        /// <returns>加密后的字符串</returns>
        public static string Base64Encode(string source)
        {
            return Base64Encode(Encoding.UTF8, source);
        }

        /// <summary>
        /// Base64解密
        /// </summary>
        /// <param name="encodeType">解密采用的编码方式</param>
        /// <param name="source">待解密的密文</param>
        /// <returns>解密后的字符串</returns>
        public static string Base64Decode(Encoding encodeType, string source)
        {
            byte[] bytes = Convert.FromBase64String(source);
            string decode;
            try
            {
                decode = encodeType.GetString(bytes);
            }
            catch
            {
                decode = source;
            }
            return decode;
        }

        /// <summary>
        /// Base64解密，采用utf8编码方式解密
        /// </summary>
        /// <param name="source">待解密的密文</param>
        /// <returns>解密后的字符串</returns>
        public static string Base64Decode(string source)
        {
            return Base64Decode(Encoding.UTF8, source);
        }
    }
}
