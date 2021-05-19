package com.limac.pautaservice.rest

import com.limac.pautaservice.rest.dto.PautaCriacaoDto
import com.limac.pautaservice.rest.dto.RespostaErroDto
import com.limac.pautaservice.rest.dto.VotoAddDto
import com.limac.pautaservice.rest.dto.VotoDto
import com.limac.pautaservice.type.VotoType
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PautaControllerValidacaoIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    @Shared
    RequestSpecification requestSpecification

    String pautaId = UUID.randomUUID()
    String cpf = '08301655097'

    def setup() {
        requestSpecification = given()
            .baseUri('http://localhost')
            .port(port)
            .basePath('/api/pauta-service/v1/')
            .contentType(ContentType.JSON)
    }

    def "Pauta nao deve ser criada ao chamar o endpoint 'pauta.criar' com contentType invalido"() {
        given: 'um PautaCriacaoDto payload'
        PautaCriacaoDto pautaCriacaoDto = new PautaCriacaoDto()
        pautaCriacaoDto.descricao = 'descrição'
        pautaCriacaoDto.tempoDuracao = 1

        when: 'POST para criar Pauta é chamado'
        Response response = requestSpecification
            .body(pautaCriacaoDto)
            .contentType(ContentType.TEXT)
            .post('pauta.criar')

        response.then().log().all()

        then: 'deve retornar o status UNSUPPORTED CONTENT TYPE'
        response.statusCode == 415

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Content type 'text/plain;charset=ISO-8859-1' not supported"
    }

    def "Voto nao deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar' com contentType invalido"() {
        given: 'um VotoAddDto payload'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = pautaId
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoDto.votoType = VotoType.SIM
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .contentType(ContentType.TEXT)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status UNSUPPORTED CONTENT TYPE'
        response.statusCode == 415

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Content type 'text/plain;charset=ISO-8859-1' not supported"
    }

    def "Voto nao deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar' com pautaId invalido"() {
        given: 'um VotoAddDto payload'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = 'pautaIdInvalido'
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoDto.votoType = VotoType.SIM
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == 'pautaId [UUID inválido]'
    }

    def "Voto nao deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar' sem VotoDto"() {
        given: 'um VotoAddDto payload'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = pautaId

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == 'votoDto [não pode ser null]'
    }

    def "Voto nao deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar' com cpf invalido"() {
        given: 'um VotoAddDto payload'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = pautaId
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = 'cpfInvalido'
        votoDto.votoType = VotoType.SIM
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == 'votoDto.cpf [CPF inválido]'
    }

    def "Voto nao deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar' sem votoType"() {
        given: 'um VotoAddDto payload'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = pautaId
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == 'votoDto.votoType [não pode ser null]'
    }
}
