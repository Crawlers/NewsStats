package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
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
public class CeylonTodayContentHandler extends BasicContentHandler {


    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            doc.getElementsByClass("breakings").remove();
            Elements articleElements = doc.select("p");

            for (Element articleElement : articleElements) {

                String title = articleElement.getElementsByClass("newsdetailssubtitle").remove().text();
                String content = articleElement.text();

                content = content.replaceFirst("^By.*\\s\\s", "");

                if (!filterArticles(content)) {
                    continue; // ignore the article if filter does not approve
                }

                Article article = new CeylonTodayArticle();
                article.setTitle(title);
                article.setContent(content);

                String sentences[] = content.split("\\.");
                if (sentences[0].matches("^By.*")) {
                    String author = sentences[0].replace("By", "");
                    author = author.replace("\u00a0", "");
                    String authorData[] = author.split("  +");
                    author = authorData[0];
                    author = author.trim();
                    article.setAuthor(author);
                    content = content.replaceFirst("^By.*" + author, "");
                }
                content = content.replace("\u00a0", "");
                content = content.trim();
                article.setContent(content);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
