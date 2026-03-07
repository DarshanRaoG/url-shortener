package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import com.darshanraog.urlshortener.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UrlShortenerService implements IUrlShortenerService {

    @Value("${url.shortener.secret}")
    private long secretKey;

    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    /**
     * Creates a shortened URL for the given URL, saves it in DB and then returns it.
     *
     * @param originalUrl the URL received.
     * @return the shortened URL.
     */
    @Override
    public String createShortUrl(String originalUrl) {
        if (Objects.isNull(originalUrl) || originalUrl.isEmpty()) {
            throw new RuntimeException("Empty url object received!!!");
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping = urlMappingRepository.save(urlMapping);

        // Scramble the id using XOR for security
        long scrambledId = urlMapping.getUrlId() ^ secretKey;
        String shortCode = Base62Encoder.encode(scrambledId);
        urlMapping.setShortCode(shortCode);

        urlMappingRepository.save(urlMapping);

        return shortCode;
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
