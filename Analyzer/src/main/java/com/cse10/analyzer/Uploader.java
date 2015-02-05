package com.cse10.analyzer;

/**
 * Created by sampath on 1/26/15.
 */

import com.cse10.database.HibernateUtil;
import com.google.gson.Gson;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import com.mongodb.util.JSON;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Uploader{
    MongoClient mongoClient;
    String dbName;


    public Uploader(String user, String password, String dbName, String host, String port){
        this.dbName = dbName;
        MongoCredential credential = MongoCredential.createMongoCRCredential(user, dbName, password.toCharArray());
        try {
            ServerAddress address = new ServerAddress(host,Integer.parseInt(port));
            mongoClient = new MongoClient(address, Arrays.asList(credential));
            //mongoClient = new MongoClient(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void upload(String sqlTable, String mongoCollection){
        DB db = mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(mongoCollection);
        BasicDBObject mQuery = new BasicDBObject();
        collection.remove(mQuery);

        Session session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM "+sqlTable;
        SQLQuery sQuery = session.createSQLQuery(sql);
        sQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = sQuery.list();
        session.close();

        DBObject obj = new BasicDBList();
        BulkWriteOperation builder = collection.initializeOrderedBulkOperation();
        for (int i=0; i<results.size(); i++){
            String json = new Gson().toJson(results.get(i));
            DBObject dbObject = (DBObject) JSON.parse(json);
            builder.insert(dbObject);
        }
        BulkWriteResult result = builder.execute();
        System.out.println("updating "+sqlTable+" to the collection "+ mongoCollection +" completed");
    }
}
