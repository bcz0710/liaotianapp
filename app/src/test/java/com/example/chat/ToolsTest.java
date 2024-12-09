package com.example.chat.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToolsTest {

    @Test
    public void testIsPasswordValid() {
        // 测试符合条件的密码
        assertTrue(Tools.isPassword("abc12345"));   // 字母和数字组合
        assertTrue(Tools.isPassword("A1b2C3d4"));  // 混合大小写和数字

        // 测试不符合条件的密码
        assertFalse(Tools.isPassword("12345678")); // 纯数字
        assertFalse(Tools.isPassword("abcdefgh")); // 纯字母
        assertFalse(Tools.isPassword("abc12"));    // 长度不足
        assertFalse(Tools.isPassword("abc12345678901234")); // 长度超出
    }

    @Test
    public void testIsNameValid() {
        // 测试符合条件的昵称
        assertTrue(Tools.isName("user123"));  // 字母和数字组合
        assertTrue(Tools.isName("a1b2"));     // 简单混合

        // 测试不符合条件的昵称
        assertFalse(Tools.isName("12345678")); // 纯数字
        assertFalse(Tools.isName("username")); // 纯字母
        assertFalse(Tools.isName("user12345")); // 长度超出限制
        assertFalse(Tools.isName(""));          // 空字符串
    }
}
