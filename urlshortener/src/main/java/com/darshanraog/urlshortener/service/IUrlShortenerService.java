package com.darshanraog.urlshortener.service;

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
    String getOriginalUrl(String shortCode);

    //TODO: Might add this functionality
    void incrementClickCount();

    //TODO: Might add this functionality
    LocalDateTime checkExpiryDate();
}
