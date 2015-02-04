package com.cse10.crawler.paperCrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: sampath
 * Date: 7/14/14
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicCrawler extends WebCrawler {

    protected Logger logger = Logger.getLogger(this.getClass());

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        logger.info("Docid: " + docid);
        logger.info("URL: " + url);
        logger.info("Domain: '" + domain + "'");
        logger.info("Sub-domain: '" + subDomain + "'");
        logger.info("Path: '" + path + "'");
        logger.info("Parent page: " + parentUrl);
        logger.info("Anchor text: " + anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();

            List<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.info("Text length: " + text.length());
            logger.info("Html length: " + html.length());
            logger.info("Number of outgoing links: " + links.size());
        }

        //        Header[] responseHeaders = page.getFetchResponseHeaders();
//        if (responseHeaders != null) {
//            System.out.println("Response headers:");
//            for (Header header : responseHeaders) {
//                System.out.println("\t" + header.getName() + ": " + header.getValue());
//            }
//        }
    }
}
