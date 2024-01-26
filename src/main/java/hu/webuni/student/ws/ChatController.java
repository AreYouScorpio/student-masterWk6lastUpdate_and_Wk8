package hu.webuni.student.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {

// tanari verzio - @PreAuthorize nem mukodik
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat") // this method runs if call is coming to here
    @PreAuthorize("#message.sender == authentication.principal.username")//ne kuldhessen a course-ra nem feliratkozott uzenetet a course-be
    public void send(ChatMessage message) throws Exception{
        messagingTemplate.convertAndSend(
                "/topic/courseChat/" + message.getCourseId(),
                //String.format("%s: %s :%s", message.getSender(), message.getText(), message.getTimestamp()));
                String.format("%s: %s", message.getSender(), message.getText()));

    }



/* sajat verzio
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void send(ChatMessage message) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            if (message.getSender().equals(username)) {
                messagingTemplate.convertAndSend(
                        "/topic/courseChat/" + message.getCourseId(),
                        String.format("%s: %s :%s", message.getSender(), message.getText(), message.getTimestamp()));
            } else {
                // Handle unauthorized sender
            }
        } else {
            // Handle unauthenticated user
        }
    }
    ez sem mukodott
 */

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