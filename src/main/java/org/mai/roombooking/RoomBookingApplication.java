package org.mai.roombooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("org.mai.roombooking.entities")
@EnableJpaRepositories("org.mai.roombooking.repositories")
public class RoomBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomBookingApplication.class, args);
    }

}
