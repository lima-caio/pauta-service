package com.limac.pautaservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implementação do Serviço para executar operações que necessitam agendamento.
 */
@Slf4j
@Service
public class AgendadorServiceImpl implements AgendadorService {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * {@inheritDoc}
     */
    @Override
    public void agendarExecucao(Runnable runnable, long tempoEspera, TimeUnit tempoUnidade) {
        scheduledExecutorService.schedule(runnable, tempoEspera, tempoUnidade);
    }
}
