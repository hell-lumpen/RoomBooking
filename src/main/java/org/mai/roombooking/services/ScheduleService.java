package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import org.mai.roombooking.dtos.GroupDTO;
import org.mai.roombooking.dtos.LessonDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.GroupNotFoundException;
import org.mai.roombooking.exceptions.TagNotFoundException;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final BookingService bookingService;
    private final RoomService roomService;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final GroupRepository groupRepository;


//    public ScheduleService(BookingService bookingService,
//                           RoomService roomService,
//                           UserService userService,
//                           TagRepository tagRepository,
//                           GroupRepository groupRepository) {
//        this.bookingService = bookingService;
//        this.roomService = roomService;
//        this.userService = userService;
//        this.tagRepository = tagRepository;
//        this.groupRepository = groupRepository;
//    }


    public void bookingSchedule(@NonNull List<LessonDTO> dtos) {
        var bookings = dtos.stream().map((this::getBookingFromLessonDTO)).toList();
        for(var booking : bookings) {
//            roomService.getAvailableRooms(booking.getStartTime(), booking.getEndTime())
        }
        //TODO: реализация
    }

    public RoomBookingDTO bookingLesson(LessonDTO dto) throws BookingException {
        return new RoomBookingDTO(bookingService.updateBooking(getBookingFromLessonDTO(dto)));
    }


    private Booking getBookingFromLessonDTO(@NonNull LessonDTO dto) {
        return null;
//        return Booking.builder()
//                .bookingGroupId(UUID.fromString(dto.getGroupBookingId()))
//                .tag(tagRepository.findByShortName(dto.getTag())
//                        .orElseThrow(() -> new TagNotFoundException(dto.getTag())))
//                .groups(dto.getGroups().stream()
//                        .map((group) -> groupRepository.findByName(group)
//                                .orElseThrow(() -> new GroupNotFoundException(0L)))
//                        .toList())
//                .staff(new ArrayList<>())
//                .owner(userService.getUserByFullName(dto.getEmployee()))
//                .description(dto.getDescription())
//                .startTime(dto.getStartTime())
//                .endTime(dto.getEndTime())
//                .room(roomService.getRoomByName(dto.getRoom()))
//                .build();
    }

}
