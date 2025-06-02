package javaproject.webcrawler.model;

import java.time.LocalDateTime;

public class PageMetadata {
    private String url;
    private int contentLength;
    private int responseCode;
    private int depth;
    private LocalDateTime timestamp;

    public PageMetadata(String url, int contentLength, int responseCode, int depth) {
        this.url = url;
        this.contentLength = contentLength;
        this.responseCode = responseCode;
        this.depth = depth;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getUrl() { return url; }
    public int getContentLength() { return contentLength; }
    public int getResponseCode() { return responseCode; }
    public int getDepth() { return depth; }
    public LocalDateTime getTimestamp() { return timestamp; }
}