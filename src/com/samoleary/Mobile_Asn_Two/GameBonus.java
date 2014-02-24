package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 5
 * Revision History:
 *      1: 15/11/13
 *      2: 16/11/13
 *      3: 20/11/13
 *      4: 27/11/13
 *      5: 30/11/13
 *
 * Description:
 *      This class is the 'Bonus' Activity that starts once the user has answered 10 equations. An image of an apple
 *      and a basket is displayed on the screen, as well as one more equation. The user is required to drag the apple
 *      and drop it into the basket. Once the user is finished they must tap on the button at the end of the screen.
 *      The number of apples dropped into the basket should equal the answer to the equation.
 *      The 'Bonus' Activity is configured to run on the 'Beginner Game' settings so as to not allow the answer to
 *      become too big.
 */
public class GameBonus extends Activity implements View.OnTouchListener, View.OnDragListener {
    private TextView title;
    private TextView dragndrop;
    private ImageView apple;                // This is the image of the apple that will be dragged by the user.
    private ImageView basket;               // This is the image of the basket.
    private TextView equation;
    private Typeface chalkTypeFace;         // The Typeface object changes the font of the text in view to which it is applied to.
    private Button doneBtn;
    private int operator;                   // This variable contains the operator selected by the user.
    private int answer;                     // This variable contains the randomly generated answer.
    private int score;                      // This variable contains the score achieved by the user so far in the game.
    private int totalqs;                    // This variable contains the total number of questions answered by the user so far.
    private int applesDropped;              // The counter for the number of apples dropped into the basket.

    private LinearLayout basketLayout;      // The 'Drag'n'Drop' feature can only work by dragging an Image into a Layout,
                                            // not another Image

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamebonus);

        init();
    }

    /**
     * This initialize method receives and assigns the values for the score, total questions and operator used that passed
     * in from the GameScreen Activity, displays the Views and sets the Typeface for all the text visible in this Activity.
     * As well as this, the appropriate Drag and Touch listeners are applied to the Apple Image and Basket Layout, and a
     * click listener is applied to the 'Done' button. Once all this is completed the beginnerGame() method is called to
     * generate the Answer and Equation.
     */
    private void init() {
        Intent i = getIntent();
        score = i.getIntExtra("score", 0);
        totalqs = i.getIntExtra("totalqs", 0);
        operator = i.getIntExtra("operator", 1);

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");  // The file that contains the fonts is a .tff file and
                                                                                // and is stored in the 'fonts' folder in the 'assets' folder.
        title = (TextView) findViewById(R.id.gamebonus_title);
        dragndrop = (TextView) findViewById(R.id.gamebonus_dragndrop);
        equation = (TextView) findViewById(R.id.gamebonus_equation);
        doneBtn = (Button) findViewById(R.id.gamebonus_btn);

        title.setTypeface(chalkTypeFace);
        dragndrop.setTypeface(chalkTypeFace);
        equation.setTypeface(chalkTypeFace);
        doneBtn.setTypeface(chalkTypeFace);

        apple = (ImageView) findViewById(R.id.imageApple);
        basket = (ImageView) findViewById(R.id.imageBasket);
        basketLayout = (LinearLayout) findViewById(R.id.basketLayout);

        apple.setOnTouchListener(this);
        basketLayout.setOnDragListener(this);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishGame();
            }
        });
        beginnerGame();
    }

    /**
     * This method is called when the user is finished and taps the 'Done' button.
     * The number of apples they've dropped is compared with the actual answer, if they match their score is increased
     * by one, otherwise it stays the same, and the values for their final score and the total number of questions
     * asked is passed to the final Activity.
     */
    private void finishGame() {
        Intent launchGameFinish = new Intent(this, GameFinish.class);
        if(applesDropped == answer){
            score++;
            totalqs++;
            launchGameFinish.putExtra("score", score);
            launchGameFinish.putExtra("totalqs", totalqs);
            startActivity(launchGameFinish);
        }
        else {
            totalqs++;
            launchGameFinish.putExtra("score", score);
            launchGameFinish.putExtra("totalqs", totalqs);
            startActivity(launchGameFinish);
        }
    }

    /**
     * This method is called when the user touches the Apple Image.
     * A very slight shadow is generated behind the Apple as the user drags it across the screen.
     *
     * @param v
     *      Identifies the View that has been touched. In this case it is the Apple Image. Image is a subclass of View.
     *
     * @param e
     *      Describes the Motion that has taken place on the Image.
     *
     * @return
     *      Returns True once the Apple Image has been touched, otherwise its always False.
     */
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is called once the Apple Image that has been dragged over the screen is dropped.
     * If the Apple Image is dropped over the Basket Layout the applesDropped counter is increased.
     *
     * @param v
     *      Identifies the View that the Apple Image has been dropped on. In this case it is the Basket Layout.
     *      Images can only be dropped onto Layouts and not other Images. The Basket Layout wraps the Basket Image
     *      so it has essentially become the Image.
     *
     * @param e
     *      Describes the Event that has taken place on the Layout.
     *
     * @return
     *      This method always returns True, not necessarily for all Views but only for Views that are set up
     *      to listen for such Events.
     */
    public boolean onDrag(View v, DragEvent e) {
        if (e.getAction()==DragEvent.ACTION_DROP) {
            v.setVisibility(View.VISIBLE);
            applesDropped++;
        }
        return true;
    }

    /**
     * This method, beginnerGame, generates a random number which becomes the answer for the equation.
     * Then two more numbers are generated around the answer to form the equation.
     * The range for the answer is between 0 and 20, depending on the chosen operator two more numbers are
     * generated randomly to form an equation that will equal the answer.
     * For example:
     *      Answer generated: 13
     *      Operator: +
     *      A number is then generated between the range of 0 and 5, the answer. Lets say this number is 3.
     *      This number is now subtracted from the answer to create the second value in the equation, 13 - 3 = 10.
     *      Thus the equation becomes 3 + 13 with the answer being 13.
     *
     * This is similar to when the operator is '-'. In this case a number is generated between the answer and
     * the maximum value, 20. This becomes the first value in the equation. To create the second value the answer
     * is subtracted from it.
     * For example:
     *      Answer generated: 13
     *      Operator: -
     *      First number generated (between 20 and 13): 17
     *      Second number: 4 (17 - 13)
     *      Equation: 17 - 4
     */
    private void beginnerGame() {
        int min = 0;
        int max = 20;
        int intEq1;
        int intEq2;

        Random r = new Random();
        answer = r.nextInt(max - min + 1) + min;

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
}
