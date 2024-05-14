package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestInputDTO {

    private Long id;

    private String description;

    private Long requesterId;
}
