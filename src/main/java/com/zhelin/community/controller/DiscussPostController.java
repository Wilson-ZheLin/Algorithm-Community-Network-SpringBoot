package com.zhelin.community.controller;

import com.zhelin.community.entity.DiscussPost;
import com.zhelin.community.entity.User;
import com.zhelin.community.service.DiscussPostService;
import com.zhelin.community.service.UserService;
import com.zhelin.community.util.CommunityUtil;
import com.zhelin.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(path="/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if(user == null) {
            return CommunityUtil.getJSONString(403, "You have not logged in, please log in first!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        return CommunityUtil.getJSONString(0, "Publish success!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int id, Model model) {
        DiscussPost post = discussPostService.findDiscussPostById(id);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        return "site/discuss-detail";
    }
}
