package com.limac.pautaservice.rest

import com.limac.pautaservice.repository.PautaRepository
import com.limac.pautaservice.rest.dto.*
import com.limac.pautaservice.type.ResultadoType
import com.limac.pautaservice.type.VotoType
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import kafka.server.KafkaConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.kafka.test.context.EmbeddedKafka
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.Duration

import static io.restassured.RestAssured.given

@Stepwise
@EmbeddedKafka(topics = ['${kafka.topic}'], partitions = 1, brokerProperties = ['listeners=PLAINTEXT://localhost:9092', 'port=9092'])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PautaControllerIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    @Autowired
    KafkaConsumer<String, Object> kafkaConsumer

    @Shared
    RequestSpecification requestSpecification

    @Shared
    String pautaId

    String cpf = '08301655097'

    def setup() {
        requestSpecification = given()
            .baseUri('http://localhost')
            .port(port)
            .basePath('/api/pauta-service/v1/')
            .contentType(ContentType.JSON)
    }

    @Autowired
    PautaRepository pautaRepository

    def "Pauta deve ser criada ao chamar o endpoint 'pauta.criar'"() {
        given: 'um PautaCriacaoDto payload'
        PautaCriacaoDto pautaCriacaoDto = new PautaCriacaoDto()
        pautaCriacaoDto.descricao = 'descrição'
        pautaCriacaoDto.tempoDuracao = 1

        when: 'POST para criar Pauta é chamado'
        Response response = requestSpecification
            .body(pautaCriacaoDto)
            .post('pauta.criar')

        response.then().log().all()

        pautaId = response.path('pautaId')

        then: 'deve retornar o status CREATED'
        response.statusCode == 201

        and: 'PautaDto deve ser retornado'
        response.body().as(PautaDto) != null
    }

    def "Pauta deve ser criada com tempo de duracao 1 quando nao informado"() {
        given: 'um PautaCriacaoDto payload'
        PautaCriacaoDto pautaCriacaoDto = new PautaCriacaoDto()

        when: 'POST para criar Pauta é chamado'
        Response response = requestSpecification
            .body(pautaCriacaoDto)
            .post('pauta.criar')

        response.then().log().all()

        then: 'deve retornar o status CREATED'
        response.statusCode == 201

        and: 'tempoDuracao deve ser 1'
        response.path('tempoDuracao') == 1
    }

    def "Voto nao deve ser adicionado a Pauta fechada ao chamar o endpoint 'pauta.voto.adicionar'"() {
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
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Pauta '${pautaId}' está fechada"
    }

    def "Pauta deve ser aberta ao chamar o endpoint 'pauta.abrir'"() {
        given: 'um pautaId'

        when: 'POST para abrir uma Pauta é chamado'
        Response response = requestSpecification
            .post("pauta.abrir/${pautaId}")

        response.then().log().all()

        then: 'deve retornar o status OK'
        response.statusCode == 200

        and: 'Pauta deve ser aberta'
        pautaRepository.findById(pautaId).get().aberta
    }

    def "Pauta inexistente nao deve ser aberta ao chamar o endpoint 'pauta.abrir'"() {
        given: 'uma Pauta inexistente'
        String pautaIdInexistente = UUID.randomUUID()

        when: 'POST para abrir uma Pauta é chamado'
        Response response = requestSpecification
            .post("pauta.abrir/${pautaIdInexistente}")

        response.then().log().all()

        then: 'deve retornar o status NOT FOUND'
        response.statusCode == 404

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Pauta '${pautaIdInexistente}' não foi encontrada"
    }

    def "Pauta nao deve ser aberta quando ja esta aberta ao chamar o endpoint 'pauta.abrir'"() {
        given: 'um pautaId'

        when: 'POST para abrir uma Pauta é chamado'
        Response response = requestSpecification
            .post("pauta.abrir/${pautaId}")

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Pauta '${pautaId}' já está aberta"
    }

    def "Voto deve ser adicionado a Pauta ao chamar o endpoint 'pauta.voto.adicionar'"() {
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
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status OK'
        response.statusCode == 200
    }

    def "Voto nao deve ser adicionado a uma Pauta quando o cpf ja votou ao chamar o endpoint 'pauta.voto.adicionar'"() {
        given: 'uma Pauta inexistente'
        VotoAddDto votoAddDto = new VotoAddDto()
        votoAddDto.pautaId = pautaId
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoDto.votoType = VotoType.NAO
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status BAD REQUEST'
        response.statusCode == 400

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Voto do Associado '${cpf}' já existe"
    }

    def "Voto nao deve ser adicionado a uma Pauta inexistente ao chamar o endpoint 'pauta.voto.adicionar'"() {
        given: 'uma Pauta inexistente'
        VotoAddDto votoAddDto = new VotoAddDto()
        String pautaIdInexistente = UUID.randomUUID()
        votoAddDto.pautaId = pautaIdInexistente
        VotoDto votoDto = new VotoDto()
        votoDto.cpf = cpf
        votoDto.votoType = VotoType.SIM
        votoAddDto.votoDto = votoDto

        when: 'POST para adicionar Voto é chamado'
        Response response = requestSpecification
            .body(votoAddDto)
            .post('pauta.voto.adicionar')

        response.then().log().all()

        then: 'deve retornar o status NOT FOUND'
        response.statusCode == 404

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Pauta '${pautaIdInexistente}' não foi encontrada"
    }

    def "Resultado parcial da Pauta deve retornar enquanto a Pauta estiver aberta ao chamar o endpoint 'pauta.resultado'"() {
        given: 'uma Pauta fechada'

        when: 'GET para obter resultado da Pauta é chamado'
        Response response = requestSpecification
            .get("pauta.resultado/${pautaId}")

        response.then().log().all()

        PautaResultadoDto pautaResultadoDto = response.body().as(PautaResultadoDto)

        then: 'deve retornar o status OK'
        response.statusCode == 200

        and: 'deve retornar o resultado'
        pautaResultadoDto != null
        pautaResultadoDto.resultado == ResultadoType.SIM
        pautaResultadoDto.aberta
    }

    def 'Pauta deve fechar apos o tempo de duracao acabar'() {
        given: 'uma Pauta aberta'

        when: 'tempo de duração acabar'
        ConsumerRecords<String, Object> consumerRecords = kafkaConsumer.poll(Duration.ofMinutes(1))

        then: 'Pauta deve estar fechada'
        !pautaRepository.findById(pautaId).get().aberta

        and: 'PautaResultadoDto deve ser publicada'
        consumerRecords.forEach({ consumerRecord ->
            consumerRecord.value() instanceof PautaResultadoDto
            (consumerRecord.value() as PautaResultadoDto).pautaId == pautaId
            (consumerRecord.value() as PautaResultadoDto).resultado != null
        })
    }

    def "Resultado de uma Pauta fechada deve retornar ao chamar o endpoint 'pauta.resultado'"() {
        given: 'uma Pauta fechada'

        when: 'GET para obter resultado da Pauta é chamado'
        Response response = requestSpecification
            .get("pauta.resultado/${pautaId}")

        response.then().log().all()

        PautaResultadoDto pautaResultadoDto = response.body().as(PautaResultadoDto)

        then: 'deve retornar o status OK'
        response.statusCode == 200

        and: 'deve retornar o resultado'
        pautaResultadoDto != null
        pautaResultadoDto.resultado == ResultadoType.SIM
        !pautaResultadoDto.aberta
    }

    def "Resultado da Pauta inexistente nao deve retornar ao chamar o endpoint 'pauta.resultado'"() {
        given: 'uma Pauta inexistente'
        String pautaIdInexistente = UUID.randomUUID()

        when: 'GET para obter resultado da Pauta é chamado'
        Response response = requestSpecification
            .get("pauta.resultado/${pautaIdInexistente}")

        response.then().log().all()

        then: 'deve retornar o status NOT FOUND'
        response.statusCode == 404

        and: 'deve retornar a mensagem de erro'
        response.body.as(RespostaErroDto).mensagem == "Pauta '${pautaIdInexistente}' não foi encontrada"
    }
}
