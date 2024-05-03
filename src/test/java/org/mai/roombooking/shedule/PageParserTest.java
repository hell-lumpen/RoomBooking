//package org.mai.roombooking.shedule;
//
//import org.junit.jupiter.api.Test;
//import org.mai.roombooking.entities.Group;
//import org.mai.roombooking.services.Shedule.EnrichmentService;
//import org.mai.roombooking.services.Shedule.ExcelParser;
//import org.mai.roombooking.services.Shedule.PageParser;
//import org.mai.roombooking.services.Shedule.ScheduleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//public class PageParserTest {
//    @Autowired
//    PageParser pageParser;
//
//    @Autowired
//    EnrichmentService enrichmentService;
//
//    @Autowired
//    private ScheduleService scheduleService;
//    @Test
//    public void excelTest() {
//        ExcelParser excelParser = new ExcelParser();
//        var groups = excelParser.parseGroupFile("src/main/resources/groups.xlsx");
//        for (var group : groups){
//            System.out.println(group.getName() + "  " + group.getSpecialty() + "  " + group.getSize());
//        }
//    }
//
//    @Test
//    public void parsingTest() {
//        // два преподавателя на одну пару
//        // https://mai.ru/education/studies/schedule/index.php?group=%D0%9C8%D0%9E-406%D0%91-20&week=2
//
//        var group = Group.builder().name("М1О-304Б-21").build();
//        var lessons = pageParser.parse(group, 5);
//        for (var lesson : lessons) {
//            System.out.println(lesson);
//        }
//    }
//
//    @Test
//    public void enrichmentTest() {
//        var group = Group.builder().name("М8О-406Б-20").build();
//        var lessons = pageParser.parse(group, 2);
//        var bookings = enrichmentService.enrichment(lessons);
//        for (var booking : bookings) {
//            System.out.println(booking);
//        }
//    }
//
//    @Test
//    public void scheduleTest() {
//        scheduleService.updateSchedule("src/main/resources/groups.xlsx");
//        scheduleService.updateSchedule(List.of(1,2,3,4,5,6,7,9,10,11,12,14));
//    }
//}
