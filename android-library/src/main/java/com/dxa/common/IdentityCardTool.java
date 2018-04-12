package com.dxa.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类
 */
public class IdentityCardTool {

    /**
     * 中国公民身份证号码最小长度
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 每位加权因子
     */
    private static final int FACTORS[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 第18位校检码 
     */
//	private static final String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

    /**
     * 最低年限 
     */
    private static final int MIN_YEAR = 1930;
    /**
     * 省、直辖市代码表
     */
    private static Map<String, String> PROVINCE_CODES = new HashMap<>();
    /**
     * 台湾身份首字母对应数字 
     */
    private static Map<String, Integer> TW_FIRST_CODE = new HashMap<>();
    /**
     * 香港身份首字母对应数字 
     */
    private static Map<String, Integer> HK_FIRST_CODE = new HashMap<>();

    private static final ThreadLocal<SimpleDateFormat> BIRTHDAY_CACHE;
    private static final ThreadLocal<SimpleDateFormat> BIRTHDAY_CACHE15;
    private static final ThreadLocal<SimpleDateFormat> YEAR_CACHE;

    static {
        BIRTHDAY_CACHE = new ThreadLocal<>();
        BIRTHDAY_CACHE15 = new ThreadLocal<>();
        YEAR_CACHE = new ThreadLocal<>();

        // 大陆
        PROVINCE_CODES.put("11", "北京");
        PROVINCE_CODES.put("12", "天津");
        PROVINCE_CODES.put("13", "河北");
        PROVINCE_CODES.put("14", "山西");
        PROVINCE_CODES.put("15", "内蒙古");
        PROVINCE_CODES.put("21", "辽宁");
        PROVINCE_CODES.put("22", "吉林");
        PROVINCE_CODES.put("23", "黑龙江");
        PROVINCE_CODES.put("31", "上海");
        PROVINCE_CODES.put("32", "江苏");
        PROVINCE_CODES.put("33", "浙江");
        PROVINCE_CODES.put("34", "安徽");
        PROVINCE_CODES.put("35", "福建");
        PROVINCE_CODES.put("36", "江西");
        PROVINCE_CODES.put("37", "山东");
        PROVINCE_CODES.put("41", "河南");
        PROVINCE_CODES.put("42", "湖北");
        PROVINCE_CODES.put("43", "湖南");
        PROVINCE_CODES.put("44", "广东");
        PROVINCE_CODES.put("45", "广西");
        PROVINCE_CODES.put("46", "海南");
        PROVINCE_CODES.put("50", "重庆");
        PROVINCE_CODES.put("51", "四川");
        PROVINCE_CODES.put("52", "贵州");
        PROVINCE_CODES.put("53", "云南");
        PROVINCE_CODES.put("54", "西藏");
        PROVINCE_CODES.put("61", "陕西");
        PROVINCE_CODES.put("62", "甘肃");
        PROVINCE_CODES.put("63", "青海");
        PROVINCE_CODES.put("64", "宁夏");
        PROVINCE_CODES.put("65", "新疆");
        PROVINCE_CODES.put("71", "台湾");
        PROVINCE_CODES.put("81", "香港");
        PROVINCE_CODES.put("82", "澳门");
        PROVINCE_CODES.put("91", "国外");

        // 台湾
        TW_FIRST_CODE.put("A", 10);
        TW_FIRST_CODE.put("B", 11);
        TW_FIRST_CODE.put("C", 12);
        TW_FIRST_CODE.put("D", 13);
        TW_FIRST_CODE.put("E", 14);
        TW_FIRST_CODE.put("F", 15);
        TW_FIRST_CODE.put("G", 16);
        TW_FIRST_CODE.put("H", 17);
        TW_FIRST_CODE.put("J", 18);
        TW_FIRST_CODE.put("K", 19);
        TW_FIRST_CODE.put("L", 20);
        TW_FIRST_CODE.put("M", 21);
        TW_FIRST_CODE.put("N", 22);
        TW_FIRST_CODE.put("P", 23);
        TW_FIRST_CODE.put("Q", 24);
        TW_FIRST_CODE.put("R", 25);
        TW_FIRST_CODE.put("S", 26);
        TW_FIRST_CODE.put("T", 27);
        TW_FIRST_CODE.put("U", 28);
        TW_FIRST_CODE.put("V", 29);
        TW_FIRST_CODE.put("X", 30);
        TW_FIRST_CODE.put("Y", 31);
        TW_FIRST_CODE.put("W", 32);
        TW_FIRST_CODE.put("Z", 33);
        TW_FIRST_CODE.put("I", 34);
        TW_FIRST_CODE.put("O", 35);

        // 香港
        HK_FIRST_CODE.put("A", 1);
        HK_FIRST_CODE.put("B", 2);
        HK_FIRST_CODE.put("C", 3);
        HK_FIRST_CODE.put("R", 18);
        HK_FIRST_CODE.put("U", 21);
        HK_FIRST_CODE.put("Z", 26);
        HK_FIRST_CODE.put("X", 24);
        HK_FIRST_CODE.put("W", 23);
        HK_FIRST_CODE.put("O", 15);
        HK_FIRST_CODE.put("N", 14);
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param code 15位身份编码
     * @return 18位身份编码
     */
    public static String convert15To18(String code) {
        if (!isCode15(code)) {
            return code;
        }

        String code18 = code;
        if (isNumber(code)) {
            // 获取出生年月日
            String birthdayStr = code.substring(6, 12);
            Date birthday = parseDate15(birthdayStr);
            Calendar calendar = getCalendar();
            if (birthday != null)
                calendar.setTime(birthday);

            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(calendar.get(Calendar.YEAR));
            code18 = code.substring(0, 6) + sYear + code.substring(8);
            // 转换字符数组
            char[] chars = code18.toCharArray();
            if (chars != null) {
                int[] array = charToInt(chars);
                int sum17 = getPowerSum(array);
                // 获取校验位
                String sVal = getCheckCode18(sum17);
                if (sVal.length() > 0) {
                    code18 += sVal;
                } else {
                    return null;
                }
            }
        }
        return code18;
    }

    /**
     * 验证身份证是否合法
     */
    public static boolean validateCard(String code) {
        String card = code.trim();
        if (validateIdCard18(card)) {
            return true;
        }

        if (validateIdCard15(card)) {
            return true;
        }

        String[] cardArray = validateIdCard10(card);
        return notNull(cardArray) && "true".equals(cardArray[2]);
    }

    /**
     * 验证18位身份编码是否合法
     *
     * @param code 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard18(String code) {
        boolean result = false;
        if (isCode18(code)) {
            // 前17位
            String code17 = code.substring(0, 17);
            // 第18位
            String code18 = code.substring(17, CHINA_ID_MAX_LENGTH);
            if (isNumber(code17)) {
                char[] chars = code17.toCharArray();
                if (chars != null) {
                    int[] array = charToInt(chars);
                    int sum17 = getPowerSum(array);
                    // 获取校验位
                    String val = getCheckCode18(sum17);
                    if (val.length() > 0 && val.equalsIgnoreCase(code18)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param code 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard15(String code) {
        if (!isCode15(code))
            return false;

        if (!isNumber(code))
            return false;

        String provinceCode = code.substring(0, 2);
        if (PROVINCE_CODES.get(provinceCode) == null) {
            return false;
        }

        String sBirthday = code.substring(6, 12);
        Date birthday = parseYear(sBirthday.substring(0, 2));
        Calendar calendar = getCalendar();
        if (birthday != null)
            calendar.setTime(birthday);

        int year = calendar.get(Calendar.YEAR);
        int month = Integer.parseInt(sBirthday.substring(2, 4));
        int day = Integer.parseInt(sBirthday.substring(4, 6));
        return validateDate(year, month, day);

//		if (!validateDate(cal.get(Calendar.YEAR), 
//				Integer.parseInt(sBirthday.substring(2, 4)),
//				Integer.parseInt(sBirthday.substring(4, 6)))) {
//			return false;
//		}
//		return true;
    }

    /**
     * 验证10位身份编码是否合法
     *
     * @param code 身份编码
     * @return 身份证信息数组
     * <p>
     *  [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) \
     *  若不是身份证件号码则返回null
     * </p>
     */
    public static String[] validateIdCard10(String code) {
        String card = code.replaceAll("[\\(|\\)]", "");
        if (card.length() != 8 && card.length() != 9 && code.length() != 10) {
            return null;
        }

        String[] info = new String[3];
        if (code.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            String char2 = code.substring(1, 2);
            if (char2.equals("1")) {
                info[1] = "M";
            } else if (char2.equals("2")) {
                info[1] = "F";
            } else {
                info[1] = "N";
                info[2] = "false";
                return info;
            }
            info[2] = validateTWCard(code) ? "true" : "false";
        } else if (code.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
        } else if (code.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = validateHKCard(code) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证台湾身份证号码
     *
     * @param code 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateTWCard(String code) {
        String start = code.substring(0, 1);
        String mid = code.substring(1, 9);
        String end = code.substring(9, 10);
        int iStart = TW_FIRST_CODE.get(start);
        int sum = iStart / 10 + (iStart % 10) * 9;
        char[] chars = mid.toCharArray();
        int iflag = 8;
        for (char c : chars) {
            sum = sum + Integer.parseInt(String.valueOf(c)) * iflag;
            iflag--;
        }
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end);
        // return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true :
        // false;
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     *
     * @param code 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateHKCard(String code) {
        String card = code.replaceAll("[\\(|\\)]", "");
        int sum = 0;
        if (card.length() == 9) {
            sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9
                    + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        Integer iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.valueOf(c + "") * iflag;
            iflag--;
        }
        if (end.toUpperCase().equals("A")) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.valueOf(end);
        }
        return (sum % 11 == 0) ? true : false;
    }

    /**
     * 将字符数组转换成数字数组
     *
     * @param chars
     *            字符数组
     * @return 数字数组
     */
    public static int[] charToInt(char[] chars) {
        int length = chars.length;
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = Character.digit(chars[i], 10);
            // array[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        return array;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param array
     * @return 身份证编码。
     */
    private static int getPowerSum(int[] array) {
        int sum = 0;
        if (FACTORS.length == array.length) {
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < FACTORS.length; j++) {
                    if (i == j) {
                        sum = sum + array[i] * FACTORS[j];
                    }
                }
            }
        }
        return sum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param sum
     * @return 校验位
     */
    private static String getCheckCode18(int sum) {
        String lastCode;
        switch (sum % 11) {
            case 10:
                lastCode = "2";
                break;
            case 9:
                lastCode = "3";
                break;
            case 8:
                lastCode = "4";
                break;
            case 7:
                lastCode = "5";
                break;
            case 6:
                lastCode = "6";
                break;
            case 5:
                lastCode = "7";
                break;
            case 4:
                lastCode = "8";
                break;
            case 3:
                lastCode = "9";
                break;
            case 2:
                lastCode = "x";
                break;
            case 1:
                lastCode = "0";
                break;
            case 0:
                lastCode = "1";
                break;
            default:
                lastCode = "";
                break;
        }
        return lastCode;
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param code 身份编号
     * @return 年龄
     */
    public static int getAge(String code) {
        if (notMainland(code))
            return -1;

        code = convert15To18(code);
        String year = code.substring(6, 10);
        int currentYear = getCalendar().get(Calendar.YEAR);
        return currentYear - Integer.parseInt(year);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param code 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getCodeBirthday(String code) {
        if (notMainland(code)) {
            return null;
        }

        code = convert15To18(code);
        return code.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param code 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static Date getBirthday(String code) {
        String birthday = getCodeBirthday(code);
        return parseDate(birthday);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param code 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYear(String code) {
        if (code.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }

        code = convert15To18(code);
        return Short.valueOf(code.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param code 身份编号
     * @return 生日(MM)
     */
    public static Short getMonth(String code) {
        if (notMainland(code)) {
            return null;
        }

        code = convert15To18(code);
        return Short.valueOf(code.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param code 身份编号
     * @return 生日(dd)
     */
    public static Short getDay(String code) {
        if (notMainland(code)) {
            return null;
        }

        code = convert15To18(code);
        return Short.valueOf(code.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     *
     * @param code 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static char getGender(String code) {
        if (notMainland(code))
            return 'N';

        code = convert15To18(code);
        String genderCode = code.substring(16, 17);
        boolean man = Integer.parseInt(genderCode) % 2 != 0;
        return man ? 'M' : 'F';
    }

    /**
     * 根据身份编号获取性别
     *
     * @param code 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderForEN(String code) {
        char gender = getGender(code);
        return 'M' == gender ? "man" : 'F' == gender ? "woman" : "unknown";
    }

    /**
     * 根据身份编号获取性别
     *
     * @param code 身份编号
     * @return 性别(男，女，N-未知)
     */
    public static String getGenderForCN(String code) {
        char gender = getGender(code);
        return 'N' == gender ? "未知" : 'M' == gender ? "男" : "女";
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param code 身份编码
     * @return 省级编码
     */
    public static String getProvince(String code) {
        if (isMainland(code)) {
            String provinceCode = code.substring(0, 2);
            return PROVINCE_CODES.get(provinceCode);
        }
        return "unknown";
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param year 待验证日期(年)
     * @param month 待验证日期(月 1-12)
     * @param day 待验证日期(日)
     * @return 是否有效
     */
    public static boolean validateDate(int year, int month, int day) {
        if (isRangeOfYear(year) && isRangeOfMonth(month)) {
            int datePerMonth;
            switch (month) {
                case 4:
                case 6:
                case 9:
                case 11:
                    datePerMonth = 30;
                    break;
                case 2:
                    datePerMonth = isLeapYear(year) ? 29 : 28;
                    break;
                default:
                    datePerMonth = 31;
            }
            return (day >= 1) && (day <= datePerMonth);
        }
        return false;
    }

    /**
     * 解析出生日期
     *
     * @param source 出生日期的字符串
     * @return 返回时间
     */
    private static Date parseDate(String source) {
        if (source == null)
            return null;

        synchronized (BIRTHDAY_CACHE) {
            SimpleDateFormat sdf = BIRTHDAY_CACHE.get();
            if (sdf == null) {
                sdf = new SimpleDateFormat("yyyyMMdd");
                BIRTHDAY_CACHE.set(sdf);
            }
            try {
                return sdf.parse(source);
            } catch (ParseException ignore) {
            }
        }
        return null;
    }

    // yyMMdd
    /**
     * 解析出生日期
     *
     * @param source 出生日期的字符串
     * @return 返回时间
     */
    private static Date parseDate15(String source) {
        if (source == null)
            return null;

        synchronized (BIRTHDAY_CACHE15) {
            SimpleDateFormat sdf = BIRTHDAY_CACHE15.get();
            if (sdf == null) {
                sdf = new SimpleDateFormat("yyMMdd");
                BIRTHDAY_CACHE15.set(sdf);
            }
            try {
                return sdf.parse(source);
            } catch (ParseException ignore) {
            }
        }
        return null;
    }

    /**
     * 解析出生日期
     *
     * @param source 出生日期的字符串
     * @return 返回时间
     */
    private static Date parseYear(String source) {
        if (source == null)
            return null;

        synchronized (YEAR_CACHE) {
            SimpleDateFormat sdf = YEAR_CACHE.get();
            if (sdf == null) {
                sdf = new SimpleDateFormat("yy");
                YEAR_CACHE.set(sdf);
            }
            try {
                return sdf.parse(source);
            } catch (ParseException ignore) {
            }
        }
        return null;
    }

    /**
     * 数字验证
     *
     * @param code 提取的数字
     * @return 是否
     */
    private static boolean isNumber(CharSequence code) {
        int length = code.length();
        for (int i = 0; i < length; i++) {
            char c = code.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否来自大陆
     */
    private static boolean isMainland(CharSequence code) {
        return isCode15(code) || isCode18(code);
    }

    /**
     * 不是来自大陆
     */
    private static boolean notMainland(CharSequence code) {
        return code == null || code.length() < CHINA_ID_MIN_LENGTH || code.length() > CHINA_ID_MAX_LENGTH;
    }

    /**
     * 验证是否为位18位身份证号
     */
    private static boolean isCode18(CharSequence code) {
        return notNull(code) && code.length() == CHINA_ID_MAX_LENGTH;
    }

    /**
     * 验证是否为位15位身份证号
     */
    private static boolean isCode15(CharSequence code) {
        return notNull(code) && code.length() == CHINA_ID_MIN_LENGTH;
    }

    /**
     * 年份是否在范围之内
     *
     * @param year 判断的年份
     * @return 如果在范围之内，返回true，否则返回false
     */
    private static boolean isRangeOfYear(int year, int currentYear) {
        return year < MIN_YEAR || year > currentYear;
    }

    /**
     * 年份是否在范围之内
     *
     * @param year 判断的年份
     * @return 如果在范围之内，返回true，否则返回false
     */
    private static boolean isRangeOfYear(int year) {
        int currentYear = getCalendar().get(Calendar.YEAR);
        return isRangeOfYear(year, currentYear);
    }

    /**
     * 获取Calendar对象
     */
    private static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 年份是否在范围之内
     *
     * @param year 判断的年份
     * @return 如果在范围之内，返回true，否则返回false
     */
    private static boolean isRangeOfMonth(int month) {
        return month > 0 && month < 13;
    }

    /**
     * 是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static boolean notNull(Object o) {
        return o != null;
    }

}
