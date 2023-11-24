package com.showroom.ServiceImpl;

import com.showroom.Service.EmailMailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailMailServiceImpl implements EmailMailService {

//    @Autowired
//    private MailSender simpleMailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("#{'${spring.mail.cc}'.split(',')}")
    private String[] ccEmails;

    @Override
    @Async
    public void sendMail(String toMail, String subject, String message,byte[] attachment) {
//        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
//        simpleMailMessage.setFrom("roy71ston@gmail.com");
//        simpleMailMessage.setTo(toMail);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(message);
//        simpleMailSender.send(simpleMailMessage);

        // create mine message
        MimeMessage mimeMailMessage=javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMailMessage,true);
            mimeMessageHelper.setFrom(emailFrom);
            mimeMessageHelper.setTo(toMail);
            mimeMessageHelper.setSubject(subject);
            String confirmOrderHtmlTemplate= """
                    <div style="background: aliceblue;font-family: 'Poppins', sans-serif; width: 500px;margin: auto;">
                        <h1
                        style="  font-family: 'Cairo Play', sans-serif;
                        font-style: italic;background-color: #3676ee;color: white;padding: 10px;">MyDrive</h1>
                        <h2 style="text-align: center;padding-bottom: 30px;">Your order has been Confirmed.</h2>
                    </div>
                    """;
            mimeMessageHelper.setText(confirmOrderHtmlTemplate,true);

//            ccEmails.forEach(mail->{
//                try {
//                    mimeMailMessage.addRecipients(Message.RecipientType.CC, mail);
//                } catch (MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
            mimeMessageHelper.addAttachment("order-invoice.pdf", new ByteArrayResource(attachment));
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            log.error("Error in sendMail {}",e);
        }

    }
}
