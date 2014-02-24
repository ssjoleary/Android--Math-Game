package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

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
 *      This Activity simply displays some basic info about the application. It was created to demonstrate the use of an
 *      ImageButton
 */

public class Info extends Activity {
    private TextView title;
    private TextView content;
    private Typeface chalkTypeFace;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        init();
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);                             // Allows the user to get back to the Main Screen via the enabled Back button in the Action Bar.

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The file that contains the fonts is a .tff file and
                                                                                    // and is stored in the 'fonts' folder in the 'assets' folder.
        title = (TextView) findViewById(R.id.info_title);
        content = (TextView) findViewById(R.id.info_content);

        title.setTypeface(chalkTypeFace);
        content.setTypeface(chalkTypeFace);
    }
}
