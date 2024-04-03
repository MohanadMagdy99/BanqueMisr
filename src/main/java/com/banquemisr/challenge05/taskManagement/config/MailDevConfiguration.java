package com.banquemisr.challenge05.taskManagement.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail.dev.smtp")
public class MailDevConfiguration {

  @NotBlank
  String userName;
  @NotBlank
  String password;
  @NotNull
  Integer port;
  @NotBlank
  String host;
  @NotBlank
  String timeout;
  @NotBlank
  String writeTimeout;
  @NotBlank
  String connectionTimeout;
  @NotNull
  Boolean enableStartttls;
  @NotNull
  Boolean auth;
  @NotBlank
  String sslTrust;
  @NotBlank
  String supportMail;

}
