package com.darshanraog.urlshortener.controller;


import com.darshanraog.urlshortener.dto.UrlMappingDTO;
import com.darshanraog.urlshortener.entity.UrlMapping;
import com.darshanraog.urlshortener.service.IUrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
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
    ResponseEntity<Void> redirectUrl(@PathVariable String shortCode, HttpServletRequest request) {
        if (Objects.isNull(shortCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String ipAddress = request.getRemoteAddr();

            // Make sure there aren't too many requests at a time from the same IP
            if (!urlShortenerService.isRequestRateBelowLimit(ipAddress)) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }

            UrlMapping urlMapping = urlShortenerService.getOriginalUrlMapping(shortCode);

            return ResponseEntity.status(302)
                    .location(URI.create(urlMapping.getOriginalUrl()))
                    .build();

        } catch (RuntimeException e) {
            if ("URL expired!".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.GONE).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/{shortCode}")
    ResponseEntity<UrlMappingDTO> getAnalyticsForShortCode(@PathVariable String shortCode) {
        if (Objects.isNull(shortCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(urlShortenerService.getStats(shortCode));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
    }
}
