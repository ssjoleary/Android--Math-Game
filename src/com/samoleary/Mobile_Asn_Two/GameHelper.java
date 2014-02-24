package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.graphics.Typeface;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 30/11/13
 * Revision: 1
 * Revision History:
 *      1:  30/11/13
 *
 * Description:
 *      More than one class makes use of the methods below. This class is intended to reduce the repetition of code and
 *      encourage reusability.
 */
public class GameHelper extends Activity{

    public void GameHelper (){

    }

    /**
     * This method, getAvg(), takes a set of numbers and works out the average of them.
     *
     * @param scoreArray
     *      An Array of numbers of which the average is to be found.
     *
     * @return
     *      Once the average number has been found it is returned.
     */
    public int getAvg(int[] scoreArray) {
        double total = 0;
        double length = scoreArray.length;
        double avg;
        int intAvg;
        for(int i = 0; i < length; i++) {
            total += scoreArray[i];         // Add up all the numbers in the array to get a total.
        }
        avg = total / scoreArray.length;    // Divide this total by the amount of numbers in the array.
        intAvg = (int) Math.round(avg);     // Round the result to the nearest whole number.
        return intAvg;
    }

    /**
     * This method, getMax(), takes a set of numbers and works out which is the largest number.
     *
     * @param scoreArray
     *      An Array of numbers of which the largest number is be found.
     *
     * @return
     *      Once the largest number is found it is returned.
     */
    public int getMax(int[] scoreArray) {
        int length = scoreArray.length;
        int largest = scoreArray[0];        // Start by assuming the first number in the array is the largest.

        for(int i = 0; i < length; i++) {   // Then compare it to the next number in the array
            if(scoreArray[i] > largest){    // Is this number bigger than the next number in the array?
                largest = scoreArray[i];    // If not then the next number in the array is larger and is now the largest.
            }                               // If so then the largest number stays the same.
        }                                   // Either way, run through the loop again until the arrays finished.
        return largest;
    }

}
