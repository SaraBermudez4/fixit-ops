package com.fixit.user.infraestructure.adapters.driving.rest.dto.request;

import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.infraestructure.adapters.driving.rest.util.RestConstants;
import com.fixit.user.infraestructure.adapters.driving.rest.validation.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.fixit.user.domain.util.constants.AuthConstants.PHONE_FORMAT_REGEX;
import static com.fixit.user.domain.util.constants.AuthConstants.PHONE_REGEX_EXCEPTION_MESSAGE;

public record UserRequest(
        @NotBlank(message = RestConstants.DNI_REQUIRED)
        @Size(max = 20, message = RestConstants.DNI_SIZE)
        String dni,

        @NotBlank(message = RestConstants.NAME_REQUIRED)
        @Size(max = 100, message = RestConstants.NAME_SIZE)
        String name,

        @NotBlank(message = RestConstants.LAST_NAME_REQUIRED)
        @Size(max = 100, message = RestConstants.LAST_NAME_SIZE)
        String lastName,

        @NotBlank(message = RestConstants.EMAIL_REQUIRED)
        @Email(message = RestConstants.EMAIL_VALID)
        String email,

        @NotBlank(message = RestConstants.PASSWORD_REQUIRED)
        @Size(min = 8, max = 100, message = RestConstants.PASSWORD_SIZE)
        String password,

        @NotBlank(message = RestConstants.PHONE_NUMBER_REQUIRED)
        @Pattern(regexp = PHONE_FORMAT_REGEX, message = PHONE_REGEX_EXCEPTION_MESSAGE)
        String phoneNumber,

        @ValidEnum(
                enumClass = TechnicianCategory.class,
                message = RestConstants.CATEGORY_VALID
        )
        String category
) {
}