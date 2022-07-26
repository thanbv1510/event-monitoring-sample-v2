package dev.thanbv1510.eventmonitoringsamplev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class EventMonitoringSampleV2Application {

    public static void main(String[] args) {
        SpringApplication.run(EventMonitoringSampleV2Application.class, args);
    }
}
