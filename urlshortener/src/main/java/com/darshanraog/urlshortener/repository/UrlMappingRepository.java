package com.darshanraog.urlshortener.repository;

import com.darshanraog.urlshortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    /**
     * Returns the UrlMapping with the given shortcode.
     *
     * @param shortCode shortcode to look for.
     * @return the Url Mapping POJO.
     */
    UrlMapping findByShortCode(String shortCode);

    /**
     * Deletes URLs which are expired.
     * Modifying annotation added for indicating it is
     * updating/deleting data through the native query.
     */
    @Modifying
    @Query(value = "DELETE FROM url_mapping WHERE expires_at < NOW()", nativeQuery = true)
    void deleteExpiredUrls();
}
