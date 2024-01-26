package hu.webuni.student.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat") // this method runs if call is coming to here
    @PreAuthorize("message.sender == authentication.principal.username")//ne kuldhessen a course-ra nem feliratkozott uzenetet a course-be
    public void send(ChatMessage message) throws Exception{
        messagingTemplate.convertAndSend(
                "/topic/courseChat/" + message.getCourseId(),
                String.format("%s: %s :%s", message.getSender(), message.getText(), message.getTimestamp()));

    }

}


/* @Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void send(Principal principal, ChatMessage message) throws Exception {
        // Access authentication details
        Authentication authentication = (Authentication) principal;
        Jwt jwt = (Jwt) authentication.getCredentials();

        // Extract user information from JWT
        String username = jwt.getClaim("username");

        messagingTemplate.convertAndSend(
                "/topic/courseChat/" + message.getCourseId(),
                String.format("%s: %s :%s", username, message.getText(), message.getTimestamp()));
    }
}
*/