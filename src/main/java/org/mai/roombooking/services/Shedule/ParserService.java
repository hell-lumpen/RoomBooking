package org.mai.roombooking.services.Shedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ParserService {
    private final SaverLesson saver;
    private final GroupRepository groupRepository;


    public ParserService(GroupRepository groupRepository, SaverLesson saver) {
        this.saver = saver;
        this.groupRepository = groupRepository;

//        this.processors = Runtime.getRuntime().availableProcessors();
    }

    public List<String> parseGroups(int department, int course) {
        String url = "https://mai.ru/education/studies/schedule/groups.php?department=Институт+№" +
                department + "&course=" + course;
        try {
            Connection connection = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent("YandexNews")

//                    .referrer("http://www.google.com")
//                    .timeout(12000)
                    .followRedirects(true)
                    .cookie("BITRIX_SM_GUEST_ID", "%D0%9C8%D0%9E-101%D0%91-23")
                    .cookie("__ym_tab_guid", "114a3ed1-60be-4f9b-6b8f-8b2ae9bd4a1f");
//                    .cookie("BITRIX_SM_LAST_ADV", "5")
//                    .cookie("PHPSESSID", "PsUgwFOO7dRCtndPbqr5U4foZybeu7IH")
//                    .cookie("schedule-group-cache", "2.0");

            Document document = connection.get();

            Elements elements = document.getElementsByClass("btn btn-soft-secondary btn-xs mb-1 fw-medium btn-group");

            List<String> result = new LinkedList<>();
            for (Element element : elements) {
                result.add(element.text());
            }

            return result;

        } catch (HttpStatusException e) {
            System.err.println("Error parsing groups in course " + course + " and department " + department);
            System.err.println(e.getMessage());
            return new LinkedList<>();
        } catch (IOException ex) {
            System.err.println("Error parsing groups in course " + course + " and department " + department);
            System.err.println(ex.getMessage());
            return new LinkedList<>();
        }
    }

    public List<Group> saveGroups(@NonNull List<String> groupNames) {
        List<Group> result = new LinkedList<>();
        for (var name : groupNames)
            result.add(groupRepository.save(Group.builder().name(name).build()));

        System.out.printf("end saving groups by %s department and %s course%n", result.get(0).getName().charAt(1), result.get(0).getName().charAt(4));
        return result;
    }


   public @NonNull List<ScheduleLesson> parseWebSite(@NonNull List<Group> groups) {
        System.out.printf("start parsing Lessons by %s department and %s course%n", groups.get(0).getName().charAt(1), groups.get(0).getName().charAt(4));

        List<ScheduleLesson> result = new LinkedList<>();
        ConcurrentLinkedQueue<ParsingError> errors = new ConcurrentLinkedQueue<>();

        var parser = new Parser(errors);
        for (Group group : groups)
            for (int week = 0; week < 20; week++) {
                result.addAll(parser.run(group, week));
            }

       while (!errors.isEmpty()) {
           ParsingError currError = errors.poll();
           result.addAll(parser.run(currError.getGroup(), currError.getWeek()));
       }

       System.out.printf("end parsing Lessons by %s department and %s course%n", groups.get(0).getName().charAt(1), groups.get(0).getName().charAt(4));


       return result;
   }

   public void saveSchedule(@NonNull List<ScheduleLesson> lessons) {
       System.out.printf("start saving Lessons by %s department and %s course%n", lessons.get(0).getGroup().get(0).getName().charAt(1), lessons.get(0).getGroup().get(0).getName().charAt(4));
       for (ScheduleLesson lesson : lessons)
           saver.run(lesson);

       System.out.printf("end saving Lessons by %s department and %s course%n", lessons.get(0).getGroup().get(0).getName().charAt(1), lessons.get(0).getGroup().get(0).getName().charAt(4));
   }

    public @NonNull List<ScheduleLesson> groupLessons(@NonNull List<ScheduleLesson> lessons) {
        Map<GroupingByLessonNameKey, UUID> lessonsGroup = new HashMap<>();

        for (int i = 0; i < lessons.size(); i++) {
            var lesson = lessons.get(i);
            var key = new GroupingByLessonNameKey(lesson.group, lesson.name, lesson.tag);
            if (lessonsGroup.containsKey(key)) {
                lesson.groupId = lessonsGroup.get(key);
                lessons.set(i, lesson);
            } else {
                UUID newUUID = UUID.randomUUID();
                lesson.groupId = newUUID;
                lessons.set(i, lesson);
                lessonsGroup.put(key, newUUID);
            }
        }
        return lessons;
    }


    @Getter
    @AllArgsConstructor
    static public class ParsingError {
        private Group group;
        private int week;
    }

    @Getter
    @Builder
    static public class ScheduleLesson {
        private String name;
        private String employee;
        private String tag;
        private String room;
        private List<Group> group;
        private LocalDateTime start;
        private LocalDateTime end;
        private UUID groupId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static public class GroupingByGroupKey {
        private LocalDateTime start;
        private LocalDateTime end;
        private String employee;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static public class GroupingByLessonNameKey {
        private List<Group> group;
        private String name;
        private String tag;
    }


}


// bh=EkEiR29vZ2xlIENocm9tZSI7dj0iMTE5IiwgIkNocm9taXVtIjt2PSIxMTkiLCAiTm90P0FfQnJhbmQiO3Y9IjI0IhoFIng4NiIiECIxMTkuMC42MDQ1LjE2MCIqAj8wMgIiIjoJIldpbmRvd3MiQggiMTQuMC4wIkoEIjY0IlJcIkdvb2dsZSBDaHJvbWUiO3Y9IjExOS4wLjYwNDUuMTYwIiwiQ2hyb21pdW0iO3Y9IjExOS4wLjYwNDUuMTYwIiwiTm90P0FfQnJhbmQiO3Y9IjI0LjAuMC4wIiI=;
// Expires=Wed, 27-Nov-2024 14:10:56 GMT;
// Path=/

// bh=Ej8iR29vZ2xlIENocm9tZSI7dj0iMTE5IiwiQ2hyb21pdW0iO3Y9IjExOSIsIk5vdD9BX0JyYW5kIjt2PSIyNCIaBSJ4ODYiIhAiMTE5LjAuNjA0NS4xNjAiKgI/MDICIiI6CSJXaW5kb3dzIkIIIjE0LjAuMCJKBCI2NCJSXCJHb29nbGUgQ2hyb21lIjt2PSIxMTkuMC42MDQ1LjE2MCIsIkNocm9taXVtIjt2PSIxMTkuMC42MDQ1LjE2MCIsIk5vdD9BX0JyYW5kIjt2PSIyNC4wLjAuMCIi;
// Expires=Wed, 27-Nov-2024 14:12:47 GMT;
// Domain=.yandex.ru;
// Path=/;
// SameSite=None;
// Secure