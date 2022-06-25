/**
 * Generates the winning entry for the random competition, which is the lucky entry number. 
 * Testing mode uses competition id as seed while testing mode is completely random
 * Reports the winning member's information and their prizes
 * Count the total number of prizes awarded and the total amount of award
 * @param name Stores the name of RandomPickCompetition
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
public class RandomPickCompetition extends Competition implements Serializable{
    private final int FIRST_PRIZE = 50000;
    private final int SECOND_PRIZE = 5000;
    private final int THIRD_PRIZE = 1000;
    private final int MAX_WINNING_ENTRIES = 3;
    private final int PRINT_PRIZE_VALUE_LENGTH = 5;
    private final int[] prizes = {FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE};
    private ArrayList<Entry> winningEntries = new ArrayList<Entry>();
    private ArrayList <Entry> entries = new ArrayList<Entry>();
    private int numWinningEntries;

    public RandomPickCompetition(String name) {
        super(name);

    }

    public String getCompetitionName(){
        return "RandomPickCompetition";
    }

    public void addEntries(Entry newEntry) {
        entries.add(newEntry);
        newEntry.setEntryId(entries.size());
    }

    public ArrayList <Entry> getEntries(){
        return this.entries;
    }

    /**
     * Checks which member the winning entry belongs to
     * Checks whether other entries of the member have prizes
     * @param winningEntry The entry that has entry id matching with the winning entry id generated
     */
    public boolean checkMemberOtherPrizes(Entry winningEntry) {
        String memberId = winningEntry.getMemberId();
        for(Entry entry : entries) {
            if(entry.getMemberId().equals(memberId)) {
                if(entry.getPrize() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * If the competition is in testing mode, generate three random entries based on the competition id
     * If the competition is not in testing mode, the winning entries are completely random
     * The winning entries are sorted by winning entries id and printed
     * @param simpleCompetition to check whether the current competition is in testing mode
     */

    public void drawWinners(SimpleCompetitions simpleCompetition) {
        int lastIndex = simpleCompetition.competitions.size() - 1;
        Competition currentCompetition = simpleCompetition.competitions.get(lastIndex);
        Random randomGenerator = null;
        if (currentCompetition.getIsTestingMode()) {
            randomGenerator = new Random(this.getId());
        } else {
            randomGenerator = new Random();
        }
        // winningEntryCount to iterate through the list of prizes
        int winningEntryCount = 0;

        while (winningEntryCount < MAX_WINNING_ENTRIES) {
            // generates three random numbers 
            int winningEntryIndex = randomGenerator.nextInt(entries.size());
            // checks the entry which has the winning entry index
            Entry winningEntry = entries.get(winningEntryIndex);
            /*
             * Ensure that once an entry has been selected,
             * it will not be selected again.
             */
            if (winningEntry.getPrize() == 0) {
                int currentPrize = prizes[winningEntryCount];
                winningEntryCount++;
                // only award the prize if the member does not have other winning entries
                if(!checkMemberOtherPrizes(winningEntry)) {

                    winningEntry.setPrize(currentPrize);
                    winningEntries.add(winningEntry);
                }
            }
        }
        sortWinningEntries();
        printWinningEntries(simpleCompetition);
        super.setNumWinningEntries(winningEntries.size());
        super.setTotalAwardedPrizes(findTotalAwardedPrize());
    }

    // sort the winning entries based on entry id
    public void sortWinningEntries() {
        // Use a comparator to sort an arraylist of object based on an attribute
        Collections.sort(winningEntries, new Comparator<Entry>() {
            public int compare(Entry e1, Entry e2){
                if(e1.getEntryId() == e2.getEntryId()) {
                    return 0;
                }
                return e1.getEntryId() < e2.getEntryId() ? -1 : 1;
            }
        });
    }

    // helper method for drawWinners, print the winningEntries after sorting by entryid
    public void printWinningEntries(SimpleCompetitions simpleCompetition) {
        System.out.println("Winning entries:");
        for(Entry theEntry : winningEntries) {
            String memberName = findMemberNameWithID(simpleCompetition, theEntry.getMemberId());
            String prizeValue = String.valueOf(theEntry.getPrize());
            if(prizeValue.length() < PRINT_PRIZE_VALUE_LENGTH) {
                int numSpaces = PRINT_PRIZE_VALUE_LENGTH - prizeValue.length();
                System.out.println("Member ID: " + theEntry.getMemberId() + ", Member Name: " + memberName + ", Entry ID: " + theEntry.getEntryId() + ", Prize: " + theEntry.getPrize()+" ".repeat(numSpaces));
            }
            else{
                System.out.println("Member ID: " + theEntry.getMemberId() + ", Member Name: " + memberName + ", Entry ID: " + theEntry.getEntryId() + ", Prize: " + theEntry.getPrize());
            }

        }
    }


    public int findTotalAwardedPrize() {
        int sum = 0;
        for(Entry win : winningEntries) {
            sum += win.getPrize();
        }
        return sum;
    }
    public String findMemberNameWithID(SimpleCompetitions simpleCompetitions, String winningMemberId) {
        String memberName = null;
        for(Member member : simpleCompetitions.members) {
            if(member.getMemberId().equals(winningMemberId)) {
                memberName = member.getMemberName();
            }
        }
        return memberName;
    }
}