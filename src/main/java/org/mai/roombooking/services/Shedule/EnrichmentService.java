package org.mai.roombooking.services.Shedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.entities.*;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.repositories.TagRepository;
import org.mai.roombooking.services.RoomService;
import org.mai.roombooking.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class EnrichmentService {
    private final UserService userService;
    private final RoomService roomService;
    private final TagRepository tagRepository;


    public @NonNull List<Booking> enrichment(@NonNull List <PageParser.ScheduleLesson> lessons) {
        List<Booking> res = new ArrayList<>();
        for (var lesson : lessons)
            res.add(enrichment(lesson));
        return res;
    }
    private @NonNull Booking enrichment(@NonNull PageParser.ScheduleLesson lesson) {
        Optional<User> employee =  userService.getUserByFullName(lesson.getEmployee());
        Optional<Room> room = roomService.getRoomByName(lesson.getRoom());
        Optional<Tag> tag = tagRepository.findByShortName(lesson.getTag());
        List<Optional<User>> staff = lesson.getStaff().stream().map(userService::getUserByFullName).toList();


        if (employee.isEmpty())
            synchronized (userService) {
                employee =  userService.getUserByFullName(lesson.getEmployee());
                if (employee.isEmpty())
                    employee = Optional.of(userService.createEmployee(lesson.getEmployee()));
            }

        if (staff.stream().anyMatch(Optional::isEmpty))
            synchronized (userService) {
                staff = lesson.getStaff().stream().map(userService::getUserByFullName).toList();
                if (staff.stream().anyMatch(Optional::isEmpty))
                    staff = lesson.getStaff().stream().map((x) -> {
                        var user = userService.getUserByFullName(x);
                        if (user.isPresent())
                            return user;
                        else
                            return Optional.of(userService.createEmployee(x));
                    }).toList();
            }

        if (room.isEmpty())
            synchronized(roomService) {
                room = roomService.getRoomByName(lesson.getRoom());
                if (room.isEmpty())
                    room = Optional.of(roomService.update(new RoomDTO(lesson.getRoom())));
            }

        if (tag.isEmpty())
            synchronized(tagRepository) {
                tag = tagRepository.findByShortName(lesson.getTag());
                if (tag.isEmpty())
                    tag = Optional.of(tagRepository.save(new Tag(lesson.getTag(), lesson.getTag(), "#FF0000")));
            }



        return Booking.builder()
                .title(lesson.getName())
                .owner(employee.get())
                .tags(Set.of(tag.get()))
                .room(room.get())
                .staff(staff.stream().map(Optional::get).toList())
                .groups(lesson.getGroup())
                .startTime(lesson.getStart())
                .endTime(lesson.getEnd())
                .bookingGroupId(lesson.getGroupId())
                .description("")
                .build();
    }
}
