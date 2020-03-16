package club.nipengfei.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    // 日期转换成字符串
    public static String date2String(Date date, String string){
        SimpleDateFormat sdf = new SimpleDateFormat(string);
        String format = sdf.format(date);
        return format;
    }
    // 字符串转换成日期
    public static Date string2Date(String string,String patt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        Date date = sdf.parse(string);
        return date;
    }
}
