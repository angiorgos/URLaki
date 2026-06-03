package com.urlaki.Repository;

import com.urlaki.model.Urls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Urls, Long>{

    Optional<Urls> findByBigURL(String bigURL);

    Optional<Urls> findByShortURL(String shortURL);
}
