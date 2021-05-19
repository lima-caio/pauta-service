package com.limac.pautaservice.messaging.publisher;

import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.mapping.PautaMapper;
import com.limac.pautaservice.messaging.callback.PublicarResultadoCallback;
import com.limac.pautaservice.rest.dto.PautaResultadoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Implementação do Publicador de mensagens de {@link Pauta}.
 */
@Slf4j
@Service
public class PautaPublisherImpl implements PautaPublisher {

    private final String topic;
    private final PautaMapper pautaMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Construtor.
     *
     * @param topic         topic
     * @param pautaMapper   pautaMapper
     * @param kafkaTemplate kafkaTemplate
     */
    public PautaPublisherImpl(@Value(value = "${kafka.topic}") String topic, PautaMapper pautaMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.pautaMapper = pautaMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publicarResultado(Pauta pauta) {
        final PautaResultadoDto pautaResultadoDto = pautaMapper.pautaParaPautaResultadoDto(pauta);

        log.info("Publicando {}, {}", kv("mensagem", pautaResultadoDto), kv("topico", topic));

        final ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send(topic, pautaResultadoDto);
        listenableFuture.addCallback(new PublicarResultadoCallback());
    }
}
