package com.darshanraog.urlshortener.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "url.shortener")
public class UrlShortenerProperties {
    private Long secret;
    private String baseUrl;
    private Long defaultExpiryDays;
    private Long defaultRateLimit;
}
