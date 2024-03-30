package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.generic.BaseEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private String name;
    private String email;
}
