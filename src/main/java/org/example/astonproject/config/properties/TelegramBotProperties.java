package org.example.astonproject.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "integration.telegram.bot")
public class TelegramBotProperties {
    private String token;
    private String name;
}
