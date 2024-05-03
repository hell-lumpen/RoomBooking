//package org.mai.roombooking.serviceTest;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Assertions;
//
//import org.mai.roombooking.exceptions.BookingNotFoundException;
//import org.mai.roombooking.services.BookingService;
//import org.mai.roombooking.services.CalendarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest
//public class BookingServiceGetTests {
//
//    private final BookingService bookingService;
//    private final CalendarService calendarService;
//
//    @Autowired
//    public BookingServiceGetTests(BookingService bookingService, CalendarService calendarService) {
//        this.bookingService = bookingService;
//        this.calendarService = calendarService;
//    }
//
//    @Test
//    public void getAll() {
//        Assertions.assertEquals(12, bookingService.getAll().size());
//    }
//    @Test
//    public void getInTimeRange() {
//        Assertions.assertEquals(3, bookingService.getBookingsInTimeRange(
//                LocalDateTime.of(2023, 10,24,0,0),
//                LocalDateTime.of(2023, 10,24,23,59)).stream()
//                .map((pair) -> (long) pair.getBookings().size())
//                .mapToInt(Long::intValue)
//                .sum());
//    }
//
//    @Test
//    public void getById() throws BookingNotFoundException {
//        Assertions.assertEquals("ДОД маи", bookingService.getBookingById(1L).getTitle());
//    }
//
//    @Test
//    public void get() {
//        calendarService.getAllBookings();
//    }
//}
