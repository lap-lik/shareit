package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemInputDTO {

    private Long id;

    @NotBlank(message = "The name must not be empty.", groups = Marker.OnCreate.class)
    private String name;

    @NotBlank(message = "The description must not be empty.", groups = Marker.OnCreate.class)
    private String description;

    @NotNull(message = "The available must not be null.", groups = Marker.OnCreate.class)
    private Boolean available;

    private Long ownerId;

    private Long requestId;
}
