package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentRequestDto {

    @NotBlank(message = "The text must not be empty.")
    private String text;

    @NotNull(message = "The authorId must not be null.")
    private Long itemId;

    @NotNull(message = "The authorId must not be null.")
    private Long authorId;

    @NotNull(message = "The created must not be null.")
    private LocalDateTime created;
}
