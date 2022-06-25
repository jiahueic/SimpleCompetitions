/**
 * Each customer may have multiple winning entries. 
 * If the prize value of these entries are different, select the entry with the highest prize.
 * If the prize value are the same, select the entry with the smallest id.
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.Serializable;
public class LuckyNumbersCompetition extends Competition implements Serializable {
    private final int FIRST_PRIZE = 50000;
    private final int FIRST_PRIZE_MATCHES = 7;
    private final int SECOND_PRIZE = 5000;
    private final int SECOND_PRIZE_MATCHES = 6;
    private final int THIRD_PRIZE = 1000;
    private final int THIRD_PRIZE_MATCHES = 5;
    private final int FOURTH_PRIZE = 500;
    private final int FOURTH_PRIZE_MATCHES = 4;
    private final int FIFTH_PRIZE = 100;
    private final int FIFTH_PRIZE_MATCHES = 3;
    private final int SIXTH_PRIZE = 50;
    private final int SIXTH_PRIZE_MATCHES = 2;
    private final int WINNING_DIGIT_LENGTH_ONE = 1;
    private final int WINNING_DIGIT_LENGTH_TWO = 2;
    private final int INITIAL_MAXINDEX = -1;
    private ArrayList <Entry> entries = new ArrayList<Entry>();

    private ArrayList <NumbersEntry> winningEntries = new ArrayList<NumbersEntry> ();
    private int numWinningEntries;
    private boolean isTestingMode;

    /**
     * Constuctor for LuckyNumbersCompetition
     * @param name Records the name for LuckyNumbersCompetition
     */
    public LuckyNumbersCompetition(String name){
        super(name);
    }

    public void addEntries(Entry newEntry) {
        entries.add(newEntry);
        newEntry.setEntryId(entries.size());
    }

    public ArrayList <Entry> getEntries(){
        return this.entries;
    }

    public int getNumWinningEntries() {
        return this.numWinningEntries;
    }

    public void setNumWinningEntries(int numWinningEntries) {
        this.numWinningEntries = numWinningEntries;
    }

    public int[] printLucky(SimpleCompetitions simpleCompetition) {
        // the system generates a lucky entry that contains 7 lucky numbers
        int [] dummyNumbers = {0};
        // both modes of simpleCompetition need to create an instance of autoNumbersEntry
        AutoNumbersEntry autoNumbersEntry = new AutoNumbersEntry("0", "0", dummyNumbers);
        int[] winningNumbers = new int[7];
        if(super.getIsTestingMode()) {
            winningNumbers = autoNumbersEntry.createNumbers(simpleCompetition.competitions.size(), simpleCompetition);
        }
        else if(!super.getIsTestingMode()) {
            // the seed is a dummy variable here
            winningNumbers = autoNumbersEntry.createNumbers(0, simpleCompetition);
        }

        // print the winning lucky number combination 
        System.out.print("Lucky Numbers:");
        //add space before each number
        // if number has one digit, add two spaces in front
        // if number has two digits, add one space in front
        for(int i = 0; i < winningNumbers.length; i++) {
            String theNumber = String.valueOf(winningNumbers[i]);
            if(theNumber.length() == WINNING_DIGIT_LENGTH_ONE) {
                System.out.print(" ".repeat(WINNING_DIGIT_LENGTH_TWO) + winningNumbers[i]);
            }
            if(theNumber.length() == WINNING_DIGIT_LENGTH_TWO) {
                System.out.print(" ".repeat(WINNING_DIGIT_LENGTH_ONE) + winningNumbers[i]);
            }
        }
        System.out.print(" [Auto]");
        return winningNumbers;
    }


    /**
     * Checks how many matches are there between the members' 7 number entry and the lucky numbers entry
     * The more the matches, the higher the prize
     * Each member can only win one prize (the one with the highest value or with smallest entry id if same prize amount applies)
     * @param simpleCompetition input for printLucky helper method to check if the current competition is in testing mofr
     */
    public void drawWinners(SimpleCompetitions simpleCompetition) {
        int[] winningNumbers = printLucky(simpleCompetition);
        int numberOfMatches;
        int[] numbersArray;

        // go through the entries list to see if there are any matches
        // use downcasting in order to access the method getNumbers()

        NumbersEntry luckyNumberEntry;
        //checking all entries how many matching numbers they have with the winning entry generated
        for(Entry entry: entries) {
            numberOfMatches = 0;
            // luckyNumberEntry is the parameter wanted when accessing the prizes
            luckyNumberEntry = (NumbersEntry)entry;
            numbersArray = luckyNumberEntry.getNumbers();
            //scan winningEntry individual numbers in outer loadExistingCompetition
            // scan the current entry numbers in the inner loop
            for(int i = 0; i < winningNumbers.length; i++) {
                for(int j = 0; j < winningNumbers.length; j++){
                    if(numbersArray[j] == winningNumbers[i]) {
                        numberOfMatches++;
                    }
                }
            }
            // only sets the prize if current prize greater than maximum
            // need to remove the previous winning entry in this case
            // if the maximum iteself is greater than 0
            if(numberOfMatches == FIRST_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, FIRST_PRIZE);
            }

            else if(numberOfMatches == SECOND_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, SECOND_PRIZE);
            }

            else if(numberOfMatches == THIRD_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, THIRD_PRIZE);
            }

            else if(numberOfMatches == FOURTH_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, FOURTH_PRIZE);
            }
            else if (numberOfMatches == FIFTH_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, FIFTH_PRIZE);

            }
            else if (numberOfMatches == SIXTH_PRIZE_MATCHES) {
                decidingAddWinningEntry(luckyNumberEntry, SIXTH_PRIZE);
            }
        }

        sortWinningEntries();
        reportDrawWins(simpleCompetition);
        System.out.println();
        super.setNumWinningEntries(sumTotalWins());
        super.setTotalAwardedPrizes(sumTotalPrizes());
    }

    public void sortWinningEntries() {
        //rearrange the order of the winning based on entry id
        Collections.sort(winningEntries, new Comparator<NumbersEntry>() {
            public int compare(NumbersEntry e1, NumbersEntry e2) {
                if(e1.getEntryId() == e2.getEntryId()) {
                    return 0;
                }
                return e1.getEntryId() < e2.getEntryId() ? -1 : 1;
            }
        });
    }

    // count the total number of prizes awarded
    public int sumTotalWins(){
        int count = 0;
        for(NumbersEntry luckyEntry : winningEntries) {
            count++;
        }
        return count;
    }

    // count the total prize money awarded
    public int sumTotalPrizes() {
        int sum = 0;
        for(NumbersEntry luckyEntry : winningEntries) {
            sum += luckyEntry.getPrize();
        }
        return sum;
    }

    /**
     * Report the lucky number entry and the member information who wins any amount of prizes
     * @param simpleCompetition To retrieve member information from its arraylist in the simulator instance
     */
    public void reportDrawWins(SimpleCompetitions simpleCompetition) {
        String memberName;
        System.out.println();
        System.out.println("Winning entries:");
        int count = 0;
        for(NumbersEntry wins : winningEntries) {
            memberName = searchWinningMember(wins.getMemberId(), simpleCompetition);
            String prizeAmount = String.valueOf(wins.getPrize());
            if(prizeAmount.length() == 2) {
                if(memberName.length() <= 4) {
                    System.out.print("Member ID: " + wins.getMemberId() + ", Member Name: " + memberName + ", Prize: " + wins.getPrize() + " ".repeat(3));
                }
                else if(memberName.length() > 4) {

                    System.out.print("Member ID: " + wins.getMemberId() + ", Member Name: " + memberName + ", Prize: " + wins.getPrize() + " ".repeat(1));
                }

            }
            else{
                System.out.print("Member ID: " + wins.getMemberId() + ", Member Name: " + memberName + ", Prize: " + wins.getPrize());
            }
            if(count == winningEntries.size() - 1) {
                System.out.print("  ");
            }
            System.out.println();
            System.out.print("--> Entry ID: " + wins.getEntryId() + ", Numbers:");
            printWinningNumbers(wins);
            if(wins.getIsAuto() == true) {
                System.out.print(" [Auto]");
            }
            if(count < winningEntries.size() - 1){
                System.out.println();
            }

            count++;
        }
    }

    // helper method for reportDrawWins
    // prints the lucky numbers of any entries that won prizes
    public void printWinningNumbers(NumbersEntry wins) {
        int[] numbers = wins.getNumbers();

        for(int number : numbers) {
            String numString = String.valueOf(number);
            if(numString.length() == WINNING_DIGIT_LENGTH_ONE) {
                System.out.print(" ".repeat(WINNING_DIGIT_LENGTH_TWO) + number);
            }
            else if(numString.length() == WINNING_DIGIT_LENGTH_TWO){
                System.out.print(" ".repeat(WINNING_DIGIT_LENGTH_ONE) + number);
            }
        }
    }

    //returns the name of the winning member
    public String searchWinningMember(String memberId, SimpleCompetitions simpleCompetition) {
        String memberName = null;
        for(Member member: simpleCompetition.members) {
            if(member.getMemberId().equals(memberId)) {
                memberName = member.getMemberName();
            }
        }
        // return a dummy member variable

        return memberName;
    }

    // finds the maximum of member's other prizes 
    // if there are not entries, the maximum is zero
    public int searchMemberOtherPrizes(NumbersEntry luckyNumberEntry) {
        //downcast the inputs in the entries list
        ArrayList <NumbersEntry> otherEntries = new ArrayList<NumbersEntry>();
        NumbersEntry otherEntry;
        for(Entry entry : entries) {
            otherEntry = (NumbersEntry)entry;
            if(luckyNumberEntry.getMemberId().equals(otherEntry.getMemberId())) {
                if(otherEntry.getPrize() > 0) {
                    //save the other winning entries of the member
                    otherEntries.add(otherEntry);
                }
            }
        }
        int maximum = 0;
        // search the maximum of other prizes 
        if(otherEntries.size() > 0) {
            for(NumbersEntry anotherEntry : otherEntries) {
                if(anotherEntry.getPrize() > maximum) {
                    maximum = anotherEntry.getPrize();
                }
            }
        }
        return maximum;
    }
    /** find the index of the maximum other entry given that it is greater than 0
     * in the winningEntries arraylist
     * @param memberId the identification number of the member 
     * @param maxValue the maximum value of prize won previously by the member
     */
    public int findIndexMax(String memberId, int maxValue) {
        int maxIndex = INITIAL_MAXINDEX;
        //keep track of the iterating index
        int count = 0;
        for(NumbersEntry winningEntry : winningEntries) {
            if(winningEntry.getMemberId().equals(memberId) && winningEntry.getPrize() == maxValue) {
                maxIndex = count;
            }
            count++;
        }
        return maxIndex;
    }

    /**
     * Helper method for drawWinners
     * When a particular number of matches is found between the winning lucky number and the lucky number of an entry
     * The entry is checked against other entries belonging to the same Member
     * The maximum prize that the previous owner has won previously is recorded
     * If the current entry has a prize amount greater than the amount the member has previously won
     * The previous entry index and its prize amount are found, and this entry is removed from the winningEntries ArrayList
     * If the member does not have any previous winning entries, findIndexMax will return a value of -1 index (meaning it was not found)
     * Hence, nothing will be removed in thiscase.
     * @param luckyNumberEntry The Entry object with a certain amount of matches with the lucky winning entry
     * @param valueOfPrize The prize value for that particular number of matches with the winning lucky number
     */
    public void decidingAddWinningEntry(NumbersEntry luckyNumberEntry, final int valueOfPrize) {
        int maximum;
        int index;

        maximum = searchMemberOtherPrizes(luckyNumberEntry);

        if(valueOfPrize > maximum) {
            if(maximum > 0) {
                index = findIndexMax(luckyNumberEntry.getMemberId(), maximum);
                if(index > INITIAL_MAXINDEX) {
                    winningEntries.remove(index);
                }

            }
            luckyNumberEntry.setPrize(valueOfPrize);
            winningEntries.add(luckyNumberEntry);
        }

    }

    public String getCompetitionName(){
        return "LuckyNumbersCompetition";
    }
}