package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.article.DailyMirrorArticle;
import com.cse10.article.TheIslandArticle;
import com.cse10.crawler.crawlControler.DailyMirrorCrawlController;
import com.cse10.crawler.crawlControler.TheIslandCrawlController;
import com.cse10.crawler.paperCrawler.TheIslandCrawler;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Element articleElement;
            articleElement = doc.getElementById("left_video_area");
            articleElement.getElementsByTag("div").remove();
            Elements els = articleElement.getElementsByTag("h1");
            Element el = els.first();
            String title = el.text();
            articleElement.getElementsByTag("h1").remove();
            articleElement.getElementsByClass("article_date").remove();



                String content = articleElement.text();
                if (content.length() < 150 || title.equals("News")){
                    return null;
                }

                if (filterArticles(content)) {
//
                    Article article = new TheIslandArticle();
                    article.setTitle(title);
                    String author = null;

                    els = articleElement.getElementsByTag("p");
                    article.setContent(articleElement.text());
                    for (Element ele : els) {
                        if (ele.text().toLowerCase().startsWith("by") && ele.text().length() < 100){
                               author = ele.text();
                               ele.remove();
                               author = author.replaceFirst("(By|by|BY)\\s","");
                            article.setContent(articleElement.text());
                        }
                    }
                    if (author == null && !articleElement.ownText().trim().equals("")){
                        author = articleElement.ownText();
                        String cont = articleElement.text();
                        article.setContent(cont.replaceFirst(author, ""));
                        author = author.replaceFirst("(By|by|BY)\\s","");
                    }

                    if (author == null) {
                        String text = articleElement.text();
                        Pattern pattern = Pattern.compile("^(By|by|BY)\\s([A-Z][^\\s]*\\s)+");
                        Matcher matcher = pattern.matcher(text);
                        if (matcher.find()) {
                            author = matcher.group().trim();
                            author = author.substring(0, author.lastIndexOf(" "));
                            text = text.replaceFirst(author, "");
                            article.setContent(text);
                            author = author.replaceFirst("(By|by|BY)\\s", "");
                            article.setAuthor(author);
                        } else {
                            article.setContent(text);
                        }
                    }
                    article.setAuthor(author);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        article.setCreatedDate(df.parse(TheIslandCrawlController.current_date));
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    articles.add(article);

                    return articles;
                }
            }


         return null;

    }

}
