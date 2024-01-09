package hu.webuni.student.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat") // this method runs if call is coming to here
    public void send(ChatMessage message) throws Exception{
        messagingTemplate.convertAndSend(
                "/topic/courseChat/" + message.getCourseId(),
                String.format("%s: %s", message.getSender(), message.getText()));

    }

}
