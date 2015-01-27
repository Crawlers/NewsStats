package com.cse10.gui;

import com.cse10.database.DatabaseHandler;

/**
 * Created by TharinduWijewardane on 2015-01-25.
 */
public class TempSetNull {

    public static void main(String[] args) {
        DatabaseHandler.executeUpdate("UPDATE `article_the_island` SET label = NULL");
        DatabaseHandler.executeUpdate("UPDATE `article_ceylon_today` SET label = NULL");
        DatabaseHandler.executeUpdate("UPDATE `article_daily_mirror` SET label = NULL");
    }

}
