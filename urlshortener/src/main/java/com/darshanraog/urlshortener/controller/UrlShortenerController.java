package com.darshanraog.urlshortener.controller;


import com.darshanraog.urlshortener.service.IUrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        String originalUrl = urlShortenerService
                .getOriginalUrlMapping(shortCode)
                .getOriginalUrl();
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }
}
