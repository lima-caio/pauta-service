package com.limac.pautaservice.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configuração de segurança desabilitada.
 */
@Configuration
public class PautaSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Desabilita a segurança da aplicação.
     *
     * @param webSecurity webSecurity
     */
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/**");
    }
}
