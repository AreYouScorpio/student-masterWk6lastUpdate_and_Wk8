package hu.webuni.student.ws;

import lombok.Data;

import java.time.OffsetDateTime;

@Data // @Data is a convenient shortcut annotation that bundles the features of @ToString , @EqualsAndHashCode , @Getter / @Setter and @RequiredArgsConstructor together: In other words, @Data generates all the boilerplate that is normally associated with simple POJOs (Plain Old Java Objects) and beans
public class ChatMessage {

    private String sender;

    private long courseId;
    private String text;
    //private OffsetDateTime timestamp;

}
