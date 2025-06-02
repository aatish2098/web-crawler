package javaproject.webcrawler.controller;

import javaproject.webcrawler.model.CrawledPage;
import javaproject.webcrawler.model.PageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javaproject.webcrawler.service.CrawlService;

import java.util.List;

@RestController
@RequestMapping("/crawl")
public class CrawlController {
    @Autowired
    private CrawlService crawlService;

    @PostMapping("/start")
    public String start(@RequestParam String url, @RequestParam(defaultValue = "2") int depth) {
        crawlService.startCrawling(url, depth);
        return "Crawl started for " + url;
    }

    @GetMapping("/metadata")
    public List<PageMetadata> metadata() {
        return crawlService.getMetadata();
    }

    @GetMapping("/metrics")
    public CrawlService.Metrics metrics() {
        return crawlService.getMetrics();
    }

    @GetMapping("/getCrawledData")
    public List<CrawledPage> getCrawledData(@RequestParam String baseUrl) {
        return crawlService.getCrawledData(baseUrl);
    }
}
