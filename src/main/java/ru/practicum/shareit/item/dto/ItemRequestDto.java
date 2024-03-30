package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.generic.BaseDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemRequestDto extends BaseDto {

    @NotBlank(message = "The name must not be empty.")
    private String name;
    @NotBlank(message = "The description must not be empty.")
    private String description;
    @NotNull(message = "The available must not be null.")
    private Boolean available;
    private Long ownerId;

    @Override
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be null.")
    public Long getId() {
        return super.getId();
    }
}
