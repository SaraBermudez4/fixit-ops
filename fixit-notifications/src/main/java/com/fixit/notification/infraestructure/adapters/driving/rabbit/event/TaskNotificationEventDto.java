package  com.fixit.notification.infraestructure.adapters.driving.rabbit.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.fixit.notification.infraestructure.adapters.driving.rabbit.util.EventConstants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskNotificationEventDto {

    @NotBlank(message = EVENT_TYPE_REQUIRED)
    String eventType;

    @NotBlank(message = PHONE_REQUIRED)
    @Pattern(regexp = PHONE_REGEX, message = PHONE_INVALID_FORMAT)
    String phoneNumber;

    @NotBlank(message = MESSAGE_REQUIRED)
    String message;
}