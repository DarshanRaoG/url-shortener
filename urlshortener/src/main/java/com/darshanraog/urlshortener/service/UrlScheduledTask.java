package com.darshanraog.urlshortener.service;

import com.darshanraog.urlshortener.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UrlScheduledTask {
    private final StringRedisTemplate redisTemplate;
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
        System.out.println("== Running url clean up now! ==");
        urlMappingRepository.deleteExpiredUrls();
        System.out.println("== Cleanup done! ==");
    }

    /**
     * Syncs click count from redis to db every 2 minutes.
     */
    @Scheduled(fixedRate = 120000)
    @Transactional
    void syncClicksToDatabase() {
        System.out.println("== Running click count sync up now! ==");

        Set<String> keys = redisTemplate.keys("clicks:*");
        if (Objects.isNull(keys) || keys.isEmpty()) {
            System.out.println("== Nothing to sync! ==");
            return;
        }

        for (String key : keys) {
            String countStr = redisTemplate.opsForValue().getAndSet(key, "0");
            if (Objects.isNull(countStr) || countStr.equals("0")) {
                continue;
            }
            long count = Long.parseLong(countStr);
            String shortCode = key.substring(7); // removing "clicks:"

            urlMappingRepository.incrementCountBy(shortCode, count);
        }

        System.out.println("== Sync up done! ==");
    }
}
