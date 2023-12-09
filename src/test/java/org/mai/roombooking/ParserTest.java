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



    }
}