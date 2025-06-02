package javaproject.webcrawler.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import javaproject.webcrawler.model.PageMetadata;

@Service
public class CrawlService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Queue<PageMetadata> metadataQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
<<<<<<< HEAD
=======
    private String baseUrl;
    private String rootUrl;
>>>>>>> b957fe3 (enhancements)

    public void startCrawling(String startUrl, int maxDepth) {
        metadataQueue.clear();
        visited.clear();
        if (visited.add(startUrl)) {
            this.deleteByBaseUrl(startUrl);
            executor.submit(new CrawlerTask(startUrl, 0, maxDepth));
        }
    }

    public long deleteByBaseUrl(String baseUrl) {
        return pageRepo.deleteByBaseUrl(baseUrl);
    }

    public List<PageMetadata> getMetadata() {
        return metadataQueue.stream().collect(Collectors.toList());
    }

    public Metrics getMetrics() {
        List<PageMetadata> list = getMetadata();
        double avgSize = list.stream().mapToInt(PageMetadata::getContentLength).average().orElse(0);
        long successCount = list.stream().filter(m -> m.getResponseCode() == 200).count();
        long errorCount = list.size() - successCount;
        return new Metrics(avgSize, successCount, errorCount, list.size());
    }

    public List<CrawledPage> getCrawledData(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return pageRepo.findAll(); // Return all pages if no baseUrl provided
        }
        return pageRepo.findByBaseUrl(baseUrl); // Return pages filtered by baseUrl
    }


    private class CrawlerTask implements Runnable {
        private final String pageUrl;
        private final int depth;
        private final int maxDepth;

        public CrawlerTask(String pageUrl, int depth, int maxDepth) {
            this.pageUrl = pageUrl;
            this.depth = depth;
            this.maxDepth = maxDepth;
        }

        @Override
        public void run() {
            try {
                URL urlObj = new URL(pageUrl);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                String threadName = Thread.currentThread().getName();
                System.out.println("[" + threadName + "] Crawling " + pageUrl + " at depth " + depth);
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                int code = conn.getResponseCode();
                int length = conn.getContentLength();

                metadataQueue.add(new PageMetadata(pageUrl, length, code, depth));

<<<<<<< HEAD
                if (depth < maxDepth && code == 200 && conn.getContentType().contains("text/html")) {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("a[href]");
                    links.stream()
=======
                if (depth <= maxDepth && code == 200 && conn.getContentType().contains("text/html")) {
                    Document doc = Jsoup.connect(pageUrl).get();
                    // extract <head>
                    String headContent = doc.select("head").html();
                    // derive relative URL
                    String urlAfterBase = pageUrl.startsWith(baseUrl)
                            ? pageUrl.substring(baseUrl.length())
                            : pageUrl;

//                    pageRepo.
                    // save to MongoDB
                    String id_ = String.valueOf(pageRepo.save(new CrawledPage(baseUrl, pageUrl, urlAfterBase, depth, headContent)));
                    System.out.println(id_);
                    // follow links
                    if(depth < maxDepth) {
                        Elements links = doc.select("a[href]");
                        links.stream()
>>>>>>> b957fe3 (enhancements)
                            .map(link -> link.absUrl("href"))
                            .filter(link -> link.startsWith("http"))
                            .distinct()
                            .forEach(link -> {
                                if (visited.add(link)) {
                                    executor.submit(new CrawlerTask(link, depth + 1, maxDepth));
                                }
                            });
                    }
                }
            } catch (Exception e) {
                System.out.println("Crawling " + pageUrl + " failed: " + e);
                metadataQueue.add(new PageMetadata(pageUrl, 0, -1, depth));
            }
        }
    }

    // Metrics.java
    public static class Metrics {
        private double averageSize;
        private long successCount;
        private long errorCount;
        private long total;

        public Metrics(double averageSize, long successCount, long errorCount, long total) {
            this.averageSize = averageSize;
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.total = total;
        }
        // Getters
        public double getAverageSize() { return averageSize; }
        public long getSuccessCount() { return successCount; }
        public long getErrorCount() { return errorCount; }
        public long getTotal() { return total; }
    }
}
