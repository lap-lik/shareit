package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentInputDTO {

    @NotBlank(message = "The text must not be empty.")
    private String text;

    private Long itemId;

    private Long authorId;

    private LocalDateTime created;
}
