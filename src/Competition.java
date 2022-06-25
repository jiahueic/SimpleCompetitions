/**
 * Stores attributes shared by both LuckyNumbersCompetition and RandomPickCompetition such as name and competition id.
 * Modified the class (no more abstract drawWinners and addEntries method) because the input type is different in its descendents.
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;
public abstract class Competition implements Serializable{
    private String name; //competition name
    private int id; //competition identifier
    private boolean isActive;
    private boolean isCompleted;
    private boolean isTestingMode;
    private int numWinningEntries;
    private int totalAwardedPrizes;

    /**
     * @param name Records the name for the competition
     */
    public Competition(String name) {
        this.name = name;
        setIsActive(true);

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAwardedPrizes() {
        return this.totalAwardedPrizes;
    }

    public void setTotalAwardedPrizes(int totalAwardedPrizes) {
        this.totalAwardedPrizes = totalAwardedPrizes;
    }

    public void setIsTestingMode(boolean isTestingMode){
        this.isTestingMode = isTestingMode;
    }

    public boolean getIsTestingMode() {
        return this.isTestingMode;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return this.id;
    }

    public abstract String getCompetitionName();
    public abstract void addEntries(Entry newEntry);
    public abstract void drawWinners(SimpleCompetitions simpleCompetition);

    public String getName() {
        return this.name;
    }

    public abstract ArrayList<Entry> getEntries();

    public int getNumWinningEntries(){
        return this.numWinningEntries;
    }

    public void setNumWinningEntries(int numWinningEntries){
        this.numWinningEntries = numWinningEntries;
    }

    /**
     * Reports the competiition id, number of winning entries and the number of prizes awarded if the competition is completed
     * Only reports competition id and name if the competition is still active
     */
    public void report() {
        if(isActive) {
            System.out.println();
            System.out.println("Competition ID: " + getId() + ", name: " + name + ", active: " + "yes");
        }
        else{
            System.out.println();
            System.out.println("Competition ID: " + getId() + ", name: " + name + ", active: " + "no");
        }

        System.out.println("Number of entries: "+ getEntries().size());
        if(isCompleted == true) {
            System.out.println("Number of winning entries: " + numWinningEntries);
            System.out.println("Total awarded prizes: " + getTotalAwardedPrizes());
        }

    }
}