package com.limac.pautaservice.service;

/**
 * Serviço de CPF.
 */
public interface CpfService {

    /**
     * Valida um CPF.
     *
     * @param cpf cpf para ser validado.
     * @return se o CPF é valido.
     */
    boolean isValido(String cpf);
}
