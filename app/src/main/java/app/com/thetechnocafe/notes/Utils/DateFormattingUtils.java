package app.com.thetechnocafe.notes.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gurleensethi on 14/04/17.
 */

public class DateFormattingUtils {
    private static DateFormattingUtils sInstance;

    //Instance method
    public static DateFormattingUtils getInstance() {
        if (sInstance == null) {
            sInstance = new DateFormattingUtils();
        }
        return sInstance;
    }

    //Singleton class
    private DateFormattingUtils() {
    }

    //Convert long to string date format (Format : 14:15 21 Jan 2016)
    public String convertLongToDateString(long timeInLong) {
        //Convert long to date
        Date date = new Date(timeInLong);

        //Convert date to string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd MMM yy");

        return simpleDateFormat.format(date);
    }
}
