package com.limac.pautaservice.messaging.publisher

import com.limac.pautaservice.domain.Pauta
import com.limac.pautaservice.mapping.PautaMapper
import com.limac.pautaservice.rest.dto.PautaResultadoDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.SettableListenableFuture
import spock.lang.Specification

class PautaPublisherImplSpec extends Specification {

    String topic = 'topic'
    PautaMapper pautaMapper = Mock(PautaMapper)
    KafkaTemplate kafkaTemplate = Mock(KafkaTemplate)

    PautaPublisher pautaPublisher = new PautaPublisherImpl(topic, pautaMapper, kafkaTemplate)

    def 'Resultado deve ser publicado sem erros'() {
        given: 'uma Pauta'
        Pauta pauta = Mock(Pauta)
        PautaResultadoDto pautaResultadoDto = Mock(PautaResultadoDto)
        SettableListenableFuture future = new SettableListenableFuture<>()
        future.set(Mock(SendResult))

        1 * pautaMapper.pautaParaPautaResultadoDto(pauta) >> pautaResultadoDto
        1 * kafkaTemplate.send(topic, _) >> future

        when: 'Resultado é publicado'
        pautaPublisher.publicarResultado(pauta)

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'Se acontecer algum erro durante a publicacao o callback deve tratar o erro'() {
        given: 'uma Pauta'
        Pauta pauta = Mock(Pauta)
        PautaResultadoDto pautaResultadoDto = Mock(PautaResultadoDto)
        SettableListenableFuture future = new SettableListenableFuture<>()
        future.setException(new Exception())

        1 * pautaMapper.pautaParaPautaResultadoDto(pauta) >> pautaResultadoDto
        1 * kafkaTemplate.send(topic, _) >> future

        when: 'Resultado é publicado'
        pautaPublisher.publicarResultado(pauta)

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }
}
