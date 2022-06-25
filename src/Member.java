/**
 * Stores the member information such as memberId, memberName, and member emailaddress
 * Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
public class Member implements Serializable {
    private String memberId;
    private String memberName;
    private String memberAddress;

    /**
     *
     * @param memberId Records the identification number for the member
     * @param memberName Records the name of the member
     * @param memberAddress Records the email address of the member
     */
    public Member(String memberId, String memberName, String memberAddress){
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberAddress = memberAddress;

    }

    public String getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

}