package com.limac.pautaservice.mapping

import com.limac.pautaservice.domain.Voto
import com.limac.pautaservice.rest.dto.VotoDto
import com.limac.pautaservice.type.VotoType
import spock.lang.Specification
import spock.lang.Unroll

class VotoMapperSpec extends Specification {

    VotoMapper votoMapper = new VotoMapperImpl()

    @Unroll
    def 'VotoDto deve ser mapeada para Voto'() {
        given: 'um VotoDto'
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoDto.votoType = votoType

        when: 'Voto é criada'
        Voto voto = votoMapper.votoDtoParaVoto(votoDto)

        then: 'Voto deve ser mapeada'
        voto.cpf == cpf
        voto.votoType == votoType

        where:
        cpf     | votoType
        'cpf'   | VotoType.SIM
        'cpf'   | VotoType.NAO
    }

    def 'VotoDto null deve ser mapeada para Voto null'() {
        given: 'um VotoDto null'

        when: 'Voto é criada'
        Voto voto = votoMapper.votoDtoParaVoto(null)

        then: 'Voto deve ser null'
        voto == null
    }
}
