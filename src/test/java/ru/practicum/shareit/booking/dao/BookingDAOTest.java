package ru.practicum.shareit.booking.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
class BookingDAOTest {
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private TestEntityManager entityManager;


    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Booking booking;
    private final LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(2);

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();

        user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();

        item1 = Item.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .owner(user1.toBuilder().id(1L).build())
                .build();

        item2 = Item.builder()
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .owner(user2.toBuilder().id(2L).build())
                .build();

        booking = Booking.builder()
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .booker(user1.toBuilder().id(1L).build())
                .item(item2.toBuilder().id(2L).build())
                .build();
    }

    void init() {
        userDAO.save(user1);
        userDAO.save(user2);
        itemDAO.save(item1);
        itemDAO.save(item2);
    }

    @Test
    @DisplayName("DataJpaTest: обновить у Booking поле status, возвращается ответ: Booking со статусом APPROVED.")
    void testUpdateBooking_ReturnsBookingWithApprovedStatus() {
        log.info("Start test: обновить у Booking поле status.");
        init();
        Booking bookingFromDB = bookingDAO.save(booking);
        bookingDAO.approvedBooking(bookingFromDB.getId(), "APPROVED");
        entityManager.clear();

        assertEquals(Status.APPROVED, bookingDAO.findById(bookingFromDB.getId()).get().getStatus());

        log.info("End test: обновить у Booking поле status, возвращается ответ: Booking со статусом APPROVED.");
    }
}