package com.limac.pautaservice.rest;

import com.limac.pautaservice.domain.Voto;
import com.limac.pautaservice.mapping.VotoMapper;
import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.mapping.PautaMapper;
import com.limac.pautaservice.rest.dto.RespostaErroDto;
import com.limac.pautaservice.rest.dto.PautaCriacaoDto;
import com.limac.pautaservice.rest.dto.PautaDto;
import com.limac.pautaservice.rest.dto.PautaResultadoDto;
import com.limac.pautaservice.rest.dto.VotoAddDto;
import com.limac.pautaservice.rest.dto.VotoDto;
import com.limac.pautaservice.service.PautaService;
import com.limac.pautaservice.validation.annotation.Uuid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller para {@link Pauta}.
 */
@Api(tags = "Pauta Controller")
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
public class PautaController {

    private static final String PAUTA_ID = "pautaId";

    private static final String OK_STATUS = "Ok";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String CREATED = "Created";
    private static final String UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    private final VotoMapper votoMapper;
    private final PautaMapper pautaMapper;
    private final PautaService pautaService;

    /**
     * Cria uma nova {@link Pauta}.
     *
     * @param pautaCriacaoDto payload de criação de {@link Pauta}.
     * @return {@link PautaDto}.
     */
    @PostMapping(path = "pauta.criar", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Cria uma nova Pauta.")
    @ApiResponses({
        @ApiResponse(code = 201, message = CREATED, response = PautaDto.class),
        @ApiResponse(code = 400, message = BAD_REQUEST, response = RespostaErroDto.class),
        @ApiResponse(code = 415, message = UNSUPPORTED_MEDIA_TYPE, response = RespostaErroDto.class),
        @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = RespostaErroDto.class)
    })
    public PautaDto criarPauta(@ApiParam(name = "pauta", required = true) @Valid @RequestBody PautaCriacaoDto pautaCriacaoDto) {
        log.info("Request recebido para criar uma nova Pauta: {}", kv("pautaCriacaoDto", pautaCriacaoDto));

        final Pauta pauta = pautaService.criarPauta(pautaMapper.pautaCriacaoDtoParaPauta(pautaCriacaoDto));

        log.info("Pauta {} foi criada", kv(PAUTA_ID, pauta.getPautaId()));

        return pautaMapper.pautaParaPautaDto(pauta);
    }

    /**
     * Abre uma {@link Pauta}.
     *
     * @param pautaId id da {@link Pauta} que deve ser aberta.
     */
    @PostMapping(path = "pauta.abrir/{pautaId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Abre uma Pauta.")
    @ApiResponses({
        @ApiResponse(code = 200, message = OK_STATUS, response = PautaDto.class),
        @ApiResponse(code = 400, message = BAD_REQUEST, response = RespostaErroDto.class),
        @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = RespostaErroDto.class)
    })
    public void abrirPauta(@ApiParam(name = PAUTA_ID, required = true) @Uuid @PathVariable String pautaId) {
        log.info("Request recebido para abrir a Pauta: {}", kv(PAUTA_ID, pautaId));

        pautaService.abrirPauta(pautaId);

        log.info("Pauta {} foi aberta", kv(PAUTA_ID, pautaId));

    }

    /**
     * Vota em uma {@link Pauta}.
     *
     * @param votoAddDto payload de adição de {@link Voto}.
     */
    @PostMapping(path = "pauta.voto.adicionar", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Adiciona um voto em uma Pauta.")
    @ApiResponses({
        @ApiResponse(code = 200, message = OK_STATUS, response = PautaDto.class),
        @ApiResponse(code = 400, message = BAD_REQUEST, response = RespostaErroDto.class),
        @ApiResponse(code = 415, message = UNSUPPORTED_MEDIA_TYPE, response = RespostaErroDto.class),
        @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = RespostaErroDto.class)
    })
    public void addVoto(@ApiParam(name = "voto", required = true) @Valid @RequestBody VotoAddDto votoAddDto) {
        log.info("Request recebido para adicionar um voto à Pauta: {}", kv("votoAddDto", votoAddDto));

        final VotoDto votoDto = votoAddDto.getVotoDto();
        pautaService.adicionarVoto(votoAddDto.getPautaId(), votoMapper.votoDtoParaVoto(votoDto));

        log.info("Voto do {} adicionado à Pauta {}", kv("cpf", votoDto.getCpf()), kv(PAUTA_ID, votoAddDto.getPautaId()));
    }

    /**
     * Retorna o resultado de uma {@link Pauta}.
     *
     * @param pautaId id da {@link Pauta}.
     * @return {@link PautaResultadoDto}.
     */
    @GetMapping(path = "pauta.resultado/{pautaId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna o resultado da votação de uma Pauta.")
    @ApiResponses({
        @ApiResponse(code = 200, message = OK_STATUS, response = PautaResultadoDto.class),
        @ApiResponse(code = 400, message = BAD_REQUEST, response = RespostaErroDto.class),
        @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = RespostaErroDto.class)
    })
    public PautaResultadoDto buscarResultado(@ApiParam(name = PAUTA_ID, required = true) @Uuid @PathVariable String pautaId) {
        log.info("Request recebido para obter o resultado da Pauta: {}", kv(PAUTA_ID, pautaId));

        final PautaResultadoDto pautaResultadoDto = pautaMapper.pautaParaPautaResultadoDto(pautaService.buscarResultado(pautaId));

        log.info("Resultado da Pauta: {}, {}", kv(PAUTA_ID, pautaId), kv("resultado", pautaResultadoDto.getResultado()));

        return pautaResultadoDto;
    }
}
