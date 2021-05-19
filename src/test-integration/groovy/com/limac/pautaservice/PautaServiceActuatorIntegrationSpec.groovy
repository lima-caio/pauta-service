package com.limac.pautaservice

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PautaServiceActuatorIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    def 'Actuator healthcheck deve estar disponivel'() {
        given: 'o healthcheck endpoint'
        RequestSpecification requestSpecification = RestAssured.given()
            .baseUri('http://localhost').port(port).basePath('/api/pauta-service/actuator/health')

        when: 'healthcheck é chamado'
        Response response = requestSpecification.contentType(ContentType.JSON).get()
        response.then().log().all()

        then: 'deve retornar o código 200'
        response.statusCode == 200

        and: 'status deve ser UP'
        response.path('status') == 'UP'
    }
}
