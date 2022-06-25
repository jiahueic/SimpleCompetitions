/**
 * Stores the bill information for bills with a valid 6-digit billid number entered in the simpleCompetitions simulator
 * @auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
public class Bill implements Serializable{
    private int billId;
    private String memberId;
    private double billTotalAmount;
    private boolean isUsed;
    /**
     *
     * @param billId Record the bill number associated with the bill
     * @param memberId Record the member identification associated with the bill
     * @param billTotalAmount Records the amount spent to check how many entries in the competition is the customer eligible for
     * @param isUsed Should be false before it is used in a competition, such the the same bill cannot be reused
     */
    public Bill(int billId, String memberId, double billTotalAmount, boolean isUsed){
        this.billId = billId;
        this.memberId = memberId;
        this.billTotalAmount = billTotalAmount;
        this.isUsed = isUsed;
    }
    public int getBillId() {
        return this.billId;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public double getBillTotalAmount() {
        return this.billTotalAmount;
    }

    // each bill can only be used once for an entry
    public boolean getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }



}