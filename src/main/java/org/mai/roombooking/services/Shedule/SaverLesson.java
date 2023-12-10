package org.mai.roombooking.services.Shedule;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.entities.*;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.repositories.TagRepository;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.RoomService;
import org.mai.roombooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SaverLesson {


    private final TagRepository tagRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final RoomService roomService;

    @Autowired
    public SaverLesson(TagRepository tagRepository, UserService userService, BookingService bookingService,
                       RoomService roomService) {
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    public void run(@NonNull ParserService.ScheduleLesson lesson) {

        Optional<User> employee =  userService.getUserByFullName(lesson.getEmployee());
        Optional<Room> room = roomService.getRoomByName(lesson.getRoom());
        Optional<Tag> tag = tagRepository.findByShortName(lesson.getTag());


        if (employee.isEmpty())
            synchronized (userService) {
                employee =  userService.getUserByFullName(lesson.getEmployee());
                if (employee.isEmpty())
                    employee = Optional.of(userService.createEmployee(lesson.getEmployee()));
            }

        if (room.isEmpty())
            synchronized(roomService) {
                room = roomService.getRoomByName(lesson.getRoom());
                if (room.isEmpty())
                    room = Optional.of(roomService.update(new RoomDTO(lesson.getRoom())));
            }

        if (tag.isEmpty())
            tag = Optional.of(tagRepository.findByShortName("ЛК").get());

        if (lesson.getRoom().equals("--каф.")) {
            var availableRooms = roomService.getAvailableRooms(lesson.getStart(),lesson.getEnd(),
                    lesson.getGroup().stream().map(Group::getSize).mapToInt(Integer::intValue).sum(),
                    null, null);

            if (availableRooms.isEmpty()) {
                log.error("Бронирование не распределено: "+ lesson);
                //TODO: ошибка
            }

            room = Optional.of(availableRooms.get(0));
        }


        Booking booking = Booking.builder()
                .description("")
                .title(lesson.getName())
                .bookingGroupId(lesson.getGroupId())
                .startTime(lesson.getStart())
                .endTime(lesson.getEnd())
                .tag(tag.get())
                .owner(employee.get())
                .groups(lesson.getGroup())
                .room(room.get())
                .build();

        try {
            bookingService.updateBooking(booking);
            log.info("Course: " + lesson.getGroup().get(0).getCourse() + "Faculty: " +
                    lesson.getGroup().get(0).getFaculty() + " saved");
        } catch (BookingConflictException ex) {
            log.error("Конфликт при сохранении: "+ lesson);
            // TODO: разрешить конфликт
        }
    }
}
