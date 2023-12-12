package org.mai.roombooking.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookingFilteringTest {
    private final BookingRepository bookingRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingFilteringTest(BookingRepository bookingRepository,
                                GroupRepository groupRepository,
                                UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

//    @Test
//    void getByGroup() {
//        var group = groupRepository.findByName("Группа 1").orElseThrow();
//        var bookingList = bookingRepository.findByGroupsContains(group.getId());
//
//        Assertions.assertEquals(1, bookingList.size());
//        Assertions.assertEquals(1, bookingList.get(0).getId());
//    }

//    @Test
//    void getByStaff() {
//        var staff = userRepository.findById(2L).orElseThrow();
//        var bookingList = bookingRepository.findByStaffContaining(staff);
//
//        Assertions.assertEquals(2,  bookingList.size());
//
//        Assertions.assertEquals(2,  bookingList.get(0).getId());
//        Assertions.assertEquals(10, bookingList.get(1).getId());
//    }
}
