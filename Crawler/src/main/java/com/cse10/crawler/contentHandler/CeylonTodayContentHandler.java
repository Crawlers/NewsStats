package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.DailyMirrorArticle;
import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
import com.cse10.crawler.crawlControler.DailyMirrorCrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sampath Liyanage on 17.07.2014.
 */
public class CeylonTodayContentHandler extends PaperContentHandler {


    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            doc.getElementsByClass("breakings").remove();
            Elements articleElements = doc.select("p");

            for (Element articleElement : articleElements) {
                Article article = new CeylonTodayArticle();
                article.setContent(articleElement.ownText());
                DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    article.setCreatedDate(df.parse(CeylonTodayCrawlController.current_date));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                articles.add(article);
            }


        }

        return articles;
    }
}
