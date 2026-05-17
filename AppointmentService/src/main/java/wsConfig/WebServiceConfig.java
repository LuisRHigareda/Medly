package wsConfig;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {
    
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    
    // Se generará en: http://localhost:8080/ws/appointments.wsdl
    @Bean
    public DefaultWsdl11Definition appointments(XsdSchema appointmentsSchema) { // Corregido: Usamos la interfaz XsdSchema
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AppointmentsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://itson.edu.mx/soap/appointments");
        wsdl11Definition.setSchema(appointmentsSchema); // Inyectamos el bean corregido
        return wsdl11Definition;
    }
    
    @Bean
    public XsdSchema appointmentsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/AppointmentServiceContract.xsd"));
    }
}