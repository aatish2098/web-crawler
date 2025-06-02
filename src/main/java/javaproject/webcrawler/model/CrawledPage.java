package javaproject.webcrawler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "crawled_pages")
@Getter
@Setter
@NoArgsConstructor
public class CrawledPage {
    @Id
    private String id;
    private String rootUrl;
    private String baseUrl;
    private String pageUrl;
    private String urlAfterBase;
    private int depth;
    private String headContent;
    private LocalDateTime crawledAt;

    public CrawledPage(String baseUrl, String pageUrl, String urlAfterBase, int depth, String headContent) {
        this.baseUrl = baseUrl;
        this.pageUrl = pageUrl;
        this.urlAfterBase = urlAfterBase;
        this.depth = depth;
        this.headContent = headContent;
        this.crawledAt = LocalDateTime.now();
    }
}
