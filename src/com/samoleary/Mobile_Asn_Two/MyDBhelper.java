package com.samoleary.Mobile_Asn_Two;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 28/11/13
 * Revision: 2
 * Revision History:
 *      1: 28/11/13
 *      2: 30/11/13
 *
 * Description:
 *      This Class is a helper class for the GameDB class. Its methods deal with the creation of tables and handling of
 *      upgrades to the Database.
 */

public class MyDBhelper extends SQLiteOpenHelper {
    // This is the SQLite command to create a table containing columns for the games played, high score, experience points
    // and current level. There is also a column for the Key Id which is the primary key for the table and uniquely
    // identifies each row in the table.
    private static final String CREATE_TABLE="create table "+
            Constants.TABLE_NAME+" ("+
            Constants.KEY_ID+" integer primary key autoincrement, "+
            Constants.GAMES_PLAYED+" integer not null, "+
            Constants.HIGH_SCORE+" integer not null, "+
            Constants.XP+" integer not null, "+
            Constants.LEVEL+" integer not null);";

    /**
     * The constructor for the MyDBhelper class.
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MyDBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    /**
     * This method, onCreate(), creates the table using the command shown above.
     *
     * @param db
     *      The Database in which the table is to be created.
     */
    public void onCreate(SQLiteDatabase db) {
        Log.v("MyDBhelper onCreate","Creating all the tables");
        try {
            db.execSQL(CREATE_TABLE);
        } catch(SQLiteException e) {
            Log.v("Create table exception", e.getMessage());
        }
    }

    /**
     * This method, onUpgrade(), drops the existing table to allow for the upgrade to take place. Afterwards it creates
     * the table again. If certain precautions aren't taken before the upgrade to backup the table then it will be lost
     * during the uprade. The new table created is blank.
     *
     * @param db
     *      The Database in which the table is stored.
     *
     * @param oldVersion
     *      The old version number.
     *
     * @param newVersion
     *      The new version number.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version "+oldVersion +" to "+newVersion +", which will destroy all old data");
        db.execSQL("drop table if exists " +Constants.TABLE_NAME);
        onCreate(db);
    }
}
