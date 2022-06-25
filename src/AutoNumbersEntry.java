/**
 * Returns an integer array which consists of 7 lucky numbers between 1 and 35
 * @auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.io.Serializable;
public class AutoNumbersEntry extends NumbersEntry implements Serializable {
    private final int NUMBER_COUNT = 7;
    private final int MAX_NUMBER = 35;
    /**
     * Constructor for AutoNumbersEntry class
     * @param billId The reciept number linked to the auto-generated entry
     * @param memberId The identification number of the member for automated entry
     * @param numbers The 7 lucky numbers automatically generated
     */
    public AutoNumbersEntry(String billId, String memberId, int[] numbers) {
        super(billId, memberId, numbers);
    }
    /**
     *
     * @param seed Controls the randomness in the generation of lucky 
     * @param simpleCompetitions the instance of the simulator, used to check whether the current competition is in testing mode
     * @return tempNumbers An array recording the 7 lucky numbers generated between 1 and 35
     */
    public int[] createNumbers (int seed, SimpleCompetitions simpleCompetitions) {
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        int lastIndex = simpleCompetitions.competitions.size() - 1;
        Competition currentCompetition = simpleCompetitions.competitions.get(lastIndex);
        // the validlist stores all integers from 1 to 35
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        if(currentCompetition.getIsTestingMode()) {
            // the validlist is shuffled
            Collections.shuffle(validList, new Random(seed));
        }
        else if(!currentCompetition.getIsTestingMode()) {
            Collections.shuffle(validList, new Random());
        }
        // the first 7 elements are chosen
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return tempNumbers;
    }
}