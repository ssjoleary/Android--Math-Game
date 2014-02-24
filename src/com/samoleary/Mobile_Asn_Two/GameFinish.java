package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 3
 * Revision History:
 *      1: 15/11/13
 *      2: 27/11/13
 *      3: 29/11/13
 *
 * Description:
 *      This Activity is called once the user completes the bonus game and finishes their overall game.
 *      If the user manages to answer over 40% of the questions correctly they will have passed the game and a congratulations
 *      message is displayed, along with the number of questions they got right and the experience points they have earned.
 *      If they get less than 40% then a condolences message is displayed, along with the number of questions they got right
 *      but no experience points will have been earned.
 *      The user can navigate away from this Activity via the Action Bar or the Main Menu button.
 *      The results of their game are stored in the database once this activity starts.
 *      If the user gets a score that is higher than any score they've previously gotten then they will receive a notification
 *      congratulating them.
 */

public class GameFinish extends Activity {
    private NotificationManager mNManager;
    private static final int NOTIFY_ID = 1100;

    private GameDB dba;                     // The Database object used to read/write to the SQLite Database.
    private ArrayList<GameData> gameDatas;  // This ArrayList will hold GameData objects. These objects contain data about the user
    // that are stored in a Database.
    private int size;                       // This variable holds the size of the ArrayList.
    private GameData latestGame;            // GameData object that holds the information from the previous game.

    private int resultPercent;              // The percentage, or grade, the user achieved in their game.

    private TextView heading;
    private TextView msg;
    private TextView qsRight;
    private TextView xpEarned;

    private int score;
    private int totalqs;
    private Typeface chalkTypeFace;         // The Typeface object changes the font of the text in view to which it is applied to.

    private GameHelper helper;              // This object contains 2 methods that calculate the largest number in a series of numbers
    // and the average of a series of numbers.

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamefinish);

        init();
    }

    /**
     * onCreateOptionsMenu allows menu items to be placed in the Action Bar that runs along the top of the screen. The
     * menu items and their properties, such as the id and icon, are described in the XML file 'main_action_menu'.
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
        inflater.inflate(R.menu.main_action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This initialize method sets up the Views to be displayed to the user, as well as configuring a font for the Typeface
     * object to use and applying this Typeface to all the Views with text in them.
     * The Notification Service and Notifcation Manager are initialized here.
     * The GameHelper object is also initialized in here, as well the Database object. An ArrayList configured to contain
     * objects of type GameData is initialized and will be populated with data from the Database.
     */
    private void init() {
        helper = new GameHelper();

        // Initializing the Notification Manager
        String ns = Context.NOTIFICATION_SERVICE;
        mNManager = (NotificationManager) getSystemService(ns);
        final Notification msgNotif = new Notification(R.drawable.apple,
                String.format(getResources().getString(R.string.gamefinish_notifmsg)),
                System.currentTimeMillis());   // Sets the message displayed at the top of the screen when the notification comes in.

        // Initializing the Database object and opening the connection to the Database.
        dba = new GameDB(this);
        dba.open();

        gameDatas = new ArrayList<GameData>();

        // Retrieving information from the previous Activity.
        Intent infoFromGameBonus = getIntent();
        score = infoFromGameBonus.getIntExtra("score", 0);
        totalqs = infoFromGameBonus.getIntExtra("totalqs", 0);

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The Typeface object changes the font of the text in view to which it is applied to.

        heading = (TextView) findViewById(R.id.gamefinish_heading);
        msg = (TextView) findViewById(R.id.gamefinish_msg);
        qsRight = (TextView) findViewById(R.id.gamefinish_qsright);
        xpEarned = (TextView) findViewById(R.id.gamefinish_xpearned);
        Button mainMenu = (Button) findViewById(R.id.gamefinish_btn);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gotoMainMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        heading.setTypeface(chalkTypeFace);
        msg.setTypeface(chalkTypeFace);
        qsRight.setTypeface(chalkTypeFace);
        xpEarned.setTypeface(chalkTypeFace);
        mainMenu.setTypeface(chalkTypeFace);

        // Sets the Views to display the data from the current game.
        setViews();

        // Determines if the user has set a highscore and, if so, sets off a notification.
        getData(msgNotif);
    }

    /**
     * Saves the data from the current game to the Database.
     * If the size of the ArrayList that contains data previously read out from the Database is zero, i.e the Database
     * is empty then this method determines if the user gained enough experience points to gain their first level then
     * enters the info into the Database.
     * If the ArrayList does contain previous data then steps are taken to update some of the data, such as total
     * experience points, games played and current level before being saved to the Database.
     */
    private void saveToDB() {
        size = gameDatas.size();

        if(size == 0) {
            if(resultPercent == 100) {                      // The user has gained 100 experience points in their first
                dba.insertInfo(1, score, resultPercent, 1); // game and has gained a level.
                dba.close();
            } else {
                dba.insertInfo(0, score, resultPercent, 1);
                dba.close();
            }
        } else {
            latestGame = gameDatas.get(size - 1);           // Get the latest GameData object.
            int newXP = latestGame.getxP() + resultPercent;
            int newLevel = calculateLevel();
            int gamesPlayed = latestGame.getLevel();
            gamesPlayed++;

            dba.insertInfo(newLevel, score, newXP, gamesPlayed);
            dba.close();
        }
    }

    /**
     * This method, calculateLevel(), determines if a user has gained a level based on their current experience points
     * and experience points they've just earned. Every 100 points a user gains a level.
     *
     * @return
     */
    private int calculateLevel() {
        size = gameDatas.size();

        int newLevel;

        if(!(size == 0)) {                          // If the ArrayList isn't empty then a game has previously taken place
            latestGame = gameDatas.get(size - 1);   // and we think the user might have some experience or a level.

            int currentLvl = latestGame.getGamesPlayed();
            int currentXP = latestGame.getxP();

            if(currentLvl == 0) {                   // If the user hasn't gained a level but has played previously
                if (resultPercent + currentXP >= 100) { // Add their current experience to the experience they've just earned
                    newLevel = currentLvl + 1;          // If the result is over 100 they level up.
                    return newLevel;
                } else {                                // If the results is below 100 they don't level up yet.
                    newLevel = 0;
                    return newLevel;
                }
            }
            else {                                 // If the user is level 1 or above
                int excessXP = currentXP - (currentLvl * 100);  // Find out how much experience they have in their current level
                if(resultPercent + excessXP >= 100) {           // Add this experience to the experience they've just earned
                    newLevel = currentLvl + 1;                  // If the result is over 100 they level up.
                    return newLevel;
                } else {                                        // If the results is below 100 they don't level up yet.
                    return currentLvl;
                }
            }
        } else {                                    // This is the users first game, they have no experience or levels.
            if(resultPercent == 100) {              // If they get 100 in their first game they gain their first level.
                newLevel = 1;
                return newLevel;
            } else {
                newLevel = 0;                       // If not, then they stay at level 0.
                return newLevel;
            }
        }
    }

    /**
     * This method, getData(), attempts to retrieve data from the Database to populate the gameDatas ArrayList.
     * If data is retrieved from the database then a quick comparison is made between the highest score achieved so far
     * and the score just achieved. If the score that has just been achieved is greater than the highest score to date then
     * the user receives a notification.
     *
     * @param msgNotif
     *      This is an object of type Notification that is passed to the giveNotificaiton() method if a highscore has been
     *      achieved.
     */
    private void getData(Notification msgNotif) {
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

        if(!gameDatas.isEmpty()) {
            size = gameDatas.size();
            int[] scoreArray = new int[size];
            int max;
            for (int i = 0; i < size; i++) {
                scoreArray[i] = gameDatas.get(i).getHighScore();
            }

            max = helper.getMax(scoreArray);

            if(score > max){
                giveNotification(msgNotif);
            }
        }
        saveToDB();
    }

    /**
     * This method, giveNotification(), notifies the user if they have achieved a new high score. When the user checks the
     * Notification Manager and taps on the notification they are directed to a Pending Activity that displays a couple
     * of Text Views congratulating the user.
     *
     * @param msgNotif
     *      This is the notification object. Once passed in to this method a title, some text, a sound and a pending
     *      activity are assigned to this object.
     */
    private void giveNotification(Notification msgNotif) {
        Context context = getApplicationContext();
        CharSequence contentTitle = String.format(getResources().getString(R.string.gamefinish_notiftitle));
        CharSequence contentText = String.format(getResources().getString(R.string.gamefinish_notiftext));
        Intent msgIntent = new Intent(this, NewHighscore.class);
        PendingIntent intent = PendingIntent.getActivity(GameFinish.this, 0, msgIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        msgNotif.defaults |= Notification.DEFAULT_SOUND;
        msgNotif.flags |= Notification.FLAG_AUTO_CANCEL;

        msgNotif.setLatestEventInfo(context, contentTitle, contentText, intent);
        mNManager.notify(NOTIFY_ID, msgNotif);
    }

    /**
     * This method, setViews(), determines what message to display to the user by doing a quick calculation on the score
     * they've just received. If the score is less than 40 then they've failed and an appropriate message is displayed.
     * If they've score more than 40 then a different message is displayed.
     */
    private void setViews() {
        double result = (score / (double) totalqs) * 100;
        resultPercent = (int) Math.round(result);

        String resultText = String.format(getResources().getString(R.string.gamefinish_qsright), score, totalqs);
        qsRight.setText(resultText);

        if(resultPercent < 40) {
            heading.setText(String.format(getResources().getString(R.string.gamefinish_headingfail)));
            msg.setText(String.format(getResources().getString(R.string.gamefinish_msgfail)));
            int xp = 0;
            xpEarned.setText(String.format(getResources().getString(R.string.gamefinish_xpearned), xp));
        } else {
            heading.setText(String.format(getResources().getString(R.string.gamefinish_headingpass)));
            msg.setText(String.format(getResources().getString(R.string.gamefinish_msgpass)));
            xpEarned.setText(String.format(getResources().getString(R.string.gamefinish_xpearned), resultPercent));
        }
    }

    /**
     * onOptionsItemSelected is a method that identifies the Menu Item that is selected from the Menu(s) available and
     * and takes the appropriate action upon selection. In this case two Items are available to select from the Action
     * Bar.
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
            case R.id.action_profile:
                gotoUserProfile();
                return true;
            case R.id.action_settings:
                gotoOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates an Intent to take the user to the Profile Activity.
     */
    private void gotoUserProfile() {
        Intent launchUserProfile = new Intent(this, Profile.class);
        startActivity(launchUserProfile);
    }

    /**
     * Creates an Intent to take the user to the Preferences Activity.
     */
    private void gotoOptions() {
        Intent launchOptions = new Intent(this, Preferences.class);
        startActivity(launchOptions);
    }

    /**
     * Creates an Intent to take the user to the Main Screen Activity.
     */
    private void gotoMainMenu() {
        Intent launchMainMenu = new Intent(this, MainScreen.class);
        startActivity(launchMainMenu);
    }
}
