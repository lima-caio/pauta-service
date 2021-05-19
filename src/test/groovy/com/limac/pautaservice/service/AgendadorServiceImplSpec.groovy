package com.limac.pautaservice.service

import spock.lang.Specification

import java.util.concurrent.TimeUnit

class AgendadorServiceImplSpec extends Specification {

    AgendadorService agendadorService = new AgendadorServiceImpl()

    def 'Pauta deve ser fechada sem erros quando o comando e executado'() {
        given: 'um comando para ser executado em um determinado tempo'
        Runnable runnable = Mock(Runnable)
        long tempoEspera = 1
        TimeUnit tempoUnidade = TimeUnit.SECONDS

        when: 'Comando é agendado'
        agendadorService.agendarExecucao(runnable, tempoEspera, tempoUnidade)

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }
}
