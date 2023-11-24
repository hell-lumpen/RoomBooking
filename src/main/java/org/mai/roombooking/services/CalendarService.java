package org.mai.roombooking.services;

import lombok.NonNull;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.GroupNotFoundException;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CalendarService {
    private final BookingRepository bookingRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public CalendarService(BookingRepository bookingRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.groupRepository = groupRepository;

        this.userRepository = userRepository;
    }

    public String getAllBookings() {
        return convertToICalendar(bookingRepository.findAll());
    }

    public String getByGroup(Long groupId) {
        var group = groupRepository.findById(groupId).orElseThrow(()-> new GroupNotFoundException(groupId));
        return convertToICalendar(bookingRepository.findByGroupsContaining(group));
    }

    public String getByStaff(Long staffId) {
        var staff = userRepository.findById(staffId).orElseThrow(()-> new UserNotFoundException(staffId));
        return convertToICalendar(bookingRepository.findByStaffContaining(staff));
    }

    public String getByGroup(String groupName) {
        var group = groupRepository.findByName(groupName).orElseThrow();
        return convertToICalendar(bookingRepository.findByGroupsContaining(group));
    }
    private static @NonNull String convertToICalendar(@NonNull List<Booking> bookings) {
        StringBuilder builder = new StringBuilder();

        builder.append("BEGIN:VCALENDAR\n");
        builder.append("VERSION:2.0\n");
        builder.append("PRODID:-//Smart Campus//Calendar//EN\n");
        builder.append("CALSCALE:GREGORIAN\n");
        builder.append("METHOD:PUBLISH\n");
        builder.append("X-WR-CALNAME:Календарь Smart Campus\n");
        builder.append("X-WR-TIMEZONE:Europe/Moscow\n");

        for(var booking : bookings)
            builder.append(convertToICalendar(booking));

        builder.append("END:VCALENDAR");

        return builder.toString();
    }

    private static @NonNull String convertToICalendar(@NonNull Booking booking) {
        StringBuilder icsBuilder = new StringBuilder();

        icsBuilder.append("BEGIN:VEVENT\n");

        icsBuilder.append("UID:").append(booking.getId()).append("\n");
        icsBuilder.append("SUMMARY:").append(booking.getTitle()).append("\n");
        icsBuilder.append("DESCRIPTION:").append(booking.getDescription());

        if ((booking.getStaff() != null && !booking.getStaff().isEmpty()) ||
                (booking.getGroups() != null && !booking.getGroups().isEmpty())) {
            icsBuilder.append("Участники мероприятия: ");

            List<User> staff = booking.getStaff();
            List<Group> groups = booking.getGroups();

            boolean isNotFirst = false;

            if (staff != null && !staff.isEmpty()) {
                for (User user : staff) {
                    if (isNotFirst) {
                        icsBuilder.append(", ");
                    } else {
                        isNotFirst = true;
                    }
                    icsBuilder.append(user.getFullName());
                }
            }

            if (groups != null && !groups.isEmpty()) {
                for (Group group : groups) {
                    if (isNotFirst) {
                        icsBuilder.append(", ");
                    } else {
                        isNotFirst = true;
                    }
                    icsBuilder.append(group.getName());
                }
            }
        }
        icsBuilder.append("\n");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        icsBuilder.append("DTSTART:").append(booking.getStartTime().format(formatter)).append("\n");
        icsBuilder.append("DTEND:").append(booking.getEndTime().format(formatter)).append("\n");

        icsBuilder.append("LOCATION:").append(booking.getRoom().getRoomName()).append("\n");
        icsBuilder.append("ORGANIZER:mailto:").append(booking.getOwner().getFullName()).append("\n");

        icsBuilder.append("END:VEVENT\n");

        return icsBuilder.toString();
    }
}
