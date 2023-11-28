package org.mai.roombooking.services.Shedule;

import lombok.NonNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mai.roombooking.entities.Group;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class Parser {
        private final ConcurrentLinkedQueue<ParserService.ParsingError> errors;
        private final Random random;
        Parser(ConcurrentLinkedQueue<ParserService.ParsingError> errors) {
            this.errors = errors;
            random = new Random();
        }


        public List<ParserService.ScheduleLesson> run(@NonNull Group group, int week) {
            String url = "https://mai.ru/education/studies/schedule/index.php?group=" + group.getName() + "&week=" + week;
            Map<ParserService.GroupingByGroupKey, ParserService.ScheduleLesson> result = new HashMap<>();
            try {
                int randomNumber = random.nextInt(900000) + 9000000;

                Connection connection = Jsoup.connect(url).timeout(120 * 1000); // таймаут 2 мин
//                connection.cookie("schedule-st-group", group.getName());
//                connection.cookie("BITRIX_SM_GUEST_ID", Integer.toString(randomNumber));
//                connection.cookie("BITRIX_SM_GUEST_ID", "%D0%9C8%D0%9E-101%D0%91-23");
//                connection.cookie("schedule-st-group", "9453633");
//                connection.cookie("schedule-group-cache", "2.0");
                Document document = connection.get();

                Elements elements = document.getElementsByClass("step-content");

                for (Element element : elements) {
                    String[] dateStr = element.select("span.step-title")
                            .text()
                            .split(",")[1]
                            .trim()
                            .split(" ");
                    LocalDate date = LocalDate.of(LocalDate.now().getYear(),
                            month.get(dateStr[1]),
                            Integer.parseInt(dateStr[0]));


                    for (Element lesson : element.select("div.mb-4")) {
                        var tmp = element.select("p").get(0).text();
                        String name = tmp.substring(0,tmp.length() - 3).trim();
                        String tag = tmp.substring(tmp.length() - 3).trim();

                        String room;
                        if (mapper.containsKey(name))
                            room = mapper.get(name);
                        else
                            room = lesson.select("li.list-inline-item")
                                    .get(lesson.select("li.list-inline-item").size()-1).text();


                        String[] splittedTime = lesson.select("li.list-inline-item").get(0).text()
                                .split("–");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime start = LocalTime.parse(splittedTime[0].trim(), formatter);
                        LocalTime end = LocalTime.parse(splittedTime[1].trim(), formatter);
                        String employee = lesson.select("li.list-inline-item").select("a.text-body")
                                .text().trim();


                        ParserService.GroupingByGroupKey key = new ParserService.GroupingByGroupKey(LocalDateTime.of(date,start),
                                                                LocalDateTime.of(date,end), employee);

                        if (result.containsKey(key))
                            result.get(key).getGroup().add(group);
                        else
                            result.put(key, ParserService.ScheduleLesson.builder()
                                    .employee(employee)
                                    .tag(tag)
                                    .room(room)
                                    .name(name)
                                    .group(new ArrayList<>(List.of(group)))
                                    .tag(tag)
                                    .start(LocalDateTime.of(date,start))
                                    .end(LocalDateTime.of(date,end))
                                    .build());
                    }
                }
                return new ArrayList<>(result.values());
            }
            catch (IOException e) {
                System.err.println("Error in: Group " + group.getName() + ", week " +  week);
                System.err.println(e.getMessage());
                errors.add(new ParserService.ParsingError(group, week));
            }
            return new LinkedList<>();
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

}