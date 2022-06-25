/**
 * DataFormatException error is thrown in the DataProvider constructor
 * Thrown if the filetype is not .csv for memberfile or billfile
 * @ auth Student name: Cheah Jia Huei Student ID: 1078203 LMS username: jiahueic
 */
import java.io.Serializable;
public class DataFormatException extends Exception implements Serializable{
    /**
     * Overloading of DataFormatException constructors
     * DataFormatException is the default input when there is not input is provided in constructor
     */
    public DataFormatException() {
        super("DataFormatException");
    }
    public DataFormatException(String aMessage) {
        super(aMessage);
    }

}