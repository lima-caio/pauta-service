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
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * O {@link java.lang.annotation.ElementType#FIELD} ou {@link java.lang.annotation.ElementType#PARAMETER} anotado deve ser um UUID válido.
 */
@Target({FIELD, PARAMETER})
@NotNull
@Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
public @interface Uuid {

    /**
     * Define a mensagem caso a validação falhe.
     * Mensagem default é "UUID inválido".
     *
     * @return message
     */
    String message() default "UUID inválido";

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

