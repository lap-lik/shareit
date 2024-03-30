package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.generic.BaseEntity;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseEntity {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
