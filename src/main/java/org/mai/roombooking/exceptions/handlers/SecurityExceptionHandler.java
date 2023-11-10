package org.mai.roombooking.exceptions.handlers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityExceptions(Exception exception) throws Exception {
        ProblemDetail detail;
        if (exception instanceof BadCredentialsException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            detail.setProperty("assess_denied_reason", "Authentication failed");
            detail.setProperty("exception_description", "Ошибка аутентификации! Неверные учетные данные.");
        }

        else if (exception instanceof AccessDeniedException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            detail.setProperty("assess_denied_reason", "Authorization error. Not enough permissions");
            detail.setProperty("exception_description", "Ошибка авторизации! Недостаточно прав для доступа к запрашиваемому ресурсу.");
        }

        else if (exception instanceof LockedException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            detail.setProperty("assess_denied_reason", "Authentication failed. Your account has been blocked");
            detail.setProperty("exception_description", "Ошибка аутентификации! Ваш аккаунт был заблокирован администратором.");
        }

        else
            throw exception;

        return detail;
    }
}
