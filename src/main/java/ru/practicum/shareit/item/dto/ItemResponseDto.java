package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.generic.BaseDto;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemResponseDto extends BaseDto {
    private String name;
    private String description;
    private boolean available;
}
