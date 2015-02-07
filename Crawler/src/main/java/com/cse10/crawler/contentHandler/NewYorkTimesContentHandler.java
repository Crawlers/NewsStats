package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.NewYorkTimesArticle;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-02-07.
 */
public class NewYorkTimesContentHandler extends BasicContentHandler {
    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {

            System.out.println("paaaaaaaaaaaaaaath: "+page.getWebURL());

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            Element articleElement = doc.getElementById("story");

            if (articleElement == null) { // if no article can be found
                return articles;
            }

            String title = articleElement.getElementById("story-heading").ownText();
            String dateString = articleElement.getElementsByClass("dateline").first().getElementsByAttribute("datetime").html();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String author = articleElement.getElementsByClass("byline-author").first().ownText();
            String content = "";
            Elements contentElements = articleElement.select("p.story-body-text.story-content");
            for (Element contentElement : contentElements) {
                content += contentElement.ownText();
            }

            if (!filterArticles(content)) {
                return articles; // ignore the article if filter does not approve
            }

            Article article = new NewYorkTimesArticle();
            article.setTitle(title);
            article.setCreatedDate(date);
            article.setAuthor(author);
            article.setContent(content);

            articles.add(article);

        }
        return articles;
    }
}
