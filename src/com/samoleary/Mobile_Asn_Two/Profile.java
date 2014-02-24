package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 4
 * Revision History:
 *      1: 15/11/13
 *      2: 17/11/13
 *      3: 27/11/13
 *      4: 30/11/13
 *
 * Description:
 *      This class is used to display information such as,
 *      Games Played,
 *      Highest Score,
 *      Average Score,
 *      Level and
 *      Experience Points achieved by the user.
 */

public class Profile extends Activity {
    private Typeface chalkTypeFace;         // The Typeface object changes the font of the text in view to which it is applied to.
    private TextView profile_level;
    private TextView profile_highestscore;
    private TextView profile_gamesplayed;
    private TextView profile_avgscore;
    private TextView profile_levelvalue;
    private TextView profile_hsvalue;
    private TextView profile_asvalue;
    private TextView profile_gpvalue;
    private TextView profile_xp;
    private TextView profile_xpvalue;

    private GameDB dba;                     // The Database object used to read/write to the SQLite Database.
    private ArrayList<GameData> gameDatas;  // This ArrayList will hold GameData objects. These objects contain data about the user
                                            // that are stored in a Database.

    private GameHelper helper;              // This object contains 2 methods that calculate the largest number in a series of numbers
                                            // and the average of a series of numbers.

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        init();
    }

    /**
     * onCreateOptionsMenu allows menu items to be placed in the Action Bar that runs along the top of the screen. The
     * menu items and their properties, such as the id and icon, are described in the XML file 'prof_action_menu'.
     * The MenuInflater takes this list and literally 'inflates' it into a menu to be displayed.
     *
     * @param menu
     *      The Menu object representing the Menu to be 'inflated', or populated.
     *
     * @return
     *      Returns either True or False if the Menu has been created successfully or not.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prof_action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This initialize method sets up the Views to be displayed to the user, as well as configuring a font for the Typeface
     * object to use and applying this Typeface to all the Views with text in them.
     * A 'Back' button is also enabled/created on the left hand side of the Action Bar. This 'Back' button is a left pointing
     * caret and takes the user back to the Main Screen.
     * The GameHelper object is also initialized in here, as well the Database object. An ArrayList configured to contain
     * objects of type GameData is initialized and will be populated with data from the Database.
     */
    private void init() {
        helper = new GameHelper();
        dba = new GameDB(this);
        dba.open();                                         // Opens the connection to the Database.
        gameDatas = new ArrayList<GameData>();
        getData();

        // Enabling Up / Back navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);     // Allows the user to get back to the Main Screen via the enabled Back button in the Action Bar.

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The file that contains the fonts is a .tff file and
        // and is stored in the 'fonts' folder in the 'assets' folder.

        profile_level = (TextView) findViewById(R.id.profile_level);
        profile_highestscore = (TextView) findViewById(R.id.profile_highestscore);
        profile_gamesplayed = (TextView) findViewById(R.id.profile_gamesplayed);
        profile_avgscore = (TextView) findViewById(R.id.profile_avgscore);
        profile_xp = (TextView) findViewById(R.id.profile_xp);

        profile_levelvalue = (TextView) findViewById(R.id.profile_levelvalue);
        profile_hsvalue = (TextView) findViewById(R.id.profile_hsvalue);
        profile_asvalue = (TextView) findViewById(R.id.profile_asvalue);
        profile_gpvalue = (TextView) findViewById(R.id.profile_gpvalue);
        profile_xpvalue = (TextView) findViewById(R.id.profile_xpvalue);

        profile_level.setTypeface(chalkTypeFace);
        profile_avgscore.setTypeface(chalkTypeFace);
        profile_gamesplayed.setTypeface(chalkTypeFace);
        profile_highestscore.setTypeface(chalkTypeFace);
        profile_xp.setTypeface(chalkTypeFace);
        profile_xpvalue.setTypeface(chalkTypeFace);
        profile_levelvalue.setTypeface(chalkTypeFace);
        profile_hsvalue.setTypeface(chalkTypeFace);
        profile_asvalue.setTypeface(chalkTypeFace);
        profile_gpvalue.setTypeface(chalkTypeFace);

        displayData();
    }

    /**
     * This method, displayData(), checks the size of the gameDatas ArrayList. If it finds that it is empty then no action
     * is taken. If there is at least 1 or more GameData objects in the ArrayList then this method will make use of the
     * GameHelper object to determine the highest and average score and display these values, along with the games played,
     * level and experience points values retrieved from the most recent GameData object in the ArrayList.
     */
    private void displayData() {
        int size = gameDatas.size();
        int[] scoreArray = new int[size];
        int max;
        int avg;

        if(!gameDatas.isEmpty()) {
            for (int i = 0; i < size; i++) {
                scoreArray[i] = gameDatas.get(i).getHighScore();
            }

            max = helper.getMax(scoreArray);
            avg = helper.getAvg(scoreArray);

            GameData latestGame = gameDatas.get(size - 1);

            profile_hsvalue.setText(Integer.toString(max));
            profile_asvalue.setText(Integer.toString(avg));
            profile_gpvalue.setText(Integer.toString(latestGame.getGamesPlayed()));
            profile_levelvalue.setText(Integer.toString(latestGame.getLevel()));
            profile_xpvalue.setText(Integer.toString(latestGame.getxP()));

        }
    }

    /**
     * This method, getData(), uses the GameDB object to query the Database and then recursively work its way through the
     * result set, creating GameData objects with the retrieved data and then adding these objects to the gameDatas ArrayList.
     */
    private void getData() {
        Cursor c = dba.getInfo();
        startManagingCursor(c);
        if(c.moveToFirst()){
            do{
                int gp = c.getInt(c.getColumnIndex(Constants.GAMES_PLAYED));
                int hs = c.getInt(c.getColumnIndex(Constants.HIGH_SCORE));
                int xp = c.getInt(c.getColumnIndex(Constants.XP));
                int lvl = c.getInt(c.getColumnIndex(Constants.LEVEL));

                GameData temp = new GameData(gp,hs,xp,lvl);
                gameDatas.add(temp);
            } while(c.moveToNext());
        }
    }

    /**
     * onOptionsItemSelected is a method that identifies the Menu Item that is selected from the Menu(s) available and
     * and takes the appropriate action upon selection. In this case the user has only one Item to select from the Action
     * Bar and this Menu Item will take them to the Preferences Activity.
     *
     * @param item
     *      The selected Item is passed into the method where its ID is identified and used in a Switch statement to
     *      determine the action to take.
     *
     * @return
     *      The method returns True if the Item ID is matched within in the Switch statement, otherwise the Menu Item is
     *      passed to the Superclass where it is dealt with and the value from that method is returned.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                dba.close();
                gotoOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates an Intent to take the user to the Preferences Activity.
     */
    private void gotoOptions() {
        Intent launchOptions = new Intent(this, Preferences.class);
        startActivity(launchOptions);
    }
}
