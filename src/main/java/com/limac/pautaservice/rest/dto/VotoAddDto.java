package com.limac.pautaservice.rest.dto;

import com.limac.pautaservice.domain.Voto;
import com.limac.pautaservice.validation.annotation.Uuid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Objeto de transferência de dados para adicionar {@link Voto}.
 */
@Data
public class VotoAddDto {

    @Uuid
    @ApiModelProperty(value = "pautaId", required = true, position = 1)
    private String pautaId;

    @Valid
    @NotNull(message = "não pode ser null")
    @ApiModelProperty(value = "voto", required = true, position = 2)
    private VotoDto votoDto;
}
