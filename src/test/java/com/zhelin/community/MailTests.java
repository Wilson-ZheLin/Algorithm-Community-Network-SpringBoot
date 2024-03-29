package com.zhelin.community;

import com.zhelin.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("lin.zhe3@northeastern.edu", "TEST", "Welcome");
    }

    @Test
    public void testHTMLMail() {
        Context context = new Context();
        context.setVariable("username", "lin.zhe3@northeastern.edu");
        String content = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("lin.zhe3@northeastern.edu", "HTML EMAIL TEST", content);
    }
}
