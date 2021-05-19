package com.limac.pautaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starting point of the service.
 */
@SuppressWarnings("PMD.UseUtilityClass")
@SpringBootApplication
public class PautaServiceApplication {

    /**
     * Starting point of springBoot service.
     *
     * @param args string arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(PautaServiceApplication.class, args);
    }
}
