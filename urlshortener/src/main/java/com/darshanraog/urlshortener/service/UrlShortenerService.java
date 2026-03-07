package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UrlShortenerService implements IUrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    private static final char[] alphanumerics =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                    .toCharArray();

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

        String shortCode = "";
        do {
            shortCode = generateShortCode();
        } while (urlMappingRepository.findByShortCode(shortCode) != null);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setCreatedAt(LocalDateTime.now());
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

    /**
     * Generates unique and random 7 character code.
     *
     * @return the generated code.
     */
    private String generateShortCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            int randomIdx = (int) (Math.random() * 62);
            code.append(alphanumerics[randomIdx]);
        }
        return code.toString();
    }
}
