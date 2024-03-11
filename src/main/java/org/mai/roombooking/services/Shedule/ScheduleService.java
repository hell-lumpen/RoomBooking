package org.mai.roombooking.services.Shedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.GroupService;
import org.mai.roombooking.services.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduleService {

    private final GroupingService groupingService;
    private final GroupService groupService;
    private final PageParser pageParser;
    private final EnrichmentService enrichmentService;
    private final BookingService bookingService;
    private final RoomService roomService;

    public void updateSchedule(List<Integer> departments) {
        var groups = saveGroups(parseGroups(departments));
        List<Booking> schedule = parseSchedule(groups);
        List<Booking> groupedBookings = groupingService.group(schedule);
        saveBookings(groupedBookings);
    }

    public void updateSchedule(String groupsDataFilePath) {
        ExcelParser excelParser = new ExcelParser();
        List<Group> groups = excelParser.parseGroupFile(groupsDataFilePath);
        groupService.saveGroups(groups);
        List<Booking> schedule = parseSchedule(groups);
        List<Booking> groupedBookings = groupingService.group(schedule);
        saveBookings(groupedBookings);
    }

    public void updateSchedule(MultipartFile groupsData) {
        ExcelParser excelParser = new ExcelParser();
        List<Group> groups = excelParser.parseGroupFile(groupsData);
        groupService.saveGroups(groups);
        List<Booking> schedule = parseSchedule(groups);
        List<Booking> groupedBookings = groupingService.group(schedule);
        saveBookings(groupedBookings);
    }

    private @NonNull List<Booking> parseSchedule(@NonNull List<Group> groups) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ExecutorService executor2 = Executors.newFixedThreadPool(5);
        List<CompletableFuture<List<Booking>>> completableFutures = new ArrayList<>();
        for (var group : groups) {
            for (int week = 1; week < 16; week++) {
                int finalWeek = week;
                var lessons = CompletableFuture.supplyAsync(() -> pageParser.parse(group, finalWeek),
                        executor);
                completableFutures.add(lessons.thenApplyAsync(enrichmentService::enrichment, executor2));
            }
        }
        return completableFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect((Collectors.toCollection(ArrayList::new)));
    }

    private void saveBookings (@NonNull List<Booking> bookings) {
        for (var booking : bookings) {
            boolean validation = false;
            if (booking.getRoom().getRoomName().equals("--каф.") && isCathedral(booking.getGroups())) {
                var availableRooms = roomService.getAvailableRooms(
                            booking.getStartTime(),
                            booking.getEndTime(),
                            booking.getGroups().stream().map(Group::getSize).mapToInt(Integer::intValue).sum(),
                            null,
                            null)
                        .stream().
                        filter(Room::getIsCathedral)
                        .sorted((a,b) -> a.getCapacity().compareTo(b.getCapacity()))
                        .toList();

                if (availableRooms.isEmpty()) {
                    //TODO: обработка ошибки
                    log.error("Бронирование не распределено: " + booking);
                    continue;
                }
                booking.setRoom(availableRooms.get(0));
                validation = true;
            }

            try {
                bookingService.updateBooking(booking, validation);
            } catch (BookingException e) {
                // TODO: записать в файл для ручного добавления
                System.err.println("Error on saving booking from schedule. " + e.getMessage());
            }

        }
    }
    private boolean isCathedral(@NonNull Set<Group> groups) {
        for (var group : groups) {
            if (!(group.getSpecialty().equals("01.03.02") || group.getSpecialty().equals("02.03.02") ||
                                                             group.getSpecialty().equals("02.04.02")))
                return false;
        }
        return true;
    }

    private List<String> parseGroups(List<Integer> departments) {
        List<String> result = new LinkedList<>();
        for (int department : departments) {
            for (int course = 1; course <= 6; course++) {
                String url = "https://mai.ru/education/studies/schedule/groups.php?department=Институт+№" +
                        department + "&course=" + course;
                try {
                    Thread.sleep(1000);
                    Connection connection = Jsoup.connect(url)
                            .cookie("BITRIX_SM_GUEST_ID", "%D0%9C8%D0%9E-101%D0%91-23");

                    Document document = connection.get();
                    Elements elements = document.getElementsByClass("btn btn-soft-secondary btn-xs mb-1 fw-medium btn-group");

                    for (Element element : elements) {
                        result.add(element.text());
                    }
                } catch (IOException ex) {
                    System.err.println("Error parsing groups in course " + course + " and department " + department);
                    System.err.println(ex.getMessage());
                    return new LinkedList<>();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    private List<Group> saveGroups(List<String> groups) {
        return groupService.saveGroups(groups.stream().map(x -> Group.builder().name(x).size(25).specialty("").build()).toList());
    }
}
