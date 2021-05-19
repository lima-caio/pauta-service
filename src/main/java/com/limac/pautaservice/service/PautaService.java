package com.limac.pautaservice.service;

import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.domain.Voto;

/**
 * Serviço para executar operações de {@link Pauta}.
 */
public interface PautaService {

    /**
     * Cria uma nova {@link Pauta}.
     *
     * @param pauta {@link Pauta} para ser criada.
     * @return {@link Pauta} criada.
     */
    Pauta criarPauta(Pauta pauta);

    /**
     * Procura uma {@link Pauta} pelo seu identificar.
     *
     * @param pautaId identificador da pauta.
     * @return {@link Pauta} encontrada.
     */
    Pauta procurarPauta(String pautaId);

    /**
     * Abre uma {@link Pauta}.
     *
     * @param pautaId identificador da pauta.
     */
    void abrirPauta(String pautaId);

    /**
     * Fecha uma {@link Pauta}.
     *
     * @param pautaId identificador da pauta.
     */
    void fecharPauta(String pautaId);

    /**
     * Adiciona um {@link Voto} em uma {@link Pauta}.
     *
     * @param pautaId identificador da pauta.
     * @param voto {@link Voto} para ser adicionado.
     */
    void adicionarVoto(String pautaId, Voto voto);

    /**
     * Retorna o resultado de uma {@link Pauta}.
     *
     * @param pautaId identificador da pauta.
     * @return {@link Pauta} com seu resultado.
     */
    Pauta buscarResultado(String pautaId);
}
