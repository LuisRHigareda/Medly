package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
            "controller",
            "model"
        }
)
public class MedlyAppointmentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedlyAppointmentsApplication.class, args);
    }
}