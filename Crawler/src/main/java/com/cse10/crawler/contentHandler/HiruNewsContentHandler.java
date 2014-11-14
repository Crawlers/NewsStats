package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.HiruNewsArticle;
import com.cse10.crawler.crawlControler.HiruNewsCrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tharindu on 2014-11-13.
 */
public class HiruNewsContentHandler extends PaperContentHandler {

    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            Element articleElement = doc.getElementsByClass("left_txt").first();

            if (articleElement == null) { // if no article can be found
                return articles;
            }

            Element titleElement;
            if (!articleElement.getElementsByClass("lft_txtpc").isEmpty()) {
                titleElement = articleElement.getElementsByClass("lft_txtpc").first();
            } else {
                titleElement = articleElement.getElementsByClass("nws_tpc").first();
            }
            String title = titleElement.ownText();

            if (title != null && title.length() > 100) {
                title = title.substring(0, 100);
            }

            String dateString = titleElement.getElementsByClass("time").first().ownText();
            Date date = null;
            try {
                date = new SimpleDateFormat("EEEE, d MMMM yyyy").parse(dateString);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                if (cal.get(Calendar.YEAR) != HiruNewsCrawlController.cal.get(Calendar.YEAR) || cal.get(Calendar.MONTH) != HiruNewsCrawlController.cal.get(Calendar.MONTH)) {
                    return articles; // if news does not belong to current month (ad news)
                }

            } catch (ParseException e) {
                System.out.println("Error ::: Date not available");
                e.printStackTrace();
                return articles; // if date cannot be extracted
            }
            String author = null; // no author

            String content;
            if (!articleElement.getElementsByClass("hnimage").isEmpty()) {
                content = articleElement.getElementsByClass("hnimage").first().ownText().trim();
            } else {
                content = articleElement.getElementsByClass("lft_newscnt").first().ownText().trim();
            }

            if (!filterArticles(content)) {
                return articles; // ignore the article if filter does not approve
            }

            Article article = new HiruNewsArticle();
            article.setTitle(title);
            article.setCreatedDate(date);
            article.setAuthor(author);
            article.setContent(content);

            articles.add(article);

        }
        return articles;
    }

}
