package com.limac.pautaservice.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração de {@link RestTemplate}.
 */
@Configuration
public class RestTemplateConfiguration {

    /**
     * Configura um {@link RestTemplate} bean.
     *
     * @return {@link RestTemplate} bean.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
