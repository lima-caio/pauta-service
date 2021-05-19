package com.limac.pautaservice.service;

import java.util.concurrent.TimeUnit;

/**
 * Serviço para executar operações que necessitam agendamento.
 */
public interface AgendadorService {

    /**
     * Agenda a execução.
     *
     * @param runnable execução agendada.
     * @param tempoEspera tempo de espera para execução.
     * @param unidadeTempo unidade do tempo de espera.
     */
    void agendarExecucao(Runnable runnable, long tempoEspera, TimeUnit unidadeTempo);
}
