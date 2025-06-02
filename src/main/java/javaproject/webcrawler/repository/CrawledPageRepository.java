package javaproject.webcrawler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import javaproject.webcrawler.model.CrawledPage;

import java.util.List;

public interface CrawledPageRepository extends MongoRepository<CrawledPage, String> {
    long deleteByBaseUrl(String baseUrl);
    List<CrawledPage> findByBaseUrl(String baseUrl);
}