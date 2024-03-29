package com.zhelin.community;

import com.zhelin.community.dao.LoginTicketMapper;
import com.zhelin.community.dao.UserMapper;
import com.zhelin.community.dao.DiscussPostMapper;
import com.zhelin.community.entity.DiscussPost;
import com.zhelin.community.entity.LoginTicket;
import com.zhelin.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("zhelin");
        System.out.println(user);

        user = userMapper.selectByEmail("lin.zhe3@northeastern.edu");
        System.out.println(user);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10, 0);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testFindPosyByUser() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPostByUser(101);
        for(DiscussPost post : list) {
            System.out.println(post);
        }
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(101);
        ticket.setTicket("abc");
        ticket.setStatus(0);
        ticket.setExpired(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(ticket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket ticket = loginTicketMapper.selectByTicket("abc");
        System.out.println(ticket);

        loginTicketMapper.updateStatus("abc", 1);
        ticket = loginTicketMapper.selectByTicket("abc");
        System.out.println(ticket);
    }

}
