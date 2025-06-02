package javaproject.webcrawler.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javaproject.webcrawler.model.CrawledPage;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CrawlerUI extends JFrame {
    private JTextField urlField;
    private JSpinner depthSpinner;
    private JButton startButton;
    private JButton showButton;
    private JTable table;
    private JLabel metricsLabel;
    private DefaultTableModel tableModel;
    private ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public CrawlerUI() {
        setTitle("Multithreaded Web Crawler");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout());
        urlField = new JTextField("https://aatish.org", 30);
        depthSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        startButton = new JButton("Start Crawl");
        showButton = new JButton("Show Crawled pages");
        topPanel.add(new JLabel("Start URL:")); topPanel.add(urlField);
        topPanel.add(new JLabel("Depth:")); topPanel.add(depthSpinner);
        topPanel.add(startButton); topPanel.add(showButton);

        // Table setup
        tableModel = new DefaultTableModel(
                new Object[]{"Page URL","Relative URL","Depth","Head HTML","Crawled At"},
                0
        );
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        metricsLabel = new JLabel("Metrics: ");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(tableScroll, BorderLayout.CENTER);
        getContentPane().add(metricsLabel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            try {
                startCrawl();
                loadData();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        showButton.addActionListener(e -> loadData());
    }

    private void startCrawl() throws Exception {
        invokeEndpoint("/crawl/start?url=" + urlField.getText() + "&depth=" + depthSpinner.getValue(), "POST");
    }

    private void loadData() {
        try {
            String baseUrl = urlField.getText();

            // Pass the baseUrl parameter to the endpoint
            String metaJson = invokeEndpoint("/crawl/getCrawledData?baseUrl=" +
                    java.net.URLEncoder.encode(baseUrl, "UTF-8"), "GET");

            List<CrawledPage> pages = mapper.readValue(metaJson,
                    new TypeReference<List<CrawledPage>>(){});

            tableModel.setRowCount(0);

            for (CrawledPage p : pages) {
                tableModel.addRow(new Object[]{
                        p.getPageUrl(),
                        p.getUrlAfterBase(),
                        p.getDepth(),
                        p.getHeadContent().length() > 50 ?
                                p.getHeadContent().substring(0, 50) + "..." :
                                p.getHeadContent(),
                        p.getCrawledAt().toString()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }


    private String invokeEndpoint(String path, String method) throws Exception {
        URL url = new URL("http://localhost:8080" + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        try (InputStream in = conn.getInputStream()) {
            return new String(in.readAllBytes());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CrawlerUI::new);
    }
}
