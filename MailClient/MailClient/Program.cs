using System;
using MailClient.Utils;

namespace MailClient
{
    class Program
    {
        static void Main(string[] args)
        {
            string x = "Hello World!";
            Console.WriteLine(Base64.Base64Encode(x));
            x = Base64.Base64Encode(x);
            Console.WriteLine(Base64.Base64Decode(x));
        }
    }
}
