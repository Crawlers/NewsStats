package com.cse10.crawler.crawlControler;

/**
 * Created by Sampath Liyanage on 13.07.2014.
 */

import com.cse10.util.GlobalConstants;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

abstract public class BasicCrawlController extends Observable {

    private CrawlConfig config;

    //global configurations
    final String CRAWL_STORAGE_DIR_ROOT = "E:/CrawlData";
    final String PROXY_ADDRESS = GlobalConstants.PROXY_ADDRESS;
    final int PROXY_PORT = GlobalConstants.PROXY_PORT;

    // start and end dates used by sub classes
    protected String startDate;
    protected String endDate;

    protected CrawlController controller; //to be used in subclasses
    protected boolean crawlingStopped = false;

    /**
     * @param startDate format: yyyy-MM-dd
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(Date startDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.startDate = sdf.format(c.getTime());
    }

    /**
     * @param endDate format: yyyy-MM-dd
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(Date endDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.endDate = sdf.format(c.getTime());
    }

    public BasicCrawlController() {

        config = new CrawlConfig();


        //***********setting default configurations*********
        /*
         * crawlStorageFolder is a folder where intermediate crawl data is
         * stored.
         */
        String crawlStorageFolder = CRAWL_STORAGE_DIR_ROOT + "/" + this.getClass().getSimpleName();

        /*
         * numberOfCrawlers shows the number of concurrent threads that should
         * be initiated for crawling.
         */
        int numberOfCrawlers = 4; //Integer.parseInt(args[1]);

        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(1000);

        /*
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        config.setMaxDepthOfCrawling(1);

        /*
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(-1);

        /*
         * Do you need to set a proxy? If so, you can use:
         */
        if (!PROXY_ADDRESS.isEmpty() && PROXY_PORT != 0) {
            config.setProxyHost(PROXY_ADDRESS);
            config.setProxyPort(PROXY_PORT);
        }

         /*
         * If your proxy also needs authentication:
         * config.setProxyUsername(username); config.getProxyPassword(password);
         * --------Isn't it proxy.setProxyPassword(password) ?---------
         */

        /*
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false);

        //*********setting customized configurations*************
        configure(config);
        System.out.println(config.toString()); // print config
    }

    public CrawlConfig getConfig() {
        return config;
    }

    //extend if default configurations should be customized
    protected CrawlConfig configure(CrawlConfig crawlConfig) {
        return config;
    }

    abstract public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception;

    public void stopCrawl() {
        crawlingStopped = true;
        if (controller != null) {
            controller.shutdown();
        }
    }
}
