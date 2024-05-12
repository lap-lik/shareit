package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserInputDTO {

    private Long id;

    private String name;

    private String email;
}
