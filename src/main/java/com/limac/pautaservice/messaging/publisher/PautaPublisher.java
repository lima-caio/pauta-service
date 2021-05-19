package com.limac.pautaservice.messaging.publisher;

import com.limac.pautaservice.domain.Pauta;

/**
 * Publicador de mensagens de {@link Pauta}.
 */
public interface PautaPublisher {

    /**
     * Publica o resultado da {@link Pauta}.
     *
     * @param pauta pauta
     */
    void publicarResultado(Pauta pauta);
}
