package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UrlCleanupService {
    private final UrlMappingRepository urlMappingRepository;

    /**
     * Schedules a cron job to delete all expired URLs
     * every Sunday at 5pm IST.
     * Transactional annotation used as Modifying queries
     * must run inside a transaction.
     */
    @Scheduled(cron = "0 0 17 * * SUN", zone = "Asia/Kolkata")
    @Transactional
    void deleteExpiredUrls() {
        System.out.println("== Running url clean up service now! ==");
        urlMappingRepository.deleteExpiredUrls();
        System.out.println("== Cleanup done! ==");
    }
}
