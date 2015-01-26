package com.cse10.database;

/**
 * Created by TharinduWijewardane on 02.07.2014.
 */

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import com.cse10.entities.LocationDistrictMapper;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DatabaseHandler {

    /**
     * insert an article (table will be selected according to the type of object)
     *
     * @param article
     */
    public static void insertArticle(Article article) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(article);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * update an article (table will be selected according to the type of object)
     *
     * @param article
     */
    public static void updateArticle(Article article) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.update(article);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * save a crime article and update ppr article in a single transaction
     *
     * @param crimeArticle
     * @param article
     */
    public static void insertCrimeArticleAndUpdatePprArticle(CrimeArticle crimeArticle, Article article) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            session.save(crimeArticle);
            session.update(article);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                session.getTransaction().rollback();
                System.out.println("Error: Transaction rolled back " + e);
            } catch (RuntimeException rbe) {
                System.out.println("Error: Could not roll back transaction " + rbe);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * insert multiple articles (table will be selected according to the type of object)
     *
     * @param articles
     */
    public static void insertArticles(List<? extends Article> articles) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        for (Article article : articles) {
            session.save(article);
        }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * fetch articles of given class (given table)
     *
     * @param articleClass ex:- CeylonTodayArticle.class
     * @return
     */
    public static List<Article> fetchArticles(Class articleClass) {
        ArrayList<Article> articles;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        articles = (ArrayList<Article>) session.createCriteria(articleClass).list();
        session.getTransaction().commit();
        session.close();

        return articles;
    }

    /**
     * fetch articles of given class (given table) which have the specified IDs
     *
     * @param articleClass ex:- CeylonTodayArticle.class
     * @param idList       list of ids which the fetched rows should have
     * @return
     */
    public static List<Article> fetchArticlesByIdList(Class articleClass, List<Integer> idList) {

        ArrayList<Article> articles;

        if (idList.isEmpty()) {
            return new ArrayList<Article>();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        articles = (ArrayList<Article>) session.createCriteria(articleClass)
                .add(Restrictions.in("id", idList))
                .list();
        session.getTransaction().commit();
        session.close();

        return articles;
    }

    /**
     * @param articleClass ex:- Article.class
     * @param startId      start id (inclusive)
     * @param endId        end id (inclusive)
     * @return
     */
    public static List<Article> fetchArticlesByIdRange(Class articleClass, int startId, int endId) {

        ArrayList<Article> articles;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        articles = (ArrayList<Article>) session.createCriteria(articleClass)
                .add(Restrictions.ge("id", startId))
                .add(Restrictions.le("id", endId))
                .list();
        session.getTransaction().commit();
        session.close();

        return articles;
    }

    /**
     * fetch articles of given class (given table) which have null values for label column
     *
     * @param articleClass ex:- CeylonTodayArticle.class
     * @return
     */
    public static List<Article> fetchArticlesWithNullLabels(Class articleClass) {
        ArrayList<Article> articles;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        articles = (ArrayList<Article>) session.createCriteria(articleClass)
                .add(Restrictions.isNull("label"))
                .list();
        session.getTransaction().commit();
        session.close();

        return articles;
    }

    /**
     * execute a query without using hibernate and return ResultSet
     *
     * @param query
     * @return
     */
    public static ResultSet executeQuery(String query) {

        java.sql.Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USERNAME, DatabaseConstants.DB_PASSWORD);

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rs;
        }
    }

    /**
     * execute an update query without using hibernate and return ResultSet
     *
     * @param query
     */
    public static void executeUpdate(String query) {

        java.sql.Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USERNAME, DatabaseConstants.DB_PASSWORD);

            // create the java statement
            Statement st = conn.createStatement();

            // execute the update
            st.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * insert an object containing crime entities
     *
     * @param crimeEntityGroup
     */
    public static void insertCrimeEntities(CrimeEntityGroup crimeEntityGroup) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(crimeEntityGroup);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * insert multiple objects containing crime entities
     *
     * @param crimeEntityGroups details about crime entity
     */
    public static void insertCrimeEntityGroups(List<CrimeEntityGroup> crimeEntityGroups) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        for (CrimeEntityGroup crimeEntityGroup : crimeEntityGroups) {
            session.save(crimeEntityGroup);
        }
        session.getTransaction().commit();
        session.close();
    }


    /**
     * fetch ArrayList of objects containing crime entities
     *
     * @return ArrayList<CrimeEntityGroup> entityGroups
     */
    public static ArrayList<CrimeEntityGroup> fetchCrimeEntityGroups() {
        ArrayList<CrimeEntityGroup> entityGroups;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        entityGroups = (ArrayList<CrimeEntityGroup>) session.createCriteria(CrimeEntityGroup.class).list();
        session.getTransaction().commit();
        session.close();

        return entityGroups;
    }

    /**
     * fetch a list of CrimeEntityGroups within the given id range
     *
     * @param startId start id (inclusive)
     * @param endId   end id (inclusive)
     * @return
     */
    public static List<CrimeEntityGroup> fetchCrimeEntityGroupsByIdRange(int startId, int endId) {

        ArrayList<CrimeEntityGroup> crimeEntityGroups;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        crimeEntityGroups = (ArrayList<CrimeEntityGroup>) session.createCriteria(CrimeEntityGroup.class)
                .add(Restrictions.ge("id", startId))
                .add(Restrictions.le("id", endId))
                .list();
        session.getTransaction().commit();
        session.close();

        return crimeEntityGroups;
    }

    /**
     * insert details of a certain location
     *
     * @param locationDistrict
     */
    public static void insertLocationDistrict(LocationDistrictMapper locationDistrict) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(locationDistrict);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * fetch details of a certain location
     *
     * @param location name of the location
     * @return LocationDistrictMapper contains district of the location
     */
    public static LocationDistrictMapper fetchLocation(String location) {

        LocationDistrictMapper locationDistrict = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        locationDistrict = (LocationDistrictMapper) session.load(LocationDistrictMapper.class, location);
        Hibernate.initialize(locationDistrict);
        session.getTransaction().commit();
        session.close();

        return locationDistrict;
    }

    /**
     * insert details of a person related to a crime
     *
     * @param crimePerson name of the person and crime he/she involved
     */
    public static void insertCrimePerson(CrimePerson crimePerson) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(crimePerson);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * fetch ArrayList of people involved in a crime
     *
     * @param entityGroupID certain crime entity
     * @return ArrayList<CrimePerson> people involved in the crime
     */
    public static ArrayList<CrimePerson> fetchCrimePeople(int entityGroupID) {

        ArrayList<CrimePerson> crimePersonList;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        crimePersonList = (ArrayList<CrimePerson>) session.createCriteria(CrimePerson.class).list();
        Hibernate.initialize(crimePersonList);
        session.getTransaction().commit();
        session.close();

        return crimePersonList;
    }

    /**
     * insert a certain crime entity and people involved in it at a single operation
     *
     * @param crimeEntityGroup details of a certain crime entity
     * @param crimePeopleSet   names of people involved in that crime
     */
    public static void insertCrimeDetails(CrimeEntityGroup crimeEntityGroup, HashSet<String> crimePeopleSet) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(crimeEntityGroup);

        if (crimePeopleSet != null && !crimePeopleSet.isEmpty()) {
            for (String person : crimePeopleSet) {
                CrimePerson crimePerson = new CrimePerson();
                crimePerson.setName(person);
                crimePerson.setEntityGroup(crimeEntityGroup);
                session.save(crimePerson);
                crimeEntityGroup.getCrimePersonSet().add(crimePerson);
            }
        }

        session.save(crimeEntityGroup);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * get the row count of a table containing articles of given type
     *
     * @param articleClass
     * @return
     */
    public static int getRowCount(Class articleClass) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Long count = (Long) session.createCriteria(articleClass)
                .setProjection(Projections.rowCount()).uniqueResult();
        session.close();
        return count.intValue();
    }

    /**
     * get the count of rows having given value for given column of a table containing articles of given type
     *
     * @param articleClass
     * @param column
     * @param value
     * @return
     */
    public static int getRowCount(Class articleClass, String column, String value) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Long count = (Long) session.createCriteria(articleClass)
                .add(Restrictions.eq(column, value))
                .setProjection(Projections.rowCount()).uniqueResult();
        session.close();
        return count.intValue();
    }

    /**
     * get the max id value of the table containing articles of given type
     *
     * @param articleClass ex:- CeylonTodayArticle.class
     * @return
     */
    public static int getMaxIdOf(Class articleClass, String newsPaper) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        Integer count = (Integer) session.createCriteria(articleClass).add(Restrictions.eq("newspaper", newsPaper)).setProjection(Projections.max("newspaperId")).uniqueResult();

        session.getTransaction().commit();
        session.close();

        return count.intValue();
    }

    /**
     * get latest date of the table containing articles of given type
     *
     * @param articleClass
     * @return
     */
    public static java.util.Date getLatestDate(Class articleClass) {

        if (getRowCount(articleClass) == 0) { // if there are no articles
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        java.sql.Date latestDate = (java.sql.Date) session.createCriteria(articleClass)
                .setProjection(Projections.max("createdDate")).uniqueResult();
        session.close();

        return new java.util.Date(latestDate.getTime()); //convert from sql date to util date
    }

    /**
     * get latest date string of the table containing articles of given type
     *
     * @param articleClass
     * @return
     */
    public static String getLatestDateString(Class articleClass) {

        java.util.Date date = getLatestDate(articleClass);

        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * closes the hibernate session factory. (otherwise JVM won't stop)
     */
    public static void closeDatabase() {
        HibernateUtil.shutdown();
    }
}
