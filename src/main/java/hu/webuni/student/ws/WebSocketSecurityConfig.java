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


/* alternativ:

A web socket security új stílusú konfigjáról itt lehet olvasni:
https://docs.spring.io/spring-security/reference/6.0/servlet/integrations/websocket.html
Az AbstractSecurityWebSocketMessageBrokerConfigurer deprecated, helyette az @EnableWebSocketSecurity annotáció használható, és az általam a videón mutatott
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages.simpSubscribeDestMatchers("/topic/delay/*").hasAuthority("admin");
	}
helyett így kellene az autorizációt beállítani:
    @Bean
    AuthorizationManager messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .simpSubscribeDestMatchers("/topic/delay/*").hasAuthority("admin");
        return messages.build();
    }

Mégsem álltam át erre a megoldásra, mert az @EnableWebSocketSecurity  egyelőre nem támogatja a CSRF kikapcsolását. Azt egyelőre csak XML konfiggal, vagy pont a videón mutatott régebbi API-val lehet megoldani.
https://docs.spring.io/spring-security/reference/6.1.1/servlet/integrations/websocket.html#websocket-sameorigin-disable

 */


    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
