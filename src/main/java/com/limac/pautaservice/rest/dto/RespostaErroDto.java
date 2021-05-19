package com.limac.pautaservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Objeto de transferência de dados de resposta de erro.
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class RespostaErroDto {

    @ApiModelProperty(value = "mensagem de erro", position = 1)
    private String mensagem;
}
