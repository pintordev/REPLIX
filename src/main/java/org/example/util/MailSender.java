package org.example.util;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    public String emailCertification(String email, String Keyword) {
        String certifiacationUserEmail = email;
        String certificationKeyword = Keyword;
        String certificationCode = (int) (Math.random() * 999999) + 1 + "";
        certificationCode = "0".repeat(6 - certificationCode.length()) + certificationCode;
        String osName = System.getProperty("os.name");
        String appPw = null;
        if (osName.startsWith("Windows")) {
            appPw = "yrndtmktwljwowdu";
        }
        if (osName.startsWith("Mac")) {
            appPw = "msfupdeesjnjqbxr";
        }
        String finalAppPw = appPw;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("replix.noreply@gmail.com", finalAppPw);
            }
        });

        String receiver = certifiacationUserEmail;
        String title = "REPLIX " + certificationKeyword + " 인증메일입니다";
        String content = "<span>REPLIX " + certificationKeyword + "인증을 진행합니다</span><br>";
        content += "<span>아래의 인증 코드를 입력해주세요</span>";
        content += "<h2 style='color:indigo'>" + certificationCode + "</h2>";
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("replix.noreply@gmail.com", "REPLIX Administrator", "utf-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return certificationCode;
    }
}
