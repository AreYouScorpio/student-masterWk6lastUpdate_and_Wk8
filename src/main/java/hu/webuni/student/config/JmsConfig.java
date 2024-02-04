package hu.webuni.student.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.api.core.TransportConfiguration;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {
    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) { // springMVC-s objectmappert importaljuk, ami mar megvan nekunk am is
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper); //es megmondjuk ennek, h ezt hasznalja .. enelkul nem tudna serializalni az OffserDateTime-ot pl a flight-os pelda DalayMessage-ebol
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type"); // a jsonben benne lesz, milyen tipusu objektum, deserializalashoz ez az info majd kelleni fog, nem fogja tudni enelkul kitalalni
        return converter;
    }

    //a publikalashoz TCP-re, kell egy brokerService-t legyartani, de csak a kuldonel (hogy mas is elerje az embedded activeMQ-nkat

    // broker mar nem kell, a fogadonal, csak a kuldonel

    // de converter kell, h vissza tudja alakitani Payment object-te

    // es kell, hogy hova akarunk csatlakozni:

/*
        @Bean
        public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer){

            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();     // Create a new instance of DefaultJmsListenerContainerFactory
            ((SingleConnectionFactory) connectionFactory).setClientId("student-master");     // Set the client ID for the JMS listener container

            configurer.configure(factory, connectionFactory);     // Configure the listener container factory using the provided configurer
            factory.setSubscriptionDurable(true);     // Enable durable subscriptions for the listener container
            return factory;     // Return the configured listener container factory

        }



 */         //ehelyett szetszedve:

    @Bean
    public ConnectionFactory financeConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        return connectionFactory;
    }

    @Bean
    public ConnectionFactory educationConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61617");
        return connectionFactory;
    }

    @Bean
    public JmsTemplate educationTemplate(ObjectMapper objectMapper) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(educationConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter(objectMapper));
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> financeFactory(ConnectionFactory financeConnectionFactory,
                                                         DefaultJmsListenerContainerFactoryConfigurer configurer) {

        return setPubSubAndDurableSubscription(financeConnectionFactory, configurer, "student-master");
    }


    private JmsListenerContainerFactory<?> setPubSubAndDurableSubscription(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer,
            String clientId) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setClientId(clientId);
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        factory.setSubscriptionDurable(true);

        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> educationFactory(ConnectionFactory educationConnectionFactory,
                                                           DefaultJmsListenerContainerFactoryConfigurer configurer) {

        return setPubSubAndDurableSubscription(educationConnectionFactory, configurer, "student-master");
    }





}