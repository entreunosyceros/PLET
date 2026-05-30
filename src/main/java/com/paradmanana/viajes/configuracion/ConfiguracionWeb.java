package com.paradmanana.viajes.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de recursos estáticos servidos por Spring MVC.
 * Los ficheros JS en {@code /static/js/} se sirven por defecto en {@code /js/**}.
 */
@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registro) {
        registro.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
    }
}
