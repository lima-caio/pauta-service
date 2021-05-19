package com.limac.pautaservice.rest.dto;

import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.type.ResultadoType;
import com.limac.pautaservice.validation.annotation.Uuid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Objeto de transferência de dados de resultado da {@link Pauta}.
 */
@Data
public class PautaResultadoDto {

    @Uuid
    @ApiModelProperty(value = "pautaId", position = 1)
    private String pautaId;

    @ApiModelProperty(value = "descrição", position = 2)
    private String descricao;

    @ApiModelProperty(value = "se a pauta está aberta", position = 3, allowableValues = "true, false")
    private boolean aberta;

    @ApiModelProperty(value = "quantidade de votos 'SIM'", position = 4)
    private int sim;

    @ApiModelProperty(value = "quantidade de votos 'NAO'", position = 5)
    private int nao;

    @ApiModelProperty(value = "resultado da votação (pode ser parcial se a pauta estiver abera)", position = 6, allowableValues = "SIM, NAO, EMPATE")
    @NotNull
    private ResultadoType resultado;
}
