package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
            "restControllers",
            "interfaces",
            "services",
            "repositories",
            "soapControllers",
            "wsConfig"
        })
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "entities")
public class AppointmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}

}