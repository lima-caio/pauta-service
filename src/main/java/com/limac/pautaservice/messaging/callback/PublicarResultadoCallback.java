package com.limac.pautaservice.messaging.callback;

import com.limac.pautaservice.domain.Pauta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Implementação do Callback para publicação de resultados de {@link Pauta}.
 */
@Slf4j
@RequiredArgsConstructor
public class PublicarResultadoCallback implements ListenableFutureCallback<SendResult<String, Object>> {

    /**
     * Loga a mensagem de sucesso.
     */
    @Override
    public void onSuccess(SendResult<String, Object> sendResult) {
        log.info("A mensagem foi publicada");
    }

    /**
     * Loga a mensagem de erro.
     */
    @Override
    public void onFailure(Throwable throwable) {
        log.error("Kafka indisponível. A mensagem não foi publicada");
    }

}
