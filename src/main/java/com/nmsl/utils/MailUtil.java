package com.nmsl.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * STMP 邮箱 工具类
 * @author paracosm
 * @version 1.0
 * @date 2021/4/24 0:46
 */
public class MailUtil {

    private String host = "smtp.qq.com";
    private int port = 25;
    private String userName = "paracosm@foxmail.com";
    private String password = "";
    private String to = "";

    /**
     * 发送邮件
     * @param subject 标题
     * @param content 内容
     * @param toEmail 接受邮箱
     * @throws GeneralSecurityException
     * @throws MessagingException
     */
    public void sendMail(String subject,String content,String toEmail) throws GeneralSecurityException, MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // ssl 加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        StringBuilder builder = new StringBuilder();
        builder.append("\n"+content);
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress(userName,"paracosm","UTF-8"));
        msg.setSentDate(new Date());

        Transport transport = session.getTransport();
        transport.connect(host, userName, password);

        transport.sendMessage(msg, new Address[] { new InternetAddress(toEmail) });
        transport.close();
    }

    /**
     * 发送文本邮件
     *
     * @throws Exception
     */
    public void sendTextMail() throws Exception
    {
        SimpleEmail mail = new SimpleEmail();
        // 设置邮箱服务器信息
        mail.setSmtpPort(port);
        mail.setHostName(host);
        // 设置密码验证器
        mail.setAuthentication(userName, password);
        // 设置邮件发送者
        mail.setFrom(userName);
        // 设置邮件接收者
        mail.addTo(to);
        // 设置邮件编码
        mail.setCharset("UTF-8");
        // 设置邮件主题
        mail.setSubject("Test Email");
        // 设置邮件内容
        mail.setMsg("this is a test Text mail");
        // 设置邮件发送时间
        mail.setSentDate(new Date());
        // 发送邮件
        mail.send();
    }

    /**
     * 发送Html邮件
     *
     * @throws Exception
     */
    public void sendHtmlMail() throws Exception
    {
        HtmlEmail mail = new HtmlEmail();
        // 设置邮箱服务器信息
        mail.setSmtpPort(port);
        mail.setHostName(host);
        // 设置密码验证器
        mail.setAuthentication(userName, password);
        // 设置邮件发送者
        mail.setFrom(userName);
        // 设置邮件接收者
        mail.addTo(to);
        // 设置邮件编码
        mail.setCharset("UTF-8");
        // 设置邮件主题
        mail.setSubject("Test Email");
        // 设置邮件内容
        mail.setHtmlMsg(
                "<html><body><img src='http://avatar.csdn.net/A/C/A/1_jianggujin.jpg'/><div>this is a HTML email.</div></body></html>");
        // 设置邮件发送时间
        mail.setSentDate(new Date());
        // 发送邮件
        mail.send();
    }

    /**
     * 发送内嵌图片邮件
     *
     * @throws Exception
     */
    public void sendImageMail() throws Exception
    {
        HtmlEmail mail = new HtmlEmail();
        // 设置邮箱服务器信息
        mail.setSmtpPort(port);
        mail.setHostName(host);
        // 设置密码验证器
        mail.setAuthentication(userName, password);
        // 设置邮件发送者
        mail.setFrom(userName);
        // 设置邮件接收者
        mail.addTo(to);
        // 设置邮件编码
        mail.setCharset("UTF-8");
        // 设置邮件主题
        mail.setSubject("Test Email");
        mail.embed(new File("1_jianggujin.jpg"), "image");
        // 设置邮件内容
        String htmlText = "<html><body><img src='cid:image'/><div>this is a HTML email.</div></body></html>";
        mail.setHtmlMsg(htmlText);
        // 设置邮件发送时间
        mail.setSentDate(new Date());
        // 发送邮件
        mail.send();
    }

    /**
     * 发送附件邮件
     *
     * @throws Exception
     */
    public void sendAttachmentMail() throws Exception
    {
        MultiPartEmail mail = new MultiPartEmail();
        // 设置邮箱服务器信息
        mail.setSmtpPort(port);
        mail.setHostName(host);
        // 设置密码验证器
        mail.setAuthentication(userName, password);
        // 设置邮件发送者
        mail.setFrom(userName);
        // 设置邮件接收者
        mail.addTo(to);
        // 设置邮件编码
        mail.setCharset("UTF-8");
        // 设置邮件主题
        mail.setSubject("Test Email");

        mail.setMsg("this is a Attachment email.this email has a attachment!");
        // 创建附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("1_jianggujin.jpg");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName("1_jianggujin.jpg");
        mail.attach(attachment);

        // 设置邮件发送时间
        mail.setSentDate(new Date());
        // 发送邮件
        mail.send();
    }

    /**
     * 发送内嵌图片和附件邮件
     *
     * @throws Exception
     */
    public void sendImageAndAttachmentMail() throws Exception
    {
        HtmlEmail mail = new HtmlEmail();
        // 设置邮箱服务器信息
        mail.setSmtpPort(port);
        mail.setHostName(host);
        // 设置密码验证器
        mail.setAuthentication(userName, password);
        // 设置邮件发送者
        mail.setFrom(userName);
        // 设置邮件接收者
        mail.addTo(to);
        // 设置邮件编码
        mail.setCharset("UTF-8");
        // 设置邮件主题
        mail.setSubject("Test Email");
        mail.embed(new File("1_jianggujin.jpg"), "image");
        // 设置邮件内容
        String htmlText = "<html><body><img src='cid:image'/><div>this is a HTML email.</div></body></html>";
        mail.setHtmlMsg(htmlText);
        // 创建附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("1_jianggujin.jpg");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName("1_jianggujin.jpg");
        mail.attach(attachment);
        // 设置邮件发送时间
        mail.setSentDate(new Date());
        // 发送邮件
        mail.send();
    }

}
