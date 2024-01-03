package hu.webuni.student.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.core.MessagePostProcessor;

import java.time.OffsetDateTime;

@Data // @Data is a convenient shortcut annotation that bundles the features of @ToString , @EqualsAndHashCode , @Getter / @Setter and @RequiredArgsConstructor together: In other words, @Data generates all the boilerplate that is normally associated with simple POJOs (Plain Old Java Objects) and beans
@AllArgsConstructor
public class CourseMessage {

    private String message;
    private OffsetDateTime timestamp;

}
