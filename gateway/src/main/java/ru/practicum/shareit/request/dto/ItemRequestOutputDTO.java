package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestOutputDTO {

    private Long id;

    private String description;

    private Long requesterId;

    private LocalDateTime created;

    private List<ItemShortOutputDTO> items;
}
