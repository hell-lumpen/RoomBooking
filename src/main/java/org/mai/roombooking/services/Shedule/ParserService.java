package org.mai.roombooking.services.Shedule;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
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
@Slf4j
public class ParserService {
    private final SaverLesson saver;
    private final GroupRepository groupRepository;

    public void startParsing() {
        ExecutorService parseGroupExecutor = Executors.newFixedThreadPool(1);
        ExecutorService saveGroupExecutor = Executors.newFixedThreadPool(1);
        ExecutorService parseExecutor = Executors.newFixedThreadPool(10);
        ExecutorService groupingExecutor = Executors.newFixedThreadPool(2);
        ExecutorService saveExecutor = Executors.newFixedThreadPool(2);

        List<CompletableFuture<Void>> allFutures = new ArrayList<>();

        for (int department = 8; department <= 8; department++) {
            for (int course = 4; course <= 4; course++) {
                int finalDepartment = department;
                int finalCourse = course;
                CompletableFuture<List<String>> parsedGroups = CompletableFuture.supplyAsync(() ->
                        this.parseGroups(finalDepartment, finalCourse), parseGroupExecutor);

                CompletableFuture<List<Group>> savedGroups =
                        parsedGroups.thenApplyAsync(this::saveGroups, saveGroupExecutor);

                CompletableFuture<List<ParserService.ScheduleLesson>> parsedLessons =
                        savedGroups.thenApplyAsync(this::parseWebSite, parseExecutor);

                CompletableFuture<List<ParserService.ScheduleLesson>> groupedLessons =
                        parsedLessons.thenApplyAsync(this::groupLessons, groupingExecutor);

                CompletableFuture<Void> saveScheduleFuture =
                        groupedLessons.thenAcceptAsync(this::saveSchedule, saveExecutor);

                allFutures.add(saveScheduleFuture);
            }
        }


        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
        try {
            // Ожидание завершения всех CompletableFuture.
            allOfFuture.join();

            parseGroupExecutor.shutdown();
            saveGroupExecutor.shutdown();
            groupingExecutor.shutdown();
            parseExecutor.shutdown();
            saveExecutor.shutdown();

            if (parseExecutor.awaitTermination(36, TimeUnit.HOURS) &&
                    saveExecutor.awaitTermination(36, TimeUnit.HOURS) &&
                    parseGroupExecutor.awaitTermination(36, TimeUnit.HOURS) &&
                    saveGroupExecutor.awaitTermination(36, TimeUnit.HOURS) &&
                    groupingExecutor.awaitTermination(36, TimeUnit.HOURS)) {
                System.out.println("Все запросы обработаны успешно.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public ParserService(GroupRepository groupRepository, SaverLesson saver) {
        this.saver = saver;
        this.groupRepository = groupRepository;
    }

    private @NonNull List<String> parseGroups(int department, int course) {
        String url = "https://mai.ru/education/studies/schedule/groups.php?department=Институт+№" +
                department + "&course=" + course;
        try {
            Thread.sleep(1000);
            Connection connection = Jsoup.connect(url)
                    .cookie("BITRIX_SM_GUEST_ID", "%D0%9C8%D0%9E-101%D0%91-23");

            Document document = connection.get();
            Elements elements = document.getElementsByClass("btn btn-soft-secondary btn-xs mb-1 fw-medium btn-group");

            List<String> result = new LinkedList<>();
            for (Element element : elements) {
                result.add(element.text());
            }

            return result;

        } catch (IOException ex) {
            System.err.println("Error parsing groups in course " + course + " and department " + department);
            System.err.println(ex.getMessage());
            return new LinkedList<>();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private @NonNull List<Group> saveGroups(@NonNull List<String> groupNames) {
        List<Group> result = new LinkedList<>();
        for (var name : groupNames)
            result.add(groupRepository.save(Group.builder().name(name).build()));

        log.info("Groups saved: " + groupNames);

        return result;
    }


   private @NonNull List<ScheduleLesson> parseWebSite(@NonNull List<Group> groups) {
        List<ScheduleLesson> result = new LinkedList<>();
        ConcurrentLinkedQueue<ParsingError> errors = new ConcurrentLinkedQueue<>();

        // Парсинг
        var parser = new Parser(errors);
        for (Group group : groups) {
            for (int week = 2; week < 4; week++)
                result.addAll(parser.parse(group, week));
            log.info("Group parsed: name = " + group.getName());
        }

        // Повторная попытка запроса в случае ошибки
        while (!errors.isEmpty()) {
            ParsingError currError = errors.poll();
            result.addAll(parser.parse(currError.getGroup(), currError.getWeek()));
        }


        // Группировка по времени и месту проведения
        Map<ParserService.GroupingByGroupKey, ParserService.ScheduleLesson> grouped = new HashMap<>();
        for (var lesson : result) {
            ParserService.GroupingByGroupKey key = new ParserService.GroupingByGroupKey(lesson);
            if (grouped.containsKey(key))
                grouped.get(key).getGroup().addAll(lesson.group);
            else
                grouped.put(key, lesson);
        }

       log.info("Parsed website");
       return new ArrayList<>(grouped.values());
   }

    private @NonNull List<ScheduleLesson> groupLessons(@NonNull List<ScheduleLesson> lessons) {
        log.info("Start grouping");
        Map<GroupingByLessonNameKey, UUID> lessonsGroup = new HashMap<>();
        List<ScheduleLesson> result = new ArrayList<>();

        for(int i = 0; i < lessons.size(); i++) {
            var lesson = lessons.get(i);
            var key = new GroupingByLessonNameKey(lesson.name, lesson.tag, lesson.room);
            if (lessonsGroup.containsKey(key)) {
                lesson.groupId = lessonsGroup.get(key);

            } else {
                UUID newUUID = UUID.randomUUID();
                lesson.groupId = newUUID;
                lessonsGroup.put(key, newUUID);
            }
            result.add(i, lesson);
        }
        log.info("End grouping");
        return result;
    }

    private void saveSchedule(@NonNull List<ScheduleLesson> lessons) {
        log.info("Saving start");
        for (ScheduleLesson lesson : lessons)
            saver.run(lesson);
        log.info("Saving end");
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

    @Data
    @Builder
    @AllArgsConstructor
    static public class GroupingByGroupKey {
        private LocalDateTime start;
        private LocalDateTime end;
        private String room;

        public GroupingByGroupKey(@NonNull ScheduleLesson lesson) {
            start = lesson.getStart();
            end = lesson.getEnd();
            room = lesson.getRoom();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static public class GroupingByLessonNameKey {
        private String name;
        private String tag;
        private String room;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupingByLessonNameKey that = (GroupingByLessonNameKey) o;
            return  Objects.equals(name, that.name) &&
                    Objects.equals(tag, that.tag) &&  Objects.equals(room, that.room);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, tag, room);
        }
    }
}