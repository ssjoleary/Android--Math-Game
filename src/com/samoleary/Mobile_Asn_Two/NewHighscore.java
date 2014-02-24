package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Typeface;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 3
 * Revision History:
 *      1: 15/11/13
 *      2: 27/11/13
 *      3: 30/11/13
 *
 * Description:
 *      This Activity simply displays a message to the user congratulating them on achieving a new high score. This
 *      Activity can only be accessed when the user taps on the notification they received from the app in the
 *      Notification Manager.
 */

public class NewHighscore extends Activity {
    private TextView title;
    private TextView content;
    private Typeface chalkTypeFace;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newhighscore);

        init();
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);                             // Allows the user to get back to the Main Screen via the enabled Back button in the Action Bar.

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The file that contains the fonts is a .tff file and
                                                                                    // and is stored in the 'fonts' folder in the 'assets' folder.
        title = (TextView) findViewById(R.id.newhighscore_title);
        content = (TextView) findViewById(R.id.newhighscore_content);

        title.setTypeface(chalkTypeFace);
        content.setTypeface(chalkTypeFace);
    }

}
