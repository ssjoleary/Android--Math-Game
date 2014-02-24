package com.samoleary.Mobile_Asn_Two;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
 *      This class handles the opening, closing, inserting and retrieving of data and information from the SQLite
 *      Database.
 */

public class GameDB {
    private SQLiteDatabase db;
    private final Context context;
    private final MyDBhelper dbhelper;      // Responsible for the creation of tables.

    public GameDB(Context c) {
        context = c;
        dbhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /**
     * Closes the connection to the Database
     */
    public void close(){
        db.close();
    }

    /**
     * Attempts to open a connection to a Database to write to. If this fails an exception is thrown and an attempt to
     * connect to a Database to read from is made.
     * @throws SQLiteException
     */
    public void open() throws SQLiteException {
        try {
            db = dbhelper.getWritableDatabase();
        } catch(SQLiteException e) {
            System.out.println("open database exception caught");
            Log.v("open database exception caught", e.getMessage());
            db = dbhelper.getReadableDatabase();
        }
    }

    /**
     * This method, insertInfo(), is called once information has been retrieved and prepared elsewhere and is now ready
     * to be inserted into the Database.
     *
     * @param gamesPlayed
     *      Number of games played by the user.
     * @param highScore
     *      Highest Score acheived.
     * @param xp
     *      Total experience points earned.
     * @param level
     *      Current skill level of the user.
     * @return
     *      A return value of anything but -1 is good, -1 means an exception has occurred and the insert was unsuccessful.
     */
    public long insertInfo(int gamesPlayed, int highScore, int xp, int level) {
        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.GAMES_PLAYED, gamesPlayed);
            newTaskValue.put(Constants.HIGH_SCORE, highScore);
            newTaskValue.put(Constants.XP, xp);
            newTaskValue.put(Constants.LEVEL, level);
            return db.insert(Constants.TABLE_NAME, null, newTaskValue);
        } catch(SQLiteException e) {
            System.out.println("Insert into database exception caught");
            Log.v("Insert into database exception caught", e.getMessage());
            return -1;
        }
    }

    /**
     * This method, getInfo(), queries the Database and returns all the information it contains.
     *
     * @return
     *      Returns the Cursor containing the information retrieved.
     */
    public Cursor getInfo() {
        Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
        return c;
    }

    /**
     * This method, dropTable(), is called when the user resets their profile. The table is dropped, or deleted, and all
     * the information gathered up to that point is lost.
     */
    public void dropTable() {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        dbhelper.onCreate(db);
    }
}
