package utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class HLLUtils {
    public static String TIME_FORMAT = "yyyyMMddHHmm";
    private static String TIME_FORMAT_MONTH_DAY = "MMdd";
    private static String TIME_FORMAT_DAY_MINUTES = "MMddHHmm";
    private static String TIME_FORMAT_DAY_HOURS = "MMddHH";
    static SimpleDateFormat FORMAT_MONTH_DAY = new SimpleDateFormat(TIME_FORMAT_MONTH_DAY);
    static SimpleDateFormat FORMAT_DAY_HOURS = new SimpleDateFormat(TIME_FORMAT_DAY_HOURS);
    static SimpleDateFormat FORMAT_DAY_MINUTES = new SimpleDateFormat(TIME_FORMAT_DAY_MINUTES);

    public static List<String> parse(Date d1, Date d2) {
        ArrayList list;
        if (d1.compareTo(d2) == 0) {
            return Collections.emptyList();
        }

        // if less than an hour, sum all minutes
        long delta = d2.getTime() - d1.getTime();
        if (delta == 0) {
            return Collections.emptyList();
        }
        if (delta < DateUtils.MILLIS_PER_HOUR) {
            int minutesDiff = (int) (delta / DateUtils.MILLIS_PER_MINUTE);
            list = new ArrayList<String>();
            Date date1Increment = d1;
            while (d2.compareTo(date1Increment) > 0 && minutesDiff > 0) {
                list.add(FORMAT_DAY_MINUTES.format(date1Increment));
                date1Increment = DateUtils.addMinutes(date1Increment, 1);
            }
            // if less than an day, trim hours sum all, pass minutes part
        } else if (delta < DateUtils.MILLIS_PER_DAY) {
            list = new ArrayList<String>();
            Date dateLastPortionHour = DateUtils.truncate(d2, Calendar.HOUR_OF_DAY);
            list.addAll(parse(dateLastPortionHour, d2));
            long delta2 = dateLastPortionHour.getTime() - d1.getTime();
            int hoursDiff = (int) (delta2 / DateUtils.MILLIS_PER_HOUR);
            Date date1Increment = DateUtils.addHours(dateLastPortionHour, -1 * hoursDiff);
            while (dateLastPortionHour.compareTo(date1Increment) > 0 && hoursDiff > 0) {
                list.add(FORMAT_DAY_HOURS.format(date1Increment));
                date1Increment = DateUtils.addHours(date1Increment, 1);
            }
            list.addAll(parse(d1, DateUtils.addHours(dateLastPortionHour, -1 * hoursDiff)));
        } else {
            list = new ArrayList<String>();
            Date dateLastPortionDay = DateUtils.truncate(d2, Calendar.DAY_OF_MONTH);
            list.addAll(parse(dateLastPortionDay, d2));
            long delta2 = dateLastPortionDay.getTime() - d1.getTime();
            // if less than an month, trim days sum all, pass hours part
            int daysDiff = (int) (delta2 / DateUtils.MILLIS_PER_DAY);
            // Date date1Increment = d1;
            Date date1Increment = DateUtils.addDays(dateLastPortionDay, -1 * daysDiff);
            while (dateLastPortionDay.compareTo(date1Increment) > 0 && daysDiff > 0) {
                list.add(FORMAT_MONTH_DAY.format(date1Increment));
                date1Increment = DateUtils.addDays(date1Increment, 1);
            }
            list.addAll(parse(d1, DateUtils.addDays(dateLastPortionDay, -1 * daysDiff)));
        }
        return list;
    }

    public static List<String> getLastMinutes(Date date, int minutes) {
        return parse(DateUtils.addMinutes(date, -1 * minutes), date);
    }

    public static List<String> getLastHours(Date date, int hours) {
        return parse(DateUtils.addHours(date, -1 * hours), date);
    }

    public static List<String> getLastDays(Date date, int days) {
        return parse(DateUtils.addDays(date, -1 * days), date);
    }

    public static List<String> addPrefix(List<String> keys, String prefix) {
        return keys.stream().map(key -> prefix + key).collect(Collectors.toList());
    }
}