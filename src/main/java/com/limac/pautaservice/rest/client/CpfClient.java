package com.limac.pautaservice.rest.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

/**
 * Client do serviço de CPF.
 */
public interface CpfClient {

    /**
     * Chama o serviço de CPF para validação.
     *
     * @param cpf          cpf
     * @param responseType response type
     * @return response entity
     */
    <T> ResponseEntity<T> getForObject(String cpf, ParameterizedTypeReference<T> responseType);
}
