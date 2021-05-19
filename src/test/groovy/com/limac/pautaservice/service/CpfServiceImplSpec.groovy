package com.limac.pautaservice.service

import com.limac.pautaservice.rest.client.CpfClient
import com.limac.pautaservice.rest.client.dto.CpfResultadoDto
import com.limac.pautaservice.type.CpfStatusType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class CpfServiceImplSpec extends Specification {

    CpfClient cpfClient = Mock(CpfClient)

    CpfService cpfService = new CpfServiceImpl(cpfClient, true)

    def 'CPF que pode votar deve retornar true ao ser validado'() {
        given: 'um CPF'
        String cpf = 'cpf'
        CpfResultadoDto cpfResultadoDto = new CpfResultadoDto()
        cpfResultadoDto.status = CpfStatusType.ABLE_TO_VOTE
        ResponseEntity<CpfResultadoDto> responseEntity = new ResponseEntity<>(cpfResultadoDto, HttpStatus.OK)

        1 * cpfClient.getForObject(cpf, _) >> responseEntity

        when: 'CPF é validado'
        boolean isValido = cpfService.isValido(cpf)

        then: 'retorno é true'
        isValido
    }

    def 'CPF que nao pode votar deve retornar false ao ser validado'() {
        given: 'um CPF'
        String cpf = 'cpf'
        CpfResultadoDto cpfResultadoDto = new CpfResultadoDto()
        cpfResultadoDto.status = CpfStatusType.UNABLE_TO_VOTE
        ResponseEntity<CpfResultadoDto> responseEntity = new ResponseEntity<>(cpfResultadoDto, HttpStatus.OK)

        1 * cpfClient.getForObject(cpf, _) >> responseEntity

        when: 'CPF é validado'
        boolean isValido = cpfService.isValido(cpf)

        then: 'retorno é false'
        !isValido
    }

    def 'CPF invalido deve retornar false ao ser validado'() {
        given: 'um CPF'
        String cpf = 'cpfInvalido'
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND)

        1 * cpfClient.getForObject(cpf, _) >> responseEntity

        when: 'CPF é validado'
        boolean isValido = cpfService.isValido(cpf)

        then: 'retorno é false'
        !isValido
    }

    def 'quando o servico esta desabilitado o cpf deve ser sempre considerado valido'() {
        given: 'um CPF'
        String cpf = 'cpf'

        0 * cpfClient.getForObject(cpf, _)

        when: 'CPF é validado'
        boolean isValido = new CpfServiceImpl(cpfClient, false).isValido(cpf)

        then: 'retorno é true'
        isValido
    }
}
