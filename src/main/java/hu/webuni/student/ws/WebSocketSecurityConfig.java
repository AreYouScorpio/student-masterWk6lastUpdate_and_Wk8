package hu.webuni.student.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {


    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        //messages.simpSubscribeDestMatchers("/topic/courseChat/{courseId}").hasAuthority("admin"); //-> ha bonyolultabb, akkor szervezzuk ki
        messages.simpSubscribeDestMatchers("/topic/courseChat/{courseId}")
                .access("@courseChatGuard.checkCourseId(authentication, #courseId)"); //szabalyok kiszervezese, az url es az authorization alapjan dont, ezek az un guard-ok
                // # - hashmark-kal tudjuk behivatkozni az url-nek a /{courseId} darabjat
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
