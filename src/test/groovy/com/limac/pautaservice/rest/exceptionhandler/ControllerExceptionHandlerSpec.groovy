package com.limac.pautaservice.rest.exceptionhandler

import com.limac.pautaservice.exception.CpfInvalidoException
import com.limac.pautaservice.exception.NaoEncontradoException
import com.limac.pautaservice.exception.PautaAbertaException
import com.limac.pautaservice.exception.PautaFechadaException
import com.limac.pautaservice.rest.dto.RespostaErroDto
import org.springframework.core.MethodParameter
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

class ControllerExceptionHandlerSpec extends Specification {

    ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler()

    def 'MethodArgumentNotValidException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma MethodArgumentNotValidException'
        String mensagem = 'field1 [precisa ser válido], field2 [precisa ser válido]'

        BindingResult bindingResult = Mock(BindingResult)

        FieldError fieldError1 = Mock(FieldError)
        fieldError1.field >> 'field1'
        fieldError1.defaultMessage >> 'precisa ser válido'

        FieldError fieldError2 = Mock(FieldError)
        fieldError2.field >> 'field2'
        fieldError2.defaultMessage >> 'precisa ser válido'

        bindingResult.fieldErrors >> [fieldError1, fieldError2]

        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(Mock(MethodParameter), bindingResult)

        when: 'Bad Request é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleBadRequest(methodArgumentNotValidException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.mensagem == mensagem
    }

    def 'VotoExistenteException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma VotoExistenteException'
        String mensagem = 'esse voto já existe'
        NaoEncontradoException naoEncontradoException = new NaoEncontradoException(mensagem)

        when: 'Bad Request é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleBadRequest(naoEncontradoException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.mensagem == mensagem
    }

    def 'PautaFechadaException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma PautaFechadaException'
        String mensagem = 'essa pauta está fechada'
        PautaFechadaException pautaFechadaException = new PautaFechadaException(mensagem)

        when: 'Bad Request é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleBadRequest(pautaFechadaException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.mensagem == mensagem
    }

    def 'PautaAbertaException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma PautaAbertaException'
        String mensagem = 'essa pauta está aberta'
        PautaAbertaException pautaAbertaException = new PautaAbertaException(mensagem)

        when: 'Bad Request é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleBadRequest(pautaAbertaException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.mensagem == mensagem
    }

    def 'CpfInvalidoException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma CpfInvalidoException'
        String mensagem = 'cpf inválido'
        CpfInvalidoException cpfInvalidoException = new CpfInvalidoException(mensagem)

        when: 'Bad Request é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleBadRequest(cpfInvalidoException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.mensagem == mensagem
    }

    def 'NaoEncontradoException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma NaoEncontradoException'
        String mensagem = 'entidade não encontrada'
        NaoEncontradoException naoEncontradoException = new NaoEncontradoException(mensagem)

        when: 'Not Found é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleNotFound(naoEncontradoException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == NOT_FOUND
        responseEntity.body.mensagem == mensagem
    }

    def 'HttpMediaTypeNotSupportedException deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma HttpMediaTypeNotSupportedException'
        String mensagem = 'Content type \'application/xml\' não é suportado'
        HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = new HttpMediaTypeNotSupportedException(mensagem)

        when: 'Unsupported Media Type é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleUnsupportedMediaType(httpMediaTypeNotSupportedException)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == UNSUPPORTED_MEDIA_TYPE
        responseEntity.body.mensagem == mensagem
    }

    def 'Exception inesperada deve enviar um RespostaErroDto com a mensagem de erro'() {
        given: 'uma Exception'
        String mensagem = 'erro inesperado'
        Exception exception = new Exception(mensagem)

        when: 'Internal Server Error é capturado'
        ResponseEntity<RespostaErroDto> responseEntity = controllerExceptionHandler.handleInternalServerError(exception)

        then: 'ResponseEntity com os erros é retornada'
        responseEntity.statusCode == INTERNAL_SERVER_ERROR
        responseEntity.body.mensagem == mensagem
    }
}
