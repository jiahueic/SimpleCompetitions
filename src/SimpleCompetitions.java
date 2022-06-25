/**
 * Provides an application interface for staff to create new RandomPick or LuckyNumbers competition.
 * Allows staff to enter the members in the competitions if they fulfill the requirements
 * Draw the winners and record the total cash pool funded 
 * Report how many competitions have been held and their status respectively
 * @ auth: Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
public class SimpleCompetitions {
    private final static int MINIMUM_COMPETITION_AMOUNT = 50;
    private final static int ONE = 1;
    private final static int FIVE = 5;
    private final static int REQUIRED_BILLID_LENGTH = 6;
    // competitions and members are accessed frequently to decide on prizes and member's information
    // for LuckyNumbersCompetition and RandomPickCompetition
    ArrayList <Competition> competitions = new ArrayList<Competition>();
    ArrayList <Member> members = new ArrayList<Member>();
    ArrayList <Bill> bills = new ArrayList<Bill>();
    // This is to save the billFileName to update the billFile with the same name
    String billFileName;
    private boolean isTestingMode;
    private boolean isCompetitionLoaded;
    private int myManualNumber;

    public void setMyManualNumber(int myManualNumber) {
        this.myManualNumber = myManualNumber;
    }
    // when handling the exception for entering the number of manual entries required
    // for LuckyNumbers competition, the change in the manual entry variable set in previous attempts was not passed during recursion
    // hence, a new attribute is set in the SimpleCompetition 
    public int getMyManualNumber() {
        return this.myManualNumber;
    }
    // captures whether the user enters if it is testing mode if the competition file is not loaded
    public void setIsTestingMode(boolean isTestingMode){
        this.isTestingMode = isTestingMode;
    }

    /**
     * Allows user to select the five options from the app
     * The app is exited if option 5 is selected
     * @param keyboard  To accept inputs from the user
     * @param simpleCompetition An instance of the SimpleCompetitions class
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public void runMainSimpleGameLoop(Scanner keyboard, SimpleCompetitions simpleCompetition) throws DataAccessException, DataFormatException{
        welcome(keyboard, simpleCompetition);
        boolean isExit = false;
        while(! isExit) {
            isExit = menu(keyboard, simpleCompetition);
        }
    }
    public void addNewCompetition(Competition newCompetition) {
        //if file is not loaded, add the new competition to the arraylist on the simulator
        if(isCompetitionLoaded) {
            if(competitions.size() > 0) {
                newCompetition.setIsTestingMode(competitions.get(0).getIsTestingMode());
            }
        }
        else if(!isCompetitionLoaded){
            newCompetition.setIsTestingMode(isTestingMode);
        }
        competitions.add(newCompetition);
        newCompetition.setId(competitions.size());
        // if the file is loaded, add the new competition to the object arraylist read
    }

    /**
     * Save the competitions arraylist to a binary file such that it can be used in the app in the future
     * Records the full history of competitions
     * @param competitionFileName The name of the binary file
     */
    public void saveCompetition(String competitionFileName) {
        try {

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(competitionFileName));
            outputStream.writeObject(competitions);
            System.out.println("Competitions have been saved to file.");
            outputStream.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Load the competitions from the binary file 
     * @param competitionFileName A path to the competition file (e.g., members.csv)
     * @return An arraylist of previous competitions
     */
    public ArrayList <Competition> loadExistingCompetition(String competitionFileName) {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(competitionFileName));

            ArrayList <Competition> competitions = (ArrayList <Competition>)inputStream.readObject();
            inputStream.close();
            return competitions;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return  new ArrayList<Competition>();
    }

    /**
     * Prints the welcome message to the user
     * Runs a loop until the user either enter "y" or "n" (case insensitive) to load the competition
     * If the user chooses not to load the competition from a binary file, 
     * the user needs to choose whether the current session uses the testing or normal mode
     * @param keyboard Receives input from the user
     * @param simpleCompetitions An instance of the SimpleCompetitions class
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public void welcome(Scanner keyboard, SimpleCompetitions simpleCompetitions) throws DataAccessException, DataFormatException {
        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");
        System.out.println("Load competitions from file? (Y/N)?");

        String isLoadCompetition = keyboard.next().toUpperCase();
        while( !(isLoadCompetition.equals("Y") || isLoadCompetition.equals("N")) ) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Load competitions from file? (Y/N)?");
            isLoadCompetition = keyboard.next().toUpperCase();
        }
        if (isLoadCompetition.equals("Y")) {
            System.out.println("File name:");
            String filename = keyboard.next();
            competitions = loadExistingCompetition(filename);
            isCompetitionLoaded = true;
        }
        else if(isLoadCompetition.equals("N")) {
            System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
            String mode = keyboard.next().toUpperCase();
            isCompetitionLoaded = false;

            while( !(mode.equals("T") || mode.equals("N")) ) {
                System.out.println("Invalid mode! Please choose again.");
                System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
                mode = keyboard.next().toUpperCase();
            }
            if(mode.equals("T")){
                isTestingMode = true;
            }
            else if(mode.equals("N")){
                isTestingMode = false;
            }

        }

        System.out.println("Member file: ");
        String memberFile = keyboard.next();
        System.out.println("Bill file: ");
        String billFile = keyboard.next();
        this.billFileName = billFile;

        // create a DataProvider object to read member and bills file
        DataProvider dataProvider = new DataProvider(memberFile, billFile, simpleCompetitions);
    }


    // helper function for menu when option 1 or option 3 is selected
    public void displayCompetitionInfo() {
        int lastItemIndex = competitions.size() - ONE;
        Competition currentCompetition = competitions.get(lastItemIndex);
        System.out.print("Competition ID: " + currentCompetition.getId() + ", ");
        System.out.print("Competition Name: " + currentCompetition.getName() + ", ");
        System.out.print("Type: " + currentCompetition.getCompetitionName());
        System.out.println();
    }

    // executed if welcome() is successful
    public boolean menu(Scanner keyboard, SimpleCompetitions simpleCompetition) {
        boolean isExit = false;
        try{
            System.out.println("Please select an option. Type 5 to exit.");
            System.out.println("1. Create a new competition");
            System.out.println("2. Add new entries");
            System.out.println("3. Draw winners");
            System.out.println("4. Get a summary report");
            System.out.println("5. Exit");
            int option = keyboard.nextInt();
            isExit = checkOption(option, keyboard, simpleCompetition);
        }
        catch(InputMismatchException e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
        }
        catch(Exception e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
        }
        return isExit;
    }

    // helper method for option 1 of checkOption method
    public boolean checkAllCompetitionCompleted() {
        for(Competition competition: competitions) {
            if(!competition.getIsCompleted()) {
                return false;
            }
        }
        return true;
    }

    // helper method for option 1 of checkOption if the tyepe of competition entered is invalid
    public void competitionTypeLoop(Scanner keyboard, String competitionType) {
        while( !(competitionType.equals("L") || competitionType.equals("R")) ) {
            System.out.println("Invalid competition type! Please choose again.");
            System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
            competitionType = keyboard.next().toUpperCase();
        }

        System.out.println("Competition name: ");
        keyboard.nextLine();
        String competitionName = keyboard.nextLine();
        System.out.println("A new competition has been created!");
        if(competitionType.equals("L")) {
            addNewCompetition(new LuckyNumbersCompetition(competitionName));
        }

        else if(competitionType.equals("R")){
            addNewCompetition(new RandomPickCompetition(competitionName));
        }

        displayCompetitionInfo();

    }

    /**
     * Helper function for menu
     * @param option Records the option number that the user had entered
     * @param keyboard Allows the user to enter more inputs if certain options were selected
     * @param simpleCompetition An instance of the SimpleCompetitions class
     * @return false if option 5 is not selected, true otherwise
     */
    public boolean checkOption(int option, Scanner keyboard, SimpleCompetitions simpleCompetition) {
        // if there are no competitions
        // or all previous competitions are completed
        if(option == 1) {
            if(competitions.size() == 0 || checkAllCompetitionCompleted()){
                System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
                String competitionType = keyboard.next().toUpperCase();
                competitionTypeLoop(keyboard, competitionType);
            }

            else{
                System.out.println("There is an active competition. SimpleCompetitions does not support concurrent competitions!");
            }
        }

        else if(option == 2){
            secondOptionOperation(keyboard, simpleCompetition);
        }
        else if (option == 3) {
            wrapperThirdOptionOperation(simpleCompetition);
        }

        else if (option == 4) {
            if(competitions.size() == 0) {
                System.out.println("No competition has been created yet!");
            }
            else{
                report();
            }
        }

        else if (option == 5){
            fifthOptionOperation(keyboard);
            return true;
        }
        return false;
    }

    public void wrapperThirdOptionOperation(SimpleCompetitions simpleCompetition) {
        if(competitions.size() == 0 || checkAllCompetitionCompleted()) {
            System.out.println("There is no active competition. Please create one!");
        }
        // if there is an active competition
        //check if there are any entries registered
        else if(!checkAllCompetitionCompleted()) {
            int lastIndex = competitions.size() - ONE;
            Competition currCompetition = competitions.get(lastIndex);
            if(currCompetition.getEntries().size() == 0) {
                System.out.println("The current competition has no entries yet!");
            }
            else{
                thirdOptionOperation(simpleCompetition);
            }
        }
    }
    public void secondOptionOperation(Scanner keyboard, SimpleCompetitions simpleCompetition) {
        if(competitions.size() == 0) {
            System.out.println("There is no active competition. Please create one!");
        }
        // the last competition is inactive
        else if(competitions.size() > 0) {
            if(competitions.get(competitions.size() - ONE).getIsActive() == false) {
                System.out.println("There is no active competition. Please create one!");
            }
            else{
                addMoreEntriesLoop(keyboard, simpleCompetition);
                boolean isTrue = addEntryValidityLoop(keyboard);
                while(isTrue) {
                    addMoreEntriesLoop(keyboard, simpleCompetition);
                    isTrue = addEntryValidityLoop(keyboard);
                }
            }
        }
    }

    // helper method for option 5 of checkOption
    public void fifthOptionOperation(Scanner keyboard) {
        System.out.println("Save competitions to file? (Y/N)?");
        String saveFile = keyboard.next().toUpperCase();
        while( !(saveFile.equals("Y") || saveFile.equals("N")) ) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Save competitions to file? (Y/N)?");
            saveFile = keyboard.next().toUpperCase();
        }
        if(saveFile.equals("Y")) {
            System.out.println("File name:");
            String filename = keyboard.next();
            saveCompetition(filename);
            updateBillFile(this.billFileName);
        }
        System.out.println("Goodbye!");
    }

    //helper method for option 3 of checkOption
    public void thirdOptionOperation(SimpleCompetitions simpleCompetition) {
        displayCompetitionInfo();
        // the current competition is the last competition registered in the competitions arraylist
        int lastItemIndex = competitions.size() - ONE;
        Competition currentCompetition = competitions.get(lastItemIndex);
        currentCompetition.drawWinners(simpleCompetition);
        // the competition is deactivated after the winners are chosen
        currentCompetition.setIsActive(false);
        // mark the current competition as complete
        currentCompetition.setIsCompleted(true);
    }

    // helper method for addMoreEntries loop when the bill id entered is invalid (less than 6 digits)
    public String resultantStringLoop(String resultantString, Scanner keyboard) {
        outerloop:
        while (resultantString.length() < REQUIRED_BILLID_LENGTH) {
            // if the user enters any string of less than length six
            // the system automatically asks for more inputs
            String subsequentString = keyboard.next();
            if(subsequentString.length() == REQUIRED_BILLID_LENGTH) {
                resultantString = subsequentString;
                break outerloop;
            }
            resultantString += subsequentString;
        }
        return resultantString;
    }

    // helper method for addMoreEntriesLoop if the competition type is RandomPickCompetition
    public void printRandomPickCompetitionEntries(Bill currentBill, int numberOfEntries, Competition currentCompetition) {
        System.out.println();
        final int MAX_ENTRYID_LENGTH = 6;
        System.out.println("The following entries have been automatically generated:");
        String custBillId = String.valueOf(currentBill.getBillId());
        for (int i = 0; i < numberOfEntries; i++) {
            String customerBillId = String.valueOf(currentBill.getBillId());
            currentCompetition.addEntries(new Entry(customerBillId, currentBill.getMemberId()));
            int currentEntryIndex = currentCompetition.getEntries().size() - ONE;
            Entry currentEntry = currentCompetition.getEntries().get(currentEntryIndex);
            String entryId = String.valueOf(currentEntry.getEntryId());
            if (entryId.length() == ONE) {
                System.out.println("Entry ID: " + currentEntry.getEntryId() + " ".repeat(FIVE));
            } else if (entryId.length() > ONE && entryId.length() <= MAX_ENTRYID_LENGTH) {
                int newSpace = FIVE - (entryId.length() - ONE);
                System.out.println("Entry ID: " + currentEntry.getEntryId() + " ".repeat(newSpace));
            }
        }

    }

    // helper method for addMoreEntriesLoop if the competition type is LuckyNumbersCompetition
    public void printLuckyNumberCompetitionEntries(Competition currentCompetition, int numberOfEntries) {
        System.out.println("The following entries have been added:");
        int currentEntrySize = currentCompetition.getEntries().size();
        int startIndex = currentEntrySize - numberOfEntries;
        final int DEFAULT_SPACE = 6;
        for (int i = startIndex; i < currentEntrySize; i++) {
            Entry currentEntry = currentCompetition.getEntries().get(i);
            //downcast currentEntry to use getNumbers method
            NumbersEntry myEntry = (NumbersEntry) currentEntry;
            String theEntryID = String.valueOf(myEntry.getEntryId());
            int numberOfDigits = theEntryID.length();
            if (numberOfDigits > ONE) {
                int differenceInNumber = numberOfDigits - ONE;
                int numSpaceRepeated = DEFAULT_SPACE - differenceInNumber;
                System.out.print("Entry ID: " + myEntry.getEntryId() + " ".repeat(numSpaceRepeated));
            } else {
                System.out.print("Entry ID: " + myEntry.getEntryId() + "      ");
            }
            // need to print out all numbers in myEntry arraylist
            int[] luckyNumbersFilled = myEntry.getNumbers();
            System.out.print("Numbers: ");
            int count = 0;
            // add another space in front of the number if the number is single-digit
            for (int number : luckyNumbersFilled) {
                String numString = String.valueOf(number);
                int lengthOfNum = numString.length();
                if (lengthOfNum == ONE) {
                    System.out.print(" ");
                }
                if (count < luckyNumbersFilled.length - ONE) {
                    System.out.print(number + " ");
                } else {
                    System.out.print(number);
                }
                count++;
            }
            if (currentEntry.getIsAuto()) {
                System.out.print(" [Auto]");
            }
            System.out.println();
        }
    }
    /**
     * Helper method for option 2 if there is an active competition
     * @param keyboard Allows user to enter more bill ids when it is invalid
     * @param simpleCompetitions An instance for the SimpleCompetitions class
     */
    public void addMoreEntriesLoop(Scanner keyboard, SimpleCompetitions simpleCompetitions) {
        boolean moreEntries;
        System.out.println("Bill ID: ");
        String inputBillId = keyboard.next();
        // checks if the input provided is a six - digit number
        boolean isSixDigits;
        int billIdNum;
        // resultantString is used when the inputBillId length is less than 6
        String resultantString = inputBillId;

        if(resultantString.length() > REQUIRED_BILLID_LENGTH) {
            System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
            addMoreEntriesLoop(keyboard, simpleCompetitions);
        }
        resultantString = resultantStringLoop(resultantString,keyboard);
        if (resultantString.length() == REQUIRED_BILLID_LENGTH) {
            isSixDigits = checkSixDigits(resultantString);
            if (!isSixDigits) {
                System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
                // use a recursive approach 
                addMoreEntriesLoop(keyboard, simpleCompetitions);
            } else if (isSixDigits) {
                billIdNum = Integer.parseInt(resultantString);
                boolean checkId = checkBillId(billIdNum);
                if (!checkId) {
                    System.out.println("This bill does not exist. Please try again.");
                    addMoreEntriesLoop(keyboard, simpleCompetitions);
                } else if (checkId) {
                    Bill currentBill = getCurrentBill(billIdNum);
                    // this means there are no members associated with the bill
                    if (currentBill.getMemberId().equals("")) {
                        System.out.println("This bill has no member id. Please try again.");
                        addMoreEntriesLoop(keyboard, simpleCompetitions);
                    }
                    // when the bill has already been used
                    else if (!currentBill.getMemberId().equals("") && currentBill.getBillTotalAmount() >= MINIMUM_COMPETITION_AMOUNT && currentBill.getIsUsed()) {
                        System.out.println("This bill has already been used for a competition. Please try again.");
                        addMoreEntriesLoop(keyboard, simpleCompetitions);
                    }
                    // we can only use the bill if and only if the bill has a payment of equal or more than 50 dollars
                    // and the bill should be unused for any entries
                    if (currentBill.getBillTotalAmount() < MINIMUM_COMPETITION_AMOUNT) {
                        System.out.print("This bill ($" + currentBill.getBillTotalAmount() + ") is not eligible for an entry. The total amount is smaller than $50.0.");
                    }
                    else if (!currentBill.getMemberId().equals("") && currentBill.getBillTotalAmount() >= MINIMUM_COMPETITION_AMOUNT && !currentBill.getIsUsed()) {
                        currentBill.setIsUsed(true);
                        int numberOfEntries = (int) currentBill.getBillTotalAmount() / MINIMUM_COMPETITION_AMOUNT;
                        System.out.print("This bill ($" + currentBill.getBillTotalAmount() + ") is eligible for " + numberOfEntries + " entries.");
                        int lastItemIndex = competitions.size() - ONE;
                        Competition currentCompetition = competitions.get(lastItemIndex);
                        if (currentCompetition.getCompetitionName().equals("RandomPickCompetition")) {
                            printRandomPickCompetitionEntries(currentBill, numberOfEntries, currentCompetition);
                        }

                        else if (currentCompetition.getCompetitionName().equals("LuckyNumbersCompetition")) {
                            System.out.print(" How many manual entries did the customer fill up?: ");
                            System.out.println();
                            luckyNumbersCompetitionWrapper(simpleCompetitions, keyboard, currentCompetition, currentBill, numberOfEntries);
                        }
                    }
                }
            }
        }
    }
    /** A helper method for luckyNumbersCompetitionWrapper when the number of manual entries entered 
     * is more than the total number of entries eligible for the bill
     * This is to check whether the number of manual entries captured in the user console is an integer
     * If not, the error is captured, and the user is urged to enter the input again in the console
     * @param keyboard Captures input from user
     * @param myManualNumber This is passed recursively until it is checked as a number
     */
    public void handleInputException(Scanner keyboard, int myManualNumber) {
        int lastIndex = competitions.size() - ONE;
        Competition currentCompetition = competitions.get(lastIndex);
        try{
            myManualNumber = keyboard.nextInt();
            setMyManualNumber(myManualNumber);

        }
        catch(InputMismatchException e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
            handleInputException(keyboard, myManualNumber);
        }
        catch(Exception e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
            handleInputException(keyboard, myManualNumber);
        }
    }
    /**
     * Helper method for addMoreEntriesLoop when the type of competition is LuckyNumbersCompetition and the bill number entered is valid
     * Catches the exception error and recurses the method if a non-number input was captured in the console
     */

    public void luckyNumbersCompetitionWrapper(SimpleCompetitions simpleCompetitions, Scanner keyboard, Competition currentCompetition, Bill currentBill, int numberOfEntries) {

        int numManualEntry = 0;
        try{
            numManualEntry = keyboard.nextInt();
        }
        catch(InputMismatchException e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
            luckyNumbersCompetitionWrapper(simpleCompetitions, keyboard, currentCompetition, currentBill, numberOfEntries);
        }
        catch(Exception e) {
            System.out.println("A number is expected. Please try again.");
            keyboard.nextLine();
            luckyNumbersCompetitionWrapper(simpleCompetitions, keyboard, currentCompetition, currentBill, numberOfEntries);
        }
        while (numManualEntry > numberOfEntries) {
            System.out.println("The number must be in the range from 0 to " + numberOfEntries + ". Please try again.");
            int myManualNumber = 0;
            handleInputException(keyboard, myManualNumber);
            numManualEntry = getMyManualNumber();
        }

        if (numManualEntry <= numberOfEntries) {
            if (numManualEntry > 0) {
                luckyNumbersManualEntryLoop(currentCompetition, keyboard, numManualEntry, currentBill);
            }
            luckyNumberAutoGenerateLoop(simpleCompetitions, currentCompetition, numManualEntry, numberOfEntries, currentBill);
            printLuckyNumberCompetitionEntries(currentCompetition, numberOfEntries);
        }
    }
    /**
     * Checks if the user wants to enter more entries
     * A loop is executed if the user does not enter either "y" or "n" (input insensitive)
     * @param keyboard Allows user to enter inputs in the console
     */
    public boolean addEntryValidityLoop(Scanner keyboard) {
        System.out.println("Add more entries (Y/N)?");
        String wantMoreEntries = keyboard.next().toUpperCase();
        while( !(wantMoreEntries.equals("N") || wantMoreEntries.equals("Y")) ) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Add more entries (Y/N)?");
            wantMoreEntries = keyboard.next().toUpperCase();
        }
        if(wantMoreEntries.equals("N")) {
            return false;
        }
        else if(wantMoreEntries.equals("Y")) {
            return true;
        }
        return false;
    }

    /**
     * helper method for option 2 in the menu method if the current active competition is the LuckyNumbersCompetition
     * executed after manual entries are added
     * uses auto number generator
     * @param simpleCompetitions An instance of the SimpleCompetitions class
     * @param currentCompetition To check whether the current competition is in testing mode
     * @param numManualEntry To check whether number of manual entries is less than number of eligible entries
     * @param numberOfEntries Number of eligibile entries based on the total bill amount
     * @param currentBill To get the bill id and customer id from the bill to create a new NumbersEntry instance
     */
    public void luckyNumberAutoGenerateLoop(SimpleCompetitions simpleCompetitions, Competition currentCompetition, int numManualEntry, int numberOfEntries, Bill currentBill) {

        for (int i = numManualEntry; i < numberOfEntries; i++) {
            // create dummy numbers for autoNumbersEntry constructor to use its method
            int[] dummyNumbers = {0};
            String custBillId = String.valueOf(currentBill.getBillId());
            AutoNumbersEntry autoNumbersEntry = new AutoNumbersEntry(custBillId, currentBill.getMemberId(), dummyNumbers);
            int[] tempNumbers = new int[7];

            if(currentCompetition.getIsTestingMode()) {
                // the seed is the previous entry id 
                // this is the size before adding the new automated entry
                int seed = currentCompetition.getEntries().size();
                tempNumbers = autoNumbersEntry.createNumbers(seed, simpleCompetitions);
            }
            else if(!currentCompetition.getIsTestingMode()) {
                tempNumbers = autoNumbersEntry.createNumbers(0, simpleCompetitions);
            }

            currentCompetition.addEntries(new NumbersEntry(custBillId, currentBill.getMemberId(), tempNumbers));
            // get the most recent entry
            int lastIndex = currentCompetition.getEntries().size() - ONE;
            // save this entry as automatically generated
            currentCompetition.getEntries().get(lastIndex).setIsAuto(true);
        }
    }

    /**
     * @param allNumbers Collection of 7 "lucky numbers" in string format
     * @return false if an error is caught when converting String to Integer
     */
    public boolean isNumeric(String[] allNumbers) {
        for(String theNumber : allNumbers) {
            try{
                int number = Integer.parseInt(theNumber);
            }
            catch(Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method for option 2 in the menu method if the current active competition is the LuckyNumbersCompetition
     * This is for the manual entry for LuckyNumbersCompetition
     * @param keyboard Allows user to enter input in the console
     * @param currentCompetition To check whether the current competition is in testing mode
     * @param numManualEntry To check whether number of manual entries is less than number of eligible entries
     * @param currentBill To get the bill id and customer id from the bill to create a new NumbersEntry instance

     */
    public void luckyNumbersManualEntryLoop(Competition currentCompetition, Scanner keyboard, int numManualEntry, Bill currentBill) {
        String allNumbersInLine;
        String[] allNumbers;
        boolean isEntryCorrect = false;
        int count = 0;
        System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
        while(count < numManualEntry ) {
            do{
                allNumbersInLine = keyboard.nextLine();
                //System.out.println("The numbers are: " + allNumbersInLine);
                allNumbers = allNumbersInLine.split(" ");
                if(allNumbersInLine.equals("")) {
                    continue;
                }
                else if(!isNumeric(allNumbers)) {
                    System.out.println("Invalid input! Numbers are expected. Please try again!");
                    System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    continue;
                }
                else if(allNumbers.length < 7){
                    System.out.println("Invalid input! Fewer than 7 numbers are provided. Please try again!");
                    System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    continue;
                }
                else if(!isNumeric(allNumbers)) {
                    System.out.println("Invalid input! Numbers are expected. Please try again!");
                    System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    continue;
                }
                else if (allNumbers.length > 7){
                    System.out.println("Invalid input! More than 7 numbers are provided. Please try again!");
                    System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    continue;
                }
                else if(allNumbers.length == 7){
                    // this is for saving the actual correct number
                    int[] luckyNumbers = luckyNumbersArray(allNumbers);
                    boolean isNumberDifferent = allNumbersIsDifferent(luckyNumbers);
                    boolean isInRange = checkLuckyNumberRange(luckyNumbers);
                    if(!isNumberDifferent) {
                        System.out.println("Invalid input! All numbers must be different!");
                        System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    }
                    else if(!isInRange) {
                        System.out.println("Invalid input! All numbers must be in the range from 1 to 35!");
                        System.out.println("Please enter 7 different numbers (from the range 1 to 35) separated by whitespace.");
                    }
                    else{
                        isEntryCorrect = true;
                        String custBillId = String.valueOf(currentBill.getBillId());
                        Arrays.sort(luckyNumbers);
                        currentCompetition.addEntries(new NumbersEntry(custBillId, currentBill.getMemberId(), luckyNumbers));
                        int lastIndex = currentCompetition.getEntries().size() - ONE;
                        // save this entry as automatically generated
                        currentCompetition.getEntries().get(lastIndex).setIsAuto(false);
                        count++;
                    }
                }
            }while(!isEntryCorrect);
        }
    }
    /**
     * Get the bill object with the matching bill id
     * @param inputBillId The bill id obtained from the user input console
     * @return The bill from the bills arraylist that has the same billId as entered by the user in the console
     */
    public Bill getCurrentBill(int inputBillId){
        for(Bill bill : bills) {
            if(inputBillId == bill.getBillId()){
                return bill;
            }
        }
        // return a dummy bill
        return new Bill(0, "0", 0, false);
    }
    public boolean checkLuckyNumberRange(int[] luckyNumberArray) {
        final int MAX_LUCKYNUMBER = 35;
        for(int i = 0; i < luckyNumberArray.length; i++) {
            if(luckyNumberArray[i] < ONE || luckyNumberArray[i] > MAX_LUCKYNUMBER){
                return false;
            }
        }
        return true;
    }
    /**
     * Convert all the lucky numbers entered by the user from String to Integer
     * @param allNumbers A string array of size 7
     * @return an array of 7 lucky numbers
     */
    public int[] luckyNumbersArray(String[] allNumbers){
        int[] luckyNumberArray = new int[allNumbers.length];

        for(int i = 0; i < allNumbers.length; i++) {
            luckyNumberArray[i] = Integer.parseInt(allNumbers[i]);
        }

        return luckyNumberArray;
    }
    /**
     * Checks if the user enters 7 distinct numbers for manual entry of LuckyNumbersCompetition
     * @param luckyNumberArray An integer array of size 7
     * @return true if all numbers are distinct
     */
    public boolean allNumbersIsDifferent(int[] luckyNumberArray){
        Arrays.sort(luckyNumberArray);
        for(int i = 0; i < luckyNumberArray.length - ONE; i++) {
            if( luckyNumberArray[i] == luckyNumberArray[i + 1] ){
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the billid belongs to a member
     * @param inputBillId The bill id captured from the user input
     * @return true if the bill id belongs to a member
     */
    public boolean checkBillId(int inputBillId) {
        for(Bill bill : bills) {
            if(inputBillId == bill.getBillId()){
                if(bill.getMemberId().equals(" ")) {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkSixDigits(String inputBillId) {
        boolean isDigit;
        isDigit = checkIsDigit(inputBillId);
        return isDigit;
    }

    public boolean checkIsDigit(String someInput) {
        for (int i = 0; i < someInput.length() ; i++) {
            if(!( Character.isDigit(someInput.charAt(i)) )){
                return false;
            }
        }
        return true;
    }
    /**
     * Helper method for runMainSimpleGameLoop
     * @param keyboard Accepts inputs from user
     * @param simpleCompetition An instance of the SimpleCompetitions class
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public void simpleCompetitionLoop(Scanner keyboard, SimpleCompetitions simpleCompetition) throws DataAccessException, DataFormatException {
        boolean isExit = false;
        welcome(keyboard, simpleCompetition);
        while(isExit){
            isExit = menu(keyboard, simpleCompetition);
        }

    }
    /**
     * Generates the summary report for the number of completed competitions or active competitions
     * Helper method for option 4 of checkOption
     * Calls on the report method from the Competition class
     */
    public void report() {
        System.out.println("----SUMMARY REPORT----");
        int numCompletedCompetition = countCompletedCompetition();
        System.out.println("+Number of completed competitions: " + numCompletedCompetition);
        int numActiveCompetition = countActiveCompetition();
        System.out.println("+Number of active competitions: " + numActiveCompetition);

        for(Competition competition : competitions) {
            competition.report();

        }

    }
    // used as a helper method for report() method
    public int countCompletedCompetition() {
        int count = 0;
        for(Competition competition : competitions) {
            if(competition.getIsCompleted() == true) {
                count++;
            }
        }
        return count;
    }
    // used as a helper method for report() method
    public int countActiveCompetition() {
        int count = 0;
        for(Competition competition : competitions) {
            if(competition.getIsActive() == true) {
                count++;
            }
        }
        return count;
    }
    /**
     * Updates the bill file when option 5 is selected and the user chooses to save the competition binary file
     * @param billFileName The name/path of the bill file to be updated
     */
    public void updateBillFile(String billFileName) {
        // 1. Read each line from the file
        // 2. Split the line
        // 3. Prepare another line with updated data to write to temp file
        // 4. Finally delete your old file and rename temp file to the old file
        Scanner inputStream = null;
        PrintWriter outputStream = null;
        String billLine;
        String[] billInfo;
        int billId;
        String customerId;
        double totalAmount;
        boolean hasUsed;
        Bill currentBill;
        try {
            File originalFile = new File(billFileName);
            // Construct the new file that will later be renamed to the original filename
            File tempFile = new File("tempfile.csv");
            inputStream = new Scanner( new FileInputStream(originalFile) );
            outputStream = new PrintWriter( new FileOutputStream(tempFile) );
            while(inputStream.hasNextLine()) {
                billLine = inputStream.nextLine();
                billInfo = billLine.split(",");
                billId = Integer.parseInt(billInfo[0]);
                customerId = billInfo[1];
                totalAmount = Double.parseDouble(billInfo[2]);
                hasUsed = Boolean.parseBoolean(billInfo[3]);
                currentBill = getCurrentBill(billId);
                // check if the bill used state has changed
                if(currentBill.getIsUsed() != hasUsed) {
                    outputStream.println(billId + "," + customerId + "," + String.format("%.1f", totalAmount) + "," + currentBill.getIsUsed());
                }
                else{
                    outputStream.println(billId + "," + customerId + "," + String.format("%.1f", totalAmount) + "," + hasUsed);
                }
            }
            inputStream.close();
            outputStream.close();
            // Delete the original file
            originalFile.delete();
            tempFile.renameTo(originalFile);
            System.out.println("The bill file has also been automatically updated.");
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Main program that uses the main SimpleCompetitions class
     * @param args main program arguments
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public static void main(String[] args) throws DataAccessException, DataFormatException {
        Scanner keyboard = new Scanner(System.in);

        //Create an object of the SimpleCompetitions class
        SimpleCompetitions sc = new SimpleCompetitions();
        sc.runMainSimpleGameLoop(keyboard, sc);
        //Add your code to complete the task
    }
}