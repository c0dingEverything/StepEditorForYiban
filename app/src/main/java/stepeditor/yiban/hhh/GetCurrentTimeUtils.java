package stepeditor.yiban.hhh;

import java.util.Calendar;

public class GetCurrentTimeUtils {

    public static String getData() {
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String result = year + "-" + month + "-" + day;
        System.out.println(result);
        return result;
    }
}
