package org.main_java.caso_practico_tema_2_programacion_concurrente.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * Provide attributes available in all templates.
 */
@ControllerAdvice
public class WebAdvice {

    @ModelAttribute("isDevserver")
    public Boolean getIsDevserver(final HttpServletRequest request) {
        return "1".equals(request.getHeader("X-Devserver"));
    }

}
