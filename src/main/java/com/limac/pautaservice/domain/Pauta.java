package com.limac.pautaservice.domain;

import com.limac.pautaservice.type.ResultadoType;
import com.limac.pautaservice.validation.annotation.Uuid;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Objeto de domínio da Pauta.
 */
@Data
@Document(collection = "pautas")
@TypeAlias("PAUTA")
public class Pauta {

    @Id
    @Uuid
    private String pautaId = UUID.randomUUID().toString();

    private String descricao;

    private int tempoDuracao = 1;

    private TimeUnit tempoUnidade = TimeUnit.MINUTES;

    private boolean aberta;

    private Set<Voto> votos = new HashSet<>();

    private int sim;

    private int nao;

    private ResultadoType resultado;
}
