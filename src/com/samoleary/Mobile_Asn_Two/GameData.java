package com.samoleary.Mobile_Asn_Two;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 28/11/13
 * Revision: 3
 * Revision History:
 *      1: 28/11/13
 *      2: 29/11/13
 *      3: 30/11/13
 *
 * Description:
 *      This class is used to store the information that is retrieved from the SQLite Database. This data can then be
 *      used and manipulated through the 'getter' methods within this class.
 */

public class GameData {
    private int gamesPlayed;        // The number of games the user has played so far.
    private int highScore;          // The highest score the user has achieved whilst playing so far.
    private int xP;                 // The total experience points the user has earned so far.
    private int level;              // The level of the experience the user has achieved so far.
                                    // The level of a user increases every 100 experience points.

    public GameData(int gp, int hs, int xp, int lvl) {
        gamesPlayed = gp;
        highScore = hs;
        xP = xp;
        level = lvl;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getxP() {
        return xP;
    }

    public int getLevel() {
        return level;
    }
}
