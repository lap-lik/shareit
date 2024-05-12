package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentInputDTO {

    private String text;

    private Long itemId;

    private Long authorId;

    private LocalDateTime created;
}
