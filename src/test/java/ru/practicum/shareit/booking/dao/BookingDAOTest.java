package ru.practicum.shareit.booking.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingDAOTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private BookingDAO bookingDAO;

    private User user1;
    private User user2;
    private Item item1FromUser1;
    private Item item2FromUser1;
    private Item item3FromUser2;
    private Booking booking1WithUser1AndItem3;
    private Booking booking2WithUser2AndItem1;
    private Booking booking3WithUser2AndItem1;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime startTime = now.plusHours(1);
    private final LocalDateTime endTime = now.plusHours(2);

    @BeforeEach
    @Transactional
    void init() {
        user1 = User.builder().name("RuRu").email("RuRu@yandex.ru").build();
        user2 = User.builder().name("ComCom").email("ComCom@gmail.com").build();
        userDAO.save(user1);
        userDAO.save(user2);

        item1FromUser1 = Item.builder().name("Дрель").description("Простая дрель").owner(user1).build();
        item2FromUser1 = Item.builder().name("Отвертка").description("Аккумуляторная отвертка").owner(user1).build();
        item3FromUser2 = Item.builder().name("Телевизор").description("Телевизор 40 дюймов.").owner(user2).build();
        itemDAO.save(item1FromUser1);
        itemDAO.save(item2FromUser1);
        itemDAO.save(item3FromUser2);

        booking1WithUser1AndItem3 = Booking.builder().start(startTime).end(endTime).status(Status.WAITING).booker(user1).item(item3FromUser2).build();
        booking2WithUser2AndItem1 = Booking.builder().start(startTime).end(endTime).status(Status.WAITING).booker(user2).item(item1FromUser1).build();
        booking3WithUser2AndItem1 = Booking.builder().start(startTime).end(endTime).status(Status.WAITING).booker(user2).item(item2FromUser1).build();
        bookingDAO.save(booking1WithUser1AndItem3);
        bookingDAO.save(booking2WithUser2AndItem1);
        bookingDAO.save(booking3WithUser2AndItem1);
    }


    @Test
    @DisplayName("DataJpaTest: обновить у Booking поле status, возвращается ответ: Booking со статусом APPROVED.")
    void testUpdateBooking_ReturnsBookingWithApprovedStatus() {
        log.info("Start test: обновить у Booking поле status.");

        bookingDAO.approvedBooking(booking1WithUser1AndItem3.getId(), "APPROVED");
        entityManager.clear();

        assertEquals(Status.APPROVED, bookingDAO.findById(booking1WithUser1AndItem3.getId()).get().getStatus());

        log.info("End test: обновить у Booking поле status, возвращается ответ: Booking со статусом APPROVED.");
    }


}