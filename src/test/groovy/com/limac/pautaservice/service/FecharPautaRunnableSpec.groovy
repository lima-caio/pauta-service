package com.limac.pautaservice.service

import com.limac.pautaservice.service.runnable.FecharPautaRunnable
import spock.lang.Specification

class FecharPautaRunnableSpec extends Specification {

    PautaService pautaService = Mock(PautaService)
    String pautaId = 'pautaId'

    FecharPautaRunnable fecharPautaRunnable = new FecharPautaRunnable(pautaId, pautaService)

    def 'Pauta deve ser fechada sem erros quando o comando e executado'() {
        given: 'um pautaId'

        1 * pautaService.fecharPauta(pautaId)

        when: 'Comando de fechar Pauta é executado'
        fecharPautaRunnable.run()

        then: 'nenhum erro deve ser lançado'
        noExceptionThrown()
    }
}
