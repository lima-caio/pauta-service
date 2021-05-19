package com.limac.pautaservice.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * O {@link java.lang.annotation.ElementType#FIELD} anotado deve ser um UUID válido.
 */
@Target(FIELD)
@NotNull
@Pattern(regexp = "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})")
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
public @interface Cpf {

    /**
     * Define a mensagem caso a validação falhe.
     * Mensagem default é "CPF inválido".
     *
     * @return message.
     */
    String message() default "CPF inválido";

    /**
     * Define os groups para validação.
     *
     * @return groups
     */
    Class[] groups() default {};

    /**
     * Define o payload para validação.
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};
}

