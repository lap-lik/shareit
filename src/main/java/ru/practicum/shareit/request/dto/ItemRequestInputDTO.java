package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestInputDTO {

    private Long id;

    @NotBlank(message = "The description must not be empty.", groups = Marker.OnCreate.class)
    private String description;

    private Long requesterId;
}
