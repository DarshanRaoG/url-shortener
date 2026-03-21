package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.config.UrlShortenerProperties;
import com.darshanraog.urlshortener.dto.UrlMappingDTO;
import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import com.darshanraog.urlshortener.util.Base62Encoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UrlShortenerService implements IUrlShortenerService {

    private final UrlShortenerProperties urlShortenerProperties;
    private final UrlMappingRepository urlMappingRepository;
    private final StringRedisTemplate redisTemplate;
    private static final long PRIME = 2654435761L;
    private static final String CLICKS_KEY = "clicks:";

    /**
     * Creates a shortened URL for the given URL, saves it in DB and then returns it.
     *
     * @param originalUrl the URL received.
     * @return the shortened URL.
     */
    @Override
    @Transactional
    public String createShortUrl(String originalUrl) {
        if (Objects.isNull(originalUrl) || originalUrl.isBlank()) {
            throw new RuntimeException("Empty url object received!!!");
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpiresAt(LocalDateTime.now().plusDays(urlShortenerProperties.getDefaultExpiryDays()));

        urlMapping = urlMappingRepository.save(urlMapping);

        // Scramble the id using XOR for security (Multiply with prime for a more even scrambling)
        long scrambledId = (urlMapping.getUrlId() * PRIME) ^ urlShortenerProperties.getSecret();
        String shortCode = Base62Encoder.encode(scrambledId);

        // Updates in db automatically as it is transactional
        urlMapping.setShortCode(shortCode);

        return urlShortenerProperties.getBaseUrl() + "/" + shortCode;
    }

    /**
     * Fetch the original URL mapped to the given shortcode.
     *
     * @param shortCode the shortcode received.
     * @return the original URL.
     */
    @Override
    public UrlMapping getOriginalUrlMapping(String shortCode) {
        if (Objects.isNull(shortCode) || shortCode.isEmpty()) {
            throw new RuntimeException("Empty url object received!");
        }

        UrlMapping urlMapping;
        ObjectMapper objectMapper = new ObjectMapper();
        // Check if there is a cached URL in Redis
        String cachedJson = redisTemplate.opsForValue().get(shortCode);

        if (Objects.nonNull(cachedJson)) {
            System.out.println("Got it through redis! " + cachedJson);
            try {
                urlMapping = objectMapper.readValue(cachedJson, UrlMapping.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse cached data!", e);
            }
        } else {
            // Fall back to DB call
            urlMapping = urlMappingRepository.findByShortCode(shortCode);
            if (Objects.isNull(urlMapping)) {
                throw new RuntimeException("Url doesn't exist!");
            }

            try {
                String json = objectMapper.writeValueAsString(urlMapping);

                // Store shortCode in Redis for cache
                redisTemplate.opsForValue().set(
                        shortCode,
                        json,
                        Duration.ofHours(24)
                );
            } catch (Exception e) {
                System.out.println("WARNING: Failed to cache data " + e);
            }
        }

        if (urlMapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("URL expired!");
        }

        // Cache Aside -> Increment click count in redis (Will be synced later)
        try {
            redisTemplate.opsForValue().increment(CLICKS_KEY + shortCode);
        } catch (Exception e) {
            System.out.println("WARNING: Redis increment failed (" + e.getMessage() + ")");
            System.out.println("Continuing anyway...");
        }

        return urlMapping;
    }

    /**
     * Returns statistics for the shortened URL.
     *
     * @param shortCode given shortcode.
     * @return the DTO containing analytics.
     */
    @Override
    public UrlMappingDTO getStats(String shortCode) {
        if (Objects.isNull(shortCode) || shortCode.isEmpty()) {
            throw new RuntimeException("Empty url object received!");
        }
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode);
        if (urlMapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("URL expired!");
        }
        String redisCount = redisTemplate.opsForValue().get("clicks:" + shortCode);

        long latestCount = urlMapping.getClickCount();
        if (redisCount != null) {
            latestCount += Long.parseLong(redisCount);
        }
        return UrlMappingDTO.builder()
                .clickCount(latestCount)
                .createdAt(urlMapping.getCreatedAt())
                .expiresAt(urlMapping.getExpiresAt())
                .originalUrl(urlMapping.getOriginalUrl())
                .shortCode(urlMapping.getShortCode())
                .build();
    }

    @Override
    public LocalDateTime checkExpiryDate() {
        return null;
    }

}
