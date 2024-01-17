package com.zhelin.community.controller;

import com.zhelin.community.annotation.LoginRequired;
import com.zhelin.community.entity.User;
import com.zhelin.community.service.UserService;
import com.zhelin.community.util.CommunityUtil;
import com.zhelin.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if(headerImage == null) {
            model.addAttribute("error", "You have to select an image to upload.");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "The file must have an extension.");
            return "/site/setting";
        }

        // Generate a random filename
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + headerImage.getOriginalFilename(), e);
        }

        // Update the avatar
        User user = hostHolder.getUser();
        int userId = user.getId();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(userId, headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        try(FileInputStream fis = new FileInputStream(fileName);
            OutputStream os = response.getOutputStream()) {
            response.setContentType("image/" + suffix);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("Failed to read header image: " + fileName, e);
        }
    }

    @LoginRequired
    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public String changePassword(String oldPassword, String newPassword, Model model) {
        if(StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg", "New password cannot be empty");
            return "/site/setting";
        }

        User user = hostHolder.getUser();
        int userId = user.getId();
        String oldMd5 = CommunityUtil.md5(oldPassword + user.getSalt());
        if(!oldMd5.equals(user.getPassword())) {
            model.addAttribute("incorrectOldPassword", "The old password is incorrect");
            return "/site/setting";
        }
        String newMd5 = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(userId, newMd5);

        return "redirect:/index";
    }

}
