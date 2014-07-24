package com.cse10.database;

/**
 * Created by TharinduWijewardane on 02.07.2014.
 */
import com.cse10.article.Article;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler {

//    public static void main(String args[]) {
//
//        System.out.println("startingggggggggg");
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        session.beginTransaction();
//        Article article = new Article();
//
//        article.setId(100);
//        article.setTitle("title 1111");
//        article.setContent("content bla bla bla");
//        article.setAuthor("author 1111");
//        article.setCreatedDate(new Date());
//
//        session.save(article);
//        session.getTransaction().commit();
//
//    }

//    public static void insertArticle(int id, String title, String content, String author, String createdDate) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        session.beginTransaction();
//        Article article = new Article();
//
//        article.setId(id);
//        article.setTitle(title);
//        article.setContent(content);
//        article.setAuthor(author);
//        article.setCreatedDate(new Date()); // for now
//
//        session.save(article);
//        session.getTransaction().commit();
//    }

    public static void insertArticle(Article article) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(article);
        session.getTransaction().commit();
    }

   public static ArrayList<Article> fettchArticle() {
       ArrayList<Article> articles;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        articles = (ArrayList<Article>)session.createCriteria(Article.class).list();
        session.getTransaction().commit();

       return  articles;
    }

}
