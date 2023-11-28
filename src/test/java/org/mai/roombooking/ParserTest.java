package org.mai.roombooking;

import org.junit.jupiter.api.Test;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.Shedule.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ParserTest {

    @Autowired
    ParserService parserService;

    @Autowired
    BookingService bookingService;
    @Test
    public void test() {

        ExecutorService parseGroupExecutor = Executors.newFixedThreadPool(1);
        ExecutorService saveGroupExecutor = Executors.newFixedThreadPool(1);
        ExecutorService parseExecutor = Executors.newFixedThreadPool(1);
        ExecutorService groupingExecutor = Executors.newFixedThreadPool(1);
        ExecutorService saveExecutor = Executors.newFixedThreadPool(1);

        List<CompletableFuture<Void>> allFutures = new ArrayList<>();

        for (int department = 3; department <= 4; department++) {
            for (int course = 1; course <= 6; course++) {
                int finalDepartment = department;
                int finalCourse = course;
                CompletableFuture<List<String>> parsedGroups = CompletableFuture.supplyAsync(() ->
                        parserService.parseGroups(finalDepartment, finalCourse), parseGroupExecutor);

                CompletableFuture<List<Group>> savedGroups = parsedGroups.thenApplyAsync(
                        parserService::saveGroups, saveGroupExecutor);

                CompletableFuture<List<ParserService.ScheduleLesson>> parsedLessons =
                        savedGroups.thenApplyAsync(parserService::parseWebSite, parseExecutor);

                CompletableFuture<List<ParserService.ScheduleLesson>> groupedLessons =
                        parsedLessons.thenApplyAsync(parserService::groupLessons, groupingExecutor);

                CompletableFuture<Void> saveScheduleFuture = groupedLessons.thenAcceptAsync(parserService::saveSchedule, saveExecutor);

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
}

//long m = System.currentTimeMillis();
//
//var groups = parserService.parseGroups(department,course);
//parserService.parseSchedule(groups);
//
//System.out.println("Department " + department + ", Course " + course + " complete");
//System.out.println("Working time = " + ((double) (System.currentTimeMillis() - m))/1000/60);
