package org.mai.roombooking;

import org.junit.jupiter.api.Test;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.Shedule.Parser;
import org.mai.roombooking.services.Shedule.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class ParserTest {

    @Autowired
    ParserService parserService;

    @Autowired
    BookingService bookingService;

    @Autowired
    GroupRepository groupRepository;
    @Test
    public void test() {

        var group = groupRepository.save(Group.builder().name("М8О-411Б-20").size(20).build());
        Parser parser = new Parser(new ConcurrentLinkedQueue<>());

        var list = parser.parse(group, 6);
        System.out.println("Complete");

    }
}