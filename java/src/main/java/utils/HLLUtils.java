package utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class HLLUtils {
    private static String TIME_FORMAT_MONTH_DAY = "MMdd";
    private static String TIME_FORMAT_DAY_MINUTES = "MMddHHmm";
    private static String TIME_FORMAT_DAY_HOURS = "MMddHH";
    private static SimpleDateFormat FORMAT_MONTH_DAY = new SimpleDateFormat(TIME_FORMAT_MONTH_DAY);
    private static SimpleDateFormat FORMAT_DAY_HOURS = new SimpleDateFormat(TIME_FORMAT_DAY_HOURS);
    private static SimpleDateFormat FORMAT_DAY_MINUTES = new SimpleDateFormat(TIME_FORMAT_DAY_MINUTES);

    /**
     * 获取两个日期之间的键
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 键列表
     */
    public static List<String> parse(Date d1, Date d2) {
        List<String> list = new ArrayList<>();
        if (d1.compareTo(d2) == 0) {
            return list;
        }

        // 若时间差小于 1 小时
        long delta = d2.getTime() - d1.getTime();
        if (delta == 0) {
            return list;
        }
        if (delta < DateUtils.MILLIS_PER_HOUR) {
            int minutesDiff = (int) (delta / DateUtils.MILLIS_PER_MINUTE);
            Date date1Increment = d1;
            while (d2.compareTo(date1Increment) > 0 && minutesDiff > 0) {
                list.add(FORMAT_DAY_MINUTES.format(date1Increment));
                date1Increment = DateUtils.addMinutes(date1Increment, 1);
            }
            // 若时间差小于 1 天
        } else if (delta < DateUtils.MILLIS_PER_DAY) {
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
            Date dateLastPortionDay = DateUtils.truncate(d2, Calendar.DAY_OF_MONTH);
            list.addAll(parse(dateLastPortionDay, d2));
            long delta2 = dateLastPortionDay.getTime() - d1.getTime();
            // 若时间差小于 1 个月
            int daysDiff = (int) (delta2 / DateUtils.MILLIS_PER_DAY);
            Date date1Increment = DateUtils.addDays(dateLastPortionDay, -1 * daysDiff);
            while (dateLastPortionDay.compareTo(date1Increment) > 0 && daysDiff > 0) {
                list.add(FORMAT_MONTH_DAY.format(date1Increment));
                date1Increment = DateUtils.addDays(date1Increment, 1);
            }
            list.addAll(parse(d1, DateUtils.addDays(dateLastPortionDay, -1 * daysDiff)));
        }
        return list;
    }

    /**
     * 获取从 date 往前推 minutes 分钟的键列表
     *
     * @param date    特定日期
     * @param minutes 分钟数
     * @return 键列表
     */
    public static List<String> getLastMinutes(Date date, int minutes) {
        return parse(DateUtils.addMinutes(date, -1 * minutes), date);
    }

    /**
     * 获取从 date 往前推 hours 个小时的键列表
     *
     * @param date  特定日期
     * @param hours 小时数
     * @return 键列表
     */
    public static List<String> getLastHours(Date date, int hours) {
        return parse(DateUtils.addHours(date, -1 * hours), date);
    }

    /**
     * 获取从 date 开始往前推 days 天的键列表
     *
     * @param date 特定日期
     * @param days 天数
     * @return 键列表
     */
    public static List<String> getLastDays(Date date, int days) {
        return parse(DateUtils.addDays(date, -1 * days), date);
    }

    /**
     * 为keys列表添加前缀
     *
     * @param keys   键列表
     * @param prefix 前缀符号
     * @return 添加了前缀的键列表
     */
    public static List<String> addPrefix(List<String> keys, String prefix) {
        return keys.stream().map(key -> prefix + key).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Date d1 = new Date();
        d1.setTime((long) (System.currentTimeMillis() - 3600 * 1000 * 24 * 1.2));
        Date d2 = new Date();
        List<String> res = parse(d1, d2);
        res.forEach(System.out::println);
        List<String> newRes = addPrefix(res, "USER:LOGIN:");
        System.out.println("-------");
        newRes.forEach(System.out::println);

        List<String> lastThreeDays = getLastDays(new Date(), 3);
        System.out.println("-------");
        lastThreeDays.forEach(System.out::println);

        List<String> lastTwoHours = getLastHours(new Date(), 2);
        System.out.println("-------");
        lastTwoHours.forEach(System.out::println);

        List<String> lastFourMinutes = getLastMinutes(new Date(), 4);
        System.out.println("-------");
        lastFourMinutes.forEach(System.out::println);
    }
}