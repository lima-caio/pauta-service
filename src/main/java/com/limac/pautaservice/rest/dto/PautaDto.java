package com.limac.pautaservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.validation.annotation.Uuid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Objeto de transferência de dados de {@link Pauta}.
 */
@Data
@JsonInclude(NON_NULL)
public class PautaDto {

    @Uuid
    @ApiModelProperty(value = "pautaId", position = 1)
    private String pautaId;

    @ApiModelProperty(value = "descrição", position = 2)
    private String descricao;

    @ApiModelProperty(value = "tempo de duração", position = 3)
    private int tempoDuracao;

    @ApiModelProperty(value = "votos", position = 4)
    private Set<VotoDto> votos;
}
