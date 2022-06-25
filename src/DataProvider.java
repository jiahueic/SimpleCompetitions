/**
 * To be instantiated in the SimpleCompetition File to read both the member file and the bills file
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
public class DataProvider implements Serializable{
    /**
     *
     * @param memberFile A path to the member file (e.g., members.csv)
     * @param billFile A path to the bill file (e.g., bills.csv)
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public DataProvider(String memberFile, String billFile, SimpleCompetitions simpleCompetitions)
            throws DataAccessException, DataFormatException {
        try{
            File memberTempFile = new File(memberFile);
            File billTempFile = new File(billFile);
            if(!memberTempFile.isFile() || !billTempFile.isFile()){
                throw new DataAccessException();
            }

            // last three characters of memberFile and billFile String
            int memberFileBeginIndex = memberFile.length() - 3;
            int billFileBeginIndex = billFile.length() - 3;
            String memberFileFormat = memberFile.substring(memberFileBeginIndex);
            String billFileformat = billFile.substring(billFileBeginIndex);
            if(!memberFileFormat.equals("csv") || !billFileformat.equals("csv")){
                throw new DataFormatException();
            }
            Scanner memberInputStream = new Scanner(new FileInputStream(memberFile));
            readMemberFile(simpleCompetitions, memberInputStream);
            Scanner billFileStream = new Scanner( new FileInputStream(billFile) );
            readBillFile(simpleCompetitions, billFileStream);
        }
        catch(DataAccessException e){
            System.out.println(e.getMessage());
        }
        catch(DataFormatException e){
            System.out.println(e.getMessage());
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Reads the member file line by line after the csv file has been opened
     * @param simpleCompetitions An instance of the SimpleCompetitions class
     * @param inputStream The stream of data received after the opening of the member file is successful
     */
    public void readMemberFile(SimpleCompetitions simpleCompetitions, Scanner inputStream) {
        String memberLine;
        String[] memberInfo;
        String memberId;
        String memberName;
        String address;
        while(inputStream.hasNextLine()) {
            memberLine = inputStream.nextLine();
            memberInfo = memberLine.split(",");
            memberId = memberInfo[0];
            memberName = memberInfo[1];
            address = memberInfo[2];
            simpleCompetitions.members.add( new Member(memberId, memberName, address) );
        }
        inputStream.close();
    }

    /**
     * Reads the bill file line by line after the bills csv file is successfully opened
     * @param simpleCompetitions An instance of the SimpleCompetitions class
     * @param inputFileStream The stream of data received after the opening of the bill file is successful
     */

    public void readBillFile(SimpleCompetitions simpleCompetitions, Scanner inputFileStream) {
        String billLine;
        String[] billInfo;
        int billId;
        String customerId;
        double totalAmount;
        boolean hasUsed;
        while(inputFileStream.hasNextLine()){
            billLine = inputFileStream.nextLine();
            billInfo = billLine.split(",");
            billId = Integer.parseInt(billInfo[0]);
            customerId = billInfo[1];
            totalAmount = Double.parseDouble(billInfo[2]);
            hasUsed = Boolean.parseBoolean(billInfo[3]);
            simpleCompetitions.bills.add(new Bill(billId, customerId, totalAmount, hasUsed));
        }
        inputFileStream.close();

    }
}