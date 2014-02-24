package com.samoleary.Mobile_Asn_Two;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 4
 * Revision History:
 *      1: 15/11/13
 *      2: 16/11/13
 *      3: 27/11/13
 *      4: 30/11/13
 *
 * Description:
 *      This Activity gives the user two options they can change to modify their game, the difficulty and the operator
 *      used. The Difficulty Spinner has three options, Beginner, Intermediate and Professional. The Operators Spinner
 *      has two options, Addition and Subtraction. As the difficulty increases the range of numbers in which the answers
 *      and values for the equations are generated gets larger.
 */
public class GameConfig extends Activity implements AdapterView.OnItemSelectedListener {
    private int diff;
    private int op;
    private Typeface chalkTypeFace;

    private TextView difficultyTV;
    private TextView operatorsTV;
    private Button goBtn;

    private Spinner difficultySpin;
    private Spinner operatorSpin;
    private ArrayAdapter<CharSequence> diffSpinAdapter;
    private ArrayAdapter<CharSequence> opSpinAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameconfig);

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
     * This initialize method enables the Action Bar, displays all the Views and applies the Typeface to them.
     * The Spinners and their Adapters are then configured and the Adapters are applied to the Spinners.
     */
    private void init() {
        // get action bar
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The file that contains the fonts is a .tff file and
                                                                                    // and is stored in the 'fonts' folder in the 'assets' folder.
        difficultyTV = (TextView) findViewById(R.id.gameconfig_difficulty);
        operatorsTV = (TextView) findViewById(R.id.gameconfig_operators);
        goBtn = (Button) findViewById(R.id.gameconfig_gobtn);

        goBtn.setTypeface(chalkTypeFace);
        difficultyTV.setTypeface(chalkTypeFace);
        operatorsTV.setTypeface(chalkTypeFace);

        // Displays the Difficulty and Operator Spinners.
        difficultySpin = (Spinner) findViewById(R.id.gameconfig_difficultyspin);
        operatorSpin = (Spinner) findViewById(R.id.gameconfig_operatorspin);

        // Attaches a Listener to each Spinner to listen for when the user selects an item from them.
        difficultySpin.setOnItemSelectedListener(this);
        operatorSpin.setOnItemSelectedListener(this);

        // The following lines of code describe the look and feel of the Spinners. First the Array that contains the Spinner Items is located
        // and is used to populate the Spinner. How each Item looks within the Spinner is specified next, followed by how the Spinner is laid
        // out when the user taps it.
        diffSpinAdapter = ArrayAdapter.createFromResource(this, R.array.gameconfig_difficultyspin, android.R.layout.simple_spinner_item);
        diffSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opSpinAdapter = ArrayAdapter.createFromResource(this, R.array.gameconfig_operatorspin, android.R.layout.simple_spinner_item);
        opSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Once the layout of the Spinners is figured out they are applied to the spinner objects themselves.
        difficultySpin.setAdapter(diffSpinAdapter);
        operatorSpin.setAdapter(opSpinAdapter);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

    }

    /**
     * This method, startGame(), is called once the user taps the 'Go' button. The values for the 'difficulty' and
     * 'operator' variables are taken and passed into the next Activity where they are used in the generation of the
     * equations and answers.
     */
    private void startGame() {
        Intent launchGame = new Intent(this, GameScreen.class);
        launchGame.putExtra("difficulty", diff);
        launchGame.putExtra("operator", op);
        startActivity(launchGame);
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
     * Creates an Intent to take the user to the Preferences Activity.
     */
    private void gotoOptions() {
        Intent launchOptions = new Intent(this, Preferences.class);
        startActivity(launchOptions);
    }

    /**
     * Creates an Intent to take the user to the Profile Activity.
     */
    private void gotoUserProfile() {
        Intent launchUserProfile = new Intent(this, Profile.class);
        startActivity(launchUserProfile);
    }

    /**
     * This method, onItemSelected(), is used to determine the position of the Item selected by the user and in which
     * Spinner the item was selected in. The position of the Item in the Spinner is used to identify the value, for
     * example, in the Difficulty Spinner an Item position of 0 indicates that the Beginner difficulty has been selected.
     *
     * @param parent
     *       Identifies the Spinner object.
     * @param view
     *
     * @param position
     *      The position of the selected Item in the Spinner.
     *
     * @param id
     *
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.gameconfig_difficultyspin) {
            diff = position;
        } else if(parent.getId() == R.id.gameconfig_operatorspin) {
            op = position;
        }
    }

    /**
     * As this Activity implements the AdapterView.onItemSelectedListener Interface all the methods specified within
     * the interface need to be specified here also, regardless of whether this class actually needs or implements the
     * method.
     *
     * @param parent
     *      Identifies the Spinner object.
     */
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
