package org.mai.roombooking.exceptions.handlers;

import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.exceptions.base.BookingException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingServiceExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ProblemDetail handleBookingServiceExceptions(BookingException exception) {
        ProblemDetail detail = null;

        if (exception instanceof BookingConflictException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
            detail.setProperty("exception_description", "Упс! Конфликт бронирования. Временной интервал уже занят.");
        }

        else if (exception instanceof BookingNotFoundException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
            detail.setProperty("exception_description", "Ой! Бронирование не найдено. Возможно, его уже нет или никогда не было.");
        }

        return detail;
    }
}
