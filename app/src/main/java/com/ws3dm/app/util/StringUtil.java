package com.ws3dm.app.util;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc

/**
 * 描述：字符串处理类.
 */

public class StringUtil {

    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    public static String newMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 功能描述: MD5加密
     */
    public static String getMD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        String strMD5_32 = hexValue.toString();
//		return strMD5_32;
        return strMD5_32.toUpperCase();
    }

    public static String MD5(String string) {
        String key = "e8S8Ho0N25z78u6qn4kHyN";
        string = string + key;
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 功能描述: 判断email格式是否正确
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
//		第二种方法
//		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
//		Pattern p = Pattern.compile(str);
//		Matcher m = p.matcher(email);
//		return m.matches();
    }

    /**
     * 判断输入是否包含数字和字母混合
     */
    public static boolean isCharAndNum(String input) {
        String regex = "^[a-z0-9A-Z]+$";
        return input.matches(regex);
    }

    /**
     * 判断字数不多于150字
     *
     * @param mobiles
     * @return
     */
    public static boolean isValidLength(String mobiles) {
        Pattern p = Pattern.compile("/^.{1,150}$/");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 设置优惠价格样式
     *
     * @param price
     * @param textview
     * @return void
     */
    public static void setTextViewStyle(String price, TextView textview) {
        textview.setText(price);
        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * @Function：在字符串中间添加空格
     */
    public static String insertSpace(String target, int septum) {
        String regex = "(.{" + septum + "})";
        target = target.replaceAll(regex, "$1 ");
//        System.out.println (temp);

        return target;
    }

    /**
     * 保留2位小数
     *
     * @param f
     * @return
     */
    public static String Keep2DecimalPlaces(double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(f);
        return s;
    }

    /**
     * 保留1位小数
     *
     * @return
     */
    public static String Keep1DecimalPlaces(String s) {
        DecimalFormat df = new DecimalFormat("0.0");
        String p = df.format(Float.parseFloat(s));
        return p;
    }

    /**
     * 至少显示2位数，5->05
     *
     * @return
     */
    public static String Keep2DecimalPlaces(int s) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(s);
//		另一种方法：String.format("%02d",5)
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        int forSize = c.length;
        for (int i = 0; i < forSize; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    //取消字符串中的换行
    public static String LineFeed(String line) {
        if (line == null) {
            return "";
        }
        while (line.contains("\n")) {
            line = line.replace("\n", "");
        }
        return line;
    }

    public static String filterHtml(String content) {
        return content.replaceAll("</?[^<]+>", "").replaceAll("\\s*|\t|\r|\n", "");
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if (!isEmpty(str)) {
            int forSize = str.length();
            for (int i = 0; i < forSize; i++) {
                /* 获取一个字符 */
                String temp = str.substring(i, i + 1);
                /* 判断是否为中文字符 */
                if (temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }
        return valueLength;
    }

    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            int forSize = str.length();
            for (int i = 0; i < forSize; i++) {
                //获取一个字符
                String temp = str.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                    //中文字符长度为2
                    valueLength += 2;
                } else {
                    //其他字符长度为1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 描述：获取指定长度的字符所在位置.
     *
     * @param str  指定的字符串
     * @param maxL 要取到的长度（字符长度，中文字符计2个）
     * @return 字符的所在位置
     */
    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        int forSize = str.length();
        for (int i = 0; i < forSize; i++) {
            //获取一个字符
            String temp = str.substring(i, i + 1);
            //判断是否为中文字符
            if (temp.matches(chinese)) {
                //中文字符长度为2
                valueLength += 2;
            } else {
                //其他字符长度为1
                valueLength += 1;
            }
            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    /**
     * 严格的验证是否是手机号
     *
     * @param phoneNum
     * @return boolean
     */
    public static boolean isMobieNumber(String phoneNum) {
        // 验证电话的正则表达式
        String format = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0,6,7,8])|(14[5,7,8]))\\d{8}$";
        Pattern p = Pattern.compile(format);
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    /**
     * Describution : 隐藏手机号
     * <p>
     * Author : DKjuan
     * <p>
     * Date : 2018/11/27 12:03
     **/
    public static String hidePhone(String phoneNum) {
        return phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 描述：是否只是数字.
     *
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * 描述：是否是中文.
     *
     * @param str 指定的字符串
     * @return 是否是中文:是为true，否则false
     */
    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            int forSize = str.length();
            for (int i = 0; i < forSize; i++) {
                //获取一个字符
                String temp = str.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                } else {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                //获取一个字符
                String temp = str.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                    isChinese = true;
                } else {

                }
            }
        }
        return isChinese;
    }

    /**
     * 描述：从输入流中获得String.
     *
     * @param is 输入流
     * @return 获得的String
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            //最后一个\n删除
            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    the str
     * @param length 指定字节长度
     * @return 截取后的字符串
     */
    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    文本
     * @param length 字节长度
     * @param dot    省略符号
     * @return 截取后的字符串
     */
    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if (strBLen <= length) {
            return str;
        }
        int temp = 0;
        StringBuffer sb = new StringBuffer(length);
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append(c);
            if (c > 256) {
                temp += 2;
            } else {
                temp += 1;
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot);
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 描述：截取字符串从第一个指定字符.
     *
     * @param str1   原文本
     * @param str2   指定字符
     * @param offset 偏移的索引
     * @return 截取后的字符串
     */
    public static String cutStringFromChar(String str1, String str2, int offset) {
        if (isEmpty(str1)) {
            return "";
        }
        int start = str1.indexOf(str2);
        if (start != -1) {
            if (str1.length() > start + offset) {
                return str1.substring(start + offset);
            }
        }
        return "";
    }

    /**
     * 描述：获取字节长度.
     *
     * @param str     文本
     * @param charset 字符集（GBK）
     * @return the int
     */
    public static int strlen(String str, String charset) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int length = 0;
        try {
            length = str.getBytes(charset).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取大小的描述.
     *
     * @param size 字节个数
     * @return 大小的描述
     */
    public static String getSizeDesc(long size) {
        String suffix = "B";
        if (size >= 1024) {
            suffix = "K";
            size = size >> 10;
            if (size >= 1024) {
                suffix = "M";
                //size /= 1024;
                size = size >> 10;
                if (size >= 1024) {
                    suffix = "G";
                    size = size >> 10;
                    //size /= 1024;
                }
            }
        }
        return size + suffix;
    }

    /**
     * 描述：ip地址转换为10进制数.
     *
     * @param ip the ip
     * @return the long
     */
    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 返回带有emoji的字符串
     *
     * @return
     */
    public static String unmaskEmoji(String input) {
        if (input == null) {
            return input;
        }

        Pattern patternLong = Pattern.compile("\\[emoji:[a-zA-Z0-9]{8}\\]");
        Matcher matcherLong = patternLong.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcherLong.find()) {
            String str = matcherLong.group();
            matcherLong.appendReplacement(sb, convertString2Unicode(str.substring(7, str.length() - 1)));
        }
        matcherLong.appendTail(sb);
        String longEmoji = sb.toString();

        Pattern patternShort = Pattern.compile("\\[emoji:[a-zA-Z0-9]{4}\\]");
        Matcher matcherShort = patternShort.matcher(longEmoji);
        sb = new StringBuffer();
        while (matcherShort.find()) {
            String str = matcherShort.group();
            matcherShort.appendReplacement(sb, convertString2Unicode(str.substring(7, str.length() - 1)));
        }
        matcherShort.appendTail(sb);
        return sb.toString();
    }

    public static String convertString2Unicode(String input) {
        if (input == null) {
            return input;
        }
        if (input.length() != 4 && input.length() != 8) {
            return input;
        }
        char[] chars = new char[input.length() / 4];
        String high = input.substring(0, 4);
        chars[0] = (char) Integer.decode("0x" + high).intValue();
        if (input.length() == 8) {
            String low = input.substring(4, 8);
            chars[1] = (char) Integer.decode("0x" + low).intValue();
        }
        return new String(chars);
    }

    /**
     * 限制EditText回车换行
     *
     * @param et
     */
    public static void LimitsEditEnter(EditText et) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 判断str1中包含str2的个数
     *
     * @param str1
     * @param str2
     * @return counter
     */
    static int counter = 0;

    public static int countStr(String str1, String str2) {
        counter = 0;
        return subCount(str1, str2);
    }

    static int subCount(String str1, String str2) {
        if (str1.indexOf(str2) == -1) {
            return 0;
        } else if (str1.indexOf(str2) != -1) {
            counter++;
            subCount(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }

    /**
     * 获取标签类型
     */
    public static String getShowType(int type) {
        if (type == 1) {
            return "新闻";
        }
        if (type == 2) {
            return "攻略";
        }
        if (type == 3) {
            return "单机专区";
        }
        if (type == 4) {
            return "手游下载";
        }
        if (type == 5) {
            return "杂谈";
        }
        if (type == 6) {
            return "评测";
        }
        if (type == 7) {
            return "原创";
        }
        if (type == 8) {
            return "安利";
        }
        if (type == 9) {
            return "礼包";
        }
        if (type == 10) {
            return "视频";
        }
        if (type == 11) {
            return "专栏";
        }
        if (type == 12) {
            return "节目";
        }
        if (type == 13) {
            return "投票";
        }
        if (type == 14) {
            return "专题H5链接";
        }
        if (type == 15) {
            return "H5游戏";
        }
        if (type == 16) {
            return "原创作者";
        }
        if (type == 17) {
            return "单机专区攻略";
        }
        if (type == 18) {
            return "手游专区攻略";
        }
        if (type == 19) {
            return "网游专区攻略";
        }
        if (type == 20) {
            return "手游专区";
        }
        if (type == 21) {
            return "网游专区";
        }
        if (type == 22) {
            return "网游礼包";
        }
        if (type == 23) {
            return "网游电竞专题";
        }
        return "超出类型";
    }
}
