/**
 * DataAccessException error is thrown in the DataProvider constructor if the member/bills file cannot be opened/read
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
public class DataAccessException extends Exception implements Serializable{
    /**
     * Overloading of DataAccessException constructors
     * DataAccessException is the default input when there is not input is provided in constructor
     */
    public DataAccessException(){
        super("DataAccessException");
    }
    public DataAccessException(String aMessage){
        super(aMessage);
    }


}