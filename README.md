# Pauta Service

Pauta Service disponibiliza APIs para criação e manutenção de Pautas para serem votadas pelos Associados.

## APIs

### Criar Pauta

POST para criar uma nova Pauta.

     {host}/v1/pauta.criar
     
Payload:

```json
{
  "descricao": "string",
  "tempoDuracao": 0
}
``` 

Response: 

201 CREATED

```json
{
  "descricao": "string",
  "pautaId": "string",
  "tempoDuracao": 1,
  "votos": [
    
  ]
}
```
     
### Abrir Pauta

POST para abrir uma Pauta para votação.

     {host}/v1/pauta.abrir/{pautaId}
     
Response: 

200 OK

### Adicionar Voto

POST para adicionar Voto em uma Pauta.

     {host}/v1/pauta.voto.adicionar

Payload:

```json
{
  "pautaId": "string",
  "votoDto": {
    "cpf": "string",
    "votoType": "SIM / NAO"
  }
}
``` 

Response:

200 OK

### Buscar Resultado

GET para buscar o resultado de uma Pauta.

     {host}/v1/pauta.resultado/{pautaID}

Response:

200 OK

```json
{
  "descricao": "string",
  "nao": 0,
  "pautaId": "string",
  "resultado": "SIM`/ NAO / EMPATE",
  "sim": 0
}
```

## Architecture

### Language

* Implementation:
    * Java 8
    
* Tests
    * Groovy 2.5.6
    
* Integration Tests
    * Groovy 2.5.6
    
* Build
    * Gradle 4.10.3
    
### Frameworks

* Implementation:
    * [SpringBoot](https://spring.io/projects/spring-boot)
    * [Lombok](https://projectlombok.org/)
    * [Mapstruct](https://mapstruct.org/)
    * [Swagger](https://swagger.io/)
    * [Logstash Logback Encoder](https://github.com/logstash/logstash-logback-encoder)
    
* Tests:
    * [Spock](https://spockframework.org/)
     
* Integration Tests:
    * [Spock](https://spockframework.org/)
    * [REST Assured](https://rest-assured.io/)
    * [Flapdoodle](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
    
* Build
    * [Checkstyle](http://checkstyle.sourceforge.net/)
    * [Codenarc](http://codenarc.sourceforge.net/)
    * [PMD](https://pmd.github.io/)
    * [Error Prone](http://errorprone.info/)
    * [JaCoCo](https://www.eclemma.org/jacoco/)

### Code structure
```
/src
├── main
│   ├── java
│   │   └── com
│   │       └── limac
│   │           └── pautaservice
│   │               ├── PautaServiceApplication.java
│   │               ├── autoconfigure
│   │               │   ├── KafkaConfiguration.java
│   │               │   ├── PautaSecurityConfiguration.java
│   │               │   ├── RestTemplateConfiguration.java
│   │               │   └── SwaggerAutoConfiguration.java
│   │               ├── domain
│   │               │   ├── Pauta.java
│   │               │   └── Voto.java
│   │               ├── exception
│   │               │   ├── CpfInvalidoException.java
│   │               │   ├── NaoEncontradoException.java
│   │               │   ├── PautaAbertaException.java
│   │               │   ├── PautaFechadaException.java
│   │               │   └── VotoExistenteException.java
│   │               ├── mapping
│   │               │   ├── PautaMapper.java
│   │               │   └── VotoMapper.java
│   │               ├── messaging
│   │               │   ├── Message.java
│   │               │   ├── callback
│   │               │   │   └── PublicarResultadoCallback.java
│   │               │   └── publisher
│   │               │       ├── PautaPublisher.java
│   │               │       └── PautaPublisherImpl.java
│   │               ├── repository
│   │               │   └── PautaRepository.java
│   │               ├── rest
│   │               │   ├── PautaController.java
│   │               │   ├── client
│   │               │   │   ├── CpfClient.java
│   │               │   │   ├── CpfClientImpl.java
│   │               │   │   └── dto
│   │               │   │       └── CpfResultadoDto.java
│   │               │   ├── dto
│   │               │   │   ├── PautaCriacaoDto.java
│   │               │   │   ├── PautaDto.java
│   │               │   │   ├── PautaResultadoDto.java
│   │               │   │   ├── RespostaErroDto.java
│   │               │   │   ├── VotoAddDto.java
│   │               │   │   └── VotoDto.java
│   │               │   └── exceptionhandler
│   │               │       └── ControllerExceptionHandler.java
│   │               ├── service
│   │               │   ├── AgendadorService.java
│   │               │   ├── AgendadorServiceImpl.java
│   │               │   ├── CpfService.java
│   │               │   ├── CpfServiceImpl.java
│   │               │   ├── PautaService.java
│   │               │   ├── PautaServiceImpl.java
│   │               │   └── runnable
│   │               │       └── FecharPautaRunnable.java
│   │               ├── type
│   │               │   ├── CpfStatusType.java
│   │               │   ├── ResultadoType.java
│   │               │   └── VotoType.java
│   │               └── validation
│   │                   └── annotation
│   │                       ├── Cpf.java
│   │                       └── Uuid.java
│   └── resources
│       ├── application-local.yml
│       ├── banner.txt
│       ├── bootstrap.yml
│       ├── logback-spring.xml
│       └── swagger.properties
├── test
│   ├── groovy
│   │   └── com
│   │       └── limac
│   │           └── pautaservice
│   │               ├── mapping
│   │               │   ├── PautaMapperImplSpec.groovy
│   │               │   └── VotoMapperSpec.groovy
│   │               ├── messaging
│   │               │   └── publisher
│   │               │       └── PautaPublisherImplSpec.groovy
│   │               ├── rest
│   │               │   ├── PautaControllerSpec.groovy
│   │               │   ├── client
│   │               │   │   └── CpfClientImplSpec.groovy
│   │               │   └── exceptionhandler
│   │               │       └── ControllerExceptionHandlerSpec.groovy
│   │               └── service
│   │                   ├── AgendadorServiceImplSpec.groovy
│   │                   ├── CpfServiceImplSpec.groovy
│   │                   ├── FecharPautaRunnableSpec.groovy
│   │                   └── PautaServiceImplSpec.groovy
│   └── resources
│       └── META-INF
│           └── services
│               └── com.athaydes.spockframework.report.IReportCreator.properties
└── test-integration
    ├── groovy
    │   └── com
    │       └── limac
    │           └── pautaservice
    │               ├── PautaServiceActuatorIntegrationSpec.groovy
    │               ├── helpers
    │               │   └── KafkaConsumerConfiguration.groovy
    │               └── rest
    │                   ├── PautaControllerIntegrationSpec.groovy
    │                   └── PautaControllerValidacaoIntegrationSpec.groovy
    └── resources
        ├── META-INF
        │   └── services
        │       └── com.athaydes.spockframework.report.IReportCreator.properties
        ├── application-integrationtestlocal.yml
        └── bootstrap.yml
```
## Gradle

### Gradle Clean

Comando para limpar todos os arquivos gerados.

```bash
./gradlew clean
```

### Gradle Build

Comando para verificar o código com CheckStyle, PMD, Codenarc e rodar os testes unitários com JaCoCo para cobertura de código.

```bash
./gradlew build
```

O ```pauta-service-1.0.0.jar``` pode ser encontrado em ```build/libs```.
   
Relatório do CheckStyle pode ser encontrado em ```build/reports/checkstyle```.

Relatório do Codenarc pode ser encontrado em ```build/reports/codenarc```.

Relatório do PMD pode ser encontrado em ```build/reports/pmd```.

Relatório do JaCoCo pode ser encontrado em ```build/reports/jacoco```.

### Gradle Run

Comando para rodar a aplicação. Por padrão, o serviço inicia em 'http://localhost:7080/api/pauta-service'. <br>
É necessário ter o Mongo rodando em seu ambiente, se não tiver, verifique a sessão "Docker". <br>
O Kafka é opcional, mas para isso é necessário ter o Kafka rodando em seu ambiente.

```bash
./gradlew bootRun
```

## Unit Test

### Rodar Unit Tests

Comando para rodar os testes unitários.

```bash
./gradlew test
```
   
Relatório dos testes pode ser encontrado em ```build/reports/test```.

## Integration Test

### Rodar Integration Test

Comando para rodar os testes de integração.

```bash
./gradlew integrationTest
```
   
```Flapdoodle``` fornece um MongoDB no escopo dos testes de integração, então não é necessário ter o Mongo rodando para rodar os testes de integração. <br><br>
```Spring Kafka Test``` fornece um Kafka no escopo dos testes de integração, então não é necessário ter o Kafka rodando para rodar os testes de integração.


Relatório dos testes de integração pode ser encontrado em ```build/reports/integrationTest```.

## Docker

### Pauta Service Image

Veja a sessão ```Gradle Build``` para gerar o ```pauta-service-1.0.0.jar```.

Comando para construir a imagem do pauta-service.

```bash
docker build -t pauta-service .
```
    
### Iniciando Pauta Service

Comando para iniciar o pauta-service.

```bash
docker-compose up
```
    
Docker Compose vai baixar uma imagem do MongoDB, então não é necessário ter o Mongo rodando em seu ambiente.
Porém, não possui imagem do Kafka, portanto as mensagens não serão publicadas.
    
## Command Line

### Criar Pauta

Comando para criar Pauta.

```bash
curl -X POST "http://localhost:7080/api/pauta-service/v1/pauta.criar" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"descricao\":\"string\",\"tempoDuracao\":0}"
```

### Abrir Pauta

Comando para abrir Pauta.

```bash
curl -X POST "http://localhost:7080/api/pauta-service/v1/pauta.abrir/{pautaId}"
``` 
    
### Adicionar Voto

Comando para adicionar Voto.

```bash
curl -X POST "http://localhost:7080/api/pauta-service/v1/pauta.voto.adicionar" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"pautaId\":\"{pautaId}\",\"votoDto\":{\"cpf\":\"{cpf}\",\"votoType\":\"{votoType}\"}}"
```

### Buscar Resultado

Comando para buscar o Resultado da Pauta.

```bash
curl -X GET "http://localhost:7080/api/pauta-service/v1/pauta.resultado/{pautaId}" -H "accept: application/json"
```

## Swagger

### Swagger UI

Swagger UI pode ser acessada em [http://localhost:7080/api/pauta-service/swagger-ui.html](http://localhost:7080/api/pauta-service/swagger-ui.html)

### Swagger JSON

Swagger JSON pode ser acessado em [http://localhost:7080/api/pauta-service/swagger](http://localhost:7080/api/pauta-service/swagger)

## Actuator

Actuator pode ser acessado em [http://localhost:7080/api/pauta-service/actuator](http://localhost:7080/api/pauta-service/actuator)

### Health

Healthcheck pode ser acessado em [http://localhost:7080/api/pauta-service/actuator/health](http://localhost:7080/api/pauta-service/actuator/health)
