package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.generic.BaseDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import static ru.practicum.shareit.constant.UserConstant.EMAIL_REGEX;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserRequestDto extends BaseDto {

    @NotBlank(message = "The name must not be empty.")
    private String name;
    @NotBlank(message = "The email must not be empty.")
    @Email(regexp = EMAIL_REGEX, message = "The email is incorrect.")
    private String email;

    @Override
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be null.")
    public Long getId() {
        return super.getId();
    }
}
