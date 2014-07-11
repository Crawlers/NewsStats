package com.cse10.crawler.contenthandler;

import com.cse10.article.Article;
import com.cse10.article.TheIslandArticle;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by TharinduWijewardane on 10.07.2014.
 */
public class TheIslandContentHandler extends PaperContentHandler {
    @Override
    public List extractArticles(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parseBodyFragment(html);
            Elements divs = doc.select("body div");

            for (Element div : divs) {
                Elements paras = div.getElementsByTag("p");
                for (Element para : paras) {
                    Article article = new TheIslandArticle();
                    article.setContent(para.text());
                    articles.add(article);
                }
            }

        }

        return articles;
    }
}
