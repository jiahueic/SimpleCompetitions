/**
 * Used for RandomPickCompetition where only entry id, and memberid is needed
 * Stores the amount of prizes that the entry has gotten so far.
 * The ancestor class for NumbersEntry 
 * @auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
// 
import java.io.Serializable;
public class Entry implements Serializable{
    private int entryId;
    private String billId;
    private String memberId;
    private int prize;
    // stores if the luckyNumber entry is manually filled or automatically generated
    private boolean isAuto;

    /**
     * For each entry, the initial prize amount is set to 0
     * @param billId Links the member's bill id to the competition entry
     * @param memberId Links the member's identification to the competition entry
     */
    public Entry(String billId, String memberId){
        this.billId = billId;
        this.memberId = memberId;
        setPrize(0);
    }

    public String getMemberId(){
        return this.memberId;
    }

    public int getEntryId(){
        return this.entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public void setPrize(int newPrize) {
        this.prize = newPrize;
    }

    public int getPrize() {
        return this.prize;
    }

    public boolean getIsAuto() {
        return this.isAuto;
    }

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

}