package com.limac.pautaservice.service

import com.limac.pautaservice.domain.Pauta
import com.limac.pautaservice.domain.Voto
import com.limac.pautaservice.exception.*
import com.limac.pautaservice.messaging.publisher.PautaPublisher
import com.limac.pautaservice.repository.PautaRepository
import com.limac.pautaservice.type.ResultadoType
import com.limac.pautaservice.type.VotoType
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

class PautaServiceImplSpec extends Specification {

    CpfService cpfService = Mock(CpfService)
    PautaPublisher pautaPublisher = Mock(PautaPublisher)
    PautaRepository pautaRepository = Mock(PautaRepository)
    AgendadorService agendadorService = Mock(AgendadorService)

    PautaService pautaService = new PautaServiceImpl(cpfService, pautaPublisher, pautaRepository, agendadorService)

    def 'Pauta deve ser criado sem erros'() {
        given: 'uma Pauta'
        Pauta pautaMock = Mock(Pauta)

        1 * pautaRepository.insert(pautaMock) >> pautaMock

        when: 'Pauta é criada'
        Pauta pauta = pautaService.criarPauta(pautaMock)

        then: 'pauta deve ser retornada'
        pauta == pautaMock

        and: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'Pauta deve ser encontrada sem erros'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pautaMock = Mock(Pauta)

        1 * pautaRepository.findById(pautaId) >> Optional.of(pautaMock)

        when: 'Pauta é procurada'
        Pauta pauta = pautaService.procurarPauta(pautaId)

        then: 'Pauta deve ser retornada'
        pauta == pautaMock

        and: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'NaoEncontradoException deve ser lancada quando uma Pauta nao e encontrada'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'

        1 * pautaRepository.findById(pautaId) >> Optional.empty()

        when: 'Pauta é procurada'
        pautaService.procurarPauta(pautaId)

        then: 'NaoEncontradoException é lançada'
        thrown(NaoEncontradoException)
    }

    def 'PautaAbertaException deve ser lancada ao tentar abrir uma Pauta que ja esta aberta'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.aberta = true

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)

        when: 'Pauta é aberta'
        pautaService.abrirPauta(pautaId)

        then: 'PautaAbertaException é lançada'
        thrown(PautaAbertaException)
    }

    def 'Pauta deve ser aberta sem erros'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.tempoUnidade = TimeUnit.SECONDS

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        1 * pautaRepository.save(pauta) >> pauta
        1 * agendadorService.agendarExecucao(_, pauta.tempoDuracao, pauta.tempoUnidade)
        sleep(2000)

        when: 'Pauta é aberta'
        pautaService.abrirPauta(pautaId)

        then: 'Pauta deve ser aberta'
        pauta.aberta
    }

    @Unroll
    def 'Pauta deve ser fechada sem erros'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.aberta = true
        pauta.sim = sim
        pauta.nao = nao

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        1 * pautaRepository.save(pauta) >> pauta

        when: 'Pauta é fechada'
        pautaService.fecharPauta(pautaId)

        then: 'Pauta deve ser fechada'
        !pauta.aberta

        and: 'Resultado deve ser obtido'
        pauta.resultado == resultado

        where:
        sim | nao   | resultado
        1   | 0     | ResultadoType.SIM
        0   | 1     | ResultadoType.NAO
        0   | 0     | ResultadoType.EMPATE
    }

    def 'Voto deve ser adicionado a Pauta sem erros'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.aberta = true
        Voto voto = new Voto()
        voto.cpf = 'cpf'
        voto.votoType = votoType

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        1 * cpfService.isValido(voto.cpf) >> true
        1 * pautaRepository.save(pauta) >> pauta

        when: 'Voto é adicionado à Pauta'
        pautaService.adicionarVoto(pautaId, voto)

        then: 'Voto deve ser adicionado à Pauta'
        pauta.votos.contains(voto)

        and: 'Contagem deve ser atualizada'
        pauta.sim == sim
        pauta.nao == nao

        where:
        votoType        | sim   | nao
        VotoType.SIM    | 1     | 0
        VotoType.NAO    | 0     | 1
    }

    def 'PautaFechadaException deve ser lancada ao tentar adicionar um Voto quando a Pauta esta fechada'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        Voto voto = new Voto()
        voto.votoType = VotoType.SIM

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        0 * pautaRepository.save(pauta)

        when: 'Voto é adicionado à Pauta'
        pautaService.adicionarVoto(pautaId, voto)

        then: 'PautaFechadaException é lançada'
        thrown(PautaFechadaException)
    }

    def 'CpfInvalidoException deve ser lancada ao tentar adicionar um Voto com um cpf invalido'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.aberta = true
        Voto voto = new Voto()
        voto.cpf = 'cpfInvalido'
        voto.votoType = VotoType.SIM

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        1 * cpfService.isValido(voto.cpf) >> false
        0 * pautaRepository.save(pauta)

        when: 'Voto é adicionado à Pauta'
        pautaService.adicionarVoto(pautaId, voto)

        then: 'CpfInvalidoException é lançada'
        thrown(CpfInvalidoException)
    }

    def 'VotoExistenteException deve ser lancada ao tentar adicionar um Voto para um cpf ja existente'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()
        pauta.aberta = true
        Voto votoExistente = new Voto()
        votoExistente.cpf = 'cpfExistente'
        votoExistente.votoType = VotoType.SIM
        pauta.votos = [votoExistente]
        Voto voto = new Voto()
        voto.cpf = 'cpfExistente'
        voto.votoType = VotoType.SIM

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)
        1 * cpfService.isValido(voto.cpf) >> true
        0 * pautaRepository.save(pauta)

        when: 'Voto é adicionado à Pauta'
        pautaService.adicionarVoto(pautaId, voto)

        then: 'VotoExistenteException é lançada'
        thrown(VotoExistenteException)
    }

    def 'Resultado da Pauta deve ser retornado sem erros'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = new Pauta()

        1 * pautaRepository.findById(pautaId) >> Optional.of(pauta)

        when: 'resultado da Pauta é buscado'
        Pauta pautaResultado = pautaService.buscarResultado(pautaId)

        then: 'Pauta deve ser retornada'
        pautaResultado == pauta

        and: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }
}
