package com.banquemisr.challenge05.taskManagement.config;


import java.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {
  private final MailDevConfiguration mailDevConfiguration;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailDevConfiguration.getHost());
    mailSender.setPort(mailDevConfiguration.getPort());
    mailSender.setUsername(mailDevConfiguration.getUserName());
    mailSender.setPassword(mailDevConfiguration.getPassword());
    Properties properties = getProperties();
    mailSender.setJavaMailProperties(properties);

    return mailSender;
  }

  private Properties getProperties() {
    Properties properties = new Properties();
    properties.put("mail.smtp.ssl.trust", mailDevConfiguration.getSslTrust());
    properties.put("mail.smtp.auth", mailDevConfiguration.getAuth());
    properties.put("mail.smtp.starttls.enable", mailDevConfiguration.getEnableStartttls());
    properties.put("mail.smtp.connectiontimeout", mailDevConfiguration.getConnectionTimeout());
    properties.put("mail.smtp.timeout", mailDevConfiguration.getTimeout());
    properties.put("mail.smtp.writetimeout", mailDevConfiguration.getWriteTimeout());
    return properties;
  }
}
