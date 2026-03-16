package com.darshanraog.urlshortener.controller;


import com.darshanraog.urlshortener.dto.UrlMappingDTO;
import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.service.IUrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    private final IUrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    ResponseEntity<String> createShortUrl(@RequestParam("url") String url) {
        if (Objects.isNull(url) || url.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(urlShortenerService.createShortUrl(url), HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    ResponseEntity<Void> redirectUrl(@PathVariable String shortCode) {
        if (Objects.isNull(shortCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UrlMapping urlMapping = urlShortenerService
                .getOriginalUrlMapping(shortCode);
        if (Objects.isNull(urlMapping.getExpiresAt()) || urlMapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        String originalUrl = urlMapping
                .getOriginalUrl();
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }

    @GetMapping("/stats/{shortCode}")
    ResponseEntity<UrlMappingDTO> getAnalyticsForShortCode(@PathVariable String shortCode) {
        if (Objects.isNull(shortCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(urlShortenerService.getStats(shortCode));
    }
}
