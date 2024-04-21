package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {

    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be null.")
    private Long id;

    @NotBlank(message = "The name must not be empty.")
    private String name;

    @NotBlank(message = "The description must not be empty.")
    private String description;

    @NotNull(message = "The available must not be null.")
    private Boolean available;

    @NotNull(message = "The ownerId must not be null.")
    private Long ownerId;
}
