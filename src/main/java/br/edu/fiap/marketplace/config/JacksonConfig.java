package br.edu.fiap.marketplace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Disponibiliza o {@link ObjectMapper} para componentes que precisam
 * serializar respostas JSON manualmente.
 */
@Configuration
public class JacksonConfig {

    /**
     * Cria o serializador JSON usado nas respostas de erro da aplicação.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}
