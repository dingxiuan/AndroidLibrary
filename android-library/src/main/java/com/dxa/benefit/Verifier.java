package com.dxa.benefit;

/**
 * 数据校验
 */
public final class Verifier {

    /**
     * 验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";
    /**
     * 验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
    /**
     * 验证邮箱
     */
//    String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 验证邮箱
     */
    public static final String REGEX_EMAIL2 = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    /**
     * 验证字母或数字
     */
    public static final String REGEX_LETTER_NUMER = "^[A-Za-z0-9]+$";
    /**
     * 验证数字
     */
    public static final String REGEX_NUMBER = "^[0-9]+$";
    /**
     * 验证字母
     */
    public static final String REGEX_LETTER = "^[A-Za-z]+$";
    /**
     * 验证是否只有中文
     */
    public static final String REGEX_CHINESE = "^[\u0391-\uFFE5]+$";
    // String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 是否为邮编码
     */
    public static final String REGEX_POST_CODE = "^[0-9]{6,10}";
    /**
     * 验证是否为身份证号
     */
    public static final String REGEX_ID_CARD = "^[0-9]{17}[0-9xX]$";
    // String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
//    /**
//     * 验证手机号
//     */
//    String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    /**
     * 验证IP地址
     */
    public static final String REGEX_IP_ADDRESS = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    /**
     * 验证月份
     */
    public static final String REGEX_MONTH = "^(0?[[1-9]|1[0-2])$";

    private Verifier() {
    }

    /**
     * 验证
     *
     * @param source 数据
     * @param regex 正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String source, String regex) {
        return source.matches(regex);
    }

    /**
     * 邮箱验证
     *
     * @param email 可能是Email的字符串
     * @return 是否是Email
     */
    public static boolean isEmail(String email) {
        return email.matches(REGEX_EMAIL);
    }

    /**
     * 邮箱检测
     *
     * @param email 可能是Email的字符串
     * @return 是否是Email
     */
    public static boolean isEmail2(String email) {
        return email.matches(REGEX_EMAIL2);
    }

    /**
     * 是否是手机号(对部分新手机号不管用，建议不要使用)
     */
    @Deprecated
    public static boolean isMobile(String mobile) {
        /**
         * 手机号码:
         * 13[0-9], 14[5,7, 9], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[0-9], 18[0-9]
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         */
//	    String MOBILE = "^1(3[0-9]|4[579]|5[0-35-9]|7[0-9]|8[0-9])\\d{8}$";

        // 中国移动：China Mobile:
        // 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
//	    String CM = "^1(3[4-9]|4[7]|5[0-27-9]|7[08]|8[2-478])\\d{8}$";

        // 中国联通：China Unicom: 130,131,132,145,155,156,170,171,175,176,185,186
//	    String CU = "^1(3[0-2]|4[5]|5[56]|7[0156]|8[56])\\d{8}$";

        // 中国电信：China Telecom: 133,149,153,170,173,177,180,181,189
//	    String CT = "^1(3[3]|4[9]|53|7[037]|8[019])\\d{8}$";

//		boolean isMobile = matches(mobile, MOBILE)
//			|| matches(mobile, CM)
//			|| matches(mobile, CU)
//			|| matches(mobile, CT);

        return matches(mobile, "^1(3[0-9]|4[579]|5[0-35-9]|7[0-9]|8[0-9])\\d{8}$") // 公用
                || matches(mobile, "^1(3[4-9]|4[7]|5[0-27-9]|7[08]|8[2-478])\\d{8}$") // 移动
                || matches(mobile, "^1(3[0-2]|4[5]|5[56]|7[0156]|8[56])\\d{8}$") // 联通
                || matches(mobile, "^1(3[3]|4[9]|53|7[037]|8[019])\\d{8}$"); // 电信
    }

//    /**
//     * 是否为手机号
//     *
//     * @param mobile 手机号
//     * @return 返回是否为手机号
//     */
//    public static boolean isMobile2(String mobile) {
////    	isNumber2(mobile);
//    }
//
    /**
     * 只含字母和数字
     *
     * @param numOrLetter 可能只包含字母和数字的字符串
     * @return 是否只包含字母和数字
     */
    public static boolean isNumberOrLetter(String numOrLetter) {
        return numOrLetter.matches(REGEX_LETTER_NUMER);
    }

    /**
     * 只含数字
     *
     * @param number 可能只包含数字的字符串
     * @return 是否只包含数字
     */
    public static boolean isNumber(String number) {
        return number.matches(REGEX_NUMBER);
    }

    /**
     * 只含数字
     *
     * @param number 可能只包含数字的字符串
     * @return 是否只包含数字
     */
    public static boolean isNumber2(CharSequence number) {
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    /**
     * 只含字母
     *
     * @param letter 可能只包含字母的字符串
     * @return 是否只包含字母
     */
    public static boolean isLetter(String letter) {
        return letter.matches(REGEX_LETTER);
    }

    /**
     * 只是中文
     *
     * @param chinese 可能是中文的字符串
     * @return 是否只是中文
     */
    public static boolean isChinese(String chinese) {
        return chinese.matches(REGEX_CHINESE);
    }

    /**
     * 包含中文
     *
     * @param chinese 可能包含中文的字符串
     * @return 是否包含中文
     */
    public static boolean isContainChinese(String chinese) {
        String regex = "[\u0391-\uFFE5]";
        if (isEmptyAndNotNull(chinese)) {
            for (int i = 0; i < chinese.length(); i++) {
                if (chinese.substring(i, i + 1).matches(regex)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 小数点位数
     *
     * @param data   可能包含小数点的字符串
     * @param length 小数点后的长度
     * @return 是否小数点后有length位数字
     */
    public static boolean isDianWeiShu(String data, int length) {
        String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
        return data.matches(expr);
    }

    /**
     * 身份证号码验证
     *
     * @param code 可能是身份证号码的字符串
     * @return 是否是身份证号码
     */
    public static boolean isCard(String code) {
        return code.matches(REGEX_ID_CARD);
    }

    /**
     * 邮政编码验证
     *
     * @param code 可能包含邮政编码的字符串
     * @return 是否是邮政编码
     */
    public static boolean isPostCode(String code) {
        return code.matches(REGEX_POST_CODE);
    }

    /**
     * 验证密码长度
     * @param password 密码
     * @param min 最小长度
     * @param max 最大长度
     * @return 是否在规定的长度范围内
     */
    public static boolean isPasswordLength(String password, int min, int max) {
        return password.matches("^\\d{" + min + "," + max + "}$");
    }

    /**
     * 验证月份
     *
     * @param month 月份
     * @return 是否为月份
     */
    public static boolean isMonth(String month) {
        return month.matches(REGEX_MONTH);
    }

    /**
     * 长度验证
     *
     * @param cs   源字符串
     * @param length 期望长度
     * @return 是否是期望长度
     */
    public static boolean isLength(CharSequence cs, int length) {
        return cs != null && cs.length() == length;
    }

    /**
     * 是否非null且为空
     */
    public static boolean isEmptyAndNotNull(String s) {
        return s != null && s.isEmpty();
    }

}
