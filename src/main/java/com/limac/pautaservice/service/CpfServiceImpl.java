package com.limac.pautaservice.service;

import com.limac.pautaservice.rest.client.CpfClient;
import com.limac.pautaservice.rest.client.dto.CpfResultadoDto;
import com.limac.pautaservice.type.CpfStatusType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementação do Serviço de CPF.
 */
@Service
public class CpfServiceImpl implements CpfService {

    private final CpfClient cpfClient;

    /**
     * Configuração adicionada devido a natureza do serviço disponibilizado. <br>
     * Como o serviço retorna o status aleatoriamente, impossibilita o uso para os testes de integração.
     */
    private final boolean cpfServiceEnabled;

    public CpfServiceImpl(CpfClient cpfClient, @Value("${cpfService.enabled}") boolean cpfServiceEnabled) {
        this.cpfClient = cpfClient;
        this.cpfServiceEnabled = cpfServiceEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValido(String cpf) {
        if (!cpfServiceEnabled) {
            return true;
        }

        final ResponseEntity<CpfResultadoDto> response = cpfClient.getForObject(cpf, new ParameterizedTypeReference<CpfResultadoDto>() {
            });

        if (HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
            return false;
        }

        return CpfStatusType.ABLE_TO_VOTE.equals(Objects.requireNonNull(response.getBody()).getStatus());
    }
}
