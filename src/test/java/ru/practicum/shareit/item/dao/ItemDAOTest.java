package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@DataJpaTest
@Transactional(readOnly = true)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemDAOTest {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ItemDAO itemDAO;

    private User user1;
    private User user2;
    private Item item1FromUser1;
    private Item item2FromUser1;
    private Item item1FromUser2;

    @BeforeEach
    @Transactional
    public void init() {
        user1 = User.builder().name("RuRu").email("RuRu@yandex.ru").build();
        user2 = User.builder().name("ComCom").email("ComCom@gmail.com").build();
        userDAO.save(user1);
        userDAO.save(user2);

        item1FromUser1 = Item.builder().name("Дрель").description("Простая дрель").owner(user1).build();
        item2FromUser1 = Item.builder().name("Отвертка").description("Аккумуляторная отвертка").owner(user1).build();
        item1FromUser2 = Item.builder().name("Набор инструментов").description("В наборе молоток, отвертки, шурупы.").owner(user2).build();
        itemDAO.save(item1FromUser1);
        itemDAO.save(item2FromUser1);
        itemDAO.save(item1FromUser2);
    }

    @Test
    @DisplayName("DataJpaTest: поиск предметов по ID создателя, возвращается корректное количество предметов.")
    public void testFindAllByOwnerId_OrderById_ReturnCorrectItemsReturned() {
        log.info("Start test: поиск предметов по ID создателя.");

        List<Item> itemsByUser1 = itemDAO.findAllByOwnerIdOrderById(user1.getId(), 0, 10);
        assertThat(itemsByUser1).containsExactly(item1FromUser1, item2FromUser1);

        List<Item> itemsByUser2 = itemDAO.findAllByOwnerIdOrderById(user2.getId(), 0, 10);
        assertThat(itemsByUser2).containsExactly(item1FromUser2);

        log.info("End test: поиск предметов по ID создателя, возвращается корректное количество предметов.");
    }

    @Test
    @DisplayName("DataJpaTest: поиск предметов по ID создателя, с использованием параметров пагинации, " +
            "возвращается корректное количество предметов.")
    public void testFindAllByOwnerId_OrderByIdAndOffset_ReturnCorrectItemsReturned() {
        log.info("Start test: поиск предметов по ID создателя, с использованием параметров пагинации.");

        List<Item> itemsByUser1WithOffset1 = itemDAO.findAllByOwnerIdOrderById(user1.getId(), 1, 2);
        assertThat(itemsByUser1WithOffset1).containsExactly(item2FromUser1);

        List<Item> itemsByUser1WithOffset2 = itemDAO.findAllByOwnerIdOrderById(user1.getId(), 2, 2);
        assertThat(itemsByUser1WithOffset2).isEmpty();

        log.info("End test: поиск предметов по ID создателя, с использованием параметров пагинации, " +
                "возвращается корректное количество предметов.");
    }

    @Test
    @DisplayName("DataJpaTest: поиск предметов по части названия или описания, возвращается корректное количество предметов.")
    void testFindAllByContainsText_ReturnCorrectItemsReturned() {
        log.info("Start test: поиск предметов по части названия или описания.");

        List<Item> itemsByText1 = itemDAO.findAllByNameOrDescriptionContains("вер", 0, 2);
        assertThat(itemsByText1).containsExactly(item2FromUser1, item1FromUser2);

        List<Item> itemsByText2 = itemDAO.findAllByNameOrDescriptionContains("р", 0, 2);
        assertThat(itemsByText2).containsExactly(item1FromUser1, item2FromUser1);

        log.info("End test: поиск предметов по части названия или описания, возвращается корректное количество предметов.");
    }

    @Test
    @DisplayName("DataJpaTest: поиск предметов по части названия или описания с использованием параметров пагинации, " +
            "возвращается корректное количество предметов.")
    void testFindAllByContainsText_OrderByIdAndOffset_ReturnCorrectItemsReturned() {
        log.info("Start test: поиск предметов по части названия или описания с использованием параметров пагинации.");

        List<Item> itemsByText1WithOffset1 = itemDAO.findAllByNameOrDescriptionContains("вер", 0, 2);
        assertThat(itemsByText1WithOffset1).containsExactly(item2FromUser1, item1FromUser2);

        List<Item> itemsByText2WithOffset2 = itemDAO.findAllByNameOrDescriptionContains("р", 2, 2);
        assertThat(itemsByText2WithOffset2).containsExactly(item1FromUser2);
        log.info("End test: поиск предметов по части названия или описания с использованием параметров пагинации, " +
                "возвращается корректное количество предметов.");
    }
}
