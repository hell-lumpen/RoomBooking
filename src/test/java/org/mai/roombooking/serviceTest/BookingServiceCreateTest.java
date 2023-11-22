package org.mai.roombooking.serviceTest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDetailsDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.repositories.TagRepository;
import org.mai.roombooking.repositories.UserRepository;
import org.mai.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

@SpringBootTest
public class BookingServiceCreateTest {

    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    @Autowired
    BookingServiceCreateTest(BookingService bookingService, UserRepository userRepository, TagRepository tagRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Test
    @Transactional
    public void TestUpdate() throws BookingConflictException, BookingNotFoundException {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 23,11,40))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 23,12,40))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .tag(tagRepository.findByShortName("ЛК").get())
                .description("Some description")
                .build();

        Booking booking = bookingService.updateBooking(dto);
        booking.setTitle("Hello 2");
        booking = bookingService.updateBooking(booking);


        Assertions.assertEquals(bookingService.getBookingById(booking.getId()).getTitle(), "Hello 2");
    }

    @Test
    @Transactional
    public void TestDelete() throws BookingConflictException {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 23,11,40))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 23,12,40))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .tag(tagRepository.findByShortName("ЛК").get())
                .description("Some description")
                .build();

        Booking booking = bookingService.updateBooking(dto);

        bookingService.deleteBooking(booking.getId());

        Assertions.assertThrows(BookingNotFoundException.class, ()->bookingService.getBookingById(booking.getId()));
    }

    @Test
    @Transactional
    public void testSuccessfulBookingSave() throws BookingConflictException, BookingNotFoundException {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 23,11,40))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 23,12,40))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .tag(tagRepository.findByShortName("ЛК").get())
                .description("Some description")
                .build();

        Long id = bookingService.updateBooking(dto).getId();

        Assertions.assertTrue(compareRequestDtoAndBookingDetailsDto(dto, bookingService.getBookingById(id)));
    }

    @Test
    @Transactional
    public void testLeftErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .tag(tagRepository.findByShortName("ЛК").get())
                .build();

        Assertions.assertThrows(BookingConflictException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testRightErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,12,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .tag(tagRepository.findByShortName("ЛК").get())
                .description("Some description")
                .build();

        Assertions.assertThrows(BookingConflictException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testExternalErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,9,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,12,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .tag(tagRepository.findByShortName("ЛК").get())
                .build();

        Assertions.assertThrows(BookingConflictException.class, () -> bookingService.updateBooking(dto));
    }

    @Test
    @Transactional
    public void testInternalErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,30))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,11,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .tag(tagRepository.findByShortName("ЛК").get())
                .build();

        Assertions.assertThrows(BookingConflictException.class, () -> bookingService.updateBooking(dto));
    }
    @Test
    @Transactional
    public void testMixedDataTimeErrorBookingSave() {
        var dto = RoomBookingRequestDTO
                .builder()
                .roomId(5L)
                .title("Hello")
                .ownerId(userRepository.findAll().get(0).getUserId())
                .startTime(LocalDateTime.of(2023, Month.OCTOBER, 24,10,0))
                .endTime(LocalDateTime.of(2023, Month.OCTOBER, 24,9,0))
                .staffId(new ArrayList<>())
                .groupsId(new ArrayList<>())
                .description("Some description")
                .tag(tagRepository.findByShortName("ЛК").get())
                .build();

        Assertions.assertThrows(BookingConflictException.class, () -> bookingService.updateBooking(dto));
    }

    public boolean compareRequestDtoAndBookingDetailsDto(@NonNull RoomBookingRequestDTO dto1,@NonNull RoomBookingDetailsDTO dto2) {
        return (dto1.getBookingGroupId() == null && dto2.getBookingGroupId() == null) ||
                    (dto1.getBookingGroupId() != null && dto2.getBookingGroupId() != null &&
                            dto1.getBookingGroupId().equals(dto2.getBookingGroupId())) &&
                dto1.getTitle().equals(dto2.getTitle()) &&
                dto1.getRoomId().equals(dto2.getRoom().getRoomId()) &&
                dto1.getOwnerId().equals(dto2.getOwner().getId()) &&
                dto1.getStartTime().equals(dto2.getStartTime()) &&
                dto1.getEndTime().equals(dto2.getEndTime()) &&
                dto1.getDescription().equals(dto2.getDescription()) &&
                (dto1.getStaffId() == null && dto2.getStaff() == null) ||
                    (dto1.getStaffId() != null && dto2.getStaff() != null &&
                        dto2.getStaff().stream().map(UserDTO::getId).toList().equals(dto1.getStaffId())) &&
                (dto1.getGroupsId() == null && dto2.getGroups() == null) ||
                    (dto1.getGroupsId() != null && dto2.getGroups() != null &&
                        dto2.getGroups().stream().map(Group::getId).toList().equals(dto1.getGroupsId())) &&
                dto1.getTag().equals(dto2.getTag());
    }

}
