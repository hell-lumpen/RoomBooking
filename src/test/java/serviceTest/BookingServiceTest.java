package org.mai.roombooking.serviceTest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.exceptions.BookingException;
import org.mai.roombooking.repositories.UserRepository;
import org.mai.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

@SpringBootTest
public class BookingServiceTest {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    @Autowired
    BookingServiceTest(BookingService bookingService1, UserRepository userRepository1) {
        this.bookingService = bookingService1;
        this.userRepository = userRepository1;
    }

    @Test
    @Transactional
    public void testSuccessfulBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 23,11,40))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 23,12,40))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        bookingService.updateBooking(dto);

        Assertions.assertEquals("Some description", bookingService.getBookingsByRoomInTimeRange(
                5L,
                LocalDateTime.of(2023, Month.OCTOBER, 23,11, 0),
                LocalDateTime.of(2023, Month.OCTOBER, 23,13,0)).get(0)
                .getDescription());
    }

    @Test
    @Transactional
    public void testLeftErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testRightErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,12,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testExternalErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,9,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,12,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testInternalErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,30))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.updateBooking(dto));
    }
    @Test
    @Transactional
    public void testMixedDataTimeErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .userId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,9,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.updateBooking(dto));
    }

}
