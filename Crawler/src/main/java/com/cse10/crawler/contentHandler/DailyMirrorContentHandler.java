package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.DailyMirrorArticle;
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
 * Created by TharinduWijewardane on 10.07.2014.
 */
public class DailyMirrorContentHandler extends PaperContentHandler {


    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            Elements articleElements = doc.getElementsByClass("article-content");

            for (Element articleElement : articleElements) {

                String content = articleElement.ownText();

                if (!filterArticles(content)) {
//                    continue; // ignore the article if it is not crime related
                }

                Article article = new DailyMirrorArticle();
                article.setContent(content);
                String title =  page.getWebURL().getPath().replaceAll("/.*/", "");
                title = title.replaceAll(".html","");
                title = title.replaceAll("^[^a-zA-Z]+","");
                title = title.replaceAll("-"," ");
                article.setTitle(title);
                DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    article.setCreatedDate(df.parse(DailyMirrorCrawlController.current_date));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                articles.add(article);
            }


        }

        return articles;
    }

}
