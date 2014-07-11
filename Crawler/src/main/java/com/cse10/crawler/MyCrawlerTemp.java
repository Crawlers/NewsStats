package com.cse10.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */
public class MyCrawlerTemp extends WebCrawler {
//
//    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
//            + "|png|tiff?|mid|mp2|mp3|mp4"
//            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
//            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
//
//    /**
//     * You should implement this function to specify whether
//     * the given url should be crawled or not (based on your
//     * crawling logic).
//     */
//    @Override
//    public boolean shouldVisit(WebURL url) {
//        String href = url.getURL().toLowerCase();
//        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/"); //http://www.ics.uci.edu/
//    }
//
//    /**
//     * This function is called when a page is fetched and ready
//     * to be processed by your program.
//     */
//    @Override
//    public void visit(Page page) {
//        String url = page.getWebURL().getURL();
//        System.out.println("URL: " + url);
//
//        if (page.getParseData() instanceof HtmlParseData) {
//            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//            String text = htmlParseData.getText();
//            String html = htmlParseData.getHtml();
//            List<WebURL> links = htmlParseData.getOutgoingUrls();
//
//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
//        }
//    }
}
