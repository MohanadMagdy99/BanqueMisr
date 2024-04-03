package com.banquemisr.challenge05.taskManagement.service.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogUtils {

  private static final String EMAIL_MASKING_REGEX = "(?<=.{3}).(?=.*@)";

  public static String safeEmail(String email) {
    if (email == null) {
      return "NULL";
    }
    return email.replaceAll(EMAIL_MASKING_REGEX, "*");
  }

}
