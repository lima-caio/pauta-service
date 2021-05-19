package com.limac.pautaservice.rest.client;

import com.limac.pautaservice.exception.CpfInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpMethod.GET;

/**
 * Implementação do Client do serviço de CPF.
 */
@Slf4j
@Service
public class CpfClientImpl implements CpfClient {

    private final RestTemplate restTemplate;

    private final String cpfServiceUrl;

    public CpfClientImpl(RestTemplate restTemplate, @Value("${cpfService.url}") String cpfServiceUrl) {
        this.restTemplate = restTemplate;
        this.cpfServiceUrl = cpfServiceUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResponseEntity<T> getForObject(String cpf, ParameterizedTypeReference<T> responseType) {
        try {
            final ResponseEntity<T> responseEntity = restTemplate
                .exchange(cpfServiceUrl + cpf, GET, new HttpEntity<>(new HttpHeaders()), responseType, cpf);

            log.info("Cpf Service [{}, {}]",
                kv("cpf", cpf),
                kv("response", responseEntity));

            return responseEntity;

        } catch (RestClientException exception) {
            throw new CpfInvalidoException(String.format("Associado '%s' não pode votar", cpf), exception);
        }
    }
}
