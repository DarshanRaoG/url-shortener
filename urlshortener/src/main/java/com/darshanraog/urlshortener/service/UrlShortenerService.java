package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.config.UrlShortenerProperties;
import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import com.darshanraog.urlshortener.util.Base62Encoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UrlShortenerService implements IUrlShortenerService {

    private final UrlShortenerProperties urlShortenerProperties;
    private final UrlMappingRepository urlMappingRepository;
    private static final long PRIME = 2654435761L;

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

        UrlMapping existingUrlMapping = urlMappingRepository.findByShortCode(shortCode);
        if (Objects.isNull(existingUrlMapping)) {
            throw new RuntimeException("Url doesn't exist!");
        }
        existingUrlMapping.setClickCount(existingUrlMapping.getClickCount() + 1);
        urlMappingRepository.save(existingUrlMapping);
        return existingUrlMapping;
    }

    @Override
    public void incrementClickCount() {

    }

    @Override
    public LocalDateTime checkExpiryDate() {
        return null;
    }

}
