package com.limac.pautaservice.mapping

import com.limac.pautaservice.domain.Pauta
import com.limac.pautaservice.domain.Voto
import com.limac.pautaservice.rest.dto.PautaCriacaoDto
import com.limac.pautaservice.rest.dto.PautaDto
import com.limac.pautaservice.rest.dto.PautaResultadoDto
import com.limac.pautaservice.type.ResultadoType
import com.limac.pautaservice.type.VotoType
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

class PautaMapperImplSpec extends Specification {

    PautaMapper pautaMapper = Mappers.getMapper(PautaMapper)

    @Unroll
    def 'PautaCriacaoDto deve ser mapeada para Pauta'() {
        given: 'um PautaCriacaoDto'
        PautaCriacaoDto pautaCriacaoDto = new PautaCriacaoDto()
        pautaCriacaoDto.descricao = descricao
        pautaCriacaoDto.tempoDuracao = tempoDuracao

        when: 'Pauta é criada'
        Pauta pauta = pautaMapper.pautaCriacaoDtoParaPauta(pautaCriacaoDto)

        then: 'Pauta deve ser mapeada'
        pauta.pautaId != null
        pauta.descricao == descricao
        pauta.tempoDuracao == tempoDuracaoEsperado
        pauta.tempoUnidade == TimeUnit.MINUTES
        !pauta.aberta
        pauta.votos.isEmpty()
        pauta.sim == 0
        pauta.nao == 0

        where:
        descricao     | tempoDuracao    | tempoDuracaoEsperado
        'descrição'   | 2               | 2
        'descrição'   | 1               | 1
        'descrição'   | 0               | 1
        ''            | -1              | 1
        null          | null            | 1
    }

    def 'Pauta deve ser mapeada para PautaDto'() {
        given: 'uma Pauta'
        Pauta pauta = new Pauta()
        pauta.pautaId = pautaId
        pauta.descricao = descricao
        pauta.tempoDuracao = tempoDuracao
        Voto voto = new Voto()
        voto.cpf = cpf
        voto.votoType = votoType
        pauta.votos << voto

        when: 'PautaDto é criada'
        PautaDto pautaDto = pautaMapper.pautaParaPautaDto(pauta)

        then: 'PautaDto deve ser mapeada'
        pautaDto.pautaId == pautaId
        pautaDto.descricao == descricao
        pautaDto.tempoDuracao == tempoDuracao
        pautaDto.votos[0].cpf == cpf
        pautaDto.votos[0].votoType == votoType

        where:
        pautaId     | descricao      | tempoDuracao  | cpf      | votoType
        'pautaId'   | 'descrição'    | 2             | 'cpf'    | VotoType.SIM
    }

    @Unroll
    def 'Pauta deve ser mapeada para PautaResultadoDto'() {
        given: 'uma Pauta'
        Pauta pauta = new Pauta()
        pauta.pautaId = pautaId
        pauta.descricao = descricao
        pauta.sim = sim
        pauta.nao = nao
        pauta.resultado = resultado

        when: 'PautaResultadoDto é criada'
        PautaResultadoDto pautaResultadoDto = pautaMapper.pautaParaPautaResultadoDto(pauta)

        then: 'PautaResultadoDto deve ser mapeada'
        pautaResultadoDto.pautaId == pautaId
        pautaResultadoDto.descricao == descricao
        pautaResultadoDto.sim == sim
        pautaResultadoDto.nao == nao
        pautaResultadoDto.resultado == resultado

        where:
        pautaId     | descricao     | sim   | nao   | resultado
        'pautaId'   | 'descrição'   | 1     | 0     | ResultadoType.SIM
        'pautaId'   | 'descrição'   | 0     | 1     | ResultadoType.NAO
    }

    def 'Pauta com resultado empatado deve ser mapeada para PautaResultadoDto com resultado EMPATE'() {
        given: 'uma Pauta'
        Pauta pauta = new Pauta()
        pauta.pautaId = pautaId
        pauta.descricao = descricao
        pauta.resultado = resultado

        when: 'PautaDto é criada'
        PautaResultadoDto pautaResultadoDto = pautaMapper.pautaParaPautaResultadoDto(pauta)

        then: 'PautaDto deve ser mapeada'
        pautaResultadoDto.pautaId == pautaId
        pautaResultadoDto.descricao == descricao
        pautaResultadoDto.sim == 0
        pautaResultadoDto.nao == 0
        pautaResultadoDto.resultado == resultado

        where:
        pautaId     | descricao     | resultado
        'pautaId'   | 'descrição'   | ResultadoType.EMPATE
    }

    def 'PautaCriacaoDto null deve ser mapeada para Pauta null'() {
        given: 'um PautaCriacaoDto null'

        when: 'Pauta é criada'
        Pauta pauta = pautaMapper.pautaCriacaoDtoParaPauta(null)

        then: 'Pauta deve ser null'
        pauta == null
    }

    def 'Pauta null deve ser mapeada para PautaDto null'() {
        given: 'um Pauta null'

        when: 'PautaDto é criada'
        PautaDto pautaDto = pautaMapper.pautaParaPautaDto(null)

        then: 'PautaDto deve ser null'
        pautaDto == null
    }

    def 'Set de Voto null deve ser mapeado para Set de VotoDto null'() {
        given: 'um Set de Voto null'
        Pauta pauta = new Pauta()
        pauta.votos = null

        when: 'PautaDto é criada'
        PautaDto pautaDto = pautaMapper.pautaParaPautaDto(pauta)

        then: 'Set de VotoDto deve ser null'
        pautaDto.votos == null
    }

    def 'Voto null deve ser mapeado para VotoDto null'() {
        given: 'um Voto null'
        Pauta pauta = new Pauta()
        pauta.votos << null

        when: 'PautaDto é criada'
        PautaDto pautaDto = pautaMapper.pautaParaPautaDto(pauta)

        then: 'VotoDto deve ser null'
        pautaDto.votos[0] == null
    }

    def 'Pauta null deve ser mapeada para PautaResultadoDto null'() {
        given: 'um Pauta null'

        when: 'PautaResultadoDto é criada'
        PautaResultadoDto pautaResultadoDto = pautaMapper.pautaParaPautaResultadoDto(null)

        then: 'PautaResultadoDto deve ser null'
        pautaResultadoDto == null
    }
}
