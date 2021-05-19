package com.limac.pautaservice.rest.client

import com.limac.pautaservice.exception.CpfInvalidoException
import com.limac.pautaservice.rest.client.dto.CpfResultadoDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class CpfClientImplSpec extends Specification {

    RestTemplate restTemplate = Mock(RestTemplate)

    CpfClient cpfClient = new CpfClientImpl(restTemplate, 'url')

    def 'client deve ser executado para validar um cpf sem erros'() {
        given: 'um CPF'
        String cpf = 'cpf'

        1 * restTemplate.exchange(*_) >> Mock(ResponseEntity)

        when: 'client é chamado'
        ResponseEntity responseEntity = cpfClient.getForObject(cpf, new ParameterizedTypeReference<CpfResultadoDto>() { })

        then: 'responseEntity deve ser retornada'
        responseEntity != null

        and: 'nenhum erro deve ocorrer'
        noExceptionThrown()
    }

    def 'CpfInvalidoException deve ser lancada caso algum erro aconteca durante a chamada do client'() {
        given: 'um CPF'
        String cpf = 'cpf'

        1 * restTemplate.exchange(*_) >> { throw new RestClientException('erro') }

        when: 'client é chamado'
        cpfClient.getForObject(cpf, new ParameterizedTypeReference<CpfResultadoDto>() { })

        then: 'CpfInvalidoException é lançada'
        thrown(CpfInvalidoException)
    }
}
