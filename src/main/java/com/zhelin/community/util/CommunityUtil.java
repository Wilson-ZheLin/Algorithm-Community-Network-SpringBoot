package com.zhelin.community.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class CommunityUtil {

    // Generate random string
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // MD5 encryption
    public static String md5(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
