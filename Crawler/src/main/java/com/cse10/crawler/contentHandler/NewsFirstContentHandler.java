package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.NewsFirstArticle;
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
 * Created by TharinduWijewardane on 17.07.2014.
 */
public class NewsFirstContentHandler extends BasicContentHandler {
    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {

            String postId = "post-" + page.getWebURL().getPath().replaceAll(".*/", "");
            System.out.println(page.getWebURL().getPath());
            System.out.println(postId);

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            Element articleElement = doc.getElementById(postId);

            if (articleElement == null) { // if no article can be found
                return articles;
            }

            String title = articleElement.getElementsByClass("post-title").first().ownText();
            String dateString = articleElement.getElementsByClass("date").first().ownText();
            Date date = null;
            try {
                date = new SimpleDateFormat("MMMM d, yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String author = articleElement.getElementsByAttributeValue("rel", "author").first().ownText();
            String content = "";
            Elements contentElements = articleElement.select("p:not(.post-meta)");
            for (Element contentElement : contentElements) {
                content += contentElement.ownText();
            }

            if (!filterArticles(content)) {
                return articles; // ignore the article if filter does not approve
            }

            Article article = new NewsFirstArticle();
            article.setTitle(title);
            article.setCreatedDate(date);
            article.setAuthor(author);
            article.setContent(content);

            articles.add(article);

        }
        return articles;
    }
}
