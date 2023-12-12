package org.mai.roombooking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mai.roombooking.entities.*;
import org.mai.roombooking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final TagRepository tagRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public BookingRepositoryTest(BookingRepository bookingRepository, UserRepository userRepository,
                                 RoomRepository roomRepository, GroupRepository groupRepository,
                                 TagRepository tagRepository, UserInfoRepository userInfoRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.groupRepository = groupRepository;
        this.tagRepository = tagRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @BeforeEach
    void setup() {
        // ROOM
        var roomBuilder = Room.builder()
                .isCathedral(true)
                .hasProjector(true)
                .hasComputers(true)
                .capacity(50);

        var room1 = roomBuilder.roomName("Аудитория 1").build();
        var room2 = roomBuilder.roomName("Аудитория 2").build();
        var room3 = roomBuilder.roomName("Аудитория 3").build();

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);


        // USER
        UserInfo userInfo1 = UserInfo.builder()
                .username("user1")
                .password("pass1")
                .isAccountLocked(false)
                .phoneNumber("+79290000001")
                .role(User.UserRole.ADMINISTRATOR)
                .build();
        UserInfo userInfo2 = UserInfo.builder()
                .username("user2")
                .password("pass2")
                .isAccountLocked(false)
                .phoneNumber("+79290000002")
                .role(User.UserRole.ADMINISTRATOR)
                .build();
        UserInfo userInfo3 = UserInfo.builder()
                .username("user3")
                .password("pass3")
                .isAccountLocked(false)
                .phoneNumber("+79290000003")
                .role(User.UserRole.ADMINISTRATOR)
                .build();

        userInfo1 = userInfoRepository.save(userInfo1);
        userInfo2 = userInfoRepository.save(userInfo2);
        userInfo3 = userInfoRepository.save(userInfo3);

        User user1 = User.builder().fullName("Иванова Анна Сергеевна").info(userInfo1).build();
        User user2 = User.builder().fullName("Петров Дмитрий Александрович").info(userInfo2).build();
        User user3 = User.builder().fullName("Козлова Екатерина Игоревна").info(userInfo3).build();

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        // Группы
        Group group1 = Group.builder().name("Группа 1").size(25).build();
        Group group2 = Group.builder().name("Группа 2").size(25).build();
        Group group3 = Group.builder().name("Группа 3").size(25).build();

        group1 = groupRepository.save(group1);
        group2 = groupRepository.save(group2);
        group3 = groupRepository.save(group3);

        // Тэг
        Tag tag1 = Tag.builder().fullName("Тэг 1").shortName("Тг1").color("#000001").build();
        Tag tag2 = Tag.builder().fullName("Тэг 2").shortName("Тг2").color("#000002").build();
        Tag tag3 = Tag.builder().fullName("Тэг 3").shortName("Тг3").color("#000003").build();

        tag1 = tagRepository.save(tag1);
        tag2 = tagRepository.save(tag2);
        tag3 = tagRepository.save(tag3);

        // Бронирования
        var booking1 = Booking.builder()
                .owner(user1)
                .tags(Set.of(tag1))
                .staff(List.of(user2))
                .groups(List.of(group1))
                .title("Мероприятие 1")
                .description("")
                .room(room1)
                .startTime(LocalDateTime.of(2023, Month.DECEMBER, 22, 12, 30,0,0))
                .endTime(LocalDateTime.of(2023, Month.DECEMBER, 22, 12, 30,0,0))
                .build();
        var booking2 = Booking.builder()
                .owner(user3)
                .tags(Set.of(tag1))
                .staff(List.of(user2, user1))
                .groups(List.of(group2))
                .title("Мероприятие 2")
                .description("")
                .room(room1)
                .startTime(LocalDateTime.of(2023, Month.DECEMBER, 23, 12, 30,0,0))
                .endTime(LocalDateTime.of(2023, Month.DECEMBER, 23, 12, 30,0,0))
                .build();
        var booking3 = Booking.builder()
                .owner(user1)
                .tags(Set.of(tag1))
                .staff(List.of(user2, user3))
                .groups(List.of(group1, group2))
                .title("Мероприятие 3")
                .description("")
                .room(room1)
                .startTime(LocalDateTime.of(2023, Month.DECEMBER, 24, 12, 30,0,0))
                .endTime(LocalDateTime.of(2023, Month.DECEMBER, 24, 12, 30,0,0))
                .build();

        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);
    }

    @Test
    void test1() {
        var bookings = bookingRepository.findByGroupsContains(1L,
                LocalDateTime.of(2023, Month.DECEMBER, 22, 12, 30,0,0),
                LocalDateTime.of(2023, Month.DECEMBER, 25, 12, 30,0,0));

        Assertions.assertEquals(2,bookings.size());
        Assertions.assertArrayEquals(new Long[] {1L,3L}, bookings.stream().map(Booking::getId).toArray(Long[]::new));

    }
}
