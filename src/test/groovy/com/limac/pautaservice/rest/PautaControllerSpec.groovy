package com.limac.pautaservice.rest

import com.limac.pautaservice.domain.Pauta
import com.limac.pautaservice.domain.Voto
import com.limac.pautaservice.mapping.PautaMapper
import com.limac.pautaservice.mapping.VotoMapper
import com.limac.pautaservice.rest.dto.PautaCriacaoDto
import com.limac.pautaservice.rest.dto.PautaDto
import com.limac.pautaservice.rest.dto.PautaResultadoDto
import com.limac.pautaservice.rest.dto.VotoAddDto
import com.limac.pautaservice.rest.dto.VotoDto
import com.limac.pautaservice.service.PautaService
import spock.lang.Specification

class PautaControllerSpec extends Specification {

    VotoMapper votoMapper = Mock(VotoMapper)
    PautaMapper pautaMapper = Mock(PautaMapper)
    PautaService pautaService = Mock(PautaService)

    PautaController pautaController = new PautaController(votoMapper, pautaMapper, pautaService)

    def 'Pauta deve ser criado sem erros ao receber um PautaCriacaoDto'() {
        given: 'um PautaCriacaoDto'
        PautaCriacaoDto pautaCriacaoDto = Mock(PautaCriacaoDto)
        Pauta pauta = Mock(Pauta)
        PautaDto pautaDtoMock = Mock(PautaDto)

        1 * pautaMapper.pautaCriacaoDtoParaPauta(pautaCriacaoDto) >> pauta
        1 * pautaService.criarPauta(pauta) >> pauta
        1 * pautaMapper.pautaParaPautaDto(pauta) >> pautaDtoMock

        when: 'Pauta é criada'
        PautaDto pautaDto = pautaController.criarPauta(pautaCriacaoDto)

        then: 'PautaDto deve ser retornado'
        pautaDto == pautaDtoMock

        and: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'Pauta deve ser aberta sem erros ao receber um pautaId'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'

        1 * pautaService.abrirPauta(pautaId)

        when: 'Pauta é aberta'
        pautaController.abrirPauta(pautaId)

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'Voto deve ser adicionado sem erros ao receber um VotoAddDto'() {
        given: 'um VotoAddDto'
        VotoAddDto votoAddDto = Mock(VotoAddDto)
        String pautaId = 'pautaId'
        VotoDto votoCriacaoDto = Mock(VotoDto)
        Voto voto = Mock(Voto)
        votoAddDto.pautaId >> pautaId
        votoAddDto.votoDto >> votoCriacaoDto

        1 * votoMapper.votoDtoParaVoto(votoCriacaoDto) >> voto
        1 * pautaService.adicionarVoto(pautaId, voto)

        when: 'Voto é adicionado à Pauta'
        pautaController.addVoto(votoAddDto)

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }

    def 'Resultado da Pauta deve ser retornado sem erros ao receber um pautaId'() {
        given: 'um pautaId'
        String pautaId = 'pautaId'
        Pauta pauta = Mock(Pauta)
        PautaResultadoDto pautaResultadoDtoMock = Mock(PautaResultadoDto)

        1 * pautaService.buscarResultado(pautaId) >> pauta
        1 * pautaMapper.pautaParaPautaResultadoDto(pauta) >> pautaResultadoDtoMock

        when: 'Resultado é buscado'
        PautaResultadoDto pautaResultadoDto = pautaController.buscarResultado(pautaId)

        then: 'PautaResultadoDto deve ser retornado'
        pautaResultadoDto == pautaResultadoDtoMock

        and: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }
}
