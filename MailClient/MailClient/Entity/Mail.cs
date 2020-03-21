using System;
using System.Collections.Generic;
using System.Text;

namespace MailClient.Entity
{
    public class Mail
    {
        private string subject = "";    // 主题
        private string sender = "";     // 发件人
        private string time = "";       // 时间
        private List<string> stringList = new List<string>();
        private string content;         // 内容
        private string receiver;        // 收件人

        public Mail() { }

        public Mail(string _subject, string _sender, List<string> _stringList,
                    string _content, string _receiver)
        {
            subject = _subject;
            sender = _sender;
            stringList = _stringList;
            content = _content;
            receiver = _receiver;
        }
    }
}
