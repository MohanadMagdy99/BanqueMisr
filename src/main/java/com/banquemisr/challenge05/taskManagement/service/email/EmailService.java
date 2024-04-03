package com.banquemisr.challenge05.taskManagement.service.email;

import com.banquemisr.challenge05.taskManagement.config.MailDevConfiguration;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;
  private final MailDevConfiguration mailDevConfiguration;


  public void sendEmail(String email, String employeeName, String taskName)
      throws MessagingException {
    Context context = new Context();
    context.setVariable("employeeName", employeeName);
    context.setVariable("taskName", taskName);

    var subject = "Please review important!";
    log.info("sending email={} subject='{}'", LogUtils.safeEmail(email), subject);
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
    helper.setTo(email);
    helper.setFrom(mailDevConfiguration.getSupportMail());
    helper.setBcc(mailDevConfiguration.getSupportMail());
    helper.setSubject(subject);
    String emailContent = templateEngine.process("important-update-mail", context);
    helper.setText(emailContent, true);
    javaMailSender.send(mimeMessage);
  }
}
