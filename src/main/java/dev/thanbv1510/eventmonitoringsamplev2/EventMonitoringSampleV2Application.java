package dev.thanbv1510.eventmonitoringsamplev2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class EventMonitoringSampleV2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EventMonitoringSampleV2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
