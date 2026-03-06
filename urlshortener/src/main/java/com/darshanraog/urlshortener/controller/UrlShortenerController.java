package com.darshanraog.urlshortener.controller;


import com.darshanraog.urlshortener.service.IUrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
public class UrlShortenerController {

    private final IUrlShortenerService urlShortenerService;
    private final String basePath = "http://localhost:8080/";

    public UrlShortenerController(IUrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    ResponseEntity<String> createShortUrl(@RequestParam("url") String url) {
        if (Objects.isNull(url) || url.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String shortCode = urlShortenerService.createShortUrl(url);
        return new ResponseEntity<>(basePath + shortCode, HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    ResponseEntity<Void> redirectUrl(@PathVariable String shortCode) {
        if (Objects.isNull(shortCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }
}
