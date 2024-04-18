package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import static ru.practicum.shareit.constant.Constant.EMAIL_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRequestDto {

    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be null.")
    public Long id;

    @NotBlank(message = "The name must not be empty.")
    private String name;

    @NotBlank(message = "The email must not be empty.")
    @Email(regexp = EMAIL_REGEX, message = "The email is incorrect.")
    private String email;


}
