package org.mai.roombooking.services.Shedule;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mai.roombooking.entities.Group;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class PageParser {

    public List<ScheduleLesson> parse(@NonNull Group group, int week) {
        Optional<Document> document = getPage(group, week);
        if (document.isEmpty()) {
            log.error("Error in parsing schedule group " + group.getName() + " on week " + week);
            return new ArrayList<>();
        }
        var res = parseDocument(document.get(), group);
        log.info("The schedule of group " + group.getName() + " for week " + week + " has been processed.");
        return res;
    }

    private Optional<Document> getPage(@NonNull Group group, int week) {
        String url = "https://mai.ru/education/studies/schedule/index.php?group=" +
                                                    group.getName() + "&week=" + week;
        int count = 0;
        int maxTries = 3;
        boolean continueHttpCall = true;

        while (continueHttpCall) {
            try {
                Thread.sleep(1000);
                Connection connection = Jsoup.connect(url)
                        .timeout(120 * 1000)
                        .cookie("BITRIX_SM_GUEST_ID", "12179208")
                        .cookie("schedule-st-group", "%D0%9C4%D0%9E-604%D0%A1%D0%BA-18")
                        .cookie("BITRIX_SM_LAST_VISIT", "28.11.2023%2019%3A58%3A34")
                        .cookie("schedule-group-cache", "2.0");

                return Optional.of(connection.get());
            } catch (IOException e) {
                if (++count == maxTries) {
                    continueHttpCall = false;
                    System.err.println("Error accessing the schedule page: group = " + group + ", week = " + week);
                    System.err.println(e.getMessage());
//                    errors.add(new ParserService.ParsingError(group, week));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    private @NonNull List<ScheduleLesson>  parseDocument(@NonNull Document document, Group group) {
        List<ScheduleLesson> result = new ArrayList<>();
        Elements elements = document.getElementsByClass("step-content");

        for (Element element : elements)
            result.addAll(parseDay(element, group));

        return result;
    }

    private @NonNull List<ScheduleLesson> parseDay(@NonNull Element element, @NonNull Group group) {
        List<ScheduleLesson> res = new ArrayList<>();
        // Дата
        String[] dateStr = element.select("span.step-title")
                .text()
                .split(",")[1]
                .trim()
                .split(" ");
        LocalDate date = LocalDate.of(LocalDate.now().getYear(),
                month.get(dateStr[1]),
                Integer.parseInt(dateStr[0]));


        for (Element lesson : element.select("div.mb-4")) {
            String tag = lesson.select("span.badge").get(0).text().trim();
            String name = lesson.select("p.mb-2").get(0).text().replace(tag, "").trim();


            Elements TimeRoomEmployeeLine = lesson.select("li.list-inline-item");
            // Аудитория
            String room;
            if (mapper.containsKey(name))
                room = mapper.get(name);
            else
                room = TimeRoomEmployeeLine.get(TimeRoomEmployeeLine.size() - 1).text();

            // Дата
            String[] splittedTime = TimeRoomEmployeeLine.get(0).text().split("–");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(splittedTime[0].trim(), formatter);
            LocalTime end = LocalTime.parse(splittedTime[1].trim(), formatter);

            // Преподаватели
            Deque<String> employees = new ArrayDeque<>();
            for (int i = 1; i < TimeRoomEmployeeLine.size(); i++)
                employees.add(TimeRoomEmployeeLine.get(i).select("a.text-body").text().trim());

            // Сборка
            var booking = ScheduleLesson.builder()
                    .tag(tag)
                    .room(room)
                    .name(name)
                    .group(new TreeSet<Group>(Collections.singletonList(group)))
                    .tag(tag)
                    .start(LocalDateTime.of(date, start))
                    .end(LocalDateTime.of(date, end));
            if (!employees.isEmpty())
                booking.employee(employees.pop());
            booking.staff(employees.stream().map(String::trim).toList());
            res.add(booking.build());
        }
        return res;
    }


    static Map<String, Month> month = new HashMap<>() {{
        put("января",   Month.JANUARY);
        put("февраля",  Month.FEBRUARY);
        put("марта",    Month.MARCH);
        put("апреля",   Month.APRIL);
        put("мая",      Month.MAY);
        put("июня",     Month.JUNE);
        put("июля",     Month.JULY);
        put("августа",  Month.AUGUST);
        put("сентября", Month.SEPTEMBER);
        put("октября",  Month.OCTOBER);
        put("ноября",   Month.NOVEMBER);
        put("декабря",  Month.DECEMBER);

    }};

    static Map<String, String> mapper = new HashMap<>(){{
        put("Физическая культура (спортивные секции)", "Спортзал");
        put("Военная подготовка", "ВУЦ");
    }};

    @Getter
    @Builder
    static public class ScheduleLesson {
        private String name;
        private String employee;
        private String tag;
        private String room;
        private Set<Group> group;
        private List<String> staff;
        private LocalDateTime start;
        private LocalDateTime end;
        private UUID groupId;

        @Override
        public String toString() {
            String res = name + "  ||  " + tag + "  ||  ";
            if (employee != "")
                res += employee;
            else
                res += "Преподаватель не установлен";

            res += "  ||  ";

            if (room != "")
                res += room;
            else
                res += "Аудитория не установлена";

            return res;
        }
    }

}