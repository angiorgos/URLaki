package com.urlaki.Repository;

import com.urlaki.model.Urls;
import com.urlaki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Urls, Long> {

    Optional<Urls> findByOwnerAndBigUrl(User owner, String bigUrl);

    Optional<Urls> findByShortUrl(String shortUrl);

    java.util.List<Urls> findByOwner(User owner);
}