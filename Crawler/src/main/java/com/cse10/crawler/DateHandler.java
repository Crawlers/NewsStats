package com.cse10.crawler;

import com.cse10.database.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import org.hibernate.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 8/31/14.
 */
public class DateHandler {
         public static String getFromDateToResume(String startingDate, String tableName){

             Session session = HibernateUtil.getSessionFactory().openSession();

             String q = "select max(created_date) from " + tableName ;
             List results = session.createSQLQuery(q).list();
             Iterator iterator = results.iterator();
             Date latestDateCrawled = (Date) iterator.next();

             if (latestDateCrawled != null){
                 Session session1 = HibernateUtil.getSessionFactory().openSession();
                 Transaction transaction1 = session1.beginTransaction();
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 q = "delete from " + tableName + " where created_date >= '" + sdf.format(latestDateCrawled) + "'";
                 session1.createSQLQuery(q).executeUpdate();
                 transaction1.commit();
                 try {
                     if (latestDateCrawled.compareTo(sdf.parse(startingDate)) > 0){
                         return sdf.format(latestDateCrawled);
                     }
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }
             return startingDate;
         }
}
