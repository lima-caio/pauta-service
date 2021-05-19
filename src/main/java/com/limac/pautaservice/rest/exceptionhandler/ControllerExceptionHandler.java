package com.limac.pautaservice.rest.exceptionhandler;

import com.limac.pautaservice.exception.CpfInvalidoException;
import com.limac.pautaservice.exception.NaoEncontradoException;
import com.limac.pautaservice.exception.PautaAbertaException;
import com.limac.pautaservice.exception.PautaFechadaException;
import com.limac.pautaservice.exception.VotoExistenteException;
import com.limac.pautaservice.rest.dto.RespostaErroDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.StringJoiner;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.*;

/**
 * Controller Exception Handler to map exceptions to {@link RespostaErroDto}.
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Lida com {@link MethodArgumentNotValidException} para enviar como {@link RespostaErroDto} com status 400.
     *
     * @param exception exceção.
     * @return {@link RespostaErroDto} contendo a {@link RespostaErroDto}.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErroDto> handleBadRequest(MethodArgumentNotValidException exception) {
        final StringJoiner stringJoiner = new StringJoiner(", ");

        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
            stringJoiner.add(fieldError.getField() + " [" + fieldError.getDefaultMessage() + "]"));

        final String mensagem = stringJoiner.toString();

        log.error("{} foi capturada com {}", kv("exception", exception.getClass().getSimpleName()), kv("mensagem", mensagem));

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(RespostaErroDto.builder().mensagem(mensagem).build());
    }

    /**
     * Lida com {@link VotoExistenteException}, {@link PautaFechadaException}, {@link PautaAbertaException} e {@link CpfInvalidoException}
     * para enviar como {@link RespostaErroDto} com status 400.
     *
     * @param exception exceção.
     * @return {@link RespostaErroDto} contendo a {@link RespostaErroDto}.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({VotoExistenteException.class, PautaFechadaException.class, PautaAbertaException.class, CpfInvalidoException.class})
    public ResponseEntity<RespostaErroDto> handleBadRequest(Exception exception) {
        return buildResponseEntity(exception, BAD_REQUEST);
    }

    /**
     * Lida com {@link NaoEncontradoException} para enviar como {@link RespostaErroDto} com status 404.
     *
     * @param exception exceção.
     * @return {@link RespostaErroDto} contendo a {@link RespostaErroDto}.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<RespostaErroDto> handleNotFound(Exception exception) {
        return buildResponseEntity(exception, NOT_FOUND);
    }

    /**
     * Lida com {@link NaoEncontradoException} para enviar como {@link RespostaErroDto} com status 415.
     *
     * @param exception exceção.
     * @return {@link RespostaErroDto} contendo a {@link RespostaErroDto}.
     */
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RespostaErroDto> handleUnsupportedMediaType(Exception exception) {
        return buildResponseEntity(exception, UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Lida com {@link Exception} inesperadas para enviar como {@link RespostaErroDto} com status 500.
     *
     * @param exception exceção.
     * @return {@link RespostaErroDto} contendo a {@link RespostaErroDto}.
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErroDto> handleInternalServerError(Exception exception) {
        return buildResponseEntity(exception, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<RespostaErroDto> buildResponseEntity(Exception exception, HttpStatus httpStatus) {
        final String errorMessage = exception.getMessage();

        log.error("{} foi capturada com {}", kv("exception", exception.getClass().getSimpleName()), kv("errorMessage", errorMessage));

        return ResponseEntity
            .status(httpStatus)
            .body(RespostaErroDto.builder().mensagem(errorMessage).build());
    }
}
