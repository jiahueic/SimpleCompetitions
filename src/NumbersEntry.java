/**
 * Records the corresponding 7 lucky draw number array for a specified member with an eligible bill
 * This is then inserted into the entries list in the simulation.
 * Inherited by AutoNumbersEntry, whereby numbers store the auto - generated variable, tempNumbers. 
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
public class NumbersEntry extends Entry implements Serializable{
    private int[] numbers;
    /**
     *
     * @param billId Records the bill number
     * @param memberId Records the member's identification number
     * @param numbers Stores the lucky numbers manually entered/ automatically generated for the member
     */
    public NumbersEntry(String billId, String memberId, int[] numbers) {
        super(billId, memberId);
        setNumbers(numbers);

    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }


    public int[] getNumbers() {
        return this.numbers = numbers;
    }




}