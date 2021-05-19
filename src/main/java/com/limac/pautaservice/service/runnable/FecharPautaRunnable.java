package com.limac.pautaservice.service.runnable;

import com.limac.pautaservice.service.PautaService;
import lombok.RequiredArgsConstructor;

/**
 * Implementação de {@link Runnable} para executar {@link PautaService#fecharPauta(String)}.
 */
@RequiredArgsConstructor
public class FecharPautaRunnable implements Runnable {

    private final String pautaId;
    private final PautaService pautaService;

    /**
     * Executa o {@link PautaService#fecharPauta(String)}.
     */
    @Override
    public void run() {
        pautaService.fecharPauta(pautaId);
    }
}
