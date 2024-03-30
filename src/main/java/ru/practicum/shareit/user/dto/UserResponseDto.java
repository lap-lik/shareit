package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.generic.BaseDto;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserResponseDto extends BaseDto {
    private String name;
    private String email;
}
