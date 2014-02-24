package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 6
 * Revision History:
 *      1:  15/11/13
 *      2:  16/11/13
 *      3:  20/11/13
 *      4:  27/11/13
 *      5:  28/11/13
 *      6:  30/11/13
 *
 * Description:
 *      This Activity randomly generates equations and answers according to the difficulty and operator selected
 *      and presents them to the user. The user then taps the correct answer among a list of four buttons. Once ten
 *      questions have been answered the user is directed to the Bonus Game Activity. A Progress Bar at the bottom of
 *      the screen shows the user how far into this current game the user has progressed so far.
 */

public class GameScreen extends Activity {
    private int operator;                   // The operator selected at the Game Config Activity.
    private int difficulty;                 // The difficulty selected at the Game Config Activity.

    private int positionOfAns;              // The position of the answer among the buttons, compared against the users selection.
    private int score = 0;                  // Current score
    private int turns = 0;                  // Current turn
    private int maxTurns = 10;              // Maximum number of turns per game

    private TextView equation;
    private ArrayList<Button> ansBtns;          // Placing the buttons in an ArrayList makes them easier to
    private static final int[] BUTTON_IDS = {   // deal with as the same operation is carried out on all of them.
            R.id.gamescreen_ansone,             // The same goes for the Array of Button Ids, a For loop can be used to
            R.id.gamescreen_anstwo,             // to find all the Buttons and add them to the ArrayList.
            R.id.gamescreen_ansthree,
            R.id.gamescreen_ansfour
    };

    private Typeface chalkTypeFace;             // The Typeface object changes the font of the text in view to which it is applied to.

    // The progress bar runs as a separate Thread alongside the Main Thread and is updated by 10% each time a question
    // is answered. updateGameProgBar is the Thread and myHandler is used to pass the updated percent_done value to this
    // Thread. The Progress Bar is then updated within this Thread.
    private static ProgressBar gameProgBar;
    int percent_done = 0;
    final Handler myHandler = new Handler();
    final Runnable updateGameProgBar = new Runnable() {
        public void run() {
            gameProgBar.setProgress(percent_done);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamescreen);

        init();
    }

    /**
     * This initialize method starts by reading the values for the operator and difficulty that are passed in from the
     * Game Config Activity. The Typeface is then configured. Each Button is then found by their Id, configured with a Typeface
     * and added to the ArrayList. The ArrayList is then iterated through again and an onClickListener is applied to each Button.
     * When the answer is generated and applied to a Button its position among the Buttons is saved and is compared to the
     * position of the Button that the user selects. When the user selects a Button, if its the correct Button then the
     * users score increases by 1, the turns variable is increased by 1 and the Progress Bar is updated. Once the turns
     * variable reaches 10 the user is directed to the Games Bonus Activity.
     */
    private void init() {
        Intent infoGameConfig = getIntent();
        operator = infoGameConfig.getIntExtra("operator", 0);
        difficulty = infoGameConfig.getIntExtra("difficulty", 0);

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");

        equation = (TextView) findViewById(R.id.gamescreen_equation);
        equation.setTypeface(chalkTypeFace);

        ansBtns = new ArrayList<Button>();

        for(int id : BUTTON_IDS) {
            Button ansBtn = (Button) findViewById(id);
            ansBtn.setTypeface(chalkTypeFace);
            ansBtns.add(ansBtn);
        }

        for(final Button btn : ansBtns) {
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ansBtns.get(positionOfAns) == btn) {
                        score++;
                        beginGame();
                    } else {
                        beginGame();
                    }
                    turns++;
                    percent_done += 10;
                    myHandler.post(updateGameProgBar);
                    if(turns == maxTurns){
                        gotoBonusGame();
                    }
                }
            });
        }

        gameProgBar = (ProgressBar) findViewById(R.id.gamescreen_progbar);
        beginGame();

    }

    /**
     * This method, gotoBonusGame(), is called once the user taps one of the answer buttons for the tenth time.
     * The values for the 'operator', 'score' and 'totalqs' variables are taken and passed into the next Activity
     * where they are used in the generation of the last equation and answer before being passed to the GameFinish Activity.
     */
    private void gotoBonusGame() {
        Intent launchBonus = new Intent(this, GameBonus.class);
        launchBonus.putExtra("operator", operator);
        launchBonus.putExtra("score", score);
        launchBonus.putExtra("totalqs", maxTurns);
        startActivity(launchBonus);
    }

    /**
     * This method, beginGame(), starts be determining the range that the random numbers are to be generated in. This range
     * is determined by the difficulty selected by the user. Once the range is set one random number is generated, this number
     * is the answer. This number and the range from which is was generated in is sent to the setAnswers() method to generate
     * the incorrect answers and set the position of the answer among the buttons.
     * Using the randomly generated answer an equation is formed using 2 more randomly generated numbers based on the first.
     * For example:
     *      Answer generated: 13
     *      Operator: +
     *      A number is then generated between the range of 0 and the answer 13. Lets say this number is 3.
     *      This number is now subtracted from the answer to create the second value in the equation, 13 - 3 = 10.
     *      Thus the equation becomes 3 + 13 with the answer being 13.
     *
     * This is similar to when the operator is '-'. In this case a number is generated between the answer and
     * the maximum value 20. This becomes the first value in the equation. To create the second value the answer
     * is subtracted from it.
     * For example:
     *      Answer generated: 13
     *      Operator: -
     *      First number generated (between 20 and 13): 17
     *      Second number: 4 (17 - 13)
     *      Equation: 17 - 4
     */
    private void beginGame() {
        int min = 0;
        int max = 20;
        int intEq1;
        int intEq2;

        switch (difficulty) {
            case 0: min = 0; max = 20;
                break;
            case 1: min = 20; max = 120;
                break;
            case 2: min = 120; max = 250;
                break;
        }

        Random r = new Random();
        int answer = r.nextInt(max - min + 1) + min;

        setAnswers(max, min, answer);

        switch (operator) {
            case 0: intEq1 = r.nextInt(answer - min + 1) + min;
                intEq2 = answer - intEq1;
                equation.setText(intEq1 + " + " + intEq2);
                break;
            case 1: intEq1 = r.nextInt(max - answer + 1) + answer;
                intEq2 = intEq1 - answer;
                equation.setText(intEq1 + " - " + intEq2);
                break;
        }

    }

    /**
     * This method, setAnswers(), uses a range bounded by the max and min values and the value of the answer to generate
     * three more incorrect answers. A position among the Buttons is randomly generated also and the answer takes this
     * position, the three incorrect answers take up the remaining buttons in no particular order. A method, getRandomWithExclusion(),
     * attempts to minimize the chances of a value being generated for an incorrect answer being the same as the actual
     * answer.
     *
     * @param max
     *      The maximum value in the range of randomly generated numbers.
     *
     * @param min
     *      The minimum value in the range of randomly generated numbers.
     *
     * @param answer
     *      The value of the answer to the equation.
     */
    private void setAnswers(int max, int min, int answer) {

        Random r = new Random();
        positionOfAns = r.nextInt(3 - 0 + 1) + 0;
        int[] excluded = new int[4];
        excluded[0] = answer;

        int fakeAnsOne = getRandomWithExclusion(r,min,max,excluded);
        excluded[1] = fakeAnsOne;
        int fakeAnsTwo = getRandomWithExclusion(r,min,max,excluded);
        excluded[2] = fakeAnsTwo;
        int fakeAnsThree = getRandomWithExclusion(r,min,max,excluded);
        excluded[3] = fakeAnsThree;

        ansBtns.get(positionOfAns).setText(""+answer);

        if(positionOfAns == 0)  {
            ansBtns.get(1).setText(""+fakeAnsOne);
            ansBtns.get(2).setText(""+fakeAnsTwo);
            ansBtns.get(3).setText(""+fakeAnsThree);
        } else if (positionOfAns == 1) {
            ansBtns.get(0).setText(""+fakeAnsOne);
            ansBtns.get(2).setText(""+fakeAnsTwo);
            ansBtns.get(3).setText(""+fakeAnsThree);
        } else if (positionOfAns == 2) {
            ansBtns.get(0).setText(""+fakeAnsOne);
            ansBtns.get(1).setText(""+fakeAnsTwo);
            ansBtns.get(3).setText(""+fakeAnsThree);
        } else if (positionOfAns == 3) {
            ansBtns.get(0).setText(""+fakeAnsOne);
            ansBtns.get(1).setText(""+fakeAnsTwo);
            ansBtns.get(2).setText(""+fakeAnsThree);
        }
    }

    /**
     * This method attempts to reduce the chances of a random number being generated that is equal to the answer that was
     * generated. It generates a number from a specified range but excluding one number. For example, if the answer 13 and
     * the range is 0 to 20 then an attempt is made to generate any other number in this range but 13.
     *
     * @param rnd
     *      The Random object used to generate random numbers.
     *
     * @param start
     *      The start of the range of numbers, either 0, 20, 120.
     *
     * @param end
     *      The end of the range of numbers, either 20, 120, 250.
     *
     * @param exclude
     *      The value to be excluded from the range so that it won't be randomly generated.
     *
     * @return
     *      The randomly generated number is returned.
     */
    private int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    /**
     * onCreateOptionsMenu creates the Menu that is displayed when the user presses the physical Menu Button. The
     * menu items and their properties, such as the id and icon, are described in the XML file 'game_options_menu'.
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
        inflater.inflate(R.menu.game_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
        switch(item.getItemId()) {
            case R.id.optionone:
                gotoCalculator();
                return true;
            case R.id.optiontwo:
                gotoUserProfile();
                return true;
            case R.id.optionthree:
                gotoOptions();
                return true;
            case R.id.optionfour:
                gotoMainMenu();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates an Intent to take the user to the Web Browser to show them www.calculator.com.
     */
    private void gotoCalculator() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.calculator.com/")));
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
