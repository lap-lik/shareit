package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestInputDTO {

    private Long id;

    @NotBlank(message = "The description must not be empty.", groups = Marker.OnCreate.class)
    private String description;

    private Long requesterId;
}
