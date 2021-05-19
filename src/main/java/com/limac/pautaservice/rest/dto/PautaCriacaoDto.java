package com.limac.pautaservice.rest.dto;

import com.limac.pautaservice.domain.Pauta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Objeto de transferência de dados de Criação de {@link Pauta}.
 */
@Data
public class PautaCriacaoDto {

    @ApiModelProperty(value = "descrição", position = 1)
    private String descricao;

    @ApiModelProperty(value = "tempo de duração em minutos (quando não informado o valor assumido é 1)", position = 2)
    private Integer tempoDuracao;
}
