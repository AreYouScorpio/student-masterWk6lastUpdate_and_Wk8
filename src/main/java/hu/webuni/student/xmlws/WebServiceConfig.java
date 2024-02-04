package hu.webuni.student.xmlws;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.cxf.jaxws.EndpointImpl;
import jakarta.xml.ws.Endpoint;


@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {

    private final Bus bus;
    private final TimetableWs timetableWs;

    @Bean
    public Endpoint endPoint() {
        EndpointImpl endpointImpl = new EndpointImpl(bus, timetableWs);
        endpointImpl.publish("/timetable");
        return endpointImpl;
    }

}
