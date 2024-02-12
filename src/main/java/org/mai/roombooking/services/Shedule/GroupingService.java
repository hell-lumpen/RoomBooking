package org.mai.roombooking.services.Shedule;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.Tag;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class GroupingService {
    final Integer memoryLimit = 1048576 * 1000;
    public List<Booking> group(@NonNull List<Booking> bookings) {

        // Группировка по времени и месту проведения
        Map<GroupingByGroupKey, Booking> grouped = new HashMap<>();
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking current = iterator.next();
            GroupingByGroupKey key = new GroupingByGroupKey(current);
            if (grouped.containsKey(key)) {
                grouped.get(key).getGroups().addAll(current.getGroups());
                iterator.remove();
            }
            else {
                grouped.put(key, current);
            }
        }


        Map<GroupingByLessonNameKey, UUID> lessonsGroup = new TreeMap<>();
        List<Booking> result = new ArrayList<>(grouped.values());
        grouped = null;
        System.gc();


        log.info("necessary to process: " + result.size());
        for(int i = 0; i < result.size(); i++) {
            var lesson = result.get(i);
            var key = new GroupingByLessonNameKey(lesson);
            if (lessonsGroup.containsKey(key)) {
                lesson.setBookingGroupId(lessonsGroup.get(key));

            } else {
                UUID newUUID = UUID.randomUUID();
                lesson.setBookingGroupId(newUUID);
                lessonsGroup.put(key, newUUID);
            }
            result.set(i, lesson);


            if (i%50000 == 0) {
                log.info("processed: " + i + "total: " + result.size());
                if (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory() > memoryLimit) {
                    log.warn("memory limit exceeded: totalMemory =  " +
                            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 +
                            "mb");
                    System.gc();
                }
            }

        }


        return result;
    }

    @Data
    @Builder
    @AllArgsConstructor
    static public class GroupingByGroupKey {
        private LocalDateTime start;
        private LocalDateTime end;
        private Room room;

        public GroupingByGroupKey(@NonNull Booking lesson) {
            start = lesson.getStartTime();
            end = lesson.getEndTime();
            room = lesson.getRoom();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static public class GroupingByLessonNameKey implements Comparable<GroupingByLessonNameKey> {
        private String title;
        private Set<Tag> tags;
        private Set<Group> groups;

        public GroupingByLessonNameKey(Booking booking) {
            title = booking.getTitle();
            tags = booking.getTags();
            groups = booking.getGroups();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupingByLessonNameKey that = (GroupingByLessonNameKey) o;
            return Objects.equals(title, that.title) && Objects.equals(tags, that.tags) && Objects.equals(groups, that.groups);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, tags, groups);
        }


        @Override
        public int compareTo(GroupingByLessonNameKey o) {
            int titleComparison = getTitle().compareTo(o.getTitle());
            if (titleComparison != 0) return titleComparison;

            return Integer.compare(Objects.hash(getTags()), Objects.hash(o.getTags())) != 0 ?
                    Integer.compare(Objects.hash(getTags()), Objects.hash(o.getTags())) :
                    Integer.compare(Objects.hash(getGroups()), Objects.hash(o.getGroups()));
        }
    }
}
