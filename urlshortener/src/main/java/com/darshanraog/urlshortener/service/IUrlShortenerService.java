package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.dto.UrlMappingDTO;
import com.darshanraog.urlshortener.entity.UrlMapping;

import java.time.LocalDateTime;

public interface IUrlShortenerService {

    /**
     * Creates a shortened URL for the given URL.
     *
     * @param originalUrl the URL received.
     * @return the shortened URL.
     */
    String createShortUrl(String originalUrl);

    /**
     * Fetch the original URL mapped to the given shortcode.
     *
     * @param shortCode the shortcode received.
     * @return the original URL.
     */
    UrlMapping getOriginalUrlMapping(String shortCode);

    /**
     * Returns statistics for the shortened URL.
     *
     * @param shortCode given shortcode.
     * @return the DTO containing analytics.
     */
    UrlMappingDTO getStats(String shortCode);

    //TODO: Might add this functionality
    LocalDateTime checkExpiryDate();
}
