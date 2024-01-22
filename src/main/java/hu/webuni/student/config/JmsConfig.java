package hu.webuni.student.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {
    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) { // springMVC-s objectmappert importaljuk
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper); //es megmondjuk ennek, h ezt hasznalja
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type"); // a jsonben benne lesz, milyen tipusu objektum, deserializalashoz ez az info majd kelleni fog, nem fogja tudni enelkul kitalalni
        return converter;
    }

    //a publikalashoz TCP-re, kell egy brokerService-t legyartani, de csak a kuldonel

    // broker mar nem kell, a fogadonal, csak a kuldonel

    // de converter kell, h vissza tudja alakitani Payment object-te

    // es kell, hogy hova akarunk csatlakozni:

        @Bean
        public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer){

            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            ((SingleConnectionFactory) connectionFactory).setClientId("student-master");

            configurer.configure(factory, connectionFactory);
            factory.setSubscriptionDurable(true);
            return factory;

        }

    {

    }



}