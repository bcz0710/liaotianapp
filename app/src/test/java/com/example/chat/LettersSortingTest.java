package com.example.chat.util;

import com.example.chat.bean.User;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class LettersSortingTest {

    private LettersSorting lettersSorting;

    @Before
    public void setUp() {
        // 初始化 LettersSorting
        lettersSorting = new LettersSorting();
    }

    @Test
    public void testCompare_LetterSorting() {
        // 创建一些 User 对象，传入所有需要的参数
        User user1 = new User(1, "alice123", "password", "Alice", "female", "photo1.jpg", "C");
        User user2 = new User(2, "bob456", "password", "Bob", "male", "photo2.jpg", "A");
        User user3 = new User(3, "charlie789", "password", "Charlie", "male", "photo3.jpg", "B");

        // 将这些 User 对象放入一个 List 中
        List<User> users = Arrays.asList(user1, user2, user3);

        // 对 List 进行排序
        users.sort(lettersSorting);

        // 检查排序后的结果是否正确
        assertTrue(users.get(0).getLetter().equals("A"));
        assertTrue(users.get(1).getLetter().equals("B"));
        assertTrue(users.get(2).getLetter().equals("C"));
    }
}
