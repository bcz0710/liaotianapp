package com.example.chat.util;

import java.util.HashMap;
import java.util.Map;

public class Trans2PinYin {

    private static final int[] pyvalue = new int[]{/* values omitted for brevity */};
    private static final String[] pystr = new String[]{/* values omitted for brevity */};

    private static final Trans2PinYin INSTANCE = new Trans2PinYin();
    private static final Map<Integer, String> asciiToPinyinMap = new HashMap<>();
    private String resource;

    // Static block to initialize the ASCII to Pinyin map for faster lookups
    static {
        for (int i = 0; i < pyvalue.length; i++) {
            asciiToPinyinMap.put(pyvalue[i], pystr[i]);
        }
    }

    private Trans2PinYin() {
        // Private constructor to prevent instantiation
    }

    public static Trans2PinYin getInstance() {
        return INSTANCE;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 获取汉字的 ASCII 编码
     *
     * @param chs 汉字
     * @return ASCII 编码
     */
    private int getChsAscii(String chs) {
        try {
            byte[] bytes = chs.getBytes("gb2312");
            if (bytes.length == 1) { // 英文字符
                return bytes[0];
            } else if (bytes.length == 2) { // 中文字符
                int highByte = bytes[0] & 0xFF;
                int lowByte = bytes[1] & 0xFF;
                return (highByte << 8) + lowByte - 0xA000;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid character: " + chs, e);
        }
        return -1; // 非法字符返回-1
    }

    /**
     * 转换单个汉字
     *
     * @param str 单个汉字
     * @return 拼音
     */
    public String convert(String str) {
        int ascii = getChsAscii(str);
        if (ascii < 0) return str; // 非法字符直接返回原字符

        // 查找拼音
        return asciiToPinyinMap.getOrDefault(ascii, str); // 默认返回原字符
    }

    /**
     * 转换多个汉字为拼音
     *
     * @param str 输入的汉字字符串
     * @return 拼音
     */
    public String convertAll(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            result.append(convert(str.substring(i, i + 1)));
        }
        return result.toString();
    }

    public String getSpelling() {
        return convertAll(this.resource);
    }

    /**
     * 将汉字字符串转换为拼音
     *
     * @param str 输入的汉字字符串
     * @return 拼音
     */
    public static String trans2PinYin(String str) {
        return getInstance().convertAll(str);
    }
}
