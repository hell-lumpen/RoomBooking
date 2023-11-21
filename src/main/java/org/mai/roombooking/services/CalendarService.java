package org.mai.roombooking.services;

import lombok.NonNull;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CalendarService {
    private final BookingRepository bookingRepository;

    @Autowired
    public CalendarService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String getAllBookings() {
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:VCALENDAR\n");
        builder.append("VERSION:2.0\n");
        builder.append("PRODID:-//Smart Campus//Calendar//EN\n");
        builder.append("CALSCALE:GREGORIAN");
        builder.append("METHOD:PUBLISH\n");
        builder.append("X-WR-CALNAME:Календарь Smart Campus\n");
        builder.append("X-WR-TIMEZONE:Europe/Moscow\n");

        for(var booking : bookingRepository.findAll())
            builder.append(convertToICalendar(booking));

        builder.append("END:VCALENDAR");

        return builder.toString();
    }

    public static @NonNull String convertToICalendar(@NonNull Booking booking) {
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
