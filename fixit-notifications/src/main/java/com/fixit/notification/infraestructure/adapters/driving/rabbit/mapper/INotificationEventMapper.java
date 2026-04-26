package  com.fixit.notification.infraestructure.adapters.driving.rabbit.mapper;
import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.event.TaskNotificationEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationEventMapper {

    @Mapping(source = "phoneNumber", target = "toPhoneNumber")
    @Mapping(source = "message",     target = "message")
    SmsNotification toModel(TaskNotificationEventDto dto);
}