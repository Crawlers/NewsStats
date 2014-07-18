package com.cse10.crawler.crawlControler;

/**
 * Created by Sampath Liyanage on 13.07.2014.
 */

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

abstract public class BasicCrawlController {

    private CrawlConfig config;

    //global configurations
    final String CRAWL_STORAGE_DIR_ROOT = "/home/sampath/FYP/crawlData";
    final String PROXY_ADDRESS = "cache.mrt.ac.lk";
    final int PROXY_PORT = 3128;

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

    ;

    abstract public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception;
}
